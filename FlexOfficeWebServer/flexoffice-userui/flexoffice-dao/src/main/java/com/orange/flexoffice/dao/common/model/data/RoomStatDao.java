package com.orange.flexoffice.dao.common.model.data;

import java.util.Date;

import com.orange.flexoffice.dao.common.model.enumeration.E_RoomType;

/**
 * RoomStatDao
 * @author oab
 *
 */
public class RoomStatDao extends AbstractData {
	
	private Integer roomId;
	private E_RoomType roomType;
	private Date beginOccupancyDate;
	private Date endOccupancyDate;
	
	public Integer getRoomId() {
		return roomId;
	}
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}
	public E_RoomType getRoomType() {
		return roomType;
	}
	public void setRoomType(E_RoomType roomType) {
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
	@Override
	public void setColumnId(String columnId) {
		setId(Long.valueOf(columnId));
	}
	@Override
	public String getColumnId() {
		return getId().toString();
	}
	

}
