package com.orange.flexoffice.business.common.service.data.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.business.common.service.data.BuildingManager;
import com.orange.flexoffice.dao.common.model.data.BuildingDao;
import com.orange.flexoffice.dao.common.repository.data.jdbc.BuildingDaoRepository;

/**
 * Manages {@link BuildingDao}.
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
	public List<BuildingDao> findAllBuildings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BuildingDao find(long buildingId) throws DataNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BuildingDao save(BuildingDao building) throws DataAlreadyExistsException {
		// TODO Auto-generated method stub
		return null;
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
	public BuildingDao findByName(String name) throws DataNotExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	
		
}
