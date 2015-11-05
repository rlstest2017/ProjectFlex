package com.orange.flexoffice.dao.common.repository.data.jdbc;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.common.model.data.UserDao;
import com.orange.flexoffice.dao.common.repository.data.UserDaoOperations;
import com.orange.flexoffice.dao.common.repository.data.jdbc.metadata.UserDaoMetadata;
import com.orange.flexoffice.dao.common.repository.support.DataExtractor;

@Repository
public class UserDaoRepository extends DataRepository<UserDao> implements UserDaoOperations {

	private final Logger LOGGER = Logger.getLogger(UserDaoRepository.class);
	
	public UserDaoRepository() {
		super(UserDao.class);
	}
	
	@Override
	public List<UserDao> findAllUsers() {
		SqlParameterSource paramMap = new MapSqlParameterSource();
		return jdbcTemplate.query(
				findAllQuery, 
				paramMap, 
				new BeanPropertyRowMapper<UserDao>(UserDao.class)
			);
	}
	
	@Override
	public List<UserDao> findByUserId(Long userId) {
		SqlParameterSource paramMap = new MapSqlParameterSource("columnId", userId);
		return jdbcTemplate.query(
				findByColumnIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<UserDao>(UserDao.class)
			);
	}

	@Override
	public List<UserDao> findByUserEmail(String userEmail) {
		SqlParameterSource paramMap = new MapSqlParameterSource("columnEmail", userEmail);
		return jdbcTemplate.query(
				findByColumnMailQuery, 
				paramMap, 
				new BeanPropertyRowMapper<UserDao>(UserDao.class)
			);
	}
	
		
	@Override
	protected String getTableName() {
		return UserDaoMetadata.USER_TABLE_NAME;
	}

	@Override
	protected String getColumnColName() {
		return UserDaoMetadata.USER_ID_COL;
		
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
