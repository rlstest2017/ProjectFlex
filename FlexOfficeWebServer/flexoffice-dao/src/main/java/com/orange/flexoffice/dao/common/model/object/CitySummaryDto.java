package com.orange.flexoffice.dao.common.model.object;

/**
 * A CitySummaryDto. 
 * @author oab
 *
 */
public class CitySummaryDto extends RegionSummaryDto {
	
	private String regionName;
	
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
	

}
