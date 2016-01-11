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
import com.orange.flexoffice.dao.common.model.data.GatewayDao;
import com.orange.flexoffice.dao.common.model.data.RoomDao;
import com.orange.flexoffice.dao.common.model.data.RoomStatDao;
import com.orange.flexoffice.dao.common.model.data.SensorDao;
import com.orange.flexoffice.dao.common.model.data.TeachinSensorDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_CommandModel;
import com.orange.flexoffice.dao.common.model.enumeration.E_OccupancyInfo;
import com.orange.flexoffice.dao.common.model.enumeration.E_RoomInfo;
import com.orange.flexoffice.dao.common.model.enumeration.E_RoomStatus;
import com.orange.flexoffice.dao.common.model.enumeration.E_SensorTeachinStatus;
import com.orange.flexoffice.dao.common.model.enumeration.E_TeachinStatus;
import com.orange.flexoffice.dao.common.repository.data.jdbc.AlertDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.GatewayDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RoomDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RoomStatDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.SensorDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.TeachinSensorsDaoRepository;

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
	private RoomDaoRepository roomRepository;
	@Autowired
	private RoomStatDaoRepository roomStatRepository;
	@Autowired
	private AlertDaoRepository alertRepository;
	@Autowired
	private AlertManager alertManager;
	@Autowired
	private TeachinSensorsDaoRepository teachinRepository;
	
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
	public SensorDao save(SensorDao sensorDao, String gatewayMacAdress) throws DataAlreadyExistsException {

		try {
			if (sensorDao.getRoomId() == null) {
				// Set 0 as room id
				sensorDao.setRoomId(0);				
			} 
			
			// Save SensorDao
			SensorDao returnedSensor = sensorRepository.saveSensor(sensorDao);
			
			if (gatewayMacAdress != null) { // method called by gatewayApi
				// process teachin
				try {
					TeachinSensorDao teachin = teachinRepository.findByTeachinStatus();
					// Save teachin_sensors
					if (teachin.getTeachinStatus().equals(E_TeachinStatus.INITIALIZING.toString()) || teachin.getTeachinStatus().equals(E_TeachinStatus.RUNNING.toString()))  {
						TeachinSensorDao teachinSensor = new TeachinSensorDao();
						teachinSensor.setSensorIdentifier(sensorDao.getIdentifier());
						teachinSensor.setSensorStatus(E_SensorTeachinStatus.NOT_PAIRED.toString());
						teachinRepository.saveTechinSensor(teachinSensor);
						teachinRepository.updateTeachinDate(teachin);
					}
				} catch(IncorrectResultSizeDataAccessException e ) {
					LOGGER.error("SensorManager.save : There is no activate teachin", e);
			    }
			}
			
			return returnedSensor; 
			
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
					if (sensorDao.getOccupancyInfo().equals(E_OccupancyInfo.OCCUPIED.toString())) { // L'info room OCCUPIED
						LOGGER.debug("RoomDao in updateStatus() is going to set RoomStatus to OCCUPIED");
						roomDao.setStatus(E_RoomStatus.OCCUPIED.toString());
						//-------------------------------------------------------------------------
						roomRepository.updateRoomStatus(roomDao); // update Room Status to OCCUPIED
						//-------------------------------------------------------------------------
						RoomStatDao data = new RoomStatDao();
						data.setRoomId(roomDao.getId().intValue());
						data.setRoomInfo(E_RoomInfo.RESERVED.toString());	
							try {
								// if roomId & room_info=RESERVED in roomStats
								RoomStatDao roomStat = roomStatRepository.findbyRoomId(data);
								if (roomStat != null) {
									// update begin_occupancy_date=now() & isReservationHonored=true & room_info=OCCUPIED
									roomStatRepository.updateBeginOccupancyDate(roomStat);
								} else {
									// create a new line with roomId, begin_occupancy_date=now() & room_info=OCCUPIED if not created yet !!!
									data.setRoomInfo(E_RoomInfo.OCCUPIED.toString());
									processOccupiedRoom(data);
								}
							} catch(IncorrectResultSizeDataAccessException e ) {
								LOGGER.debug("SensorManager.updateStatus : There is no RESERVED roomStat with roomId #" + roomDao.getId(), e);
								// create a new line with roomId, begin_occupancy_date=now() & room_info=OCCUPIED if not created yet !!!
								data.setRoomInfo(E_RoomInfo.OCCUPIED.toString());
								processOccupiedRoom(data);
							}
					} else if (sensorDao.getOccupancyInfo().equals(E_OccupancyInfo.UNOCCUPIED.toString())) {  // L'info room UNOCCUPIED
						// search in DB if another sensor has said thar the room is OCCUPIED 
						List<SensorDao> sensors = sensorRepository.findByRoomIdAndOccupiedInfo(roomDao.getId());
						if ((sensors == null) || (sensors.isEmpty())) {  // L'info room UNOCCUPIED est prise en compte
							LOGGER.debug("RoomDao in updateStatus() is going to set RoomStatus to FREE");
							roomDao.setStatus(E_RoomStatus.FREE.toString());
							//---------------------------------------------------------------------
							roomRepository.updateRoomStatus(roomDao); // update Room Status to FREE
							//---------------------------------------------------------------------
							try {
								// if roomId & room_info=OCCUPIED in roomStats
								RoomStatDao data = new RoomStatDao();
								data.setRoomId(roomDao.getId().intValue());
								data.setRoomInfo(E_RoomInfo.OCCUPIED.toString());
								RoomStatDao roomStat = roomStatRepository.findbyRoomId(data);
								if (roomStat != null) {
									// update by end_occupancy_date=now() & room_info=UNOCCUPIED
									roomStatRepository.updateEndOccupancyDate(roomStat);
								} 
							} catch(IncorrectResultSizeDataAccessException e ) {
								LOGGER.debug("SensorManager.updateStatus : There is no OCCUPIED roomStat with roomId #" + roomDao.getId(), e);
							}
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
			// Add REST command to Gateways Table
			if (sensor.getRoomId() != null && sensor.getRoomId() != 0) {
				RoomDao room = roomRepository.findByRoomId(sensor.getRoomId().longValue());
				//
				GatewayDao gateway = new GatewayDao();
				gateway.setId(room.getGatewayId());
				gateway.setCommand(E_CommandModel.RESET.toString());
				gatewayRepository.updateGatewayCommand(gateway);
			}
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

	/**
	 * processTeachin used in save public method in case of catch DataIntegrityException
	 * @param identifier
	 * @param gatewayMacAdress
	 */
	@Override
	public void processTeachinSensor(String identifier, String gatewayMacAdress) {
		// compute status PAIRED_OK or PAIRED_KO & save data in techin_sensors 
		LOGGER.debug("identifier is :" + identifier);
		
		// process teachin
		try {
			TeachinSensorDao teachin = teachinRepository.findByTeachinStatus();
			// Save teachin_sensors
			if (teachin.getTeachinStatus().equals(E_TeachinStatus.INITIALIZING.toString()) || teachin.getTeachinStatus().equals(E_TeachinStatus.RUNNING.toString()))  {
				SensorDao sensor = sensorRepository.findBySensorId(identifier);
				
				TeachinSensorDao teachinSensor = new TeachinSensorDao();
				teachinSensor.setSensorIdentifier(identifier);
				
				if (sensor.getRoomId() == 0) {
					teachinSensor.setSensorStatus(E_SensorTeachinStatus.NOT_PAIRED.toString());
				} else {
					RoomDao room = roomRepository.findByRoomId(Long.valueOf(sensor.getRoomId()));
					GatewayDao gateway = gatewayRepository.findByGatewayId(room.getGatewayId());
					if (gateway.getMacAddress().equals(gatewayMacAdress)) {
						teachinSensor.setSensorStatus(E_SensorTeachinStatus.PAIRED_OK.toString());
					} else {
						teachinSensor.setSensorStatus(E_SensorTeachinStatus.PAIRED_KO.toString());
					}
				}
				
				teachinRepository.saveTechinSensor(teachinSensor);
				teachinRepository.updateTeachinDate(teachin);
			}
		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.error("SensorManager.processTeachinSensor : There is no activate teachin", e);
	    }
		
			
	}

	private void processOccupiedRoom(RoomStatDao data) {
		try {
			// if roomId & room_info=OCCUPIED in roomStats
			RoomStatDao roomStat = roomStatRepository.findbyRoomId(data);
			if (roomStat == null) {
				// create a new line with roomId, begin_occupancy_date=now() & room_info=OCCUPIED
				roomStatRepository.saveOccupiedRoomStat(data);
			} 
		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.debug("SensorManager.updateStatus.processOccupiedRoom : There is no OCCUPIED roomStat with roomId #" + data.getRoomId(), e);
			// create a new line with roomId, begin_occupancy_date=now() & room_info=OCCUPIED
			roomStatRepository.saveOccupiedRoomStat(data);
		}

	}
	
	
}
