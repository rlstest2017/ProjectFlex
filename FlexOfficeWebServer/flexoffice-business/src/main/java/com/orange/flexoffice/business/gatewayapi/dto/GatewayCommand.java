package com.orange.flexoffice.business.gatewayapi.dto;

import com.orange.flexoffice.business.gatewayapi.enums.EnumCommandModel;

/**
 * GatewayCommand
 * @author oab
 *
 */
public class GatewayCommand {

	private long roomId;
	private EnumCommandModel command;
	
	public long getRoomId() {
		return roomId;
	}
	public void setRoomId(long roomId) {
		this.roomId = roomId;
	}
	public EnumCommandModel getCommand() {
		return command;
	}
	public void setCommand(EnumCommandModel command) {
		this.command = command;
	}
	
}
