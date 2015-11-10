package com.orange.flexoffice.dao.common.model.object;

import java.util.ArrayList;
import java.util.List;

import com.orange.flexoffice.dao.common.model.data.GatewayDao;
import com.orange.flexoffice.dao.common.model.data.SensorDao;
import com.orange.flexoffice.dao.common.model.data.UserDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_RoomStatus;
import com.orange.flexoffice.dao.common.model.enumeration.E_RoomType;

/**
 * RoomDao
 * @author oab
 *
 */
public class RoomDto {
	
	/**
	 * A serial Gateway ID.
	 */
	private String id;
	private String name;
	private String address;
	private Integer capacity;
	private String description;
	private E_RoomStatus status;
	private E_RoomType type;
	private GatewayDao gateway;
	private UserDao user;
	private List<SensorDao> sensors;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public E_RoomStatus getStatus() {
		return status;
	}
	public void setStatus(E_RoomStatus status) {
		this.status = status;
	}
	public E_RoomType getType() {
		return type;
	}
	public void setType(E_RoomType type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public GatewayDao getGateway() {
		return gateway;
	}
	public void setGateway(GatewayDao gateway) {
		this.gateway = gateway;
	}
	public UserDao getUser() {
		return user;
	}
	public void setUser(UserDao user) {
		this.user = user;
	}
	public List<SensorDao> getSensors() {
		if (sensors == null) {
			sensors = new ArrayList<SensorDao>();
		}
		return sensors;
	}
	public void setSensors(List<SensorDao> sensors) {
		this.sensors = sensors;
	}



}
