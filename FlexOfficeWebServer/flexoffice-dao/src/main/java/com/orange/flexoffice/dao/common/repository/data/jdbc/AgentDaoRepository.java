package com.orange.flexoffice.dao.common.repository.data.jdbc;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.common.model.data.AgentDao;
import com.orange.flexoffice.dao.common.repository.data.AgentDaoOperations;
import com.orange.flexoffice.dao.common.repository.data.jdbc.metadata.AgentDaoMetadata;

/*---------------------------------------------------------------------------------------
Manage agents table

---------------------------------------------------------------------------------------*/

@Repository
public class AgentDaoRepository extends DataRepository<AgentDao> implements AgentDaoOperations {

	private static final Logger LOGGER = Logger.getLogger(AgentDaoRepository.class);
	
	public AgentDaoRepository() {
		super(AgentDao.class);
	}
	
	@Override
	public List<AgentDao> findAllAgents() {
		SqlParameterSource paramMap = new MapSqlParameterSource();
		return jdbcTemplate.query(
				findAllQuery, 
				paramMap, 
				new BeanPropertyRowMapper<AgentDao>(AgentDao.class)
			);
	}

	@Override
	public AgentDao findByAgentId(Long agentId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("columnId", agentId);
		AgentDao data = null;
		data =  jdbcTemplate.queryForObject(
				findByColumnIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<AgentDao>(AgentDao.class)
			);
		return data;
	}
	
	@Override
	public AgentDao findByMacAddress(String macAddress) throws IncorrectResultSizeDataAccessException {
		SqlParameterSource paramMap = new MapSqlParameterSource("macAddress", macAddress);
		AgentDao data = null;
		data = jdbcTemplate.queryForObject(
				findByMacAddressQuery, 
				paramMap, 
				new BeanPropertyRowMapper<AgentDao>(AgentDao.class)
			);
		return data;
	}
	
	@Override
	public AgentDao findByMeetingRoomId(Long meetingRoomId) throws IncorrectResultSizeDataAccessException {
		SqlParameterSource paramMap = new MapSqlParameterSource("meetingRoomId", meetingRoomId);
		AgentDao data = null;
		data = jdbcTemplate.queryForObject(
				findByMeetingRoomIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<AgentDao>(AgentDao.class)
			);
		return data;
	}
	
	@Override
	public AgentDao saveAgent(AgentDao data) throws DataIntegrityViolationException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(saveAgentQuery, paramBean, keyHolder);
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		
		return data;
	}
	
	@Override
	public AgentDao updateAgent(AgentDao data) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(updateAgentQuery, paramBean, keyHolder);
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		
		return data;
	}
	
	@Override
	public AgentDao updateAgentStatus(AgentDao data) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		LOGGER.debug("Before execute jdbcTemplate update() method");
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(updateAgentStatusQuery, paramBean, keyHolder);
		
		LOGGER.debug("After execute jdbcTemplate update() method");
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		
		return data;
	}
	
	@Override
	public AgentDao updateAgentMeetingRoomId(AgentDao data) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		LOGGER.debug("Before execute jdbcTemplate update() method");
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(updateAgentMeetingRoomIdQuery, paramBean, keyHolder);
		
		LOGGER.debug("After execute jdbcTemplate update() method");
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		
		return data;
	}
	
	/*@Override
	public AgentDao updateAgentCommand(AgentDao data) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		LOGGER.debug("Before execute jdbcTemplate update() method");
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(updateGatewayCommandQuery, paramBean, keyHolder);
		
		LOGGER.debug("After execute jdbcTemplate update() method");
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		
		return data;
	}*/
	
	@Override
	public void deleteByMacAddress(String macAddress) {
		SqlParameterSource paramMap = new MapSqlParameterSource("macAddress", macAddress);
		jdbcTemplate.update(deleteByMacAddressQuery, paramMap);
	}
	
	@Override
	protected String getTableName() {
		return AgentDaoMetadata.AGENT_TABLE_NAME;
	}

	@Override
	protected String getColumnColName() {
		return AgentDaoMetadata.AGENT_ID_COL;
	}

	@Override
	protected String getColName() {
		return AgentDaoMetadata.AGENT_NAME_COL;
	}

}
