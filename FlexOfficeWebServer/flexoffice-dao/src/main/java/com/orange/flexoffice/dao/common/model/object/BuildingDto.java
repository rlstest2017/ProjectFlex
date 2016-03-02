package com.orange.flexoffice.dao.common.model.object;

/**
 * A BuildingDto. 
 * @author oab
 *
 */
public class BuildingDto extends CityDto {
	
	private String address;
	private LocationDto city; 
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
	 * @return the city
	 */
	public LocationDto getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(LocationDto city) {
		this.city = city;
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
