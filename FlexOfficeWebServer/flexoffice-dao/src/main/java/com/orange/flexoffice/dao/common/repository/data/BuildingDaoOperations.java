package com.orange.flexoffice.dao.common.repository.data;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import com.orange.flexoffice.dao.common.model.data.BuildingDao;
import com.orange.flexoffice.dao.common.model.object.BuildingDto;
import com.orange.flexoffice.dao.common.model.object.BuildingSummaryDto;

/**
 * BuildingDaoOperations
 * @author oab
 *
 */
public interface BuildingDaoOperations {
	
	List<BuildingSummaryDto> findAllBuildingsSummary();
	
	List<BuildingDao> findBuildingsHaveRoomsByCityId(long cityId);
	
	List<BuildingDao> findByCityId(Long cityId) throws IncorrectResultSizeDataAccessException;
	
	BuildingDto findByBuildingId(Long buildingId) throws IncorrectResultSizeDataAccessException;
	
	BuildingDao saveBuilding(BuildingDao data) throws DataIntegrityViolationException;
	
	BuildingDao updateBuilding(BuildingDao data) throws DataAccessException; 
	
}
