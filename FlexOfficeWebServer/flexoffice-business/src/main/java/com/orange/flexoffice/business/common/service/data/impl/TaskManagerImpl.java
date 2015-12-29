package com.orange.flexoffice.business.common.service.data.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.common.service.data.TaskManager;
import com.orange.flexoffice.business.common.utils.DateTools;
import com.orange.flexoffice.dao.common.model.data.ConfigurationDao;
import com.orange.flexoffice.dao.common.model.data.RoomDailyOccupancyDao;
import com.orange.flexoffice.dao.common.model.data.RoomStatDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_ConfigurationKey;
import com.orange.flexoffice.dao.common.model.enumeration.E_RoomInfo;
import com.orange.flexoffice.dao.common.repository.data.jdbc.ConfigurationDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RoomDailyOccupancyDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RoomStatDaoRepository;

/**
 * TaskManagerImpl
 * @author oab
 */
@Service("TaskManager")
@Transactional
public class TaskManagerImpl implements TaskManager {
	
	@Autowired
	private RoomStatDaoRepository roomStatsRepository;
	@Autowired
	private ConfigurationDaoRepository configRepository;
	@Autowired
	private RoomDailyOccupancyDaoRepository roomDailyRepository;
	@Autowired
	DateTools dateTools;

	@Override
	public void checkReservationTimeOut() {
		RoomStatDao roomStat = new RoomStatDao();
		roomStat.setRoomInfo(E_RoomInfo.RESERVED.toString());
		// 1 - Get RoomStats with room_info='RESERVED'
		List<RoomStatDao> roomStats = roomStatsRepository.findbyRoomInfo(roomStat);

		// 2 - Calculate Date with BOOKIN_DURATION parameter
		ConfigurationDao bookingDuration = configRepository.findByKey(E_ConfigurationKey.BOOKING_DURATION.toString());
		int bookingDurationValue = Integer.valueOf(bookingDuration.getValue()); // in seconds
		
		// 3 - check booking duration
		for (RoomStatDao roomst : roomStats) {
			if (dateTools.reservationDateDelayBeforeTimeOut(roomst.getReservationDate(), bookingDurationValue).before(new Date())) { // expired delay !!!
				// update roomInfo='TIMEOUT
				RoomStatDao roomstat = new RoomStatDao();
				roomstat.setId(roomst.getId());
				roomstat.setRoomInfo(E_RoomInfo.TIMEOUT.toString());
				roomStatsRepository.updateRoomStatById(roomstat);
			}
		}
	}

	@Override
	public void processDailyStats() {
		
		List<RoomDailyOccupancyDao> roomDailyList = new ArrayList<RoomDailyOccupancyDao>();
			
		// 1 - Get Date with DATE_BEGIN_DAY & DATE_END_DAY parameters
		ConfigurationDao beginDay = configRepository.findByKey(E_ConfigurationKey.DATE_BEGIN_DAY.toString());
		String  beginDayValue = beginDay.getValue(); // in hh:mm
		ConfigurationDao endDay = configRepository.findByKey(E_ConfigurationKey.DATE_END_DAY.toString());
		String  endDayValue = endDay.getValue(); // in hh:mm
		
		// 2 - Process the Dates
		Date beginDayDate = dateTools.dateBeginDay(beginDayValue);
		Date endDayDate = dateTools.dateEndDay(endDayValue);
		
		// 3 - find used RoomStats in the current day
		RoomStatDao roomStat = new RoomStatDao();
		roomStat.setBeginOccupancyDate(beginDayDate);
		roomStat.setEndOccupancyDate(endDayDate);
		roomStat.setRoomInfo(E_RoomInfo.UNOCCUPIED.toString());
		List<RoomStatDao> roomSt = roomStatsRepository.findAllOccupiedDailyRoomStats(roomStat);
		
		// 4 - cumulate the stats by roomId
		for (RoomStatDao rstat : roomSt) { // the roomStats are order by roomId 1,2,3,....
			Integer index = getRoomInList(rstat.getRoomId(), roomDailyList);
			if (index != -1) {
				// calculate occupancyDuration
				Long duration = dateTools.calculateDuration(rstat.getBeginOccupancyDate(), rstat.getEndOccupancyDate());
				RoomDailyOccupancyDao roomGet = roomDailyList.get(index);
				// ------ update Occupancy Duration for existing Room (cumulate) ------
				roomGet.setOccupancyDuration(roomGet.getOccupancyDuration() + duration);
				//
			} else {
				// add entry
				RoomDailyOccupancyDao roomEntry = new RoomDailyOccupancyDao();
				// calculate occupancyDuration
				Long duration = dateTools.calculateDuration(rstat.getBeginOccupancyDate(), rstat.getEndOccupancyDate());
				roomEntry.setRoomId(rstat.getRoomId());
				roomEntry.setOccupancyDuration(duration);
				// ------ Add new room in the list ----
				roomDailyList.add(roomEntry);
			}
		}
		
		// 5 - save in Table room_daily_occupancy
		for (RoomDailyOccupancyDao roomDailyOccupancyDao : roomDailyList) {
			roomDailyRepository.saveRoomDaily(roomDailyOccupancyDao);	
		}
		
		
	}
	
	/**
	 * isRoomInList
	 * @param roomId
	 * @param roomDailyList
	 * @return
	 */
	private Integer getRoomInList(Integer roomId, List<RoomDailyOccupancyDao> roomDailyList) {
		boolean state = false;
		Integer index = -1;
		if (!roomDailyList.isEmpty()) {
			for (RoomDailyOccupancyDao roomDaily : roomDailyList) {
				index = index + 1;
				if (roomId == roomDaily.getRoomId()) {
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
