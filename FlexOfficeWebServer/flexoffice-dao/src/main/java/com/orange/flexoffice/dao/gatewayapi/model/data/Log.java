package com.orange.flexoffice.dao.gatewayapi.model.data;

/**
 * A log of usage describe a relation between a user and an item.
 * 
 * @author Guillaume Mouricou
 *
 */
public class Log extends AbstractData {
	
	/**
	 * A user ID.
	 */
	private String userId;
	/**
	 * An item ID.
	 */
	private String itemId;
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
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
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
		setItemId(rowId);		
	}
	@Override
	public String getRowId() {
		return getItemId();
	}

}
