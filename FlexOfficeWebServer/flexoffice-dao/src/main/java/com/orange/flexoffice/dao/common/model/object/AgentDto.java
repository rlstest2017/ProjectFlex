package com.orange.flexoffice.dao.common.model.object;

import java.util.Date;

import com.orange.flexoffice.dao.common.model.data.MeetingRoomDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_AgentStatus;
import com.orange.flexoffice.dao.common.model.enumeration.E_CommandModel;

/**
 * AgentDto
 * @author oab
 *
 */
public class AgentDto {
	
	/**
	 * A serial Agent ID.
	 */
	private String id;
	private String name;
	private String macAddress;
	private String description;
	private E_AgentStatus status;
	private E_CommandModel command;

	/**
	 * Meetingroom infos
	 */
	private MeetingRoomDao meetingroom;
	private Date lastMeasureDate;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	public E_AgentStatus getStatus() {
		return status;
	}
	public void setStatus(E_AgentStatus status) {
		this.status = status;
	}
	public MeetingRoomDao getMeetingRoom() {
		return meetingroom;
	}
	public void setMeetingRoom(MeetingRoomDao meeetingroom) {
		this.meetingroom = meeetingroom;
	}
	public Date getLastMeasureDate() {
		return lastMeasureDate;
	}
	public void setLastMeasureDate(Date lastPollingDate) {
		this.lastMeasureDate = lastPollingDate;
	}
	public E_CommandModel getCommand() {
		return command;
	}
	public void setCommand(E_CommandModel command) {
		this.command = command;
	}
	
	

}
