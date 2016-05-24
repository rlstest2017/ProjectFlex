package com.orange.flexoffice.dao.common.model.data;

import java.util.Date;

/**
 * MeetingRoomStatDao
 * @author oab
 *
 */
public class MeetingRoomStatDao extends AbstractData {
	
	private Integer meetingroomId;
	private Date beginOccupancyDate;
	private Date endOccupancyDate;
	private String meetingRoomInfo;
	
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
	public Integer getMeetingroomId() {
		return meetingroomId;
	}
	public void setMeetingroomId(Integer meetingroomId) {
		this.meetingroomId = meetingroomId;
	}
	public String getMeetingRoomInfo() {
		return meetingRoomInfo;
	}
	public void setMeetingRoomInfo(String meetingRoomInfo) {
		this.meetingRoomInfo = meetingRoomInfo;
	}
}
