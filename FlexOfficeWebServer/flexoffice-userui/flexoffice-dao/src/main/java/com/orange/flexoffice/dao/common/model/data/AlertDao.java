package com.orange.flexoffice.dao.common.model.data;

import java.util.Date;

/**
 * AlertDao
 * @author oab
 *
 */
public class AlertDao extends AbstractData {
	
	private String name;
	private String type;
	private Date lastNotification;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getLastNotification() {
		return lastNotification;
	}
	public void setLastNotification(Date lastNotification) {
		this.lastNotification = lastNotification;
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
