package com.orange.flexoffice.business.common.service.data.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.service.data.MeetingRoomGroupsConfigurationManager;
import com.orange.flexoffice.dao.common.model.data.BuildingDao;
import com.orange.flexoffice.dao.common.model.data.DashboardDao;
import com.orange.flexoffice.dao.common.model.data.MeetingRoomGroupsConfigurationDao;
import com.orange.flexoffice.dao.common.model.object.MeetingRoomGroupsConfigurationDto;
import com.orange.flexoffice.dao.common.repository.data.jdbc.BuildingDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.DashboardDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.MeetingRoomGroupsConfigurationDaoRepository;

/**
 * Manages {@link MeetingRoomGroupsConfigurationDto}.
 * For PROD LOG LEVEL is info then we say info & error logs.
 * 
 * @author oab
 */
@Service("MeetingRoomGroupsConfigurationManager")
@Transactional
public class MeetingRoomGroupsConfigurationManagerImpl implements MeetingRoomGroupsConfigurationManager {
	
	private static final Logger LOGGER = Logger.getLogger(MeetingRoomGroupsConfigurationManagerImpl.class);
	
	@Autowired
	private MeetingRoomGroupsConfigurationDaoRepository meetingRoomGroupsConfigurationRepository;
	@Autowired
	private DashboardDaoRepository dashboardRepository;
	@Autowired
	private BuildingDaoRepository buildingRepository;
	
	@Override
	public List<String> getConfigurationFiles(String macAddress) throws DataNotExistsException {
		List<String> configFiles = new ArrayList<String>();
		List<MeetingRoomGroupsConfigurationDao> lst = new ArrayList<MeetingRoomGroupsConfigurationDao>();
		
		try {
			DashboardDao dashboardDao = dashboardRepository.findByMacAddress(macAddress);
			Long buildingId = dashboardDao.getBuildingId();
			Long floor = dashboardDao.getFloor();
			
			if (buildingId != null) {
				 if(floor != null){
					MeetingRoomGroupsConfigurationDao meetingRoomGroupsConfiguration = meetingRoomGroupsConfigurationRepository.findByBuildingIdAndFloor(dashboardDao);
					lst.add(meetingRoomGroupsConfiguration);
				 } else {
					 lst.addAll(meetingRoomGroupsConfigurationRepository.findByBuildingId(buildingId));
				 }
			} else {
				List<BuildingDao> buildings = buildingRepository.findByCityId(dashboardDao.getCityId());
				for(BuildingDao building: buildings){
					lst.addAll(meetingRoomGroupsConfigurationRepository.findByBuildingId(building.getId()));
				}
			}		
			
			for(MeetingRoomGroupsConfigurationDao configDao : lst){
				configFiles.add(configDao.getMeetingroomGroupId());
			}
		
		} catch (EmptyResultDataAccessException e){
			LOGGER.debug("MeetingRoomGroupsConfigurationManagerImpl.getConfigurationFiles: Dashboard not existing");
			throw new DataNotExistsException("MeetingRoomGroupsConfigurationManagerImpl.getConfigurationFiles: dashboard macAddress not found");
		}
		return configFiles;
	}
	
}
