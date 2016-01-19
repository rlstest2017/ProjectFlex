package com.orange.flexoffice.dao.common.repository.data.jdbc;

import java.util.Date;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.orange.flexoffice.dao.common.model.data.UserDao;
import com.orange.flexoffice.dao.common.repository.data.UserDaoOperations;
import com.orange.flexoffice.dao.common.repository.data.jdbc.metadata.UserDaoMetadata;

@Repository
public class UserDaoRepository extends DataRepository<UserDao> implements UserDaoOperations {

	
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
	public UserDao findByUserId(Long userId) throws IncorrectResultSizeDataAccessException {
		SqlParameterSource paramMap = new MapSqlParameterSource("columnId", userId);
		return jdbcTemplate.queryForObject(
				findByColumnIdQuery, 
				paramMap, 
				new BeanPropertyRowMapper<UserDao>(UserDao.class)
			);
	}

	
	@Override
	public UserDao findByUserEmail(String userEmail) throws IncorrectResultSizeDataAccessException {
		SqlParameterSource paramMap = new MapSqlParameterSource("email", userEmail);
		return jdbcTemplate.queryForObject(
				findByColumnMailQuery, 
				paramMap, 
				new BeanPropertyRowMapper<UserDao>(UserDao.class)
			);
	}
	
	@Override
	public UserDao findByUserEmailAndPassword(UserDao data) throws IncorrectResultSizeDataAccessException {
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		return jdbcTemplate.queryForObject(
				findByColumnMailAndPasswordQuery, 
				paramBean, 
				new BeanPropertyRowMapper<UserDao>(UserDao.class)
			);
	}
	
	
	@Override
	public UserDao findByAccessToken(String accessToken) throws IncorrectResultSizeDataAccessException {
		SqlParameterSource paramMap = new MapSqlParameterSource("accessToken", accessToken);
		return jdbcTemplate.queryForObject(
				findByColumnAccessTokenQuery, 
				paramMap, 
				new BeanPropertyRowMapper<UserDao>(UserDao.class)
			);
	}
	
	@Override
	public UserDao updateUser(UserDao data) throws IncorrectResultSizeDataAccessException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(updateUserQuery, paramBean, keyHolder);
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		
		return data;
	}

	@Override
	public UserDao updateAccessToken(UserDao data) {
	KeyHolder keyHolder = new GeneratedKeyHolder();
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(updateAccessTokenQuery, paramBean, keyHolder);
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		
		return data;
	
	}
	
	@Override
	public UserDao updateUserByEmail(UserDao data) {
	KeyHolder keyHolder = new GeneratedKeyHolder();
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(updateUserByMailQuery, paramBean, keyHolder);
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		
		return data;
	}
	
	@Override
	public UserDao saveUser(UserDao data) throws DataIntegrityViolationException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(saveUserQuery, paramBean, keyHolder);
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		
		return data;
	}
	
	@Override
	public UserDao saveUserFromUserUI(UserDao data) throws DataIntegrityViolationException {
	KeyHolder keyHolder = new GeneratedKeyHolder();
		
		SqlParameterSource paramBean = new BeanPropertySqlParameterSource(data);
		jdbcTemplate.update(saveUserFromUserUIQuery, paramBean, keyHolder);
		
		// Retrieves generated id of saved data.
		Integer id = (Integer)keyHolder.getKeys().get("id");
		data.setId(id.longValue());
		
		return data;
	}
		
	@Override
	public Long countActiveUsers(Date lastConnexionDate) {
		SqlParameterSource paramMap = new MapSqlParameterSource("lastConnexionDate", lastConnexionDate);
		return jdbcTemplate.queryForObject(
				countActiveUsersQuery, 
				paramMap, 
				Long.class
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
	protected String getColName() {
		return UserDaoMetadata.USER_MAIL_COL;
	}
	
}
