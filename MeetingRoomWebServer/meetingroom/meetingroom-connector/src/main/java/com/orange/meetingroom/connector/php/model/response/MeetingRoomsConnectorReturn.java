package com.orange.meetingroom.connector.php.model.response;

import java.util.ArrayList;
import java.util.List;

/**
 * MeetingRoom
 * @author oab
 *
 */
public class MeetingRoomsConnectorReturn {
	
	Integer currentDate;
	List<MeetingRoomBookingsConnectorReturn> meetingrooms;
	
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
	public List<MeetingRoomBookingsConnectorReturn> getMeetingRooms() {
		if (meetingrooms == null) {
			meetingrooms = new ArrayList<MeetingRoomBookingsConnectorReturn>();
		}
		return meetingrooms;
	}
	/**
	 * @param rooms the rooms to set
	 */
	public void setMeetingRooms(List<MeetingRoomBookingsConnectorReturn> meetingrooms) {
		this.meetingrooms = meetingrooms;
	}
	
	
	
}
