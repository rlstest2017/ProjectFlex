package com.orange.flexoffice.dao.common.repository.data.jdbc;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.common.model.data.AlertDao;
import com.orange.flexoffice.dao.common.repository.data.AlertDaoOperations;
import com.orange.flexoffice.dao.common.repository.data.jdbc.metadata.AlertDaoMetadata;
import com.orange.flexoffice.dao.common.repository.support.DataExtractor;

@Repository
public class AlertDaoRepository extends DataRepository<AlertDao> implements AlertDaoOperations {

	private static final Logger LOGGER = Logger.getLogger(AlertDaoRepository.class);
	
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
	protected String getTableName() {
		return AlertDaoMetadata.ALERT_TABLE_NAME;
	}

	@Override
	protected String getColumnColName() {
		return AlertDaoMetadata.ALERT_ID_COL;
		
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
