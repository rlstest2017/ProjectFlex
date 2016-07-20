package com.orange.flexoffice.business.common.service.data;

import java.util.List;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.dao.common.model.data.AgentDao;
import com.orange.flexoffice.dao.common.model.object.AgentDto;

/**
 * AgentManager
 * @author oab
 *
 */
public interface AgentManager {
	/**
	 * findAllAgents method used by adminui
	 * @return
	 */
	List<AgentDao> findAllAgents();
	
	/**
	 * Finds a {@link AgentDto} by its ID.
	 * method used by adminui
	 * @param agentId the {@link agentId} ID
	 * @return a {@link AgentDto}
	 */
	AgentDto find(long agentId)  throws DataNotExistsException;
	
	/**
	 * Finds a {@link AgentDto} by its macAddress.
	 * method used by adminui
	 * @param macAddress the {@link agentId} macAddress
	 * @return a {@link AgentDto}
	 */
	AgentDto findByMacAddress(String macAddress)  throws DataNotExistsException;
	
	/**
	 * Finds a {@link AgentDto} by its macAddress.
	 * method used by adminui
	 * @param macAddress the {@link agentId} macAddress
	 * @return a {@link AgentDto}
	 */
	AgentDto findByMacAddressWithoutMeetingRoomInfo(String macAddress)  throws DataNotExistsException;
	
	/**
	 * Saves a {@link AgentDao}
	 * method used by adminui
	 * @param AgentDao the new {@link AgentDao}
	 * @return a saved {@link AgentDao}
	 * @throws DataAlreadyExistsException 
	 */
	AgentDao save(AgentDao agentDao) throws DataAlreadyExistsException;

	/**
	 * Updates a {@link AgentDao}
	 * method used by adminui
	 * @param AgentDao the new {@link AgentDao}
	 * @return a saved {@link AgentDao}
	 */
	AgentDao update(AgentDao agentDao) throws DataNotExistsException;

	/**
	 * Updates a {@link AgentDao}
	 * method used by meetingroomapi
	 * @param AgentDao the new {@link AgentDao}
	 * @return a command {@link AgentCommand}
	 */
	AgentDao updateStatus(AgentDao agentDao) throws DataNotExistsException;
	
	/**
	 * Deletes a {@link AgentDto}.
	 * method used by adminui
	 * @param id a {@link AgentDto} macAddress
	 */
	void delete(String macAddress) throws DataNotExistsException, IntegrityViolationException;
	
	/**
	 * When the web-server is restarted, I check if all OFFLINE Agents have an alert
	 * OtherWise I create one
	 */
	//void updateOFFLINEAgentsAlerts();
	
	
}