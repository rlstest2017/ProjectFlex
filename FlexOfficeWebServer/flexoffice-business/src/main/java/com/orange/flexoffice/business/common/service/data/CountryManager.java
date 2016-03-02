package com.orange.flexoffice.business.common.service.data;

import java.util.List;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.dao.common.model.data.CountryDao;

/**
 * CountryManager
 * @author oab
 *
 */
public interface CountryManager {
	/**
	 * findAllCountries method used by adminui
	 * @return
	 */
	List<CountryDao> findAllCountries();

	/**
	 * Finds a country by its ID.
	 * method used by adminui
	 * @param countryId
	 * 		  the {@link countryId} ID
	 * @return a {@link CountryDao}
	 */
	CountryDao find(long countryId)  throws DataNotExistsException;

	/**
	 * Saves a {@link CountryDao}
	 * method used by adminui
	 * @param CountryDao
	 * 		  the new {@link CountryDao}
	 * @return a saved {@link CountryDao}
	 * @throws DataAlreadyExistsException 
	 */
	CountryDao save(CountryDao countryDao) throws DataAlreadyExistsException;

	/**
	 * Updates a {@link CountryDao}
	 * method used by adminui
	 * @param CountryDao
	 * 		  the new {@link CountryDao}
	 * @return a saved {@link CountryDao}
	 */
	CountryDao update(CountryDao CountryDao) throws DataNotExistsException;

	/**
	 * Deletes a country
	 * method used by adminui
	 * @param countryId 
	 * 		  a country ID
	 */
	void delete(long countryId) throws DataNotExistsException, IntegrityViolationException;

	
	/**
	 * @param name
	 * 
	 * @return CountryDao object if found
	 */
	CountryDao findByName(String name) throws DataNotExistsException;
	
	
}