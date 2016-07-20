package com.orange.flexoffice.business.meetingroom.config;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Array
 * @author oab
 *
 */
@XmlRootElement(name="array")  
public class Array {
	private String description;
	
	private Rooms rooms;
 
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@XmlElement(name="rooms")
	public Rooms getRooms() {
		return rooms;
	}

	public void setRooms(Rooms rooms) {
		this.rooms = rooms;
	}
	
	public String toString(){
		
		return getDescription();
	}



}
