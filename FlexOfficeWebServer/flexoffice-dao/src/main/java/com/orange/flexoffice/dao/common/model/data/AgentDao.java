package com.orange.flexoffice.dao.common.model.data;

import java.util.Date;

/**
 * AgentDao
 * @author oab
 *
 */
public class AgentDao extends AbstractData {
	
	private String name;
	private String macAddress;
	private String description;
	private String status;
	private Date lastMeasureDate;
	private String command;
	private Long meetingroomId;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
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
	public Date getLastMeasureDate() {
		return lastMeasureDate;
	}
	public void setLastMeasureDate(Date lastMeasureDate) {
		this.lastMeasureDate = lastMeasureDate;
	}
	/**
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}
	/**
	 * @param command the command to set
	 */
	public void setCommand(String command) {
		this.command = command;
	}
	@Override
	public void setColumnId(String columnId) {
		setId(Long.valueOf(columnId));
	}
	@Override
	public String getColumnId() {
		return getId().toString();
	}
	public Long getMeetingroomId() {
		return meetingroomId;
	}
	public void setMeetingroomId(Long meetingroomId) {
		this.meetingroomId = meetingroomId;
	}
	

}
