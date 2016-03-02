package com.orange.flexoffice.business.common.service.data;

import java.util.List;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.dao.common.model.data.RegionDao;

/**
 * RegionManager
 * @author oab
 *
 */
public interface RegionManager {
	/**
	 * findAllRegions method used by adminui
	 * @return
	 */
	List<RegionDao> findAllRegions();

	/**
	 * Finds a region by its ID.
	 * method used by adminui
	 * @param regionId
	 * 		  the {@link regionId} ID
	 * @return a {@link RegionDao}
	 */
	RegionDao find(long regionId)  throws DataNotExistsException;

	/**
	 * Saves a {@link RegionDao}
	 * method used by adminui
	 * @param RegionDao
	 * 		  the new {@link RegionDao}
	 * @return a saved {@link RegionDao}
	 * @throws DataAlreadyExistsException 
	 */
	RegionDao save(RegionDao RegionDao) throws DataAlreadyExistsException;

	/**
	 * Updates a {@link RegionDao}
	 * method used by adminui
	 * @param RegionDao
	 * 		  the new {@link RegionDao}
	 * @return a saved {@link RegionDao}
	 */
	RegionDao update(RegionDao RegionDao) throws DataNotExistsException;

	/**
	 * Delete a region
	 * method used by adminui
	 * @param regionId 
	 * 		  a region ID
	 */
	void delete(long regionId) throws DataNotExistsException, IntegrityViolationException;

	
	/**
	 * @param name
	 * 
	 * @return RegionDao object if found
	 */
	RegionDao findByName(String name) throws DataNotExistsException;
	
	
}