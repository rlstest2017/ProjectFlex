package com.orange.meetingroom.connector.flexoffice.model.request;

import com.orange.meetingroom.connector.flexoffice.enums.EnumMeetingRoomStatus;

/**
 * MeetingRoomData
 * @author oab
 *
 */
public class MeetingRoomData {

	String meetingRoomExternalId;
	EnumMeetingRoomStatus meetingRoomStatus;
	Integer startDate;
	Integer endDate;
	String organizerLabel;
	
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
	/**
	 * @return the startDate
	 */
	public Integer getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Integer startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public Integer getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Integer endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the organizerLabel
	 */
	public String getOrganizerLabel() {
		return organizerLabel;
	}
	/**
	 * @param organizerLabel the organizerLabel to set
	 */
	public void setOrganizerLabel(String organizerLabel) {
		this.organizerLabel = organizerLabel;
	}
	
	
}
