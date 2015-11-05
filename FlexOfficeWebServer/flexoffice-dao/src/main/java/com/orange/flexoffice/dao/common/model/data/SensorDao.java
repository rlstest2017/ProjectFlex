package com.orange.flexoffice.dao.common.model.data;

import java.util.Date;

import com.orange.flexoffice.dao.common.model.enumeration.E_SensorStatus;
import com.orange.flexoffice.dao.common.model.enumeration.E_SensorType;

/**
 * SensorDao
 * @author oab
 *
 */
public class SensorDao extends AbstractData {
	
	private String identifier; // sensor back id on hexa 4 octets
	private String name;
	private E_SensorType type;
	private String profile; // profile back as a5-07-01 & a5-04-01
	private String description;
	private E_SensorStatus status;
	private Integer roomId;
	private Date lastMeasureDate;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public E_SensorType getType() {
		return type;
	}
	public void setType(E_SensorType type) {
		this.type = type;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public E_SensorStatus getStatus() {
		return status;
	}
	public void setStatus(E_SensorStatus status) {
		this.status = status;
	}
	public Integer getRoomId() {
		return roomId;
	}
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}
	public Date getLastMeasureDate() {
		return lastMeasureDate;
	}
	public void setLastMeasureDate(Date lastMeasureDate) {
		this.lastMeasureDate = lastMeasureDate;
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
