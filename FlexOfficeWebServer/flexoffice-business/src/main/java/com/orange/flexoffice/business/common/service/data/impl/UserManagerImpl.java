package com.orange.flexoffice.business.common.service.data.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	private final Logger LOGGER = Logger.getLogger(UserManagerImpl.class);
	
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
	public UserDao find(long UserDaoId) {
		return userRepository.findOne(UserDaoId);
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
		String userEmail = userDao.getEmail();
		
		LOGGER.debug("UserMail : " + userEmail);
		
		List<UserDao> userFound = userRepository.findByUserEmail(userEmail);
		if ((userFound != null)&&(userFound.size() > 0)) {
			LOGGER.debug("testUserDao.size() : " + userFound.size());
			throw new DataAlreadyExistsException("UserDao already saves.");
		}
		
		// Saves UserDao
		return userRepository.saveUser(userDao);
	}
	
	/**
	 * Updates a {@link UserDao}
	 * 
	 * @param UserDao
	 * 		  the new {@link UserDao}
	 * @return a saved {@link UserDao}
	 */
	public UserDao update(UserDao userDao) throws DataNotExistsException {
		Long userId = userDao.getId();
		UserDao userFound = userRepository.findOne(userDao.getId());
		
		if (userFound == null) {
			LOGGER.debug("user by id " + userId + " is not found");
			throw new DataNotExistsException("UserDao already saves.");
		}
		
		// Saves UserDao
		return userRepository.updateUser(userDao);
	}

	/**
	 * Deletes a {@link UserDao}.
	 * 
	 * @param id 
	 * 		  a {@link UserDao} ID
	 */
	public void delete(long id) throws DataNotExistsException {
	
		UserDao userFound = userRepository.findOne(id);
		
		if (userFound == null) {
			LOGGER.debug("user by id " + id + " is not found");
			throw new DataNotExistsException("user is not found.");
		}
		
		// Deletes UserDao
		userRepository.delete(id);
	}
	
	/**
	 * 
	 * @param userEmail
	 * @return
	 */
	public UserDao findByUserMail(String userEmail) {
		UserDao returnedUserDao = new UserDao();
		
		List<UserDao> userFound = userRepository.findByUserEmail(userEmail);
		if ((userFound != null)&&(userFound.size() > 0)) {
			returnedUserDao = userFound.get(0);
		}
		
		// Saves UserDao
		return returnedUserDao;
	}

}
