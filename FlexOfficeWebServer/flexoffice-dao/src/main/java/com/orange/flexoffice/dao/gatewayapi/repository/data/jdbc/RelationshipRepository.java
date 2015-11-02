package com.orange.flexoffice.dao.gatewayapi.repository.data.jdbc;

import static com.orange.flexoffice.dao.gatewayapi.repository.data.jdbc.metadata.RelationshipMetadata.RELATIONSHIP_FRIEND_ID_COL;
import static com.orange.flexoffice.dao.gatewayapi.repository.data.jdbc.metadata.RelationshipMetadata.RELATIONSHIP_RATING_COL;
import static com.orange.flexoffice.dao.gatewayapi.repository.data.jdbc.metadata.RelationshipMetadata.RELATIONSHIP_TABLE_NAME;
import static com.orange.flexoffice.dao.gatewayapi.repository.data.jdbc.metadata.RelationshipMetadata.RELATIONSHIP_USER_ID_COL;

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
import com.orange.flexoffice.dao.gatewayapi.model.data.Relationship;
import com.orange.flexoffice.dao.gatewayapi.repository.data.RelationshipOperations;
import com.orange.flexoffice.dao.gatewayapi.repository.support.DataExtractor;
import com.orange.flexoffice.dao.gatewayapi.repository.support.StreamingPreparedStatement;

@Repository
public class RelationshipRepository 
	extends DataRepository<Relationship>
	implements RelationshipOperations {
	
	public RelationshipRepository() {
		super(Relationship.class);
	}

	@Override
	protected String getTableName() {
		return RELATIONSHIP_TABLE_NAME;
	}

	@Override
	protected String getColumnColName() {
		return RELATIONSHIP_USER_ID_COL;
	}

	@Override
	protected String getRowColName() {
		return RELATIONSHIP_FRIEND_ID_COL;
	}

	@Override
	protected String getRatingColName() {
		return RELATIONSHIP_RATING_COL;
	}

	@Override
	public Relationship findByUserIdAndFriendId(String userId, String friendId) {
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("columnId", userId);
		paramMap.addValue("rowId", friendId);
		
		Relationship data = null;
		try {
			data = jdbcTemplate.queryForObject(
					findByColumnIdAndRowIdQuery, 
					paramMap, 
					new BeanPropertyRowMapper<Relationship>(Relationship.class)
				);
		} catch (EmptyResultDataAccessException erdae) {
			// If no data found just return null
		}
		return data;
	}

	@Override
	public List<Relationship> findByUserId(String userId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("columnId", userId);
		return jdbcTemplate.query(
				findByColumnIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<Relationship>(Relationship.class)
			);
	}

	@Override
	public List<Relationship> findByFriendId(String friendId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("rowId", friendId);
		return jdbcTemplate.query(
				findByRowIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<Relationship>(Relationship.class)
			);
	}
	
	@Override
	public void forEach(final DataExtractor dataExtractor) {
		jdbcTemplate.getJdbcOperations().query(new StreamingPreparedStatement(findAllQuery), new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				Data data = new Relationship();
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
	protected String getColumnMailName() {
		// TODO Auto-generated method stub
		return null;
	}

}
