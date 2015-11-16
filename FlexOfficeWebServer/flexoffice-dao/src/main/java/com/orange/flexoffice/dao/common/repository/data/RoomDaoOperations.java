package com.orange.flexoffice.dao.common.repository.data;

import java.util.List;

import com.orange.flexoffice.dao.common.model.data.RoomDao;

/**
 * RoomDaoOperations
 * @author oab
 *
 */
public interface RoomDaoOperations {
	
	List<RoomDao> findAllRooms();
	
	RoomDao findByRoomId(Long roomId);
	
	List<RoomDao> findByGatewayId(Long gatewayId);

	RoomDao findByName(String name);
	
	RoomDao saveRoom(RoomDao data);
	
	RoomDao updateRoom(RoomDao data); 
	
	RoomDao updateRoomStatus(RoomDao data); 	
}
