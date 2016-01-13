package com.orange.flexoffice.adminui.ws.task;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.orange.flexoffice.business.common.service.data.TaskManager;

 
public class AdminUiTasks {
	
	private static final Logger LOGGER = Logger.getLogger(AdminUiTasks.class);
	@Autowired
	TaskManager taskManager;
	
	// Every every day at 23:00
	//second, minute, hour, day of month, month, day(s) of week
	@Scheduled(cron="0 0 23 * * ?")
    public void processDailyStatsMethod()  {
		LOGGER.debug("Before Method executed every day at 23:00. Current time is :: "+ new Date());
		taskManager.processDailyStats();
		LOGGER.debug("After Method executed every day at 23:00. Current time is :: "+ new Date());
    }
	
	// Every 5 seconds
	@Scheduled(cron="*/5 * * * * ?")
    public void checkTeachinTimeOutMethod()  {
		LOGGER.debug("Before checkTeachinTimeoutMethod. Current time is : "+ new Date());
		taskManager.checkTeachinTimeOut();
		LOGGER.debug("After checkTeachinTimeoutMethod. Current time is : "+ new Date());
    }
	
	// for tests
	public boolean processDailyStatsTestMethod()  {
		LOGGER.debug("Before Method executed every day at 23:00. Current time is :: "+ new Date());
		taskManager.processDailyStats();
		LOGGER.debug("After Method executed every day at 23:00. Current time is :: "+ new Date());
		return true;
    }
	
	// for tests
	public boolean checkTeachinTimeOutTestMethod()  {
		LOGGER.debug("Before checkTeachinTimeOutTestMethod. Current time is : "+ new Date());
		taskManager.checkTeachinTimeOut();
		LOGGER.debug("After checkTeachinTimeOutTestMethod. Current time is : "+ new Date());
		return true;
    }
	
}
