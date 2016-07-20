package com.orange.flexoffice.meetingroomapi.ws.task;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.orange.flexoffice.business.common.service.data.TaskManager;

 
public class MeetingRoomApiTasks {
	
	private static final Logger LOGGER = Logger.getLogger(MeetingRoomApiTasks.class);
	@Autowired
	TaskManager taskManager;
	

	// Every 4 minutes
	@Scheduled(cron="0 0/4 * * * ?")
    public void checkAgentDashboardTimeOutMethod()  {
		LOGGER.debug("Before checkAgentDashboardTimeOutMethod. Current time is : "+ new Date());
		taskManager.checkAgentDashboardTimeOut();
		LOGGER.debug("After checkAgentDashboardTimeOutMethod. Current time is : "+ new Date());
    }
	

	// for tests
	public boolean checkAgentDashboardTimeOutTestMethod()  {
		LOGGER.debug("Before checkAgentDashboardTimeOutTestMethod. Current time is : "+ new Date());
		taskManager.checkAgentDashboardTimeOut();
		LOGGER.debug("After checkAgentDashboardTimeOutTestMethod. Current time is : "+ new Date());
		return true;
    }
	
}
