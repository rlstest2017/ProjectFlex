package com.orange.flexoffice.dao.common.model.object;

/**
 * A RegionDto. 
 * @author oab
 *
 */
public class RegionDto extends LocationDto {
	
	private LocationDto country;
		
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

}
