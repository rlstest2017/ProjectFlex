package com.orange.flexoffice.adminui.ws.endPoint.data.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.AuthenticationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.adminui.ws.endPoint.data.SystemEndpoint;
import com.orange.flexoffice.adminui.ws.model.DeviceAlertSummary;
import com.orange.flexoffice.adminui.ws.model.ECommandModel;
import com.orange.flexoffice.adminui.ws.model.EDeviceStatus;
import com.orange.flexoffice.adminui.ws.model.EDeviceType;
import com.orange.flexoffice.adminui.ws.model.EMeetingroomType;
import com.orange.flexoffice.adminui.ws.model.ERoomType;
import com.orange.flexoffice.adminui.ws.model.ESensorType;
import com.orange.flexoffice.adminui.ws.model.ETeachinSensorStatus;
import com.orange.flexoffice.adminui.ws.model.ETeachinStatus;
import com.orange.flexoffice.adminui.ws.model.ObjectFactory;
import com.orange.flexoffice.adminui.ws.model.Teachin;
import com.orange.flexoffice.adminui.ws.model.TeachinSensor;
import com.orange.flexoffice.adminui.ws.model.Token;
import com.orange.flexoffice.adminui.ws.utils.ErrorMessageHandler;
import com.orange.flexoffice.business.common.enums.EnumErrorModel;
import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.service.data.SystemManager;
import com.orange.flexoffice.business.common.service.data.TestManager;
import com.orange.flexoffice.dao.common.model.data.AlertDao;
import com.orange.flexoffice.dao.common.model.data.UserDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_SensorTeachinStatus;
import com.orange.flexoffice.dao.common.model.enumeration.E_TeachinStatus;
import com.orange.flexoffice.dao.common.model.object.SensorDto;
import com.orange.flexoffice.dao.common.model.object.SystemDto;
import com.orange.flexoffice.dao.common.model.object.TeachinSensorDto;

/**
 * SystemEndpointImpl
 * @author oab
 *
 */
public class SystemEndpointImpl implements SystemEndpoint {
	
	private static final Logger LOGGER = Logger.getLogger(SystemEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();
		
	@Autowired
	private SystemManager systemManager;
	
	@Autowired
	private TestManager testManager;
	
	@Autowired
	private ErrorMessageHandler errorMessageHandler;
	
	@Override
	public com.orange.flexoffice.adminui.ws.model.System getSystem() {
		try {
			
			LOGGER.debug( "Begin call doGet method for SystemEndpoint at: " + new Date() );
			
			SystemDto data = systemManager.getSystem();
			
			if (data == null) {
				LOGGER.debug("data is null");
			}
			com.orange.flexoffice.adminui.ws.model.System system = factory.createSystem();
		
			system.setActiveUserCount(BigInteger.valueOf(data.getActiveUserCount()));
			system.setUserCount(BigInteger.valueOf(data.getUserCount()));
			system.setGatewayCount(BigInteger.valueOf(data.getGatewayCount()));
			system.setActiveGatewayCount(BigInteger.valueOf(data.getActiveGatewayCount()));
			system.setRoomCount(BigInteger.valueOf(data.getRoomCount()));
			system.setFreeRoomCount(BigInteger.valueOf(data.getFreeRoomCount()));
			system.setOccupiedRoomCount(BigInteger.valueOf(data.getOccupiedRoomCount()));
			system.setAgentCount(BigInteger.valueOf(data.getAgentCount()));
			system.setActiveAgentCount(BigInteger.valueOf(data.getActiveAgentCount()));
			system.setMeetingroomCount(BigInteger.valueOf(data.getMeetingroomCount()));
			system.setFreeMeetingroomCount(BigInteger.valueOf(data.getFreeMeetingroomCount()));
			system.setOccupiedMeetingroomCount(BigInteger.valueOf(data.getOccupiedMeetingroomCount()));
			
			List<EDeviceStatus> deviceStatuses = constructDeviceStatuses();
			List<EDeviceStatus> deviceStatusesToRet = system.getDeviceStatuses();
			for (EDeviceStatus eDeviceStatus : deviceStatuses) {
				deviceStatusesToRet.add(eDeviceStatus);
			}
			
			List<ESensorType> sensorTypes = constructSensortypes();
			List<ESensorType> sensorTypesToRet = system.getSensorTypes();
			for (ESensorType eSensorType : sensorTypes) {
				sensorTypesToRet.add(eSensorType);
			}
			
			List<ERoomType> roomTypes = constructRoomtypes();
			List<ERoomType> roomTypesToRet = system.getRoomTypes();
			for (ERoomType eRoomType : roomTypes) {
				roomTypesToRet.add(eRoomType);
			}
			
			List<EMeetingroomType> meetingRoomTypes = constructMeetingRoomtypes();
			List<EMeetingroomType> meetingRoomTypesToRet = system.getMeetingRoomTypes();
			for (EMeetingroomType eMeetingRoomType : meetingRoomTypes) {
				meetingRoomTypesToRet.add(eMeetingRoomType);
			}
			
			List<ECommandModel> commands = constructCommands();
			List<ECommandModel> commandsToRet = system.getCommands();
			for (ECommandModel eCommand : commands) {
				commandsToRet.add(eCommand);
			}
			
			List<AlertDao> alertsDao = data.getDeviceAlerts();
			List<DeviceAlertSummary> alertsSummary = system.getDeviceAlerts();
			for (AlertDao alertDao : alertsDao) {
				DeviceAlertSummary alert = new DeviceAlertSummary();
				alert.setDeviceName(alertDao.getName());
				alert.setDeviceType(EDeviceType.valueOf(alertDao.getType()));
				if (alertDao.getLastNotification() != null) {
					alert.setLastNotification(BigInteger.valueOf(alertDao.getLastNotification().getTime()));
				}
				alertsSummary.add(alert);
			}
			
			LOGGER.debug( "End call doGet method for SystemEndpoint at: " + new Date() );
			
			return factory.createSystem(system).getValue();
			
			} catch (RuntimeException ex){
				LOGGER.debug("RuntimeException in getSystem() SystemEndpointImpl with message :" + ex.getMessage(), ex);
				throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
			}

	}
	
	@Override
	public Response login(String auth, String origin) {
		try {
			// check & process login
			UserDao userToken = systemManager.processLogin(auth, true, null, 0); 
			Token token = factory.createToken();
			token.setAccessToken(userToken.getAccessToken());
			token.setExpiredDate(userToken.getExpiredTokenDate().getTime());

			// init teachin_sensors table 
			// if teachin was launched by the logged user !!!
			systemManager.deleteAllTeachinSensorsByUserId(userToken.getId());
			
			if (origin != null) {
				LOGGER.debug("Origin value is :" + origin);
				return Response.ok(token).status(200)
			            .header("Access-Control-Allow-Origin", "*")
			            .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
			            .header("Access-Control-Allow-Credentials", "true")
			            .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
			            .header("Access-Control-Max-Age", "1209600")
			            .build();
			} else {
	        	LOGGER.debug("Origin value is null");
	        	return Response.status(200).entity(token).build();
	        }
		} catch (DataNotExistsException e) {
				LOGGER.debug("DataNotExistsException in login() SystemEndpointImpl with message :" + e.getMessage(), e);
				throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_34, Response.Status.METHOD_NOT_ALLOWED));
		} catch (AuthenticationException e) {
				LOGGER.debug("AuthenticationException in login() SystemEndpointImpl with message :" + e.getMessage(), e);
				throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_33, Response.Status.UNAUTHORIZED));
		} catch (RuntimeException ex) {
				LOGGER.debug("RuntimeException in login() SystemEndpointImpl with message :" + ex.getMessage(), ex);
				throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}
	}
	
	@Override
	public Response logout(String token) {
		UserDao user = systemManager.processLogout(token);
		
		// update teachin status to ENDED if launched by the user 
		systemManager.updateTeachinStatusByUser(user.getId());
		
		return Response.status(200).build();
		
	}
	
	@Override
	public Teachin getTeachin() {
		try {
			TeachinSensorDto teachinDto = systemManager.getTeachin();
			
			Teachin teachin = factory.createTeachin();
			
			teachin.setRoomId(String.valueOf(teachinDto.getRoomId()));
			teachin.setGatewayId(teachinDto.getGatewayMacAddress());
			
			if (teachinDto.getTeachinStatus().equals(E_TeachinStatus.INITIALIZING.toString())) {
				teachin.setStatus(ETeachinStatus.INITIALIZING);
			} else if (teachinDto.getTeachinStatus().equals(E_TeachinStatus.RUNNING.toString())) {
				teachin.setStatus(ETeachinStatus.RUNNING);
			} else if (teachinDto.getTeachinStatus().equals(E_TeachinStatus.ENDED.toString())) {
				teachin.setStatus(ETeachinStatus.ENDED);
			}  
				
			List<SensorDto> sensorsDto = teachinDto.getSensors();
			for (SensorDto sensorDto : sensorsDto) {
				TeachinSensor sensor = factory.createTeachinSensor();
				sensor.setIdentifier(sensorDto.getSensorIdentifier());
				if (sensorDto.getSensorStatus().equals(E_SensorTeachinStatus.NOT_PAIRED.toString())) {
					sensor.setStatus(ETeachinSensorStatus.NOT_PAIRED);
				} else if (sensorDto.getSensorStatus().equals(E_SensorTeachinStatus.PAIRED_KO.toString())) {
					sensor.setStatus(ETeachinSensorStatus.PAIRED_KO);
				} else if (sensorDto.getSensorStatus().equals(E_SensorTeachinStatus.PAIRED_OK.toString())) {
					sensor.setStatus(ETeachinSensorStatus.PAIRED_OK);
				}
				teachin.getSensors().add(sensor);
			}
			
			return factory.createTeachin(teachin).getValue();
			
		} catch(DataNotExistsException ex) {
			LOGGER.debug("DataNotExistsException in getTeachin() SystemEndpointImpl with message :" + ex.getMessage(), ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_37, Response.Status.NOT_FOUND));
		} catch (RuntimeException ex) {
			LOGGER.debug("RuntimeException in getTeachin() SystemEndpointImpl with message :" + ex.getMessage(), ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}
	}
	
	@Override
	public Response initTeachin(String auth, String roomId) {
		try {
			systemManager.initTeachin(auth, Long.parseLong(roomId));
			return Response.status(200).entity(new Date().getTime()).build();
		} catch(DataAlreadyExistsException e) {
			LOGGER.debug("DataAlreadyExistsException in initTeachin() SystemEndpointImpl with message :" + e.getMessage(), e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_36, Response.Status.METHOD_NOT_ALLOWED));
		} catch(DataNotExistsException ex) {
			LOGGER.debug("DataNotExistsException in initTeachin() SystemEndpointImpl with message :" + ex.getMessage(), ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_35, Response.Status.NOT_FOUND));
		} catch (RuntimeException ex) {
			LOGGER.debug("RuntimeException in initTeachin() SystemEndpointImpl with message :" + ex.getMessage(), ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}
		
	}
	
	@Override
	public Response cancelTeachin() {
		try {
				systemManager.updateTeachinStatus();
				return Response.status(200).build();
			} catch (DataNotExistsException e) {
				LOGGER.debug("DataNotExistsException in cancelTeachin() SystemEndpointImpl with message :" + e.getMessage(), e);
				throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_37, Response.Status.NOT_FOUND));
		}  catch (RuntimeException ex) {
			LOGGER.debug("RuntimeException in cancelTeachin() SystemEndpointImpl with message :" + ex.getMessage(), ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}
	}
	
	@Override
	public Response submitTeachin(List<String> sensorIdentifiers) {
		try {
			// setTeachinStatus to ENDED
			systemManager.updateTeachinStatus();
			
			systemManager.submitTeachin(sensorIdentifiers);
			return Response.status(200).build();
			
		} catch (DataNotExistsException e) {
			LOGGER.debug("DataNotExistsException in submitTeachin() SystemEndpointImpl with message :" + e.getMessage(), e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_37, Response.Status.NOT_FOUND));
		}  catch (RuntimeException ex) {
			LOGGER.debug("RuntimeException in submitTeachin() SystemEndpointImpl with message :" + ex.getMessage(), ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}
	}
	
	@Override
	public boolean executeInitTestFile() {
		return testManager.executeInitTestFile();
	}
	
	@Override
	public Response options() {
	    return Response.ok("")
	            .header("Access-Control-Allow-Origin", "*")
	            .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization, x-auth-token")
	            .header("Access-Control-Allow-Credentials", "true")
	            .header("Access-Control-Allow-Methods", "OPTIONS, HEAD")
	            .header("Access-Control-Max-Age", "1209600")
	            .build();
	}

	@Override
	public boolean initTeachinSensorsTable() {
			return testManager.initTeachinSensorsTable();
	}
	
	@Override
	public boolean setTeachinSensorsTable() {
		return testManager.setTeachinSensorsTable();
	}
	
	
	private List<EDeviceStatus> constructDeviceStatuses() {
		List<EDeviceStatus> deviceStatuses = new ArrayList<EDeviceStatus>();
		deviceStatuses.add(EDeviceStatus.OFFLINE);
		deviceStatuses.add(EDeviceStatus.ONLINE);
		deviceStatuses.add(EDeviceStatus.UNSTABLE);
		
		return deviceStatuses;
	}
	
	private List<ESensorType> constructSensortypes() {
		List<ESensorType> sensorTypes = new ArrayList<ESensorType>();
		sensorTypes.add(ESensorType.MOTION_DETECTION);
		sensorTypes.add(ESensorType.TEMPERATURE_HUMIDITY);
		
		return sensorTypes;
	}
	
	private List<ERoomType> constructRoomtypes() {
		List<ERoomType> roomTypes = new ArrayList<ERoomType>();
		roomTypes.add(ERoomType.BOX);
		roomTypes.add(ERoomType.VIDEO_CONF);
		
		return roomTypes;
	}
	
	private List<EMeetingroomType> constructMeetingRoomtypes() {
		List<EMeetingroomType> meetingRoomTypes = new ArrayList<EMeetingroomType>();
		meetingRoomTypes.add(EMeetingroomType.BOX);
		meetingRoomTypes.add(EMeetingroomType.VIDEO_CONF);
		
		return meetingRoomTypes;
	}
	
	private List<ECommandModel> constructCommands() {
		List<ECommandModel> commands = new ArrayList<ECommandModel>();
		commands.add(ECommandModel.ONLINE);
		commands.add(ECommandModel.ECONOMIC);
		commands.add(ECommandModel.STANDBY);
		commands.add(ECommandModel.RESET);
		commands.add(ECommandModel.OFFLINE);
		commands.add(ECommandModel.NONE);
		
		return commands;
	}
}
