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
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.business.common.service.data.DashboardManager;
import com.orange.flexoffice.dao.common.model.data.AlertDao;
import com.orange.flexoffice.dao.common.model.data.ConfigurationDao;
import com.orange.flexoffice.dao.common.model.data.DashboardDao;
import com.orange.flexoffice.dao.common.model.data.RoomStatDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_ConfigurationKey;
import com.orange.flexoffice.dao.common.model.enumeration.E_DashboardStatus;
import com.orange.flexoffice.dao.common.model.object.DashboardDto;
import com.orange.flexoffice.dao.common.repository.data.jdbc.AlertDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.ConfigurationDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.DashboardDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RoomStatDaoRepository;

/**
 * Manages {@link DashboardDto}.
 * For PROD LOG LEVEL is info then we say info & error logs.
 * 
 * @author oab
 */
@Service("DashboardManager")
@Transactional
public class DashboardManagerImpl implements DashboardManager {
	
	private static final Logger LOGGER = Logger.getLogger(DashboardManagerImpl.class);
	
	@Autowired
	private DashboardDaoRepository dashboardRepository;
	@Autowired
	private RoomStatDaoRepository roomStatRepository;
	@Autowired
	private AlertDaoRepository alertRepository;
	@Autowired
	private ConfigurationDaoRepository configRepository;
	
	@Override
	@Transactional(readOnly=true)
	public List<DashboardDao> findAllDashboards() {
		return dashboardRepository.findAllDashboards();
	}

	/*@Override
	@Transactional(readOnly=true)
	public MeetingRoomDto findAgentMeetingRoom(String agentMacAddress) {
		
		List<RoomDto> roomDtoList = new ArrayList<RoomDto>();
		
		GatewayDao gateway = agentRepository.findByMacAddress(agentMacAddress);
		
		List<RoomDao> roomsDao = roomRepository.findByGatewayId(gateway.getId());
		
		LOGGER.info("There is: " + roomsDao.size() + " rooms for gateway (macAddress) :" + agentMacAddress );
		
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
	}*/
	
	
	@Override
	@Transactional(readOnly=true)
	public DashboardDto find(long dashboardId)  throws DataNotExistsException {
		
		DashboardDao dashboardDao = dashboardRepository.findOne(dashboardId);
		
		if (dashboardDao == null) {
			LOGGER.error("dashboard by id " + dashboardId + " is not found");
			throw new DataNotExistsException("Dashboard not exist");
		}
		
		DashboardDto dto = new DashboardDto();
		dto.setId(String.valueOf(dashboardId));
		dto.setDescription(dashboardDao.getDescription());
		dto.setLastMeasureDate(dashboardDao.getLastMeasureDate());
		dto.setMacAddress(dashboardDao.getMacAddress());
		dto.setName(dashboardDao.getName());
		dto.setStatus(E_DashboardStatus.valueOf(dashboardDao.getStatus()));
		dto.setCityId(dashboardDao.getCityId());
		if (dashboardDao.getBuildingId() != null){
			dto.setBuildingId(dashboardDao.getBuildingId());
		}
		if (dashboardDao.getFloor() != null){
			dto.setFloor(dashboardDao.getFloor());
		}
		
		if (LOGGER.isDebugEnabled()) {
            LOGGER.debug( "Return find(long dashboardId) method for DashbaordManagerImpl, with parameters :");
            final StringBuilder message = new StringBuilder( 100 );
            message.append( "id :" );
            message.append( String.valueOf(dashboardId) );
            message.append( "\n" );
            message.append( "macAddress :" );
            message.append( dashboardDao.getMacAddress() );
            message.append( "\n" );
            message.append( "name :" );
            message.append( dashboardDao.getName() );
            message.append( "\n" );
            LOGGER.debug( message.toString() );
        }
		
		return dto;
	}

	@Override
	@Transactional(readOnly=true)
	public DashboardDto findByMacAddress(String macAddress)  throws DataNotExistsException {
			try {
				DashboardDao dashboardDao = dashboardRepository.findByMacAddress(macAddress);
				
				DashboardDto dto = new DashboardDto();
				dto.setId(dashboardDao.getColumnId());
				dto.setDescription(dashboardDao.getDescription());
				dto.setLastMeasureDate(dashboardDao.getLastMeasureDate());
				dto.setMacAddress(macAddress);
				dto.setName(dashboardDao.getName());
				dto.setStatus(E_DashboardStatus.valueOf(dashboardDao.getStatus()));
				dto.setCityId(dashboardDao.getCityId());
				if (dashboardDao.getBuildingId() != null){
					dto.setBuildingId(dashboardDao.getBuildingId());
				}
				if (dashboardDao.getFloor() != null){
					dto.setFloor(dashboardDao.getFloor());
				}
				
				if (LOGGER.isDebugEnabled()) {
		            LOGGER.debug( "Return findByMacAddress(String macAddress) method for DashboardManagerImpl, with parameters :");
		            final StringBuilder message = new StringBuilder( 100 );
		            message.append( "id :" );
		            message.append( dashboardDao.getColumnId() );
		            message.append( "\n" );
		            message.append( "macAddress :" );
		            message.append( macAddress );
		            message.append( "\n" );
		            message.append( "name :" );
		            message.append( dashboardDao.getName() );
		            message.append( "\n" );
		            LOGGER.debug( message.toString() );
		        }
				
				return dto;
	
			} catch(IncorrectResultSizeDataAccessException e ) {
				LOGGER.debug("dashboard by macAddress " + macAddress + " is not found", e);
				LOGGER.error("dashboard by macAddress " + macAddress + " is not found");
				throw new DataNotExistsException("Dashboard not exist");
			}
	}

	@Override
	public DashboardDao save(DashboardDao dashboardDao) throws DataAlreadyExistsException {
		try {
			// Save AgentDao
			return dashboardRepository.saveDashboard(dashboardDao);
		} catch (DataIntegrityViolationException e) {
			LOGGER.debug("DataIntegrityViolationException in save() DashboardManagerImpl with message :" + e.getMessage(), e);
			LOGGER.error("DataIntegrityViolationException in save() DashboardManagerImpl with message :" + e.getMessage());
			throw new DataAlreadyExistsException("dashboard already exists.");
		}
	}

	@Override
	public DashboardDao update(DashboardDao dashboardDao) throws DataNotExistsException {
		try {	
			dashboardRepository.findByMacAddress(dashboardDao.getMacAddress());
			// update DashboardDao
			return dashboardRepository.updateDashboard(dashboardDao);
		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.debug("DataAccessException in update() DashboardManagerImpl with message :" + e.getMessage(), e);
			LOGGER.error("DataAccessException in update() DashboardManagerImpl with message :" + e.getMessage());
			throw new DataNotExistsException("dashboard not exist");
		}
	}

	@Override
	public DashboardDao updateStatus(DashboardDao dashboardDao) throws DataNotExistsException {
		try {	
			DashboardDao agent = dashboardRepository.findByMacAddress(dashboardDao.getMacAddress());
			dashboardDao.setId(agent.getId());
			dashboardDao.setCommand(agent.getCommand());
			
			// update DashboardDao
			return dashboardRepository.updateDashboardStatus(dashboardDao);
		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.debug("DataAccessException in update() DashboardManagerImpl with message :" + e.getMessage(), e);
			LOGGER.error("DataAccessException in update() DashboardManagerImpl with message :" + e.getMessage());
			throw new DataNotExistsException("Dashboard not exist");
		}
	}

	@Override
	public void delete(String macAddress) throws DataNotExistsException, IntegrityViolationException {
		try {
			DashboardDao dashboard = dashboardRepository.findByMacAddress(macAddress);
			try {
				AlertDao alert = alertRepository.findByDashboardId(dashboard.getId());
				if (alert != null) {
					// delete alert
					alertRepository.delete(alert.getId());
					// delete dashboard
					dashboardRepository.deleteByMacAddress(macAddress);
				}
			} catch(IncorrectResultSizeDataAccessException e ) {
				LOGGER.debug("dashboard by macAddress " + macAddress + " has not alert", e);
				LOGGER.info("dashbaord by macAddress " + macAddress + " has not alert");
				// delete dashboard
				dashboardRepository.deleteByMacAddress(macAddress);
			}

		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.debug("dashboard by macAddress " + macAddress + " is not found", e);
			LOGGER.error("dashboard by macAddress " + macAddress + " is not found");
			throw new DataNotExistsException("Agent not exist");
		} catch(DataIntegrityViolationException e ) {
			LOGGER.debug("DashboardManager.delete : Dashboard associated to a meeting room", e);
			LOGGER.error("DashboardManager.delete : Dashboard associated to a meeting room");
			throw new IntegrityViolationException("DashboardManager.delete : Dashboard associated to a meeting room");
		}
	}
	
	/*@Override
	public void updateOFFLINEGatewaysAlerts() {
		LOGGER.debug("Begin GatewayManager.updateOFFLINEGatewaysAlerts method.");
		List<GatewayDao> gateways = agentRepository.findAllGateways();
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
	}*/
	

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
	/*@Transactional
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
	}*/
	
	/**
	 * setCommand
	 * @param gatewayStatus
	 * @param commandGateway
	 * @return
	 */
	/*@Transactional
	private GatewayCommand setCommandToSend(String gatewayStatus, Long gatewayId, String commandGateway) {
		GatewayCommand commandStateProcess = new GatewayCommand();
		
		if (gatewayStatus.equals(E_GatewayStatus.ONLINE.toString()) || gatewayStatus.equals(E_GatewayStatus.ONTEACHIN.toString())) {
			if (commandGateway != null && commandGateway.equals(E_CommandModel.RESET.toString())) {
				commandStateProcess.setCommand(EnumCommandModel.RESET);
				LOGGER.debug("RESET command has sent to gateway id #: " + gatewayId);
				// update Command colon for this Gateway "Delete REST state"
				GatewayDao gateway = new GatewayDao();
				gateway.setId(gatewayId);
				agentRepository.updateGatewayCommand(gateway);
				LOGGER.debug("The RESET state has deleted from gateway id #: " + gatewayId);
			} else {
				commandStateProcess.setCommand(EnumCommandModel.NONE);
			}
		} else {
			commandStateProcess.setCommand(EnumCommandModel.NONE);

			// Set status associated Rooms to UNKNOWN
			List<RoomDao> rooms = meetingroomRepository.findByGatewayId(gatewayId);
			for (RoomDao room : rooms) {
				room.setStatus(E_RoomStatus.UNKNOWN.toString());
				room.setUserId(null);
				meetingroomRepository.updateRoomStatus(room);
				// Set status associated Sensors to OFFLINE
				List<SensorDao> sensors = sensorRepository.findByRoomId(room.getId());
				for (SensorDao sensor : sensors) {
					sensor.setStatus(E_SensorStatus.OFFLINE.toString());
					sensorRepository.updateSensorStatus(sensor);
				}
			}
		}
		
		return commandStateProcess;
	}*/
	
	/**
	 * findByRoomId synchronized method
	 * @param data
	 * @return
	 */
	private synchronized RoomStatDao findByRoomId(RoomStatDao data) { 
	 return roomStatRepository.findbyRoomId(data);
	}
	
}
