package com.orange.flexoffice.dao.gatewayapi.model.object;

import java.util.List;

import com.orange.flexoffice.dao.gatewayapi.model.data.Characteristic;
import com.orange.flexoffice.dao.gatewayapi.model.data.Preference;

/**
 * Aggregates information about a descriptor.
 * 
 *@author Guillaume Mouricou
 *
 */
public class Descriptor {
	
	/**
	 * A descriptor ID 
	 */
	private String id;
	/**
	 * All characteristics for this descriptors.
	 */
	private List<Characteristic> characteristics;
	/**
	 * All preferences for this descriptors.
	 */
	private List<Preference> preferences;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<Characteristic> getCharacteristics() {
		return characteristics;
	}
	public void setCharacteristics(List<Characteristic> characteristics) {
		this.characteristics = characteristics;
	}
	public List<Preference> getPreferences() {
		return preferences;
	}
	public void setPreferences(List<Preference> preferences) {
		this.preferences = preferences;
	}
	
	

}
