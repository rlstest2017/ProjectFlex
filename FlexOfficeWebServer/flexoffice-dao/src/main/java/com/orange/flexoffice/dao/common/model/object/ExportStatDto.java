package com.orange.flexoffice.dao.common.model.object;

import java.util.Date;

/**
 * ExportStatDto
 * @author oab
 *
 */
public class ExportStatDto {
	
	private String countryName;
	private String regionName;
	private String cityName;
	private String buildingName;
	private String roomName;
	private Integer roomFloor;
	private String roomType;
	private Date beginOccupancyDate;
	private Date endOccupancyDate;
	
	
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
	/**
	 * @return the roomFloor
	 */
	public Integer getRoomFloor() {
		return roomFloor;
	}
	/**
	 * @param roomFloor the roomFloor to set
	 */
	public void setRoomFloor(Integer roomFloor) {
		this.roomFloor = roomFloor;
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
	 * @return the roomType
	 */
	public String getRoomType() {
		return roomType;
	}
	/**
	 * @param roomType the roomType to set
	 */
	public void setRoomType(String roomType) {
		this.roomType = roomType;
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

}
