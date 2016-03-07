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

	@Override
	public List<CitySummaryDto> findAllCities() {
		return cityRepository.findAllCitiesSummary();
	}

	@Override
	public CityDto find(long cityId) throws DataNotExistsException {
		try {
			CityDto city = cityRepository.findByCityId(cityId);
			return city;
			} catch(IncorrectResultSizeDataAccessException e ) {
				LOGGER.debug("CityManager.find : Country by id #" + cityId + " is not found", e);
				LOGGER.error("CityManager.find : Country by id #" + cityId + " is not found");
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
		// TODO delete only if there is no buildings associated to the city !!!
		cityRepository.delete(cityId);		
	}

	@Override
	public CityDao findByName(String name) throws DataNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

		

		
}
