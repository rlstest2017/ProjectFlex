package com.orange.flexoffice.business.common.service.data.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.business.common.service.data.PreferenceUserManager;
import com.orange.flexoffice.dao.common.model.data.ConfigurationDao;
import com.orange.flexoffice.dao.common.model.data.PreferencesDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_ConfigurationKey;
import com.orange.flexoffice.dao.common.repository.data.jdbc.ConfigurationDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.PreferencesDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RoomDaoRepository;

/**
 * Manages {@link PreferencesDao}.
 * For PROD LOG LEVEL is info then we say info & error logs.
 * 
 * @author oab
 */
@Service("PreferenceUserManager")
@Transactional
public class PreferenceUserManagerImpl implements PreferenceUserManager {
	
	private static final Logger LOGGER = Logger.getLogger(PreferenceUserManagerImpl.class);
	
	@Autowired
	private PreferencesDaoRepository preferencesRepository;
	@Autowired
	private ConfigurationDaoRepository configRepository;
	@Autowired
	private RoomDaoRepository roomRepository;
	
	@Override
	public PreferencesDao findByUserId(long userId) throws DataNotExistsException {
		PreferencesDao preferencesDao;
		try {
			preferencesDao = preferencesRepository.findByUserId(userId);
		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.debug("PreferenceUserManager.find : Preference by userId #" + userId + " is not found", e);
			LOGGER.error("PreferenceUserManager.find : Preference by userId #" + userId + " is not found");
			throw new DataNotExistsException("PreferenceUserManager.find : Preference by userId #" + userId + " is not found");
		}
		return preferencesDao;
	}

	@Override
	public PreferencesDao save(String countryId, String regionId, String cityId, String buildingId, Integer floor, Long userId) {
		PreferencesDao data = new PreferencesDao();
		if (countryId != null) { 
			data.setCountryId(Long.valueOf(countryId)); 
		} 
		if (regionId != null) { 
			data.setRegionId(Long.valueOf(regionId));
		} 
		if (cityId != null) { 
			data.setCityId(Long.valueOf(cityId)); 
		} 
		if (buildingId != null) {
			data.setBuildingId(Long.valueOf(buildingId)); 
		} 
		if (floor != null) {
			data.setFloor(Long.valueOf(floor));
		}
		
		data.setUserId(userId);
		
		return preferencesRepository.savePreferences(data);
	}

	@Override
	public PreferencesDao update(PreferencesDao preferenceDao) {
		return preferencesRepository.updatePreferences(preferenceDao);
	}

	@Override
	public void delete(long id) throws DataNotExistsException, IntegrityViolationException {
		preferencesRepository.delete(id);
	}

	@Override
	public boolean useLocationExplorer() {
		boolean isExplorerEnabled = false;
		ConfigurationDao nbRooms = configRepository.findByKey(E_ConfigurationKey.THRESHOLD_ENABLED_ADVANCEDRESEARCH_OF_ROOMS.toString());
		String nbRoomsValue = nbRooms.getValue();
		Long rooms = roomRepository.count();
		if (rooms > Long.valueOf(nbRoomsValue)) {
			isExplorerEnabled = true;
		}
		return isExplorerEnabled;
	}
}
