package com.orange.flexoffice.meetingroomapi.ws.endPoint.entity.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.service.data.DashboardManager;
import com.orange.flexoffice.business.common.service.data.MeetingRoomGroupsConfigurationManager;
import com.orange.flexoffice.dao.common.model.data.DashboardDao;
import com.orange.flexoffice.meetingroomapi.ws.endPoint.entity.DashboardApiEndpoint;
import com.orange.flexoffice.meetingroomapi.ws.model.DashboardInput;
import com.orange.flexoffice.meetingroomapi.ws.model.DashboardOutput;
import com.orange.flexoffice.meetingroomapi.ws.model.ECommandModel;
import com.orange.flexoffice.meetingroomapi.ws.model.ObjectFactory;

/**
 * DashboardApiEndpointImpl
 * @author oab
 *
 */
public class DashboardApiEndpointImpl implements DashboardApiEndpoint {

	private static final Logger LOGGER = Logger.getLogger(DashboardApiEndpointImpl.class);
	
	private final ObjectFactory factory = new ObjectFactory();
	
	@Autowired
	private DashboardManager dashboardManager;
	@Autowired
	private MeetingRoomGroupsConfigurationManager meetinRoomGroupsConfigurationManager;

	@Override
	public List<String> getConfig(String macAddress) {
		return meetinRoomGroupsConfigurationManager.getConfigurationFiles(macAddress);
	}

	@Override
	public DashboardOutput updateStatus(String identifier, DashboardInput dashboard) {
		DashboardOutput returnedDashboard = factory.createDashboardOutput();
		try {
			DashboardDao dashboardDao = new DashboardDao();
			dashboardDao.setMacAddress(identifier);
			dashboardDao.setStatus(dashboard.getDashboardStatus().toString());
			DashboardDao dashboardUpdated = dashboardManager.updateStatus(dashboardDao);
			
			ECommandModel command = ECommandModel.valueOf(dashboardUpdated.getCommand());
			returnedDashboard.setCommand(command);
			
		} catch (DataNotExistsException e) {
			LOGGER.debug("Dashboard not existing");
		}
		return factory.createDashboardOutput(returnedDashboard).getValue();
	}

}
