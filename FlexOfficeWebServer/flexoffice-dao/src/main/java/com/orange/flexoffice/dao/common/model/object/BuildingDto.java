package com.orange.flexoffice.dao.common.model.object;

/**
 * A BuildingDto. 
 * @author oab
 *
 */
public class BuildingDto extends CityDto {
	
	private Long cityId;
	private String cityName;
	private String address;
	private Long nbFloors;

	
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
