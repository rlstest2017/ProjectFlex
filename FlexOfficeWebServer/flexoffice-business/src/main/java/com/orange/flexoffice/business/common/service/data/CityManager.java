package com.orange.flexoffice.business.common.service.data;

import java.util.List;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.dao.common.model.data.CityDao;
import com.orange.flexoffice.dao.common.model.object.CityDto;
import com.orange.flexoffice.dao.common.model.object.CitySummaryDto;

/**
 * CityManager
 * @author oab
 *
 */
public interface CityManager {
	/**
	 * findAllCities method used by adminui
	 * @return
	 */
	List<CitySummaryDto> findAllCities();

	/**
	 * findCitiesByRegionId method used by adminui & userui
	 * @return
	 */
	List<CityDao> findCitiesByRegionId(String regionId, boolean isFromAdminUI);

	/**
	 * Finds a city by its ID.
	 * method used by adminui
	 * @param cityId
	 * 		  the {@link cityId} ID
	 * @return a {@link CityDto}
	 */
	CityDto find(long cityId)  throws DataNotExistsException;

	/**
	 * Saves a {@link CityDao}
	 * method used by adminui
	 * @param CityDao
	 * 		  the new {@link CityDao}
	 * @return a saved {@link CityDao}
	 * @throws DataAlreadyExistsException 
	 */
	CityDao save(CityDao city) throws DataAlreadyExistsException;

	/**
	 * Updates a {@link CityDao}
	 * method used by adminui
	 * @param CityDao
	 * 		  the new {@link CityDao}
	 * @return a saved {@link CityDao}
	 */
	CityDao update(CityDao city) throws DataNotExistsException;

	/**
	 * Delete a city
	 * method used by adminui
	 * @param cityId 
	 * 		  a city ID
	 */
	void delete(long cityId) throws DataNotExistsException, IntegrityViolationException;
	
	
}