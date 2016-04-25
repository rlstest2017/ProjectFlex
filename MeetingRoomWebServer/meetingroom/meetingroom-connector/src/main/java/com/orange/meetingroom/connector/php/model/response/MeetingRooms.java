package com.orange.meetingroom.connector.php.model.response;

import java.util.ArrayList;
import java.util.List;

/**
 * MeetingRoom
 * @author oab
 *
 */
public class MeetingRooms {
	
	Integer currentDate;
	List<MeetingRoomBookings> meetingrooms;
	
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
	 * @return the rooms
	 */
	public List<MeetingRoomBookings> getMeetingRooms() {
		if (meetingrooms == null)
			meetingrooms = new ArrayList<MeetingRoomBookings>();
		return meetingrooms;
	}
	/**
	 * @param rooms the rooms to set
	 */
	public void setMeetingRooms(List<MeetingRoomBookings> meetingrooms) {
		this.meetingrooms = meetingrooms;
	}
	
	
	
}
