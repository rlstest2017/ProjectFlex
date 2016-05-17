package com.orange.meetingroom.connector.php.model.response;

import java.util.ArrayList;
import java.util.List;

/**
 * MeetingRoomBookings
 * @author oab
 *
 */
public class MeetingRoomBookingsConnectorReturn {

	MeetingRoomDetailsConnectorReturn meetingRoomDetails;
	List<BookingConnectorReturn> bookings;
	
	/**
	 * @return the meetingRoomDetails
	 */
	public MeetingRoomDetailsConnectorReturn getMeetingRoomDetails() {
		return meetingRoomDetails;
	}
	/**
	 * @param meetingRoomDetails the meetingRoomDetails to set
	 */
	public void setMeetingRoomDetails(MeetingRoomDetailsConnectorReturn meetingRoomDetails) {
		this.meetingRoomDetails = meetingRoomDetails;
	}
	/**
	 * @return the bookings
	 */
	public List<BookingConnectorReturn> getBookings() {
		if (bookings == null) {
			bookings = new ArrayList<BookingConnectorReturn>();
		}
		return bookings;
	}
	/**
	 * @param bookings the bookings to set
	 */
	public void setBookings(List<BookingConnectorReturn> bookings) {
		this.bookings = bookings;
	}
	
	
	
}
