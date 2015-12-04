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
	
	RoomStatDao updateReservedRoomStat(RoomStatDao data);
	
}
