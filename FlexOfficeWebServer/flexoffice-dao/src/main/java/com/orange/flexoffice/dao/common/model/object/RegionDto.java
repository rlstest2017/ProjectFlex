package com.orange.flexoffice.dao.common.model.object;

/**
 * A RegionDto. 
 * @author oab
 *
 */
public class RegionDto extends LocationDto {
	
	private Long countryId;
	private String countryName;

	
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


}
