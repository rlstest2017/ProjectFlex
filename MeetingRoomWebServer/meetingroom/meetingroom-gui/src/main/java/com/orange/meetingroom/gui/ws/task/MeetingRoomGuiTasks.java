package com.orange.meetingroom.gui.ws.task;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import com.orange.meetingroom.business.service.TaskManager;

/**
 * MeetingRoomGuiTasks 
 * @author oab
 *
 */
public class MeetingRoomGuiTasks {
	
	private static final Logger LOGGER = Logger.getLogger(MeetingRoomGuiTasks.class);
	@Autowired
	TaskManager taskManager;
	

	// Every 5 minutes
	//second, minute, hour, day of month, month, day(s) of week
	@Scheduled(cron="0 */5 * * * ?")
    public void checkMeetingRoomsStatusTimeOutMethod()  {
		LOGGER.debug("Before checkMeetingRoomsStatusTimeOutMethod. Current time is : "+ new Date());
		taskManager.checkMeetingRoomTimeOut();
		LOGGER.debug("After checkMeetingRoomsStatusTimeOutMethod. Current time is : "+ new Date());
    }

	// for tests
	public boolean checkMeetingRoomsStatusTimeOutTestMethod()  {
		LOGGER.debug("Before checkMeetingRoomsStatusTimeOutTestMethod. Current time is : "+ new Date());
		taskManager.checkMeetingRoomTimeOut();
		LOGGER.debug("After checkMeetingRoomsStatusTimeOutTestMethod. Current time is : "+ new Date());
		return true;
    }
	
}
