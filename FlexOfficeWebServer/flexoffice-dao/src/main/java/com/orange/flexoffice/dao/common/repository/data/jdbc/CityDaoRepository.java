package com.orange.flexoffice.dao.common.repository.data.jdbc;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.common.model.data.CityDao;
import com.orange.flexoffice.dao.common.repository.data.CityDaoOperations;

@Repository
public class CityDaoRepository extends DataRepository<CityDao> implements CityDaoOperations {

	
	public CityDaoRepository() {
		super(CityDao.class);
	}

	@Override
	public List<CityDao> findAllCities() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<CityDao> findByRegionId(Long regionId) throws IncorrectResultSizeDataAccessException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public CityDao findByCityId(Long cityId) throws IncorrectResultSizeDataAccessException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public CityDao findByName(String name) throws IncorrectResultSizeDataAccessException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public CityDao saveCity(CityDao data) throws DataIntegrityViolationException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public CityDao updateCity(CityDao data) throws DataAccessException {
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
