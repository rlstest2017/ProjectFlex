package com.orange.flexoffice.dao.common.model.object;

/**
 * SensorTypeAndRoomDto
 * @author oab
 *
 */
public class SensorTypeAndRoomDto  {

	private String type; 
	private Integer roomId;
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
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
}
