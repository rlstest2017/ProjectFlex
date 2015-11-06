package com.orange.flexoffice.dao.common.model.data;

import java.util.ArrayList;
import java.util.List;

import com.orange.flexoffice.dao.common.model.enumeration.E_RoomStatus;
import com.orange.flexoffice.dao.common.model.enumeration.E_RoomType;

/**
 * RoomDao
 * @author oab
 *
 */
public class RoomDao extends AbstractData {
	
	private String name;
	private String adress;
	private Integer capacity;
	private String description;
	private String status;
	private String type;
	private Integer gatewayId;
	private Integer userId;
	private org.postgresql.jdbc4.Jdbc4Array sensorsId;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	public Integer getCapacity() {
		return capacity;
	}
	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Integer getGatewayId() {
		return gatewayId;
	}
	public void setGatewayId(Integer gatewayId) {
		this.gatewayId = gatewayId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public org.postgresql.jdbc4.Jdbc4Array getSensorsId() {
		return sensorsId;
	}
	public void setSensorsId(org.postgresql.jdbc4.Jdbc4Array sensorsId) {
		this.sensorsId = sensorsId;
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
