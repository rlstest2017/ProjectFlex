package com.orange.meetingroom.business.connector.utils;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.orange.meetingroom.connector.exception.MeetingRoomInternalServerException;
import com.orange.meetingroom.connector.exception.MethodNotAllowedException;
import com.orange.meetingroom.connector.exception.PhpInternalServerException;
import com.orange.meetingroom.connector.flexoffice.enums.EnumMeetingRoomStatus;
import com.orange.meetingroom.connector.flexoffice.enums.EnumSystemInMap;
import com.orange.meetingroom.connector.flexoffice.model.request.MeetingRoomData;
import com.orange.meetingroom.connector.php.PhpConnectorClient;
import com.orange.meetingroom.connector.php.model.request.UpdateBookingParameters;
import com.orange.meetingroom.connector.php.model.response.BookingConnectorReturn;
import com.orange.meetingroom.connector.php.model.response.MeetingRoomConnectorReturn;

/**
 * MeetingRoomInfoTools
 * @author oab
 *
 */
@Component
public class MeetingRoomInfoTools {
	
	private static final Logger LOGGER = Logger.getLogger(MeetingRoomInfoTools.class);
	static final String FORMAT_JSON = "json";
	static final String DEFAULT_CONTENT_SUBJECT_EN = "Booking";
	static final String DEFAULT_CONTENT_SUBJECT_FR = "Delegate";
	static final String DEFAULT_ORGANIZER = "delegate";
	static final String DEFAULT_BOOK_FROM_AGENT_OR_DASHBOARD_EN = "Quick";
	static final String DEFAULT_BOOK_FROM_AGENT_OR_DASHBOARD_FR = "automatique";
	
	@Autowired
	ConfHashMapFactoryBean confHashMapFactoryBean; 
	@Autowired
	PhpConnectorClient phpConnector;
	@Autowired
	DateTools dateTools;
	
	Integer ackTime = 0;
	
	/**
	 * processMeetingRoomStatus
	 * @param mmetingroomreturn
	 * @return
	 */
	public MeetingRoomData processMeetingRoomStatus(MeetingRoomConnectorReturn metingroomreturn) {
		try {
			Integer currentDate = metingroomreturn.getCurrentDate();
			String meetingRoomExternalId = metingroomreturn.getMeetingRoom().getMeetingRoomDetails().getMeetingRoomExternalId();
			List<BookingConnectorReturn> bookings = metingroomreturn.getMeetingRoom().getBookings();
			
			MeetingRoomData data = new MeetingRoomData();
			data.setMeetingRoomStatus(EnumMeetingRoomStatus.FREE);
			data.setMeetingRoomExternalId(meetingRoomExternalId);
			
			setAckTime(); // set AckTime properties from Config hashMap from DataBase System Table
			
			for (BookingConnectorReturn book : bookings) {
				Boolean compareDates1 = dateTools.isTime1BeforeOrEqualsTime2(book.getStartDate(), currentDate, 0);
				Boolean compareDates2 = dateTools.isTime1BeforeOrEqualsTime2(currentDate, book.getEndDate(), 0);
				if ((compareDates1) && (compareDates2)) { // currentDate of a booking is between startDate and endDate
					updateInfos(book, data, meetingRoomExternalId, currentDate);
					break;
				} 
			}
			
			return data;
		} catch (RuntimeException e ) {
			LOGGER.debug("RuntimeException in processMeetingRoomStatus(...) method. ", e);
			return null;
		}
	}
	
	/**
	 * updateInfos
	 * @param book BookingConnectorReturn
	 * @param data MeetingRoomData
	 */
	private void updateInfos(BookingConnectorReturn book, MeetingRoomData data, String meetingRoomExternalId, Integer currentDate) {
		if (ackTime == 0) {
			data.setMeetingRoomStatus(EnumMeetingRoomStatus.OCCUPIED);
			setMeetingRoomData(book, data, meetingRoomExternalId);
		} else { // (ackTime != 0)
			if (book.getAcknowledged()) { // acknowledged = true
				data.setMeetingRoomStatus(EnumMeetingRoomStatus.OCCUPIED);
				setMeetingRoomData(book, data, meetingRoomExternalId);
			} else { // acknowledged = false
				Boolean compareDates = dateTools.isTime1BeforeOrEqualsTime2(book.getStartDate(), currentDate, ackTime);
				if (compareDates) { // startDate + ackTime and currentDate of a booking
					// exceeding ack Time
					try {
						// cancel book
						UpdateBookingParameters params = new UpdateBookingParameters(); 
						params.setRoomID(meetingRoomExternalId);
						params.setIdReservation(book.getIdReservation());
						params.setRevisionReservation(book.getRevisionReservation());
						params.setEndDate(currentDate.toString()); // only endDate to cancel request !!!
						params.setFormat(FORMAT_JSON);
						phpConnector.updateBooking(params);
						data.setMeetingRoomStatus(EnumMeetingRoomStatus.FREE);
					} catch (MeetingRoomInternalServerException | MethodNotAllowedException	| PhpInternalServerException e) {
						LOGGER.debug("Exception when trying to cancel reservation, with message:" + e.getMessage(), e);
						// keep it in ack status
						data.setMeetingRoomStatus(EnumMeetingRoomStatus.ACK);
						setMeetingRoomData(book, data, meetingRoomExternalId);
					}
				} else {
					// still in ack time
					data.setMeetingRoomStatus(EnumMeetingRoomStatus.ACK);
					setMeetingRoomData(book, data, meetingRoomExternalId);
				}
			}
		}
	}
	
	/**
	 * setMeetingRoomData
	 * @param book BookingConnectorReturn
	 * @param data MeetingRoomData
	 * @param meetingRoomExternalId
	 */
	private void setMeetingRoomData(BookingConnectorReturn book, MeetingRoomData data, String meetingRoomExternalId) {
		data.setStartDate(book.getStartDate());
		data.setEndDate(book.getEndDate());
		data.setMeetingRoomExternalId(meetingRoomExternalId);
		data.setOrganizerLabel(processOrganizerLabel(book));
	}
	
	/**
	 * getAckTime
	 * @return
	 */
	private Integer setAckTime() {
		Map<String, Integer> configMap = confHashMapFactoryBean.getObject();
		if (configMap.containsKey(EnumSystemInMap.ACK_TIME.toString())) {
			this.ackTime = configMap.get(EnumSystemInMap.ACK_TIME.toString());
		}
		return this.ackTime;
	}
	
	/**
	 * processOrganizerLabel
	 * @param book
	 * @return
	 */
	private String processOrganizerLabel(BookingConnectorReturn book) {
		String label = null;
		String subject = book.getSubject().trim();
		
		if (!subject.isEmpty()) {
			String[] subjectArray = subject.split("-");
			if (subjectArray != null) {
				String sub = subjectArray[0];
				for (int i=1; i< subjectArray.length; i++) {
					if (!subjectArray[i].contains(DEFAULT_BOOK_FROM_AGENT_OR_DASHBOARD_EN) && !subjectArray[i].contains(DEFAULT_BOOK_FROM_AGENT_OR_DASHBOARD_FR)) {
						sub = sub +"-"+subjectArray[i];
					}
				}
				if (sub.contains(DEFAULT_CONTENT_SUBJECT_EN) || sub.contains(DEFAULT_CONTENT_SUBJECT_FR)) { 
					// ex : Booking Agent - Quick Booking
					// ex : Delegate - Agent de réservation automatique
					String organizer = book.getOrganizerFullName();
					if (!organizer.contains(DEFAULT_ORGANIZER)) {
						label = organizer.trim();
					} 
				} else { // ex : rachid - Quick Booking
					label = sub.trim(); 
				}
			}
		} else {
			String organizer = book.getOrganizerFullName();
			if (!organizer.contains(DEFAULT_ORGANIZER)) {
				label = organizer.trim();
			} 
		}
		
		return label;
	}

	

}
