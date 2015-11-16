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
	
	UserDao findByUserId(Long userId);
	
	UserDao findByUserEmail(String userEmail);
	
	UserDao updateUser(UserDao data);
	
	UserDao saveUser(UserDao data);
	
}
