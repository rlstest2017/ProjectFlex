package com.orange.flexoffice.business.common.service.data;

import java.util.List;

import javax.naming.AuthenticationException;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.dao.common.model.data.UserDao;
import com.orange.flexoffice.dao.common.model.object.UserDto;

public interface UserManager {

	List<UserDao> findAllUsers();
	
	/**
	 * Finds a {@link UserDao} by its ID.
	 * 
	 * @param userDaoId
	 * 		  the {@link UserDao} ID
	 * @return a {@link UserDao}
	 */
	UserDao find(long userDaoId) throws DataNotExistsException;
	
	/**
	 * 
	 * @param userEmail
	 * @return
	 */
	UserDao findByUserMail(String userEmail) throws DataNotExistsException;

	/**
	 * 
	 * @param accessToken
	 * @return
	 * @throws AuthenticationException
	 */
	UserDto findByUserAccessToken(String accessToken) throws AuthenticationException;
	
	/**
	 * Saves a {@link UserDao}
	 * 
	 * @param UserDao
	 * 		  the new {@link UserDao}
	 * @return a saved {@link UserDao}
	 * @throws DataAlreadyExistsException 
	 */
	UserDao save(UserDao userDao) throws DataAlreadyExistsException;

	/**
	 * Updates a {@link UserDao}
	 * 
	 * @param UserDao
	 * 		  the new {@link UserDao}
	 * @return a saved {@link UserDao}
	 */
	UserDao update(UserDao userDao) throws DataNotExistsException;

	/**
	 * Deletes a {@link UserDao}.
	 * 
	 * @param id 
	 * 		  a {@link UserDao} ID
	 */
	void delete(long id) throws DataNotExistsException, IntegrityViolationException;

}