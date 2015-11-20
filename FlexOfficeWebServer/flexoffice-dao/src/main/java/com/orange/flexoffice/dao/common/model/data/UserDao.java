package com.orange.flexoffice.dao.common.model.data;

import java.util.Date;

/**
 * A user. 
 * 
 * @author oab
 *
 */
public class UserDao extends AbstractData {
	
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String accessToken;
	private String role;
	private Date lastConnectionDate;
	private Boolean expiredTokenDate;
	private Date isCreatedFromUserui;
	
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
	public Boolean getExpiredTokenDate() {
		return expiredTokenDate;
	}
	public void setExpiredTokenDate(Boolean expiredTokenDate) {
		this.expiredTokenDate = expiredTokenDate;
	}
	public Date getIsCreatedFromUserui() {
		return isCreatedFromUserui;
	}
	public void setIsCreatedFromUserui(Date isCreatedFromUserui) {
		this.isCreatedFromUserui = isCreatedFromUserui;
	}
	@Override
	public void setColumnId(String columnId) {
		setId(Long.valueOf(columnId));
	}
	@Override
	public String getColumnId() {
		if (getId() != null) {
			return getId().toString();
		} else {
			return "0";
		}
	}
	

}
