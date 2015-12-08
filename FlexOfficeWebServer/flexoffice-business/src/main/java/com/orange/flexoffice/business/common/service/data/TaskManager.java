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
	 * generateDailyStats
	 * Every day at night one cumulate line is calculated from RoomStats to RoomDailyOccupancy 
	 */
	void generateDailyStats();
	
}