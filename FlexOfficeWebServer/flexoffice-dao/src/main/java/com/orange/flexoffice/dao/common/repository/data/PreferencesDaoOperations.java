package com.orange.flexoffice.dao.common.repository.data;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import com.orange.flexoffice.dao.common.model.data.PreferencesDao;

/**
 * PreferencesDaoOperations
 * @author oab
 *
 */
public interface PreferencesDaoOperations {
	
	PreferencesDao findByUserId(Long userId) throws IncorrectResultSizeDataAccessException;
	
	PreferencesDao savePreferences(PreferencesDao data) throws DataIntegrityViolationException;
	
	PreferencesDao updatePreferences(PreferencesDao data) throws DataAccessException; 
	
	void deleteByCountryId(long countryId);
	
	void deleteByRegionId(long regionId);
	
	void deleteByCityId(long cityId);
	
	void deleteByUserId(long userId);
	
	void deleteByBuildingId(long buildingId);
	
}
