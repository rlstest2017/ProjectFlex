package com.orange.flexoffice.business.common.service.data.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.common.service.data.AlertManager;
import com.orange.flexoffice.dao.common.model.data.AlertDao;
import com.orange.flexoffice.dao.common.model.data.RoomDao;
import com.orange.flexoffice.dao.common.model.data.SensorDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_DeviceType;
import com.orange.flexoffice.dao.common.model.enumeration.E_GatewayStatus;
import com.orange.flexoffice.dao.common.model.enumeration.E_SensorStatus;
import com.orange.flexoffice.dao.common.repository.data.jdbc.AlertDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RoomDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.SensorDaoRepository;

/**
 * Manages 
 * 
 * @author oab
 */
@Service("AlertManager")
@Transactional
public class AlertManagerImpl implements AlertManager {
	
	private static final Logger LOGGER = Logger.getLogger(AlertManagerImpl.class);
	
	@Autowired
	private AlertDaoRepository alertRepository;

	@Autowired
	private RoomDaoRepository roomRepository;

	@Autowired
	private SensorDaoRepository sensorRepository;
	
	@Override
	public void updateGatewayAlert(Long gatewayId, String status) {
		
		LOGGER.debug ("Begin updateGatewayAlert with parameters : gatewayId "+gatewayId+" status: "+ status);
		
		if ((status.equals(E_GatewayStatus.ONLINE.toString())) || (status.equals(E_GatewayStatus.ONTEACHIN.toString()))) {
			LOGGER.debug ("updateGatewayAlert ONLINE/TEACHIN condition");
			alertRepository.deleteAlertByGatewayId(gatewayId);
		} else if (status.equals(E_GatewayStatus.OFFLINE.toString())) {  
			LOGGER.debug ("updateGatewayAlert OFFLINE condition");
			// if Gateway is appeared then declare error
				try {
					List<RoomDao> rooms = roomRepository.findByGatewayId(gatewayId);
					if ((rooms != null)&&(!rooms.isEmpty())) {
						LOGGER.debug ("updateGatewayAlert Gateway is associated to " + rooms.size() + " rooms.");
						setAlert(gatewayId, null, status);
					} else {
						alertRepository.deleteAlertByGatewayId(gatewayId);
					}
				} catch(IncorrectResultSizeDataAccessException e ) {
					LOGGER.debug("gateway by id " + gatewayId + " has not rooms", e);
					alertRepository.deleteAlertByGatewayId(gatewayId);
				}
		} else {
			LOGGER.debug ("updateGatewayAlert OTHER condition");
			setAlert(gatewayId, null, status);
		}
		
		LOGGER.debug ("End updateGatewayAlert");
	}

	@Override
	public void updateSensorAlert(Long sensorId, String status) {
		
		LOGGER.debug ("Begin updateSensorAlert with parameters : sensorId "+sensorId+" status: "+ status);
		
		if (status.equals(E_SensorStatus.ONLINE.toString())) { 
			LOGGER.debug ("updateSensorAlert ONLINE condition");
			alertRepository.deleteAlertBySensorId(sensorId); 
		} else if (status.equals(E_SensorStatus.OFFLINE.toString())) {
			LOGGER.debug ("updateSensorAlert OFFLINE condition");
			// if Sensor is appeared then declare error
			try {
				SensorDao sensor = sensorRepository.findOne(sensorId);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug( "SensorDao, with parameters :");
					final StringBuilder message = new StringBuilder( 100 );
					message.append( "\n" );
					message.append( "identifier :" );
					message.append( sensor.getIdentifier() );
					message.append( "roomId :" );
					message.append( sensor.getRoomId() );
					message.append( "\n" );
					message.append( "status :" );
					message.append( sensor.getStatus() );
					message.append( "\n" );
					LOGGER.debug( message.toString() );
				}
				if ((sensor!= null)&&(sensor.getRoomId()!= null)&&(sensor.getRoomId()!=0)) {
					LOGGER.debug ("updateSensorAlert Sensor appared to room");
					setAlert(null, sensorId, status);
				} else {
					LOGGER.debug ("updateSensorAlert Sensor not appared to room");
					alertRepository.deleteAlertBySensorId(sensorId);
				}
			} catch(IncorrectResultSizeDataAccessException e ) {
				LOGGER.debug("sensor by id " + sensorId + " is not appared", e);
				alertRepository.deleteAlertBySensorId(sensorId);
			}
		} else {
			LOGGER.debug ("updateSensorAlert OTHER condition");
			setAlert(null, sensorId, status);
		}
		
		LOGGER.debug ("End updateSensorAlert");
	}
	
	/**
	 * setAlert
	 * @param gatewayId
	 * @param sensorId
	 * @param status
	 */
	private void setAlert(Long gatewayId, Long sensorId, String status) {
		LOGGER.debug ("Begin setAlert");
		AlertDao alert = null;
		// if status exist in DB then no thing
		if (gatewayId != null) {
			try {
				alert = alertRepository.findByGatewayId(gatewayId);
				if (alert != null) {
					if ((alert.getType().equals(E_DeviceType.GATEWAY.toString()))&&(!alert.getName().equals(status))) {
						// update name & lastnotification
						alert.setName(status);
						alert.setLastNotification(new Date());
						alertRepository.updateAlert(alert);
					}
				} else {
					// save alert
					alert = new AlertDao();
					alert.setName(status);
					alert.setType(E_DeviceType.GATEWAY.toString());
					alertRepository.saveAlert(alert);
				}
			} catch(IncorrectResultSizeDataAccessException e ) {
				LOGGER.debug("gateway by id " + gatewayId + " has not rooms", e);
				// save alert
				alert = new AlertDao();
				alert.setName(status);
				alert.setType(E_DeviceType.GATEWAY.toString());
				alert.setGatewayId(gatewayId.intValue());
				alertRepository.saveAlert(alert);
			}
		} else if (sensorId != null) {
				try {
					alert = alertRepository.findBySensorId(sensorId);
					if (alert != null) {
						if ((alert.getType().equals(E_DeviceType.SENSOR.toString()))&&(!alert.getName().equals(status))) {
							// update name & lastnotification
							alert.setName(status);
							alert.setLastNotification(new Date());
							alertRepository.updateAlert(alert);
						}
					} else {
						// save alert
						alert = new AlertDao();
						alert.setName(status);
						alert.setType(E_DeviceType.SENSOR.toString());
						alertRepository.saveAlert(alert);
					}
				} catch(IncorrectResultSizeDataAccessException e ) {
					LOGGER.debug("gateway by id " + gatewayId + " has not rooms", e);
					// save alert
					alert = new AlertDao();
					alert.setName(status);
					alert.setType(E_DeviceType.SENSOR.toString());
					alert.setSensorId(sensorId.intValue());
					alertRepository.saveAlert(alert);
				}	

		}
		
		LOGGER.debug ("End setAlert");
	}
}
