package com.orange.flexoffice.business.gatewayapi.service.object.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.gatewayapi.service.object.UserManager;
import com.orange.flexoffice.dao.gateway.repository.data.jdbc.LogRepository;
import com.orange.flexoffice.dao.gateway.repository.data.jdbc.PreferenceRepository;
import com.orange.flexoffice.dao.gateway.repository.data.jdbc.RelationshipRepository;
import com.orange.flexoffice.dao.gatewayapi.model.data.Log;
import com.orange.flexoffice.dao.gatewayapi.model.data.Preference;
import com.orange.flexoffice.dao.gatewayapi.model.data.Relationship;
import com.orange.flexoffice.dao.gatewayapi.model.object.User;

@Service("userManager")
@Transactional(readOnly=true)
public class UserManagerImpl implements UserManager {
	
	@Autowired
	private LogRepository logRepository;
	@Autowired
	private PreferenceRepository preferenceRepository;
	@Autowired
	private RelationshipRepository relationshipRepository;
	
	public User find(String userId) {
		
		User user = new User();
		user.setId(userId);
		user.setLogs(getUserLogs(userId));
		user.setPreferences(getUserPreferences(userId));
		user.setFriends(getUserFriends(userId));
		user.setFollowers(getUserFollowers(userId));
		
		return user;
	}
	
	public List<Log> getUserLogs(String userId) {
		return logRepository.findByUserId(userId);
	}
	public List<Preference> getUserPreferences(String userId) {
		return preferenceRepository.findByUserId(userId);
	}
	public List<Relationship> getUserFriends(String userId) {
		return relationshipRepository.findByUserId(userId);
	}
	public List<Relationship> getUserFollowers(String userId) {
		return relationshipRepository.findByFriendId(userId);
	}

}
