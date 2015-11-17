package com.orange.flexoffice.adminui.ws.endPoint.entity.impl;

import static com.orange.flexoffice.adminui.ws.ParamsConst.SENSOR_IDENTIFIER_PARAM;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
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
import com.orange.flexoffice.business.common.service.data.UserManager;
import com.orange.flexoffice.dao.common.model.data.SensorDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_SensorStatus;
import com.orange.flexoffice.dao.common.model.object.RoomDto;


public class SensorEndpointImpl implements SensorEndpoint {

	private final Logger LOGGER = Logger.getLogger(SensorEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();

	@Context
	private UriInfo uriInfo;

	@Autowired
	private SensorManager sensorManager;

	@Autowired
	private RoomManager roomManager;

	@Autowired
	private UserManager userManager;

	@Autowired
	private ErrorMessageHandler errorMessageHandler;

	@Override
	public List<SensorSummary> getSensors() {

		LOGGER.info( "Begin call getSensors method for SensorEndpoint at: " + new Date() );

		List<SensorDao> dataList = sensorManager.findAllSensors();

		if (dataList == null) {

			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_10, Response.Status.NOT_FOUND));

		}

		List<SensorSummary> sensorList = new ArrayList<SensorSummary>();

		for (SensorDao sensorDao : dataList) {
			SensorSummary sensor = factory.createSensorSummary();
			sensor.setIdentifier(sensorDao.getIdentifier());
			sensor.setName(sensorDao.getName());
			sensor.setType(ESensorType.valueOf(sensorDao.getType().toString()));		
			sensor.setRoom(getRoomFromId(sensorDao.getRoomId(), sensorDao.getIdentifier()));
			sensor.setStatus(EDeviceStatus.valueOf(sensorDao.getStatus().toString()));

			sensorList.add(sensor);
		}

		LOGGER.debug("List of sensors : " + sensorList.size());

		LOGGER.info( "End call getSensors method for SensorEndpoint at: " + new Date() );

		return sensorList;
	}

	@Override
	public List<SensorSummary> getUnpairedSensors() {

		LOGGER.info( "Begin call getUnpairedSensors method for SensorEndpoint at: " + new Date() );

		List<SensorDao> dataList = sensorManager.findAllSensors();

		if (dataList == null) {

			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_10, Response.Status.NOT_FOUND));

		}

		List<SensorSummary> sensorList = new ArrayList<SensorSummary>();

		for (SensorDao sensorDao : dataList) {
			if ((sensorDao.getRoomId() != null) && (sensorDao.getRoomId() > 0)){
				SensorSummary sensor = factory.createSensorSummary();
				sensor.setIdentifier(sensorDao.getIdentifier());
				sensor.setName(sensorDao.getName());
				sensor.setType(ESensorType.valueOf(sensorDao.getType().toString()));		
				sensor.setRoom(getRoomFromId(sensorDao.getRoomId(), sensorDao.getIdentifier()));
				sensor.setStatus(EDeviceStatus.valueOf(sensorDao.getStatus().toString()));

				sensorList.add(sensor);
			}
		}

		LOGGER.debug("List of unpaired sensors : " + sensorList.size());

		LOGGER.info( "End call getUnpairedSensors method for SensorEndpoint at: " + new Date() );

		return sensorList;
	}

	/* (non-Javadoc)
	 * @see com.orange.flexoffice.adminui.ws.endPoint.entity.SensorEndpoint#getSensor(java.lang.String)
	 */
	@Override
	public Sensor getSensor(String sensorIdentifier) {

		LOGGER.info( "Begin call getSensor method for SensorEndpoint at: " + new Date() );

		try {
			SensorDao sensorDao = sensorManager.find(sensorIdentifier);

			Sensor sensor = factory.createSensor();
			sensor.setIdentifier(sensorDao.getIdentifier());
			sensor.setName(sensorDao.getName());
			sensor.setType(ESensorType.valueOf(sensorDao.getType().toString()));
			sensor.setRoom(getRoomFromId(sensorDao.getRoomId(), sensorDao.getIdentifier()));
			sensor.setDesc(sensorDao.getDescription());
			sensor.setStatus(EDeviceStatus.valueOf(sensorDao.getStatus().toString()));

			LOGGER.info( "End call getSensor method for SensorEndpoint at: " + new Date() );

			return sensor;

		} catch (DataNotExistsException e){

			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_14, Response.Status.NOT_FOUND));

		} catch (RuntimeException ex){

			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));

		}


	}


	/* (non-Javadoc)
	 * @see com.orange.flexoffice.adminui.ws.endPoint.entity.SensorEndpoint#addSensor
	 */
	@Override
	public SensorOutput addSensor(SensorInput1 sensorInput) {

		LOGGER.info( "Begin call addSensor method for SensorEndpoint at: " + new Date() );

		SensorDao sensorDao = new SensorDao();
		sensorDao.setIdentifier(sensorInput.getIdentifier());
		sensorDao.setName(sensorInput.getName());
		sensorDao.setType(sensorInput.getType().toString());
		sensorDao.setDescription(sensorInput.getDesc());
		sensorDao.setStatus(E_SensorStatus.OFFLINE.toString());
		sensorDao.setRoomId(Integer.valueOf(sensorInput.getRoom().getId()));
	
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "Begin call addSensor(UserInput userInput) method of SensorEndpoint, with parameters :");
			final StringBuffer message = new StringBuffer( 1000 );
			message.append( "name :" );
			message.append( sensorInput.getName() );
			message.append( "\n" );
			message.append( "room id :" );
			message.append( sensorInput.getRoom().getId() );
			message.append( "\n" );
			LOGGER.debug( message.toString() );
		}

		try {
			sensorDao = sensorManager.save(sensorDao);

		} catch (DataAlreadyExistsException e) {
			
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_11, Response.Status.METHOD_NOT_ALLOWED));

		} catch (RuntimeException ex) {

			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}


		SensorOutput returnedSensor = factory.createSensorOutput();
		returnedSensor.setIdentifier(sensorDao.getIdentifier());
		returnedSensor.setName(sensorDao.getName());

		LOGGER.info( "End call addSensor method for SensorEndpoint at: " + new Date() );

		return factory.createSensorOutput(returnedSensor).getValue();
	}

	
	/* (non-Javadoc)
	 * @see com.orange.flexoffice.adminui.ws.endPoint.entity.SensorEndpoint#updateSensor
	 */
	@Override
	public Response updateSensor(@PathParam(SENSOR_IDENTIFIER_PARAM)String id, SensorInput2 sensorInput) {
		
		LOGGER.info( "Begin call updateSensor method for SensorEndpoint at: " + new Date() );

		SensorDao sensorDao = new SensorDao();
		sensorDao.setIdentifier(id);
		sensorDao.setName(sensorInput.getName());
		sensorDao.setType(sensorInput.getType().toString());
		sensorDao.setDescription(sensorInput.getDesc());
		sensorDao.setRoomId(Integer.valueOf(sensorInput.getRoom().getId()));

		try {
			sensorDao = sensorManager.update(sensorDao);

		} catch (DataNotExistsException e){
			
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_12, Response.Status.NOT_FOUND));
			
		} catch (RuntimeException ex){

			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}

		LOGGER.info( "End call updateSensor method for SensorEndpoint at: " + new Date() );

		return Response.status(Status.ACCEPTED).build();
	}


	/* (non-Javadoc)
	 * @see com.orange.flexoffice.adminui.ws.endPoint.entity.SensorEndpoint#removeSensor
	 */
	@Override
	public Response removeSensor(@PathParam(SENSOR_IDENTIFIER_PARAM)String id) {
		
		LOGGER.info( "Begin call removeSensor method for SensorEndpoint at: " + new Date() );

		try {

			sensorManager.delete(Long.valueOf(id));

		} catch (DataNotExistsException e){
			
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_13, Response.Status.NOT_FOUND));
			
		} catch (RuntimeException ex){

			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}

		LOGGER.info( "End call removeSensor method for SensorEndpoint at: " + new Date() );

		return Response.noContent().build();
	}



	
	/* (non-Javadoc)
	 * @see com.orange.flexoffice.adminui.ws.endPoint.entity.SensorEndpoint#findByName
	 */
	@Override
	public SensorDao findByName(String name) throws DataNotExistsException {
		return sensorManager.findByName(name);
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

		try {
			final RoomDto roomDto = roomManager.find(roomId);
			
			roomOutput.setId(roomDto.getId());
			roomOutput.setName(roomDto.getName());
			
		} catch(DataNotExistsException e ) {
			LOGGER.warn("Get sensors / Get sensor id : Wrong Room on sensor #" + sensorIdentifier, e);
		}

		return (roomOutput);		
	}


}
