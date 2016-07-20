package com.orange.meetingroom.connector.php.model.request;

public class GetAgentBookingsParameters {
	
	String roomID;
	String format;
	String forceUpdateCache;
	
	/**
	 * @return the roomID
	 */
	public String getRoomID() {
		return roomID;
	}
	/**
	 * @param roomID the roomID to set
	 */
	public void setRoomID(String roomID) {
		this.roomID = roomID;
	}
	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}
	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}
	/**
	 * @return the forceUpdateCache
	 */
	public String getForceUpdateCache() {
		return forceUpdateCache;
	}
	/**
	 * @param forceUpdateCache the forceUpdateCache to set
	 */
	public void setForceUpdateCache(String forceUpdateCache) {
		this.forceUpdateCache = forceUpdateCache;
	}
			
	
}
