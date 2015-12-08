package com.orange.flexoffice.business.common.service.data.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.common.service.data.TaskManager;
import com.orange.flexoffice.business.common.utils.DateTools;
import com.orange.flexoffice.dao.common.model.data.ConfigurationDao;
import com.orange.flexoffice.dao.common.model.data.RoomStatDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_ConfigurationKey;
import com.orange.flexoffice.dao.common.model.enumeration.E_RoomInfo;
import com.orange.flexoffice.dao.common.repository.data.jdbc.ConfigurationDaoRepository;
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
	public void generateDailyStats() {
		// TODO Auto-generated method stub
		
	}
	
}