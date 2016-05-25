package com.orange.flexoffice.business.common.service.data.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import com.orange.flexoffice.business.common.exception.InvalidParametersException;
import com.orange.flexoffice.business.common.service.data.BuildingManager;
import com.orange.flexoffice.business.meetingroom.config.FileManager;
import com.orange.flexoffice.dao.common.model.data.BuildingDao;
import com.orange.flexoffice.dao.common.model.data.MeetingRoomDao;
import com.orange.flexoffice.dao.common.model.data.MeetingRoomGroupsConfigurationDao;
import com.orange.flexoffice.dao.common.model.data.RoomDao;
import com.orange.flexoffice.dao.common.model.object.BuildingDto;
import com.orange.flexoffice.dao.common.model.object.BuildingSummaryDto;
import com.orange.flexoffice.dao.common.repository.data.jdbc.BuildingDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.MeetingRoomDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.MeetingRoomGroupsConfigurationDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.PreferencesDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RoomDaoRepository;

/**
 * Manages {@link BuildingSummaryDto}.
 * For PROD LOG LEVEL is info then we say info & error logs.
 * 
 * @author oab
 */
@Service("BuildingManager")
@Transactional
public class BuildingManagerImpl implements BuildingManager {

	private static final Logger LOGGER = Logger.getLogger(BuildingManagerImpl.class);
	
	private Properties properties;
	
	@Autowired
    @Qualifier("appProperties")
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

	@Autowired
	private BuildingDaoRepository buildingRepository;
	@Autowired
	private MeetingRoomGroupsConfigurationDaoRepository meetingRoomGroupsConfigurationRepository;
	@Autowired
	private PreferencesDaoRepository preferenceRepository;
	@Autowired
	private RoomDaoRepository roomRepository;
	@Autowired
	private MeetingRoomDaoRepository meetingRoomRepository;
	@Autowired
	private FileManager fileManager;

	@Override
	public List<BuildingSummaryDto> findAllBuildings() {
		return buildingRepository.findAllBuildingsSummary();
	}

	@Override
	public List<BuildingDao> findBuildingsByCityId(String cityId, boolean isFromAdminUI) {
		if (isFromAdminUI) {
			return buildingRepository.findByCityId(Long.valueOf(cityId));
		} else {
			// get only cities have rooms
			return buildingRepository.findBuildingsHaveRoomsByCityId(Long.valueOf(cityId));
		}
	}

	@Override
	public BuildingDto find(long buildingId) throws DataNotExistsException {
		try {
			BuildingDto building = buildingRepository.findByBuildingId(buildingId);
			return building;
			} catch(IncorrectResultSizeDataAccessException e ) {
				LOGGER.debug("BuildingManager.find : Building by id #" + buildingId + " is not found", e);
				LOGGER.error("BuildingManager.find : Building by id #" + buildingId + " is not found");
				throw new DataNotExistsException("BuildingManager.find : Building by id #" + buildingId + " is not found");
			}
	}

	@Override
	public BuildingDao save(BuildingDao buildingDao) throws DataAlreadyExistsException, UnsupportedEncodingException, FileNotFoundException, DataNotExistsException {
		try {
			// save BuildingDao
			BuildingDao building = buildingRepository.saveBuilding(buildingDao);
			
			// if meetingroom activated 
			String meetingroomActivated = properties.getProperty("meetingroom.activated");
			
			if (Boolean.TRUE.toString().equalsIgnoreCase(meetingroomActivated)){
				MeetingRoomGroupsConfigurationDao meetingRoomGroupsConfiguration;
				
				for(int i = 0; i < buildingDao.getNbFloors(); i ++){
					String fileName = buildingDao.getId() + "_" + i;
					
					meetingRoomGroupsConfiguration = new MeetingRoomGroupsConfigurationDao();
					meetingRoomGroupsConfiguration.setBuildingId(building.getId());
					meetingRoomGroupsConfiguration.setFloor(Long.valueOf(i));
					meetingRoomGroupsConfiguration.setMeetingroomGroupId(fileName);
					
					// Add entry in meetingroom_groups_configuration
					meetingRoomGroupsConfigurationRepository.saveMeetingRoomGroupsConfiguration(meetingRoomGroupsConfiguration);
					
					// create xml file
					fileManager.createFile(fileName);
				}
			}
			
			return building;
			
		} catch (DataIntegrityViolationException e) {
			LOGGER.debug("BuildingManager.save : Building already exists", e);
			LOGGER.error("BuildingManager.save : Building already exists");
			throw new DataAlreadyExistsException("BuildingManager.save : Building already exists"); 
		} catch (IOException | JAXBException e) {
			LOGGER.debug("ConfigurationEndpoint.addBuilding : Meeting Room xml meeting room file in error", e);
			LOGGER.error("ConfigurationEndpoint.addBuilding : Meeting Room xml meeting room file in error");
			throw new RuntimeException("ConfigurationEndpoint.addBuilding : Meeting Room xml meeting room file in error");
		}
	}

	@Override
	public BuildingDao update(BuildingDao building) throws DataNotExistsException, IOException, JAXBException, ParserConfigurationException, SAXException, InvalidParametersException {
		// If newNbFloors < oldNBFloors && room associated to deleted floors -> throws exception
		BuildingDao oldBuildingDao = buildingRepository.findOne(building.getId());
		if(building.getNbFloors() < oldBuildingDao.getNbFloors()){
			List<RoomDao> rooms = roomRepository.findRoomsByBuildingId(building.getId());
			for(RoomDao room :rooms){
				if (room.getFloor() > building.getNbFloors()){ 
					LOGGER.debug("ConfigurationEndpoint.update : Can not update buildding floor because rooms are on deleted floors");
					LOGGER.error("ConfigurationEndpoint.update : Can not update buildding floor because rooms are on deleted floors");
					throw new InvalidParametersException("ConfigurationEndpoint.update : Can not update buildding floor because rooms are on deleted floors");
				}
			}
			// If newNbFloors < oldNBFloors && meeting room associated to deleted floors -> throws exception
			List<MeetingRoomDao> meetingRooms = meetingRoomRepository.findMeetingRoomsByBuildingId(building.getId());
			for(MeetingRoomDao meetingRoom :meetingRooms){
				if (meetingRoom.getFloor() > building.getNbFloors()){ 
					LOGGER.debug("ConfigurationEndpoint.update : Can not update buildding floor because meeting meeting rooms are on deleted floors");
					LOGGER.error("ConfigurationEndpoint.update : Can not update buildding floor because meeting meeting rooms are on deleted floors");
					throw new InvalidParametersException("ConfigurationEndpoint.update : Can not update buildding floor because meeting rooms are on deleted floors");
				}
			}
		}
		
		BuildingDao buildingUpdated = buildingRepository.updateBuilding(building);
		
		// update xml meeting room config file
		String meetingroomActivated = properties.getProperty("meetingroom.activated");
		if (Boolean.TRUE.toString().equalsIgnoreCase(meetingroomActivated)){
			
			MeetingRoomGroupsConfigurationDao meetingRoomGroupsConfiguration;
			if (oldBuildingDao.getNbFloors() < building.getNbFloors()){
				for(int i = oldBuildingDao.getNbFloors().intValue(); i < building.getNbFloors(); i ++){
					String fileName = building.getId() + "_" + i;
					
					meetingRoomGroupsConfiguration = new MeetingRoomGroupsConfigurationDao();
					meetingRoomGroupsConfiguration.setBuildingId(building.getId());
					meetingRoomGroupsConfiguration.setFloor(Long.valueOf(i));
					meetingRoomGroupsConfiguration.setMeetingroomGroupId(fileName);
					
					// Add entry in meetingroom_groups_configuration
					meetingRoomGroupsConfigurationRepository.saveMeetingRoomGroupsConfiguration(meetingRoomGroupsConfiguration);
					
					fileManager.createFile(fileName);
				}
			} else if (oldBuildingDao.getNbFloors() > building.getNbFloors()){
				for(int i = oldBuildingDao.getNbFloors().intValue(); i >= building.getNbFloors(); i --){
					String fileName = oldBuildingDao.getId() + "_" + i;
					
					meetingRoomGroupsConfiguration = new MeetingRoomGroupsConfigurationDao();
					meetingRoomGroupsConfiguration.setBuildingId(building.getId());
					meetingRoomGroupsConfiguration.setFloor(Long.valueOf(i));
					
					// delete entry in meetingroom_groups_configuration
					meetingRoomGroupsConfigurationRepository.deleteByBuildingIdAndFloor(meetingRoomGroupsConfiguration);
					
					fileManager.deleteFile(fileName);
				}
			}
		}
		return buildingUpdated;
	}

	@Override
	public void delete(long buildingId) throws DataNotExistsException, IntegrityViolationException {
		try {
			BuildingDao buildingDao = buildingRepository.findOne(buildingId);
			
			preferenceRepository.deleteByBuildingId(buildingId);
			buildingRepository.delete(buildingId);
			
			// if meetingroom activated in flexOffice
			String meetingroomActivated = properties.getProperty("meetingroom.activated");
			
			// delete xml meeting room config file
			if (Boolean.TRUE.toString().equalsIgnoreCase(meetingroomActivated)){
				// delete entries in table meetingroom_groups_configuration for buildingId
				meetingRoomGroupsConfigurationRepository.deleteByBuildingId(buildingDao.getId());
				
				for(int i = 0; i < buildingDao.getNbFloors(); i ++){
					String fileName = buildingDao.getId() + "_" + i;
					fileManager.deleteFile(fileName);
				}
			}
			
		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.debug("Building by id " + buildingId + " is not found", e);
			LOGGER.error("Building by id " + buildingId + " is not found");
			throw new DataNotExistsException("Building not exist");
		} catch(DataIntegrityViolationException e ) {
			LOGGER.debug("BuildingManager.delete : Building associated to rooms", e);
			LOGGER.error("BuildingManager.delete : Building associated to rooms");
			throw new IntegrityViolationException("BuildingManager.delete : Building associated to rooms");
		}	
	}
		
}
