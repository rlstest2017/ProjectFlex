package com.orange.flexoffice.dao.gatewayapi.repository.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.gatewayapi.model.data.Data;
import com.orange.flexoffice.dao.gatewayapi.model.data.Log;
import com.orange.flexoffice.dao.gatewayapi.repository.data.LogOperations;
import com.orange.flexoffice.dao.gatewayapi.repository.data.jdbc.metadata.UserFlexofficeMetadata;
import com.orange.flexoffice.dao.gatewayapi.repository.support.DataExtractor;
import com.orange.flexoffice.dao.gatewayapi.repository.support.StreamingPreparedStatement;

@Repository
public class LogRepository 
	extends DataRepository<Log>
	implements LogOperations {

	public LogRepository() {
		super(Log.class);
	}
	
	@Override
	public Log findByUserIdAndItemId(String userId, String itemId) {
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("columnId", userId);
		paramMap.addValue("rowId", itemId);
		
		Log data = null;
		try {
			data = jdbcTemplate.queryForObject(
					findByColumnIdAndRowIdQuery, 
					paramMap, 
					new BeanPropertyRowMapper<Log>(Log.class)
				);
		} catch (EmptyResultDataAccessException erdae) {
			// If no data found just return null
		}
		return data;
	}

	@Override
	public List<Log> findByUserId(String userId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("columnId", userId);
		return jdbcTemplate.query(
				findByColumnIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<Log>(Log.class)
			);
	}

	@Override
	public List<Log> findByItemId(String itemId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("rowId", itemId);
		return jdbcTemplate.query(
				findByRowIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<Log>(Log.class)
			);
	}
	
	@Override
	public void forEach(final DataExtractor dataExtractor) {
		jdbcTemplate.getJdbcOperations().query(new StreamingPreparedStatement(findAllQuery), new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				Data data = new Log();
				data.setColumnId(rs.getString(getColumnColName()));
				data.setRowId(rs.getString(getRowColName()));
				data.setComment(rs.getString("comment"));
				data.setTimestamp(rs.getDate("timestamp"));
				data.setRating(rs.getFloat(getRatingColName()));
				
				dataExtractor.extractData(data);
				
			}
		});		
	}
	
	@Override
	protected String getTableName() {
		return UserFlexofficeMetadata.LOG_TABLE_NAME;
	}

	@Override
	protected String getColumnColName() {
		return UserFlexofficeMetadata.LOG_USER_ID_COL;
	}

	@Override
	protected String getRowColName() {
		return UserFlexofficeMetadata.LOG_ITEM_ID_COL;
	}

	@Override
	protected String getRatingColName() {
		return UserFlexofficeMetadata.LOG_RATING_COL;
	}

}
