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

import com.orange.flexoffice.dao.common.model.data.TeachinSensorDao;

import com.orange.flexoffice.dao.common.repository.data.TeachinSensorDaoOperations;
import com.orange.flexoffice.dao.common.repository.data.jdbc.metadata.TeachinSensorsDaoMetadata;

/*---------------------------------------------------------------------------------------
Manage teachin_sensors table
when teachin is init(), one line is created with room_id, gateway_id & user_id renseigned with status INITIALIZING 
when GATEWAY (gateway_id) is in TEACHIN mode the status become RUNNING
when a sensors are detected, other lines ares created in table with only informations :
sensor_identifier, sensor_status (PAIRED_OK, PAIRED_KO, UNPAIRED)

The table is cleaned in two actions :
	- login by the same user_id 
	- init() another teachin (if actual teachin is in ENDED state)
	
/*---------------------------------------------------------------------------------------*/


@Repository
public class TeachinSensorsDaoRepository extends DataRepository<TeachinSensorDao> implements TeachinSensorDaoOperations {

	
	public TeachinSensorsDaoRepository() {
		super(TeachinSensorDao.class);
	}
	
	@Override
	public List<TeachinSensorDao> findAllTeachinSensors() {
		SqlParameterSource paramMap = new MapSqlParameterSource();
		return jdbcTemplate.query(
				findAllQuery, 
				paramMap, 
				new BeanPropertyRowMapper<TeachinSensorDao>(TeachinSensorDao.class)
			);
	}
	
	@Override
	public TeachinSensorDao findByTeachinStatus() throws IncorrectResultSizeDataAccessException {
		SqlParameterSource paramMap = new MapSqlParameterSource();
		return jdbcTemplate.queryForObject(
				findByTeachinStatusQuery, 
				paramMap, 
				new BeanPropertyRowMapper<TeachinSensorDao>(TeachinSensorDao.class)
			);
	}

	@Override
	public TeachinSensorDao findByUserId(Long userId) throws IncorrectResultSizeDataAccessException {
		SqlParameterSource paramMap = new MapSqlParameterSource("userId", userId);
		return jdbcTemplate.queryForObject(
				findByUserIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<TeachinSensorDao>(TeachinSensorDao.class)
			);
	}
	
	@Override
	public TeachinSensorDao updateTeachinStatus(TeachinSensorDao data) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(updateTeachinStatusQuery, paramBean, keyHolder);
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());	
		
		return data;
	}

	@Override
	public TeachinSensorDao updateTeachinDate(TeachinSensorDao data) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(updateTeachinDateQuery, paramBean, keyHolder);
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());	
		
		return data;
	}
	
	@Override
	public TeachinSensorDao saveTechinSensor(TeachinSensorDao data) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(saveTeachinSensorQuery, paramBean, keyHolder);
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		
		return data;
	}

	@Override
	public TeachinSensorDao saveTechinStatus(TeachinSensorDao data) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(saveTeachinStatusQuery, paramBean, keyHolder);
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		
		return data;
	}
	
	@Override
	public void deleteAllTeachinSensors() {
		SqlParameterSource paramMap = new MapSqlParameterSource();
		jdbcTemplate.update(deleteAllQuery, paramMap);
	}
	
	@Override
	protected String getTableName() {
		return TeachinSensorsDaoMetadata.TEACHIN_SENSORS_TABLE_NAME;
	}

	@Override
	protected String getColumnColName() {
		return TeachinSensorsDaoMetadata.TEACHIN_SENSORS_ID_COL;
	}

	@Override
	protected String getColName() {
		return TeachinSensorsDaoMetadata.TEACHIN_SENSORS_ID_COL;
	}

}
