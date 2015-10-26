package com.orange.flexoffice.dao.gateway.repository.data.jdbc;

import static com.orange.flexoffice.dao.gateway.repository.data.jdbc.metadata.PreferenceMetadata.PREFERENCE_DESC_ID_COL;
import static com.orange.flexoffice.dao.gateway.repository.data.jdbc.metadata.PreferenceMetadata.PREFERENCE_RATING_COL;
import static com.orange.flexoffice.dao.gateway.repository.data.jdbc.metadata.PreferenceMetadata.PREFERENCE_TABLE_NAME;
import static com.orange.flexoffice.dao.gateway.repository.data.jdbc.metadata.PreferenceMetadata.PREFERENCE_USER_ID_COL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.gateway.repository.data.PreferenceOperations;
import com.orange.flexoffice.dao.gatewayapi.model.data.Data;
import com.orange.flexoffice.dao.gatewayapi.model.data.Preference;
import com.orange.flexoffice.dao.gatewayapi.repository.support.DataExtractor;
import com.orange.flexoffice.dao.gatewayapi.repository.support.StreamingPreparedStatement;

@Repository
public class PreferenceRepository 
	extends DataRepository<Preference>
	implements PreferenceOperations {
	
	public PreferenceRepository() {
		super(Preference.class);
	}

	@Override
	protected String getTableName() {
		return PREFERENCE_TABLE_NAME;
	}

	@Override
	protected String getColumnColName() {
		return PREFERENCE_USER_ID_COL;
	}

	@Override
	protected String getRowColName() {
		return PREFERENCE_DESC_ID_COL;
	}

	@Override
	protected String getRatingColName() {
		return PREFERENCE_RATING_COL;
	}

	@Override
	public Preference findByUserIdAndDescriptorId(String userId,
			String descriptorId) {
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("columnId", userId);
		paramMap.addValue("rowId", descriptorId);
		
		Preference data = null;
		try {
			data = jdbcTemplate.queryForObject(
					findByColumnIdAndRowIdQuery, 
					paramMap, 
					new BeanPropertyRowMapper<Preference>(Preference.class)
				);
		} catch (EmptyResultDataAccessException erdae) {
			// If no data found just return null
		}
		return data;
	}

	@Override
	public List<Preference> findByUserId(String userId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("columnId", userId);
		return jdbcTemplate.query(
				findByColumnIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<Preference>(Preference.class)
			);
	}

	@Override
	public List<Preference> findByDescriptorId(String descriptorId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("rowId", descriptorId);
		return jdbcTemplate.query(
				findByRowIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<Preference>(Preference.class)
			);
	}
	
	@Override
	public void forEach(final DataExtractor dataExtractor) {
		jdbcTemplate.getJdbcOperations().query(new StreamingPreparedStatement(findAllQuery), new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				Data data = new Preference();
				data.setColumnId(rs.getString(getColumnColName()));
				data.setRowId(rs.getString(getRowColName()));
				data.setComment(rs.getString("comment"));
				data.setTimestamp(rs.getDate("timestamp"));
				data.setRating(rs.getFloat(getRatingColName()));
				
				dataExtractor.extractData(data);
				
			}
		});		
	}


}
