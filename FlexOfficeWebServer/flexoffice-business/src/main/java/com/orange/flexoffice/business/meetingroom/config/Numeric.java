package com.orange.flexoffice.business.meetingroom.config;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Numeric
 * @author oab
 *
 */
public class Numeric {
	
	private String roomId;
	
	private String name;

	@XmlElement(name="RoomID")
	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	
	@XmlTransient
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
