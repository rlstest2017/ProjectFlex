package com.orange.flexoffice.business.userui.service.data.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.userui.service.data.UserFlexofficeManager;
import com.orange.flexoffice.dao.userui.model.data.UserFlexoffice;
import com.orange.flexoffice.dao.userui.repository.data.jdbc.UserFlexofficeRepository;

/**
 * Manages {@link UserFlexoffice}.
 * 
 * @author oab
 */
@Service("UserFlexofficeManager")
@Transactional
public class UserFlexofficeManagerImpl implements UserFlexofficeManager {
	
	private final Logger LOGGER = Logger.getLogger(UserFlexofficeManagerImpl.class);
	
	@Autowired
	private UserFlexofficeRepository UserFlexofficeRepository;
	
	/**
	 * Finds a {@link UserFlexoffice} by its ID.
	 * 
	 * @param UserFlexofficeId
	 * 		  the {@link UserFlexoffice} ID
	 * @return a {@link UserFlexoffice}
	 */
	@Transactional(readOnly=true)
	public UserFlexoffice find(long UserFlexofficeId) {
		return UserFlexofficeRepository.findOne(UserFlexofficeId);
	}

	/**
	 * Saves a {@link UserFlexoffice}
	 * 
	 * @param UserFlexoffice
	 * 		  the new {@link UserFlexoffice}
	 * @return a saved {@link UserFlexoffice}
	 * @throws DataAlreadyExistsException 
	 */
	public UserFlexoffice save(UserFlexoffice UserFlexoffice) throws DataAlreadyExistsException {
		String userEmail = UserFlexoffice.getEmail();
		
		LOGGER.debug("UserMail : " + userEmail);
		
		List<UserFlexoffice> testUserFlexoffice = UserFlexofficeRepository.findByUserEmail(userEmail);
		if ((testUserFlexoffice != null)&&(testUserFlexoffice.size() > 0)) {
			LOGGER.debug("testUserFlexoffice.size() : " + testUserFlexoffice.size());
			throw new DataAlreadyExistsException("UserFlexoffice already saves.");
		}
		
		// Saves UserFlexoffice
		return UserFlexofficeRepository.saveUser(UserFlexoffice);
	}
	
	/**
	 * Updates a {@link UserFlexoffice}
	 * 
	 * @param UserFlexoffice
	 * 		  the new {@link UserFlexoffice}
	 * @return a saved {@link UserFlexoffice}
	 */
	public UserFlexoffice update(UserFlexoffice userFlexoffice) throws DataNotExistsException {
		Long userId = userFlexoffice.getId();
		UserFlexoffice testUserFlexoffice = UserFlexofficeRepository.findOne(userFlexoffice.getId());
		
		if (testUserFlexoffice == null) {
			LOGGER.debug("user by id " + userId + " is not found");
			throw new DataNotExistsException("UserFlexoffice already saves.");
		}
		
		// Saves UserFlexoffice
		return UserFlexofficeRepository.updateUser(userFlexoffice);
	}

	/**
	 * Deletes a {@link UserFlexoffice}.
	 * 
	 * @param id 
	 * 		  a {@link UserFlexoffice} ID
	 */
	public void delete(long id) {
		//UserFlexoffice UserFlexoffice = UserFlexofficeRepository.findOne(id);
		//String userId = UserFlexoffice.getUserId();
		
		// Deletes UserFlexoffice
		UserFlexofficeRepository.delete(id);
	}
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-business.xml");
		UserFlexofficeManager UserFlexofficeManager = (UserFlexofficeManager) context.getBean("UserFlexofficeManager");
		
		for (int i = 0; i < 1000; i++) {
			UserFlexoffice UserFlexoffice = new UserFlexoffice();
			UserFlexoffice.setId((long)i%10);
			
			try {
				UserFlexofficeManager.save(UserFlexoffice);
			} catch (DataAlreadyExistsException e) {
				// TODO Auto-generated catch block
				System.out.println("test");
			}
		}
		
	}

}
