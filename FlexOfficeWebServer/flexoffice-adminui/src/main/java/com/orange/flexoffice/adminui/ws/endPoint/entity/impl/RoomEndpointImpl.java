package com.orange.flexoffice.adminui.ws.endPoint.entity.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

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
import com.orange.flexoffice.adminui.ws.utils.ErrorMessageHandler;
import com.orange.flexoffice.business.common.enums.EnumErrorModel;
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

		return roomList;
	}

	/* (non-Javadoc)
	 * @see com.orange.flexoffice.adminui.ws.endPoint.entity.RoomEndpoint#getRoom(java.lang.String)
	 */
	@Override
	public Room getRoom(String roomId) {

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
				sensorOutput.setId(sensorDao.getColumnId());
				sensorOutput.setName(sensorDao.getName());
				room.getSensors().add(sensorOutput);
			}

			room.setAddress(roomDto.getAddress());
			room.setCapacity(BigInteger.valueOf(roomDto.getCapacity()));			
			room.setStatus(ERoomStatus.valueOf(roomDto.getStatus().toString()));
			room.setTenantName(computeTenant(room.getStatus(), roomDto.getUser(), roomDto.getName()));

			return room;

		} catch (DataNotExistsException e){

			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_31, Response.Status.NOT_FOUND));

		} catch (RuntimeException ex){

			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));

		}


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
			gateway.setId(gatewayDto.getId());
			gateway.setName(gatewayDto.getId());

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

		if (userId != null) {

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
