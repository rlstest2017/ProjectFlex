package com.orange.flexoffice.dao.common.repository.data;

import java.util.List;

import org.springframework.dao.IncorrectResultSizeDataAccessException;

import com.orange.flexoffice.dao.common.model.data.TeachinSensorDao;

/**
 * TeachinSensorDaoOperations
 * @author oab
 *
 */
public interface TeachinSensorDaoOperations {
	
	List<TeachinSensorDao> findAllTeachinSensors();
	
	//select * from teachin_sensors where teachin_status is not null //
	TeachinSensorDao findByTeachinStatus() throws IncorrectResultSizeDataAccessException;
	
	TeachinSensorDao findByUserId(Long userId) throws IncorrectResultSizeDataAccessException;
	
	TeachinSensorDao updateTeachinStatus(TeachinSensorDao data);
	
	TeachinSensorDao saveTechinSensor(TeachinSensorDao data);
	
	void deleteAllTeachinSensors();

}
