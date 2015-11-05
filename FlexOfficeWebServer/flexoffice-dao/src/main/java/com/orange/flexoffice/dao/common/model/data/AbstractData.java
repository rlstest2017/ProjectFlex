package com.orange.flexoffice.dao.common.model.data;

/**
 * Specifies commons properties shared by the different types of data.
 * 
 * @author oab
 *
 */
public abstract class AbstractData implements Data {
	
	/**
	 * The data ID.
	 */
	private Long id;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

}
