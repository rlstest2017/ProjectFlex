package com.orange.meetingroom.connector.php.model.response;

import java.util.List;

public class MeetingRoomBookings {

	Integer currentDate;
	MeetingRoomDetails meetingRoomDetails;
	List<Booking> bookings;
	
	/**
	 * @return the currentDate
	 */
	public Integer getCurrentDate() {
		return currentDate;
	}
	/**
	 * @param currentDate the currentDate to set
	 */
	public void setCurrentDate(Integer currentDate) {
		this.currentDate = currentDate;
	}
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
		return bookings;
	}
	/**
	 * @param bookings the bookings to set
	 */
	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}
	
	
	
}
