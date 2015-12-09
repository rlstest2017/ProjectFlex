package com.orange.flexoffice.userui.ws.task;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.orange.flexoffice.business.common.service.data.TaskManager;

 
public class CheckReservationTimeOutTask {
	
	private static final Logger LOGGER = Logger.getLogger(CheckReservationTimeOutTask.class);
	@Autowired
	TaskManager taskManager;
	
	// Every 5 secondes
	@Scheduled(cron="*/5 * * * * ?")
    public boolean checkReservationMethod()  {
		LOGGER.debug("Before Method executed at every 5 seconds. Current time is :: "+ new Date());
		taskManager.checkReservationTimeOut();
		LOGGER.debug("After Method executed at every 5 seconds. Current time is :: "+ new Date());
		return true;
    }
}
