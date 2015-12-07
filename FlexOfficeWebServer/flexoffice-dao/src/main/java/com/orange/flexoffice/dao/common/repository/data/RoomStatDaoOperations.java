package com.orange.flexoffice.dao.common.repository.data;

import java.util.List;

import org.springframework.dao.IncorrectResultSizeDataAccessException;

import com.orange.flexoffice.dao.common.model.data.RoomStatDao;

/**
 * RoomStatDaoOperations
 * @author oab
 *
 */
public interface RoomStatDaoOperations {
	
	List<RoomStatDao> findAllRoomStats();
	
	List<RoomStatDao> findLatestReservedRoomsByUserId(Long userId)  throws IncorrectResultSizeDataAccessException;
	
	RoomStatDao saveReservedRoomStat(RoomStatDao data);
	
	RoomStatDao saveOccupiedRoomStat(RoomStatDao data);
	
	RoomStatDao updateReservedRoomStat(RoomStatDao data);
	
	RoomStatDao findbyRoomId(RoomStatDao data) throws IncorrectResultSizeDataAccessException;
	
	RoomStatDao updateBeginOccupancyDate(RoomStatDao data);
	
	RoomStatDao updateEndOccupancyDate(RoomStatDao data);
		
}
