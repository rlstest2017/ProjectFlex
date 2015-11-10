package com.orange.flexoffice.business.common.service.data.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
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
	private UserDaoRepository UserDaoRepository;
	
	@Transactional(readOnly=true)
	public List<UserDao> findAllUsers() {
		return UserDaoRepository.findAllUsers();
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
		return UserDaoRepository.findOne(UserDaoId);
	}

	/**
	 * Saves a {@link UserDao}
	 * 
	 * @param UserDao
	 * 		  the new {@link UserDao}
	 * @return a saved {@link UserDao}
	 * @throws DataAlreadyExistsException 
	 */
	public UserDao save(UserDao UserDao) throws DataAlreadyExistsException {
		String userEmail = UserDao.getEmail();
		
		LOGGER.debug("UserMail : " + userEmail);
		
		List<UserDao> testUserDao = UserDaoRepository.findByUserEmail(userEmail);
		if ((testUserDao != null)&&(testUserDao.size() > 0)) {
			LOGGER.debug("testUserDao.size() : " + testUserDao.size());
			throw new DataAlreadyExistsException("UserDao already saves.");
		}
		
		// Saves UserDao
		return UserDaoRepository.saveUser(UserDao);
	}
	
	/**
	 * Updates a {@link UserDao}
	 * 
	 * @param UserDao
	 * 		  the new {@link UserDao}
	 * @return a saved {@link UserDao}
	 */
	public UserDao update(UserDao UserDao) throws DataNotExistsException {
		Long userId = UserDao.getId();
		UserDao testUserDao = UserDaoRepository.findOne(UserDao.getId());
		
		if (testUserDao == null) {
			LOGGER.debug("user by id " + userId + " is not found");
			throw new DataNotExistsException("UserDao already saves.");
		}
		
		// Saves UserDao
		return UserDaoRepository.updateUser(UserDao);
	}

	/**
	 * Deletes a {@link UserDao}.
	 * 
	 * @param id 
	 * 		  a {@link UserDao} ID
	 */
	public void delete(long id) throws DataNotExistsException {
	
		UserDao testUserDao = UserDaoRepository.findOne(id);
		
		if (testUserDao == null) {
			LOGGER.debug("user by id " + id + " is not found");
			throw new DataNotExistsException("UserDao already saves.");
		}
		
		// Deletes UserDao
		UserDaoRepository.delete(id);
	}
	
	/**
	 * 
	 * @param userEmail
	 * @return
	 */
	public UserDao findByUserMail(String userEmail) {
		UserDao returnedUserDao = new UserDao();
		
		List<UserDao> testUserDao = UserDaoRepository.findByUserEmail(userEmail);
		if ((testUserDao != null)&&(testUserDao.size() > 0)) {
			returnedUserDao = testUserDao.get(0);
		}
		
		// Saves UserDao
		return returnedUserDao;
	}
	
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-business.xml");
		UserManager UserDaoManager = (UserManager) context.getBean("UserDaoManager");
		
		for (int i = 0; i < 1000; i++) {
			UserDao UserDao = new UserDao();
			UserDao.setId((long)i%10);
			
			try {
				UserDaoManager.save(UserDao);
			} catch (DataAlreadyExistsException e) {
				// TODO Auto-generated catch block
				System.out.println("test");
			}
		}
		
	}

}
