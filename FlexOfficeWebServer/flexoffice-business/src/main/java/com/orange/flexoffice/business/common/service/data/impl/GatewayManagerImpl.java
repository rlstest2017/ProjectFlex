package com.orange.flexoffice.business.common.service.data.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.business.common.service.data.AlertManager;
import com.orange.flexoffice.business.common.service.data.GatewayManager;
import com.orange.flexoffice.business.gatewayapi.dto.GatewayCommand;
import com.orange.flexoffice.business.gatewayapi.enums.EnumCommandModel;
import com.orange.flexoffice.dao.common.model.data.AlertDao;
import com.orange.flexoffice.dao.common.model.data.ConfigurationDao;
import com.orange.flexoffice.dao.common.model.data.GatewayDao;
import com.orange.flexoffice.dao.common.model.data.RoomDao;
import com.orange.flexoffice.dao.common.model.data.RoomStatDao;
import com.orange.flexoffice.dao.common.model.data.SensorDao;
import com.orange.flexoffice.dao.common.model.data.TeachinSensorDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_CommandModel;
import com.orange.flexoffice.dao.common.model.enumeration.E_ConfigurationKey;
import com.orange.flexoffice.dao.common.model.enumeration.E_GatewayStatus;
import com.orange.flexoffice.dao.common.model.enumeration.E_RoomInfo;
import com.orange.flexoffice.dao.common.model.enumeration.E_RoomStatus;
import com.orange.flexoffice.dao.common.model.enumeration.E_SensorStatus;
import com.orange.flexoffice.dao.common.model.enumeration.E_TeachinStatus;
import com.orange.flexoffice.dao.common.model.object.GatewayDto;
import com.orange.flexoffice.dao.common.model.object.RoomDto;
import com.orange.flexoffice.dao.common.repository.data.jdbc.AlertDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.ConfigurationDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.GatewayDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RoomDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RoomStatDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.SensorDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.TeachinSensorsDaoRepository;

/**
 * Manages {@link GatewayDto}.
 * For PROD LOG LEVEL is info then we say info & error logs.
 * 
 * @author oab
 */
@Service("GatewayManager")
@Transactional
public class GatewayManagerImpl implements GatewayManager {
	
	private static final Logger LOGGER = Logger.getLogger(GatewayManagerImpl.class);
	
	@Autowired
	private GatewayDaoRepository gatewayRepository;
	@Autowired
	private RoomDaoRepository roomRepository;
	@Autowired
	private RoomStatDaoRepository roomStatRepository;
	@Autowired
	private SensorDaoRepository sensorRepository;
	@Autowired
	private AlertDaoRepository alertRepository;
	@Autowired
	private AlertManager alertManager;
	@Autowired
	private ConfigurationDaoRepository configRepository;
	@Autowired
	private TeachinSensorsDaoRepository teachinRepository;
	
	@Override
	@Transactional(readOnly=true)
	public List<GatewayDao> findAllGateways() {
		return gatewayRepository.findAllGateways();
	}

	@Override
	@Transactional(readOnly=true)
	public List<RoomDto> findGatewayRooms(String gatewayMacAddress) {
		
		List<RoomDto> roomDtoList = new ArrayList<RoomDto>();
		
		GatewayDao gateway = gatewayRepository.findByMacAddress(gatewayMacAddress);
		
		List<RoomDao> roomsDao = roomRepository.findByGatewayId(gateway.getId());
		
		LOGGER.info("There is: " + roomsDao.size() + " rooms for gateway (macAddress) :" + gatewayMacAddress );
		
		for (RoomDao roomDao : roomsDao) {
			RoomDto roomDto = new RoomDto();
			roomDto.setId(roomDao.getId());
			roomDto.setName(roomDao.getName());
			roomDto.setOccupancyTimeOut(getOccupancyTimeOut());
			List<SensorDao> sonsensDao = getSensors(roomDao.getId());
			
			LOGGER.info("There is: " + sonsensDao.size() + " sensors for room :" + roomDao.getColumnId());
			
			if ((sonsensDao != null)&&(!sonsensDao.isEmpty())) {
				roomDto.setSensors(sonsensDao);					
			}
			
			roomDtoList.add(roomDto);
		}
		
		return roomDtoList;
	}
	
	
	@Override
	@Transactional(readOnly=true)
	public GatewayDto find(long gatewayId)  throws DataNotExistsException {
		
		GatewayDao gatewayDao = gatewayRepository.findOne(gatewayId);
		
		if (gatewayDao == null) {
			LOGGER.error("gateway by id " + gatewayId + " is not found");
			throw new DataNotExistsException("Gateway not exist");
		}
		
		GatewayDto dto = new GatewayDto();
		dto.setId(String.valueOf(gatewayId));
		dto.setDescription(gatewayDao.getDescription());
		dto.setLastPollingDate(gatewayDao.getLastPollingDate());
		dto.setMacAddress(gatewayDao.getMacAddress());
		dto.setName(gatewayDao.getName());
		dto.setStatus(E_GatewayStatus.valueOf(gatewayDao.getStatus()));
		
		if (LOGGER.isDebugEnabled()) {
            LOGGER.debug( "Return find(long gatewayId) method for GatewayManagerImpl, with parameters :");
            final StringBuilder message = new StringBuilder( 100 );
            message.append( "id :" );
            message.append( String.valueOf(gatewayId) );
            message.append( "\n" );
            message.append( "macAddress :" );
            message.append( gatewayDao.getMacAddress() );
            message.append( "\n" );
            message.append( "name :" );
            message.append( gatewayDao.getName() );
            message.append( "\n" );
            LOGGER.debug( message.toString() );
        }
		
		List<RoomDao> roomsList = getRooms(gatewayId);
		if ((roomsList != null) && (!roomsList.isEmpty() )) { 
			dto.setRooms(roomsList);
			dto.setActivated(true);			
		} else {
			dto.setActivated(false);
		}
		
		return dto;
	}

	@Override
	@Transactional(readOnly=true)
	public GatewayDto findByMacAddress(String macAddress)  throws DataNotExistsException {
			try {
				GatewayDao gatewayDao = gatewayRepository.findByMacAddress(macAddress);
				
				GatewayDto dto = new GatewayDto();
				dto.setId(gatewayDao.getColumnId());
				dto.setDescription(gatewayDao.getDescription());
				dto.setLastPollingDate(gatewayDao.getLastPollingDate());
				dto.setMacAddress(macAddress);
				dto.setName(gatewayDao.getName());
				dto.setStatus(E_GatewayStatus.valueOf(gatewayDao.getStatus()));
				
				if (LOGGER.isDebugEnabled()) {
		            LOGGER.debug( "Return findByMacAddress(String macAddress) method for GatewayManagerImpl, with parameters :");
		            final StringBuilder message = new StringBuilder( 100 );
		            message.append( "id :" );
		            message.append( gatewayDao.getColumnId() );
		            message.append( "\n" );
		            message.append( "macAddress :" );
		            message.append( macAddress );
		            message.append( "\n" );
		            message.append( "name :" );
		            message.append( gatewayDao.getName() );
		            message.append( "\n" );
		            LOGGER.debug( message.toString() );
		        }
				
				List<RoomDao> roomsList = getRooms(gatewayDao.getId());
				if ((roomsList != null) && (!roomsList.isEmpty())) { 
					dto.setRooms(roomsList);
					dto.setActivated(true);			
				} else {
					dto.setActivated(false);
				}
				
				return dto;
	
			} catch(IncorrectResultSizeDataAccessException e ) {
				LOGGER.debug("gateway by macAddress " + macAddress + " is not found", e);
				LOGGER.error("gateway by macAddress " + macAddress + " is not found");
				throw new DataNotExistsException("Gateway not exist");
			}
	}

	@Override
	public GatewayDao save(GatewayDao gatewayDao) throws DataAlreadyExistsException {
		try {
			// Save GatewayDao
			return gatewayRepository.saveGateway(gatewayDao);
		} catch (DataIntegrityViolationException e) {
			LOGGER.debug("DataIntegrityViolationException in save() GatewayManagerImpl with message :" + e.getMessage(), e);
			LOGGER.error("DataIntegrityViolationException in save() GatewayManagerImpl with message :" + e.getMessage());
			throw new DataAlreadyExistsException("gateway already exist.");
		}
	}

	@Override
	public GatewayDao update(GatewayDao gatewayDao) throws DataNotExistsException {
		try {	
			gatewayRepository.findByMacAddress(gatewayDao.getMacAddress());
			// update GatewayDao
			return gatewayRepository.updateGateway(gatewayDao);
		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.debug("DataAccessException in update() GatewayManagerImpl with message :" + e.getMessage(), e);
			LOGGER.error("DataAccessException in update() GatewayManagerImpl with message :" + e.getMessage());
			throw new DataNotExistsException("Gateway not exist");
		}
	}

	@Override
	public GatewayCommand updateStatus(GatewayDao gatewayDao) throws DataNotExistsException {
		try {
			String status = gatewayDao.getStatus();
			
			String macAddress = gatewayDao.getMacAddress();
			GatewayDao gateway = gatewayRepository.findByMacAddress(macAddress);
			gatewayDao.setId(gateway.getId());
			
			// update Gateway Status
			gatewayDao.setCommand(gateway.getCommand()); // Don't change the present state in DB on column Command !!!
			gatewayRepository.updateGatewayStatus(gatewayDao);
			
			// update Gateway Alert
			Long gatewayId =gateway.getId();
			alertManager.updateGatewayAlert(gatewayId, status);
			

			// check room & room_stats states
			if ( status.equals(E_GatewayStatus.OFFLINE.toString()) || status.equals(E_GatewayStatus.ERROR_NO_USB_DEVICE.toString()) || status.equals(E_GatewayStatus.ERROR_FIFO_FILE.toString()) ) {
				// set all rooms of the Gateway at UNKNOWN Status & RoomStat room_info to UNOCCUPIED
				List<RoomDao> rooms = roomRepository.findByGatewayId(gatewayId);
				for (RoomDao roomDao : rooms) {
					// set Room Status to UNKNOWN
					LOGGER.info("RoomDao in gatewayManager updateStatus() is going to set RoomStatus to UNKNOWN, for room#" + roomDao.getName());
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
						LOGGER.debug("GatewayManager.updateStatus : There is no OCCUPIED roomStat with roomId #" + roomDao.getId(), e);
						LOGGER.info("GatewayManager.updateStatus : There is no OCCUPIED roomStat with roomId #" + roomDao.getId());
					}
				}
			} 
			
			// process commandGateway & teachin state
			String commandGateway = gateway.getCommand();
			return	processCommand(status, gatewayId, commandGateway);
			
		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.debug("gateway is not found", e);
			LOGGER.error("gateway is not found");
			throw new DataNotExistsException("Gateway not exist");
		}
	}

	@Override
	public void delete(String macAddress) throws DataNotExistsException, IntegrityViolationException {
		try {
			GatewayDao gateway = gatewayRepository.findByMacAddress(macAddress);
			try {
				AlertDao alert = alertRepository.findByGatewayId(gateway.getId());
				if (alert != null) {
					// delete alert
					alertRepository.delete(alert.getId());
					// delete gateway
					gatewayRepository.deleteByMacAddress(macAddress);
				}
			} catch(IncorrectResultSizeDataAccessException e ) {
				LOGGER.debug("gateway by macAddress " + macAddress + " has not alert", e);
				LOGGER.info("gateway by macAddress " + macAddress + " has not alert");
				// delete gateway
				gatewayRepository.deleteByMacAddress(macAddress);	
			}
		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.debug("gateway by macAddress " + macAddress + " is not found", e);
			LOGGER.error("gateway by macAddress " + macAddress + " is not found");
			throw new DataNotExistsException("Gateway not exist");
		} catch(DataIntegrityViolationException e ) {
			LOGGER.debug("GatewayManager.delete : Gateway associated to a room", e);
			LOGGER.error("GatewayManager.delete : Gateway associated to a room");
			throw new IntegrityViolationException("GatewayManager.delete : Gateway associated to a room");
		}
	}
	
	@Override
	public void updateOFFLINEGatewaysAlerts() {
		LOGGER.debug("Begin GatewayManager.updateOFFLINEGatewaysAlerts method.");
		List<GatewayDao> gateways = gatewayRepository.findAllGateways();
		for (GatewayDao gateway : gateways) {
			if ( E_GatewayStatus.OFFLINE.toString().equals(gateway.getStatus()) ||
					E_GatewayStatus.ERROR_FIFO_FILE.toString().equals(gateway.getStatus()) ||
							(E_GatewayStatus.ERROR_NO_USB_DEVICE.toString().equals(gateway.getStatus())))  {
				
				// update Gateway Alert
				Long gatewayId =gateway.getId();
				alertManager.updateGatewayAlert(gatewayId, gateway.getStatus());
				LOGGER.debug("alertManager.updateGatewayAlert is process for gateway :" + gatewayId);
			}
		}
		LOGGER.debug("End GatewayManager.updateOFFLINEGatewaysAlerts method.");
	}
	
	/**
	 * getRooms
	 * @param gatewayId
	 * @return
	 */
	@Transactional(readOnly=true)
	private List<RoomDao> getRooms(long gatewayId) {
		List<RoomDao> roomsDao =roomRepository.findByGatewayId(gatewayId);
		
		return roomsDao;
	}
	
	/**
	 * getSensors
	 * @param roomId
	 * @return
	 */
	@Transactional(readOnly=true)
	private List<SensorDao> getSensors(long roomId) {
		List<SensorDao> sensorsDao = sensorRepository.findByRoomId(roomId);
		
		return sensorsDao;
	}
	/**
	 * getOccupancyTimeOut
	 * @return
	 */
	@Transactional(readOnly=true)
	private Long getOccupancyTimeOut() {
		// get activeUsers
		ConfigurationDao occupancyTimeOut = configRepository.findByKey(E_ConfigurationKey.OCCUPANCY_TIMEOUT.toString());
		String occupancyTimeOutValueValue = occupancyTimeOut.getValue();
		return Long.valueOf(occupancyTimeOutValueValue);
	}
	
	/**
	 * processCommand
	 * @param gatewayStatus
	 */
	@Transactional
	private GatewayCommand processCommand(String gatewayStatus, Long gatewayId, String commandGateway) {
		
		LOGGER.debug( "Begin call processCommand method for GatewayEndpoint at: " + new Date() );
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "Call processCommand(String gatewayStatus, Long gatewayId, String commandGateway) method of GatewayEndpoint, with parameters :");
			final StringBuilder message = new StringBuilder( 1000 );
			message.append( "\n" );
			message.append( "gatewayStatus :" );
			message.append( gatewayStatus );
			message.append( "\n" );
			message.append( "gatewayId :" );
			message.append( gatewayId );
			message.append( "\n" );
			message.append( "commandGateway :" );
			message.append( commandGateway );
			message.append( "\n" );
			LOGGER.debug( message.toString() );
		}

		
		GatewayCommand commandToSendToGateway = new GatewayCommand();
		try {
			TeachinSensorDao teachin = teachinRepository.findByTeachinStatus();
			
			if (teachin.getGatewayId().intValue() == gatewayId) {
				LOGGER.info( "teachin.getGatewayId() is the same as gatewayId" );
				if (gatewayStatus.equals(E_GatewayStatus.ONTEACHIN.toString())) {
					// the teachin is founded (teachin_status not null)
					if (teachin.getTeachinStatus().equals(E_TeachinStatus.INITIALIZING.toString()))  {
						// update status to running
						teachin.setTeachinStatus(E_TeachinStatus.RUNNING.toString());
						teachinRepository.updateTeachinStatus(teachin);
						teachinRepository.updateTeachinDate(teachin);
						commandToSendToGateway.setRoomId(teachin.getRoomId());
						commandToSendToGateway.setCommand(EnumCommandModel.TEACHIN);
						LOGGER.info( "setted command in ONTEACHIN => INITIALIZING case, is :" + commandToSendToGateway.getCommand().toString() );
					} else if (teachin.getTeachinStatus().equals(E_TeachinStatus.RUNNING.toString())) { // status RUNNING
						commandToSendToGateway = setCommandToSend(gatewayStatus, gatewayId, commandGateway);
						LOGGER.info( "setted command in ONTEACHIN => RUNNING case, is :" + commandToSendToGateway.getCommand().toString() );
					} else if (teachin.getTeachinStatus().equals(E_TeachinStatus.ENDED.toString())) { // status ENDED
						commandToSendToGateway.setCommand(EnumCommandModel.STOPTEACHIN);
						LOGGER.info( "setted command in ONTEACHIN => ENDED case, is :" + commandToSendToGateway.getCommand().toString() );
					}
				} else {
					if (gatewayStatus.equals(E_GatewayStatus.ONLINE.toString())) {
						// the teachin is founded (teachin_status not null)
						if (teachin.getTeachinStatus().equals(E_TeachinStatus.INITIALIZING.toString()))  {
							if (commandGateway != null && commandGateway.equals(E_CommandModel.RESET.toString())) {
								LOGGER.info( "In ONLINE => INITIALIZING + RESET Command None" );
								commandToSendToGateway.setCommand(EnumCommandModel.NONE);
							} else {
								LOGGER.info( "In ONLINE => INITIALIZING state teachin will be send TEACHIN Command " );
								commandToSendToGateway.setRoomId(teachin.getRoomId());
								commandToSendToGateway.setCommand(EnumCommandModel.TEACHIN);
							}
							LOGGER.info( "setted command in ONLINE => INITIALIZING case, is :" + commandToSendToGateway.getCommand().toString() );
						} else if (teachin.getTeachinStatus().equals(E_TeachinStatus.RUNNING.toString()))  {
							// update status to ended
							teachin.setTeachinStatus(E_TeachinStatus.ENDED.toString());
							teachinRepository.updateTeachinStatus(teachin);
							commandToSendToGateway.setCommand(EnumCommandModel.NONE);
							LOGGER.info( "setted command in ONLINE => RUNNING case, is :" + commandToSendToGateway.getCommand().toString() );
						} else if (teachin.getTeachinStatus().equals(E_TeachinStatus.ENDED.toString()))  {
							commandToSendToGateway = setCommandToSend(gatewayStatus, gatewayId, commandGateway);
							LOGGER.info( "setted command in ONLINE => ENDED case, is :" + commandToSendToGateway.getCommand().toString() );
						} 		
					} else { // OFFLINE or ERRORs
						// the teachin is founded (teachin_status not null)
						if (teachin.getTeachinStatus().equals(E_TeachinStatus.INITIALIZING.toString()) || teachin.getTeachinStatus().equals(E_TeachinStatus.RUNNING.toString()))  {
							// update status to running
							teachin.setTeachinStatus(E_TeachinStatus.ENDED.toString());
							teachinRepository.updateTeachinStatus(teachin);
							// -----------------------------------------------------------------------------------------
							commandToSendToGateway = setCommandToSend(gatewayStatus, gatewayId, commandGateway);
							LOGGER.info( "setted command in OFFLINE or ERRORs => INITIALIZING or RUNNING case, is :" + commandToSendToGateway.getCommand().toString() );
							// -----------------------------------------------------------------------------------------
						} else if (teachin.getTeachinStatus().equals(E_TeachinStatus.ENDED.toString())) { // status ENDED
							// -----------------------------------------------------------------------------------------
							commandToSendToGateway = setCommandToSend(gatewayStatus, gatewayId, commandGateway);	
							// -----------------------------------------------------------------------------------------
							LOGGER.info( "setted command in OFFLINE or ERRORs => ENDED case, is :" + commandToSendToGateway.getCommand().toString() );
						}
					}
				}
			} else {
				LOGGER.debug( "teachin.getGatewayId() is not same as gatewayId" );
				if (gatewayStatus.equals(E_GatewayStatus.ONTEACHIN.toString())) {
					commandToSendToGateway.setCommand(EnumCommandModel.STOPTEACHIN);
					LOGGER.info( "setted command in ONTEACHIN => teachin founded but not same gateway case, is :" + commandToSendToGateway.getCommand().toString() );
				} else {
					// -----------------------------------------------------------------------------------------
					commandToSendToGateway = setCommandToSend(gatewayStatus, gatewayId, commandGateway);
					// -----------------------------------------------------------------------------------------
					LOGGER.info( "setted command in other then ONTEACHIN => teachin founded but not same gateway case, is :" + commandToSendToGateway.getCommand().toString() );
				}	
			}
			
		} catch(IncorrectResultSizeDataAccessException e ) {
			// Table teachin_sensors is empty
			if (gatewayStatus.equals(E_GatewayStatus.ONTEACHIN.toString())) {
				commandToSendToGateway.setCommand(EnumCommandModel.STOPTEACHIN);
				LOGGER.info( "setted command in ONTEACHIN => no teachin founded case, is :" + commandToSendToGateway.getCommand().toString() );
			} else {
				// -----------------------------------------------------------------------------------------
				commandToSendToGateway = setCommandToSend(gatewayStatus, gatewayId, commandGateway);
				// -----------------------------------------------------------------------------------------
				LOGGER.debug( "setted command in other then ONTEACHIN => no teachin founded case, is :" + commandToSendToGateway.getCommand().toString() );
			}

	    }
		
		LOGGER.debug( "End call processCommand method for GatewayEndpoint at: " + new Date() );
		
		return commandToSendToGateway;
	}
	
	/**
	 * setCommand
	 * @param gatewayStatus
	 * @param commandGateway
	 * @return
	 */
	@Transactional
	private GatewayCommand setCommandToSend(String gatewayStatus, Long gatewayId, String commandGateway) {
		GatewayCommand commandStateProcess = new GatewayCommand();
		
		if (gatewayStatus.equals(E_GatewayStatus.ONLINE.toString()) || gatewayStatus.equals(E_GatewayStatus.ONTEACHIN.toString())) {
			if (commandGateway != null && commandGateway.equals(E_CommandModel.RESET.toString())) {
				commandStateProcess.setCommand(EnumCommandModel.RESET);
				LOGGER.debug("RESET command has sent to gateway id #: " + gatewayId);
				// update Command colon for this Gateway "Delete REST state"
				GatewayDao gateway = new GatewayDao();
				gateway.setId(gatewayId);
				gatewayRepository.updateGatewayCommand(gateway);
				LOGGER.debug("The RESET state has deleted from gateway id #: " + gatewayId);
			} else {
				commandStateProcess.setCommand(EnumCommandModel.NONE);
			}
		} else {
			commandStateProcess.setCommand(EnumCommandModel.NONE);

			// Set status associated Rooms to UNKNOWN
			List<RoomDao> rooms = roomRepository.findByGatewayId(gatewayId);
			for (RoomDao room : rooms) {
				room.setStatus(E_RoomStatus.UNKNOWN.toString());
				room.setUserId(null);
				roomRepository.updateRoomStatus(room);
				// Set status associated Sensors to OFFLINE
				List<SensorDao> sensors = sensorRepository.findByRoomId(room.getId());
				for (SensorDao sensor : sensors) {
					sensor.setStatus(E_SensorStatus.OFFLINE.toString());
					sensorRepository.updateSensorStatus(sensor);
				}
			}
		}
		
		return commandStateProcess;
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
