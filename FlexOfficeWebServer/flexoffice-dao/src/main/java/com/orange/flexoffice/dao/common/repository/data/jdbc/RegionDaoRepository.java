package com.orange.flexoffice.dao.common.repository.data.jdbc;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.common.model.data.RegionDao;
import com.orange.flexoffice.dao.common.repository.data.RegionDaoOperations;

@Repository
public class RegionDaoRepository extends DataRepository<RegionDao> implements RegionDaoOperations {

	
	public RegionDaoRepository() {
		super(RegionDao.class);
	}

	@Override
	public List<RegionDao> findAllRegions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RegionDao> findByCountryId(Long countryId) throws IncorrectResultSizeDataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RegionDao findByRegionId(Long regionId) throws IncorrectResultSizeDataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RegionDao findByName(String name) throws IncorrectResultSizeDataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RegionDao saveRegion(RegionDao data) throws DataIntegrityViolationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RegionDao updateRegion(RegionDao data) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getColumnColName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getColName() {
		// TODO Auto-generated method stub
		return null;
	}
		
	
}
