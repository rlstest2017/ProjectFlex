package com.orange.flexoffice.business.gatewayapi.service.data.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.gatewayapi.admin.AdminService;
import com.orange.flexoffice.business.gatewayapi.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.gatewayapi.matrix.SimilarityMatrix;
import com.orange.flexoffice.business.gatewayapi.matrix.SimilarityMatrixType;
import com.orange.flexoffice.business.gatewayapi.service.data.LogManager;
import com.orange.flexoffice.dao.gatewayapi.model.Stat;
import com.orange.flexoffice.dao.gatewayapi.model.data.Log;
import com.orange.flexoffice.dao.gatewayapi.repository.data.jdbc.LogRepository;
import com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc.LogItemStatRepository;
import com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc.LogUserStatRepository;


/**
 * Manages {@link Log}.
 * 
 * @author Guillaume Mouricou
 */
@Service("logManager")
@Transactional
public class LogManagerImpl implements LogManager {
	
	@Autowired
	private LogRepository logRepository;
	@Autowired
	private LogUserStatRepository logUserStatRepository;
	@Autowired
	private LogItemStatRepository logItemStatRepository;

	/**
	 * Finds a {@link Log} by its ID.
	 * 
	 * @param logId
	 * 		  the {@link Log} ID
	 * @return a {@link Log}
	 */
	@Transactional(readOnly=true)
	public Log find(long logId) {
		return logRepository.findOne(logId);
	}

	/**
	 * Saves a {@link Log}
	 * 
	 * @param log
	 * 		  the new {@link Log}
	 * @return a saved {@link Log}
	 * @throws DataAlreadyExistsException 
	 */
	public Log save(Log log) throws DataAlreadyExistsException {
		String userId = log.getUserId();
		String itemId = log.getItemId();
		Float rating = log.getRating();
		
		Log testLog = logRepository.findByUserIdAndItemId(userId, itemId);
		if (testLog != null) {
			throw new DataAlreadyExistsException("Log already saves.");
		}
		
		// Manages statistics
		Stat userStat = logUserStatRepository.findByObjectId(userId);
		if (userStat == null) {
			userStat = new Stat();
			userStat.setObjectId(userId);
			userStat.addRating(rating);
			logUserStatRepository.save(userStat);
		} else {
			userStat.addRating(log.getRating());
			logUserStatRepository.update(userStat);
		}
		
		Stat itemStat = logItemStatRepository.findByObjectId(itemId);
		if (itemStat == null) {
			itemStat = new Stat();
			itemStat.setObjectId(itemId);
			itemStat.addRating(rating);
			logItemStatRepository.save(itemStat);
		} else {
			itemStat.addRating(log.getRating());
			logItemStatRepository.update(itemStat);
		}
		
		// Saves log
		return logRepository.save(log);
	}
	
	/**
	 * Updates a {@link Log}
	 * 
	 * @param log
	 * 		  the new {@link Log}
	 * @return a saved {@link Log}
	 */
	public Log update(Log log) {
		String userId = log.getUserId();
		String itemId = log.getItemId();
		Float rating = log.getRating();
		
		Log oldLog = logRepository.findOne(log.getId());

		if (!rating.equals(oldLog.getRating())) {
			// Manages statistics
			Stat userStat = logUserStatRepository.findByObjectId(userId);
			userStat.removeRating(oldLog.getRating());
			userStat.addRating(rating);
			logUserStatRepository.update(userStat);
			
			Stat itemStat = logItemStatRepository.findByObjectId(itemId);
			itemStat.removeRating(oldLog.getRating());
			itemStat.addRating(rating);
			logItemStatRepository.update(itemStat);
		}
		
		// Saves log
		return logRepository.update(log);
	}

	/**
	 * Deletes a {@link Log}.
	 * 
	 * @param id 
	 * 		  a {@link Log} ID
	 */
	public void delete(long id) {
		Log log = logRepository.findOne(id);
		String userId = log.getUserId();
		String itemId = log.getItemId();
		Float rating = log.getRating();
		
		// Manages statistics
		Stat userStat = logUserStatRepository.findByObjectId(userId);
		userStat.removeRating(rating);
		if (userStat.getCount() > 0) {
			logUserStatRepository.update(userStat);
		} else {
			logUserStatRepository.delete(userStat.getId());
		}
		
		Stat itemStat = logItemStatRepository.findByObjectId(itemId);
		itemStat.removeRating(rating);
		if (itemStat.getCount() > 0) {
			logItemStatRepository.update(itemStat);
		} else {
			logItemStatRepository.delete(itemStat.getId());
		}
		
		// Deletes log
		logRepository.delete(id);
	}
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-business.xml");
		LogManager logManager = (LogManager) context.getBean("logManager");
		
		for (int i = 0; i < 1000; i++) {
			Log log = new Log();
			log.setUserId("user" + i%10);
			log.setItemId("item" + (int)(Math.random() * 20));
			log.setRating(Double.valueOf(Math.random() * 5).floatValue());
			try {
				logManager.save(log);
			} catch (DataAlreadyExistsException e) {
				// TODO Auto-generated catch block
				System.out.println("test");
			}
		}
		
		AdminService adminService = (AdminService) context.getBean("adminService");
		adminService.generateSimilarityMatrix(SimilarityMatrixType.SIMI_ITEM_LOGS, 100, 1);
	}

}
