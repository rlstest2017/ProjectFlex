package com.orange.flexoffice.business.common.service.data;

/**
 * TaskManager
 * @author oab
 *
 */
public interface TaskManager {

	/**
	 * checkReservationTimeOut every 5 seconds
	 * Set room_info='TIMEOUT' in RoomStats Table, if reservation_date more than BOOKING_DURATION
	 */
	void checkReservationTimeOut();
	
	/**
	 * checkTeachinTimeOut every 5 seconds
	 * If teachin is non activate since 15 minutes, it's set to ENDED  
	 */
	void checkTeachinTimeOut();
	
	/**
	 * generateDailyStats
	 * Every day at night one cumulate line is calculated from RoomStats to RoomDailyOccupancy 
	 */
	void processDailyStats();
	
	/**
	 * purgeStatsData
	 * Every day at night purge DATA Stat 
	 */
	void purgeStatsDataMethod();
	
	/**
	 * generateMeetingRoomDailyStats
	 * Every day at night one cumulate line is calculated from MeetingRoomStats to MeetingRoomDailyOccupancy 
	 */
	void processMeetingRoomDailyStats();
	
	/**
	 * purgeMeetingRoomStatsData
	 * Every day at night purge DATA Stat 
	 */
	void purgeMeetingRoomStatsDataMethod();
	
}