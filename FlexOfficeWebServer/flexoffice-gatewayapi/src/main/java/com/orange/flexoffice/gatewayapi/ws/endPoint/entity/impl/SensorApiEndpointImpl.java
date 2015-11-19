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
import com.orange.flexoffice.business.common.enums.EnumErrorModel;
import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.service.data.SensorManager;
import com.orange.flexoffice.dao.common.model.data.SensorDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_SensorStatus;
import com.orange.flexoffice.dao.common.model.enumeration.E_SensorType;


public class SensorApiEndpointImpl implements SensorApiEndpoint {

	private static final Logger LOGGER = Logger.getLogger(SensorApiEndpointImpl.class);

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

		SensorDao sensorDao = new SensorDao();
		sensorDao.setIdentifier(sensorInput.getId());
		sensorDao.setProfile(sensorInput.getProfile());
		// TODO set the   sensorInput.getGatewayId() ?
		sensorDao.setName("");
		sensorDao.setDescription("");
		sensorDao.setStatus(E_SensorStatus.OFFLINE.toString());
		// Type depends on profile.
		sensorDao.setType(computeType(sensorInput.getProfile()));
		// No room by default
		sensorDao.setRoomId(0);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "addSensor with parameters :");
			final StringBuffer message = new StringBuffer( 1000 );
			message.append( "Identifier :" );
			message.append( sensorInput.getId() );
			message.append( "\n" );
			message.append( "Profile :" );
			message.append( sensorInput.getProfile() );
			message.append( "\n" );
			LOGGER.debug( message.toString() );
		}

		try {
			sensorDao = sensorManager.save(sensorDao);

		} catch (DataAlreadyExistsException e) {

			LOGGER.debug("DataNotExistsException in SensorApiEndpoint.addSensor with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_11, Response.Status.METHOD_NOT_ALLOWED));

		} catch (RuntimeException ex) {

			LOGGER.debug("RuntimeException in SensorApiEndpoint.addSensor with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}

		LOGGER.info( "End call SensorApiEndpoint.addSensor at: " + new Date() );

		return Response.status(Status.ACCEPTED).build();
	}

	@Override
	public Response updateSensor(String id, SensorInput sensor) {
		LOGGER.info( "Begin call SensorEndpoint.updateSensor at: " + new Date() );

		SensorDao sensorDao = new SensorDao();
		sensorDao.setIdentifier(id);
		sensorDao.setStatus(sensor.getSensorStatus().toString());


		try {
			sensorDao = sensorManager.updateStatus(sensorDao);

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



	private String computeType(final String currentProfile) {
		String type = new String(E_SensorType.TEMPERATURE_HUMIDITY.toString());

		// Type depends on profile.
		// TODO - For V2, add properties or other to avoid change code when a new type will be manage 
		if (currentProfile.contains("A5-07")) {
			type = E_SensorType.MOTION_DETECTION.toString();
		}

		return type;
	}


}