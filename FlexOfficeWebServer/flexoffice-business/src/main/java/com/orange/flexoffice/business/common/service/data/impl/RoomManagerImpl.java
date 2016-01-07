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
import com.orange.flexoffice.business.common.service.data.RoomManager;
import com.orange.flexoffice.dao.common.model.data.ConfigurationDao;
import com.orange.flexoffice.dao.common.model.data.GatewayDao;
import com.orange.flexoffice.dao.common.model.data.RoomDao;
import com.orange.flexoffice.dao.common.model.data.RoomStatDao;
import com.orange.flexoffice.dao.common.model.data.SensorDao;
import com.orange.flexoffice.dao.common.model.data.UserDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_ConfigurationKey;
import com.orange.flexoffice.dao.common.model.enumeration.E_RoomInfo;
import com.orange.flexoffice.dao.common.model.enumeration.E_RoomStatus;
import com.orange.flexoffice.dao.common.model.enumeration.E_RoomType;
import com.orange.flexoffice.dao.common.model.object.RoomDto;
import com.orange.flexoffice.dao.common.repository.data.jdbc.ConfigurationDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.GatewayDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RoomDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RoomStatDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.SensorDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.UserDaoRepository;

/**
 * Manages {@link RoomDto}.
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
	private ConfigurationDaoRepository configRepository;

	@Override
	@Transactional(readOnly=true)
	public List<RoomDao> findAllRooms() {
		return roomRepository.findAllRooms();
	}

	@Override
	@Transactional(readOnly=true)
	public RoomDto find(long roomId)  throws DataNotExistsException {

		RoomDao roomDao;
		try {
			roomDao = roomRepository.findOne(roomId);

		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.error("RoomManager.find : Room by id #" + roomId + " is not found", e);
			throw new DataNotExistsException("RoomManager.find : Room by id #" + roomId + " is not found");
		}


		RoomDto dto = new RoomDto();
		dto.setId(roomId);
		dto.setDescription(roomDao.getDescription());
		dto.setName(roomDao.getName());
		dto.setType(E_RoomType.valueOf(roomDao.getType()));
		dto.setAddress(roomDao.getAddress());
		dto.setCapacity(roomDao.getCapacity());
		dto.setStatus(E_RoomStatus.valueOf(roomDao.getStatus()));
		dto.setType(E_RoomType.valueOf(roomDao.getType()));
		dto.setLastMeasureDate(roomDao.getLastMeasureDate());

		if (roomDao.getUserId() != null) {
			try {
				UserDao userDao = userRepository.findOne(roomDao.getUserId());
				dto.setUser(userDao);
			} catch(IncorrectResultSizeDataAccessException e ) {
				LOGGER.debug("RoomManager.find : User by id #" + roomDao.getUserId() + " for current Room is not found", e);
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
			return roomRepository.saveRoom(roomDao);
		} catch (DataIntegrityViolationException e) {
			LOGGER.error("RoomManager.save : Room already exists", e);
			throw new DataAlreadyExistsException("RoomManager.save : Room already exists");
		}
	}

	@Override
	public RoomDao update(RoomDao roomDao) throws DataNotExistsException {
		try {
			// Update RoomDao
			return roomRepository.updateRoom(roomDao);
		} catch (RuntimeException e) {
			LOGGER.error("RoomManager.update : Room to update not found", e);
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
				if (!foundRoom.getStatus().equals(E_RoomStatus.FREE.toString())) {
					LOGGER.debug("Room status is not FREE !!!");
					throw new RoomAlreadyUsedException("RoomManager.updateStatus : Room is not in FREE status");
				} else {
					LOGGER.debug("RoomStat to create !!!");
					RoomStatDao roomStat = new RoomStatDao();
					roomStat.setRoomId(roomDao.getId().intValue());
					roomStat.setUserId(roomDao.getUserId().intValue());
					roomStat.setRoomInfo(E_RoomInfo.RESERVED.toString());
					roomStatRepository.saveReservedRoomStat(roomStat);
				}
			} else if  (roomDao.getStatus().equals(E_RoomStatus.FREE.toString())) { // from UserUi.RoomEndpoint.cancelRoom
				RoomDao foundRoom = roomRepository.findByRoomId(roomDao.getId());
				if (foundRoom.getStatus().equals(E_RoomStatus.RESERVED.toString())) { // cancel is operate only if room is in RESERVED status
					LOGGER.debug("RoomStat to update !!!");
					RoomStatDao roomStat = new RoomStatDao();
					roomStat.setRoomId(roomDao.getId().intValue());
					roomStat.setUserId(roomDao.getUserId().intValue());
					roomStat.setRoomInfo(E_RoomInfo.CANCELED.toString());
					roomStatRepository.updateReservedRoomStat(roomStat); // PS : there is only one line in room_stats with the same room_id, user_id and room_info=RESERVED !!!
					roomDao.setUserId(null);
				}
			}
			// update RoomDao => status & user_id
			
			return roomRepository.updateRoomStatus(roomDao);
		} catch (RuntimeException e) {
			LOGGER.error("RoomManager.updateStatus : Room to update Status not found", e);
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
					// Delete Room
					roomRepository.delete(id);
				}
			} else {
				LOGGER.error("RoomManager.delete : Room #" + id + " is reserved by userId:" + room.getUserId());
				throw new IntegrityViolationException("RoomManager.delete : Room #" + id + " is reserved");
			}
		} catch (IncorrectResultSizeDataAccessException e) {
			LOGGER.error("RoomManager.delete : Room #" + id + " not found", e);
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
			LOGGER.debug("RoomManager.findByName : Room by name #" + name + " is not found", e);
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
		
		}
		return dataList;
	}
	
	@Override
	@Transactional(readOnly=true)
	public Long countRoomsByType(String type) {
		return roomRepository.countRoomsByType(type);
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
	
	
}
