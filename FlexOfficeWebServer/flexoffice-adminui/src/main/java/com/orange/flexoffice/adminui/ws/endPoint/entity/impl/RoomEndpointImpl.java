package com.orange.flexoffice.adminui.ws.endPoint.entity.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.adminui.ws.endPoint.entity.RoomEndpoint;
import com.orange.flexoffice.adminui.ws.model.BuildingOutput;
import com.orange.flexoffice.adminui.ws.model.ERoomStatus;
import com.orange.flexoffice.adminui.ws.model.ERoomType;
import com.orange.flexoffice.adminui.ws.model.GatewayOutput;
import com.orange.flexoffice.adminui.ws.model.Location;
import com.orange.flexoffice.adminui.ws.model.LocationItem;
import com.orange.flexoffice.adminui.ws.model.RoomSummary;
import com.orange.flexoffice.adminui.ws.model.SensorOutput;
import com.orange.flexoffice.adminui.ws.model.ObjectFactory;
import com.orange.flexoffice.adminui.ws.model.Room;
import com.orange.flexoffice.adminui.ws.model.RoomInput1;
import com.orange.flexoffice.adminui.ws.model.RoomOutput;
import com.orange.flexoffice.adminui.ws.model.UserSummary;
import com.orange.flexoffice.adminui.ws.utils.ErrorMessageHandler;
import com.orange.flexoffice.business.common.enums.EnumErrorModel;
import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.business.common.service.data.BuildingManager;
import com.orange.flexoffice.business.common.service.data.GatewayManager;
import com.orange.flexoffice.business.common.service.data.RoomManager;
import com.orange.flexoffice.business.common.service.data.TestManager;
import com.orange.flexoffice.business.common.service.data.UserManager;
import com.orange.flexoffice.dao.common.model.data.RoomDao;
import com.orange.flexoffice.dao.common.model.data.SensorDao;
import com.orange.flexoffice.dao.common.model.data.UserDao;
import com.orange.flexoffice.dao.common.model.object.BuildingDto;
import com.orange.flexoffice.dao.common.model.object.GatewayDto;
import com.orange.flexoffice.dao.common.model.object.RoomDto;


public class RoomEndpointImpl implements RoomEndpoint {

	private static final Logger LOGGER = Logger.getLogger(RoomEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();

	@Autowired
	private RoomManager roomManager;
	@Autowired
	private BuildingManager buildingManager;
	@Autowired
	private GatewayManager gatewayManager;
	@Autowired
	private UserManager userManager;
	@Autowired
	private TestManager testManager;
	@Autowired
	private ErrorMessageHandler errorMessageHandler;

	@Override
	public List<RoomSummary> getRooms() {

		LOGGER.debug( "Begin call RoomEndpoint.getRooms at: " + new Date() );

		List<RoomDao> dataList = roomManager.findAllRooms();

		List<RoomSummary> roomList = new ArrayList<RoomSummary>();

		for (RoomDao roomDao : dataList) {
			RoomSummary room = factory.createRoomSummary();
			room.setId(roomDao.getColumnId());
			room.setName(roomDao.getName());
			room.setType(ERoomType.valueOf(roomDao.getType().toString()));
			room.setGateway(getGatewayFromId(Long.valueOf(roomDao.getGatewayId()), roomDao.getName()));
			try {
				room.setAddress(getAddressFromBuilding(roomDao.getBuildingId()));
			} catch (DataNotExistsException e) {
				LOGGER.debug("Building with id#" + roomDao.getBuildingId() + " does not exist");
			}
			if (roomDao.getCapacity() != null) {
				room.setCapacity(BigInteger.valueOf(roomDao.getCapacity()));
			}
			if (roomDao.getStatus() != null) {
				room.setStatus(ERoomStatus.valueOf(roomDao.getStatus().toString()));
			}
			room.setTenantName(computeTenant(room.getStatus(), roomDao.getUserId(), roomDao.getName()));

			roomList.add(room);
		}

		LOGGER.debug("List of rooms : nb = " + roomList.size());

		LOGGER.debug( "End call RoomEndpoint.getRooms  at: " + new Date() );

		return roomList;
	}

	/* (non-Javadoc)
	 * @see com.orange.flexoffice.adminui.ws.endPoint.entity.RoomEndpoint#getRoom(java.lang.String)
	 */
	@Override
	public Room getRoom(String roomId) {

		LOGGER.debug( "Begin call RoomEndpoint.getRoom at: " + new Date() );

		try {
			RoomDto roomDto = roomManager.find(Long.valueOf(roomId));

			Room room = factory.createRoom();
			room.setId(String.valueOf(roomDto.getId()));
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

			if (roomDto.getCapacity() != null) {
				room.setCapacity(BigInteger.valueOf(roomDto.getCapacity()));
			}			
			room.setStatus(ERoomStatus.valueOf(roomDto.getStatus().toString()));
			room.setTenant(computeTenantSummary(room.getStatus(), roomDto.getUser(), roomDto.getName()));
			if (roomDto.getTemperature() != null) {
				room.setTemperature(roomDto.getTemperature());
			}
			if (roomDto.getHumidity() != null) {
				room.setHumidity(roomDto.getHumidity());
			}
			
			BuildingDto buidingDto = buildingManager.find(Long.valueOf(roomDto.getBuildingId()));
			Location location = factory.createLocation();
				BuildingOutput building = factory.createBuildingOutput();
					building.setId(String.valueOf(buidingDto.getId()));
					building.setName(buidingDto.getName());
					building.setAddress(buidingDto.getAddress());
					building.setNbFloors(BigInteger.valueOf(buidingDto.getNbFloors()));
			location.setBuilding(building);
				LocationItem locationCountry = factory.createLocationItem();
					locationCountry.setId(buidingDto.getCountryId().toString());
					locationCountry.setName(buidingDto.getCountryName());
			location.setCountry(locationCountry);
				LocationItem locationRegion = factory.createLocationItem();
					locationRegion.setId(buidingDto.getRegionId().toString());
					locationRegion.setName(buidingDto.getRegionName());
			location.setRegion(locationRegion);
				LocationItem locationCity = factory.createLocationItem();
					locationCity.setId(buidingDto.getCityId().toString());
					locationCity.setName(buidingDto.getCityName());
			location.setCity(locationCity);
			location.setFloor(BigInteger.valueOf(roomDto.getFloor()));
			
			room.setLocation(location);
			
			LOGGER.debug( "End call RoomEndpoint.getRoom  at: " + new Date() );

			return room;

		} catch (DataNotExistsException e){

			LOGGER.debug("DataNotExistsException in RoomEndpoint.getSensor with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_31, Response.Status.NOT_FOUND));

		} catch (RuntimeException ex){

			LOGGER.debug("RuntimeException in RoomEndpoint.getSensor with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));

		}


	}


	/* (non-Javadoc)
	 * @see com.orange.flexoffice.adminui.ws.endPoint.entity.RoomEndpoint#addRoom
	 */
	@Override
	public RoomOutput addRoom(RoomInput1 roomInput) {

		LOGGER.debug( "Begin call RoomEndpoint.addRoom  at: " + new Date() );
		
		try {
			
		RoomDao roomDao = new RoomDao();
		roomDao.setName(roomInput.getName());
		roomDao.setType(roomInput.getType().toString());
		roomDao.setBuildingId(Long.valueOf(roomInput.getBuildingId()));
		roomDao.setFloor(roomInput.getFloor().longValue());
		
		if (roomInput.getGateway() !=null) {
			GatewayDto gateway = gatewayManager.findByMacAddress(roomInput.getGateway().getMacAddress());
			roomDao.setGatewayId(Long.valueOf(gateway.getId()));
		} else {
			roomDao.setGatewayId(0L);
		}
		
		String desc = roomInput.getDesc(); 
		if ( desc != null) { 
			roomDao.setDescription(roomInput.getDesc());
		}
		
		roomDao.setCapacity(roomInput.getCapacity().intValue());
		roomDao.setStatus(ERoomStatus.UNKNOWN.toString());	
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "addRoom with parameters :");
			final StringBuilder message = new StringBuilder( 1000 );
			message.append( "name :" );
			message.append( roomInput.getName() );
			message.append( "\n" );
			message.append( "gateway id :" );
			message.append( roomDao.getGatewayId() );
			message.append( "\n" );
			LOGGER.debug( message.toString() );
		}

			roomDao = roomManager.save(roomDao);
			
			RoomOutput returnedRoom = factory.createRoomOutput();
			returnedRoom.setId(roomDao.getColumnId());
			returnedRoom.setName(roomDao.getName());

			LOGGER.debug( "End call RoomEndpoint.addRoom at: " + new Date() );

			return factory.createRoomOutput(returnedRoom).getValue();


		} catch (DataNotExistsException e){
			LOGGER.debug("DataNotExistsException in RoomEndpoint.saveRoom with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_29, Response.Status.NOT_FOUND));
		} catch (DataAlreadyExistsException e) {
			
			LOGGER.debug("DataNotExistsException in RoomEndpoint.addRoom with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_28, Response.Status.METHOD_NOT_ALLOWED));

		} catch (RuntimeException ex) {

			LOGGER.debug("RuntimeException in RoomEndpoint.addRoom with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}


		}

	
	/* (non-Javadoc)
	 * @see com.orange.flexoffice.adminui.ws.endPoint.entity.RoomEndpoint#updateRoom
	 */
	@Override
	public Response updateRoom(String id, RoomInput1 roomInput) {
		
		LOGGER.debug( "Begin call RoomEndpoint.updateRoom at: " + new Date() );

		try {
			
		RoomDao roomDao = new RoomDao();
		roomDao.setId(Long.valueOf(id));
		roomDao.setName(roomInput.getName());
		roomDao.setType(roomInput.getType().toString());
		roomDao.setBuildingId(Long.valueOf(roomInput.getBuildingId()));
		roomDao.setFloor(roomInput.getFloor().longValue());

		if (roomInput.getGateway() !=null) {
			GatewayDto gateway = gatewayManager.findByMacAddress(roomInput.getGateway().getMacAddress());
			roomDao.setGatewayId(Long.valueOf(gateway.getId()));
		} else {
			roomDao.setGatewayId(0L);
		}
		
		String desc = roomInput.getDesc(); 
		if ( desc != null) { 
			roomDao.setDescription(roomInput.getDesc());
		}
		roomDao.setCapacity(roomInput.getCapacity().intValue());
		
		roomDao = roomManager.update(roomDao);

		LOGGER.debug( "End call RoomEndpoint.updateRoom at: " + new Date() );
		return Response.status(Status.ACCEPTED).build();

		} catch (DataNotExistsException e){
			LOGGER.debug("DataNotExistsException in RoomEndpoint.updateRoom with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_29, Response.Status.NOT_FOUND));
		} catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in RoomEndpoint.updateRoom with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}

	}


	/* (non-Javadoc)
	 * @see com.orange.flexoffice.adminui.ws.endPoint.entity.RoomEndpoint#removeRoom
	 */
	@Override
	public Response removeRoom(String id) {
		
		LOGGER.debug( "Begin call RoomEndpoint.removeRoom at: " + new Date() );

		try {

			roomManager.delete(Long.valueOf(id));

		} catch (DataNotExistsException e){
			
			LOGGER.debug("DataNotExistsException in RoomEndpoint.removeRoom with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_30, Response.Status.NOT_FOUND));
			
		} catch (IntegrityViolationException e){
			LOGGER.debug("IntegrityViolationException in RoomEndpoint.removeRoom with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_30, Response.Status.METHOD_NOT_ALLOWED));
		} catch (RuntimeException ex){

			LOGGER.debug("RuntimeException in RoomEndpoint.removeRoom with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}

		LOGGER.debug( "End call RoomEndpoint.removeRoom at: " + new Date() );

		return Response.noContent().build();
	}
	
	/* (non-Javadoc)
	 * @see com.orange.flexoffice.adminui.ws.endPoint.entity.RoomEndpoint#findByName
	 */
	@Override
	public RoomDao findByName(String name) throws DataNotExistsException {
		return roomManager.findByName(name);
	}

	@Override
	public boolean initRoomStatsTable() {
		return testManager.initRoomStatsTableForAdminUI();
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
		return gateway;		
	}

	/**
	 * getAddressFromBuilding
	 * @param buildingId
	 * @return
	 * @throws DataNotExistsException
	 */
	private String getAddressFromBuilding(final Long buildingId) throws DataNotExistsException {
			final BuildingDto buiding = buildingManager.find(Long.valueOf(buildingId));
			return buiding.getAddress();	
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

		String tenant = "";

		// Compute tenant name only if room is reserved or occupied
		if ((status == ERoomStatus.RESERVED) ||
				(status == ERoomStatus.OCCUPIED)) {

			// And if user is known
			if (userId != null && userId != 0) {
				try {
					// Get user object from DB
					UserDao userDao = userManager.find(userId);
					// Compute tenant name
					if ((userDao != null) && (userDao.getLastName() != null)) {
						tenant = userDao.getFirstName() + " " + userDao.getLastName();
					}

				} catch(DataNotExistsException e ) {
					LOGGER.debug("Get rooms / Get room id : user not found on room " + roomName, e);
				}
			}
		}

		return tenant;
	}



	/** Create tenant if room status is not free 
	 * 
	 * @param status
	 * @param userId
	 * @param roomName
	 * 
	 * @return tenant
	 */
	private UserSummary computeTenantSummary(final ERoomStatus status, final UserDao userDao, final String roomName) {
		UserSummary tenant = factory.createUserSummary();

		// Compute tenant name only if room is reserved or occupied
		if ((status == ERoomStatus.RESERVED) ||
				(status == ERoomStatus.OCCUPIED)) {

			if (userDao == null) {

				LOGGER.info("Get rooms / Get room id   : user not found on room " + roomName);

			// And if user is known
			} else {
				tenant.setId(userDao.getId().toString());
				// Compute label
				tenant.setLabel(userDao.getFirstName() + " " + userDao.getLastName());
				tenant.setFirstName(userDao.getFirstName());
				tenant.setLastName(userDao.getLastName());
				tenant.setEmail(userDao.getEmail());
			}
		}

		return tenant;
	}
	

}
