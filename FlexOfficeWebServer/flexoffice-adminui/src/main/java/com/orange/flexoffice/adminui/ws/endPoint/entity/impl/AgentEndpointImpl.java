package com.orange.flexoffice.adminui.ws.endPoint.entity.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.adminui.ws.endPoint.entity.AgentEndpoint;
import com.orange.flexoffice.adminui.ws.model.Agent;
import com.orange.flexoffice.adminui.ws.model.AgentInput;
import com.orange.flexoffice.adminui.ws.model.AgentInput2;
import com.orange.flexoffice.adminui.ws.model.AgentOutput;
import com.orange.flexoffice.adminui.ws.model.AgentSummary;
import com.orange.flexoffice.adminui.ws.model.EAgentStatus;
import com.orange.flexoffice.adminui.ws.model.ECommandModel;
import com.orange.flexoffice.adminui.ws.model.MeetingRoomOutput;
import com.orange.flexoffice.adminui.ws.model.ObjectFactory;
import com.orange.flexoffice.adminui.ws.utils.ErrorMessageHandler;
import com.orange.flexoffice.business.common.enums.EnumErrorModel;
import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.business.common.service.data.AgentManager;
import com.orange.flexoffice.business.common.service.data.TestManager;
import com.orange.flexoffice.dao.common.model.data.AgentDao;
import com.orange.flexoffice.dao.common.model.data.MeetingRoomDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_AgentStatus;
import com.orange.flexoffice.dao.common.model.enumeration.E_CommandModel;
import com.orange.flexoffice.dao.common.model.object.AgentDto;


public class AgentEndpointImpl implements AgentEndpoint {
	
	private static final Logger LOGGER = Logger.getLogger(AgentEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();
		
	@Autowired
	private AgentManager agentManager;
	@Autowired
	private TestManager testManager;
	@Autowired
	private ErrorMessageHandler errorMessageHandler;
	
	@Override
	public List<AgentSummary> getAgents() {
		LOGGER.debug( "Begin call AgentEndpointImpl.getAgents at: " + new Date() );
		
		try {	
			List<AgentDao> dataList = agentManager.findAllAgents();
			
			if (dataList == null) {
				throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_59, Response.Status.NOT_FOUND));
			}
			
			LOGGER.debug("List of agents : " + dataList.size());
			
			List<AgentSummary> agentList = new ArrayList<AgentSummary>();
			
			for (AgentDao agentDao : dataList) {
				AgentSummary agent = factory.createAgentSummary();
				agent.setMacAddress(agentDao.getMacAddress());
				agent.setName(agentDao.getName());
				agent.setMeetingroomId(agentDao.getMeetingroomId().toString());
				if (agentDao.getStatus().equals(E_AgentStatus.ONLINE.toString())) {
					agent.setStatus(EAgentStatus.ONLINE);
				} else if (agentDao.getStatus().equals(E_AgentStatus.OFFLINE.toString())) {
					agent.setStatus(EAgentStatus.OFFLINE);
				} else if (agentDao.getStatus().equals(E_AgentStatus.STANDBY.toString())){
					agent.setStatus(EAgentStatus.STANDBY);
				} else {
					agent.setStatus(EAgentStatus.ECONOMIC);
				}
				if (agentDao.getLastMeasureDate() != null) {
					agent.setLastMeasureDate(agentDao.getLastMeasureDate().getTime());
				}
				
				agentList.add(agent);
			}
			
			LOGGER.debug( "End call AgentEndpointImpl.getAgents  at: " + new Date() );
			
			return agentList;
		} catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in getAgents() AgentEndpointImpl with message :" + ex.getMessage(), ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}
	}

	@Override
	public Agent getAgent(String macAddress) {
		LOGGER.debug( "Begin call AgentEndpointImpl.getAgent at: " + new Date() );
		
		try {
			AgentDto data = agentManager.findByMacAddress(macAddress.toLowerCase().replaceAll("-", ":"));
			
			Agent agent = factory.createAgent();
			agent.setId(data.getId());
			agent.setMacAddress(data.getMacAddress());
			agent.setName(data.getName());
			agent.setDesc(data.getDescription());
			if (data.getStatus().equals(E_AgentStatus.ONLINE.toString())) {
				agent.setStatus(EAgentStatus.ONLINE);
			} else if (data.getStatus().equals(E_AgentStatus.OFFLINE.toString())) {
				agent.setStatus(EAgentStatus.OFFLINE);
			} else if(data.getStatus().equals(E_AgentStatus.STANDBY.toString())){
				agent.setStatus(EAgentStatus.STANDBY);
			} else {
				agent.setStatus(EAgentStatus.ECONOMIC);
			}
			
			if (data.getLastMeasureDate() != null) {
				agent.setLastMeasureDate(data.getLastMeasureDate().getTime());
			}
			
			if (data.getCommand() == E_CommandModel.NONE) {
				agent.setCommand(ECommandModel.NONE);
			} else if (data.getCommand() == E_CommandModel.ECONOMIC) {
				agent.setCommand(ECommandModel.ECONOMIC);
			} else if (data.getCommand()== E_CommandModel.ONLINE) {
				agent.setCommand(ECommandModel.ONLINE);
			} else if (data.getCommand() == E_CommandModel.OFFLINE) {
				agent.setCommand(ECommandModel.OFFLINE);
			} else if (data.getCommand() == E_CommandModel.RESET) {
				agent.setCommand(ECommandModel.RESET);
			} else if (data.getCommand() == E_CommandModel.STANDBY) {
				agent.setCommand(ECommandModel.STANDBY);
			}
				
			MeetingRoomDao meetingroom = data.getMeetingRoom();
			
			if (meetingroom != null){
				MeetingRoomOutput rm = new MeetingRoomOutput();
				rm.setId(meetingroom.getColumnId());
				rm.setName(meetingroom.getName());
				agent.setMeetingroom(rm);
			}
			
			LOGGER.debug( "End call AgentEndpointImpl.getAgent  at: " + new Date() );
			
			return factory.createAgent(agent).getValue();
			
		} catch (DataNotExistsException e){
			LOGGER.debug("DataNotExistsException in getAgent() AgentEndpointImpl with message :" + e.getMessage(), e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_63, Response.Status.NOT_FOUND));
		} catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in getAgent() AgentEndpointImpl with message :" + ex.getMessage(), ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}
	}

	@Override
	public AgentOutput addAgent(AgentInput agent) {
		LOGGER.debug( "Begin call doPost method for AgentEndpoint at: " + new Date() );

		AgentDao agentDao = new AgentDao();
		agentDao.setMacAddress(agent.getMacAddress().toLowerCase().replaceAll("-", ":"));
		agentDao.setName(agent.getName().trim());
		agentDao.setDescription(agent.getDesc());

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "Begin call addAgent(AgentInput agent) method of AgentEndpoint, with parameters :");
			final StringBuilder message = new StringBuilder( 1000 );
			message.append( "\n" );
			message.append( "macAddress :" );
			message.append( agent.getMacAddress().toLowerCase().replaceAll("-", ":") );
			message.append( "name :" );
			message.append( agent.getName() );
			message.append( "\n" );
			message.append( "desc :" );
			message.append( agent.getDesc() );
			message.append( "\n" );
			LOGGER.debug( message.toString() );
		}

		try {
			agentDao = agentManager.save(agentDao);

		} catch (DataAlreadyExistsException e) {
			LOGGER.debug("DataAlreadyExistsException in addAgent() AgentEndpointImpl with message :" + e.getMessage(), e);			
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_60, Response.Status.METHOD_NOT_ALLOWED));

		} catch (RuntimeException ex) {
			LOGGER.debug("RuntimeException in addAgent() AgentEndpointImpl with message :" + ex.getMessage(), ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}


		AgentOutput returnedAgent = factory.createAgentOutput();
		returnedAgent.setMacAddress(agentDao.getMacAddress());

		LOGGER.debug( "End call doPost method for AgentEndpoint at: " + new Date() );

		return factory.createAgentOutput(returnedAgent).getValue();

	}

	@Override
	public Response updateAgent(String macAddress, AgentInput2 agent) {
		LOGGER.debug( "Begin call doPut method for AgentEndpoint at: " + new Date() );

		AgentDao agentDao = new AgentDao();
		agentDao.setMacAddress(macAddress.toLowerCase().replaceAll("-", ":"));
		agentDao.setName(agent.getName().trim());
		agentDao.setDescription(agent.getDesc());
		agentDao.setCommand(agent.getCommand().toString());

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "Begin call updateAgent(String id, AgentInput2 agent) method of AgentEndpoint, with parameters :");
			final StringBuilder message = new StringBuilder( 1000 );
			message.append( "macAddress :" );
			message.append( macAddress.toLowerCase().replaceAll("-", ":") );
			message.append( "\n" );
			message.append( "name :" );
			message.append( agent.getName() );
			message.append( "\n" );
			message.append( "desc :" );
			message.append( agent.getDesc() );
			message.append( "\n" );
			LOGGER.debug( message.toString() );
		}

		try {
			agentDao = agentManager.update(agentDao);

		} catch (DataNotExistsException e) {
			LOGGER.debug("DataNotExistsException in updateAgent() AgentEndpointImpl with message :" + e.getMessage(), e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_61, Response.Status.NOT_FOUND));

		} catch (RuntimeException ex) {
			LOGGER.debug("RuntimeException in updateAgent() AgentEndpointImpl with message :" + ex.getMessage(), ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}


		AgentOutput returnedAgent = factory.createAgentOutput();
		returnedAgent.setMacAddress(agentDao.getMacAddress());

		LOGGER.debug( "End call doPut method for AgentEndpoint at: " + new Date() );

		return Response.status(Status.ACCEPTED).build();
	}

	@Override
	public Response removeAgent(String macAddress) {
		try {

			agentManager.delete(macAddress.toLowerCase().replaceAll("-", ":"));

		} catch (DataNotExistsException e){
			LOGGER.debug("DataNotExistsException in removeAgent() AgentEndpointImpl with message :" + e.getMessage(), e);			
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_62, Response.Status.NOT_FOUND));
		} catch (IntegrityViolationException e){
			LOGGER.debug("IntegrityViolationException in AgentEndpoint.removeAgent with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_62, Response.Status.METHOD_NOT_ALLOWED));
		} catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in removeAgent() AgentEndpointImpl with message :" + ex.getMessage(), ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}

		return Response.noContent().build();
	}

	@Override
	public boolean executeInitTestFile() {
		return testManager.executeInitTestFile();
	}

	@Override
	public AgentDto findByMacAddress(String macAddress) throws DataNotExistsException {
		return agentManager.findByMacAddress(macAddress.toLowerCase().replaceAll("-", ":"));
	}

	@Override
	public boolean executeDropTables() {
		return testManager.executeDropTables();
	}

		
	
}
