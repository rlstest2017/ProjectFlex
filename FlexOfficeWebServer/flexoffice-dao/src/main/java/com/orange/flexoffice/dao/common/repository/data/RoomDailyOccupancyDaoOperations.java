package com.orange.flexoffice.dao.common.repository.data;

import java.util.List;

import com.orange.flexoffice.dao.common.model.data.RoomDailyOccupancyDao;
import com.orange.flexoffice.dao.common.model.object.RoomDailyOccupancyDto;

/**
 * RoomDailyOccupancyDaoOperations
 * @author oab
 *
 */
public interface RoomDailyOccupancyDaoOperations {
	
	List<RoomDailyOccupancyDao> findAllRoomsDailyOccupancy();
	
	List<RoomDailyOccupancyDao> findRequestedRoomsDailyOccupancy(RoomDailyOccupancyDto data); // by From and To parameters
	
	RoomDailyOccupancyDao saveRoomDaily(RoomDailyOccupancyDao data);
			
}
