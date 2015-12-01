package com.orange.flexoffice.business.common.service.data.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.common.service.data.AlertManager;
import com.orange.flexoffice.dao.common.model.enumeration.E_GatewayStatus;
import com.orange.flexoffice.dao.common.model.enumeration.E_SensorStatus;
import com.orange.flexoffice.dao.common.repository.data.jdbc.AlertDaoRepository;

/**
 * Manages 
 * 
 * @author oab
 */
@Service("AlertManager")
@Transactional
public class AlertManagerImpl implements AlertManager {
	
	@Autowired
	private AlertDaoRepository alertRepository;

	@Override
	public void updateGatewayAlert(Long gatewayId, String status) {
		
		if ((status.equals(E_GatewayStatus.ONLINE.toString())) || (status.equals(E_GatewayStatus.ONTEACHIN.toString()))) { 
			// TODO delete alerts for this gateway 
			
		} else if (status.equals(E_GatewayStatus.OFFLINE.toString())) {  
			// TODO if Gateway is appeared then declare error
				setAlert(gatewayId, null, status);
				
			// TODO else delete alerts for this gateway
				
		} else {
			setAlert(gatewayId, null, status);
		}
		
	}

	@Override
	public void updateSensorAlert(Long sensorId, String status) {
		if (status.equals(E_SensorStatus.ONLINE.toString())) { 
			// TODO delete alerts for this sensor 
			
		} else if (status.equals(E_SensorStatus.OFFLINE.toString())) {  
			// TODO if Sensor is appeared then declare error
				setAlert(null, sensorId, status);
				
			// TODO else delete alerts for this sensor
				
		} else {
			setAlert(null, sensorId, status);
		}
	}
	
	private void setAlert(Long gatewayId, Long sensorId, String status) {
		// TODO if status exist in DB then no thing
		// else save alert
	}
	
	
}
