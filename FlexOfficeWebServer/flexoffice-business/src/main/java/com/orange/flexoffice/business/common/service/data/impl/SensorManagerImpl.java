package com.orange.flexoffice.business.common.service.data.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.service.data.AlertManager;
import com.orange.flexoffice.business.common.service.data.SensorManager;
import com.orange.flexoffice.dao.common.model.data.AlertDao;
import com.orange.flexoffice.dao.common.model.data.RoomDao;
import com.orange.flexoffice.dao.common.model.data.SensorDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_OccupancyInfo;
import com.orange.flexoffice.dao.common.model.enumeration.E_RoomStatus;
import com.orange.flexoffice.dao.common.repository.data.jdbc.AlertDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RoomDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.SensorDaoRepository;

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
	private RoomDaoRepository roomRepository;

	@Autowired
	private AlertDaoRepository alertRepository;
	
	@Autowired
	private AlertManager alertManager;
	
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
			LOGGER.error("SensorManager.find : Sensor by identifier #" + sensorIdentifier + " is not found", e);
			throw new DataNotExistsException("SensorManager.find : Sensor by identifier #" + sensorIdentifier + " is not found");
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
			LOGGER.error("SensorManager.save : Sensor already exists", e);
			throw new DataAlreadyExistsException("SensorManager.save : Sensor already exists");
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
			LOGGER.error("SensorManager.update : Sensor to update not found", e);
			throw new DataNotExistsException("SensorManager.update : Sensor to update not found");
		}
	}


	@Override
	public void updateStatus(SensorDao sensorDao, RoomDao roomDao) throws DataNotExistsException {
		try {
			
			// update SensorDao status & occupancyInfo
			sensorRepository.updateSensorStatus(sensorDao); 
						
			// update Sensor Alert
			Long sensorId = sensorDao.getId();
			String status = sensorDao.getStatus();
			alertManager.updateSensorAlert(sensorId, status);
			
			if (roomDao != null) {
				if (sensorDao.getOccupancyInfo() != null) {
					if (sensorDao.getOccupancyInfo().equals(E_OccupancyInfo.OCCUPIED.toString())) {
						LOGGER.debug("RoomDao in updateStatus() is going to set RoomStatus to OCCUPIED");
						roomDao.setStatus(E_RoomStatus.OCCUPIED.toString());
						roomRepository.updateRoomStatus(roomDao);		
					} else if (sensorDao.getOccupancyInfo().equals(E_OccupancyInfo.UNOCCUPIED.toString())) {
						// search in DB if another sensor has said thar the room is OCCUPIED 
						List<SensorDao> sensors = sensorRepository.findByRoomIdAndOccupiedInfo(roomDao.getId());
						if ((sensors == null) || (sensors.isEmpty())) {
							LOGGER.debug("RoomDao in updateStatus() is going to set RoomStatus to FREE");
							roomDao.setStatus(E_RoomStatus.FREE.toString());
							roomRepository.updateRoomStatus(roomDao);
						}
					}
				} else {
					LOGGER.debug("SensorDao occupancyInfo propertie in updateStatus() method is null");
				}
			} else {
				LOGGER.debug("RoomDao in updateStatus() method is null");
			}
			
				
						
		} catch (RuntimeException e) {
			LOGGER.error("SensorManager.updateStatus : Sensor to update Status not found", e);
			throw new DataNotExistsException("SensorManager.updateStatus : Sensor to update Status not found");
		}
	}

	@Override
	public void delete(String sensorIdentifier) throws DataNotExistsException {

		try {
			// To generate exception if wrong id
			SensorDao sensor = sensorRepository.findBySensorId(sensorIdentifier);
			try {
				AlertDao alert = alertRepository.findBySensorId(sensor.getId());
				if (alert != null) {
					// delete alert
					alertRepository.delete(alert.getId());
					// Delete Sensor
					sensorRepository.deleteByIdentifier(sensorIdentifier);
				}
			} catch(IncorrectResultSizeDataAccessException e ) {
				LOGGER.debug("sensor by identifer " + sensorIdentifier + " has not alert", e);
				// Delete Sensor
				sensorRepository.deleteByIdentifier(sensorIdentifier);	
			}
		} catch (IncorrectResultSizeDataAccessException e) {
			LOGGER.error("SensorManager.delete : Sensor #" + sensorIdentifier + " not found", e);
			throw new DataNotExistsException("SensorManager.delete : Sensor #" + sensorIdentifier + " not found");
		}
	}


}
