package com.orange.flexoffice.dao.common.model.data;

/**
 * A City. 
 * @author oab
 *
 */
public class CityDao extends AbstractData {
	
	private String name;
	private Long regionId; 
	
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

	@Override
	public void setColumnId(String columnId) {
		setId(Long.valueOf(columnId));
	}
	@Override
	public String getColumnId() {
		return getId().toString();
	}
	

}
