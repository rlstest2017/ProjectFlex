package com.orange.flexoffice.dao.common.model.object;

import java.util.Date;

/**
 * ExportStatDto
 * @author oab
 *
 */
public class ExportStatDto {
	
	private String roomName;
	private String roomType;
	private Date beginOccupancyDate;
	private Date endOccupancyDate;
	
	/**
	 * @return the roomName
	 */
	public String getRoomName() {
		return roomName;
	}
	/**
	 * @param roomName the roomName to set
	 */
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	/**
	 * @return the roomType
	 */
	public String getRoomType() {
		return roomType;
	}
	/**
	 * @param roomType the roomType to set
	 */
	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}
	public Date getBeginOccupancyDate() {
		return beginOccupancyDate;
	}
	public void setBeginOccupancyDate(Date beginOccupancyDate) {
		this.beginOccupancyDate = beginOccupancyDate;
	}
	public Date getEndOccupancyDate() {
		return endOccupancyDate;
	}
	public void setEndOccupancyDate(Date endOccupancyDate) {
		this.endOccupancyDate = endOccupancyDate;
	}

}
