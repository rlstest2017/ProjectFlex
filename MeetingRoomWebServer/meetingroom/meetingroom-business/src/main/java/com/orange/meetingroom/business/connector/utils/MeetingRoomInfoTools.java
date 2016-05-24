package com.orange.meetingroom.business.connector.utils;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.orange.meetingroom.connector.flexoffice.enums.EnumMeetingRoomStatus;
import com.orange.meetingroom.connector.flexoffice.model.request.MeetingRoomData;
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
	
	@Autowired
	DateTools dateTools;
	
	/**
	 * processMeetingRoomStatus
	 * @param mmetingroomreturn
	 * @return
	 */
	public MeetingRoomData processMeetingRoomStatus(MeetingRoomConnectorReturn metingroomreturn) {
		
		try {
			List<BookingConnectorReturn> bookings = metingroomreturn.getMeetingRoom().getBookings();
			
			MeetingRoomData data = new MeetingRoomData();
			data.setMeetingRoomStatus(EnumMeetingRoomStatus.FREE);
			
			for (BookingConnectorReturn book : bookings) {
				Boolean compareDates = dateTools.isTime1BeforeTime2(book.getStartDate(), metingroomreturn.getCurrentDate(), 0);
				if (compareDates) { // currentDate est dans un booking
					// TODO updateInfos();
					break;
				} 
			}
			
			return data;
		} catch (RuntimeException e ) {
			LOGGER.debug("RuntimeException in processMeetingRoomStatus(...) method");
			return null;
		}
	}

}
