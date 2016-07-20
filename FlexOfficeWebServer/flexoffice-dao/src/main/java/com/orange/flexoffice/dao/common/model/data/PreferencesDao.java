package com.orange.flexoffice.dao.common.model.data;

/**
 * A Preferences of user. 
 * @author oab
 *
 */
public class PreferencesDao extends AbstractData {
	
	
	private Long userId;
	private Long countryId;
	private Long regionId;
	private Long cityId;
	private Long buildingId;
	private Long floor;
	
	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * @return the countryId
	 */
	public Long getCountryId() {
		return countryId;
	}
	/**
	 * @param countryId the countryId to set
	 */
	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}
	/**
	 * @return the regionId
	 */
	public Long getRegionId() {
		return regionId;
	}
	/**
	 * @param regionId the regionId to set
	 */
	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}
	/**
	 * @return the buildingId
	 */
	public Long getBuildingId() {
		return buildingId;
	}
	/**
	 * @param buildingId the buildingId to set
	 */
	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}
	/**
	 * @return the floor
	 */
	public Long getFloor() {
		return floor;
	}
	/**
	 * @param floor the floor to set
	 */
	public void setFloor(Long floor) {
		this.floor = floor;
	}
	/**
	 * @return the cityId
	 */
	public Long getCityId() {
		return cityId;
	}
	/**
	 * @param cityId the cityId to set
	 */
	public void setCityId(Long cityId) {
		this.cityId = cityId;
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
