package com.orange.flexoffice.userui.ws.endPoint.entity.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.AuthenticationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.userui.ws.model.ERoomStatus;
import com.orange.flexoffice.userui.ws.model.ERoomType;
import com.orange.flexoffice.userui.ws.model.UserSummary;
import com.orange.flexoffice.userui.ws.utils.ErrorMessageHandler;
import com.orange.flexoffice.business.common.enums.EnumErrorModel;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.RoomAlreadyUsedException;
import com.orange.flexoffice.business.common.service.data.RoomManager;
import com.orange.flexoffice.business.common.service.data.TestManager;
import com.orange.flexoffice.business.common.service.data.UserManager;
import com.orange.flexoffice.dao.common.model.data.RoomDao;
import com.orange.flexoffice.dao.common.model.data.UserDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_RoomStatus;
import com.orange.flexoffice.dao.common.model.object.RoomDto;
import com.orange.flexoffice.dao.common.model.object.UserDto;
import com.orange.flexoffice.userui.ws.endPoint.entity.RoomEndpoint;
import com.orange.flexoffice.userui.ws.model.ObjectFactory;
import com.orange.flexoffice.userui.ws.model.Room;
import com.orange.flexoffice.userui.ws.model.RoomSummary;

/**
 * @author oab
 *
 */
public class RoomEndpointImpl implements RoomEndpoint {
	
	private static final Logger LOGGER = Logger.getLogger(RoomEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();
	
	@Autowired
	private RoomManager roomManager;
	@Autowired
	private UserManager userManager;
	@Autowired
	private ErrorMessageHandler errorMessageHandler;
	
	@Autowired
	private TestManager testManager;
	
	/**
	 * Gets rooms.
	 * 
	 * @return room summary list.
	 * 
	 * @see RoomSummary
	 */
	@Override
	public List<RoomSummary> getRooms(String auth, Boolean latest) {
		LOGGER.debug( "Begin call UserUi.RoomEndpoint.getRooms at: " + new Date() );
		List<RoomDao> dataList = null;
		
		if (latest) { // get latest reserved rooms
			try {
				// get UserDto
				UserDto data = userManager.findByUserAccessToken(auth);
				 // get latest reserved rooms by userId
				dataList = roomManager.findLatestReservedRoomsByUserId(data.getId());
			} catch (AuthenticationException e){
				LOGGER.debug("DataNotExistsException in UserUi.RoomEndpoint.getRooms with message :", e);
				throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_34, Response.Status.UNAUTHORIZED));
			}	
		} else { // get all rooms
			dataList = roomManager.findAllRooms();
		}

		List<RoomSummary> roomList = new ArrayList<RoomSummary>();
		
		if (dataList != null) {
			for (RoomDao roomDao : dataList) {
				RoomSummary room = factory.createRoomSummary();
				room.setId(roomDao.getColumnId());
				room.setName(roomDao.getName());
				room.setType(ERoomType.valueOf(roomDao.getType()));
				if (roomDao.getCapacity() != null) {
					room.setCapacity(BigInteger.valueOf(roomDao.getCapacity()));
				}
				room.setStatus(ERoomStatus.valueOf(roomDao.getStatus().toString()));
				room.setTenantName(computeTenant(room.getStatus(), roomDao.getUserId(), roomDao.getName()));
				roomList.add(room);
			}
		}

		LOGGER.debug("UserUi.RoomEndpoint.getRooms List of rooms : nb = " + roomList.size());
		LOGGER.debug( "End call UserUi.RoomEndpoint.getRooms  at: " + new Date() );
		return roomList;
	}

	/**
	 * Gets information on a specific room.
	 * 
	 * @param roomId
	 *            the room ID
	 * 
	 * @return information about a specific room.
	 * 
	 * @see Room
	 */
	@Override
	public Room getRoom(String roomId) {
		
		LOGGER.debug( "Begin call UserUi.RoomEndpoint.getRoom at: " + new Date() );

		try {
			RoomDto roomDto = roomManager.find(Long.valueOf(roomId));

			Room room = factory.createRoom();
			room.setId(String.valueOf(roomDto.getId()));
			room.setName(roomDto.getName());
			room.setType(ERoomType.valueOf(roomDto.getType().toString()));
			room.setDesc(roomDto.getDescription());
			room.setAddress(roomDto.getAddress());
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
			
			if (roomDto.getLastMeasureDate() != null) {
				room.setLastMeasureDate(BigInteger.valueOf(roomDto.getLastMeasureDate().getTime()));
			}
			
			LOGGER.debug( "End call UserUi.RoomEndpoint.getRoom  at: " + new Date() );

			return room;

		} catch (DataNotExistsException e){
			LOGGER.debug("DataNotExistsException in UserUi.RoomEndpoint.getSensor with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_31, Response.Status.NOT_FOUND));
		} catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in UserUi.RoomEndpoint.getSensor with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}
	}

	/**
	 * Reserve a room.
	 * 
	 * @param roomId
	 *            the room ID
	 *            
	 * @return If ok, a <code>Response</code> with a status code 200.
	 * 
	 * @see Response
	 */
	@Override
	public Room reserveRoom(String auth, String roomId) {
		
		LOGGER.debug( "Begin call UserUi.RoomEndpoint.reserveRoom at: " + new Date() );
		LOGGER.debug("Room id in reserveRoom.RoomEndpoint metod is : " + roomId);
		try {
			
			RoomDao roomDao = roomManager.findByRoomId(Long.valueOf(roomId));
			roomDao.setStatus(E_RoomStatus.RESERVED.toString());
	
			// get UserDto
			UserDto data = userManager.findByUserAccessToken(auth);
			// set userId in roomDao
			roomDao.setUserId(Long.valueOf(data.getId()));
			
			// update status 
			roomManager.updateStatus(roomDao);
			
			// get Room
			RoomDto roomDto = roomManager.find(Long.valueOf(roomId));
	
			Room room = factory.createRoom();
			room.setId(String.valueOf(roomDto.getId()));
			room.setName(roomDto.getName());
			room.setType(ERoomType.valueOf(roomDto.getType().toString()));
			room.setDesc(roomDto.getDescription());
			room.setAddress(roomDto.getAddress());
			room.setCapacity(BigInteger.valueOf(roomDto.getCapacity()));			
			room.setStatus(ERoomStatus.valueOf(roomDto.getStatus().toString()));
			room.setTenant(computeTenantSummary(room.getStatus(), roomDto.getUser(), roomDto.getName()));
			if (roomDto.getTemperature() != null) {
				room.setTemperature(roomDto.getTemperature());
			}
			if (roomDto.getHumidity() != null) {
				room.setHumidity(roomDto.getHumidity());
			}
			
			if (roomDto.getLastMeasureDate() != null) {
				room.setLastMeasureDate(BigInteger.valueOf(roomDto.getLastMeasureDate().getTime()));
			}
			
			LOGGER.debug( "End call UserUi.RoomEndpoint.reserveRoom at: " + new Date() );
			
			return room;

		} catch (AuthenticationException e){
			LOGGER.debug("DataNotExistsException in UserUi.RoomEndpoint.reserveRoom with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_34, Response.Status.UNAUTHORIZED));
		}catch (DataNotExistsException e){
			LOGGER.debug("DataNotExistsException in UserUi.RoomEndpoint.reserveRoom with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_29, Response.Status.NOT_FOUND));
		} catch (RoomAlreadyUsedException e){
			LOGGER.debug("RoomAlreadyUsedException in UserUi.RoomEndpoint.reserveRoom with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_29, Response.Status.METHOD_NOT_ALLOWED));
		} catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in UserUi.RoomEndpoint.reserveRoom with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}

	}

	/**
	 * Cancel reservation of a room.
	 * 
	 * @param roomId
	 *            the room ID
	 *            
	 * @return If ok, a <code>Response</code> with a status code 200.
	 * 
	 * @see Response
	 */
	@Override
	public Room cancelRoom(String auth, String roomId) {
		
		LOGGER.debug( "Begin call UserUi.RoomEndpoint.cancelRoom at: " + new Date() );

		try {
			// get room
			RoomDao roomDao = roomManager.findByRoomId(Long.valueOf(roomId));
			roomDao.setStatus(E_RoomStatus.FREE.toString());

			// get user
			UserDto data = userManager.findByUserAccessToken(auth);
			// set userId in roomDao
			roomDao.setUserId(Long.valueOf(data.getId()));

			// update status
			roomManager.updateStatus(roomDao);

			// get Room
			RoomDto roomDto = roomManager.find(Long.valueOf(roomId));

			Room room = factory.createRoom();
			room.setId(String.valueOf(roomDto.getId()));
			room.setName(roomDto.getName());
			room.setType(ERoomType.valueOf(roomDto.getType().toString()));
			room.setDesc(roomDto.getDescription());
			room.setAddress(roomDto.getAddress());
			room.setCapacity(BigInteger.valueOf(roomDto.getCapacity()));			
			room.setStatus(ERoomStatus.valueOf(roomDto.getStatus().toString()));
			if (roomDto.getTemperature() != null) {
				room.setTemperature(roomDto.getTemperature());
			}
			if (roomDto.getHumidity() != null) {
				room.setHumidity(roomDto.getHumidity());
			}
			
			if (roomDto.getLastMeasureDate() != null) {
				room.setLastMeasureDate(BigInteger.valueOf(roomDto.getLastMeasureDate().getTime()));
			}
			
			LOGGER.debug( "End call UserUi.RoomEndpoint.cancelRoom at: " + new Date() );
			
			return room;

		} catch (AuthenticationException e){
			LOGGER.debug("DataNotExistsException in UserUi.RoomEndpoint.cancelRoom with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_34, Response.Status.UNAUTHORIZED));
		} catch (DataNotExistsException e){
			LOGGER.debug("DataNotExistsException in UserUi.RoomEndpoint.cancelRoom with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_29, Response.Status.NOT_FOUND));
		} catch (RoomAlreadyUsedException e){
			LOGGER.debug("RoomAlreadyUsedException in UserUi.RoomEndpoint.cancelRoom with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_29, Response.Status.METHOD_NOT_ALLOWED));
		} catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in UserUi.RoomEndpoint.cancelRoom with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}

	}
	
	

	/** Initialize tests data in DB
	 * 
	 * @return true if successfully done
	 */
	@Override
	public boolean executeInitTestFile() {
		return testManager.executeInitTestFile();
	}
	
	@Override
	public boolean initRoomStatsTable() {
		return testManager.initRoomStatsTableForUserUI();
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

		String tenant = null;

		// Compute tenant name only if room is reserved or occupied
		if ((status == ERoomStatus.RESERVED) ||
				(status == ERoomStatus.OCCUPIED)) {

			// And if user is known
			if (userId != null && userId != 0) {
				try {
					// Get user object from DB
					UserDao userDao = userManager.find(userId);
					
					// Compute tenant name
					if (userDao.getFirstName() == null) {
						userDao.setFirstName("");
					}
					
					if (userDao.getLastName() == null) {
						userDao.setLastName("");
					} 
					
					tenant = userDao.getFirstName() + " " + userDao.getLastName();
					if (tenant.trim().isEmpty()) {
						tenant = userDao.getEmail();
					}
	
				} catch(DataNotExistsException e ) {
					LOGGER.debug("UserUi Get rooms / Get room id : user not found on room " + roomName, e);
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
		
		UserSummary tenant = null;
		// Compute tenant name only if room is reserved or occupied
		if ((status == ERoomStatus.RESERVED) ||
				(status == ERoomStatus.OCCUPIED)) {

			if (userDao == null) {
				LOGGER.info("UserUi Get rooms / Get room id   : user not found on room " + roomName);
			// And if user is known
			} else {
				tenant = factory.createUserSummary();
				tenant.setId(userDao.getId().toString());
				
				// Compute tenant
				if (userDao.getFirstName() != null) {
					tenant.setFirstName(userDao.getFirstName());
				} else {
					userDao.setFirstName("");
				}
				
				if (userDao.getLastName() != null) {
					tenant.setLastName(userDao.getLastName());
				} else {
					userDao.setLastName("");
				}
				
				// label field
				String label = userDao.getFirstName() + " " + userDao.getLastName();
				if (label.trim().isEmpty()) {
					label = userDao.getEmail();
				}
				
				tenant.setLabel(label.trim());
				
			}
		}

		return tenant;
	}

	
}
