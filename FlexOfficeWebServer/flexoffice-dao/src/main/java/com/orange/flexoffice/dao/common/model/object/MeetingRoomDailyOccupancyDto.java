package com.orange.flexoffice.dao.common.model.object;

import java.util.Date;

/**
 * MeetingRoomDailyOccupancyDto
 * @author oab
 *
 */
public class MeetingRoomDailyOccupancyDto {
	
	private Date fromDate;
	private	Date toDate;
	/**
	 * @return the fromDate
	 */
	public Date getFromDate() {
		return fromDate;
	}
	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	/**
	 * @return the toDate
	 */
	public Date getToDate() {
		return toDate;
	}
	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	
}
