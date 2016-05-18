package com.orange.flexoffice.business.common.service.data;

import java.util.List;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.business.common.exception.RoomAlreadyUsedException;
import com.orange.flexoffice.dao.common.model.data.MeetingRoomDao;
import com.orange.flexoffice.dao.common.model.object.MeetingRoomDto;

/**
 * MeetingRoomManager
 * @author oab
 *
 */
public interface MeetingRoomManager {
	/**
	 * findAllMeetingRooms method used by adminui
	 * @return
	 */
	List<MeetingRoomDao> findAllMeetingRooms();

	/**
	 * findMeetingRoomsByCriteria
	 * @param countryId
	 * @param regionId
	 * @param cityId
	 * @param buildingId
	 * @param floor
	 * @return
	 */
	List<MeetingRoomDao> findMeetingRoomsByCriteria(String countryId, String regionId, String cityId, String buildingId, Integer floor);
	
	/**
	 * findLatestReservedMeetingRoomsByUserId
	 * @param userId
	 * @return
	 */
	List<MeetingRoomDao> findLatestReservedMeetingRoomsByUserId(String userId);
	
	/**
	 * Finds a meeting room by its ID.
	 * method used by adminui
	 * @param roomId the {@link roomId} ID
	 * @return a {@link MeetingRoomDto}
	 */
	MeetingRoomDto find(long meetingroomId)  throws DataNotExistsException;

	/**
	 * Saves a {@link MeetingRoomDao}
	 * method used by adminui
	 * @param roomDao the new {@link MeetingRoomDao}
	 * @return a saved {@link MeetingRoomDao}
	 * @throws DataAlreadyExistsException 
	 */
	MeetingRoomDao save(MeetingRoomDao meetingroomDao) throws DataAlreadyExistsException;

	/**
	 * Updates a {@link MeetingRoomDao}
	 * method used by adminui
	 * @param roomDao the new {@link MeetingRoomDao}
	 * @return a saved {@link MeetingRoomDao}
	 * @throws DataAlreadyExistsException 
	 */
	MeetingRoomDao update(MeetingRoomDao meetingroomDao) throws DataNotExistsException, DataAlreadyExistsException;


	/**
	 * Updates status {@link MeetingRoomDao}
	 * method used by adminui
	 * @param roomDao the new {@link MeetingRoomDao}
	 * @return a saved {@link MeetingRoomDao}
	 */
	MeetingRoomDao updateStatus(MeetingRoomDao meetingroomDao) throws DataNotExistsException, RoomAlreadyUsedException;
	
	/**
	 * Updates Data {@link MeetingRoomDao}
	 * method used by meetingroomapi
	 * @param roomDao the new {@link MeetingRoomDao}
	 * @return a saved {@link MeetingRoomDao}
	 */
	MeetingRoomDao updateData(MeetingRoomDao meetingroomDao) throws DataNotExistsException, RoomAlreadyUsedException;

	/**
	 * Deletes a meeting room
	 * method used by adminui
	 * @param meetingroomId a meeting room ID
	 */
	void delete(long meetingroomId) throws DataNotExistsException, IntegrityViolationException;

	
	/**
	 * @param name
	 * 
	 * @return MeetingRoomDao object if found
	 */
	MeetingRoomDao findByName(String name) throws DataNotExistsException;
	
	/**
	 * countMeetingRoomsByType
	 * @param type
	 * @return
	 */
	public Long countMeetingRoomsByType(String type);
	
	/**
	 * findByMeetingRoomId
	 * @param meetingroomId
	 * @return
	 * @throws DataNotExistsException
	 */
	MeetingRoomDao findByMeetingRoomId(Long meetingroomId) throws DataNotExistsException;
	
}