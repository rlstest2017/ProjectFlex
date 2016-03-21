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
import com.orange.flexoffice.dao.common.model.enumeration.E_SensorStatus;
import com.orange.flexoffice.dao.common.model.enumeration.E_SensorTeachinStatus;
import com.orange.flexoffice.dao.common.model.enumeration.E_SensorType;
import com.orange.flexoffice.dao.common.model.enumeration.E_TeachinStatus;
import com.orange.flexoffice.dao.common.model.object.SensorTypeAndRoomDto;
import com.orange.flexoffice.dao.common.repository.data.jdbc.AlertDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.GatewayDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RoomDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RoomStatDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.SensorDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.TeachinSensorsDaoRepository;

/**
 * Manage Sensors
 * For PROD LOG LEVEL is info then we say info & error logs.
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
			LOGGER.debug("SensorManager.find : Sensor by identifier #" + sensorIdentifier + " is not found", e);
			LOGGER.error("SensorManager.find : Sensor by identifier #" + sensorIdentifier + " is not found");
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
						
						try {
							teachinRepository.findBySensorIdentifier(sensorDao.getIdentifier());
						} catch(IncorrectResultSizeDataAccessException e ) {
							LOGGER.debug("SensorManager.save : sensorIdentifier #" +sensorDao.getIdentifier()+" will be added in teachin_sensors table", e);
							LOGGER.info("SensorManager.save : sensorIdentifier #" +sensorDao.getIdentifier()+" will be added in teachin_sensors table");
							teachinRepository.saveTechinSensor(teachinSensor);
							teachinRepository.updateTeachinDate(teachin);
					    }
						
					}
				} catch(IncorrectResultSizeDataAccessException e ) {
					LOGGER.debug("SensorManager.save : There is no activate teachin", e);
					LOGGER.info("SensorManager.save : There is no activate teachin");
			    }
			}
			
			return returnedSensor; 
			
		} catch (DataIntegrityViolationException e) {
			LOGGER.debug("SensorManager.save : Sensor already exists", e);
			LOGGER.error("SensorManager.save : Sensor already exists");
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
			// update old associated roomInfos (status, temperature & humidity)
			SensorDao oldSensorInfos = sensorRepository.findBySensorId(sensorDao.getIdentifier());
			if (oldSensorInfos.getRoomId() != sensorDao.getRoomId()) {
				updateRoomInfos(oldSensorInfos.getRoomId(), sensorDao.getType());
			}
			
			// Update SensorDao
			return sensorRepository.updateSensor(sensorDao);
			
		} catch (RuntimeException e) {
			LOGGER.debug("SensorManager.update : Sensor to update not found", e);
			LOGGER.error("SensorManager.update : Sensor to update not found");
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
				
				// if Status is OFFLINE
				if (status.equals(E_SensorStatus.OFFLINE.toString())) {
					
					// check if there is another sensor in room not OFFLINE
					boolean isThereAnotherNotOfflineSensor = false;
					List<SensorDao> sensors = sensorRepository.findByRoomId(roomDao.getId());
					for (SensorDao sens : sensors) {
						if (sens.getId() != sensorId) {
							if (!sens.getStatus().equals(E_SensorStatus.OFFLINE.toString())) {
								isThereAnotherNotOfflineSensor = true;
								break;
							}
						}
					}
					
					if (!isThereAnotherNotOfflineSensor) { // No other sensor with status other than OFFLINE :(  
						// set Room Status to UNKNOWN
						LOGGER.info("RoomDao in updateStatus() is going to set RoomStatus to UNKNOWN, for room#" + roomDao.getName());
						roomDao.setStatus(E_RoomStatus.UNKNOWN.toString());
						roomDao.setUserId(null);
						roomRepository.updateRoomStatus(roomDao); // update Room Status to UNKNOWN
						
						// set RoomStatus room_info UNOCCUPIED if there was OCCUPIED !!!
						try {
							// if roomId & room_info=OCCUPIED in roomStats
							RoomStatDao data = new RoomStatDao();
							data.setRoomId(roomDao.getId().intValue());
							data.setRoomInfo(E_RoomInfo.OCCUPIED.toString());
							RoomStatDao roomStat = findByRoomId(data); // Synchronised method to avoid concurrent access  !!!
							if (roomStat != null) {
								// update by end_occupancy_date=now() & room_info=UNOCCUPIED
								roomStatRepository.updateEndOccupancyDate(roomStat);
							} 
						} catch(IncorrectResultSizeDataAccessException e ) {
							LOGGER.debug("SensorManager.updateStatus : There is no OCCUPIED roomStat with roomId #" + roomDao.getId(), e);
							LOGGER.info("SensorManager.updateStatus : There is no OCCUPIED roomStat with roomId #" + roomDao.getId());
						}
					}
					
					
				} else {
					if (sensorDao.getOccupancyInfo() != null) {
						//--------------------------------------
						processOccupancyInfo(sensorDao, roomDao);
						//--------------------------------------
					} else {
						LOGGER.info("SensorDao occupancyInfo propertie in updateStatus() method is null");
					}
				}
			} else {
				LOGGER.info("RoomDao in updateStatus() method is null");
			}
			
				
						
		} catch (RuntimeException e) {
			LOGGER.debug("SensorManager.updateStatus : Sensor to update Status not found", e);
			LOGGER.error("SensorManager.updateStatus : Sensor to update Status not found");
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
				// update room informations 
				updateRoomInfos(room.getId(), sensor.getType());
				
				// ADD REST command
				GatewayDao gateway = new GatewayDao();
				gateway.setId(room.getGatewayId());
				gateway.setCommand(E_CommandModel.RESET.toString());
				gatewayRepository.updateGatewayCommand(gateway);
				LOGGER.info("RESET command has set in table for gateway id #: " + room.getGatewayId());
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
				LOGGER.info("sensor by identifer " + sensorIdentifier + " has not alert");
				// Delete Sensor
				sensorRepository.deleteByIdentifier(sensorIdentifier);	
			}
		} catch (IncorrectResultSizeDataAccessException e) {
			LOGGER.debug("SensorManager.delete : Sensor #" + sensorIdentifier + " not found", e);
			LOGGER.error("SensorManager.delete : Sensor #" + sensorIdentifier + " not found");
			throw new DataNotExistsException("SensorManager.delete : Sensor #" + sensorIdentifier + " not found");
		}
	}

	/**
	 * processTeachin used in save public method in case of catch DataIntegrityException
	 * @param identifier
	 * @param gatewayMacAdress
	 */
	@Override
	public void processTeachinSensor(String identifier, String gatewayMacAdress, Boolean isFromAddSensor) {
		// compute status PAIRED_OK or PAIRED_KO & save data in techin_sensors 
		LOGGER.debug("identifier is :" + identifier);
		
		// process teachin
		try {
			TeachinSensorDao teachin = teachinRepository.findByTeachinStatus();
			
			// Save teachin_sensors
			if (teachin.getTeachinStatus().equals(E_TeachinStatus.INITIALIZING.toString()) || teachin.getTeachinStatus().equals(E_TeachinStatus.RUNNING.toString()))  {
				TeachinSensorDao teachinSensor = new TeachinSensorDao();
				teachinSensor.setSensorIdentifier(identifier);
				if (isFromAddSensor) {
					teachinSensor.setSensorStatus(E_SensorTeachinStatus.PAIRED_KO.toString());
				} else {
					teachinSensor.setSensorStatus(E_SensorTeachinStatus.PAIRED_OK.toString());
				}
					try {
						teachinRepository.findBySensorIdentifier(identifier);
					} catch(IncorrectResultSizeDataAccessException e ) {
						LOGGER.debug("SensorManager.processTeachinSensor : sensorIdentifier #" +identifier+" will be added in teachin_sensors table", e);
						LOGGER.info("SensorManager.processTeachinSensor : sensorIdentifier #" +identifier+" will be added in teachin_sensors table");
						teachinRepository.saveTechinSensor(teachinSensor);
						teachinRepository.updateTeachinDate(teachin);
				    }
				
				if (isFromAddSensor) {
					// Set Gateway to RESET 
					GatewayDao gateway = gatewayRepository.findByMacAddress(gatewayMacAdress);
					gateway.setId(gateway.getId());
					gateway.setCommand(E_CommandModel.RESET.toString());
					gatewayRepository.updateGatewayCommand(gateway);
					LOGGER.info("RESET command has set in table for gateway id #: " + gateway.getId());
				}
			}
		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.debug("SensorManager.processTeachinSensor : There is no activate teachin", e);
			LOGGER.info("SensorManager.processTeachinSensor : There is no activate teachin");
	    }
		
			
	}

	/**
	 * processOccupiedRoom
	 * @param data
	 */
	private void processOccupiedRoom(RoomStatDao data) {
		try {
			// if roomId & room_info=OCCUPIED in roomStats
			RoomStatDao roomStat = findByRoomId(data); // Synchronised method to avoid concurrent access  !!!
			if (roomStat == null) {
				// create a new line with roomId, begin_occupancy_date=now() & room_info=OCCUPIED
				roomStatRepository.saveOccupiedRoomStat(data);
			} 
		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.debug("SensorManager.updateStatus.processOccupiedRoom : There is no OCCUPIED roomStat with roomId #" + data.getRoomId(), e);
			LOGGER.info("SensorManager.updateStatus.processOccupiedRoom : There is no OCCUPIED roomStat with roomId #" + data.getRoomId());
			// create a new line with roomId, begin_occupancy_date=now() & room_info=OCCUPIED
			roomStatRepository.saveOccupiedRoomStat(data);
		}
	}
	
	/**
	 * processOccupancyInfo
	 * @param sensorDao
	 * @param roomDao
	 */
	private void processOccupancyInfo(SensorDao sensorDao, RoomDao roomDao) {
		
		if (sensorDao.getOccupancyInfo().equals(E_OccupancyInfo.OCCUPIED.toString())) { // L'info room OCCUPIED
			LOGGER.debug("RoomDao in updateStatus() is going to set RoomStatus to OCCUPIED");
			roomDao.setStatus(E_RoomStatus.OCCUPIED.toString());
			//-------------------------------------------------------------------------
			roomRepository.updateRoomStatus(roomDao); // update Room Status to OCCUPIED
			//-------------------------------------------------------------------------
			LOGGER.info("RoomStatus is set to " + roomDao.getStatus() + " for room#" + roomDao.getName());
			
			RoomStatDao data = new RoomStatDao();
			data.setRoomId(roomDao.getId().intValue());
			data.setRoomInfo(E_RoomInfo.RESERVED.toString());// Pour voir est-ce il y avait une réservation ou pas avant ce status OCCUPIED !!!	
				try {
					// if roomId & room_info=RESERVED in roomStats
					RoomStatDao roomStat = findByRoomId(data); // Synchronised method to avoid concurrent access  !!!
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
					LOGGER.info("SensorManager.updateStatus : There is no RESERVED roomStat with roomId #" + roomDao.getId());
					// create a new line with roomId, begin_occupancy_date=now() & room_info=OCCUPIED if not created yet !!!
					data.setRoomInfo(E_RoomInfo.OCCUPIED.toString());
					processOccupiedRoom(data);
				}
		} else if (sensorDao.getOccupancyInfo().equals(E_OccupancyInfo.UNOCCUPIED.toString())) {  // L'info room UNOCCUPIED
			// search in DB if another sensor has said that the room is OCCUPIED 
			List<SensorDao> sensors = sensorRepository.findByRoomIdAndOccupiedInfo(roomDao.getId());
			if ((sensors == null) || (sensors.isEmpty())) {  // L'info room UNOCCUPIED est prise en compte
				// check if the room is not reserved !!!
				RoomDao room = roomRepository.findOne(roomDao.getId());
				if (!E_RoomStatus.RESERVED.toString().equals(room.getStatus())) {
					LOGGER.info("RoomDao in updateStatus() is going to set RoomStatus to FREE");
					roomDao.setUserId(null);
					roomDao.setStatus(E_RoomStatus.FREE.toString());
					//---------------------------------------------------------------------
					roomRepository.updateRoomStatus(roomDao); // update Room Status to FREE
					//---------------------------------------------------------------------
					try {
						// if roomId & room_info=OCCUPIED in roomStats
						RoomStatDao data = new RoomStatDao();
						data.setRoomId(roomDao.getId().intValue());
						data.setRoomInfo(E_RoomInfo.OCCUPIED.toString());
						RoomStatDao roomStat = findByRoomId(data); // Synchronised method to avoid concurrent access  !!!
						if (roomStat != null) {
							// update by end_occupancy_date=now() & room_info=UNOCCUPIED
							roomStatRepository.updateEndOccupancyDate(roomStat);
						} 
					} catch(IncorrectResultSizeDataAccessException e ) {
						LOGGER.debug("SensorManager.updateStatus : There is no OCCUPIED roomStat with roomId #" + roomDao.getId(), e);
						LOGGER.info("SensorManager.updateStatus : There is no OCCUPIED roomStat with roomId #" + roomDao.getId());
					}
				}
			}
		}
		
	}
	/**
	 * updateRoomInfos
	 * @param roomId
	 * @param sensorType
	 */
	private void updateRoomInfos(long roomId, String sensorType) {
		// Delete sensor => si type detection et si le seul type detection dans la room => mettre à jour status room à indisponible
		//        => si type temperature et si le seul type temperature dans la room => mettre à null les champ temperature & humidity
		// TODO 
		SensorTypeAndRoomDto data = new SensorTypeAndRoomDto();
		data.setType(sensorType);
		data.setRoomId(Long.valueOf(roomId).intValue());
		Long count = sensorRepository.countByTypeAndRoomId(data);
		if (count == 1) {
			RoomDao room = roomRepository.findOne(roomId);
			if (E_SensorType.MOTION_DETECTION.toString().equals(sensorType)) {
				// update room status to UNKNOWN
				room.setStatus(E_RoomStatus.UNKNOWN.toString());
				room.setUserId(null);
			} else if (E_SensorType.TEMPERATURE_HUMIDITY.toString().equals(sensorType)) {
				// update temperature & humidity to null
				room.setTemperature(null);
				room.setHumidity(null);
			} 
			roomRepository.updateRoomStatus(room);
		}
	}
	
	/**
	 * findByRoomId synchronized method
	 * @param data
	 * @return
	 */
	private synchronized RoomStatDao findByRoomId(RoomStatDao data) { 
	 return roomStatRepository.findbyRoomId(data);
	}
	
}
