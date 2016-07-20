package com.orange.flexoffice.dao.common.repository.data;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import com.orange.flexoffice.dao.common.model.data.SensorDao;
import com.orange.flexoffice.dao.common.model.object.SensorTypeAndRoomDto;

/**
 * SensorDaoOperations
 * @author oab
 *
 */
public interface SensorDaoOperations {
	
	List<SensorDao> findAllSensors();
	
	SensorDao findBySensorId(String sensorIdentifier) throws IncorrectResultSizeDataAccessException;
	
	List<SensorDao> findByRoomId(Long roomId);
	
	List<SensorDao> findByRoomIdAndOccupiedInfo(Long roomId);
	
	SensorDao saveSensor(SensorDao data) throws DataIntegrityViolationException;
	
	SensorDao updateSensor(SensorDao data) throws DataAccessException; 

	SensorDao updateSensorStatus(SensorDao data) throws DataAccessException;
	
	SensorDao updateSensorRoomId(SensorDao data) throws DataAccessException;
	
	void deleteByIdentifier(String sensorIdentifier) throws DataAccessException;
	
	Long countByTypeAndRoomId(SensorTypeAndRoomDto data);
}
