package com.orange.flexoffice.dao.common.model.object;

/**
 * A BuildingDto. 
 * @author oab
 *
 */
public class BuildingSummaryDto extends CitySummaryDto {
	
	private String address;
	private String cityName; 
	private Long nbFloors;
	
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
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
	 * @return the nbFloors
	 */
	public Long getNbFloors() {
		return nbFloors;
	}
	/**
	 * @param nbFloors the nbFloors to set
	 */
	public void setNbFloors(Long nbFloors) {
		this.nbFloors = nbFloors;
	}

	

}
