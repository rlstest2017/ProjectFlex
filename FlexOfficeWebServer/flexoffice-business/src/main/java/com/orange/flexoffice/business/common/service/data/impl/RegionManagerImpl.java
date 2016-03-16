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
import com.orange.flexoffice.business.common.service.data.RegionManager;
import com.orange.flexoffice.dao.common.model.data.RegionDao;
import com.orange.flexoffice.dao.common.model.object.RegionDto;
import com.orange.flexoffice.dao.common.model.object.RegionSummaryDto;
import com.orange.flexoffice.dao.common.repository.data.jdbc.PreferencesDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RegionDaoRepository;

/**
 * Manages {@link RegionDao}.
 * For PROD LOG LEVEL is info then we say info & error logs.
 * 
 * @author oab
 */
@Service("RegionManager")
@Transactional
public class RegionManagerImpl implements RegionManager {

	private static final Logger LOGGER = Logger.getLogger(RegionManagerImpl.class);

	@Autowired
	private RegionDaoRepository regionRepository;
	@Autowired
	private PreferencesDaoRepository preferenceRepository;

	@Override
	public List<RegionSummaryDto> findAllRegions() {
		return regionRepository.findAllRegionsSummary();
	}

	@Override
	public RegionDto find(long regionId) throws DataNotExistsException {
		try {
			RegionDto region = regionRepository.findByRegionId(regionId);
			return region;
			} catch(IncorrectResultSizeDataAccessException e ) {
				LOGGER.debug("RegionManager.find : Region by id #" + regionId + " is not found", e);
				LOGGER.error("RegionManager.find : Region by id #" + regionId + " is not found");
				throw new DataNotExistsException("RegionManager.find : Region by id #" + regionId + " is not found");
			}
	}

	@Override
	public RegionDao save(RegionDao regionDao) throws DataAlreadyExistsException {
		try {
			// save RegionDao
			RegionDao region = regionRepository.saveRegion(regionDao);
			return region;
			} catch (DataIntegrityViolationException e) {
				LOGGER.debug("RegionManager.save : Region already exists", e);
				LOGGER.error("RegionManager.save : Region already exists");
				throw new DataAlreadyExistsException("RegionManager.save : Region already exists");
			}
	}

	@Override
	public RegionDao update(RegionDao regionDao) throws DataNotExistsException {
		return regionRepository.updateRegion(regionDao);
	}

	@Override
	public void delete(long regionId) throws DataNotExistsException, IntegrityViolationException {
		try {
			regionRepository.findOne(regionId);
			regionRepository.delete(regionId);
			preferenceRepository.deleteByRegionId(regionId);
		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.debug("region by id " + regionId + " is not found", e);
			LOGGER.error("region by id " + regionId + " is not found");
			throw new DataNotExistsException("Region not exist");
		} catch(DataIntegrityViolationException e ) {
			LOGGER.debug("RegionManager.delete : Region associated to cities", e);
			LOGGER.error("RegionManager.delete : Region associated to cities");
			throw new IntegrityViolationException("RegionManager.delete : Region associated to cities");
		}	
	}

	@Override
	public List<RegionDao> findRegionsByCountryId(String countryId, boolean isFromAdminUI) {
		if (isFromAdminUI) {
			return regionRepository.findByCountryId(Long.valueOf(countryId));
		} else {
			// get only regions have rooms
			return regionRepository.findRegionsHaveRoomsByCountryId(Long.valueOf(countryId));
		}
	}
		
}
