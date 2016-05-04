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
import com.orange.flexoffice.business.common.service.data.BuildingManager;
import com.orange.flexoffice.business.common.utils.AddressTools;
import com.orange.flexoffice.business.meetingroom.config.FileManager;
import com.orange.flexoffice.dao.common.model.data.BuildingDao;
import com.orange.flexoffice.dao.common.model.object.BuildingDto;
import com.orange.flexoffice.dao.common.model.object.BuildingSummaryDto;
import com.orange.flexoffice.dao.common.repository.data.jdbc.BuildingDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.PreferencesDaoRepository;

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
	private PreferencesDaoRepository preferenceRepository;
	@Autowired
	private FileManager fileManager;
	@Autowired
	private AddressTools addressTools;

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
			// create xml meeting room config file
			String meetingroomActivated = properties.getProperty("meetingroom.activated");
			if ("true".equals(meetingroomActivated)){
				String fileName = addressTools.getCountryRegionCityNamesFromCityId(buildingDao.getCityId()) + "_" + buildingDao.getName();
				fileManager.createFile(fileName);
			}
			
			// save BuildingDao
			BuildingDao building = buildingRepository.saveBuilding(buildingDao);
			return building;
			
			} catch (DataIntegrityViolationException e) {
				LOGGER.debug("BuildingManager.save : Building already exists", e);
				LOGGER.error("BuildingManager.save : Building already exists");
				throw new DataAlreadyExistsException("BuildingManager.save : Building already exists"); 
			} catch (DataNotExistsException e) {
				LOGGER.debug("BuildingManager.save : Building already exists", e);
				LOGGER.error("BuildingManager.save : Building already exists");
				throw new DataNotExistsException("BuildingManager.save : Building already exists");
			} catch (IOException | JAXBException e) {
				LOGGER.debug("ConfigurationEndpoint.addBuilding : Meeting Room xml meeting room file in error", e);
				LOGGER.error("ConfigurationEndpoint.addBuilding : Meeting Room xml meeting room file in error");
				throw new RuntimeException("ConfigurationEndpoint.addBuilding : Meeting Room xml meeting room file in error");
			}
	}

	@Override
	public BuildingDao update(BuildingDao building) throws DataNotExistsException, IOException, JAXBException, ParserConfigurationException, SAXException {
		// update xml meeting room config file
		String meetingroomActivated = properties.getProperty("meetingroom.activated");
		if ("true".equals(meetingroomActivated)){
			BuildingDao oldBuildingDao = buildingRepository.findOne(building.getId());
			String fileSubName = addressTools.getCountryRegionCityNamesFromCityId(oldBuildingDao.getCityId());
			fileManager.updateFile(fileSubName + "_" + oldBuildingDao.getName(), fileSubName + "_" + building.getName());
		}
		return buildingRepository.updateBuilding(building);
	}

	@Override
	public void delete(long buildingId) throws DataNotExistsException, IntegrityViolationException {
		try {
			buildingRepository.findOne(buildingId);
			// delete xml meeting room config file
			String meetingroomActivated = properties.getProperty("meetingroom.activated");
			if ("true".equals(meetingroomActivated)){
				BuildingDao buildingDao = buildingRepository.findOne(buildingId);
				String fileName = addressTools.getCountryRegionCityNamesFromCityId(buildingDao.getCityId()) + "_" + buildingDao.getName();
				fileManager.deleteFile(fileName);
			}
			
			preferenceRepository.deleteByBuildingId(buildingId);
			buildingRepository.delete(buildingId);
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
