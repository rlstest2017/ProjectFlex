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
import com.orange.meetingroom.connector.exception.MethodNotAllowedException;
import com.orange.meetingroom.connector.flexoffice.enums.EnumAgentStatus;
import com.orange.meetingroom.connector.flexoffice.model.request.AgentConnectorInput;
import com.orange.meetingroom.connector.flexoffice.model.response.AgentConnectorOutput;
import com.orange.meetingroom.gui.ws.endPoint.entity.AgentEndpoint;
import com.orange.meetingroom.gui.ws.model.AgentInput;
import com.orange.meetingroom.gui.ws.model.AgentOutput;
import com.orange.meetingroom.gui.ws.model.ECommandModel;
import com.orange.meetingroom.gui.ws.model.ObjectFactory;
import com.orange.meetingroom.gui.ws.utils.ErrorMessageHandler;

/**
 * AgentEndpointImpl
 * @author oab
 *
 */
public class AgentEndpointImpl implements AgentEndpoint {

	private static final Logger LOGGER = Logger.getLogger(AgentEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();
	
	@Autowired
	private FlexOfficeConnectorManager flexOfficeConnectorManager;
	@Autowired
	private ErrorMessageHandler errorMessageHandler;
	
	@Override
	public AgentOutput updateAgent(String macAddress, AgentInput agentInput ) {
		
		try {
			LOGGER.debug( "Begin call updateAgent(...) method for AgentEndpoint at: " + new Date() );
			
			AgentConnectorInput input = new AgentConnectorInput();
			input.setAgentMacAddress(macAddress);
			input.setAgentStatus(EnumAgentStatus.valueOf(agentInput.getAgentStatus().toString()));
			
			AgentConnectorOutput outputConnector = flexOfficeConnectorManager.updateAgentStatus(input);
			
			AgentOutput output = factory.createAgentOutput();
			
			output.setMeetingRoomExternalId(outputConnector.getMeetingRoomExternalId());
			output.setCommand(ECommandModel.valueOf(outputConnector.getCommand().toString()));
			
			LOGGER.debug( "End call updateAgent(...) method for AgentEndpoint at: " + new Date() );
			
			return factory.createAgentOutput(output).getValue();
			
		} catch (MethodNotAllowedException e1) {
			LOGGER.debug("RuntimeException in updateAgent() AgentEndpointImpl with message :" + e1.getMessage(), e1);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_3, Response.Status.METHOD_NOT_ALLOWED));
		} catch (DataNotExistsException e2) {
			LOGGER.debug("RuntimeException in updateAgent() AgentEndpointImpl with message :" + e2.getMessage(), e2);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_2, Response.Status.NOT_FOUND));
		} catch (FlexOfficeInternalServerException | MeetingRoomInternalServerException e) {
			LOGGER.debug("RuntimeException in updateAgent() AgentEndpointImpl with message :" + e.getMessage(), e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_1, Response.Status.INTERNAL_SERVER_ERROR));
		}
		
	}

}
