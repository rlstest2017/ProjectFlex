package com.orange.flexoffice.dao.gatewayapi.model.object;

import java.util.List;

import com.orange.flexoffice.dao.gatewayapi.model.data.Log;
import com.orange.flexoffice.dao.gatewayapi.model.data.Preference;
import com.orange.flexoffice.dao.gatewayapi.model.data.Relationship;


/**
 * Aggregates informations about a user.
 * 
 * @author Guillaume Mouricou
 *
 */
public class User {
	
	/**
	 * A user ID.
	 */
	private String id;
	/**
	 * All logs for this user.
	 */
	private List<Log> logs;
	/**
	 * All preferences for this user.
	 */
	private List<Preference> preferences;
	/**
	 * All friends for this user.
	 */
	private List<Relationship> friends;
	/**
	 * All followers for this user.
	 */
	private List<Relationship> followers;
	
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
	public List<Preference> getPreferences() {
		return preferences;
	}
	public void setPreferences(List<Preference> preferences) {
		this.preferences = preferences;
	}
	public List<Relationship> getFriends() {
		return friends;
	}
	public void setFriends(List<Relationship> friends) {
		this.friends = friends;
	}
	public List<Relationship> getFollowers() {
		return followers;
	}
	public void setFollowers(List<Relationship> followers) {
		this.followers = followers;
	}

}
