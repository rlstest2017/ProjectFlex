package com.orange.flexoffice.business.common.service.data.impl;

import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.naming.AuthenticationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import com.orange.flexoffice.business.common.exception.DataNotExistsException;
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
	public UserDao processLogin(String authorization, Boolean isFromAdminUi, Object object) throws DataNotExistsException, AuthenticationException {
	    LOGGER.debug("authorization parameter is :" + authorization);
    	UserDao user = new UserDao();
    	
		if (authorization != null && authorization.startsWith("Basic")) {
	        // Authorization: Basic base64credentials
	        String base64Credentials = authorization.substring("Basic".length()).trim();
	        String credentials = new String(Base64Utils.decodeFromString(base64Credentials),
	                Charset.forName("UTF-8"));
	        
	        String email = null;
	        String password = null;
	        String accessToken = null;
	        
	        if (isFromAdminUi) {
	        	// credentials = email:password
		        final String[] values = credentials.split(":",2);
		        email = values[0].trim();
		        password = values[1].trim();
		        LOGGER.debug("email in processLogin() method is :" + email);
		        LOGGER.debug("password in processLogin() method is :" + password);
		        accessToken = createAccessToken(email, password);
	        } else {
	        	// credentials = email
	        	email =credentials;
	        	LOGGER.debug("email in processLogin() method is :" + email);
		        accessToken = createAccessToken(email, null);
	        }
	        
	        Date expiredTokenDate = createExpiredDate();
	        
	        try {
	        	user.setEmail(email);
	        	user.setAccessToken(accessToken.trim());
	        	user.setExpiredTokenDate(expiredTokenDate);
		    	
	        	if (isFromAdminUi) {
	        		// find user by mail & password
		        	user.setPassword(password);
		        	userRepository.findByUserEmailAndPassword(user);
	        	} else {
	        		userRepository.findByUserEmail(email);
	        	}
	        	
	    		// Update UserDao
				userRepository.updateUserByEmail(user);
				
				user.setIsCreatedFromUserui(false);

			} catch(IncorrectResultSizeDataAccessException e ) {
				LOGGER.error("UserManager.findByUserMail : User by email #" + email + " is not found", e);
				if (isFromAdminUi) {
					throw new DataNotExistsException("UserManager.findByUserMail : User by email #" + email + " is not found");
				} else {
					if ((object != null)&&(object instanceof UserDao)) {
								user.setFirstName((String)((UserDao) object).getFirstName());
								user.setLastName((String)((UserDao) object).getLastName());
					}
					user.setIsCreatedFromUserui(true);
					// Create new user
					userRepository.saveUserFromUserUI(user);
				}
			}
		} else {
			LOGGER.error("UserManager.processLogin : Authorization parameter is null or wrong format");
			throw new AuthenticationException("UserManager.processLogin : Authorization parameter is null or wrong format");
		}
			
		
		return user;
	}
	
	@Override
	public void processLogout(String accessToken) {
		UserDao user = new UserDao();
		user.setAccessToken(accessToken);
		userRepository.updateAccessToken(user);
	}
	
	/**
	 * This method is used in Spring-Security authentication process
	 */
	@Override
	public Boolean checkToken(String token) {

		try {
			UserDao user = userRepository.findByAccessToken(token);
			
			if (user == null)  {
				LOGGER.debug("checkToken return : accessToken not found in DB.");
				return false;
			}else if (user.getExpiredTokenDate().before(new Date())) {
				LOGGER.debug("checkToken return : The accessToken is expired at :" + user.getExpiredTokenDate());
				return false;
			} else {
				LOGGER.debug("checkToken return : accessToken is valid.");
				return true;
			}
		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.error("UserManager.findByAccessToken : User by token #" + token + " DataAccessException", e);
			return false;
		}
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
		//TODO userRepository.countActive(String lastConnectionDuration);
		return 3L;
	}

	private String createAccessToken(String email, String password) {
		String keySource = null;
		if (password != null) {
			keySource = email + ":" + password + ":" + (new Date()).getTime();
		} else {
			keySource = email + ":" + (new Date()).getTime();
		}
		LOGGER.debug("Original keySource is " + keySource);
		String token = Base64Utils.encodeToString(keySource.getBytes());
		LOGGER.debug("Generated keySource is " + token);
		return token;
	}
	
	private Date createExpiredDate() {
		Date now = new Date();
		LOGGER.debug("Date now :" + now);
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.DAY_OF_YEAR, 1); // <--1 jour -->
		Date tomorrow = cal.getTime();
		LOGGER.debug("Date tomorrow :" + tomorrow);		
		return tomorrow;
	}

	
}
