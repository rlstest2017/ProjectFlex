package com.orange.flexoffice.dao.common.repository.data;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import com.orange.flexoffice.dao.common.model.data.RoomDao;
import com.orange.flexoffice.dao.common.model.data.SensorDao;

/**
 * SensorDaoOperations
 * @author oab
 *
 */
public interface SensorDaoOperations {
	
	List<SensorDao> findAllSensors();
	
	SensorDao findBySensorId(String sensorIdentifier) throws IncorrectResultSizeDataAccessException;
	
	List<SensorDao> findByRoomId(Long roomId);
	
	SensorDao findByName(String name) throws IncorrectResultSizeDataAccessException;

	SensorDao saveSensor(SensorDao data) throws DataIntegrityViolationException;
	
	SensorDao updateSensor(SensorDao data) throws DataAccessException; 

	SensorDao updateSensorStatus(SensorDao data) throws DataAccessException;
	
	void deleteByIdentifier(String sensorIdentifier) throws DataAccessException;
}
