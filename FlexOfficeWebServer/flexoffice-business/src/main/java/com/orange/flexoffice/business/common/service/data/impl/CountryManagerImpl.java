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
import com.orange.flexoffice.business.common.service.data.CountryManager;
import com.orange.flexoffice.dao.common.model.data.CountryDao;
import com.orange.flexoffice.dao.common.repository.data.jdbc.CountryDaoRepository;

/**
 * Manages {@link CountryDao}.
 * For PROD LOG LEVEL is info then we say info & error logs.
 * 
 * @author oab
 */
@Service("CountryManager")
@Transactional
public class CountryManagerImpl implements CountryManager {

	private static final Logger LOGGER = Logger.getLogger(CountryManagerImpl.class);

	@Autowired
	private CountryDaoRepository countryRepository;

	@Override
	public List<CountryDao> findAllCountries() {
		return countryRepository.findAllCountries();
	}

	@Override
	public CountryDao find(long countryId) throws DataNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CountryDao save(CountryDao countryDao) throws DataAlreadyExistsException {
		try {
		// save CountryDao
		CountryDao country = countryRepository.saveCountry(countryDao);
		return country;
		} catch (DataIntegrityViolationException e) {
			LOGGER.debug("CountryManager.save : Country already exists", e);
			LOGGER.error("CountryManager.save : Country already exists");
			throw new DataAlreadyExistsException("CountryManager.save : Country already exists");
		}
	}

	@Override
	public CountryDao update(CountryDao CountryDao) throws DataNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(long countryId) throws DataNotExistsException, IntegrityViolationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CountryDao findByName(String name) throws DataNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}
	
	

		
}
