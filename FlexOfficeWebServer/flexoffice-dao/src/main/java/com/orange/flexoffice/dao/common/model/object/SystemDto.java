package com.orange.flexoffice.dao.common.model.object;

import java.util.ArrayList;
import java.util.List;

import com.orange.flexoffice.dao.common.model.data.AlertDao;

/**
 * SystemDto
 * @author oab
 *
 */
public class SystemDto {
	
	private Integer userCount;
	private Integer activeUserCount;
	private Integer gatewayCount;
	private Integer roomCount;
	private List<AlertDao> deviceAlerts;
	
	public Integer getUserCount() {
		return userCount;
	}
	public void setUserCount(Integer userCount) {
		this.userCount = userCount;
	}
	
	public Integer getActiveUserCount() {
		return activeUserCount;
	}
	public void setActiveUserCount(Integer activeUserCount) {
		this.activeUserCount = activeUserCount;
	}
	
	public Integer getGatewayCount() {
		return gatewayCount;
	}
	public void setGatewayCount(Integer gatewayCount) {
		this.gatewayCount = gatewayCount;
	}
	
	public Integer getRoomCount() {
		return roomCount;
	}
	public void setRoomCount(Integer roomCount) {
		this.roomCount = roomCount;
	}
	
	public List<AlertDao> getDeviceAlerts() {
		if (deviceAlerts == null) {
			deviceAlerts = new ArrayList<AlertDao>();
		}
		return deviceAlerts;
	}
	public void setDeviceAlerts(List<AlertDao> deviceAlerts) {
		this.deviceAlerts = deviceAlerts;
	}
	
 }
