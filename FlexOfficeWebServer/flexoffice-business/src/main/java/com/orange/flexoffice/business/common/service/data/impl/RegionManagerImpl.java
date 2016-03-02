package com.orange.flexoffice.business.common.service.data.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.business.common.service.data.RegionManager;
import com.orange.flexoffice.dao.common.model.data.RegionDao;
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

	@Override
	public List<RegionDao> findAllRegions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RegionDao find(long regionId) throws DataNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RegionDao save(RegionDao RegionDao) throws DataAlreadyExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RegionDao update(RegionDao RegionDao) throws DataNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(long regionId) throws DataNotExistsException, IntegrityViolationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RegionDao findByName(String name) throws DataNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}
		

		
}
