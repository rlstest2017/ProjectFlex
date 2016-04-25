package com.orange.flexoffice.dao.common.repository.data;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import com.orange.flexoffice.dao.common.model.data.MeetingRoomDao;
import com.orange.flexoffice.dao.common.model.object.MeetingRoomBuildingInfosDto;

/**
 * MeetingRoomDaoOperations
 * @author oab
 *
 */
public interface MeetingRoomDaoOperations {
	
	List<MeetingRoomDao> findAllMeetingRooms();
	
	List<MeetingRoomDao> findMeetingRoomsByCountryId(Long countryId);
	
	List<MeetingRoomDao> findMeetingRoomsByRegionId(Long regionId);
	
	List<MeetingRoomDao> findMeetingRoomsByCityId(Long cityId);
	
	List<MeetingRoomDao> findMeetingRoomsByBuildingId(Long buildingId);

	List<MeetingRoomDao> findMeetingRoomsByBuildingIdAndFloor(MeetingRoomBuildingInfosDto data);
	
	MeetingRoomDao findByMeetingRoomId(Long roomId) throws IncorrectResultSizeDataAccessException;
	
	MeetingRoomDao findByAgentId(Long agentId) throws IncorrectResultSizeDataAccessException;

	MeetingRoomDao findByName(String name) throws IncorrectResultSizeDataAccessException;
	
	MeetingRoomDao saveMeetingRoom(MeetingRoomDao data) throws DataIntegrityViolationException;
	
	MeetingRoomDao updateMeetingRoom(MeetingRoomDao data) throws DataAccessException; 
	
	MeetingRoomDao updateMeetingRoomStatus(MeetingRoomDao data) throws DataAccessException;
	
	Long countMeetingRoomsByType(String type);
}
