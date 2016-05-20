package com.orange.meetingroom.gui.ws.endPoint.entity.impl;

import java.util.Date;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.meetingroom.business.connector.FlexOfficeConnectorManager;
import com.orange.meetingroom.business.service.enums.EnumErrorModel;
import com.orange.meetingroom.connector.exception.DataNotExistsException;
import com.orange.meetingroom.connector.exception.FlexOfficeInternalServerException;
import com.orange.meetingroom.connector.exception.MeetingRoomInternalServerException;
import com.orange.meetingroom.connector.flexoffice.enums.EnumDashboardStatus;
import com.orange.meetingroom.connector.flexoffice.model.request.DashboardConnectorInput;
import com.orange.meetingroom.connector.flexoffice.model.response.DashboardConnectorOutput;
import com.orange.meetingroom.gui.ws.endPoint.entity.DashboardEndpoint;
import com.orange.meetingroom.gui.ws.model.DashboardInput;
import com.orange.meetingroom.gui.ws.model.DashboardOutput;
import com.orange.meetingroom.gui.ws.model.ECommandModel;
import com.orange.meetingroom.gui.ws.model.ObjectFactory;
import com.orange.meetingroom.gui.ws.utils.ErrorMessageHandler;

/**
 * DashboardEndpointImpl
 * @author oab
 *
 */
public class DashboardEndpointImpl implements DashboardEndpoint {

	private static final Logger LOGGER = Logger.getLogger(DashboardEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();

	@Autowired
	private FlexOfficeConnectorManager flexOfficeConnectorManager;
	@Autowired
	private ErrorMessageHandler errorMessageHandler;
	
	@Override
	public DashboardOutput updateDashboard(String macAddress, DashboardInput dashboardInput) {
		try {
			LOGGER.debug( "Begin call updateDashboard(...) method for DashboardEndpoint at: " + new Date() );
			
			DashboardConnectorInput input = new DashboardConnectorInput();
			input.setDashboardStatus(EnumDashboardStatus.valueOf(dashboardInput.getDashboardStatus().toString()));
			input.setDashboardMacAddress(macAddress);
			
			DashboardConnectorOutput outputConnector = flexOfficeConnectorManager.updateDashboardStatus(input);
			
			DashboardOutput output = factory.createDashboardOutput();
			output.setCommand(ECommandModel.valueOf(outputConnector.getCommand().toString()));
			
			LOGGER.debug( "End call updateDashboard(...) method for DashboardEndpoint at: " + new Date() );
			
			return factory.createDashboardOutput(output).getValue();
			
		} catch (DataNotExistsException e2) {
			LOGGER.debug("RuntimeException in updateDashboard() DashboardEndpointImpl with message :" + e2.getMessage(), e2);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_4, Response.Status.NOT_FOUND));
		} catch (FlexOfficeInternalServerException | MeetingRoomInternalServerException e) {
			LOGGER.debug("RuntimeException in updateDashboard() DashboardEndpointImpl with message :" + e.getMessage(), e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_1, Response.Status.INTERNAL_SERVER_ERROR));
		}

	}
	

}
