package com.orange.meetingroom.connector.php.model.response;

/**
 * MeetingRoom
 * @author oab
 *
 */
public class MeetingRoomConnectorReturn {
	
	Integer currentDate;
	MeetingRoomBookingsConnectorReturn meetingroom;
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
	public MeetingRoomBookingsConnectorReturn getMeetingRoom() {
		return meetingroom;
	}
	/**
	 * @param room the room to set
	 */
	public void setMeetingRoom(MeetingRoomBookingsConnectorReturn meetingroom) {
		this.meetingroom = meetingroom;
	}
	
	
}
