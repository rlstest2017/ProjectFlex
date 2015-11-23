package com.orange.flexoffice.dao.common.repository.data;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import com.orange.flexoffice.dao.common.model.data.UserDao;

/**
 * UserDaoOperations
 * @author oab
 *
 */
public interface UserDaoOperations {
	
	List<UserDao> findAllUsers();
	
	UserDao findByUserId(Long userId) throws IncorrectResultSizeDataAccessException;
	
	UserDao findByUserEmail(String userEmail) throws IncorrectResultSizeDataAccessException;
	
	UserDao findByUserEmailAndPassword(UserDao data) throws IncorrectResultSizeDataAccessException;
	
	UserDao findByAccessToken(String accessToken) throws IncorrectResultSizeDataAccessException;
	
	UserDao updateUser(UserDao data) throws IncorrectResultSizeDataAccessException;
	
	UserDao updateAccessToken(UserDao data);
	
	UserDao updateUserByEmail(UserDao data);
	
	UserDao saveUser(UserDao data) throws DataIntegrityViolationException;
	
}
