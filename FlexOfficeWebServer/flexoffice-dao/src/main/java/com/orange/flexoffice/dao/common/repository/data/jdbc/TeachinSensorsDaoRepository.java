package com.orange.flexoffice.dao.common.repository.data.jdbc;

import java.util.List;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
//import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.common.model.data.TeachinSensorDao;

import com.orange.flexoffice.dao.common.repository.data.TeachinSensorDaoOperations;
import com.orange.flexoffice.dao.common.repository.data.jdbc.metadata.TeachinSensorsDaoMetadata;
import com.orange.flexoffice.dao.common.repository.support.DataExtractor;

@Repository
public class TeachinSensorsDaoRepository extends DataRepository<TeachinSensorDao> implements TeachinSensorDaoOperations {

	//private static final Logger LOGGER = Logger.getLogger(TeachinSensorsDaoRepository.class);
	
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
	public void forEach(DataExtractor dataExtractor) {
	}

	@Override
	protected String getRowColName() {
		return null;
	}

}
