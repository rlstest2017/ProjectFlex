package com.orange.flexoffice.dao.common.model.data;

import java.util.Date;

/**
 * MeetingRoomDailyOccupancyDao
 * @author oab
 *
 */
public class MeetingRoomDailyOccupancyDao extends AbstractData {
	
	private Integer meetingroomId;
	private Long occupancyDuration;
	private Date day;
	
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
	public Integer getMeetingroomId() {
		return meetingroomId;
	}
	public void setMeetingroomId(Integer meetingRoomId) {
		this.meetingroomId = meetingRoomId;
	}
	

}
