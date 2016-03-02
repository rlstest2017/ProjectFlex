package com.orange.flexoffice.business.common.service.data.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CityDto find(long cityId) throws DataNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CityDao save(CityDao city) throws DataAlreadyExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CityDao update(CityDao city) throws DataNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(long cityId) throws DataNotExistsException, IntegrityViolationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CityDao findByName(String name) throws DataNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

		

		
}
