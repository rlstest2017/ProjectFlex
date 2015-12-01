package com.orange.flexoffice.dao.common.model.data;

import java.util.Date;

/**
 * AlertDao
 * @author oab
 *
 */
public class AlertDao extends AbstractData {
	
	private String name;
	private String type;
	private Integer gatewayId;
	private Integer sensorId;
	private Date lastNotification;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public Integer getGatewayId() {
		return gatewayId;
	}
	public void setGatewayId(Integer gatewayId) {
		this.gatewayId = gatewayId;
	}
	public Integer getSensorId() {
		return sensorId;
	}
	public void setSensorId(Integer sensorId) {
		this.sensorId = sensorId;
	}
	public Date getLastNotification() {
		return lastNotification;
	}
	public void setLastNotification(Date lastNotification) {
		this.lastNotification = lastNotification;
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
