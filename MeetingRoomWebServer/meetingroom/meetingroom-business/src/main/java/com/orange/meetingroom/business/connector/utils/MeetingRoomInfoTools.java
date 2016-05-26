package com.orange.meetingroom.business.connector.utils;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.orange.meetingroom.connector.exception.MeetingRoomInternalServerException;
import com.orange.meetingroom.connector.exception.MethodNotAllowedException;
import com.orange.meetingroom.connector.exception.PhpInternalServerException;
import com.orange.meetingroom.connector.flexoffice.enums.EnumMeetingRoomStatus;
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
	@Autowired
	PhpConnectorClient phpConnector;
	@Autowired
	DateTools dateTools;
	// TODO get ackTime from /system request
	Integer ackTime = 2;
	
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
			
			for (BookingConnectorReturn book : bookings) {
				Boolean compareDates = dateTools.isTime1BeforeTime2(book.getStartDate(), currentDate, 0);
				if (compareDates) { // startDate and currentDate of a booking
					updateInfos(book, data, meetingRoomExternalId, currentDate);
					break;
				} 
			}
			
			return data;
		} catch (RuntimeException e ) {
			LOGGER.debug("RuntimeException in processMeetingRoomStatus(...) method");
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
				Boolean compareDates = dateTools.isTime1BeforeTime2(book.getStartDate(), currentDate, ackTime);
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
						LOGGER.debug("Exception when trying to cancel reservation, with message:" + e.getMessage());
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
		data.setOrganizerLabel(book.getOrganizerFullName());
		data.setMeetingRoomExternalId(meetingRoomExternalId);
	}

}
