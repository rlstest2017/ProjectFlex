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
import com.orange.flexoffice.business.common.service.data.CityManager;
import com.orange.flexoffice.dao.common.model.data.CityDao;
import com.orange.flexoffice.dao.common.model.object.CityDto;
import com.orange.flexoffice.dao.common.model.object.CitySummaryDto;
import com.orange.flexoffice.dao.common.repository.data.jdbc.CityDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.PreferencesDaoRepository;

/**
 * Manages {@link CityDao}.
 * For PROD LOG LEVEL is info then we say info & error logs.
 * 
 * @author oab
 */
@Service("CityManager")
@Transactional
public class CityManagerImpl implements CityManager {

	private static final Logger LOGGER = Logger.getLogger(CityManagerImpl.class);

	@Autowired
	private CityDaoRepository cityRepository;
	@Autowired
	private PreferencesDaoRepository preferenceRepository;

	@Override
	public List<CitySummaryDto> findAllCities() {
		return cityRepository.findAllCitiesSummary();
	}

	@Override
	public List<CityDao> findCitiesByRegionId(String regionId, boolean isFromAdminUI) {
		if (isFromAdminUI) {
			return cityRepository.findByRegionId(Long.valueOf(regionId));
		} else {
			// get only cities have rooms
			return cityRepository.findCitiesHaveRoomsByRegionId(Long.valueOf(regionId));
		}
	}
	
	@Override
	public CityDto find(long cityId) throws DataNotExistsException {
		try {
			CityDto city = cityRepository.findByCityId(cityId);
			return city;
			} catch(IncorrectResultSizeDataAccessException e ) {
				LOGGER.debug("CityManager.find : City by id #" + cityId + " is not found", e);
				LOGGER.error("CityManager.find : City by id #" + cityId + " is not found");
				throw new DataNotExistsException("CityManager.find : City by id #" + cityId + " is not found");
			}
	}

	@Override
	public CityDao save(CityDao cityDao) throws DataAlreadyExistsException {
		try {
			// save CityDao
			CityDao city = cityRepository.saveCity(cityDao);
			return city;
			} catch (DataIntegrityViolationException e) {
				LOGGER.debug("CityManager.save : City already exists", e);
				LOGGER.error("CityManager.save : City already exists");
				throw new DataAlreadyExistsException("CityManager.save : City already exists");
			}
	}

	@Override
	public CityDao update(CityDao city) throws DataNotExistsException {
		return cityRepository.updateCity(city);
	}

	@Override
	public void delete(long cityId) throws DataNotExistsException, IntegrityViolationException {
		try {
			cityRepository.findOne(cityId);
			cityRepository.delete(cityId);
			preferenceRepository.deleteByCityId(cityId);
		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.debug("city by id " + cityId + " is not found", e);
			LOGGER.error("city by id " + cityId + " is not found");
			throw new DataNotExistsException("City not exist");
		} catch(DataIntegrityViolationException e ) {
			LOGGER.debug("CityManager.delete : City associated to buildings", e);
			LOGGER.error("CityManager.delete : City associated to buildings");
			throw new IntegrityViolationException("CityManager.delete : City associated to buildings");
		}	
	}
		
}
