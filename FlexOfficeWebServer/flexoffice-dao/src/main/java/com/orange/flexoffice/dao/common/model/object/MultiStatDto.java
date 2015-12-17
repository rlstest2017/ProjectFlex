package com.orange.flexoffice.dao.common.model.object;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.orange.flexoffice.dao.common.model.enumeration.E_RoomType;

/**
 * MultiStatDto
 * @author oab
 *
 */
public class MultiStatDto {
	
	private String label;
	private List<String> values;
	private E_RoomType roomType;
	private Long occupancyDuration;
	private Date day;
	private Long nbDaysDuration;
	
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return the values
	 */
	public List<String> getValues() {
		if (values == null) {
			values = new ArrayList<String>();
        }
		return this.values;
	}	
	/**
	 * @param values the values to set
	 */
	public void setValues(List<String> values) {
		this.values = values;
	}
	/**
	 * @return the roomType
	 */
	public E_RoomType getRoomType() {
		return roomType;
	}
	/**
	 * @param roomType the roomType to set
	 */
	public void setRoomType(E_RoomType roomType) {
		this.roomType = roomType;
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
	 * @return the nbDaysDuration
	 */
	public Long getNbDaysDuration() {
		return nbDaysDuration;
	}
	/**
	 * @param nbDaysDuration the nbDaysDuration to set
	 */
	public void setNbDaysDuration(Long nbDaysDuration) {
		this.nbDaysDuration = nbDaysDuration;
	}
		
	
	
}
