package com.orange.flexoffice.meetingroomapi.ws.endPoint.entity.impl;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.business.common.enums.EnumErrorModel;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.service.data.AgentManager;
import com.orange.flexoffice.business.common.service.data.MeetingRoomManager;
import com.orange.flexoffice.business.common.service.data.TestManager;
import com.orange.flexoffice.dao.common.model.data.AgentDao;
import com.orange.flexoffice.dao.common.model.object.MeetingRoomDto;
import com.orange.flexoffice.meetingroomapi.ws.endPoint.entity.AgentApiEndpoint;
import com.orange.flexoffice.meetingroomapi.ws.model.AgentInput;
import com.orange.flexoffice.meetingroomapi.ws.model.AgentOutput;
import com.orange.flexoffice.meetingroomapi.ws.model.ECommandModel;
import com.orange.flexoffice.meetingroomapi.ws.model.ObjectFactory;
import com.orange.flexoffice.meetingroomapi.ws.utils.ErrorMessageHandler;

/**
 * AgentApiEndpointImpl
 * 
 * @author oab
 *
 */
public class AgentApiEndpointImpl implements AgentApiEndpoint {
	
	private static final Logger LOGGER = Logger.getLogger(AgentApiEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();
	
	@Autowired
	private AgentManager agentManager;
	@Autowired
	private MeetingRoomManager meetingRoomManager;
	@Autowired
	private ErrorMessageHandler errorMessageHandler;
	@Autowired
	private TestManager testManager;
	
	@Override
	public AgentOutput updateStatus(String macAddress, AgentInput agent) throws DataNotExistsException {
		AgentOutput returnedAgent = factory.createAgentOutput();
		AgentDao agentUpdated = new AgentDao();
		try {
			AgentDao agentDao = new AgentDao();
			agentDao.setMacAddress(macAddress);
			agentDao.setStatus(agent.getAgentStatus().toString());
			agentUpdated = agentManager.updateStatus(agentDao);
			
		} catch(DataNotExistsException e){
			LOGGER.debug("IncorrectResultSizeDataAccessException (agentMacAddress not found) in updateStatus() AgentApiEndpointImpl with message :" + e.getMessage(), e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_61, Response.Status.NOT_FOUND));
		} catch (RuntimeException ex) {
			LOGGER.debug("RuntimeException in updateStatus() AgentApiEndpointImpl with message :" + ex.getMessage(), ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}
		
		try{
			
			ECommandModel command = ECommandModel.valueOf(agentUpdated.getCommand());
			returnedAgent.setCommand(command);
			
			MeetingRoomDto meetingRoomDto = meetingRoomManager.find(agentUpdated.getMeetingroomId());
			returnedAgent.setMeetingRoomExternalId(meetingRoomDto.getExternalId());
			
			return factory.createAgentOutput(returnedAgent).getValue();
		} catch (DataNotExistsException e) {
			LOGGER.debug("DataNotExistsException in updateStatus() (agentMacAddress not paired to a mmeting room) AgentApiEndpointImpl with message :" + e.getMessage(), e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_61, Response.Status.METHOD_NOT_ALLOWED));
		} catch (RuntimeException ex) {
			LOGGER.debug("RuntimeException in updateStatus() AgentApiEndpointImpl with message :" + ex.getMessage(), ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}
	}

	@Override
	public boolean executeInitTestFile() {
		return testManager.executeInitTestFile();
	}
}
