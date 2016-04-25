package com.orange.meetingroom.connector.php.model.response;

import java.util.ArrayList;
import java.util.List;

/**
 * MeetingRoomBookings
 * @author oab
 *
 */
public class MeetingRoomBookings {

	MeetingRoomDetails meetingRoomDetails;
	List<Booking> bookings;
	
	/**
	 * @return the meetingRoomDetails
	 */
	public MeetingRoomDetails getMeetingRoomDetails() {
		return meetingRoomDetails;
	}
	/**
	 * @param meetingRoomDetails the meetingRoomDetails to set
	 */
	public void setMeetingRoomDetails(MeetingRoomDetails meetingRoomDetails) {
		this.meetingRoomDetails = meetingRoomDetails;
	}
	/**
	 * @return the bookings
	 */
	public List<Booking> getBookings() {
		if (bookings == null)
			bookings = new ArrayList<Booking>();
		return bookings;
	}
	/**
	 * @param bookings the bookings to set
	 */
	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}
	
	
	
}
