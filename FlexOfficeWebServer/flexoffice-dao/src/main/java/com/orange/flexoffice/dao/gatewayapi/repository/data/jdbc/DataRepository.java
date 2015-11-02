package com.orange.flexoffice.dao.gatewayapi.repository.data.jdbc;

import static com.orange.flexoffice.dao.gatewayapi.repository.data.jdbc.DataSqlTemplate.COUNT_TEMPLATE;
import static com.orange.flexoffice.dao.gatewayapi.repository.data.jdbc.DataSqlTemplate.FIND_ALL_COL_IDS_WITH_ROW_ID_CONDITIONS_TEMPLATE;
import static com.orange.flexoffice.dao.gatewayapi.repository.data.jdbc.DataSqlTemplate.FIND_ALL_TEMPLATE;
import static com.orange.flexoffice.dao.gatewayapi.repository.data.jdbc.DataSqlTemplate.FIND_BY_COL_ID_AND_ROW_ID_TEMPLATE;
import static com.orange.flexoffice.dao.gatewayapi.repository.data.jdbc.DataSqlTemplate.FIND_BY_COL_ID_TEMPLATE;
import static com.orange.flexoffice.dao.gatewayapi.repository.data.jdbc.DataSqlTemplate.FIND_BY_COL_MAIL_TEMPLATE;
import static com.orange.flexoffice.dao.gatewayapi.repository.data.jdbc.DataSqlTemplate.FIND_BY_ROW_ID_TEMPLATE;
import static com.orange.flexoffice.dao.gatewayapi.repository.data.jdbc.DataSqlTemplate.FIND_ONE_TEMPLATE;
import static com.orange.flexoffice.dao.gatewayapi.repository.data.jdbc.DataSqlTemplate.REMOVE_TEMPLATE;
import static com.orange.flexoffice.dao.gatewayapi.repository.data.jdbc.DataSqlTemplate.SAVE_TEMPLATE;
//import static com.orange.flexoffice.dao.gatewayapi.repository.data.jdbc.DataSqlTemplate.USER_TEMPLATE;
import static com.orange.flexoffice.dao.gatewayapi.repository.data.jdbc.DataSqlTemplate.UPDATE_TEMPLATE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.orange.flexoffice.dao.gatewayapi.model.data.Data;
import com.orange.flexoffice.dao.gatewayapi.repository.data.DataOperations;

public abstract class DataRepository<T extends Data> 
	implements DataOperations<T> {
	
	private final String findOneQuery;
	protected final String findAllQuery;
	private final String saveQuery;
	//private final String userQuery;
	private final String updateQuery;
	private final String deleteQuery;
	private final String countQuery;
	protected final String findByColumnIdAndRowIdQuery;
	protected final String findByColumnIdQuery;
	protected final String findByColumnMailQuery;
	protected final String findByRowIdQuery;
	protected final String findAllColumnIdsWithRowIdConditionQuery;
	
	protected NamedParameterJdbcTemplate jdbcTemplate;

	private Class<T> entityClass;
	
	public DataRepository(Class<T> entitClass) {
		this.entityClass = entitClass;
		
		findOneQuery = String.format(FIND_ONE_TEMPLATE, getTableName());
		findAllQuery = String.format(FIND_ALL_TEMPLATE, getTableName());
		saveQuery = String.format(SAVE_TEMPLATE, getTableName(), getColumnColName(), getRowColName(), getRatingColName());
		//userQuery = String.format(USER_TEMPLATE, getTableName(), getColumnColName(), getRowColName(), getRatingColName());
		updateQuery = String.format(UPDATE_TEMPLATE, getTableName(), getRatingColName(), getColumnColName(), getRowColName());
		deleteQuery = String.format(REMOVE_TEMPLATE, getTableName());
		countQuery = String.format(COUNT_TEMPLATE, getTableName());
		findByColumnIdAndRowIdQuery = String.format(FIND_BY_COL_ID_AND_ROW_ID_TEMPLATE, getTableName(), getColumnColName(), getRowColName());
		findByColumnIdQuery = String.format(FIND_BY_COL_ID_TEMPLATE, getTableName(), getColumnColName());
		findByColumnMailQuery = String.format(FIND_BY_COL_MAIL_TEMPLATE, getTableName(), getColumnMailName());
		findByRowIdQuery = String.format(FIND_BY_ROW_ID_TEMPLATE, getTableName(), getRowColName());
		findAllColumnIdsWithRowIdConditionQuery = String.format(FIND_ALL_COL_IDS_WITH_ROW_ID_CONDITIONS_TEMPLATE, getColumnColName(), getTableName(), getRowColName());
	}
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	@Override
	public T findOne(Long id) {
		SqlParameterSource paramMap = new MapSqlParameterSource("id", id);
		
		T data = null;
		try {
			data = jdbcTemplate.queryForObject(
					findOneQuery, 
					paramMap, 
					new BeanPropertyRowMapper<T>(entityClass)
				);
		} catch (EmptyResultDataAccessException erdae) {
			// If no data found just return null.
		}
		return data;
	}

	@Override
	public T save(T data) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(saveQuery, paramBean, keyHolder);
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		Date ts = (Date)keyHolder.getKeys().get("timestamp");
		data.setId(id.longValue());
		data.setTimestamp(ts);
		
		return data;
	}
	
	
	public T saveUser(T data) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(saveQuery, paramBean, keyHolder);
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		//Date ts = (Date)keyHolder.getKeys().get("timestamp");
		data.setId(id.longValue());
		//data.setTimestamp(ts);
		
		return data;
	}
	
	@Override
	public T update(T data) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(updateQuery, paramBean, keyHolder);
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		Date ts = (Date)keyHolder.getKeys().get("timestamp");
		data.setId(id.longValue());
		data.setTimestamp(ts);
		
		return data;
	}

	@Override
	public void delete(Long id) {
		SqlParameterSource paramMap = new MapSqlParameterSource("id", id);
		jdbcTemplate.update(deleteQuery, paramMap);
		
	}
	
	@Override
	public Long count() {
		return jdbcTemplate.getJdbcOperations().queryForLong(countQuery);
	}
	
	@Override
	public Data findByColumnIdAndRowId(String columnId, String rowId) {
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("columnId", columnId);
		paramMap.addValue("rowId", rowId);
		
		Data data = null;
		try {
			data = jdbcTemplate.queryForObject(
					findByColumnIdAndRowIdQuery, 
					paramMap, 
					new BeanPropertyRowMapper<T>(entityClass)
				);
		} catch (EmptyResultDataAccessException erdae) {
			// If no data found just return null
		}
		return data;
	}

	@Override
	public List<Data> findByColumnId(String columnId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("columnId", columnId);
		List<T> datas = jdbcTemplate.query(
				findByColumnIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<T>(entityClass)
			);
		return new ArrayList<Data>(datas);
	}

	@Override
	public List<Data> findByRowId(String rowId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("rowId", rowId);
		List<T> datas =  jdbcTemplate.query(
				findByRowIdQuery, 
				paramMap,
				new BeanPropertyRowMapper<T>(entityClass)
			);
		return new ArrayList<Data>(datas);
	}

	@Override
	public List<String> findColumnIdsRowConditions(Collection<String> rowIds) {
		SqlParameterSource paramMap = new MapSqlParameterSource("rowIds", rowIds);
		return jdbcTemplate.query(
				findAllColumnIdsWithRowIdConditionQuery, 
				paramMap, 
				new SingleColumnRowMapper<String>()
			);
	}
	
	protected abstract String getTableName();
	protected abstract String getColumnColName();
	protected abstract String getColumnMailName();
	protected abstract String getRowColName();
	protected abstract String getRatingColName();

}
