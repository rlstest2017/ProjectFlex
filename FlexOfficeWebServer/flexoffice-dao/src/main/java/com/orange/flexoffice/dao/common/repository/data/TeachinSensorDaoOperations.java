package com.orange.flexoffice.dao.common.repository.data;

import java.util.List;

import com.orange.flexoffice.dao.common.model.data.TeachinSensorDao;

/**
 * TeachinSensorDaoOperations
 * @author oab
 *
 */
public interface TeachinSensorDaoOperations {
	
	List<TeachinSensorDao> findAllTeachinSensors();
	
	//select * from teachin_sensors where teachin_status is not null //
	TeachinSensorDao findByTeachinStatus();
	
	TeachinSensorDao updateTeachinStatus(TeachinSensorDao data);
	
	TeachinSensorDao saveTechinSensor(TeachinSensorDao data);
	
	void deleteAllTeachinSensors();

}
