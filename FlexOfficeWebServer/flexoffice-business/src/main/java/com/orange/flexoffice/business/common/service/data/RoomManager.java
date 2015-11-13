package com.orange.flexoffice.business.common.service.data;

import java.util.List;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.dao.common.model.data.RoomDao;
import com.orange.flexoffice.dao.common.model.object.RoomDto;

/**
 * RoomManager
 * @author oab
 *
 */
public interface RoomManager {
	/**
	 * findAllRooms method used by adminui
	 * @return
	 */
	List<RoomDao> findAllRooms();

	/**
	 * Finds a room by its ID.
	 * method used by adminui
	 * @param roomId
	 * 		  the {@link roomId} ID
	 * @return a {@link RoomDto}
	 */
	RoomDto find(long roomId)  throws DataNotExistsException;

	/**
	 * Saves a {@link RoomDto}
	 * method used by adminui
	 * @param roomDto
	 * 		  the new {@link RoomDto}
	 * @return a saved {@link RoomDto}
	 * @throws DataAlreadyExistsException 
	 */
	RoomDto save(RoomDto roomDto) throws DataAlreadyExistsException;

	/**
	 * Updates a {@link RoomDto}
	 * method used by adminui
	 * @param roomDto
	 * 		  the new {@link RoomDto}
	 * @return a saved {@link RoomDto}
	 */
	RoomDto update(RoomDto roomDto) throws DataNotExistsException;

	/**
	 * Deletes a room
	 * method used by adminui
	 * @param roomId 
	 * 		  a room ID
	 */
	void delete(long roomId) throws DataNotExistsException;

}