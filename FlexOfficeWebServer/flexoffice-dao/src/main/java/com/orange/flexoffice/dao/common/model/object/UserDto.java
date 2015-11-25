package com.orange.flexoffice.dao.common.model.object;

import java.util.Date;

/**
 * A user. 
 * 
 * @author oab
 *
 */
public class UserDto {
	
	private String id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String accessToken;
	private String role;
	private Date lastConnectionDate;
	private Boolean isCreatedFromUserui;
	private Date expiredTokenDate;
	private String roomId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Date getLastConnectionDate() {
		return lastConnectionDate;
	}
	public void setLastConnectionDate(Date lastConnectionDate) {
		this.lastConnectionDate = lastConnectionDate;
	}
	public Boolean getIsCreatedFromUserui() {
		return isCreatedFromUserui;
	}
	public void setIsCreatedFromUserui(Boolean isCreatedFromUserui) {
		this.isCreatedFromUserui = isCreatedFromUserui;
	}
	public Date getExpiredTokenDate() {
		return expiredTokenDate;
	}
	public void setExpiredTokenDate(Date expiredTokenDate) {
		this.expiredTokenDate = expiredTokenDate;
	}

}
