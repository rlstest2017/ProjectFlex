package com.orange.flexoffice.adminui.ws.task;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.orange.flexoffice.business.common.service.data.TaskManager;

 
public class ProcessDailyStats {
	
	private static final Logger LOGGER = Logger.getLogger(ProcessDailyStats.class);
	@Autowired
	TaskManager taskManager;
	
	// Every every day at 23:00
	//second, minute, hour, day of month, month, day(s) of week
	@Scheduled(cron="0 0 23 * * ?")
    public void processDailyStatsMethod()  {
		LOGGER.debug("Before Method executed every day at 23:00. Current time is :: "+ new Date());
		taskManager.checkReservationTimeOut();
		LOGGER.debug("After Method executed every day at 23:00. Current time is :: "+ new Date());
    }
}
