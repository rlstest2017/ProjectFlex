package com.orange.flexoffice.dao.common.model.data;

import java.util.Date;

/**
 * RoomDao
 * @author oab
 *
 */
public class RoomDao extends AbstractData {
	
	private String name;
	private String address;
	private Integer capacity;
	private Double temperature;
	private Double humidity;
	private String description;
	private String status;
	private String type;
	private Long gatewayId;
	private Long userId;
	private Date lastMeasureDate; // the last time since the status has been modified 
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getCapacity() {
		return capacity;
	}
	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}
	
	public Double getTemperature() {
		return temperature;
	}
	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}
	public Double getHumidity() {
		return humidity;
	}
	public void setHumidity(Double humidity) {
		this.humidity = humidity;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Long getGatewayId() {
		return gatewayId;
	}
	public void setGatewayId(Long gatewayId) {
		this.gatewayId = gatewayId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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
	

}
