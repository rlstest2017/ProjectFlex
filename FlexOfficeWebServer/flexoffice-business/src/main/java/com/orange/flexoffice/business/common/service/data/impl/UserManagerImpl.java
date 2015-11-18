package com.orange.flexoffice.business.common.service.data.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.service.data.UserManager;
import com.orange.flexoffice.dao.common.model.data.UserDao;
import com.orange.flexoffice.dao.common.repository.data.jdbc.UserDaoRepository;

/**
 * Manages {@link UserDao}.
 * 
 * @author oab
 */
@Service("UserDaoManager")
@Transactional
public class UserManagerImpl implements UserManager {
	
	private static final Logger LOGGER = Logger.getLogger(UserManagerImpl.class);
	
	@Autowired
	private UserDaoRepository userRepository;
	
	@Transactional(readOnly=true)
	public List<UserDao> findAllUsers() {
		return userRepository.findAllUsers();
	}
	
	/**
	 * Finds a {@link UserDao} by its ID.
	 * 
	 * @param UserDaoId
	 * 		  the {@link UserDao} ID
	 * @return a {@link UserDao}
	 */
	@Transactional(readOnly=true)
	public UserDao find(long userDaoId) throws DataNotExistsException {
		try {
			return userRepository.findOne(userDaoId);

		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.error("UserManager.find : User by id #" + userDaoId + " is not found", e);
			throw new DataNotExistsException("UserManager.find : User by id #" + userDaoId + " is not found");
		}

	}

	
	/**
	 * 
	 * @param userEmail
	 * @return
	 */
	public UserDao findByUserMail(String userEmail) throws DataNotExistsException {
		try {
			return userRepository.findByUserEmail(userEmail);

		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.error("UserManager.findByUserMail : User by email #" + userEmail + " is not found", e);
			throw new DataNotExistsException("UserManager.findByUserMail : User by email #" + userEmail + " is not found");
		}
	}

	/**
	 * Saves a {@link UserDao}
	 * 
	 * @param userDao
	 * 		  the new {@link UserDao}
	 * @return a saved {@link UserDao}
	 * @throws DataAlreadyExistsException 
	 */
	public UserDao save(UserDao userDao) throws DataAlreadyExistsException {
		try {		
			// Saves UserDao
			return userRepository.saveUser(userDao);

		} catch(DataIntegrityViolationException e ) {
			LOGGER.error("UserManager.save : User already exists", e);
			throw new DataAlreadyExistsException("UserManager.save : User already exists");
		}
	}
	
	/**
	 * Updates a {@link UserDao}
	 * 
	 * @param UserDao
	 * 		  the new {@link UserDao}
	 * @return a saved {@link UserDao}
	 */
	public UserDao update(UserDao userDao) throws DataNotExistsException {
		try {
			// Update RoomDao
			return userRepository.updateUser(userDao);
			
		} catch (RuntimeException e) {
			LOGGER.error("UserManager.update : User to update not found", e);
			throw new DataNotExistsException("UserManager.update : User to update not found");
		}
	}

	/**
	 * Deletes a {@link UserDao}.
	 * 
	 * @param id 
	 * 		  a {@link UserDao} ID
	 */
	public void delete(long id) throws DataNotExistsException {
	
		try {
			// To generate exception if wrong id
			userRepository.findOne(id);
			// Delete Room
			userRepository.delete(id);	
		} catch (IncorrectResultSizeDataAccessException e) {
			LOGGER.error("UserManager.delete : User #" + id + " not found", e);
			throw new DataNotExistsException("UserManager.delete : User #" + id + " not found");
		}
	}

}
