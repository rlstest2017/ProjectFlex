package com.orange.flexoffice.dao.common.repository.data;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;

import com.orange.flexoffice.dao.common.model.data.DashboardDao;
import com.orange.flexoffice.dao.common.model.data.MeetingRoomGroupsConfigurationDao;

/**
 * MeetingRoomGroupsConfigurationDaoOperations
 * @author oab
 *
 */
public interface MeetingRoomGroupsConfigurationDaoOperations {
	
	List<MeetingRoomGroupsConfigurationDao> findAllMeetingRoomGroupsConfigurations();
	
	List<MeetingRoomGroupsConfigurationDao> findByBuildingId(Long buildingId);
	
	MeetingRoomGroupsConfigurationDao findByBuildingIdAndFloor(DashboardDao data);
	
	MeetingRoomGroupsConfigurationDao saveMeetingRoomGroupsConfiguration(MeetingRoomGroupsConfigurationDao data) throws DataIntegrityViolationException;
	
	MeetingRoomGroupsConfigurationDao updateMeetingRoomGroupsConfiguration(MeetingRoomGroupsConfigurationDao data);
	
	void deleteByBuildingId(Long buildingId); 
	
	void deleteByBuildingIdAndFloor(MeetingRoomGroupsConfigurationDao data); 
}
