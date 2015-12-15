package com.orange.flexoffice.dao.common.model.object;

import java.util.Date;


/**
 * RoomDailyTypeDto
 * @author oab
 *
 */
public class RoomDailyTypeDto {
	
	private Date day;
	private String type;
	private Long occupancyDuration;
	
	/**
	 * @return the day
	 */
	public Date getDay() {
		return day;
	}
	/**
	 * @param day the day to set
	 */
	public void setDay(Date day) {
		this.day = day;
	}
	
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
	 * @return the occupancyDuration
	 */
	public Long getOccupancyDuration() {
		return occupancyDuration;
	}
	/**
	 * @param occupancyDuration the occupancyDuration to set
	 */
	public void setOccupancyDuration(Long occupancyDuration) {
		this.occupancyDuration = occupancyDuration;
	}
	
	
}
