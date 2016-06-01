package com.orange.flexoffice.business.common.service.data;

/**
 * AlertManager
 * @author oab
 *
 */
public interface AlertManager {
	
	void updateGatewayAlert(Long gatewayId, String status);
	
	void updateAgentAlert(Long agentId, String status);
	
	void updateDashboardAlert(Long dashboardId, String status);
	
	void updateSensorAlert(Long sensorId, String status);
	
}