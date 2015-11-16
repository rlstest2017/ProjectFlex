package com.orange.flexoffice.dao.common.repository.data;

import java.util.List;

import com.orange.flexoffice.dao.common.model.data.GatewayDao;
import com.orange.flexoffice.dao.common.model.data.RoomDao;
import com.orange.flexoffice.dao.common.model.data.UserDao;

/**
 * RoomDaoOperations
 * @author oab
 *
 */
public interface RoomDaoOperations {
	
	List<RoomDao> findAllRooms();
	
	List<RoomDao> findByRoomId(Long roomId);
	
	List<RoomDao> findByGatewayId(Long gatewayId);

	List<RoomDao> findByName(String name);
	
	RoomDao saveRoom(RoomDao data);
	
	RoomDao updateRoom(RoomDao data); 
	
	RoomDao updateRoomStatus(RoomDao data); 	
}
