package com.orange.flexoffice.business.common.service.data;

import java.util.List;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.business.common.exception.RoomAlreadyUsedException;
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
	 * findRoomsByCriteria
	 * @param countryId
	 * @param regionId
	 * @param cityId
	 * @param buildingId
	 * @param floor
	 * @return
	 */
	List<RoomDao> findRoomsByCriteria(String countryId, String regionId, String cityId, String buildingId, Integer floor);
	
	/**
	 * findLatestReservedRoomsByUserId
	 * @param userId
	 * @return
	 */
	List<RoomDao> findLatestReservedRoomsByUserId(String userId);
	
	/**
	 * Finds a room by its ID.
	 * method used by adminui
	 * @param roomId
	 * 		  the {@link roomId} ID
	 * @return a {@link RoomDto}
	 */
	RoomDto find(long roomId)  throws DataNotExistsException;

	/**
	 * Saves a {@link RoomDao}
	 * method used by adminui
	 * @param roomDao
	 * 		  the new {@link RoomDao}
	 * @return a saved {@link RoomDao}
	 * @throws DataAlreadyExistsException 
	 */
	RoomDao save(RoomDao roomDao) throws DataAlreadyExistsException;

	/**
	 * Updates a {@link RoomDao}
	 * method used by adminui
	 * @param roomDao
	 * 		  the new {@link RoomDao}
	 * @return a saved {@link RoomDao}
	 */
	RoomDao update(RoomDao roomDao) throws DataNotExistsException;


	/**
	 * Updates status {@link RoomDao}
	 * method used by adminui
	 * @param roomDao
	 * 		  the new {@link RoomDao}
	 * @return a saved {@link RoomDao}
	 */
	RoomDao updateStatus(RoomDao roomDao) throws DataNotExistsException, RoomAlreadyUsedException;

	/**
	 * Deletes a room
	 * method used by adminui
	 * @param roomId 
	 * 		  a room ID
	 */
	void delete(long roomId) throws DataNotExistsException, IntegrityViolationException;

	
	/**
	 * @param name
	 * 
	 * @return RoomDao object if found
	 */
	RoomDao findByName(String name) throws DataNotExistsException;
	
	/**
	 * countRoomsByType
	 * @param type
	 * @return
	 */
	public Long countRoomsByType(String type);
	
	/**
	 * findByRoomId
	 * @param roomId
	 * @return
	 * @throws DataNotExistsException
	 */
	RoomDao findByRoomId(Long roomId) throws DataNotExistsException;
	
}