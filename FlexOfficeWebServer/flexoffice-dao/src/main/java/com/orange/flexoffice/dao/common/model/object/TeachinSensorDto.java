package com.orange.flexoffice.dao.common.model.object;

import java.util.ArrayList;
import java.util.List;

/**
 * TeachinSensorDto
 * @author oab
 *
 */
public class TeachinSensorDto  {

	private Long roomId;
	private Long gatewayId;
	private String teachinStatus;
	private List<SensorDto> sensors; 
	
	/**
	 * @return the roomId
	 */
	public Long getRoomId() {
		return roomId;
	}

	/**
	 * @param roomId the roomId to set
	 */
	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	/**
	 * @return the gatewayId
	 */
	public Long getGatewayId() {
		return gatewayId;
	}

	/**
	 * @param gatewayId the gatewayId to set
	 */
	public void setGatewayId(Long gatewayId) {
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
