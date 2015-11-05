package com.orange.flexoffice.dao.common.model.data;

import java.util.Date;

import com.orange.flexoffice.dao.common.model.enumeration.E_UserRole;

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
	private E_UserRole role;
	private Date lastConnectionDate; 
	
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
	public E_UserRole getRole() {
		return role;
	}
	public void setRole(E_UserRole role) {
		this.role = role;
	}
	public Date getLastConnectionDate() {
		return lastConnectionDate;
	}
	public void setLastConnectionDate(Date lastConnectionDate) {
		this.lastConnectionDate = lastConnectionDate;
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
