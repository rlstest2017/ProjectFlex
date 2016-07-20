package com.orange.flexoffice.dao.common.model.object;

import java.util.Date;

import com.orange.flexoffice.dao.common.model.enumeration.E_CommandModel;
import com.orange.flexoffice.dao.common.model.enumeration.E_DashboardStatus;

/**
 * AgentDto
 * @author oab
 *
 */
public class DashboardDto {
	
	/**
	 * A serial Dashboard ID.
	 */
	private String id;
	private String name;
	private String macAddress;
	private String description;
	private E_DashboardStatus status;
	private Long cityId;
	private Long buildingId;
	private Long floor;
	private Date lastMeasureDate;
	private E_CommandModel command;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public E_DashboardStatus getStatus() {
		return status;
	}
	public void setStatus(E_DashboardStatus status) {
		this.status = status;
	}

	public Date getLastMeasureDate() {
		return lastMeasureDate;
	}
	public void setLastMeasureDate(Date lastPollingDate) {
		this.lastMeasureDate = lastPollingDate;
	}
	public Long getCityId() {
		return cityId;
	}
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	public Long getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}
	public Long getFloor() {
		return floor;
	}
	public void setFloor(Long floor) {
		this.floor = floor;
	}
	public E_CommandModel getCommand() {
		return command;
	}
	public void setCommand(E_CommandModel command) {
		this.command = command;
	}
	
	

}
