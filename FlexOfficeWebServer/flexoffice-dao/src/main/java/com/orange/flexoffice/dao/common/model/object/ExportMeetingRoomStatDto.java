package com.orange.flexoffice.dao.common.model.object;

/**
 * ExportMeetingRoomStatDto
 * @author oab
 *
 */
public class ExportMeetingRoomStatDto {
	
	private String countryName;
	private String regionName;
	private String cityName;
	private String buildingName;
	private String meetingRoomName;
	private Integer meetingRoomFloor;
	private String meetingRoomType;
	private String beginOccupancyDate;
	private String endOccupancyDate;
	
	
	/**
	 * @return the countryName
	 */
	public String getCountryName() {
		return countryName;
	}
	/**
	 * @param countryName the countryName to set
	 */
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	/**
	 * @return the regionName
	 */
	public String getRegionName() {
		return regionName;
	}
	/**
	 * @param regionName the regionName to set
	 */
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	/**
	 * @return the cityName
	 */
	public String getCityName() {
		return cityName;
	}
	/**
	 * @param cityName the cityName to set
	 */
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	/**
	 * @return the buildingName
	 */
	public String getBuildingName() {
		return buildingName;
	}
	/**
	 * @param buildingName the buildingName to set
	 */
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public String getBeginOccupancyDate() {
		return beginOccupancyDate;
	}
	public void setBeginOccupancyDate(String beginOccupancyDate) {
		this.beginOccupancyDate = beginOccupancyDate;
	}
	public String getEndOccupancyDate() {
		return endOccupancyDate;
	}
	public void setEndOccupancyDate(String endOccupancyDate) {
		this.endOccupancyDate = endOccupancyDate;
	}
	public String getMeetingRoomName() {
		return meetingRoomName;
	}
	public void setMeetingRoomName(String meetingRoomName) {
		this.meetingRoomName = meetingRoomName;
	}
	public Integer getMeetingRoomFloor() {
		return meetingRoomFloor;
	}
	public void setMeetingRoomFloor(Integer meetingRoomFloor) {
		this.meetingRoomFloor = meetingRoomFloor;
	}
	public String getMeetingRoomType() {
		return meetingRoomType;
	}
	public void setMeetingRoomType(String meetingRoomType) {
		this.meetingRoomType = meetingRoomType;
	}

}
