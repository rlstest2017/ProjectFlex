package com.orange.flexoffice.dao.gatewayapi.model.object;

import java.util.List;

import com.orange.flexoffice.dao.gatewayapi.model.data.Characteristic;
import com.orange.flexoffice.dao.gatewayapi.model.data.Log;

/**
 * Aggregates informations about an item.
 * 
 * @author Guillaume Mouricou
 *
 */
public class Item {
	
	/**
	 * The item ID
	 */
	private String id;
	/**
	 * All logs for this item.
	 */
	private List<Log> logs;
	/**
	 * All characteristics for this item.
	 */
	private List<Characteristic> characteristics;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<Log> getLogs() {
		return logs;
	}
	public void setLogs(List<Log> logs) {
		this.logs = logs;
	}
	public List<Characteristic> getCharacteristics() {
		return characteristics;
	}
	public void setCharacteristics(List<Characteristic> characteristics) {
		this.characteristics = characteristics;
	}
	
	
}
