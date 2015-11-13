package com.orange.flexoffice.dao.common.repository.data.jdbc;

import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.COUNT_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.FIND_ALL_COL_IDS_WITH_ROW_ID_CONDITIONS_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.FIND_ALL_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.FIND_BY_COL_ID_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.FIND_BY_COL_MAIL_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.FIND_BY_COL_ROOM_ID_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.FIND_BY_COL_GATEWAY_ID_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.FIND_ONE_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.REMOVE_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.UPDATE_USER_TEMPLATE;
import static com.orange.flexoffice.dao.common.repository.data.jdbc.DataSqlTemplate.CREATE_USER_TEMPLATE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.orange.flexoffice.dao.common.model.data.Data;
import com.orange.flexoffice.dao.common.repository.data.DataOperations;

public abstract class DataRepository<T extends Data> 
	implements DataOperations<T> {
	
	private final String findOneQuery;
	protected final String findAllQuery;
	protected final String saveUserQuery;
	protected final String updateUserQuery;
	private final String deleteQuery;
	private final String countQuery;
	protected final String findByColumnIdQuery;
	protected final String findByColumnMailQuery;
	protected final String findByColumnRoomIdQuery;
	protected final String findByColumnGatewayIdQuery;
	protected final String findAllColumnIdsWithRowIdConditionQuery;
	
	protected NamedParameterJdbcTemplate jdbcTemplate;

	protected JdbcTemplate jdbcTemplateForTest;
	
	private Class<T> entityClass;
	
	public DataRepository(Class<T> entitClass) {
		this.entityClass = entitClass;
		
		findOneQuery = String.format(FIND_ONE_TEMPLATE, getTableName());
		findAllQuery = String.format(FIND_ALL_TEMPLATE, getTableName());
		saveUserQuery = String.format(CREATE_USER_TEMPLATE, getTableName());
		updateUserQuery = String.format(UPDATE_USER_TEMPLATE, getTableName(), getColumnColName());
		deleteQuery = String.format(REMOVE_TEMPLATE, getTableName());	
		countQuery = String.format(COUNT_TEMPLATE, getTableName());
		findByColumnIdQuery = String.format(FIND_BY_COL_ID_TEMPLATE, getTableName(), getColumnColName());
		findByColumnMailQuery = String.format(FIND_BY_COL_MAIL_TEMPLATE, getTableName());
		findByColumnRoomIdQuery = String.format(FIND_BY_COL_ROOM_ID_TEMPLATE, getTableName());
		findByColumnGatewayIdQuery = String.format(FIND_BY_COL_GATEWAY_ID_TEMPLATE, getTableName());
		findAllColumnIdsWithRowIdConditionQuery = String.format(FIND_ALL_COL_IDS_WITH_ROW_ID_CONDITIONS_TEMPLATE, getColumnColName(), getTableName(), getRowColName());
	}
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		// For Tests
		jdbcTemplateForTest = new JdbcTemplate(dataSource);
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
	public void delete(Long id) {
		SqlParameterSource paramMap = new MapSqlParameterSource("id", id);
		jdbcTemplate.update(deleteQuery, paramMap);
		
	}
	
	@Override
	public Long count() {
		return jdbcTemplate.getJdbcOperations().queryForLong(countQuery);
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
	protected abstract String getRowColName();

}
