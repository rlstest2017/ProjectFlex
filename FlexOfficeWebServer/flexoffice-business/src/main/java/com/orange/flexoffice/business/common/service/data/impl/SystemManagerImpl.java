package com.orange.flexoffice.business.common.service.data.impl;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import com.orange.flexoffice.business.common.service.data.SystemManager;
import com.orange.flexoffice.dao.common.model.data.AlertDao;
import com.orange.flexoffice.dao.common.model.data.UserDao;
import com.orange.flexoffice.dao.common.model.object.SystemDto;
import com.orange.flexoffice.dao.common.repository.data.jdbc.AlertDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.GatewayDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RoomDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.UserDaoRepository;

/**
 * Manages {@link SystemDto}.
 * 
 * @author oab
 */
@Service("SystemManager")
@Transactional
public class SystemManagerImpl implements SystemManager {
	
	private static final Logger LOGGER = Logger.getLogger(SystemManagerImpl.class);
	
	@Autowired
	private GatewayDaoRepository gatewayRepository;
	@Autowired
	private RoomDaoRepository roomRepository;
	@Autowired
	private UserDaoRepository userRepository;
	@Autowired
	private AlertDaoRepository alertRepository;
	
	@Override
	@Transactional(readOnly=true)
	public SystemDto getSystem() {
		SystemDto system = new SystemDto();
		
		Long usersActiveCount = countActiveUsers();
		Long usersCount = countUsers();
		Long gatewaysCount = countGateways();
		Long roomsCount = countRooms();
		List<AlertDao> deviceAlerts = findAllAlerts();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("usersActiveCount : " + usersActiveCount);
			LOGGER.debug("usersCount : " + usersCount);
			LOGGER.debug("gatewaysCount : " + gatewaysCount);
			LOGGER.debug("roomsCount : " + roomsCount);
			LOGGER.debug("deviceAlerts size  : " + deviceAlerts.size());
		}
		
		system.setActiveUserCount(usersActiveCount.intValue());
		system.setUserCount(usersCount.intValue());
		system.setGatewayCount(gatewaysCount.intValue());
		system.setRoomCount(roomsCount.intValue());
		system.setDeviceAlerts(deviceAlerts);
			
		return system;
	}

	@Override
	public UserDao processLogin(String authorization) {
		// TODO 
		
	    LOGGER.debug("authorization parameter is :" + authorization);
		   
		if (authorization != null && authorization.startsWith("Basic")) {
	        // Authorization: Basic base64credentials
	        String base64Credentials = authorization.substring("Basic".length()).trim();
	        String credentials = new String(Base64Utils.decodeFromString(base64Credentials),
	                Charset.forName("UTF-8"));
	        // credentials = email:password
	        final String[] values = credentials.split(":",2);
	        
	        String email = values[0];
	        String password = values[1];
	        
	        String accessToken = createAccessToken(email, password);
	        
	        LOGGER.debug("email in processLogin() method is :" + email);
	        LOGGER.debug("password in processLogin() method is :" + password);
	        
	        
	        
	        
	        
		} else {
			// TODO throw exception
		}
			
		
		return null;
	}
	
	private Long countGateways() {
		return gatewayRepository.count();
	}
	
	private Long countRooms() {
		return roomRepository.count();
	}
	
	private Long countUsers() {
		return userRepository.count();
	}
	
	private List<AlertDao> findAllAlerts() {
		return alertRepository.findAllAlerts();
	}
	
	private Long countActiveUsers() {
		// TODO get activeUsers
		//return userRepository.countActive(String lastConnectionDuration);
		return 3l;
	}

	private String createAccessToken(String email, String password) {
		String keySource = email + ":" + password + (new Date()).getTime();
		LOGGER.debug("Original keySource is " + keySource);
		String token = Base64Utils.encodeToString(keySource.getBytes());
		LOGGER.debug("Generated keySource is " + token);
		return token;
	}
	
}
