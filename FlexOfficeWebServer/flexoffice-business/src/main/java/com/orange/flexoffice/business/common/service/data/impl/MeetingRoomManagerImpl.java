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
import com.orange.flexoffice.business.common.service.data.MeetingRoomManager;
import com.orange.flexoffice.dao.common.model.data.AgentDao;
import com.orange.flexoffice.dao.common.model.data.MeetingRoomDao;
import com.orange.flexoffice.dao.common.model.data.RoomStatDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_MeetingRoomStatus;
import com.orange.flexoffice.dao.common.model.enumeration.E_MeetingRoomType;
import com.orange.flexoffice.dao.common.model.object.BuildingDto;
import com.orange.flexoffice.dao.common.model.object.MeetingRoomBuildingInfosDto;
import com.orange.flexoffice.dao.common.model.object.MeetingRoomDto;
import com.orange.flexoffice.dao.common.repository.data.jdbc.AgentDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.ConfigurationDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.MeetingRoomDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RoomDailyOccupancyDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RoomStatDaoRepository;

/**
 * Manages {@link MeetingRoomDto}.
 * For PROD LOG LEVEL is info then we say info & error logs.
 * 
 * @author oab
 */
@Service("MeetingRoomManager")
@Transactional
public class MeetingRoomManagerImpl implements MeetingRoomManager {

	private static final Logger LOGGER = Logger.getLogger(MeetingRoomManagerImpl.class);

	@Autowired
	private MeetingRoomDaoRepository meetingroomRepository;
	@Autowired
	private AgentDaoRepository agentRepository;
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
	public List<MeetingRoomDao> findAllMeetingRooms() {
		return meetingroomRepository.findAllMeetingRooms();
	}

	@Override
	public List<MeetingRoomDao> findMeetingRoomsByCriteria(String countryId, String regionId, String cityId, String buildingId, Integer floor) {
		
		List<MeetingRoomDao> meetingrooms;
		
		if (buildingId != null) {
			if (floor != null) {
				MeetingRoomBuildingInfosDto buildingInfo = new MeetingRoomBuildingInfosDto();
				buildingInfo.setBuildingId(Long.valueOf(buildingId));
				buildingInfo.setFloor(Long.valueOf(floor));
				meetingrooms = meetingroomRepository.findMeetingRoomsByBuildingIdAndFloor(buildingInfo);
			} else {
				meetingrooms = meetingroomRepository.findMeetingRoomsByBuildingId(Long.valueOf(buildingId));
			}
		} else if (cityId != null) {
			meetingrooms = meetingroomRepository.findMeetingRoomsByCityId(Long.valueOf(cityId));
		} else if (regionId != null) {
			meetingrooms = meetingroomRepository.findMeetingRoomsByRegionId(Long.valueOf(regionId));
		} else if (countryId != null) {
			meetingrooms = meetingroomRepository.findMeetingRoomsByCountryId(Long.valueOf(countryId));
		} else {
			meetingrooms = meetingroomRepository.findAllMeetingRooms();
		}
				
		return meetingrooms;
	}
	
	@Override
	@Transactional(readOnly=true)
	public MeetingRoomDto find(long meetingroomId) throws DataNotExistsException {

		MeetingRoomDao meetingroomDao;
		try {
			meetingroomDao = meetingroomRepository.findOne(meetingroomId);

		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.debug("MeetingRoomManager.find : Meeting Room by id #" + meetingroomId + " is not found", e);
			LOGGER.error("MeetingRoomManager.find : Meeting Room by id #" + meetingroomId + " is not found");
			throw new DataNotExistsException("RoomManager.find : Meeting Room by id #" + meetingroomId + " is not found");
		}


		MeetingRoomDto dto = new MeetingRoomDto();
		dto.setId(meetingroomId);
		dto.setExternalId(meetingroomDao.getExternalId());
		dto.setDescription(meetingroomDao.getDescription());
		dto.setName(meetingroomDao.getName());
		dto.setType(E_MeetingRoomType.valueOf(meetingroomDao.getType()));
		dto.setCapacity(meetingroomDao.getCapacity());
		dto.setStatus(E_MeetingRoomStatus.valueOf(meetingroomDao.getStatus()));
		dto.setType(E_MeetingRoomType.valueOf(meetingroomDao.getType()));
		dto.setLastMeasureDate(meetingroomDao.getLastMeasureDate());
		dto.setBuildingId(meetingroomDao.getBuildingId());
		dto.setFloor(meetingroomDao.getFloor());
		dto.setStartDate(meetingroomDao.getStartDate());
		dto.setEndDate(meetingroomDao.getEndDate());
		
		try {
			dto.setAddress(getAddressFromBuilding(meetingroomDao.getBuildingId()));
		} catch (DataNotExistsException e) {
			LOGGER.debug("Building with id#" + meetingroomDao.getBuildingId() + " does not exist");
		}

		
		if (meetingroomDao.getOrganizerLabel() != null) {
			dto.setOrganizerLabel(meetingroomDao.getOrganizerLabel());
		}

		try {
			AgentDao agentDao = agentRepository.findOne(meetingroomDao.getAgentId());
			dto.setAgent(agentDao);
		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.debug("MeetingRoomManager.find : Agent by id #" + meetingroomDao.getAgentId() + " for current Meeting Room is not found", e);
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "Return find(long meetingroomId) method for MeetingRoomManagerImpl, with parameters :");
			final StringBuilder message = new StringBuilder( 1000 );
			message.append( "id :" );
			message.append( String.valueOf(meetingroomId) );
			message.append( "\n" );
			message.append( "name :" );
			message.append( meetingroomDao.getName() );
			message.append( "\n" );
			LOGGER.debug( message.toString() );
		}

		return dto;
	}

	@Override
	public MeetingRoomDao save(MeetingRoomDao meetingroomDao) throws DataAlreadyExistsException {
		try {
			// Save MeetingRoomDao
			MeetingRoomDao meetingroom = meetingroomRepository.saveMeetingRoom(meetingroomDao);

			// Update Agent
			AgentDao agent = new AgentDao();
			agent = agentRepository.findByAgentId(meetingroom.getAgentId());
			agent.setMeetingroomId(meetingroom.getId());
			agentRepository.updateAgentMeetingRoomId(agent);
			LOGGER.debug("Agent has set in table for agent id #: " + meetingroom.getAgentId());
			
			return meetingroom;
			
		} catch (DataIntegrityViolationException e) {
			LOGGER.debug("MeetingRoomManager.save : Meeting Room already exists", e);
			LOGGER.error("MeetingRoomManager.save : Meeting Room already exists");
			throw new DataAlreadyExistsException("MeetingRoomManager.save : Meeting Room already exists");
		}
	}

	@Override
	public MeetingRoomDao update(MeetingRoomDao meetingroomDao) throws DataNotExistsException {
		try {
			// Update former Agent
			AgentDao formerAgent = new AgentDao();
			formerAgent = agentRepository.findByMeetingRoomId(meetingroomDao.getId());
			formerAgent.setMeetingroomId(0L);
			agentRepository.updateAgentMeetingRoomId(formerAgent);
			LOGGER.debug("Agent has set in table for agent id #: " + meetingroomDao.getAgentId());
			
			// Update new Agent
			AgentDao agent = new AgentDao();
			agent = agentRepository.findByAgentId(meetingroomDao.getAgentId());
			agent.setMeetingroomId(meetingroomDao.getId());
			agentRepository.updateAgentMeetingRoomId(agent);
			LOGGER.debug("Agent has set in table for agent id #: " + meetingroomDao.getAgentId());
						
			// Update MeetingRoomDao
			return meetingroomRepository.updateMeetingRoom(meetingroomDao);
		} catch (RuntimeException e) {
			LOGGER.debug("MeetingRoomManager.update : Meeting Room to update not found", e);
			LOGGER.error("MeetingRoomManager.update : Meeting Room to update not found");
			throw new DataNotExistsException("MeetingRoomManager.update : Meeting Room to update not found");
		}
	}


	@Override
	public MeetingRoomDao updateStatus(MeetingRoomDao meetingroomDao) throws DataNotExistsException, RoomAlreadyUsedException {
		try {

			LOGGER.debug("Meeting Room id is : " + meetingroomDao.getId());
			if  (meetingroomDao.getStatus().equals(E_MeetingRoomStatus.FREE.toString())) { // from UserUi.RoomEndpoint.cancelRoom
				MeetingRoomDao foundMeetingRoom = meetingroomRepository.findByMeetingRoomId(meetingroomDao.getId());
				if (foundMeetingRoom.getStatus().equals(E_MeetingRoomStatus.OCCUPIED.toString())) { // cancel from "J'ai fini " room is in OCCUPIED status
					LOGGER.debug("RoomStat to update !!!");
					RoomStatDao roomStat = new RoomStatDao();
					roomStat.setRoomId(meetingroomDao.getId().intValue());
					roomStatRepository.updateEndOccupancyDate(roomStat);
					LOGGER.info("roomStat updateEndOccupancyDate for room#" + foundMeetingRoom.getName() + " which status is : " + foundMeetingRoom.getStatus());
					meetingroomDao.setOrganizerLabel(null);
				}
			}
			// update MeetingRoomDao => status & organizer label
			
			return meetingroomRepository.updateMeetingRoomStatus(meetingroomDao);
		} catch (RuntimeException e) {
			LOGGER.debug("MeetingRoomManager.updateStatus : Meeting Room to update Status not found", e);
			LOGGER.error("MeetingRoomManager.updateStatus : Meeting Room to update Status not found");
			throw new DataNotExistsException("MeetingRoomManager.updateStatus : Meeting Room to update Status not found");
		}
	}

	@Override
	public void delete(long id) throws DataNotExistsException, IntegrityViolationException {

		try {
			// To generate exception if wrong id
			MeetingRoomDao meetingroom = meetingroomRepository.findOne(id);
			
			if (meetingroom.getOrganizerLabel() == null){
				// Set meeting room id to 0 for associated Agent 
				AgentDao agent = new AgentDao();
				agent = agentRepository.findByAgentId(meetingroom.getAgentId());
				agent.setMeetingroomId(0L);
				agentRepository.updateAgentMeetingRoomId(agent);
				LOGGER.info("Meeting room id set to 0 for agent id #: " + meetingroom.getAgentId());
				
				// Delete the room & associated stats (room_stats, room_daily_occupancy)
				meetingroomRepository.delete(id);
				roomStatRepository.deleteByRoomId(meetingroom.getId());
				roomDailyRepository.deleteByRoomId(meetingroom.getId());
			} else {
				LOGGER.error("MeetingRoomManager.delete : Meeting Room #" + id + " is occupied");
				throw new IntegrityViolationException("MeetingRoomManager.delete : Meeting Room #" + id + " is occupied");
			}
		} catch (IncorrectResultSizeDataAccessException e) {
			LOGGER.debug("MeetingRoomManager.delete : Meeting Room #" + id + " not found", e);
			LOGGER.error("MeetingRoomManager.delete : Meeting Room #" + id + " not found");
			throw new DataNotExistsException("MeetingRoomManager.delete : Meeting Room #" + id + " not found");
		}
	}

	/**
	 * @param name
	 * 
	 * @return MeetingRoomDao object if found
	 */
	@Override
	@Transactional(readOnly=true)
	public MeetingRoomDao findByName(String name) throws DataNotExistsException {
		try {
			return meetingroomRepository.findByName(name);
		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.debug("RoomManager.findByName : room#" + name + " is not found", e);
			LOGGER.error("RoomManager.findByName : room#" + name + " is not found");
			throw new DataNotExistsException("RoomManager.findByName : Room by name #" + name + " is not found");
		}
	}


	/*@Override
	@Transactional(readOnly=true)
	public List<MeetingRoomDao> findLatestReservedRoomsByUserId(String userId) {
		List<MeetingRoomDao> dataList = null;
		/*
		ConfigurationDao lastReservedCount = configRepository.findByKey(E_ConfigurationKey.LAST_RESERVED_COUNT.toString());
		int lastReservedCountValue = Integer.valueOf(lastReservedCount.getValue());
		int countAddedRoomStats = 0;
		
		try {
		// get reserved roomStats by userId order by reservation_date desc
		List<RoomStatDao> roomStatsFromDB = roomStatRepository.findLatestReservedRoomsByUserId(Long.valueOf(userId));
		
		// get latest reserved roomStats by userId
		List<RoomStatDao> roomStats = removeDuplicateFromList(roomStatsFromDB);
		
			if ((roomStats != null) && (!roomStats.isEmpty())) {
				dataList = new ArrayList<MeetingRoomDao>();
				for (RoomStatDao roomStatDao : roomStats) {
					MeetingRoomDao roomDao = meetingroomRepository.findByRoomId(Long.valueOf(roomStatDao.getRoomId()));
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
	}*/
	
	@Override
	@Transactional(readOnly=true)
	public Long countMeetingRoomsByType(String type) {
		return meetingroomRepository.countMeetingRoomsByType(type);
	}
	
	@Override
	public MeetingRoomDao findByMeetingRoomId(Long meetingroomId) throws DataNotExistsException {
		try {
			return meetingroomRepository.findByMeetingRoomId(meetingroomId);
		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.debug("RoomManager.findByRoomId : Meeting Room by Id #" + meetingroomId + " is not found", e);
			LOGGER.error("RoomManager.findByRoomId : Meeting Room by Id #" + meetingroomId + " is not found");
			throw new DataNotExistsException("RoomManager.findByRoomId : Meeting Room by Id #" + meetingroomId + " is not found");
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

	@Override
	public List<MeetingRoomDao> findLatestReservedMeetingRoomsByUserId(String userId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
