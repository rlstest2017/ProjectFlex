package com.orange.flexoffice.business.common.service.data;

/**
 * TestManager
 * @author oab
 *
 */
public interface TestManager {

	/**
	 * Init Tables 
	 * @return
	 */
	boolean executeInitTestFile();
	
	/**
	 * Drop Tables
	 * @return
	 */
	boolean executeDropTables();

	/**
	 * Initialise RoomStats tests data in DB
	 * @return true if successfully done
	 */
	boolean initRoomStatsTableForUserUI();
	
	/**
	 * Initialise RoomStats tests data in DB
	 * @return true if successfully done
	 */
	boolean initRoomStatsTableForAdminUI();
	
	/**
	 * Initialise RoomDailyOccupancy tests data in DB by DAY
	 * @return true if successfully done
	 */
	boolean initRoomDailyOccupancyTable();
	
	/**
	 * Initialise RoomDailyOccupancy tests data in DB by MONTH
	 * @return true if successfully done
	 */
	boolean initRoomMonthlyOccupancyTable();
	
	/**
	 * Initialise MeetingRoomStats tests data in DB
	 * @return true if successfully done
	 */
	boolean initMeetingRoomStatsTableForAdminUI();
	
	/**
	 * Initialise MeetingRoomDailyOccupancy tests data in DB by DAY
	 * @return true if successfully done
	 */
	boolean initMeetingRoomDailyOccupancyTable();
	
	/**
	 * Initialise MeetingRoomDailyOccupancy tests data in DB by MONTH
	 * @return true if successfully done
	 */
	boolean initMeetingRoomMonthlyOccupancyTable();
	
	/**
	 * initTeachinSensorsTable
	 * @return
	 */
	boolean initTeachinSensorsTable();
	
	/**
	 * setTeachinSensorsTable
	 * @return
	 */
	boolean setTeachinSensorsTable();
	
}