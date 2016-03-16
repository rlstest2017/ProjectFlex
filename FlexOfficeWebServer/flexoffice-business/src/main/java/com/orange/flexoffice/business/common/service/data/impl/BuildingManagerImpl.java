package com.orange.flexoffice.business.common.service.data.impl;

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
import com.orange.flexoffice.business.common.service.data.BuildingManager;
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

	@Autowired
	private BuildingDaoRepository buildingRepository;
	@Autowired
	private PreferencesDaoRepository preferenceRepository;

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
	public BuildingDao save(BuildingDao buildingDao) throws DataAlreadyExistsException {
		try {
			// save BuildingDao
			BuildingDao building = buildingRepository.saveBuilding(buildingDao);
			return building;
			} catch (DataIntegrityViolationException e) {
				LOGGER.debug("BuildingManager.save : Building already exists", e);
				LOGGER.error("BuildingManager.save : Building already exists");
				throw new DataAlreadyExistsException("BuildingManager.save : Building already exists");
			}
	}

	@Override
	public BuildingDao update(BuildingDao building) throws DataNotExistsException {
		return buildingRepository.updateBuilding(building);
	}

	@Override
	public void delete(long buildingId) throws DataNotExistsException, IntegrityViolationException {
		try {
			buildingRepository.findOne(buildingId);
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
