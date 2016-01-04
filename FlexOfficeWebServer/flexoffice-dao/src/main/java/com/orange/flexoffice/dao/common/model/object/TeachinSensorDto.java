package com.orange.flexoffice.dao.common.model.object;

import java.util.ArrayList;
import java.util.List;

/**
 * TeachinSensorDto
 * @author oab
 *
 */
public class TeachinSensorDto  {

	private Integer roomId;
	private Integer gatewayId;
	private String teachinStatus;
	private List<SensorDto> sensors; 
	
	/**
	 * @return the roomId
	 */
	public Integer getRoomId() {
		return roomId;
	}

	/**
	 * @param roomId the roomId to set
	 */
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	/**
	 * @return the gatewayId
	 */
	public Integer getGatewayId() {
		return gatewayId;
	}

	/**
	 * @param gatewayId the gatewayId to set
	 */
	public void setGatewayId(Integer gatewayId) {
		this.gatewayId = gatewayId;
	}
	
	/**
	 * @return the teachinStatus
	 */
	public String getTeachinStatus() {
		return teachinStatus;
	}

	/**
	 * @param teachinStatus the teachinStatus to set
	 */
	public void setTeachinStatus(String teachinStatus) {
		this.teachinStatus = teachinStatus;
	}

	/**
	 * @return the sensors
	 */
	public List<SensorDto> getSensors() {
	    if (sensors == null) {
	    	sensors = new ArrayList<SensorDto>();
        }
		return sensors;
	}

	/**
	 * @param sensors the sensors to set
	 */
	public void setSensors(List<SensorDto> sensors) {
		this.sensors = sensors;
	}
	
	
	
}
