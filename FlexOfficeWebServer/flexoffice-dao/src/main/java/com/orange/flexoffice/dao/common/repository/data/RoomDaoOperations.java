package com.orange.flexoffice.dao.common.repository.data;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import com.orange.flexoffice.dao.common.model.data.RoomDao;

/**
 * RoomDaoOperations
 * @author oab
 *
 */
public interface RoomDaoOperations {
	
	List<RoomDao> findAllRooms();
	
	RoomDao findByRoomId(Long roomId) throws IncorrectResultSizeDataAccessException;
	
	List<RoomDao> findByGatewayId(Long gatewayId) throws IncorrectResultSizeDataAccessException;

	RoomDao findByName(String name) throws IncorrectResultSizeDataAccessException;
	
	RoomDao findByUserId(Long userId) throws IncorrectResultSizeDataAccessException;
	
	RoomDao saveRoom(RoomDao data) throws DataIntegrityViolationException;
	
	RoomDao updateRoom(RoomDao data) throws DataAccessException; 
	
	RoomDao updateRoomStatus(RoomDao data) throws DataAccessException; 	
}
