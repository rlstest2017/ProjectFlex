package com.orange.flexoffice.business.common.service.data.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.business.common.exception.RoomAlreadyUsedException;
import com.orange.flexoffice.business.common.service.data.BuildingManager;
import com.orange.flexoffice.business.common.service.data.RoomManager;
import com.orange.flexoffice.dao.common.model.data.ConfigurationDao;
import com.orange.flexoffice.dao.common.model.data.GatewayDao;
import com.orange.flexoffice.dao.common.model.data.RoomDao;
import com.orange.flexoffice.dao.common.model.data.RoomStatDao;
import com.orange.flexoffice.dao.common.model.data.SensorDao;
import com.orange.flexoffice.dao.common.model.data.UserDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_CommandModel;
import com.orange.flexoffice.dao.common.model.enumeration.E_ConfigurationKey;
import com.orange.flexoffice.dao.common.model.enumeration.E_RoomInfo;
import com.orange.flexoffice.dao.common.model.enumeration.E_RoomStatus;
import com.orange.flexoffice.dao.common.model.enumeration.E_RoomType;
import com.orange.flexoffice.dao.common.model.object.BuildingDto;
import com.orange.flexoffice.dao.common.model.object.RoomBuildingInfosDto;
import com.orange.flexoffice.dao.common.model.object.RoomDto;
import com.orange.flexoffice.dao.common.repository.data.jdbc.ConfigurationDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.GatewayDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RoomDailyOccupancyDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RoomDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RoomStatDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.SensorDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.UserDaoRepository;

/**
 * Manages {@link RoomDto}.
 * For PROD LOG LEVEL is info then we say info & error logs.
 * 
 * @author oab
 */
@Service("RoomManager")
@Transactional
public class RoomManagerImpl implements RoomManager {

	private static final Logger LOGGER = Logger.getLogger(RoomManagerImpl.class);

	@Autowired
	private RoomDaoRepository roomRepository;
	@Autowired
	private GatewayDaoRepository gatewayRepository;
	@Autowired
	private SensorDaoRepository sensorRepository;
	@Autowired
	private UserDaoRepository userRepository;
	@Autowired
	private RoomStatDaoRepository roomStatRepository;
	@Autowired
	private RoomDailyOccupancyDaoRepository roomDailyRepository;
	@Autowired
	private ConfigurationDaoRepository configRepository;
	@Autowired
	private BuildingManager buildingManager;

	@Override
	@Transactional(readOnly=true)
	public List<RoomDao> findAllRooms() {
		return roomRepository.findAllRooms();
	}

	@Override
	public List<RoomDao> findRoomsByCriteria(String countryId, String regionId, String cityId, String buildingId, Integer floor) {
		
		List<RoomDao> rooms;
		
		if (buildingId != null) {
			if (floor != null) {
				RoomBuildingInfosDto buildingInfo = new RoomBuildingInfosDto();
				buildingInfo.setBuildingId(Long.valueOf(buildingId));
				buildingInfo.setFloor(Long.valueOf(floor));
				rooms = roomRepository.findRoomsByBuildingIdAndFloor(buildingInfo);
			} else {
				rooms = roomRepository.findRoomsByBuildingId(Long.valueOf(buildingId));
			}
		} else if (cityId != null) {
			rooms = roomRepository.findRoomsByCityId(Long.valueOf(cityId));
		} else if (regionId != null) {
			rooms = roomRepository.findRoomsByRegionId(Long.valueOf(regionId));
		} else if (countryId != null) {
			rooms = roomRepository.findRoomsByCountryId(Long.valueOf(countryId));
		} else {
			rooms = roomRepository.findAllRooms();
		}
				
		return rooms;
	}
	
	@Override
	@Transactional(readOnly=true)
	public RoomDto find(long roomId) throws DataNotExistsException {

		RoomDao roomDao;
		try {
			roomDao = roomRepository.findOne(roomId);

		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.debug("RoomManager.find : Room by id #" + roomId + " is not found", e);
			LOGGER.error("RoomManager.find : Room by id #" + roomId + " is not found");
			throw new DataNotExistsException("RoomManager.find : Room by id #" + roomId + " is not found");
		}


		RoomDto dto = new RoomDto();
		dto.setId(roomId);
		dto.setDescription(roomDao.getDescription());
		dto.setName(roomDao.getName());
		dto.setType(E_RoomType.valueOf(roomDao.getType()));
		dto.setCapacity(roomDao.getCapacity());
		dto.setStatus(E_RoomStatus.valueOf(roomDao.getStatus()));
		dto.setType(E_RoomType.valueOf(roomDao.getType()));
		dto.setLastMeasureDate(roomDao.getLastMeasureDate());
		dto.setTemperature(roomDao.getTemperature());
		dto.setHumidity(roomDao.getHumidity());
		dto.setBuildingId(roomDao.getBuildingId());
		dto.setFloor(roomDao.getFloor());
		
		try {
			dto.setAddress(getAddressFromBuilding(roomDao.getBuildingId()));
		} catch (DataNotExistsException e) {
			LOGGER.debug("Building with id#" + roomDao.getBuildingId() + " does not exist");
		}

		
		if (roomDao.getUserId() != null) {
			try {
				UserDao userDao = userRepository.findOne(roomDao.getUserId());
				dto.setUser(userDao);
			} catch(IncorrectResultSizeDataAccessException e ) {
				LOGGER.debug("RoomManager.find : User by id #" + roomDao.getUserId() + " for current Room is not found", e);
				LOGGER.info("RoomManager.find : User by id #" + roomDao.getUserId() + " for current Room is not found");
			}
		}

		List<SensorDao> sensorsDao = sensorRepository.findByRoomId(roomId);
		if (sensorsDao != null && !sensorsDao.isEmpty()) {
			dto.setSensors(sensorsDao);
		}

		try {
			GatewayDao gatewayDao = gatewayRepository.findOne(roomDao.getGatewayId());
			dto.setGateway(gatewayDao);
		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.debug("RoomManager.find : Gateway by id #" + roomDao.getGatewayId() + " for current Room is not found", e);
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "Return find(long roomId) method for RoomManagerImpl, with parameters :");
			final StringBuilder message = new StringBuilder( 1000 );
			message.append( "id :" );
			message.append( String.valueOf(roomId) );
			message.append( "\n" );
			message.append( "name :" );
			message.append( roomDao.getName() );
			message.append( "\n" );
			LOGGER.debug( message.toString() );
		}

		return dto;
	}

	@Override
	public RoomDao save(RoomDao roomDao) throws DataAlreadyExistsException {
		try {
			// Save RoomDao
			RoomDao room = roomRepository.saveRoom(roomDao);

			// Set Gateway to RESET 
			GatewayDao gateway = new GatewayDao();
			gateway.setId(room.getGatewayId());
			gateway.setCommand(E_CommandModel.RESET.toString());
			gatewayRepository.updateGatewayCommand(gateway);
			LOGGER.debug("RESET command has set in table for gateway id #: " + room.getGatewayId());
			
			return room;
			
		} catch (DataIntegrityViolationException e) {
			LOGGER.debug("RoomManager.save : Room already exists", e);
			LOGGER.error("RoomManager.save : Room already exists");
			throw new DataAlreadyExistsException("RoomManager.save : Room already exists");
		}
	}

	@Override
	public RoomDao update(RoomDao roomDao) throws DataNotExistsException {
		try {
			// Update RoomDao
			return roomRepository.updateRoom(roomDao);
		} catch (RuntimeException e) {
			LOGGER.debug("RoomManager.update : Room to update not found", e);
			LOGGER.error("RoomManager.update : Room to update not found");
			throw new DataNotExistsException("RoomManager.update : Room to update not found");
		}
	}


	@Override
	public RoomDao updateStatus(RoomDao roomDao) throws DataNotExistsException, RoomAlreadyUsedException {
		try {

			LOGGER.debug("Room id is : " + roomDao.getId());
			// Use in case of RESERVED Room in UserUI
			if  (roomDao.getStatus().equals(E_RoomStatus.RESERVED.toString())) { // from UserUi.RoomEndpoint.reserveRoom
				RoomDao foundRoom = roomRepository.findByRoomId(roomDao.getId());
				
				LOGGER.info("foundRoomStatus is " + foundRoom.getStatus() + " for room#" + foundRoom.getName());
				
				if (!foundRoom.getStatus().equals(E_RoomStatus.FREE.toString())) {
					LOGGER.error("Room status is not FREE !!!");
					throw new RoomAlreadyUsedException("RoomManager.updateStatus : Room is not in FREE status");
				} else {
					LOGGER.debug("RoomStat to create !!!");
					RoomStatDao roomStat = new RoomStatDao();
					roomStat.setRoomId(roomDao.getId().intValue());
					roomStat.setUserId(roomDao.getUserId().intValue());
					roomStat.setRoomInfo(E_RoomInfo.RESERVED.toString());
					roomStatRepository.saveReservedRoomStat(roomStat);
					LOGGER.info("roomStat created for room#" + foundRoom.getName() + " which status is : " + foundRoom.getStatus());
				}
			} else if  (roomDao.getStatus().equals(E_RoomStatus.FREE.toString())) { // from UserUi.RoomEndpoint.cancelRoom
				RoomDao foundRoom = roomRepository.findByRoomId(roomDao.getId());
				if (foundRoom.getStatus().equals(E_RoomStatus.RESERVED.toString())) { // cancel from "Je n'y vais plus " room is in RESERVED status
					LOGGER.debug("RoomStat to update !!!");
					RoomStatDao roomStat = new RoomStatDao();
					roomStat.setRoomId(roomDao.getId().intValue());
					roomStat.setUserId(roomDao.getUserId().intValue());
					roomStat.setRoomInfo(E_RoomInfo.CANCELED.toString());
					roomStatRepository.updateReservedRoomStat(roomStat); // PS : there is only one line in room_stats with the same room_id, user_id and room_info=RESERVED !!!
					LOGGER.info("roomStat updateReservedRoomStat for room#" + foundRoom.getName() + " which status is : " + foundRoom.getStatus());
					roomDao.setUserId(null);
				} else if (foundRoom.getStatus().equals(E_RoomStatus.OCCUPIED.toString())) { // cancel from "J'ai fini " room is in OCCUPIED status
					LOGGER.debug("RoomStat to update !!!");
					RoomStatDao roomStat = new RoomStatDao();
					roomStat.setRoomId(roomDao.getId().intValue());
					roomStatRepository.updateEndOccupancyDate(roomStat);
					LOGGER.info("roomStat updateEndOccupancyDate for room#" + foundRoom.getName() + " which status is : " + foundRoom.getStatus());
					roomDao.setUserId(null);
				}
			}
			// update RoomDao => status & user_id
			
			return roomRepository.updateRoomStatus(roomDao);
		} catch (RuntimeException e) {
			LOGGER.debug("RoomManager.updateStatus : Room to update Status not found", e);
			LOGGER.error("RoomManager.updateStatus : Room to update Status not found");
			throw new DataNotExistsException("RoomManager.updateStatus : Room to update Status not found");
		}
	}

	@Override
	public void delete(long id) throws DataNotExistsException, IntegrityViolationException {

		try {
			// To generate exception if wrong id
			RoomDao room = roomRepository.findOne(id);
			
			if (room.getUserId() == null){
				List<SensorDao> sensors = sensorRepository.findByRoomId(id);
				if ((sensors != null) && (!sensors.isEmpty())) {
					LOGGER.error("RoomManager.delete : Room #" + id + " has a sensors");
					throw new IntegrityViolationException("RoomManager.delete : Room #" + id + " has a sensors");
				} else {
					// Delete the room & associated stats (room_stats, room_daily_occupancy)
					roomRepository.delete(id);
					roomStatRepository.deleteByRoomId(room.getId());
					roomDailyRepository.deleteByRoomId(room.getId());
					
					// Set Gateway to RESET 
					GatewayDao gateway = new GatewayDao();
					gateway.setId(room.getGatewayId());
					gateway.setCommand(E_CommandModel.RESET.toString());
					gatewayRepository.updateGatewayCommand(gateway);
					LOGGER.info("RESET command has set in table for gateway id #: " + room.getGatewayId());
					
				}
			} else if (E_RoomStatus.RESERVED.toString().equals(room.getStatus())) { 
				LOGGER.error("RoomManager.delete : Room #" + id + " is reserved by userId:" + room.getUserId());
				throw new IntegrityViolationException("RoomManager.delete : Room #" + id + " is reserved");
			} else {
				LOGGER.error("RoomManager.delete : Room #" + id + " is occupied");
				throw new IntegrityViolationException("RoomManager.delete : Room #" + id + " is occupied");
			}
		} catch (IncorrectResultSizeDataAccessException e) {
			LOGGER.debug("RoomManager.delete : Room #" + id + " not found", e);
			LOGGER.error("RoomManager.delete : Room #" + id + " not found");
			throw new DataNotExistsException("RoomManager.delete : Room #" + id + " not found");
		}
	}

	/**
	 * @param name
	 * 
	 * @return RoomDao object if found
	 */
	@Override
	@Transactional(readOnly=true)
	public RoomDao findByName(String name) throws DataNotExistsException {
		try {
			return roomRepository.findByName(name);
		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.debug("RoomManager.findByName : room#" + name + " is not found", e);
			LOGGER.error("RoomManager.findByName : room#" + name + " is not found");
			throw new DataNotExistsException("RoomManager.findByName : Room by name #" + name + " is not found");
		}
	}


	@Override
	@Transactional(readOnly=true)
	public List<RoomDao> findLatestReservedRoomsByUserId(String userId) {
		List<RoomDao> dataList = null;
		
		ConfigurationDao lastReservedCount = configRepository.findByKey(E_ConfigurationKey.LAST_RESERVED_COUNT.toString());
		int lastReservedCountValue = Integer.valueOf(lastReservedCount.getValue());
		int countAddedRoomStats = 0;
		
		try {
		// get reserved roomStats by userId order by reservation_date desc
		List<RoomStatDao> roomStatsFromDB = roomStatRepository.findLatestReservedRoomsByUserId(Long.valueOf(userId));
		
		// get latest reserved roomStats by userId
		List<RoomStatDao> roomStats = removeDuplicateFromList(roomStatsFromDB);
		
			if ((roomStats != null) && (!roomStats.isEmpty())) {
				dataList = new ArrayList<RoomDao>();
				for (RoomStatDao roomStatDao : roomStats) {
					RoomDao roomDao = roomRepository.findByRoomId(Long.valueOf(roomStatDao.getRoomId()));
					dataList.add(roomDao);
					countAddedRoomStats = countAddedRoomStats +1;
					if ((lastReservedCountValue != 0)&&(countAddedRoomStats == lastReservedCountValue)) {
						break; // sortir de la boucle for
					} 
				}
			}
		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.debug("user by id " + userId + " has not roomStats", e);
			LOGGER.info("user by id " + userId + " has not roomStats");
		
		}
		return dataList;
	}
	
	@Override
	@Transactional(readOnly=true)
	public Long countRoomsByType(String type) {
		return roomRepository.countRoomsByType(type);
	}
	
	@Override
	public RoomDao findByRoomId(Long roomId) throws DataNotExistsException {
		try {
			return roomRepository.findByRoomId(roomId);
		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.debug("RoomManager.findByRoomId : Room by Id #" + roomId + " is not found", e);
			LOGGER.error("RoomManager.findByRoomId : Room by Id #" + roomId + " is not found");
			throw new DataNotExistsException("RoomManager.findByRoomId : Room by Id #" + roomId + " is not found");
		}
	}
	
	/**
	 * removeDuplicateFromList
	 * @param roomStats
	 * @return
	 */
	private List<RoomStatDao> removeDuplicateFromList(List<RoomStatDao> roomStats) {
		List<RoomStatDao> dataList = null;
		if ((roomStats != null) && (!roomStats.isEmpty())) {
			int s=0;
			dataList = new ArrayList<RoomStatDao>();
			for (RoomStatDao roomStatDao : roomStats) {
				for (RoomStatDao data : dataList) {
					if (roomStatDao.getRoomId() == data.getRoomId()) {
						s=1;
						break;
					} 
				} if (s==0) {
					dataList.add(roomStatDao);
				}
				s=0;
			}
		}
		
		return dataList;
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
	
}
