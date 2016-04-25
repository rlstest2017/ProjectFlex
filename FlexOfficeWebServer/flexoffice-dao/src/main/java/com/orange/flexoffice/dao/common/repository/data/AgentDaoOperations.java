package com.orange.flexoffice.dao.common.repository.data;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import com.orange.flexoffice.dao.common.model.data.AgentDao;

/**
 * AgentDaoOperations
 * @author oab
 *
 */
public interface AgentDaoOperations {
	
	List<AgentDao> findAllAgents();
	
	AgentDao findByAgentId(Long agentId);
	
	AgentDao findByMacAddress(String macAddress) throws IncorrectResultSizeDataAccessException;
	
	AgentDao updateAgentStatus(AgentDao data);
	
	AgentDao updateAgentMeetingRoomId(AgentDao data);
	
	//AgentDao updateGatewayCommand(AgentDao data);
	
	AgentDao saveAgent(AgentDao data) throws DataIntegrityViolationException;
	
	AgentDao updateAgent(AgentDao data);
	
	void deleteByMacAddress(String macAddress); 
}
