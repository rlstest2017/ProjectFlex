package com.orange.flexoffice.dao.userui.repository.data.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.gatewayapi.model.data.Data;
import com.orange.flexoffice.dao.gatewayapi.repository.data.jdbc.DataRepository;
import com.orange.flexoffice.dao.gatewayapi.repository.support.DataExtractor;
import com.orange.flexoffice.dao.gatewayapi.repository.support.StreamingPreparedStatement;
import com.orange.flexoffice.dao.userui.model.data.UserFlexoffice;
import com.orange.flexoffice.dao.userui.repository.data.UserFlexofficeOperations;
import com.orange.flexoffice.dao.userui.repository.data.jdbc.metadata.UserFlexofficeMetadata;

@Repository
public class UserFlexofficeRepository extends DataRepository<UserFlexoffice> implements UserFlexofficeOperations {

	private final Logger LOGGER = Logger.getLogger(UserFlexofficeRepository.class);
	
	public UserFlexofficeRepository() {
		super(UserFlexoffice.class);
	}
	
	
	@Override
	public List<UserFlexoffice> findByUserId(Long userId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("columnId", userId);
		return jdbcTemplate.query(
				findByColumnIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<UserFlexoffice>(UserFlexoffice.class)
			);
	}

	@Override
	public List<UserFlexoffice> findByUserEmail(String userEmail) {
		SqlParameterSource paramMap = new MapSqlParameterSource("columnEmail", userEmail);
		return jdbcTemplate.query(
				findByColumnMailQuery, 
				paramMap, 
				new BeanPropertyRowMapper<UserFlexoffice>(UserFlexoffice.class)
			);
	}
	
	@Override
	public void forEach(final DataExtractor dataExtractor) {
		jdbcTemplate.getJdbcOperations().query(new StreamingPreparedStatement(findAllQuery), new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				Data data = new UserFlexoffice();
				data.setColumnId(rs.getString(getColumnColName()));
				data.setRowId(rs.getString(getRowColName()));
				data.setTimestamp(rs.getDate("last_connection"));
				
				dataExtractor.extractData(data);
				
			}
		});		
	}
	
	@Override
	protected String getTableName() {
		return UserFlexofficeMetadata.USER_FLEXOFFICE_TABLE_NAME;
	}

	@Override
	protected String getColumnColName() {
		return UserFlexofficeMetadata.USER_FLEXOFFICE_ID_COL;
		
	}

	@Override
	protected String getRowColName() {
		return UserFlexofficeMetadata.USER_FLEXOFFICE_ID_COL;
	}

	@Override
	protected String getRatingColName() {
		return null;
	}


	@Override
	protected String getColumnMailName() {
		return UserFlexofficeMetadata.USER_FLEXOFFICE_MAIL_COL;
	}


	

}
