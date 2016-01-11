package com.orange.flexoffice.gatewayapi.ws.endPoint.entity.impl;

import java.util.Date;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.gatewayapi.ws.endPoint.entity.SensorApiEndpoint;
import com.orange.flexoffice.gatewayapi.ws.model.SensorInput;
import com.orange.flexoffice.gatewayapi.ws.model.SensorNewSummary;
import com.orange.flexoffice.gatewayapi.ws.utils.ErrorMessageHandler;
import com.orange.flexoffice.business.common.enums.EnumAcceptedProfile;
import com.orange.flexoffice.business.common.enums.EnumErrorModel;
import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.WrongProfileException;
import com.orange.flexoffice.business.common.service.data.SensorManager;
import com.orange.flexoffice.dao.common.model.data.RoomDao;
import com.orange.flexoffice.dao.common.model.data.SensorDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_SensorStatus;


public class SensorApiEndpointImpl implements SensorApiEndpoint {

	private static final Logger LOGGER = Logger.getLogger(SensorApiEndpointImpl.class);
	private static final String defaultProfile = "UNKNOWN";
	
	@Autowired
	private SensorManager sensorManager;

	@Autowired
	private ErrorMessageHandler errorMessageHandler;



	/* (non-Javadoc)
	 * @see com.orange.flexoffice.adminui.ws.endPoint.entity.SensorEndpoint#addSensor
	 */
	@Override
	public Response addSensor(SensorNewSummary sensorInput) {

		LOGGER.info( "Begin call SensorApiEndpoint.addSensor at: " + new Date() );
		try {
			
		SensorDao sensorDao = new SensorDao();
		sensorDao.setIdentifier(sensorInput.getId());
		sensorDao.setName("["+sensorInput.getId()+"]");
		sensorDao.setProfile(sensorInput.getProfile());
		sensorDao.setStatus(E_SensorStatus.ONLINE.toString());
		// Type depends on profile.
		sensorDao.setType(computeType(sensorInput.getProfile()));
		// No room by default
		sensorDao.setRoomId(0);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "addSensor with parameters :");
			final StringBuilder message = new StringBuilder( 1000 );
			message.append( "Identifier :" );
			message.append( sensorInput.getId() );
			message.append( "\n" );
			message.append( "Profile :" );
			message.append( sensorInput.getProfile() );
			message.append( "\n" );
			LOGGER.debug( message.toString() );
		}
		
			sensorManager.save(sensorDao, sensorInput.getGatewayId());

		} catch (DataAlreadyExistsException e) {
			LOGGER.debug("DataAlreadyExistsException in SensorApiEndpoint.addSensor with message :", e);
			// process Sensor Teachin if Teachin exist & actif
			sensorManager.processTeachinSensor(sensorInput.getId(), sensorInput.getGatewayId());
		} catch (WrongProfileException ex1) {
			LOGGER.debug("WrongProfileException in SensorApiEndpoint.addSensor with message :", ex1);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_38, Response.Status.METHOD_NOT_ALLOWED));
		} catch (RuntimeException ex) {
			LOGGER.debug("RuntimeException in SensorApiEndpoint.addSensor with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}

		LOGGER.info( "End call SensorApiEndpoint.addSensor at: " + new Date() );

		return Response.status(Status.ACCEPTED).build();
	}

	@Override
	public Response updateSensor(String identifier, SensorInput sensor)  {
		
		LOGGER.info( "Begin call SensorEndpoint.updateSensor at: " + new Date() );
		
		try {
			
			SensorDao sensorDao = sensorManager.find(identifier);
			sensorDao.setStatus(sensor.getSensorStatus().toString());
			if (sensor.getOccupancyInfo() != null) {
				sensorDao.setOccupancyInfo(sensor.getOccupancyInfo().toString());
			}
			
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("updateSensor with room parameters :");
				final StringBuilder message = new StringBuilder( 1000 );
				message.append( "room Id :" );
				message.append( sensorDao.getRoomId() );
				message.append( "\n" );
				message.append( "sensor room Temperature Info :" );
				message.append( sensor.getTemperature() );
				message.append( "\n" );
				message.append( "sensor room Humidity Info :" );
				message.append( sensor.getHumidity() );
				message.append( "\n" );
				message.append( "sensor Occupancy Info :" );
				message.append( sensor.getOccupancyInfo() );
				message.append( "\n" );
				LOGGER.debug( message.toString() );
			}
			
			RoomDao roomDao = null;
			if ((sensorDao.getRoomId() != null) && (sensorDao.getRoomId() != 0)) {
				roomDao = new RoomDao();
				roomDao.setId(Long.valueOf(sensorDao.getRoomId()));
				roomDao.setTemperature(sensor.getTemperature());
				roomDao.setHumidity(sensor.getHumidity());
				LOGGER.debug("RoomDao is instanciated");
			}
	
			sensorManager.updateStatus(sensorDao, roomDao);

		} catch (DataNotExistsException e){

			LOGGER.debug("DataNotExistsException in SensorEndpoint.updateSensor with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_12, Response.Status.NOT_FOUND));

		} catch (RuntimeException ex){

			LOGGER.debug("RuntimeException in SensorEndpoint.updateSensor with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}

		LOGGER.info( "End call SensorEndpoint.updateSensor at: " + new Date() );

		return Response.status(Status.ACCEPTED).build();
	}


	/**
	 * computeType
	 * @param currentProfile
	 * @return
	 * @throws WrongProfileException
	 */
	private String computeType(final String currentProfile) throws WrongProfileException {
		String type = defaultProfile;
		EnumAcceptedProfile[] values = EnumAcceptedProfile.values();
		for (EnumAcceptedProfile profile : values) {
			if (profile.code().equalsIgnoreCase(currentProfile)) {
				type = profile.value();
				break;
			}
		}
		 
		if (type.equals(defaultProfile)) {
			throw new WrongProfileException("wrong profile is detected !!!");
		} 

		return type;
	}


}
