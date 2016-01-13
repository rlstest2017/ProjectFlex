package com.orange.flexoffice.dao.common.repository.data;

import java.util.Date;
import java.util.List;

import com.orange.flexoffice.dao.common.model.data.RoomDailyOccupancyDao;
import com.orange.flexoffice.dao.common.model.object.RoomDailyOccupancyDto;
import com.orange.flexoffice.dao.common.model.object.RoomDailyTypeDto;

/**
 * RoomDailyOccupancyDaoOperations
 * @author oab
 *
 */
public interface RoomDailyOccupancyDaoOperations {
	
	List<RoomDailyOccupancyDao> findAllRoomsDailyOccupancy();
	
	List<RoomDailyOccupancyDao> findRequestedRoomsDailyOccupancy(RoomDailyOccupancyDto data); // by From and To parameters
	
	RoomDailyOccupancyDao saveRoomDaily(RoomDailyOccupancyDao data);
	
	List<RoomDailyTypeDto> findRoomsDailyAndType(RoomDailyOccupancyDto data);
	
	void deleteByDay(Date day); 
			
}
