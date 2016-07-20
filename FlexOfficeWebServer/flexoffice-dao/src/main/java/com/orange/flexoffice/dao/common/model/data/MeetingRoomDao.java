package com.orange.flexoffice.dao.common.model.data;

import java.util.Date;

/**
 * MeetingRoomDao
 * @author oab
 *
 */
public class MeetingRoomDao extends AbstractData {
	
	private String name;
	private String externalId;
	private Integer capacity;
	private String description;
	private String status;
	private String type;
	private Long agentId;
	private String organizerLabel;
	private Long buildingId;
	private Long floor;
	private Date lastMeasureDate; // the last time since the status has been modified 
	private Date startDate;
	private Date endDate;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getCapacity() {
		return capacity;
	}
	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Long getAgentId() {
		return agentId;
	}
	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the buildingId
	 */
	public Long getBuildingId() {
		return buildingId;
	}
	/**
	 * @param buildingId the buildingId to set
	 */
	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}
	/**
	 * @return the floor
	 */
	public Long getFloor() {
		return floor;
	}
	/**
	 * @param floor the floor to set
	 */
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
	/**
	 * @return the lastMeasureDate
	 */
	public Date getLastMeasureDate() {
		return lastMeasureDate;
	}
	/**
	 * @param lastMeasureDate the lastMeasureDate to set
	 */
	public void setLastMeasureDate(Date lastMeasureDate) {
		this.lastMeasureDate = lastMeasureDate;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getExternalId() {
		return externalId;
	}
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
	
	public String getOrganizerLabel() {
		return organizerLabel;
	}
	public void setOrganizerLabel(String organizerLabel) {
		this.organizerLabel = organizerLabel;
	}
	

}
