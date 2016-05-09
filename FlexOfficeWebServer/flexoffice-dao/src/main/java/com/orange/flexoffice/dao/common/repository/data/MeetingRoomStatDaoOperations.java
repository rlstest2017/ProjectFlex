package com.orange.flexoffice.dao.common.repository.data;

import java.util.Date;
import java.util.List;

import org.springframework.dao.IncorrectResultSizeDataAccessException;

import com.orange.flexoffice.dao.common.model.data.MeetingRoomStatDao;

/**
 * MeetingRoomStatDaoOperations
 * @author oab
 *
 */
public interface MeetingRoomStatDaoOperations {
	
	List<MeetingRoomStatDao> findAllMeetingRoomStats();
	
	List<MeetingRoomStatDao> findAllOccupiedDailyMeetingRoomStats(MeetingRoomStatDao data); // room_info='UNOCCUPIED' => Room occupied in the day and released !!!
	
	MeetingRoomStatDao findbyMeetingRoomId(MeetingRoomStatDao data) throws IncorrectResultSizeDataAccessException;
	
	List<MeetingRoomStatDao> findbyMeetingRoomInfo(MeetingRoomStatDao data) throws IncorrectResultSizeDataAccessException;
	
	MeetingRoomStatDao saveOccupiedMeetingRoomStat(MeetingRoomStatDao data);
	
	MeetingRoomStatDao updateMeetingRoomStatById(MeetingRoomStatDao data);
	
	MeetingRoomStatDao updateEndOccupancyDate(MeetingRoomStatDao data);
	
	void deleteByBeginOccupancyDate(Date date);
	
	void deleteByMeetingRoomId(Long roomId);
		
}
