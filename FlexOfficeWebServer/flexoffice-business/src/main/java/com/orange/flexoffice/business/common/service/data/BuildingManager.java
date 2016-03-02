package com.orange.flexoffice.business.common.service.data;

import java.util.List;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.dao.common.model.data.BuildingDao;
import com.orange.flexoffice.dao.common.model.object.BuildingDto;
import com.orange.flexoffice.dao.common.model.object.BuildingSummaryDto;

/**
 * BuildingManager
 * @author oab
 *
 */
public interface BuildingManager {
	/**
	 * findAllBuildings method used by adminui
	 * @return
	 */
	List<BuildingSummaryDto> findAllBuildings();

	/**
	 * Finds a building by its ID.
	 * method used by adminui
	 * @param buildingId
	 * 		  the {@link buildingId} ID
	 * @return a {@link BuildingDto}
	 */
	BuildingDto find(long buildingId)  throws DataNotExistsException;

	/**
	 * Saves a {@link BuildingDao}
	 * method used by adminui
	 * @param BuildingDao
	 * 		  the new {@link BuildingDao}
	 * @return a saved {@link BuildingDao}
	 * @throws DataAlreadyExistsException 
	 */
	BuildingDao save(BuildingDao building) throws DataAlreadyExistsException;

	/**
	 * Updates a {@link BuildingDao}
	 * method used by adminui
	 * @param BuildingDao
	 * 		  the new {@link BuildingDao}
	 * @return a saved {@link BuildingDao}
	 */
	BuildingDao update(BuildingDao building) throws DataNotExistsException;

	/**
	 * Delete a building
	 * method used by adminui
	 * @param buildingId 
	 * 		  a building ID
	 */
	void delete(long buildingId) throws DataNotExistsException, IntegrityViolationException;

	
	/**
	 * @param name
	 * 
	 * @return BuildingDao object if found
	 */
	BuildingSummaryDto findByName(String name) throws DataNotExistsException;
	
	
}