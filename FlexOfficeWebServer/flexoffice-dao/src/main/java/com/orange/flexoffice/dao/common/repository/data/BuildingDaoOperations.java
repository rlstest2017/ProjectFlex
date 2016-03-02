package com.orange.flexoffice.dao.common.repository.data;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import com.orange.flexoffice.dao.common.model.data.BuildingDao;

/**
 * BuildingDaoOperations
 * @author oab
 *
 */
public interface BuildingDaoOperations {
	
	List<BuildingDao> findAllBuildings();
	
	List<BuildingDao> findByCityId(Long cityId) throws IncorrectResultSizeDataAccessException;
	
	BuildingDao findByBuildingId(Long buildingId) throws IncorrectResultSizeDataAccessException;
	
	BuildingDao findByName(String name) throws IncorrectResultSizeDataAccessException;
	
	BuildingDao saveBuilding(BuildingDao data) throws DataIntegrityViolationException;
	
	BuildingDao updateBuilding(BuildingDao data) throws DataAccessException; 
	
}
