package com.orange.meetingroom.connector.flexoffice.model.response;

import com.orange.meetingroom.connector.flexoffice.enums.EnumCommand;

/**
 * DashboardOutput
 * @author oab
 *
 */
public class DashboardOutput {

	EnumCommand command;
	
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
