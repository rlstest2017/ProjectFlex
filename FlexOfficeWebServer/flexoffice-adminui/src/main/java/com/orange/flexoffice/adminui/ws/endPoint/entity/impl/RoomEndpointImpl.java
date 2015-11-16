package com.orange.flexoffice.adminui.ws.endPoint.entity.impl;

import static com.orange.flexoffice.adminui.ws.ParamsConst.ROOM_ID_PARAM;

import java.math.BigInteger;
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

import com.orange.flexoffice.adminui.ws.endPoint.entity.RoomEndpoint;
import com.orange.flexoffice.adminui.ws.model.ERoomStatus;
import com.orange.flexoffice.adminui.ws.model.ERoomType;
import com.orange.flexoffice.adminui.ws.model.GatewayOutput;
import com.orange.flexoffice.adminui.ws.model.RoomSummary;
import com.orange.flexoffice.adminui.ws.model.SensorOutput;
import com.orange.flexoffice.adminui.ws.model.ObjectFactory;
import com.orange.flexoffice.adminui.ws.model.Room;
import com.orange.flexoffice.adminui.ws.model.RoomInput1;
import com.orange.flexoffice.adminui.ws.model.RoomOutput;
import com.orange.flexoffice.adminui.ws.utils.ErrorMessageHandler;
import com.orange.flexoffice.business.common.enums.EnumErrorModel;
import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.service.data.GatewayManager;
import com.orange.flexoffice.business.common.service.data.RoomManager;
import com.orange.flexoffice.business.common.service.data.UserManager;
import com.orange.flexoffice.dao.common.model.data.RoomDao;
import com.orange.flexoffice.dao.common.model.data.SensorDao;
import com.orange.flexoffice.dao.common.model.data.UserDao;
import com.orange.flexoffice.dao.common.model.object.GatewayDto;
import com.orange.flexoffice.dao.common.model.object.RoomDto;


public class RoomEndpointImpl implements RoomEndpoint {

	private final Logger LOGGER = Logger.getLogger(RoomEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();

	@Context
	private UriInfo uriInfo;

	@Autowired
	private RoomManager roomManager;

	@Autowired
	private GatewayManager gatewayManager;

	@Autowired
	private UserManager userManager;

	@Autowired
	private ErrorMessageHandler errorMessageHandler;

	@Override
	public List<RoomSummary> getRooms() {

		LOGGER.info( "Begin call getRooms method for RoomEndpoint at: " + new Date() );

		List<RoomDao> dataList = roomManager.findAllRooms();

		if (dataList == null) {

			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_27, Response.Status.NOT_FOUND));

		}

		LOGGER.debug("List of rooms : " + dataList.size());

		List<RoomSummary> roomList = new ArrayList<RoomSummary>();

		for (RoomDao roomDao : dataList) {
			RoomSummary room = factory.createRoomSummary();
			room.setId(roomDao.getColumnId());
			room.setName(roomDao.getName());
			room.setType(ERoomType.valueOf(roomDao.getType().toString()));
			room.setGateway(getGatewayFromId(Long.valueOf(roomDao.getGatewayId()), roomDao.getName()));
			room.setAddress(roomDao.getAddress());
			room.setCapacity(BigInteger.valueOf(roomDao.getCapacity()));
			room.setStatus(ERoomStatus.valueOf(roomDao.getStatus().toString()));
			room.setTenantName(computeTenant(room.getStatus(), roomDao.getUserId(), roomDao.getName()));

			roomList.add(room);
		}

		LOGGER.info( "End call getRooms method for RoomEndpoint at: " + new Date() );

		return roomList;
	}

	/* (non-Javadoc)
	 * @see com.orange.flexoffice.adminui.ws.endPoint.entity.RoomEndpoint#getRoom(java.lang.String)
	 */
	@Override
	public Room getRoom(String roomId) {

		LOGGER.info( "Begin call getRoom method for RoomEndpoint at: " + new Date() );

		try {
			RoomDto roomDto = roomManager.find(Long.valueOf(roomId));

			Room room = factory.createRoom();
			room.setId(roomDto.getId());
			room.setName(roomDto.getName());
			room.setType(ERoomType.valueOf(roomDto.getType().toString()));
			room.setGateway(getGatewayFromId(Long.valueOf(roomDto.getGateway().getId()), roomDto.getName()));
			room.setDesc(roomDto.getDescription());

			// Set Sensor list to room
			List<SensorDao> sensors = roomDto.getSensors();

			for (SensorDao sensorDao : sensors) {
				SensorOutput sensorOutput = new SensorOutput();
				sensorOutput.setIdentifier(sensorDao.getIdentifier());
				sensorOutput.setName(sensorDao.getName());
				room.getSensors().add(sensorOutput);
			}

			room.setAddress(roomDto.getAddress());
			room.setCapacity(BigInteger.valueOf(roomDto.getCapacity()));			
			room.setStatus(ERoomStatus.valueOf(roomDto.getStatus().toString()));
			room.setTenantName(computeTenant(room.getStatus(), roomDto.getUser(), roomDto.getName()));

			LOGGER.info( "End call getRoom method for RoomEndpoint at: " + new Date() );

			return room;

		} catch (DataNotExistsException e){

			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_31, Response.Status.NOT_FOUND));

		} catch (RuntimeException ex){

			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));

		}


	}


	/* (non-Javadoc)
	 * @see com.orange.flexoffice.adminui.ws.endPoint.entity.RoomEndpoint#addRoom
	 */
	@Override
	public RoomOutput addRoom(RoomInput1 roomInput) {

		LOGGER.info( "Begin call addRoom method for RoomEndpoint at: " + new Date() );

		RoomDao roomDao = new RoomDao();
		roomDao.setName(roomInput.getName());
		roomDao.setAddress(roomInput.getAddress());
		roomDao.setCapacity(roomInput.getCapacity().intValue());
		roomDao.setDescription(roomInput.getDesc());
		roomDao.setType(roomInput.getType().toString());
		roomDao.setStatus("UNKNOWN");
		roomDao.setGatewayId(Long.valueOf(roomInput.getGateway().getId()));
	
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "Begin call addRoom(UserInput userInput) method of RoomEndpoint, with parameters :");
			final StringBuffer message = new StringBuffer( 1000 );
			message.append( "name :" );
			message.append( roomInput.getName() );
			message.append( "\n" );
			message.append( "gateway id :" );
			message.append( roomInput.getGateway().getId() );
			message.append( "\n" );
			LOGGER.debug( message.toString() );
		}

		try {
			roomDao = roomManager.save(roomDao);

		} catch (DataAlreadyExistsException e) {
			
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_28, Response.Status.METHOD_NOT_ALLOWED));

		} catch (RuntimeException ex) {

			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}


		RoomOutput returnedRoom = factory.createRoomOutput();
		returnedRoom.setId(roomDao.getColumnId());
		returnedRoom.setName(roomDao.getName());

		LOGGER.info( "End call addRoom method for RoomEndpoint at: " + new Date() );

		return factory.createRoomOutput(returnedRoom).getValue();
	}

	
	/* (non-Javadoc)
	 * @see com.orange.flexoffice.adminui.ws.endPoint.entity.RoomEndpoint#updateRoom
	 */
	@Override
	public Response updateRoom(@PathParam(ROOM_ID_PARAM)String id, RoomInput1 roomInput) {
		
		LOGGER.info( "Begin call updateRoom method for RoomEndpoint at: " + new Date() );

		RoomDao roomDao = new RoomDao();
		roomDao.setId(Long.valueOf(id));
		roomDao.setName(roomInput.getName());
		roomDao.setAddress(roomInput.getAddress());
		roomDao.setCapacity(roomInput.getCapacity().intValue());
		roomDao.setDescription(roomInput.getDesc());
		roomDao.setType(roomInput.getType().toString());
		roomDao.setGatewayId(Long.valueOf(roomInput.getGateway().getId()));

		try {
			roomDao = roomManager.update(roomDao);

		} catch (DataNotExistsException e){
			
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_29, Response.Status.NOT_FOUND));
			
		} catch (RuntimeException ex){

			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}

		LOGGER.info( "End call updateRoom method for RoomEndpoint at: " + new Date() );

		return Response.status(Status.ACCEPTED).build();
	}


	/* (non-Javadoc)
	 * @see com.orange.flexoffice.adminui.ws.endPoint.entity.RoomEndpoint#removeRoom
	 */
	@Override
	public Response removeRoom(@PathParam(ROOM_ID_PARAM)String id) {
		
		LOGGER.info( "Begin call removeRoom method for RoomEndpoint at: " + new Date() );

		try {

			roomManager.delete(Long.valueOf(id));

		} catch (DataNotExistsException e){
			
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_30, Response.Status.NOT_FOUND));
			
		} catch (RuntimeException ex){

			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}

		LOGGER.info( "End call removeRoom method for RoomEndpoint at: " + new Date() );

		return Response.noContent().build();
	}



	
	/* (non-Javadoc)
	 * @see com.orange.flexoffice.adminui.ws.endPoint.entity.RoomEndpoint#findByName
	 */
	@Override
	public RoomDao findByName(String name) {
		return roomManager.findByName(name);
	}

	/** Create Gateway output from gateway id
	 * 
	 * @param gatewayId
	 * @param roomName
	 * 
	 * @return GatewayOutput
	 */
	private GatewayOutput getGatewayFromId(final Long gatewayId, final String roomName) {

		GatewayOutput gateway = factory.createGatewayOutput();

		try {
			final GatewayDto gatewayDto = gatewayManager.find(Long.valueOf(gatewayId));
			gateway.setMacAddress(gatewayDto.getMacAddress());
			gateway.setName(gatewayDto.getName());

		} catch (DataNotExistsException e) {
			LOGGER.warn("Get rooms / Get room id : Wrong Gateway on room " + roomName, e);
		}
		return (gateway);		
	}


	/** Create tenant if room status is not free 
	 * 
	 * @param status
	 * @param userId
	 * @param roomName
	 * 
	 * @return tenant
	 */
	private String computeTenant(final ERoomStatus status, final Long userId, final String roomName) {

		UserDao userDao = null;

		if ((userId != null) && (userId != 0)) {

			userDao = userManager.find(Long.valueOf(userId));			
		}

		return computeTenant(status, userDao, roomName);
	}



	/** Create tenant if room status is not free 
	 * 
	 * @param status
	 * @param userId
	 * @param roomName
	 * 
	 * @return tenant
	 */
	private String computeTenant(final ERoomStatus status, final UserDao userDao, final String roomName) {
		String tenant = new String("");

		if ((status == ERoomStatus.RESERVED) ||
				(status == ERoomStatus.OCCUPIED)) {


			if (userDao == null) {

				LOGGER.warn("Get rooms / Get room id   : Wrong user on room " + roomName);

			} else {

				tenant = userDao.getFirstName() + " " + userDao.getLastName();
			}
		}

		return tenant;
	}


}
