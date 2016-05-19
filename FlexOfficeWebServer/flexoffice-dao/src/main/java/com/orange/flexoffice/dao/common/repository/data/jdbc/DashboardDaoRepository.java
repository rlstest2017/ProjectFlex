package com.orange.flexoffice.dao.common.repository.data.jdbc;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.common.model.data.DashboardDao;
import com.orange.flexoffice.dao.common.repository.data.DashboardDaoOperations;
import com.orange.flexoffice.dao.common.repository.data.jdbc.metadata.DashboardDaoMetadata;

/*---------------------------------------------------------------------------------------
Manage dashboards table

---------------------------------------------------------------------------------------*/

@Repository
public class DashboardDaoRepository extends DataRepository<DashboardDao> implements DashboardDaoOperations {

	private static final Logger LOGGER = Logger.getLogger(DashboardDaoRepository.class);
	
	public DashboardDaoRepository() {
		super(DashboardDao.class);
	}
	
	@Override
	public List<DashboardDao> findAllDashboards() {
		SqlParameterSource paramMap = new MapSqlParameterSource();
		return jdbcTemplate.query(
				findAllQuery, 
				paramMap, 
				new BeanPropertyRowMapper<DashboardDao>(DashboardDao.class)
			);
	}

	@Override
	public DashboardDao findByDashboardId(Long dashboardId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("columnId", dashboardId);
		DashboardDao data = null;
		data =  jdbcTemplate.queryForObject(
				findByColumnIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<DashboardDao>(DashboardDao.class)
			);
		return data;
	}
	
	@Override
	public DashboardDao findByMacAddress(String macAddress) throws EmptyResultDataAccessException {
		SqlParameterSource paramMap = new MapSqlParameterSource("macAddress", macAddress);
		DashboardDao data = null;
		data = jdbcTemplate.queryForObject(
				findByMacAddressQuery, 
				paramMap, 
				new BeanPropertyRowMapper<DashboardDao>(DashboardDao.class)
			);
		return data;
	}
	
	@Override
	public List<DashboardDao> findDashboardsInTimeout(String intervalTimeout){
		SqlParameterSource paramMap = new MapSqlParameterSource("intervalTimeout", intervalTimeout);
		return jdbcTemplate.query(
				findDashboardsInTimeoutQuery, 
				paramMap, 
				new BeanPropertyRowMapper<DashboardDao>(DashboardDao.class)
			);
	}
	
	@Override
	public DashboardDao saveDashboard(DashboardDao data) throws DataIntegrityViolationException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(saveDashboardQuery, paramBean, keyHolder);
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		
		return data;
	}
	
	@Override
	public DashboardDao updateDashboard(DashboardDao data) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(updateDashboardQuery, paramBean, keyHolder);
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		
		return data;
	}
	
	@Override
	public DashboardDao updateDashboardStatus(DashboardDao data) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		LOGGER.debug("Before execute jdbcTemplate update() method");
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(updateDashboardStatusQuery, paramBean, keyHolder);
		
		LOGGER.debug("After execute jdbcTemplate update() method");
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		
		return data;
	}
	
	@Override
	public DashboardDao updateDashboardStatusForTimeout(DashboardDao data) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		LOGGER.debug("Before execute jdbcTemplate update() method");
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(updateDashboardStatusForTimeoutQuery, paramBean, keyHolder);
		
		LOGGER.debug("After execute jdbcTemplate update() method");
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		
		return data;
	}
	
	@Override
	public void deleteByMacAddress(String macAddress) {
		SqlParameterSource paramMap = new MapSqlParameterSource("macAddress", macAddress);
		jdbcTemplate.update(deleteByMacAddressQuery, paramMap);
	}
	
	@Override
	protected String getTableName() {
		return DashboardDaoMetadata.DASHBOARD_TABLE_NAME;
	}

	@Override
	protected String getColumnColName() {
		return DashboardDaoMetadata.DASHBOARD_ID_COL;
	}

	@Override
	protected String getColName() {
		return DashboardDaoMetadata.DASHBOARD_NAME_COL;
	}

}
