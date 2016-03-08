package com.orange.flexoffice.dao.common.repository.data;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import com.orange.flexoffice.dao.common.model.data.RegionDao;
import com.orange.flexoffice.dao.common.model.object.RegionDto;
import com.orange.flexoffice.dao.common.model.object.RegionSummaryDto;

/**
 * RegionDaoOperations
 * @author oab
 *
 */
public interface RegionDaoOperations {
	
	List<RegionSummaryDto> findAllRegionsSummary();
	
	RegionDto findByRegionId(Long regionId) throws IncorrectResultSizeDataAccessException;
	
	RegionDao saveRegion(RegionDao data) throws DataIntegrityViolationException;
	
	RegionDao updateRegion(RegionDao data) throws DataAccessException; 
	
}
