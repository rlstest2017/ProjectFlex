package com.orange.flexoffice.dao.common.model.object;

/**
 * A BuildingDto. 
 * @author oab
 *
 */
public class CityDto {
	
	/**
	 * A serial ID.
	 */
	private Long id;
	private String name;
	private LocationDto country;
	private LocationDto region;
	
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


}
