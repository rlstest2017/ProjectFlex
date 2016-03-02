package com.orange.flexoffice.business.common.service.data;

import java.util.List;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.dao.common.model.data.CityDao;

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
	List<CityDao> findAllCities();

	/**
	 * Finds a city by its ID.
	 * method used by adminui
	 * @param cityId
	 * 		  the {@link cityId} ID
	 * @return a {@link CityDao}
	 */
	CityDao find(long cityId)  throws DataNotExistsException;

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

	
	/**
	 * @param name
	 * 
	 * @return CityDao object if found
	 */
	CityDao findByName(String name) throws DataNotExistsException;
	
	
}