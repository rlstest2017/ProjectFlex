package com.orange.flexoffice.dao.common.model.object;

/**
 * MeetingRoomGroupsConfigurationDto
 * @author oab
 *
 */
public class MeetingRoomGroupsConfigurationDto {
	
	/**
	 * A serial meetingroom_groups_configuration ID.
	 */
	private String id;
	private String buildingId;
	private String floor;
	private String meetingRoomGroupId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public String getMeetingRoomGroupId() {
		return meetingRoomGroupId;
	}
	public void setMeetingRoomGroupId(String meetingRoomGroupId) {
		this.meetingRoomGroupId = meetingRoomGroupId;
	}

}
