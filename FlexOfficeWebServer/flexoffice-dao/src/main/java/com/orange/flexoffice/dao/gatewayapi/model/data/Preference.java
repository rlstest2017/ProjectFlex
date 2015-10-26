package com.orange.flexoffice.dao.gatewayapi.model.data;


/**
 * A user preference is a rating given by a user to a descriptor.
 * 
 * @author Guillaume Mouricou
 *
 */
public class Preference extends AbstractData {
	
	/**
	 * A user ID.
	 */
	private String userId;
	/**
	 * A descriptor ID.
	 */
	private String descriptorId;
	/**
	 * A rating.
	 */
	private Float rating;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getDescriptorId() {
		return descriptorId;
	}
	public void setDescriptorId(String descriptorId) {
		this.descriptorId = descriptorId;
	}
	public Float getRating() {
		return rating;
	}
	public void setRating(Float rating) {
		this.rating = rating;
	}
	
	@Override
	public void setColumnId(String columnId) {
		setUserId(columnId);
	}
	@Override
	public String getColumnId() {
		return getUserId();
	}
	
	@Override
	public void setRowId(String rowId) {
		setDescriptorId(rowId);		
	}
	@Override
	public String getRowId() {
		return getDescriptorId();
	}
	
}
