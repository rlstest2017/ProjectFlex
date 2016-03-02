package com.orange.flexoffice.dao.common.repository.data.jdbc;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.common.model.data.BuildingDao;
import com.orange.flexoffice.dao.common.repository.data.BuildingDaoOperations;

@Repository
public class BuildingDaoRepository extends DataRepository<BuildingDao> implements BuildingDaoOperations {

	
	public BuildingDaoRepository() {
		super(BuildingDao.class);
	}

	@Override
	public List<BuildingDao> findAllBuildings() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public List<BuildingDao> findByCityId(Long cityId) throws IncorrectResultSizeDataAccessException {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public BuildingDao findByBuildingId(Long buildingId) throws IncorrectResultSizeDataAccessException {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public BuildingDao findByName(String name) throws IncorrectResultSizeDataAccessException {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public BuildingDao saveBuilding(BuildingDao data) throws DataIntegrityViolationException {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public BuildingDao updateBuilding(BuildingDao data) throws DataAccessException {
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
