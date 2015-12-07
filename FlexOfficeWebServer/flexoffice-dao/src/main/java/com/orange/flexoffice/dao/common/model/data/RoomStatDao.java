package com.orange.flexoffice.dao.common.model.data;

import java.util.Date;

/**
 * RoomStatDao
 * @author oab
 *
 */
public class RoomStatDao extends AbstractData {
	
	private Integer roomId;
	private Integer userId;
	private Date beginOccupancyDate;
	private Date endOccupancyDate;
	private Date reservationDate;
	private Boolean isReservationHonored;
	private String roomInfo;
	
	public Integer getRoomId() {
		return roomId;
	}
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
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
	public Date getReservationDate() {
		return reservationDate;
	}
	public void setReservationDate(Date reservationDate) {
		this.reservationDate = reservationDate;
	}
	public Boolean getIsReservationHonored() {
		return isReservationHonored;
	}
	public void setIsReservationHonored(Boolean isReservationHonored) {
		this.isReservationHonored = isReservationHonored;
	}
	public String getRoomInfo() {
		return roomInfo;
	}
	public void setRoomInfo(String roomInfo) {
		this.roomInfo = roomInfo;
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
