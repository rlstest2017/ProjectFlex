package com.orange.flexoffice.dao.common.model.data;

/**
 * Defines a generic representation 
 * @author oab
 */
public interface Data {
	
	void setId(Long id);
	
	/**
	 * Gets the technical ID of the data.
	 * 
	 * @return an ID
	 */
	Long getId();
	
	void setColumnId(String columnId);
	
	/**
	 * Gets the technical ID of the data convert on String.
	 * 
	 * @return an ID
	 */
	String getColumnId();
	
}
