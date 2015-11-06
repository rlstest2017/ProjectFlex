package com.orange.flexoffice.dao.userui.model.data;

/**
 * A user. 
 * 
 * @author oab
 *
 */
public class UserFlexoffice extends AbstractData {
	
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	
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
	
	public void setColumnEmail(String columnMail) {
		setEmail(columnMail);
	}
	
	public String getColumnEmail() {
		return getEmail();
	}
	
	@Override
	public void setColumnId(String columnId) {
		setId(Long.valueOf(columnId));
	}
	@Override
	public String getColumnId() {
		return getId().toString();
	}
	
	@Override
	public void setRowId(String rowId) {
		setId(Long.valueOf(rowId));		
	}
	@Override
	public String getRowId() {
		return getId().toString();
	}
	@Override
	public void setRating(Float rating) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Float getRating() {
		// TODO Auto-generated method stub
		return null;
	}

}