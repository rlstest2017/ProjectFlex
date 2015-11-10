package com.orange.flexoffice.dao.common.repository.data;

import java.util.List;

import com.orange.flexoffice.dao.common.model.data.SensorDao;

/**
 * SensorDaoOperations
 * @author oab
 *
 */
public interface SensorDaoOperations {
	
	List<SensorDao> findAllSensors();
	
	List<SensorDao> findBySensorId(Long sonsorId);
	
	List<SensorDao> findByRoomId(Long roomId);
	
}
