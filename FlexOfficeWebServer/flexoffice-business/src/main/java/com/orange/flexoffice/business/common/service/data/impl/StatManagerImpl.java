package com.orange.flexoffice.business.common.service.data.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.common.service.data.StatManager;
import com.orange.flexoffice.business.common.utils.DateTools;
import com.orange.flexoffice.dao.common.model.data.ConfigurationDao;
import com.orange.flexoffice.dao.common.model.data.RoomDailyOccupancyDao;
import com.orange.flexoffice.dao.common.model.data.RoomDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_ConfigurationKey;
import com.orange.flexoffice.dao.common.model.object.SimpleStatDto;
import com.orange.flexoffice.dao.common.repository.data.jdbc.ConfigurationDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RoomDailyOccupancyDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RoomDaoRepository;

/**
 * StatManagerImpl
 * @author oab
 */
@Service("StatManager")
@Transactional
public class StatManagerImpl implements StatManager {
	
	private static final Logger LOGGER = Logger.getLogger(StatManagerImpl.class);
	
	@Autowired
	private RoomDaoRepository roomRepository;
	@Autowired
	private RoomDailyOccupancyDaoRepository roomDailyRepository;
	@Autowired
	private ConfigurationDaoRepository configRepository;
	
	@Autowired
	private DateTools dateTools;

	@Override
	@Transactional(readOnly=true)
	public List<SimpleStatDto> getPopularStats() {
		
		LOGGER.debug("Begin method StatManager.getPopularStats");
		List<SimpleStatDto> simpleStatList = new ArrayList<SimpleStatDto>(); 
				
		// 1 - Get Date with DATE_BEGIN_DAY & DATE_END_DAY parameters
		ConfigurationDao beginDay = configRepository.findByKey(E_ConfigurationKey.DATE_BEGIN_DAY.toString());
		String  beginDayValue = beginDay.getValue(); // in hh:mm
		ConfigurationDao endDay = configRepository.findByKey(E_ConfigurationKey.DATE_END_DAY.toString());
		String  endDayValue = endDay.getValue(); // in hh:mm
		
		// 2 - Process the Dates
		Date beginDayDate = dateTools.dateBeginDay(beginDayValue);
		Date endDayDate = dateTools.dateEndDay(endDayValue); 
		
		// 3 - Calculate duration between beginDayDate & endDayDate in seconds
		Long duration = dateTools.calculateDuration(beginDayDate, endDayDate);
		LOGGER.debug("duration betwwen beginDayDate and endDayDate :" + duration);
		
		// 4 - Find All RoomDailOccupancy DATA order by roomId
		List<RoomDailyOccupancyDao> roomDailyList = roomDailyRepository.findAllRoomsDailyOccupancy();
		
		LOGGER.debug("roomDailyList size :" + roomDailyList.size());
		
		// 5 - cumulate & calculate the rates by roomId
		for (RoomDailyOccupancyDao roomDailyOccupancyDao : roomDailyList) { // the statsDaily are order by roomId 1,2,3,....
			Integer index = getStatInList(roomDailyOccupancyDao.getRoomId(), simpleStatList);
			if (index != -1) {
				// calculate occupancyDuration
				SimpleStatDto statGet = simpleStatList.get(index);
				// ------ update Occupancy Duration for existing Room (cumulate) ------
				statGet.setOccupancyDuration(statGet.getOccupancyDuration() + roomDailyOccupancyDao.getOccupancyDuration());
				statGet.setNbDaysDuration(statGet.getNbDaysDuration() + duration);
			} else {
				// add entry
				SimpleStatDto statEntry = new SimpleStatDto();
				// set Data
				statEntry.setRoomId(roomDailyOccupancyDao.getRoomId());
				statEntry.setOccupancyDuration(roomDailyOccupancyDao.getOccupancyDuration());
				statEntry.setNbDaysDuration(duration); // duration in seconds of One day
				// ------ Add new stat in the list ----
				simpleStatList.add(statEntry);
			}
		}
		
		// 6 - Calculate the rates & Get RoomNames in the simpleStatList
		for (SimpleStatDto simpleStatDto : simpleStatList) {
			// Get roomName
			RoomDao room =  roomRepository.findByRoomId(simpleStatDto.getRoomId().longValue());
			simpleStatDto.setRoomName(room.getName());
			// calculate rate
			float rate = ((float)simpleStatDto.getOccupancyDuration()*100/(float)simpleStatDto.getNbDaysDuration());
			simpleStatDto.setRate(rate);
		}
		
		LOGGER.debug("End method StatManager.getPopularStats");
		return simpleStatList;
	}
	
	private Integer getStatInList(Integer roomId, List<SimpleStatDto> simpleStatList) {
		boolean state = false;
		Integer index = -1;
		if (!simpleStatList.isEmpty()) {
			for (SimpleStatDto statSimple : simpleStatList) {
				index = index + 1;
				if (roomId == statSimple.getRoomId()) {
					state = true;
					break;
				} 
			}
		}
		
		if (!state) { // if entry not exist return -1
			index = -1;
		}
		
		return index;
	}
	
	
}
