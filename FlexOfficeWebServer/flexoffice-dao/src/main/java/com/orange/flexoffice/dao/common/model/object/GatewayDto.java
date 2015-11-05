package com.orange.flexoffice.dao.common.model.object;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.orange.flexoffice.dao.common.model.data.RoomDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_GatewayStatus;

/**
 * GatewayDto
 * @author oab
 *
 */
public class GatewayDto {
	
	/**
	 * A serial Gateway ID.
	 */
	private String id;
	private String name;
	private String macAdress;
	private String description;
	private E_GatewayStatus status;
	private boolean isActivated;
	/**
	 * Rooms infos
	 */
	private List<RoomDao> rooms;
	private Date lastPollingDate;
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
	public E_GatewayStatus getStatus() {
		return status;
	}
	public void setStatus(E_GatewayStatus status) {
		this.status = status;
	}
	public boolean isActivated() {
		return isActivated;
	}
	public void setActivated(boolean isActivated) {
		this.isActivated = isActivated;
	}
	public List<RoomDao> getRooms() {
		if (rooms == null) {
			rooms = new ArrayList<RoomDao>();
		}
		return rooms;
	}
	public void setRooms(List<RoomDao> rooms) {
		this.rooms = rooms;
	}
	public Date getLastPollingDate() {
		return lastPollingDate;
	}
	public void setLastPollingDate(Date lastPollingDate) {
		this.lastPollingDate = lastPollingDate;
	}
	
	

}
