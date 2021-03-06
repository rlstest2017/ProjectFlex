package com.orange.flexoffice.dao.common.repository.data.jdbc;

import java.util.List;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.common.model.data.AlertDao;
import com.orange.flexoffice.dao.common.repository.data.AlertDaoOperations;
import com.orange.flexoffice.dao.common.repository.data.jdbc.metadata.AlertDaoMetadata;

@Repository
public class AlertDaoRepository extends DataRepository<AlertDao> implements AlertDaoOperations {

	public AlertDaoRepository() {
		super(AlertDao.class);
	}

	@Override
	public List<AlertDao> findAllAlerts() {
		SqlParameterSource paramMap = new MapSqlParameterSource();
		return jdbcTemplate.query(
				findAllQuery, 
				paramMap, 
				new BeanPropertyRowMapper<AlertDao>(AlertDao.class)
			);
	}

	@Override
	public AlertDao saveAlert(AlertDao data) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(saveAlertQuery, paramBean, keyHolder);
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		
		return data;
	}

	@Override
	public AlertDao updateAlert(AlertDao data) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(updateAlertQuery, paramBean, keyHolder);
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		
		return data;
	}
	
	@Override
	public AlertDao findByGatewayId(Long gatewayId) throws IncorrectResultSizeDataAccessException {
		SqlParameterSource paramMap = new MapSqlParameterSource("gatewayId", gatewayId);
		AlertDao data = null;
		data =  jdbcTemplate.queryForObject(
				findByColumnGatewayIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<AlertDao>(AlertDao.class)
			);
		return data;
	}
	
	@Override
	public AlertDao findByAgentId(Long agentId) throws IncorrectResultSizeDataAccessException {
		SqlParameterSource paramMap = new MapSqlParameterSource("agentId", agentId);
		AlertDao data = null;
		data =  jdbcTemplate.queryForObject(
				findByColumnAgentIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<AlertDao>(AlertDao.class)
			);
		return data;
	}
	
	@Override
	public AlertDao findByDashboardId(Long dashboardId) throws IncorrectResultSizeDataAccessException {
		SqlParameterSource paramMap = new MapSqlParameterSource("dashboardId", dashboardId);
		AlertDao data = null;
		data =  jdbcTemplate.queryForObject(
				findByColumnDashboardIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<AlertDao>(AlertDao.class)
			);
		return data;
	}
	
	@Override
	public AlertDao findBySensorId(Long sensorId) throws IncorrectResultSizeDataAccessException {
		SqlParameterSource paramMap = new MapSqlParameterSource("sensorId", sensorId);
		AlertDao data = null;
		data =  jdbcTemplate.queryForObject(
				findByColumnSensorIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<AlertDao>(AlertDao.class)
			);
		return data;
	}

	@Override
	public void deleteAlertByGatewayId(Long gatewayId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("gatewayId", gatewayId);
		jdbcTemplate.update(deleteByGatewayIdQuery, paramMap);
	}
	
	@Override
	public void deleteAlertByAgentId(Long agentId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("agentId", agentId);
		jdbcTemplate.update(deleteByAgentIdQuery, paramMap);
	}
	
	@Override
	public void deleteAlertByDashboardId(Long dashboardId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("dashboardId", dashboardId);
		jdbcTemplate.update(deleteByDashboardIdQuery, paramMap);
	}

	@Override
	public void deleteAlertBySensorId(Long sensorId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("sensorId", sensorId);
		jdbcTemplate.update(deleteBySensorIdQuery, paramMap);
	}

	@Override
	protected String getTableName() {
		return AlertDaoMetadata.ALERT_TABLE_NAME;
	}

	@Override
	protected String getColumnColName() {
		return AlertDaoMetadata.ALERT_ID_COL;
		
	}

	@Override
	protected String getColName() {
		return AlertDaoMetadata.ALERT_NAME_COL;
	}

}
