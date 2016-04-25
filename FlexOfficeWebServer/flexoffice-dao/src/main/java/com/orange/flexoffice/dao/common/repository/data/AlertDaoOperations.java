package com.orange.flexoffice.dao.common.repository.data;

import java.util.List;

import org.springframework.dao.IncorrectResultSizeDataAccessException;

import com.orange.flexoffice.dao.common.model.data.AlertDao;

/**
 * AlertDaoOperations
 * @author oab
 *
 */
public interface AlertDaoOperations {
	
	List<AlertDao> findAllAlerts();
	
	AlertDao findByGatewayId(Long gatewayId) throws IncorrectResultSizeDataAccessException;
	
	AlertDao findByAgentId(Long agentId) throws IncorrectResultSizeDataAccessException;
	
	AlertDao findByDashboardId(Long dashboardId) throws IncorrectResultSizeDataAccessException;
	
	AlertDao findBySensorId(Long sensorId) throws IncorrectResultSizeDataAccessException;
	
	void deleteAlertByGatewayId(Long gatewayId);
	
	void deleteAlertBySensorId(Long sensorId);
	
	AlertDao saveAlert(AlertDao data);
	
	AlertDao updateAlert(AlertDao data);
}
