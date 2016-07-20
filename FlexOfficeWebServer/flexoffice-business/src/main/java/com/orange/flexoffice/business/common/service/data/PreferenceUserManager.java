package com.orange.flexoffice.business.common.service.data;

import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.dao.common.model.data.PreferencesDao;

/**
 * PreferenceUserManager
 * @author oab
 *
 */
public interface PreferenceUserManager {

	/**
	 * Finds a {@link PreferencesDao} by its ID.
	 * 
	 * @param userId
	 * 		  the {@link PreferencesDao} ID
	 * @return a {@link PreferencesDao}
	 */
	PreferencesDao findByUserId(long userId) throws DataNotExistsException;

	/**
	 * savePreferences
	 * @param countryId
	 * @param regionId
	 * @param cityId
	 * @param buildingId
	 * @param floor
	 * @param userId
	 * @return
	 */
	PreferencesDao save(String countryId, String regionId, String cityId, String buildingId, Integer floor, Long userId);

	/**
	 * Updates a {@link PreferencesDao}
	 * 
	 * @param UserDao
	 * 		  the new {@link PreferencesDao}
	 * @return a saved {@link PreferencesDao}
	 */
	PreferencesDao update(PreferencesDao preferenceDao) ;

	/**
	 * Deletes a {@link PreferencesDao}.
	 * 
	 * @param id 
	 * 		  a {@link PreferencesDao} ID
	 */
	void delete(long id) throws DataNotExistsException, IntegrityViolationException;

	boolean useLocationExplorer();
	
}