package com.orange.flexoffice.business.common.service.data.impl;

import java.util.Date;
import java.util.List;

import javax.naming.AuthenticationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.service.data.UserManager;
import com.orange.flexoffice.dao.common.model.data.RoomDao;
import com.orange.flexoffice.dao.common.model.data.UserDao;
import com.orange.flexoffice.dao.common.model.object.UserDto;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RoomDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.UserDaoRepository;

/**
 * Manages {@link UserDao}.
 * 
 * @author oab
 */
@Service("UserManager")
@Transactional
public class UserManagerImpl implements UserManager {
	
	private static final Logger LOGGER = Logger.getLogger(UserManagerImpl.class);
	
	@Autowired
	private UserDaoRepository userRepository;
	
	@Autowired
	private RoomDaoRepository roomRepository;
	
	@Override
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
	@Override
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
	@Override
	@Transactional(readOnly=true)
	public UserDao findByUserMail(String userEmail) throws DataNotExistsException {
		try {
			return userRepository.findByUserEmail(userEmail);

		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.error("UserManager.findByUserMail : User by email #" + userEmail + " is not found", e);
			throw new DataNotExistsException("UserManager.findByUserMail : User by email #" + userEmail + " is not found");
		}
	}

	/**
	 * 
	 * @param userEmail
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public UserDto findByUserAccessToken(String accessToken) throws AuthenticationException {
		try {
			UserDto user = new UserDto();
			
			UserDao userDao = userRepository.findByAccessToken(accessToken);
			if (userDao == null)  {
				LOGGER.debug("checkToken return : accessToken not found in DB.");
				throw new AuthenticationException("UserManager.findByUserAccessToken : User by accessToken #" + accessToken + " is not found in DB");
			}else if (userDao.getExpiredTokenDate().before(new Date())) {
				LOGGER.debug("checkToken return : The accessToken is expired at :" + user.getExpiredTokenDate());
				throw new AuthenticationException("UserManager.findByUserAccessToken : User by accessToken #" + accessToken + " is expired");
			} else {
				user.setId(userDao.getColumnId());
				user.setFirstName(userDao.getFirstName());
				user.setLastName(userDao.getLastName());
				user.setEmail(userDao.getEmail());
				try {
					RoomDao room = roomRepository.findByUserId(userDao.getId());
					user.setRoomId(room.getColumnId());
				} catch(IncorrectResultSizeDataAccessException e ) {
					LOGGER.error("UserManager.findByUserId ROOM : User by iD #" + userDao.getColumnId() + " is not found", e);
					user.setRoomId(null);
				}	
			}
			
			return user;

		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.error("UserManager.findByUserAccessToken : User by accessToken #" + accessToken + " is not found", e);
			throw new AuthenticationException("UserManager.findByUserAccessToken : User by accessToken #" + accessToken + " is not found");
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
	@Override
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
	@Override
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
	@Override
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
