package com.orange.meetingroom.connector.php.model.response;

import com.orange.meetingroom.connector.flexoffice.enums.EnumMeetingRoomStatus;

/**
 * MeetingRoomDetailsConnectorReturn
 * @author oab
 *
 */
public class MeetingRoomDetailsConnectorReturn {

	String meetingRoomExternalId;
	String meetingRoomExternalName;
	String meetingRoomExternalLocation;
	EnumMeetingRoomStatus meetingRoomStatus;
	
	/**
	 * @return the meetingRoomExternalId
	 */
	public String getMeetingRoomExternalId() {
		return meetingRoomExternalId;
	}
	/**
	 * @param meetingRoomExternalId the meetingRoomExternalId to set
	 */
	public void setMeetingRoomExternalId(String meetingRoomExternalId) {
		this.meetingRoomExternalId = meetingRoomExternalId;
	}
	/**
	 * @return the meetingRoomExternalName
	 */
	public String getMeetingRoomExternalName() {
		return meetingRoomExternalName;
	}
	/**
	 * @param meetingRoomExternalName the meetingRoomExternalName to set
	 */
	public void setMeetingRoomExternalName(String meetingRoomExternalName) {
		this.meetingRoomExternalName = meetingRoomExternalName;
	}
	/**
	 * @return the meetingRoomExternalLocation
	 */
	public String getMeetingRoomExternalLocation() {
		return meetingRoomExternalLocation;
	}
	/**
	 * @param meetingRoomExternalLocation the meetingRoomExternalLocation to set
	 */
	public void setMeetingRoomExternalLocation(String meetingRoomExternalLocation) {
		this.meetingRoomExternalLocation = meetingRoomExternalLocation;
	}
	/**
	 * @return the meetingRoomStatus
	 */
	public EnumMeetingRoomStatus getMeetingRoomStatus() {
		return meetingRoomStatus;
	}
	/**
	 * @param meetingRoomStatus the meetingRoomStatus to set
	 */
	public void setMeetingRoomStatus(EnumMeetingRoomStatus meetingRoomStatus) {
		this.meetingRoomStatus = meetingRoomStatus;
	}
	
	
}
