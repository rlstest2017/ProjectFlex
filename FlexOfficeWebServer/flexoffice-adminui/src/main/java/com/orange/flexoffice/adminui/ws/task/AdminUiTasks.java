package com.orange.flexoffice.adminui.ws.task;

import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;

import com.orange.flexoffice.business.common.service.data.TaskManager;

 
public class AdminUiTasks {
	
	private static final Logger LOGGER = Logger.getLogger(AdminUiTasks.class);
	@Autowired
	TaskManager taskManager;
	
	private Properties properties;
	
	@Autowired
    @Qualifier("appProperties")
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
	
	// Every every day at 23:00
	//second, minute, hour, day of month, month, day(s) of week
	@Scheduled(cron="0 55 13 * * ?")
    public void processDailyStatsMethod()  {
		
		taskManager.processDailyStats();
		LOGGER.debug("After Method executed every day at 23:00. Current time is :: "+ new Date());
		
		String meetingroomActivated = properties.getProperty("meetingroom.activated");
		if (Boolean.TRUE.toString().equalsIgnoreCase(meetingroomActivated)){
			LOGGER.debug("Before Method executed every day at 23:00. Current time is :: "+ new Date());
			taskManager.processMeetingRoomDailyStats();
			LOGGER.debug("After Method executed every day at 23:00. Current time is :: "+ new Date());
		}
		
    }
	
	// Every 5 seconds
	@Scheduled(cron="*/5 * * * * ?")
    public void checkTeachinTimeOutMethod()  {
		LOGGER.debug("Before checkTeachinTimeoutMethod. Current time is : "+ new Date());
		taskManager.checkTeachinTimeOut();
		LOGGER.debug("After checkTeachinTimeoutMethod. Current time is : "+ new Date());
    }
	
	// Every every day at 22:00
	//second, minute, hour, day of month, month, day(s) of week
	@Scheduled(cron="0 0 22 * * ?")
    public void purgeStatsDataMethod()  {
		LOGGER.debug("Before Method purgeStatsDataMethod execute every day at 22:00. Current time is :: "+ new Date());
		taskManager.purgeStatsDataMethod();
		LOGGER.debug("After Method purgeStatsDataMethod executed every day at 22:00. Current time is :: "+ new Date());
		
		String meetingroomActivated = properties.getProperty("meetingroom.activated");
		if (Boolean.TRUE.toString().equalsIgnoreCase(meetingroomActivated)){
			LOGGER.debug("Before Method purgeStatsDataMethod execute every day at 22:00. Current time is :: "+ new Date());
			taskManager.purgeMeetingRoomStatsDataMethod();
			LOGGER.debug("After Method purgeStatsDataMethod executed every day at 22:00. Current time is :: "+ new Date());
		}
		
    }
	
	// for tests
	public boolean processDailyStatsTestMethod()  {
		LOGGER.debug("Before Method executed every day at 23:00. Current time is :: "+ new Date());
		taskManager.processDailyStats();
		LOGGER.debug("After Method executed every day at 23:00. Current time is :: "+ new Date());
		return true;
    }
	
	// for tests
	public boolean processMeetingRoomDailyStatsTestMethod()  {
		LOGGER.debug("Before Method executed every day at 23:00. Current time is :: "+ new Date());
		taskManager.processMeetingRoomDailyStats();
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
	
	// for tests
	public boolean purgeStatsDataTestMethod()  {
		LOGGER.debug("Before purgeStatsDataTestMethod. Current time is : "+ new Date());
		taskManager.purgeStatsDataMethod();
		LOGGER.debug("After purgeStatsDataTestMethod. Current time is : "+ new Date());
		return true;
	}
	
	// for tests
	public boolean purgeMeetingRoomStatsDataTestMethod()  {
		LOGGER.debug("Before purgeStatsDataTestMethod. Current time is : "+ new Date());
		taskManager.purgeMeetingRoomStatsDataMethod();
		LOGGER.debug("After purgeStatsDataTestMethod. Current time is : "+ new Date());
		return true;
	}
	
}
