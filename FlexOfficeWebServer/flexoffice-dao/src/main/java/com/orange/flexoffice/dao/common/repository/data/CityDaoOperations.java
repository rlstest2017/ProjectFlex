package com.orange.flexoffice.dao.common.repository.data;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import com.orange.flexoffice.dao.common.model.data.CityDao;
import com.orange.flexoffice.dao.common.model.object.CityDto;
import com.orange.flexoffice.dao.common.model.object.CitySummaryDto;

/**
 * CityDaoOperations
 * @author oab
 *
 */
public interface CityDaoOperations {
	
	List<CitySummaryDto> findAllCitiesSummary();
	
	List<CityDao> findCitiesHaveRoomsByRegionId(long regionId);
	
	List<CityDao> findCitiesHaveMeetingRoomsByRegionId(long regionId);
	
	List<CityDao> findByRegionId(Long regionId);
	
	CityDto findByCityId(Long cityId) throws IncorrectResultSizeDataAccessException;
	
	CityDao saveCity(CityDao data) throws DataIntegrityViolationException;
	
	CityDao updateCity(CityDao data) throws DataAccessException; 
	
}
