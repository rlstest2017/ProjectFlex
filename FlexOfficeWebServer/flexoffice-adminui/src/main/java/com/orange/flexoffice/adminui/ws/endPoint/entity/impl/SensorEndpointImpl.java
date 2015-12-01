package com.orange.flexoffice.adminui.ws.endPoint.entity.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.adminui.ws.endPoint.entity.SensorEndpoint;
import com.orange.flexoffice.adminui.ws.model.RoomOutput;
import com.orange.flexoffice.adminui.ws.model.Sensor;
import com.orange.flexoffice.adminui.ws.model.ESensorType;
import com.orange.flexoffice.adminui.ws.model.EDeviceStatus;
import com.orange.flexoffice.adminui.ws.model.SensorInput2;
import com.orange.flexoffice.adminui.ws.model.SensorSummary;
import com.orange.flexoffice.adminui.ws.model.SensorOutput;
import com.orange.flexoffice.adminui.ws.model.ObjectFactory;
import com.orange.flexoffice.adminui.ws.model.SensorInput1;
import com.orange.flexoffice.adminui.ws.utils.ErrorMessageHandler;
import com.orange.flexoffice.business.common.enums.EnumErrorModel;
import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.service.data.RoomManager;
import com.orange.flexoffice.business.common.service.data.SensorManager;
import com.orange.flexoffice.dao.common.model.data.SensorDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_SensorStatus;
import com.orange.flexoffice.dao.common.model.object.RoomDto;


public class SensorEndpointImpl implements SensorEndpoint {

	private static final Logger LOGGER = Logger.getLogger(SensorEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();

	@Autowired
	private SensorManager sensorManager;

	@Autowired
	private RoomManager roomManager;

	@Autowired
	private ErrorMessageHandler errorMessageHandler;

	@Override
	public List<SensorSummary> getSensors() {

		LOGGER.info( "Begin call SensorEndpoint.getSensors at: " + new Date() );

		List<SensorDao> dataList = sensorManager.findAllSensors();

		if (dataList == null) {

			LOGGER.error("SensorEndpoint.getSensors : Sensors not found");
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_10, Response.Status.NOT_FOUND));

		}

		List<SensorSummary> sensorList = new ArrayList<SensorSummary>();

		for (SensorDao sensorDao : dataList) {
			SensorSummary sensor = factory.createSensorSummary();
			sensor.setIdentifier(sensorDao.getIdentifier());
			sensor.setName(sensorDao.getName());
			sensor.setType(ESensorType.valueOf(sensorDao.getType().toString()));		
			sensor.setRoom(getRoomFromId(sensorDao.getRoomId(), sensorDao.getIdentifier()));
			if ((sensorDao.getStatus().equals(E_SensorStatus.UNSTABLE_RSSI.toString())) || (sensorDao.getStatus().equals(E_SensorStatus.UNSTABLE_VOLTAGE.toString()))) {
				sensor.setStatus(EDeviceStatus.UNSTABLE);
			} else {
				sensor.setStatus(EDeviceStatus.valueOf(sensorDao.getStatus().toString()));
			}

			sensorList.add(sensor);
		}

		LOGGER.debug("List of sensors : nb = " + sensorList.size());

		LOGGER.info( "End call SensorEndpoint.getSensors at: " + new Date() );

		return sensorList;
	}

	@Override
	public List<SensorSummary> getUnpairedSensors() {

		LOGGER.info( "Begin call SensorEndpoint.getUnpairedSensors at: " + new Date() );

		List<SensorDao> dataList = sensorManager.findAllSensors();

		if (dataList == null) {

			LOGGER.error("SensorEndpoint.getUnpairedSensors : Sensors not found");
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_10, Response.Status.NOT_FOUND));

		}

		List<SensorSummary> sensorList = new ArrayList<SensorSummary>();

		for (SensorDao sensorDao : dataList) {
			if ((sensorDao.getRoomId() != null) && (sensorDao.getRoomId() == 0)){
				SensorSummary sensor = factory.createSensorSummary();
				sensor.setIdentifier(sensorDao.getIdentifier());
				sensor.setName(sensorDao.getName());
				sensor.setType(ESensorType.valueOf(sensorDao.getType().toString()));		
				sensor.setRoom(getRoomFromId(sensorDao.getRoomId(), sensorDao.getIdentifier()));
				if ((sensorDao.getStatus().equals(E_SensorStatus.UNSTABLE_RSSI.toString())) || (sensorDao.getStatus().equals(E_SensorStatus.UNSTABLE_VOLTAGE.toString()))) {
					sensor.setStatus(EDeviceStatus.UNSTABLE);
				} else {
					sensor.setStatus(EDeviceStatus.valueOf(sensorDao.getStatus().toString()));
				}

				sensorList.add(sensor);
			}
		}

		LOGGER.debug("List of unpaired sensors : nb = " + sensorList.size());

		LOGGER.info( "End call SensorEndpoint.getUnpairedSensors  at: " + new Date() );

		return sensorList;
	}

	/* (non-Javadoc)
	 * @see com.orange.flexoffice.adminui.ws.endPoint.entity.SensorEndpoint#getSensor(java.lang.String)
	 */
	@Override
	public Sensor getSensor(String sensorIdentifier) {

		LOGGER.info( "Begin call SensorEndpoint.getSensor at: " + new Date() );

		try {
			SensorDao sensorDao = sensorManager.find(sensorIdentifier);

			Sensor sensor = factory.createSensor();
			sensor.setIdentifier(sensorDao.getIdentifier());
			sensor.setName(sensorDao.getName());
			sensor.setType(ESensorType.valueOf(sensorDao.getType().toString()));
			sensor.setRoom(getRoomFromId(sensorDao.getRoomId(), sensorDao.getIdentifier()));
			sensor.setDesc(sensorDao.getDescription());
			if ((sensorDao.getStatus().equals(E_SensorStatus.UNSTABLE_RSSI.toString())) || (sensorDao.getStatus().equals(E_SensorStatus.UNSTABLE_VOLTAGE.toString()))) {
				sensor.setStatus(EDeviceStatus.UNSTABLE);
			} else {
				sensor.setStatus(EDeviceStatus.valueOf(sensorDao.getStatus().toString()));
			}

			LOGGER.info( "End call SensorEndpoint.getSensor at: " + new Date() );

			return sensor;

		} catch (DataNotExistsException e){

			LOGGER.debug("DataNotExistsException in SensorEndpoint.getSensor with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_14, Response.Status.NOT_FOUND));

		} catch (RuntimeException ex){

			LOGGER.debug("RuntimeException in SensorEndpoint.getSensor with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));

		}


	}


	/* (non-Javadoc)
	 * @see com.orange.flexoffice.adminui.ws.endPoint.entity.SensorEndpoint#addSensor
	 */
	@Override
	public SensorOutput addSensor(SensorInput1 sensorInput) {

		LOGGER.info( "Begin call SensorEndpoint.addSensor at: " + new Date() );

		SensorDao sensorDao = new SensorDao();
		sensorDao.setIdentifier(sensorInput.getIdentifier());
		sensorDao.setName(sensorInput.getName());
		sensorDao.setType(sensorInput.getType().toString());
		sensorDao.setDescription(sensorInput.getDesc());
		sensorDao.setStatus(E_SensorStatus.OFFLINE.toString());
		sensorDao.setProfile(computeProfile(sensorInput.getProfile(), sensorInput.getType()));
		
		if (sensorInput.getRoom() != null) {
			sensorDao.setRoomId(Integer.valueOf(sensorInput.getRoom().getId()));
		} else {
			sensorDao.setRoomId(0);
		}
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "addSensor with parameters :");
			final StringBuilder message = new StringBuilder( 1000 );
			message.append( "name :" );
			message.append( sensorInput.getName() );
			message.append( "\n" );
			LOGGER.debug( message.toString() );
		}

		try {
			sensorDao = sensorManager.save(sensorDao);

		} catch (DataAlreadyExistsException e) {
			
			LOGGER.debug("DataNotExistsException in SensorEndpoint.addSensor with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_11, Response.Status.METHOD_NOT_ALLOWED));

		} catch (RuntimeException ex) {

			LOGGER.debug("RuntimeException in SensorEndpoint.addSensor with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}


		SensorOutput returnedSensor = factory.createSensorOutput();
		returnedSensor.setIdentifier(sensorDao.getIdentifier());
		returnedSensor.setName(sensorDao.getName());

		LOGGER.info( "End call SensorEndpoint.addSensor at: " + new Date() );

		return factory.createSensorOutput(returnedSensor).getValue();
	}

	
	/* (non-Javadoc)
	 * @see com.orange.flexoffice.adminui.ws.endPoint.entity.SensorEndpoint#updateSensor
	 */
	@Override
	public Response updateSensor(String id, SensorInput2 sensorInput) {
		
		LOGGER.info( "Begin call SensorEndpoint.updateSensor at: " + new Date() );

		SensorDao sensorDao = new SensorDao();
		sensorDao.setIdentifier(id);
		sensorDao.setName(sensorInput.getName());
		sensorDao.setType(sensorInput.getType().toString());
		sensorDao.setDescription(sensorInput.getDesc());
		sensorDao.setProfile(computeProfile(sensorInput.getProfile(), sensorInput.getType()));

		if (sensorInput.getRoom() !=null) {
			sensorDao.setRoomId(Integer.valueOf(sensorInput.getRoom().getId()));
		}
		
		try {
			sensorDao = sensorManager.update(sensorDao);

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


	/* (non-Javadoc)
	 * @see com.orange.flexoffice.adminui.ws.endPoint.entity.SensorEndpoint#removeSensor
	 */
	@Override
	public Response removeSensor(String id) {
		
		LOGGER.info( "Begin call SensorEndpoint.removeSensor at: " + new Date() );

		try {

			sensorManager.delete(id);

		} catch (DataNotExistsException e){
			
			LOGGER.debug("DataNotExistsException in SensorEndpoint.removeSensor with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_13, Response.Status.NOT_FOUND));
			
		} catch (RuntimeException ex){

			LOGGER.debug("RuntimeException in SensorEndpoint.removeSensor with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}

		LOGGER.info( "End call SensorEndpoint.removeSensor at: " + new Date() );

		return Response.noContent().build();
	}



	/** Create Room output from room id
	 * 
	 * @param roomId
	 * @param sensorName
	 * 
	 * @return RoomOutput
	 */
	private RoomOutput getRoomFromId(final Integer roomId, final String sensorIdentifier) {

		RoomOutput roomOutput = factory.createRoomOutput();
		roomOutput.setId("0");
		if (roomId != 0) {
			try {
				
				final RoomDto roomDto = roomManager.find(roomId);
				
				roomOutput.setId(String.valueOf(roomDto.getId()));
				roomOutput.setName(roomDto.getName());
				
			} catch(DataNotExistsException e ) {
				LOGGER.warn("Get sensors / Get sensor id : Wrong Room on sensor #" + sensorIdentifier, e);
			}
		}
		return (roomOutput);		
	}

	
	private String computeProfile(final String currentProfile, final ESensorType eSensorType) {
		String profile = new String("");
		
		if ((currentProfile == null) || (currentProfile.isEmpty())) {
			// Profile depends on type.
			// TODO - For V2, add properties or other to avoid change code when a new type will be manage 
			if (eSensorType.equals(ESensorType.MOTION_DETECTION)) {
				profile = "A5-07-01";
			} else {
				profile = "A5-04-01";
			}				
		} else {
			profile = currentProfile;
		}
		return profile;
	}
	

}
