package com.orange.flexoffice.dao.common.repository.data;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.orange.flexoffice.dao.common.model.data.DashboardDao;

/**
 * DashboardDaoOperations
 * @author oab
 *
 */
public interface DashboardDaoOperations {
	
	List<DashboardDao> findAllDashboards();
	
	DashboardDao findByDashboardId(Long dashboardId);
	
	DashboardDao findByMacAddress(String macAddress) throws EmptyResultDataAccessException;
	
	List<DashboardDao> findDashboardsInTimeout(String intervalTimeout);
	
	DashboardDao updateDashboardStatus(DashboardDao data);
	
	DashboardDao updateDashboardStatusForTimeout(DashboardDao data);
	
	DashboardDao saveDashboard(DashboardDao data) throws DataIntegrityViolationException;
	
	DashboardDao updateDashboard(DashboardDao data);
	
	void deleteByMacAddress(String macAddress); 
}
