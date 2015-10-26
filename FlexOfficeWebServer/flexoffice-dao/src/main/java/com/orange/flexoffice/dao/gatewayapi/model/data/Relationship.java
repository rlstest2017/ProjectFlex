package com.orange.flexoffice.dao.gatewayapi.model.data;
/**
 * A {@link Relationship} describes a relation between 2 user.
 * 
 * @author Guillaume Mouricou
 *
 */
public class Relationship extends AbstractData {

	private String userId;
	private String friendId;
	private Float rating;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFriendId() {
		return friendId;
	}
	public void setFriendId(String friendId) {
		this.friendId = friendId;
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
		setFriendId(rowId);		
	}
	@Override
	public String getRowId() {
		return getFriendId();
	}
	
}
