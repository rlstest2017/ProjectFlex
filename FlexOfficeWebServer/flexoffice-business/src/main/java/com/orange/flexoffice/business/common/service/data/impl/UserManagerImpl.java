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
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.business.common.service.data.UserManager;
import com.orange.flexoffice.dao.common.model.data.RoomDao;
import com.orange.flexoffice.dao.common.model.data.UserDao;
import com.orange.flexoffice.dao.common.model.object.UserDto;
import com.orange.flexoffice.dao.common.repository.data.jdbc.PreferencesDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RoomDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.UserDaoRepository;

/**
 * Manages {@link UserDao}.
 * For PROD LOG LEVEL is info then we say info & error logs.
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
	@Autowired
	private PreferencesDaoRepository preferenceRepository;
	
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
			LOGGER.debug("UserManager.find : User by id #" + userDaoId + " is not found", e);
			LOGGER.error("UserManager.find : User by id #" + userDaoId + " is not found");
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
			LOGGER.debug("UserManager.findByUserMail : User by email #" + userEmail + " is not found", e);
			LOGGER.error("UserManager.findByUserMail : User by email #" + userEmail + " is not found");
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
				LOGGER.error("checkToken return : accessToken not found in DB.");
				throw new AuthenticationException("UserManager.findByUserAccessToken : User by accessToken #" + accessToken + " is not found in DB");
			}else if (userDao.getExpiredTokenDate().before(new Date())) {
				LOGGER.error("checkToken return : The accessToken is expired at :" + user.getExpiredTokenDate());
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
					LOGGER.debug("UserManager.findByUserId ROOM : User by iD #" + userDao.getColumnId() + " is not found", e);
					LOGGER.info("UserManager.findByUserId ROOM : User by iD #" + userDao.getColumnId() + " is not found");
					user.setRoomId(null);
				}	
			}
			
			return user;

		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.debug("UserManager.findByUserAccessToken : User by accessToken #" + accessToken + " is not found", e);
			LOGGER.error("UserManager.findByUserAccessToken : User by accessToken #" + accessToken + " is not found");
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
			// save userDao
			return userRepository.saveUser(userDao);

		} catch(DataIntegrityViolationException e ) {
			LOGGER.debug("UserManager.save : User already exists", e);
			LOGGER.error("UserManager.save : User already exists");
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
			// update RoomDao
			return userRepository.updateUser(userDao);
			
		} catch (RuntimeException e) {
			LOGGER.debug("UserManager.update : User to update not found", e);
			LOGGER.error("UserManager.update : User to update not found");
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
	public void delete(long id) throws DataNotExistsException, IntegrityViolationException {
	
		try {
			// To generate exception if wrong id
			userRepository.findOne(id);
			// TODO
			//preferenceRepository.deleteByUserId(id);
			// Delete Room
			userRepository.delete(id);	
		} catch (IncorrectResultSizeDataAccessException e) {
			LOGGER.debug("UserManager.delete : User #" + id + " not found", e);
			LOGGER.error("UserManager.delete : User #" + id + " not found");
			throw new DataNotExistsException("UserManager.delete : User #" + id + " not found");
		} catch(DataIntegrityViolationException e ) {
			LOGGER.debug("UserManager.delete : User associated to a room", e);
			LOGGER.error("UserManager.delete : User associated to a room");
			throw new IntegrityViolationException("UserManager.delete : User associated to a room");
		}
	}

}
