package com.orange.flexoffice.meetingroomapi.ws.endPoint.entity.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

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
	private TestManager testManager;
	@Autowired
	private ErrorMessageHandler errorMessageHandler;
	
	@Override
	public AgentOutput updateStatus(String macAddress, AgentInput agent) {
		AgentOutput returnedAgent = factory.createAgentOutput();
		try {
			AgentDao agentDao = new AgentDao();
			agentDao.setMacAddress(macAddress);
			agentDao.setStatus(agent.getAgentStatus().toString());
			AgentDao agentUpdated = agentManager.updateStatus(agentDao);
			
			ECommandModel command = ECommandModel.valueOf(agentUpdated.getCommand());
			returnedAgent.setCommand(command);
			
			MeetingRoomDto meetingRoomDto = meetingRoomManager.find(agentUpdated.getMeetingroomId());
			returnedAgent.setMeetingRoomExternalId(meetingRoomDto.getExternalId());
			
			return factory.createAgentOutput(returnedAgent).getValue();
		} catch (DataNotExistsException e) {
			returnedAgent.setMeetingRoomExternalId("0");
			return factory.createAgentOutput(returnedAgent).getValue();
		}
	}
	
	/*@Override
	public GatewayDto findByMacAddress(String macAddress) throws DataNotExistsException {
		return gatewayManager.findByMacAddress(macAddress);
	}

	@Override
	public boolean executeInitTestFile() {
		return testManager.executeInitTestFile();
	}

	@Override
	public boolean initTeachinSensorsTable() {
		return testManager.initTeachinSensorsTable();
	}*/
	
}
