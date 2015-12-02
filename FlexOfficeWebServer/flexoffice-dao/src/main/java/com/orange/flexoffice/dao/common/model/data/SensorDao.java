package com.orange.flexoffice.dao.common.model.data;

import java.util.Date;

/**
 * SensorDao
 * @author oab
 *
 */
public class SensorDao extends AbstractData {
	
	private String identifier; // sensor back id on hexa 4 octets
	private String name;
	private String type;
	private String profile; // profile back as a5-07-01 & a5-04-01
	private String description;
	private String status;
	private String occupancyInfo;
	private Integer roomId;
	private Date lastMeasureDate;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOccupancyInfo() {
		return occupancyInfo;
	}
	public void setOccupancyInfo(String occupancyInfo) {
		this.occupancyInfo = occupancyInfo;
	}
	public Integer getRoomId() {
		return roomId;
	}
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}
	public Date getLastMeasureDate() {
		return lastMeasureDate;
	}
	public void setLastMeasureDate(Date lastMeasureDate) {
		this.lastMeasureDate = lastMeasureDate;
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
