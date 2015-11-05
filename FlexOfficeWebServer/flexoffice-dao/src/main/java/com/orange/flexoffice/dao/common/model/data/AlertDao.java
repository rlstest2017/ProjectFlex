package com.orange.flexoffice.dao.common.model.data;

import java.util.Date;

import com.orange.flexoffice.dao.common.model.enumeration.E_DeviceType;

/**
 * AlertDao
 * @author oab
 *
 */
public class AlertDao extends AbstractData {
	
	private String name;
	private E_DeviceType type;
	private Date lastNotification;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public E_DeviceType getType() {
		return type;
	}
	public void setType(E_DeviceType type) {
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
