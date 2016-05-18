package com.orange.flexoffice.dao.common.model.data;

/**
 * MeetingRoomGroupsConfigurationDao
 * @author oab
 *
 */
public class MeetingRoomGroupsConfigurationDao extends AbstractData {
	
	private Long buildingId;
	private String meetingroomGroupId;
	private Long floor;
	
	public Long getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}
	public String getMeetingroomGroupId() {
		return meetingroomGroupId;
	}
	public void setMeetingroomGroupId(String meetingroomGroupId) {
		this.meetingroomGroupId = meetingroomGroupId;
	}
	public Long getFloor() {
		return floor;
	}
	public void setFloor(Long floor) {
		this.floor = floor;
	}
	@Override
	public void setColumnId(String columnId) {
		setId(Long.valueOf(columnId));
	}
	@Override
	public String getColumnId() {
		return getId().toString();
	}
}
