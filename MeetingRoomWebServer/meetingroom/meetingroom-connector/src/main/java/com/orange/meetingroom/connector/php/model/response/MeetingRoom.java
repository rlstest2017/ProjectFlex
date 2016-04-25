package com.orange.meetingroom.connector.php.model.response;

/**
 * MeetingRoom
 * @author oab
 *
 */
public class MeetingRoom {
	
	Integer currentDate;
	MeetingRoomBookings meetingroom;
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
	 * @return the room
	 */
	public MeetingRoomBookings getMeetingRoom() {
		return meetingroom;
	}
	/**
	 * @param room the room to set
	 */
	public void setMeetingRoom(MeetingRoomBookings meetingroom) {
		this.meetingroom = meetingroom;
	}
	
	
}
