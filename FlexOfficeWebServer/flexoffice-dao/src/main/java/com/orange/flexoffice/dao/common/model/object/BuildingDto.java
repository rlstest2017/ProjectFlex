package com.orange.flexoffice.dao.common.model.object;

/**
 * A BuildingDto. 
 * @author oab
 *
 */
public class BuildingDto {
	
	/**
	 * A serial Building ID.
	 */
	private Long id;
	private String name;
	private String address;
	private LocationDto country;
	private LocationDto region;
	private LocationDto city; 
	private Long nbFloors;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @return the country
	 */
	public LocationDto getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(LocationDto country) {
		this.country = country;
	}
	/**
	 * @return the region
	 */
	public LocationDto getRegion() {
		return region;
	}
	/**
	 * @param region the region to set
	 */
	public void setRegion(LocationDto region) {
		this.region = region;
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
