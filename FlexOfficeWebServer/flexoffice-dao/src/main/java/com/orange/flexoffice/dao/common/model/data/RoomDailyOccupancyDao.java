package com.orange.flexoffice.dao.common.model.data;

import java.util.Date;

/**
 * RoomDailyOccupancy
 * @author oab
 *
 */
public class RoomDailyOccupancyDao extends AbstractData {
	
	private Integer roomId;
	private Long occupancyDuration;
	private Date day;
	
	
	public Integer getRoomId() {
		return roomId;
	}
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}
	public Long getOccupancyDuration() {
		return occupancyDuration;
	}
	public void setOccupancyDuration(Long occupancyDuration) {
		this.occupancyDuration = occupancyDuration;
	}
	public Date getDay() {
		return day;
	}
	public void setDay(Date day) {
		this.day = day;
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
