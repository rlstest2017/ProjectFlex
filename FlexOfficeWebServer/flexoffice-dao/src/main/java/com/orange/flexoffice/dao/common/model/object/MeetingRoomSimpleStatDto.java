package com.orange.flexoffice.dao.common.model.object;

/**
 * SimpleStatDto
 * @author oab
 *
 */
public class MeetingRoomSimpleStatDto {
	
	private Float rate;
	private String meetingRoomName;
	private Integer meetingRoomId;
	private Long occupancyDuration;
	private Long nbDaysDuration;
	

	/**
	 * @return the rate
	 */
	public Float getRate() {
		return rate;
	}
	/**
	 * @param rate the rate to set
	 */
	public void setRate(Float rate) {
		this.rate = rate;
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
	public Integer getMeetingRoomId() {
		return meetingRoomId;
	}
	public void setMeetingRoomId(Integer meetingRoomId) {
		this.meetingRoomId = meetingRoomId;
	}
	public String getMeetingRoomName() {
		return meetingRoomName;
	}
	public void setMeetingRoomName(String meetingRoomName) {
		this.meetingRoomName = meetingRoomName;
	}

	
}
