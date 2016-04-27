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
	private Integer activeGatewayCount;
	private Integer roomCount;
	private Integer freeRoomCount;
	private Integer occupiedRoomCount;
	private Integer agentCount;
	private Integer meetingroomCount;
	private Integer freeMeetingroomCount;
	private Integer occupiedMeetingroomCount;
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
	/**
	 * @return the activeGatewayCount
	 */
	public Integer getActiveGatewayCount() {
		return activeGatewayCount;
	}
	/**
	 * @param activeGatewayCount the activeGatewayCount to set
	 */
	public void setActiveGatewayCount(Integer activeGatewayCount) {
		this.activeGatewayCount = activeGatewayCount;
	}
	/**
	 * @return the freeRoomCount
	 */
	public Integer getFreeRoomCount() {
		return freeRoomCount;
	}
	/**
	 * @param freeRoomCount the freeRoomCount to set
	 */
	public void setFreeRoomCount(Integer freeRoomCount) {
		this.freeRoomCount = freeRoomCount;
	}
	/**
	 * @return the occupiedRoomCount
	 */
	public Integer getOccupiedRoomCount() {
		return occupiedRoomCount;
	}
	/**
	 * @param occupiedRoomCount the occupiedRoomCount to set
	 */
	public void setOccupiedRoomCount(Integer occupiedRoomCount) {
		this.occupiedRoomCount = occupiedRoomCount;
	}
	public Integer getAgentCount() {
		return agentCount;
	}
	public void setAgentCount(Integer agentCount) {
		this.agentCount = agentCount;
	}
	public Integer getMeetingroomCount() {
		return meetingroomCount;
	}
	public void setMeetingroomCount(Integer meetingroomCount) {
		this.meetingroomCount = meetingroomCount;
	}
	public Integer getFreeMeetingroomCount() {
		return freeMeetingroomCount;
	}
	public void setFreeMeetingroomCount(Integer freeMeetingroomCount) {
		this.freeMeetingroomCount = freeMeetingroomCount;
	}
	public Integer getOccupiedMeetingroomCount() {
		return occupiedMeetingroomCount;
	}
	public void setOccupiedMeetingroomCount(Integer occupiedMeetingroomCount) {
		this.occupiedMeetingroomCount = occupiedMeetingroomCount;
	}
	
	
 }
