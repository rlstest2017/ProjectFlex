package com.orange.flexoffice.business.common.service.data.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.business.common.service.data.AgentManager;
import com.orange.flexoffice.business.common.service.data.AlertManager;
import com.orange.flexoffice.dao.common.model.data.AgentDao;
import com.orange.flexoffice.dao.common.model.data.AlertDao;
import com.orange.flexoffice.dao.common.model.data.MeetingRoomDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_AgentStatus;
import com.orange.flexoffice.dao.common.model.enumeration.E_CommandModel;
import com.orange.flexoffice.dao.common.model.object.AgentDto;
import com.orange.flexoffice.dao.common.repository.data.jdbc.AgentDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.AlertDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.ConfigurationDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.MeetingRoomDaoRepository;

/**
 * Manages {@link AgentDto}.
 * For PROD LOG LEVEL is info then we say info & error logs.
 * 
 * @author oab
 */
@Service("AgentManager")
@Transactional
public class AgentManagerImpl implements AgentManager {
	
	private static final Logger LOGGER = Logger.getLogger(AgentManagerImpl.class);
	
	@Autowired
	private AgentDaoRepository agentRepository;
	@Autowired
	private MeetingRoomDaoRepository meetingroomRepository;
	@Autowired
	private AlertDaoRepository alertRepository;
	@Autowired
	private ConfigurationDaoRepository configRepository;
	@Autowired
	private AlertManager alertManager;
	
	@Override
	@Transactional(readOnly=true)
	public List<AgentDao> findAllAgents() {
		return agentRepository.findAllAgents();
	}

	@Override
	@Transactional(readOnly=true)
	public AgentDto find(long agentId)  throws DataNotExistsException {
		
		AgentDao agentDao = agentRepository.findOne(agentId);
		
		if (agentDao == null) {
			LOGGER.error("agent by id " + agentId + " is not found");
			throw new DataNotExistsException("Agent not exist");
		}
		
		AgentDto dto = new AgentDto();
		dto.setId(String.valueOf(agentId));
		dto.setDescription(agentDao.getDescription());
		dto.setLastMeasureDate(agentDao.getLastMeasureDate());
		dto.setMacAddress(agentDao.getMacAddress());
		dto.setName(agentDao.getName());
		dto.setStatus(E_AgentStatus.valueOf(agentDao.getStatus()));
		
		if (LOGGER.isDebugEnabled()) {
            LOGGER.debug( "Return find(long agentId) method for AgentManagerImpl, with parameters :");
            final StringBuilder message = new StringBuilder( 100 );
            message.append( "id :" );
            message.append( String.valueOf(agentId) );
            message.append( "\n" );
            message.append( "macAddress :" );
            message.append( agentDao.getMacAddress() );
            message.append( "\n" );
            message.append( "name :" );
            message.append( agentDao.getName() );
            message.append( "\n" );
            LOGGER.debug( message.toString() );
        }
		
		MeetingRoomDao meetingroom = getMeetingRoom(agentId);
		if (meetingroom != null ) { 
			dto.setMeetingRoom(meetingroom);		
		}
		
		return dto;
	}

	@Override
	@Transactional(readOnly=true)
	public AgentDto findByMacAddress(String macAddress)  throws DataNotExistsException {
		try {
			AgentDao agentDao = agentRepository.findByMacAddress(macAddress);
			
			AgentDto dto = new AgentDto();
			dto.setId(agentDao.getColumnId());
			dto.setDescription(agentDao.getDescription());
			dto.setLastMeasureDate(agentDao.getLastMeasureDate());
			dto.setMacAddress(macAddress);
			dto.setName(agentDao.getName());
			dto.setStatus(E_AgentStatus.valueOf(agentDao.getStatus()));
			if (agentDao.getCommand() != null) {
				dto.setCommand(E_CommandModel.valueOf(agentDao.getCommand()));
			}
			
			if (LOGGER.isDebugEnabled()) {
	            LOGGER.debug( "Return findByMacAddress(String macAddress) method for AgentManagerImpl, with parameters :");
	            final StringBuilder message = new StringBuilder( 100 );
	            message.append( "id :" );
	            message.append( agentDao.getColumnId() );
	            message.append( "\n" );
	            message.append( "macAddress :" );
	            message.append( macAddress );
	            message.append( "\n" );
	            message.append( "name :" );
	            message.append( agentDao.getName() );
	            message.append( "\n" );
	            LOGGER.debug( message.toString() );
	        }
			try {
				MeetingRoomDao meetingroom = getMeetingRoom(agentDao.getId());
				if (meetingroom.getAgentId() != null || meetingroom != null) { 
					dto.setMeetingRoom(meetingroom);			
				}
			} catch(IncorrectResultSizeDataAccessException e) {
				LOGGER.debug("agent " + macAddress + " has none meeting room associated", e);
				return dto;
			} 
			
			return dto;
		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.debug("agent by macAddress " + macAddress + " is not found", e);
			LOGGER.error("agent by macAddress " + macAddress + " is not found");
			throw new DataNotExistsException("Agent not exist");
		}
	}
	
	@Override
	@Transactional(readOnly=true)
	public AgentDto findByMacAddressWithoutMeetingRoomInfo(String macAddress)  throws DataNotExistsException {
		try {
			AgentDao agentDao = agentRepository.findByMacAddress(macAddress);
			
			AgentDto dto = new AgentDto();
			dto.setId(agentDao.getColumnId());
			dto.setDescription(agentDao.getDescription());
			dto.setLastMeasureDate(agentDao.getLastMeasureDate());
			dto.setMacAddress(macAddress);
			dto.setName(agentDao.getName());
			dto.setStatus(E_AgentStatus.valueOf(agentDao.getStatus()));
			
			if (LOGGER.isDebugEnabled()) {
	            LOGGER.debug( "Return findByMacAddress(String macAddress) method for AgentManagerImpl, with parameters :");
	            final StringBuilder message = new StringBuilder( 100 );
	            message.append( "id :" );
	            message.append( agentDao.getColumnId() );
	            message.append( "\n" );
	            message.append( "macAddress :" );
	            message.append( macAddress );
	            message.append( "\n" );
	            message.append( "name :" );
	            message.append( agentDao.getName() );
	            message.append( "\n" );
	            LOGGER.debug( message.toString() );
	        }
			
			return dto;

		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.debug("agent by macAddress " + macAddress + " is not found", e);
			LOGGER.error("agent by macAddress " + macAddress + " is not found");
			throw new DataNotExistsException("Agent not exist");
		}
	}

	@Override
	public AgentDao save(AgentDao agentDao) throws DataAlreadyExistsException {
		try {
			// Save AgentDao
			return agentRepository.saveAgent(agentDao);
		} catch (DataIntegrityViolationException e) {
			LOGGER.debug("DataIntegrityViolationException in save() AgentManagerImpl with message :" + e.getMessage(), e);
			LOGGER.error("DataIntegrityViolationException in save() AgentManagerImpl with message :" + e.getMessage());
			throw new DataAlreadyExistsException("agent already exists.");
		}
	}

	@Override
	public AgentDao update(AgentDao agentDao) throws DataNotExistsException {
		try {	
			agentRepository.findByMacAddress(agentDao.getMacAddress());
			// update AgentDao
			return agentRepository.updateAgent(agentDao);
		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.debug("DataAccessException in update() AgentManagerImpl with message :" + e.getMessage(), e);
			LOGGER.error("DataAccessException in update() AgentManagerImpl with message :" + e.getMessage());
			throw new DataNotExistsException("Agent not exist");
		}
	}

	@Override
	public AgentDao updateStatus(AgentDao agentDao) throws DataNotExistsException {
		try {	
			AgentDao agent = agentRepository.findByMacAddress(agentDao.getMacAddress());
			agentDao.setId(agent.getId());
			agentDao.setMeetingroomId(agent.getMeetingroomId());
			agentDao.setName(agent.getName());
			agentDao.setDescription(agent.getDescription());
			if(agent.getCommand() == null){
				agentDao.setCommand(E_CommandModel.NONE.toString());
			} else {
				agentDao.setCommand(agent.getCommand());
			}
			
			// update Agent Alert
			Long agentId = agentDao.getId();
			alertManager.updateAgentAlert(agentId, agentDao.getStatus());
			
			// update AgentDao
			AgentDao agentToRet = agentRepository.updateAgentStatus(agentDao);
			
			// Set to NONE agent command after getting command to send 
			AgentDao agentUpdateCommand = new AgentDao();
			agentUpdateCommand.setMacAddress(agent.getMacAddress());
			agentUpdateCommand.setId(agent.getId());
			agentUpdateCommand.setMeetingroomId(agent.getMeetingroomId());
			agentUpdateCommand.setName(agent.getName());
			agentUpdateCommand.setDescription(agent.getDescription());
			agentUpdateCommand.setCommand(E_CommandModel.NONE.toString());
			agentRepository.updateAgent(agentUpdateCommand);
			
			return agentToRet;
		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.debug("DataAccessException in update() AgentManagerImpl with message :" + e.getMessage(), e);
			LOGGER.error("DataAccessException in update() AgentManagerImpl with message :" + e.getMessage());
			throw new DataNotExistsException("Agent " + agentDao.getMacAddress() + " not exist");
		}
	}

	@Override
	public void delete(String macAddress) throws DataNotExistsException, IntegrityViolationException {
		try {
			AgentDao agent = agentRepository.findByMacAddress(macAddress);
			try {
				AlertDao alert = alertRepository.findByAgentId(agent.getId());
				if (alert != null) {
					// delete alert
					alertRepository.delete(alert.getId());
					// delete agent
					agentRepository.deleteByMacAddress(macAddress);
				}
			} catch(IncorrectResultSizeDataAccessException e ) {
				LOGGER.debug("agent by macAddress " + macAddress + " has not alert", e);
				LOGGER.info("agent by macAddress " + macAddress + " has not alert");
				// delete agent
				agentRepository.deleteByMacAddress(macAddress);	
			}
		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.debug("agent by macAddress " + macAddress + " is not found", e);
			LOGGER.error("agent by macAddress " + macAddress + " is not found");
			throw new DataNotExistsException("Agent not exist");
		} catch(DataIntegrityViolationException e ) {
			LOGGER.debug("AgentManager.delete : Agent associated to a meeting room", e);
			LOGGER.error("AgentManager.delete : Agent associated to a meeting room");
			throw new IntegrityViolationException("AgentManager.delete : Agent associated to a meeting room");
		}
	}
	
	/**
	 * getMeetingRoom
	 * @param agentId
	 * @return
	 */
	@Transactional(readOnly=true)
	private MeetingRoomDao getMeetingRoom(long agentId) {
		MeetingRoomDao meetingroomDao = new MeetingRoomDao();
		meetingroomDao = meetingroomRepository.findByAgentId(agentId);
		
		if (meetingroomDao == null){
			return new MeetingRoomDao();
		}
		
		return meetingroomDao;
	}
}
