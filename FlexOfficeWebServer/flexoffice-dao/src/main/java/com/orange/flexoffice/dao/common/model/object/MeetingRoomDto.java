package com.orange.flexoffice.dao.common.model.object;

import java.util.Date;

import com.orange.flexoffice.dao.common.model.data.AgentDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_MeetingRoomStatus;
import com.orange.flexoffice.dao.common.model.enumeration.E_MeetingRoomType;

/**
 * MeetingRoomDto
 * @author oab
 *
 */
public class MeetingRoomDto {
	
	/**
	 * A serial Meeting Room ID.
	 */
	private Long id;
	private String name;
	private String externalId;
	private String address; // address from buildings table "building_address !!!
	private Integer capacity;
	private String description;
	private E_MeetingRoomStatus status;
	private E_MeetingRoomType type;
	private AgentDao agent;
	private String organizerLabel;
	private Date lastMeasureDate; // the last time since the status has been modified
	private Long buildingId;
	private Long floor;
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
	public E_MeetingRoomStatus getStatus() {
		return status;
	}
	public void setStatus(E_MeetingRoomStatus status) {
		this.status = status;
	}
	public E_MeetingRoomType getType() {
		return type;
	}
	public void setType(E_MeetingRoomType type) {
		this.type = type;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getExternalId() {
		return externalId;
	}
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
	public AgentDao getAgent() {
		return agent;
	}
	public void setAgent(AgentDao agent) {
		this.agent = agent;
	}
	public String getOrganizerLabel() {
		return organizerLabel;
	}
	public void setOrganizerLabel(String organizerLabel) {
		this.organizerLabel = organizerLabel;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
}
