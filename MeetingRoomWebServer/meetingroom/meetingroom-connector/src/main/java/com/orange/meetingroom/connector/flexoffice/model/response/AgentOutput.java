package com.orange.meetingroom.connector.flexoffice.model.response;

import com.orange.meetingroom.connector.flexoffice.enums.EnumCommand;

/**
 * AgentOutput
 * @author oab
 *
 */
public class AgentOutput {

	String meetingRoomExternalId;
	EnumCommand command;
	
	/**
	 * @return the meetingRoomExternalId
	 */
	public String getMeetingRoomExternalId() {
		return meetingRoomExternalId;
	}
	/**
	 * @param meetingRoomExternalId the meetingRoomExternalId to set
	 */
	public void setMeetingRoomExternalId(String meetingRoomExternalId) {
		this.meetingRoomExternalId = meetingRoomExternalId;
	}
	/**
	 * @return the command
	 */
	public EnumCommand getCommand() {
		return command;
	}
	/**
	 * @param command the command to set
	 */
	public void setCommand(EnumCommand command) {
		this.command = command;
	}
	
	
}
