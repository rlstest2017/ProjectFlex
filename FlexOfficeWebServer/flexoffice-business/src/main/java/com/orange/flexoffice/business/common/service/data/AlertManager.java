package com.orange.flexoffice.business.common.service.data;

/**
 * AlertManager
 * @author oab
 *
 */
public interface AlertManager {
	
	void updateGatewayAlert(Long gatewayId, String status);
	
	void updateSensorAlert(Long sensorId, String status);
	
}