package com.orange.flexoffice.business.common.service.data.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;






import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.service.data.SensorManager;
import com.orange.flexoffice.dao.common.model.data.GatewayDao;
import com.orange.flexoffice.dao.common.model.data.SensorDao;
import com.orange.flexoffice.dao.common.model.data.UserDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_SensorStatus;
import com.orange.flexoffice.dao.common.model.enumeration.E_SensorType;
import com.orange.flexoffice.dao.common.repository.data.jdbc.GatewayDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.SensorDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.UserDaoRepository;

/**
 * Manage Sensors
 * 
 * @author oab
 */
@Service("SensorManager")
@Transactional
public class SensorManagerImpl implements SensorManager {

	private static final Logger LOGGER = Logger.getLogger(SensorManagerImpl.class);

	@Autowired
	private SensorDaoRepository sensorRepository;

	@Autowired
	private GatewayDaoRepository gatewayRepository;

	@Autowired
	private UserDaoRepository userRepository;

	
	
	@Override
	@Transactional(readOnly=true)
	public List<SensorDao> findAllSensors() {
		return sensorRepository.findAllSensors();
	}

	@Override
	@Transactional(readOnly=true)
	public SensorDao find(String sensorIdentifier)  throws DataNotExistsException {

		try {
			return sensorRepository.findBySensorId(sensorIdentifier);

		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.error("SensorManager.find : Sensor by identifier #" + sensorIdentifier + " is not found");
			throw new DataNotExistsException("SensorManager.find : Sensor by identifier #" + sensorIdentifier + " is not found" + e.getMessage());
		}

	}

	@Override
	public SensorDao save(SensorDao sensorDao) throws DataAlreadyExistsException {

		try {
			if (sensorDao.getRoomId() == null) {
				// Set 0 as room id
				sensorDao.setRoomId(0);				
			} 
			
			// Save SensorDao
			return sensorRepository.saveSensor(sensorDao);
			
		} catch (DataIntegrityViolationException e) {
			LOGGER.error("SensorManager.save : Sensor already exists");
			throw new DataAlreadyExistsException("SensorManager.save : Sensor already exists" + e.getMessage());
		}
	}

	@Override
	public SensorDao update(SensorDao sensorDao) throws DataNotExistsException {

		try {
			if (sensorDao.getRoomId() == null) {
				// Set 0 as room id
				sensorDao.setRoomId(0);				
			} 
			
			// Update SensorDao
			return sensorRepository.updateSensor(sensorDao);
			
		} catch (RuntimeException e) {
			LOGGER.error("SensorManager.update : Sensor to update not found");
			throw new DataNotExistsException("SensorManager.update : Sensor to update not found" + e.getMessage());
		}
	}


	@Override
	public SensorDao updateStatus(SensorDao sensorDao) throws DataNotExistsException {

		try {
			// Update SensorDao
			return sensorRepository.updateSensorStatus(sensorDao);
			
		} catch (RuntimeException e) {
			LOGGER.error("SensorManager.updateStatus : Sensor to update Status not found");
			throw new DataNotExistsException("SensorManager.updateStatus : Sensor to update Status not found" + e.getMessage());
		}
	}

	@Override
	public void delete(String sensorIdentifier) throws DataNotExistsException {

		try {
			// To generate exception if wrong id
			sensorRepository.findBySensorId(sensorIdentifier);
			// Delete Sensor
			sensorRepository.deleteByIdentifier(sensorIdentifier);
			
		} catch (IncorrectResultSizeDataAccessException e) {
			LOGGER.error("SensorManager.delete : Sensor #" + sensorIdentifier + " not found");
			throw new DataNotExistsException("SensorManager.delete : Sensor #" + sensorIdentifier + " not found" + e.getMessage());
		}
	}


}
