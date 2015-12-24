package com.orange.flexoffice.dao.common.model.data;

/**
 * TeachinSensorDao
 * @author oab
 *
 */
public class TeachinSensorDao extends AbstractData {

	private Long roomId;
	private Long gatewayId;
	private Long userId;
	private String sensorIdentifier; // sensor back id on hexa 4 octets
	private String sensorStatus;
	private String teachinStatus;
	
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
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
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
