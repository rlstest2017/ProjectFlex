package com.orange.flexoffice.dao.common.repository.data.jdbc;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.common.model.data.SensorDao;
import com.orange.flexoffice.dao.common.repository.data.SensorDaoOperations;
import com.orange.flexoffice.dao.common.repository.data.jdbc.metadata.SensorDaoMetadata;
import com.orange.flexoffice.dao.common.repository.support.DataExtractor;

@Repository
public class SensorDaoRepository extends DataRepository<SensorDao> implements SensorDaoOperations {

	private final Logger LOGGER = Logger.getLogger(SensorDaoRepository.class);
	
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
	public List<SensorDao> findBySensorId(Long sonsorId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("columnId", sonsorId);
		return jdbcTemplate.query(
				findByColumnIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<SensorDao>(SensorDao.class)
			);
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
