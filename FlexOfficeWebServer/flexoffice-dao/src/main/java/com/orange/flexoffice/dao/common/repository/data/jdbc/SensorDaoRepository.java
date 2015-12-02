package com.orange.flexoffice.dao.common.repository.data.jdbc;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.common.model.data.SensorDao;
import com.orange.flexoffice.dao.common.repository.data.SensorDaoOperations;
import com.orange.flexoffice.dao.common.repository.data.jdbc.metadata.SensorDaoMetadata;
import com.orange.flexoffice.dao.common.repository.support.DataExtractor;

@Repository
public class SensorDaoRepository extends DataRepository<SensorDao> implements SensorDaoOperations {

	
	public SensorDaoRepository() {
		super(SensorDao.class);
	}
	
	@Override
	public List<SensorDao> findAllSensors() {
		SqlParameterSource paramMap = new MapSqlParameterSource();
		return jdbcTemplate.query(
				findAllQuery, 
				paramMap, 
				new BeanPropertyRowMapper<SensorDao>(SensorDao.class)
			);
	}

	@Override
	public SensorDao findBySensorId(String sensorIdentifier) throws IncorrectResultSizeDataAccessException {
		SqlParameterSource paramMap = new MapSqlParameterSource("identifier", sensorIdentifier);
		return jdbcTemplate.queryForObject(
				findByIdentifierQuery, 
				paramMap, 
				new BeanPropertyRowMapper<SensorDao>(SensorDao.class)
			);
	}
	
	@Override
	public List<SensorDao> findByRoomId(Long roomId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("roomId", roomId);
		return jdbcTemplate.query(
				findByColumnRoomIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<SensorDao>(SensorDao.class)
			);
	}

	@Override
	public List<SensorDao> findByRoomIdAndOccupiedInfo(Long roomId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("roomId", roomId);
		return jdbcTemplate.query(
				findByColumnRoomIdAndOccupancyInfoQuery, 
				paramMap, 
				new BeanPropertyRowMapper<SensorDao>(SensorDao.class)
			);
	}
	
	@Override
	public SensorDao saveSensor(SensorDao data) throws DataIntegrityViolationException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(saveSensorQuery, paramBean, keyHolder);
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		
		return data;
	}
	
	
	@Override
	public SensorDao updateSensor(SensorDao data) throws DataAccessException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(updateSensorQuery, paramBean, keyHolder);
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());	
		
		return data;
	}

	@Override
	public SensorDao updateSensorStatus(SensorDao data) throws DataAccessException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
			
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(updateSensorStatusQuery, paramBean, keyHolder);
				
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());	
		
		return data;
	}

	@Override
	public void deleteByIdentifier(String sensorIdentifier) throws DataAccessException {
		SqlParameterSource paramMap = new MapSqlParameterSource("identifier", sensorIdentifier);
		jdbcTemplate.update(deleteByIdentifier, paramMap);	
	}

	@Override
	protected String getTableName() {
		return SensorDaoMetadata.SENSOR_TABLE_NAME;
	}

	@Override
	protected String getColumnColName() {
		return SensorDaoMetadata.SENSOR_ID_COL;
		
	}

	@Override
	public void forEach(DataExtractor dataExtractor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String getRowColName() {
		// TODO Auto-generated method stub
		return null;
	}

}
