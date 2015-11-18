package com.orange.flexoffice.business.common.service.data;

import java.util.List;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.dao.common.model.data.SensorDao;

/**
 * SensorManager
 * @author oab
 *
 */
public interface SensorManager {
	/**
	 * findAllSensors method used by adminui
	 * @return
	 */
	List<SensorDao> findAllSensors();

	/**
	 * Finds a sensor by its ID.
	 * method used by adminui
	 * @param sensorIdentifier
	 * 		  the {@link sensorIdentifier} ID
	 * @return a {@link SensorDao}
	 */
	SensorDao find(String sensorIdentifier)  throws DataNotExistsException;

	/**
	 * Saves a {@link SensorDao}
	 * method used by adminui
	 * @param sensorDao
	 * 		  the new {@link SensorDao}
	 * @return a saved {@link SensorDao}
	 * @throws DataAlreadyExistsException 
	 */
	SensorDao save(SensorDao sensorDao) throws DataAlreadyExistsException;

	/**
	 * Updates a {@link SensorDao}
	 * method used by adminui
	 * @param sensorDao
	 * 		  the new {@link SensorDao}
	 * @return a saved {@link SensorDao}
	 */
	SensorDao update(SensorDao sensorDao) throws DataNotExistsException;


	/**
	 * Updates status {@link SensorDao}
	 * method used by adminui
	 * @param sensorDao
	 * 		  the new {@link SensorDao}
	 * @return a saved {@link SensorDao}
	 */
	SensorDao updateStatus(SensorDao sensorDao) throws DataNotExistsException;

	/**
	 * Deletes a sensor
	 * method used by adminui
	 * @param sensorIdentifier 
	 * 		  a sensor ID
	 */
	void delete(String sensorIdentifier) throws DataNotExistsException;

}