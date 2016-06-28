package com.orange.flexoffice.dao.common.model.object;

/**
 * SimpleStatDto
 * @author oab
 *
 */
public class SimpleStatDto implements Comparable<SimpleStatDto> {
	
	private Float rate;
	private String roomName;
	private Integer roomId;
	private Long occupancyDuration;
	private Long nbDaysDuration;
	
	/**
	 * @return the roomId
	 */
	public Integer getRoomId() {
		return roomId;
	}
	/**
	 * @param roomId the roomId to set
	 */
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}
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
	
	@Override
	public int compareTo(SimpleStatDto o) {
		return o.getRate().compareTo(this.getRate());
	}

	
}
