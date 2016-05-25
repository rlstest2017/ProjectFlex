package com.orange.flexoffice.meetingroomapi.ws.endPoint.entity.impl;

import java.util.Date;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.business.common.enums.EnumErrorModel;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.service.data.DashboardManager;
import com.orange.flexoffice.business.common.service.data.MeetingRoomGroupsConfigurationManager;
import com.orange.flexoffice.business.common.service.data.TestManager;
import com.orange.flexoffice.dao.common.model.data.DashboardDao;
import com.orange.flexoffice.meetingroomapi.ws.endPoint.entity.DashboardApiEndpoint;
import com.orange.flexoffice.meetingroomapi.ws.model.DashboardInput;
import com.orange.flexoffice.meetingroomapi.ws.model.DashboardOutput;
import com.orange.flexoffice.meetingroomapi.ws.model.ECommandModel;
import com.orange.flexoffice.meetingroomapi.ws.model.ObjectFactory;
import com.orange.flexoffice.meetingroomapi.ws.utils.ErrorMessageHandler;

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
	@Autowired
	private ErrorMessageHandler errorMessageHandler;
	@Autowired
	private TestManager testManager;

	@Override
	public List<String> getConfig(String macAddress) {
		LOGGER.debug( "Begin call DashboardApiEndpointImpl.getConfig at: " + new Date() );
		
		try {
			
			LOGGER.debug( "End call DashboardApiEndpointImpl.getConfig  at: " + new Date() );
			
			return meetinRoomGroupsConfigurationManager.getConfigurationFiles(macAddress);
		
		} catch (DataNotExistsException e) {
			LOGGER.debug("DataNotExistsException in getConfig() DashboardApiEndpointImpl with message :" + e.getMessage(), e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_66, Response.Status.NOT_FOUND));
		} catch (RuntimeException ex) {
			LOGGER.debug("RuntimeException in getConfig() DashboardApiEndpointImpl with message :" + ex.getMessage(), ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}
	}

	@Override
	public DashboardOutput updateStatus(String identifier, DashboardInput dashboard) {
		LOGGER.debug( "Begin call DashboardApiEndpointImpl.updateStatus at: " + new Date() );
		
		DashboardOutput returnedDashboard = factory.createDashboardOutput();
		try {
			DashboardDao dashboardDao = new DashboardDao();
			dashboardDao.setMacAddress(identifier);
			dashboardDao.setStatus(dashboard.getDashboardStatus().toString());
			DashboardDao dashboardUpdated = dashboardManager.updateStatus(dashboardDao);
			
			ECommandModel command = ECommandModel.valueOf(dashboardUpdated.getCommand());
			returnedDashboard.setCommand(command);
			
			LOGGER.debug( "End call DashboardApiEndpointImpl.updateStatus  at: " + new Date() );
			
			return factory.createDashboardOutput(returnedDashboard).getValue();
		} catch (DataNotExistsException e) {
			LOGGER.debug("DataNotExistsException in updateStatus() DashboardApiEndpointImpl with message :" + e.getMessage(), e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_66, Response.Status.NOT_FOUND));
		} catch (RuntimeException ex) {
			LOGGER.debug("RuntimeException in updateStatus() DashboardApiEndpointImpl with message :" + ex.getMessage(), ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}
		
	}
	
	@Override
	public boolean executeInitTestFile() {
		return testManager.executeInitTestFile();
	}

}
