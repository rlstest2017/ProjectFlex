package com.orange.flexoffice.dao.common.model.data;

/**
 * TeachinSensorDao
 * @author oab
 *
 */
public class TeachinSensorDao extends AbstractData {

	private Integer roomId;
	private Integer gatewayId;
	private Integer userId;
	private String sensorIdentifier; // sensor back id on hexa 4 octets
	private String sensorStatus;
	private String teachinStatus;
	
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
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * @return the sensorIdentifier
	 */
	public String getSensorIdentifier() {
		return sensorIdentifier;
	}

	/**
	 * @param sensorIdentifier the sensorIdentifier to set
	 */
	public void setSensorIdentifier(String sensorIdentifier) {
		this.sensorIdentifier = sensorIdentifier;
	}

	/**
	 * @return the sensorStatus
	 */
	public String getSensorStatus() {
		return sensorStatus;
	}

	/**
	 * @param sensorStatus the sensorStatus to set
	 */
	public void setSensorStatus(String sensorStatus) {
		this.sensorStatus = sensorStatus;
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

	@Override
	public void setColumnId(String columnId) {
	}

	@Override
	public String getColumnId() {
		return null;
	}
	

}
