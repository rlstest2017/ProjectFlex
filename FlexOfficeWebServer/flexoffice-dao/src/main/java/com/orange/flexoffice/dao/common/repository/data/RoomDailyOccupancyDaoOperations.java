package com.orange.flexoffice.dao.common.repository.data;

import java.util.List;

import com.orange.flexoffice.dao.common.model.data.RoomDailyOccupancyDao;

/**
 * RoomDailyOccupancyDaoOperations
 * @author oab
 *
 */
public interface RoomDailyOccupancyDaoOperations {
	
	List<RoomDailyOccupancyDao> findAllRoomsDailyOccupancy();
	
	RoomDailyOccupancyDao saveRoomDaily(RoomDailyOccupancyDao data);
			
}
