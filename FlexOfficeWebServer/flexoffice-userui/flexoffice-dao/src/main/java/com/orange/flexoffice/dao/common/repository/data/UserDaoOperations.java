package com.orange.flexoffice.dao.common.repository.data;

import java.util.List;

import com.orange.flexoffice.dao.common.model.data.UserDao;

/**
 * UserDaoOperations
 * @author oab
 *
 */
public interface UserDaoOperations {
	
	List<UserDao> findAllUsers();
	
	List<UserDao> findByUserId(Long userId);
	
	List<UserDao> findByUserEmail(String userEmail);
	
}