package com.orange.flexoffice.business.common.service.data.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.business.common.service.data.BuildingManager;
import com.orange.flexoffice.dao.common.model.data.BuildingDao;
import com.orange.flexoffice.dao.common.model.data.CityDao;
import com.orange.flexoffice.dao.common.model.object.BuildingDto;
import com.orange.flexoffice.dao.common.model.object.BuildingSummaryDto;
import com.orange.flexoffice.dao.common.repository.data.jdbc.BuildingDaoRepository;

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

	@Override
	public List<BuildingSummaryDto> findAllBuildings() {
		return buildingRepository.findAllBuildingsSummary();
	}

	@Override
	public BuildingDto find(long buildingId) throws DataNotExistsException {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(long buildingId) throws DataNotExistsException, IntegrityViolationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BuildingSummaryDto findByName(String name) throws DataNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	
		
}
