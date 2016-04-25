package com.orange.flexoffice.business.common.service.data;

import java.util.List;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.dao.common.model.data.DashboardDao;
import com.orange.flexoffice.dao.common.model.object.DashboardDto;

/**
 * DashboardManager
 * @author oab
 *
 */
public interface DashboardManager {
	/**
	 * findAllDashboards method used by adminui
	 * @return
	 */
	List<DashboardDao> findAllDashboards();
	
	/**
	 * Finds a {@link DashboardDto} by its ID.
	 * method used by adminui
	 * @param dashboardId the {@link dashboardId} ID
	 * @return a {@link DashboardDto}
	 */
	DashboardDto find(long dashboardId)  throws DataNotExistsException;
	
	/**
	 * Finds a {@link DashboardDto} by its macAddress.
	 * method used by adminui
	 * @param macAddress the {@link dashboardId} macAddress
	 * @return a {@link DashboardDto}
	 */
	DashboardDto findByMacAddress(String macAddress)  throws DataNotExistsException;
	
	/**
	 * Saves a {@link DashboardDao}
	 * method used by adminui
	 * @param DashboardDao the new {@link DashboardDao}
	 * @return a saved {@link DashboardDao}
	 * @throws DataAlreadyExistsException 
	 */
	DashboardDao save(DashboardDao dashboardDao) throws DataAlreadyExistsException;

	/**
	 * Updates a {@link DashboardDao}
	 * method used by adminui
	 * @param DashboardDao the new {@link DashboardDao}
	 * @return a saved {@link DashboardDao}
	 */
	DashboardDao update(DashboardDao dashbaordDao) throws DataNotExistsException;

	/**
	 * Updates a {@link DashboardDao}
	 * method used by gatewayapi
	 * @param DashboardDao the new {@link DashboardDao}
	 * @return a command {@link DashbaordCommand}
	 */
	//AgentCommand updateStatus(AgentDao agentDao) throws DataNotExistsException;
	
	/**
	 * Deletes a {@link DashboardDto}.
	 * method used by adminui
	 * @param id a {@link DashboardDto} macAddress
	 */
	void delete(String macAddress) throws DataNotExistsException, IntegrityViolationException;
	
	/**
	 * When the web-server is restarted, I check if all OFFLINE Dashboards have an alert
	 * OtherWise I create one
	 */
	//void updateOFFLINEAgentsAlerts();
	
	
}