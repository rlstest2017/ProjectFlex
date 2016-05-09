package com.orange.flexoffice.dao.common.repository.data;

import java.util.Date;
import java.util.List;

import com.orange.flexoffice.dao.common.model.data.MeetingRoomDailyOccupancyDao;
import com.orange.flexoffice.dao.common.model.object.MeetingRoomDailyOccupancyDto;
import com.orange.flexoffice.dao.common.model.object.MeetingRoomDailyTypeDto;

/**
 * MeetingRoomDailyOccupancyDaoOperations
 * @author oab
 *
 */
public interface MeetingRoomDailyOccupancyDaoOperations {
	
	List<MeetingRoomDailyOccupancyDao> findAllMeetingRoomsDailyOccupancy();
	
	List<MeetingRoomDailyOccupancyDao> findRequestedMeetingRoomsDailyOccupancy(MeetingRoomDailyOccupancyDto data); // by From and To parameters
	
	MeetingRoomDailyOccupancyDao saveMeetingRoomDaily(MeetingRoomDailyOccupancyDao data);
	
	List<MeetingRoomDailyTypeDto> findMeetingRoomsDailyAndType(MeetingRoomDailyOccupancyDto data);
	
	void deleteByDay(Date day); 
	
	void deleteByMeetingRoomId(Long roomId);
			
}
