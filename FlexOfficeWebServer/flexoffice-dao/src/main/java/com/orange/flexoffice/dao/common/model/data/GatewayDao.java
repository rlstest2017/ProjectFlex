package com.orange.flexoffice.dao.common.model.data;

import java.util.Date;

/**
 * GatewayDao
 * @author oab
 *
 */
public class GatewayDao extends AbstractData {
	
	private String name;
	private String macAdress;
	private String description;
	private String status;
	private Date lastPollingDate;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMacAdress() {
		return macAdress;
	}
	public void setMacAdress(String macAdress) {
		this.macAdress = macAdress;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getLastPollingDate() {
		return lastPollingDate;
	}
	public void setLastPollingDate(Date lastPollingDate) {
		this.lastPollingDate = lastPollingDate;
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
