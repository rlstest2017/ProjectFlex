package com.orange.flexoffice.business.common.service.data;

import java.util.List;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.dao.common.model.data.UserDao;

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
	 * Saves a {@link UserDao}
	 * 
	 * @param UserDao
	 * 		  the new {@link UserDao}
	 * @return a saved {@link UserDao}
	 * @throws DataAlreadyExistsException 
	 */
	UserDao save(UserDao UserDao) throws DataAlreadyExistsException;

	/**
	 * Updates a {@link UserDao}
	 * 
	 * @param UserDao
	 * 		  the new {@link UserDao}
	 * @return a saved {@link UserDao}
	 */
	UserDao update(UserDao UserDao) throws DataNotExistsException;

	/**
	 * Deletes a {@link UserDao}.
	 * 
	 * @param id 
	 * 		  a {@link UserDao} ID
	 */
	void delete(long id) throws DataNotExistsException;

}