package com.orange.flexoffice.business.common.service.data.impl;

import java.nio.charset.Charset;
import java.security.InvalidParameterException;
import java.util.Date;
import java.util.List;

import javax.naming.AuthenticationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.service.data.SystemManager;
import com.orange.flexoffice.business.common.utils.DateTools;
import com.orange.flexoffice.dao.common.model.data.AlertDao;
import com.orange.flexoffice.dao.common.model.data.ConfigurationDao;
import com.orange.flexoffice.dao.common.model.data.GatewayDao;
import com.orange.flexoffice.dao.common.model.data.RoomDao;
import com.orange.flexoffice.dao.common.model.data.SensorDao;
import com.orange.flexoffice.dao.common.model.data.TeachinSensorDao;
import com.orange.flexoffice.dao.common.model.data.UserDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_ConfigurationKey;
import com.orange.flexoffice.dao.common.model.enumeration.E_TeachinStatus;
import com.orange.flexoffice.dao.common.model.enumeration.E_UserRole;
import com.orange.flexoffice.dao.common.model.object.SensorDto;
import com.orange.flexoffice.dao.common.model.object.SystemDto;
import com.orange.flexoffice.dao.common.model.object.TeachinSensorDto;
import com.orange.flexoffice.dao.common.repository.data.jdbc.AlertDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.ConfigurationDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.GatewayDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RoomDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.SensorDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.TeachinSensorsDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.UserDaoRepository;
import com.orange.flexoffice.dao.common.utils.TokenTools;

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
	private SensorDaoRepository sensorRepository;
	@Autowired
	private RoomDaoRepository roomRepository;
	@Autowired
	private UserDaoRepository userRepository;
	@Autowired
	private AlertDaoRepository alertRepository;
	@Autowired
	private ConfigurationDaoRepository configRepository;
	@Autowired
	private TeachinSensorsDaoRepository teachinRepository;
	@Autowired
	private TokenTools tokenTools;
	@Autowired
	private DateTools dateTools;
	
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
	public UserDao processLogin(String authorization, Boolean isFromAdminUi, Object object, int infosDBLength) throws DataNotExistsException, AuthenticationException {
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
		        LOGGER.info("in processLogin() email#"+email+ " password#"+password);
		        accessToken = tokenTools.createAccessToken(email, password);
	        } else {
	        	// credentials = email
	        	email =credentials;
	        	if (email.length() > infosDBLength) {
	        		LOGGER.error("Invalid email length in processLogin() method is : " + email);
	        		throw new InvalidParameterException("Invalid email length : " + email);
	        	}
	        	LOGGER.info("in processLogin() email#" + email);
		        accessToken = tokenTools.createAccessToken(email, null);
	        }
	        
	        Date expiredTokenDate = tokenTools.createExpiredDate();
	        
	        try {
	        	user.setEmail(email);
	        	user.setAccessToken(accessToken.trim());
	        	user.setExpiredTokenDate(expiredTokenDate);
	        	user.setRole(E_UserRole.ADMIN.toString());
	        	
	        	if (isFromAdminUi) {
	        		// find user by mail & password & role
		        	user.setPassword(password);
		        	UserDao returnedUser = userRepository.findByUserEmailAndPassword(user);
		        	// user id is used for clear teachin !!!
		        	user.setId(returnedUser.getId());
	        	} else {
	        		userRepository.findByUserEmail(email);
	        	}
	        	
	    		// Update UserDao
				userRepository.updateUserByEmail(user);
				
				user.setIsCreatedFromUserui(false);

			} catch(IncorrectResultSizeDataAccessException e ) {
				LOGGER.error("SystemManager.findByUserMail : User by email #" + email + " is not found", e);
				if (isFromAdminUi) {
					throw new DataNotExistsException("SystemManager.findByUserMail : User by email #" + email + " is not found");
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
			LOGGER.error("SystemManager.processLogin : Authorization parameter is null or wrong format");
			throw new AuthenticationException("SystemManager.processLogin : Authorization parameter is null or wrong format");
		}
			
		
		return user;
	}
	
	@Override
	public UserDao processLogout(String accessToken) {
		UserDao user = new UserDao();
		user.setAccessToken(accessToken);
		return userRepository.updateAccessToken(user); 
	}
	
	/**
	 * This method is used in Spring-Security authentication process
	 */
	@Override
	public Boolean checkToken(String token) {

		try {
			UserDao user = userRepository.findByAccessToken(token);
			
			if (user == null)  {
				LOGGER.info("checkToken return : accessToken not found in DB.");
				return false;
			}else if (user.getExpiredTokenDate().before(new Date())) {
				LOGGER.info("checkToken return : The accessToken is expired at :" + user.getExpiredTokenDate());
				return false;
			} else {
				LOGGER.info("checkToken return : accessToken is valid.");
				return true;
			}
		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.error("SystemManager.findByAccessToken : User by token #" + token + " DataAccessException", e);
			return false;
		}
	}
	
	@Override
	public void deleteAllTeachinSensorsByUserId(Long userId) {
		try {
			teachinRepository.findByUserId(userId);
			// the teachin is founded and matches to user Id
			teachinRepository.deleteAllTeachinSensors();	
		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.error("SystemManager.deleteAllTeachinSensorsByUserId : Teachin for user by id #" + userId + " is not found", e);
		}	
	}
	
	@Override
	public void initTeachin(String auth, Long roomId) throws DataAlreadyExistsException, DataNotExistsException {
		try {
			TeachinSensorDao teachin = teachinRepository.findByTeachinStatus();
			// the teachin is founded (teachin_status not null)
			if ( teachin.getTeachinStatus().equals(E_TeachinStatus.INITIALIZING.toString()) || teachin.getTeachinStatus().equals(E_TeachinStatus.RUNNING.toString()) ) {
				LOGGER.error("SystemManager.initTeachin : Teachin founded");
				throw new DataAlreadyExistsException("SystemManager.initTeachin : Teachin already active");
			} else { // status ENDED
				processInitTeachin(auth, roomId);
			}
		} catch(IncorrectResultSizeDataAccessException e ) {
			processInitTeachin(auth, roomId);
		}	
	}
	
	@Override
	public void updateTeachinStatusByUser(Long userId) {
		try {
			TeachinSensorDao teachin = teachinRepository.findByUserId(userId);
			// the teachin is founded and matches to user Id
			teachin.setTeachinStatus(E_TeachinStatus.ENDED.toString());
			teachinRepository.updateTeachinStatus(teachin);	
		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.error("SystemManager.updateTeachinStatusByUser : Teachin for user by id #" + userId + " is not found", e);
		}	
		
	}
	
	@Override
	public void updateTeachinStatus() throws DataNotExistsException {
		try {
			TeachinSensorDao teachin = teachinRepository.findByTeachinStatus();
			// the teachin is founded (teachin_status not null)
			if ( teachin.getTeachinStatus().equals(E_TeachinStatus.INITIALIZING.toString()) || teachin.getTeachinStatus().equals(E_TeachinStatus.RUNNING.toString()) ) {
				teachin.setTeachinStatus(E_TeachinStatus.ENDED.toString());
				teachinRepository.updateTeachinStatus(teachin);	
			} 
		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.error("SystemManager.updateTeachinStatus : Teachin not found", e);
			throw new DataNotExistsException("SystemManager.updateTeachinStatus : Teachin not found");
		}	
		
	}
	
	@Override
	public TeachinSensorDto getTeachin() throws DataNotExistsException {
		try {
			TeachinSensorDao teachin = teachinRepository.findByTeachinStatus();
			
			TeachinSensorDto teachinDto = new TeachinSensorDto();
			teachinDto.setRoomId(teachin.getRoomId());
			teachinDto.setTeachinStatus(teachin.getTeachinStatus());
			// Get GatewayMacAddress
			GatewayDao gateway =gatewayRepository.findOne(teachin.getGatewayId().longValue());
			teachinDto.setGatewayMacAddress(gateway.getMacAddress());
			
			// the teachin is founded (teachin_status not null)
			if (!teachin.getTeachinStatus().equals(E_TeachinStatus.INITIALIZING.toString())) {
				// get teached sensors List
				List<TeachinSensorDao> teachinList = teachinRepository.findAllTeachinSensors();
				for (TeachinSensorDao teachinSensorDao : teachinList) {
					if (teachinSensorDao.getTeachinStatus() == null) {	
						SensorDto sensor = new SensorDto();
						sensor.setSensorIdentifier(teachinSensorDao.getSensorIdentifier());
						sensor.setSensorStatus(teachinSensorDao.getSensorStatus());
						teachinDto.getSensors().add(sensor);
					}
				}
					
			} 
			
			return teachinDto;
			
		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.error("SystemManager.getTeachin : Teachin not found", e);
			throw new DataNotExistsException("SystemManager.getTeachin : Teachin not found");
		}
	}
	
	@Override
	public void submitTeachin(List<String> sensorIdentifiers) throws DataNotExistsException {
		try {
			TeachinSensorDao teachin = teachinRepository.findByTeachinStatus();
			
			// associate sensor to room
			for (String ident : sensorIdentifiers) {
				SensorDao sensor = new SensorDao();
				sensor.setIdentifier(ident);
				sensor.setRoomId(teachin.getRoomId());
				sensorRepository.updateSensorRoomId(sensor);
			}
			
			
		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.error("SystemManager.submitTeachin : Teachin not found", e);
			throw new DataNotExistsException("SystemManager.submitTeachin : Teachin not found");
		}
		
	}
	
	@Transactional(readOnly=true)
	private Long countGateways() {
		return gatewayRepository.count();
	}
	
	@Transactional(readOnly=true)
	private Long countRooms() {
		return roomRepository.count();
	}
	
	@Transactional(readOnly=true)
	private Long countUsers() {
		return userRepository.count();
	}
	
	@Transactional(readOnly=true)
	private List<AlertDao> findAllAlerts() {
		List<AlertDao> alertList = alertRepository.findAllAlerts();
		for (AlertDao alertDao : alertList) {
			if (alertDao.getGatewayId() != null) {
				GatewayDao gateway = gatewayRepository.findByGatewayId(Long.valueOf(alertDao.getGatewayId()));
				alertDao.setName(gateway.getName() + " " + alertDao.getName());
			} else if (alertDao.getSensorId() != null) {
				SensorDao sensor = sensorRepository.findOne(Long.valueOf(alertDao.getSensorId()));
				alertDao.setName(sensor.getName() + " " + alertDao.getName());
			} 
		}
		return alertList;
	}
	
	@Transactional(readOnly=true)
	private Long countActiveUsers() {
		// get activeUsers
		ConfigurationDao lastConnectionDuration = configRepository.findByKey(E_ConfigurationKey.LAST_CONNECTION_DURATION.toString());
		String lastConnectionDurationValue = lastConnectionDuration.getValue();
		Date date = dateTools.lastConnexionDate(lastConnectionDurationValue);
		Long count = userRepository.countActiveUsers(date);
		return count;
	}

	/**
	 * processInitTeachin
	 * @param auth
	 * @param roomId
	 * @throws DataNotExistsException
	 */
	private void processInitTeachin(String auth, Long roomId) throws DataNotExistsException {
		
		teachinRepository.deleteAllTeachinSensors();
		try {
			RoomDao room = roomRepository.findOne(roomId);
			TeachinSensorDao newTeachin = new TeachinSensorDao();
			newTeachin.setRoomId(roomId.intValue());
			newTeachin.setGatewayId(room.getGatewayId().intValue());
			newTeachin.setTeachinStatus(E_TeachinStatus.INITIALIZING.toString());
			
			// get user 
			UserDao user = userRepository.findByAccessToken(auth);
			newTeachin.setUserId(user.getId().intValue());
			// save teachin
			LOGGER.debug("newTeachin room is :" + newTeachin.getRoomId());
			teachinRepository.saveTechinStatus(newTeachin);
			
		} catch(IncorrectResultSizeDataAccessException ex ) {
			LOGGER.error("SystemManager.initTeachin : roomId# : " + roomId + "not found");
			throw new DataNotExistsException("SystemManager.initTeachin : RoomId not found");
			
		}	
	}

		
}
