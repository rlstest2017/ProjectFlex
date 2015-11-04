package com.orange.flexoffice.dao.userui.repository.data.jdbc;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

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
	public List<UserFlexoffice> findAllUsers() {
		SqlParameterSource paramMap = new MapSqlParameterSource();
		return jdbcTemplate.query(
				findAllQuery, 
				paramMap, 
				new BeanPropertyRowMapper<UserFlexoffice>(UserFlexoffice.class)
			);
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


	@Override
	public void forEach(com.orange.flexoffice.dao.userui.repository.support.DataExtractor dataExtractor) {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected String getFisrtName() {
		return UserFlexofficeMetadata.USER_FLEXOFFICE_FIRSTNAME_COL;
	}


	@Override
	protected String getLastName() {
		return UserFlexofficeMetadata.USER_FLEXOFFICE_LASTNAME_COL;
	}


	@Override
	protected String getEmail() {
		return UserFlexofficeMetadata.USER_FLEXOFFICE_MAIL_COL;
	}


	@Override
	protected String getPassword() {
		return UserFlexofficeMetadata.USER_FLEXOFFICE_PASSSWOR_COL;	
	}

	

}
