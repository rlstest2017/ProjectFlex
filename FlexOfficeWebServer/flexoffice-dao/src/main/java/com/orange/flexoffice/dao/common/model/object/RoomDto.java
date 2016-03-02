package com.orange.flexoffice.dao.common.model.object;

import java.util.ArrayList;
import java.util.Date;
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
	 * A serial Room ID.
	 */
	private Long id;
	private String name;
	private String address;
	private Integer capacity;
	private String description;
	private E_RoomStatus status;
	private E_RoomType type;
	private GatewayDao gateway;
	private UserDao user;
	private List<SensorDao> sensors;
	private Long occupancyTimeOut;
	private Date lastMeasureDate; // the last time since the status has been modified
	private Double temperature;
	private Double humidity;
	
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
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
	public Long getOccupancyTimeOut() {
		return occupancyTimeOut;
	}
	public void setOccupancyTimeOut(Long occupancyTimeOut) {
		this.occupancyTimeOut = occupancyTimeOut;
	}
	/**
	 * @return the lastMeasureDate
	 */
	public Date getLastMeasureDate() {
		return lastMeasureDate;
	}
	/**
	 * @param lastMeasureDate the lastMeasureDate to set
	 */
	public void setLastMeasureDate(Date lastMeasureDate) {
		this.lastMeasureDate = lastMeasureDate;
	}
	/**
	 * @return the temperature
	 */
	public Double getTemperature() {
		return temperature;
	}
	/**
	 * @param temperature the temperature to set
	 */
	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}
	/**
	 * @return the humidity
	 */
	public Double getHumidity() {
		return humidity;
	}
	/**
	 * @param humidity the humidity to set
	 */
	public void setHumidity(Double humidity) {
		this.humidity = humidity;
	}

}
