package com.orange.flexoffice.adminui.ws.task;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.orange.flexoffice.business.common.service.data.TaskManager;

/**
 * CheckTeachinTimeout
 */  
public class CheckTeachinTimeout {
	
	private static final Logger LOGGER = Logger.getLogger(CheckTeachinTimeout.class);
	@Autowired
	TaskManager taskManager;
	
	// Every 5 seconds
	@Scheduled(cron="*/5 * * * * ?")
    public void checkTeachinTimeOutMethod()  {
		LOGGER.debug("Before checkTeachinTimeoutMethod. Current time is : "+ new Date());
		taskManager.checkTeachinTimeOut();
		LOGGER.debug("After checkTeachinTimeoutMethod. Current time is : "+ new Date());
    }
	
	// for tests
	public boolean checkTeachinTimeOutTestMethod()  {
		LOGGER.debug("Before checkTeachinTimeOutTestMethod. Current time is : "+ new Date());
		taskManager.checkTeachinTimeOut();
		LOGGER.debug("After checkTeachinTimeOutTestMethod. Current time is : "+ new Date());
		return true;
    }
	
}
