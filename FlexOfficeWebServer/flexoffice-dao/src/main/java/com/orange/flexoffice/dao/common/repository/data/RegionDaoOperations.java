package com.orange.flexoffice.dao.common.repository.data;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import com.orange.flexoffice.dao.common.model.data.RegionDao;

/**
 * RegionDaoOperations
 * @author oab
 *
 */
public interface RegionDaoOperations {
	
	List<RegionDao> findAllRegions();
	
	List<RegionDao> findByCountryId(Long countryId) throws IncorrectResultSizeDataAccessException;
	
	RegionDao findByRegionId(Long regionId) throws IncorrectResultSizeDataAccessException;
	
	RegionDao findByName(String name) throws IncorrectResultSizeDataAccessException;
	
	RegionDao saveRegion(RegionDao data) throws DataIntegrityViolationException;
	
	RegionDao updateRegion(RegionDao data) throws DataAccessException; 
	
}
