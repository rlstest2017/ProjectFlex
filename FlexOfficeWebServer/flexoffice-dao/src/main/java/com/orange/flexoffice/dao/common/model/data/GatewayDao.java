package com.orange.flexoffice.dao.common.model.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.orange.flexoffice.dao.common.model.enumeration.E_GatewayStatus;

/**
 * GatewayDao
 * @author oab
 *
 */
public class GatewayDao extends AbstractData {
	
	private String name;
	private String macAdress;
	private String description;
	private E_GatewayStatus status;
	private boolean isActivated;
	private List<Integer> roomsId;
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
	public List<Integer> getRoomsId() {
		if(roomsId == null){
			roomsId = new ArrayList<Integer>();
        }
		return roomsId;
	}
	public void setRoomsId(List<Integer> roomsId) {
		this.roomsId = roomsId;
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
