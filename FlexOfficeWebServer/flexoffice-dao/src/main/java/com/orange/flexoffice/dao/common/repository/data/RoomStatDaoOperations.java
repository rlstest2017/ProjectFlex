package com.orange.flexoffice.dao.common.repository.data;

import java.util.List;

import com.orange.flexoffice.dao.common.model.data.RoomStatDao;

/**
 * RoomStatDaoOperations
 * @author oab
 *
 */
public interface RoomStatDaoOperations {
	
	List<RoomStatDao> findAllRoomStats();
	
	List<RoomStatDao> findLatestReservedRoomsByUserId(Long userId);
	
	RoomStatDao saveReservedRoomStat(RoomStatDao data);
	
	RoomStatDao updateReservedRoomStat(RoomStatDao data);
	
}
