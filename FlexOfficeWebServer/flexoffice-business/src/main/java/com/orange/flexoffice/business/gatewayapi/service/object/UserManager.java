package com.orange.flexoffice.business.gatewayapi.service.object;

import java.util.List;

import com.orange.flexoffice.dao.gatewayapi.model.data.Log;
import com.orange.flexoffice.dao.gatewayapi.model.data.Preference;
import com.orange.flexoffice.dao.gatewayapi.model.data.Relationship;
import com.orange.flexoffice.dao.gatewayapi.model.object.User;

public interface UserManager {

	User find(String userId);

	List<Log> getUserLogs(String userId);

	List<Preference> getUserPreferences(String userId);

	List<Relationship> getUserFriends(String userId);

	List<Relationship> getUserFollowers(String userId);

}