package com.orange.flexoffice.adminui.ws.endPoint.data.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.adminui.ws.endPoint.data.SystemEndpoint;
import com.orange.flexoffice.adminui.ws.model.DeviceAlertSummary;
import com.orange.flexoffice.adminui.ws.model.EDeviceStatus;
import com.orange.flexoffice.adminui.ws.model.EDeviceType;
import com.orange.flexoffice.adminui.ws.model.ERoomType;
import com.orange.flexoffice.adminui.ws.model.ESensorType;
import com.orange.flexoffice.adminui.ws.model.ObjectFactory;
import com.orange.flexoffice.adminui.ws.model.Teachin;
import com.orange.flexoffice.adminui.ws.model.Token;
import com.orange.flexoffice.adminui.ws.utils.ErrorMessageHandler;
import com.orange.flexoffice.business.common.enums.EnumErrorModel;
import com.orange.flexoffice.business.common.service.data.SystemManager;
import com.orange.flexoffice.business.common.service.data.TestManager;
import com.orange.flexoffice.dao.common.model.data.AlertDao;
import com.orange.flexoffice.dao.common.model.object.SystemDto;


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
			
			LOGGER.info( "Begin call doGet method for SystemEndpoint at: " + new Date() );
			
			SystemDto data = systemManager.getSystem();
			
			if (data == null) {
				LOGGER.debug("data is null");
			}
			com.orange.flexoffice.adminui.ws.model.System system = factory.createSystem();
		
			system.setActiveUserCount(BigInteger.valueOf(data.getActiveUserCount()));
			system.setUserCount(BigInteger.valueOf(data.getUserCount()));
			system.setGatewayCount(BigInteger.valueOf(data.getGatewayCount()));
			system.setRoomCount(BigInteger.valueOf(data.getRoomCount()));
			
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
			
			LOGGER.info( "End call doGet method for SystemEndpoint at: " + new Date() );
			
			return factory.createSystem(system).getValue();
			
			} catch (RuntimeException ex){
				LOGGER.debug("RuntimeException in getSystem() SystemEndpointImpl with message :" + ex.getMessage());
				throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
			}

	}
	
	
	@Override
	public Response login(String auth, String origin) {
		// TODO implement
		
		Token token = factory.createToken();
		token.setAccessToken("testTokenAccess");
		token.setExpiredDate(Long.valueOf(1447853568741l));

		if (origin != null) {
//			Response.addHeader("Access-Control-Allow-Origin", origin);
//            response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
//            response.addHeader("Access-Control-Allow-Credentials", "true");
//            response.addHeader("Access-Control-Allow-Headers",
//                    request.getHeader("Access-Control-Request-Headers"));
			
			return Response.ok(token).header("Access-Control-Allow-Origin", origin).build();
        } else {
        	return Response.status(200).entity(token).build();
        }
		
		//return Response.status(200).entity(token).build();
		//return factory.createToken(token).getValue();
	}
	
	@Override
	public Response logout() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Teachin getTeachin() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void initTeachin() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Response cancelTeachin() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Response submitTeachin() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean executeInitTestFile() {
		return testManager.executeInitTestFile();
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
}
