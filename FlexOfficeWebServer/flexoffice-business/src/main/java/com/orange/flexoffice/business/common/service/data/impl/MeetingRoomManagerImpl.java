package com.orange.flexoffice.business.common.service.data.impl;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.business.common.exception.RoomAlreadyUsedException;
import com.orange.flexoffice.business.common.service.data.BuildingManager;
import com.orange.flexoffice.business.common.service.data.MeetingRoomManager;
import com.orange.flexoffice.business.meetingroom.config.FileManager;
import com.orange.flexoffice.dao.common.model.data.AgentDao;
import com.orange.flexoffice.dao.common.model.data.ConfigurationDao;
import com.orange.flexoffice.dao.common.model.data.MeetingRoomDao;
import com.orange.flexoffice.dao.common.model.data.MeetingRoomStatDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_ConfigurationKey;
import com.orange.flexoffice.dao.common.model.enumeration.E_MeetingRoomInfo;
import com.orange.flexoffice.dao.common.model.enumeration.E_MeetingRoomStatus;
import com.orange.flexoffice.dao.common.model.enumeration.E_MeetingRoomType;
import com.orange.flexoffice.dao.common.model.object.BuildingDto;
import com.orange.flexoffice.dao.common.model.object.MeetingRoomBuildingInfosDto;
import com.orange.flexoffice.dao.common.model.object.MeetingRoomDto;
import com.orange.flexoffice.dao.common.repository.data.jdbc.AgentDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.ConfigurationDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.MeetingRoomDailyOccupancyDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.MeetingRoomDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.MeetingRoomStatDaoRepository;

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
	
	private Properties properties;
	
	@Autowired
    @Qualifier("appProperties")
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

	@Autowired
	private MeetingRoomDaoRepository meetingroomRepository;
	@Autowired
	private AgentDaoRepository agentRepository;
	@Autowired
	private MeetingRoomStatDaoRepository meetingRoomStatRepository;
	@Autowired
	private MeetingRoomDailyOccupancyDaoRepository meetingRoomDailyRepository;
	@Autowired
	private ConfigurationDaoRepository configurationRepository;
	@Autowired
	private BuildingManager buildingManager;
	@Autowired
	private FileManager fileManager;

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
			
			// Update xml meeting room file config
			String meetingroomActivated = properties.getProperty("meetingroom.activated");
			if (Boolean.TRUE.toString().equalsIgnoreCase(meetingroomActivated)){
				fileManager.addObjectToFile(meetingroomDao.getBuildingId() + "_" + meetingroomDao.getFloor(), meetingroomDao.getExternalId());
			}
			
			return meetingroom;
			
		} catch (DataIntegrityViolationException e) {
			LOGGER.debug("MeetingRoomManager.save : Meeting Room already exists", e);
			LOGGER.error("MeetingRoomManager.save : Meeting Room already exists");
			throw new DataAlreadyExistsException("MeetingRoomManager.save : Meeting Room already exists");
		} catch (IOException | JAXBException | SAXException | ParserConfigurationException e) {
			LOGGER.debug("MeetingRoomManager.save : Meeting Room xml meeting room file in error", e);
			LOGGER.error("MeetingRoomManager.save : Meeting Room xml meeting room file in error");
			throw new RuntimeException("MeetingRoomManager.save : Meeting Room xml meeting room file in error");
		} catch (NumberFormatException e) {
			LOGGER.debug("MeetingRoomManager.save : Error while retieving location from building id" +  meetingroomDao.getBuildingId(), e);
			LOGGER.error("MeetingRoomManager.save : Error while retieving location from building id" +  meetingroomDao.getBuildingId());
			throw new DataAlreadyExistsException("MeetingRoomManager.save : Error while retieving location from building id");
		}
	}

	@Override
	public MeetingRoomDao update(MeetingRoomDao meetingroomDao) throws DataNotExistsException, DataAlreadyExistsException {
		MeetingRoomDao oldMeetingRoomDao = meetingroomRepository.findOne(meetingroomDao.getId()); 
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
			MeetingRoomDao updatedDao = meetingroomRepository.updateMeetingRoom(meetingroomDao);
			
			// Update xml meeting room file config
			String meetingroomActivated = properties.getProperty("meetingroom.activated");
			if (Boolean.TRUE.toString().equalsIgnoreCase(meetingroomActivated)){
				
					if (oldMeetingRoomDao.getBuildingId() == meetingroomDao.getBuildingId() && oldMeetingRoomDao.getFloor() == meetingroomDao.getFloor() && !oldMeetingRoomDao.getExternalId().equals(meetingroomDao.getExternalId())){
						fileManager.updateObjectFromFile(meetingroomDao.getBuildingId() + "_" + meetingroomDao.getFloor(), oldMeetingRoomDao.getExternalId(), meetingroomDao.getExternalId());
					} else {
						fileManager.removeObjectFromFile(oldMeetingRoomDao.getBuildingId() + "_" + oldMeetingRoomDao.getFloor(), oldMeetingRoomDao.getExternalId());
						fileManager.addObjectToFile(meetingroomDao.getBuildingId() + "_" + meetingroomDao.getFloor(), meetingroomDao.getExternalId());
					}
				
			}
			
			// Update MeetingRoomDao
			return updatedDao;
		} catch (RuntimeException e) {
			LOGGER.debug("MeetingRoomManager.update : Meeting Room to update not found", e);
			LOGGER.error("MeetingRoomManager.update : Meeting Room to update not found");
			throw new DataNotExistsException("MeetingRoomManager.update : Meeting Room to update not found");
		} catch (IOException | JAXBException | SAXException | ParserConfigurationException e) {
			String meetingroomActivated = properties.getProperty("meetingroom.activated");
			if (Boolean.TRUE.toString().equalsIgnoreCase(meetingroomActivated)){
				try {
					if (oldMeetingRoomDao.getBuildingId() == meetingroomDao.getBuildingId() && oldMeetingRoomDao.getFloor() == meetingroomDao.getFloor()){
						fileManager.updateObjectFromFile(meetingroomDao.getBuildingId() + "_" + meetingroomDao.getFloor(), meetingroomDao.getExternalId(), oldMeetingRoomDao.getExternalId());
					} else {
						fileManager.addObjectToFile(oldMeetingRoomDao.getBuildingId() + "_" + oldMeetingRoomDao.getFloor(), oldMeetingRoomDao.getExternalId());
						fileManager.removeObjectFromFile(meetingroomDao.getBuildingId() + "_" + meetingroomDao.getFloor(), meetingroomDao.getExternalId());
					}
				} catch (IOException | JAXBException | ParserConfigurationException | SAXException e1) {
					LOGGER.debug("MeetingRoomManager.update : Meeting Room xml meeting room file in error", e);
					LOGGER.error("MeetingRoomManager.update : Meeting Room xml meeting room file in error");
					throw new RuntimeException("MeetingRoomManager.update : Meeting Room xml meeting room file in error");
				}
			}
			LOGGER.debug("MeetingRoomManager.update : Meeting Room xml meeting room file in error", e);
			LOGGER.error("MeetingRoomManager.update : Meeting Room xml meeting room file in error");
			throw new RuntimeException("MeetingRoomManager.update : Meeting Room xml meeting room file in error");
		}
	}


	@Override
	public MeetingRoomDao updateStatus(MeetingRoomDao meetingroomDao) throws DataNotExistsException, RoomAlreadyUsedException {
		try {

			LOGGER.debug("Meeting Room id is : " + meetingroomDao.getId());
			// Use in case of reservation meeting Room in agent/dashboard
			if  (meetingroomDao.getStatus().equals(E_MeetingRoomStatus.OCCUPIED.toString())) { 
				MeetingRoomDao foundMeetingRoom = meetingroomRepository.findByMeetingRoomId(meetingroomDao.getId());
				
				LOGGER.info("foundMeetingRoomStatus is " + foundMeetingRoom.getStatus() + " for meeting room#" + foundMeetingRoom.getName());
				
				if (!foundMeetingRoom.getStatus().equals(E_MeetingRoomStatus.FREE.toString())) {
					LOGGER.error("Meeting Room status is not FREE !!!");
					throw new RoomAlreadyUsedException("MeetingRoomManager.updateStatus : meeting Room is not in FREE status");
				} else {
					LOGGER.debug("MeetingRoomStat to create !!!");
					MeetingRoomStatDao meetingRoomStat = new MeetingRoomStatDao();
					meetingRoomStat.setMeetingroomId(meetingroomDao.getId().intValue());
					meetingRoomStat.setMeetingRoomInfo(E_MeetingRoomInfo.OCCUPIED.toString());
					meetingRoomStatRepository.saveOccupiedMeetingRoomStat(meetingRoomStat);
					LOGGER.info("meetingRoomStat created for meeting room#" + foundMeetingRoom.getName() + " which status is : " + foundMeetingRoom.getStatus());
				}
			} else if (meetingroomDao.getStatus().equals(E_MeetingRoomStatus.FREE.toString())) { 
				MeetingRoomDao foundMeetingRoom = meetingroomRepository.findByMeetingRoomId(meetingroomDao.getId());
				if (foundMeetingRoom.getStatus().equals(E_MeetingRoomStatus.OCCUPIED.toString())) { 
					LOGGER.debug("MeetingRoomStat to update !!!");
					MeetingRoomStatDao meetingRoomStat = new MeetingRoomStatDao();
					meetingRoomStat.setMeetingroomId(meetingroomDao.getId().intValue());
					meetingRoomStatRepository.updateEndOccupancyDate(meetingRoomStat);
					LOGGER.info("meetingRoomStat updateEndOccupancyDate for meeting room#" + foundMeetingRoom.getName() + " which status is : " + foundMeetingRoom.getStatus());
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
	public MeetingRoomDao updateData(MeetingRoomDao meetingroomDao) throws DataNotExistsException {
		try {
			MeetingRoomDao foundMeetingRoom = meetingroomRepository.findByExternalId(meetingroomDao.getExternalId());
			meetingroomDao.setId(foundMeetingRoom.getId());

			LOGGER.debug("Meeting Room id is : " + meetingroomDao.getId() + " status received is " + meetingroomDao.getStatus());
			// Use in case of reservation meeting Room in agent/dashboard
			if  (meetingroomDao.getStatus().equals(E_MeetingRoomStatus.OCCUPIED.toString())) { 
				LOGGER.info("foundMeetingRoomStatus is " + foundMeetingRoom.getStatus() + " for meeting room#" + foundMeetingRoom.getName());
				
				if (foundMeetingRoom.getStatus().equals(E_MeetingRoomStatus.OCCUPIED.toString())) {
					LOGGER.error("Meeting Room status is not FREE !!!");
				} else {
					LOGGER.debug("MeetingRoomStat to create !!!");
					MeetingRoomStatDao meetingRoomStat = new MeetingRoomStatDao();
					meetingRoomStat.setMeetingroomId(meetingroomDao.getId().intValue());
					meetingRoomStat.setMeetingRoomInfo(E_MeetingRoomInfo.OCCUPIED.toString());
					meetingRoomStat.setBeginOccupancyDate(meetingroomDao.getStartDate());
					meetingRoomStatRepository.saveOccupiedMeetingRoomStat(meetingRoomStat);
					LOGGER.info("meetingRoomStat created for meeting room#" + foundMeetingRoom.getName() + " which status is : " + foundMeetingRoom.getStatus());
				}
			} else if (meetingroomDao.getStatus().equals(E_MeetingRoomStatus.FREE.toString()) || meetingroomDao.getStatus().equals(E_MeetingRoomStatus.UNKNOWN.toString()) || meetingroomDao.getStatus().equals(E_MeetingRoomStatus.ACK.toString())) { 
				if (foundMeetingRoom.getStatus().equals(E_MeetingRoomStatus.OCCUPIED.toString())) { 
					LOGGER.debug("MeetingRoomStat to update !!!");
					MeetingRoomStatDao meetingRoomStat = new MeetingRoomStatDao();
					meetingRoomStat.setMeetingroomId(meetingroomDao.getId().intValue());
					meetingRoomStat.setEndOccupancyDate(meetingroomDao.getEndDate());
					meetingRoomStatRepository.updateEndOccupancyDate(meetingRoomStat);
					LOGGER.info("meetingRoomStat updateEndOccupancyDate for meeting room#" + foundMeetingRoom.getName() + " which status is : " + foundMeetingRoom.getStatus());
				} else if (foundMeetingRoom.getStatus().equals(E_MeetingRoomStatus.ACK.toString())){
					Date now = new Date();
					Calendar cal = Calendar.getInstance();
					cal.setTime(now);
					cal.add(Calendar.SECOND, -45);
					Date modifiedTime = cal.getTime();
					// pour compenser le manque de réactivité du php le premier status free reçu après un ack est ignoré -> lorsque l'utilisateur confirme la réservation on reçoit un free avant occupied -> à ignorer
					if (meetingroomDao.getStatus().equals(E_MeetingRoomStatus.FREE.toString()) && foundMeetingRoom.getLastMeasureDate().after(modifiedTime)){
						return meetingroomDao;
					}
				} 
				
				if (!meetingroomDao.getStatus().equals(E_MeetingRoomStatus.ACK.toString())){
					meetingroomDao.setOrganizerLabel(null);
					meetingroomDao.setStartDate(null);
					meetingroomDao.setEndDate(null);
				}
			} 
			
			// update MeetingRoomDao => status & organizer label & Start & End date
			return meetingroomRepository.updateMeetingRoomData(meetingroomDao);
		} catch (RuntimeException e) {
			LOGGER.debug("MeetingRoomManager.updateStatus : Meeting Room to update Status not found", e);
			LOGGER.error("MeetingRoomManager.updateStatus : Meeting Room to update Status not found");
			throw new DataNotExistsException("MeetingRoomManager.updateStatus : Meeting Room to update Status not found");
		}
	}
	
	@Override
	public List<MeetingRoomDao> getTimeout() {
		ConfigurationDao intervalTimeout = configurationRepository.findByKey(E_ConfigurationKey.MEETINGROOM_STATUS_TIMEOUT.toString());
		List<MeetingRoomDao> listMeetingRooms = meetingroomRepository.findMeetingRoomsInTimeout(intervalTimeout.getValue()); 
		
		return listMeetingRooms;
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
				
				// Delete the room & associated stats (meetingroom_stats, meetingroom_daily_occupancy)
				meetingroomRepository.delete(id);
				meetingRoomStatRepository.deleteByMeetingRoomId(meetingroom.getId());
				meetingRoomDailyRepository.deleteByMeetingRoomId(meetingroom.getId());
				
				// Update xml meeting room file config
				String meetingroomActivated = properties.getProperty("meetingroom.activated");
				if (Boolean.TRUE.toString().equalsIgnoreCase(meetingroomActivated)){
					fileManager.removeObjectFromFile(meetingroom.getBuildingId() + "_" + meetingroom.getFloor(), meetingroom.getExternalId());
				}
			} else {
				LOGGER.error("MeetingRoomManager.delete : Meeting Room #" + id + " is occupied");
				throw new IntegrityViolationException("MeetingRoomManager.delete : Meeting Room #" + id + " is occupied");
			}
		} catch (IncorrectResultSizeDataAccessException e) {
			LOGGER.debug("MeetingRoomManager.delete : Meeting Room #" + id + " not found", e);
			LOGGER.error("MeetingRoomManager.delete : Meeting Room #" + id + " not found");
			throw new DataNotExistsException("MeetingRoomManager.delete : Meeting Room #" + id + " not found");
		} catch (IOException | JAXBException | SAXException | ParserConfigurationException e) {
			LOGGER.debug("MeetingRoomManager.delete : Meeting Room #" + id + " xml meeting room file in error", e);
			LOGGER.error("MeetingRoomManager.delete : Meeting Room #" + id + " xml meeting room file in error");
			throw new RuntimeException("MeetingRoomManager.delete : Meeting Room #" + id + " xml meeting room file in error");
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
