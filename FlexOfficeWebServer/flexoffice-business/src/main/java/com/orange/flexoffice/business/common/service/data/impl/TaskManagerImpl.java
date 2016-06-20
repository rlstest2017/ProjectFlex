package com.orange.flexoffice.business.common.service.data.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.common.service.data.AlertManager;
import com.orange.flexoffice.business.common.service.data.TaskManager;
import com.orange.flexoffice.business.common.utils.DateTools;
import com.orange.flexoffice.dao.common.model.data.AgentDao;
import com.orange.flexoffice.dao.common.model.data.ConfigurationDao;
import com.orange.flexoffice.dao.common.model.data.DashboardDao;
import com.orange.flexoffice.dao.common.model.data.MeetingRoomDailyOccupancyDao;
import com.orange.flexoffice.dao.common.model.data.MeetingRoomStatDao;
import com.orange.flexoffice.dao.common.model.data.RoomDailyOccupancyDao;
import com.orange.flexoffice.dao.common.model.data.RoomDao;
import com.orange.flexoffice.dao.common.model.data.RoomStatDao;
import com.orange.flexoffice.dao.common.model.data.TeachinSensorDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_AgentStatus;
import com.orange.flexoffice.dao.common.model.enumeration.E_ConfigurationKey;
import com.orange.flexoffice.dao.common.model.enumeration.E_DashboardStatus;
import com.orange.flexoffice.dao.common.model.enumeration.E_MeetingRoomInfo;
import com.orange.flexoffice.dao.common.model.enumeration.E_RoomInfo;
import com.orange.flexoffice.dao.common.model.enumeration.E_RoomStatus;
import com.orange.flexoffice.dao.common.model.enumeration.E_TeachinStatus;
import com.orange.flexoffice.dao.common.repository.data.jdbc.AgentDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.ConfigurationDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.DashboardDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.MeetingRoomDailyOccupancyDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.MeetingRoomStatDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RoomDailyOccupancyDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RoomDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.RoomStatDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.TeachinSensorsDaoRepository;

/**
 * TaskManagerImpl
 * For PROD LOG LEVEL is info then we say info & error logs.
 * @author oab
 */
@Service("TaskManager")
@Transactional
public class TaskManagerImpl implements TaskManager {
	
	private static final Logger LOGGER = Logger.getLogger(TaskManagerImpl.class);
	
	@Autowired
	private RoomStatDaoRepository roomStatsRepository;
	@Autowired
	private MeetingRoomStatDaoRepository meetingRoomStatsRepository;
	@Autowired
	private RoomDaoRepository roomRepository;
	@Autowired
	private ConfigurationDaoRepository configRepository;
	@Autowired
	private RoomDailyOccupancyDaoRepository roomDailyRepository;
	@Autowired
	private MeetingRoomDailyOccupancyDaoRepository meetingRoomDailyRepository;
	@Autowired
	private TeachinSensorsDaoRepository teachinRepository;
	@Autowired
	private AgentDaoRepository agentRepository;
	@Autowired
	private DashboardDaoRepository dashboardRepository;
	@Autowired
	private AlertManager alertManager;
	
	@Autowired
	DateTools dateTools;

	@Override
	public void checkReservationTimeOut() {
		
		LOGGER.debug(" Begin TaskManager.checkReservationTimeOut method : " + new Date());
		
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
				// update roomInfo='TIMEOUT'
				RoomStatDao roomstat = new RoomStatDao();
				roomstat.setId(roomst.getId());
				roomstat.setRoomInfo(E_RoomInfo.TIMEOUT.toString());
				roomStatsRepository.updateRoomStatById(roomstat);
				// update roomStatus='FREE'
				RoomDao room = roomRepository.findByRoomId(Long.valueOf(roomst.getRoomId()));
				room.setStatus(E_RoomStatus.FREE.toString());
				room.setUserId(null);
				roomRepository.updateRoomStatus(room);
				
				LOGGER.info("TaskManager.checkReservationTimeOut roomStat#"+roomstat.getId() + " status is set to TIMEOUT & room#" +room.getName()+ " status to FREE");
			}
		}
		
		LOGGER.debug(" End TaskManager.checkReservationTimeOut method : " + new Date());
	}
	
	@Override
	public void purgeStatsDataMethod() {
		
		LOGGER.debug(" Begin TaskManager.purgeStatsDataMethod method : " + new Date());
		
		// - Calculate Date with KEEP_STAT_DATA_IN_DAYS parameter
		ConfigurationDao keepStatDataInDays = configRepository.findByKey(E_ConfigurationKey.KEEP_STAT_DATA_IN_DAYS.toString());
		int keepStatDataInDaysValue = Integer.valueOf(keepStatDataInDays.getValue()); // in days	
					
		Date lastAcceptedStatDate = dateTools.lastAcceptedStatDate(String.valueOf(keepStatDataInDaysValue));
		
		// Delete all lines before lastAcceptedStatDate in room_stats & room_daily_occupancy
		roomDailyRepository.deleteByDay(lastAcceptedStatDate);
		roomStatsRepository.deleteByBeginOccupancyDate(lastAcceptedStatDate);
		
		LOGGER.info("TaskManager.purgeStatsDataMethod is executed");
		
		LOGGER.debug(" End TaskManager.purgeStatsDataMethod method : " + new Date());
	}
	
	@Override
	public void purgeMeetingRoomStatsDataMethod() {
		
		LOGGER.debug(" Begin TaskManager.purgeMeetingRoomStatsDataMethod method : " + new Date());
		
		// - Calculate Date with KEEP_STAT_DATA_IN_DAYS parameter
		ConfigurationDao keepStatDataInDays = configRepository.findByKey(E_ConfigurationKey.KEEP_STAT_DATA_IN_DAYS.toString());
		int keepStatDataInDaysValue = Integer.valueOf(keepStatDataInDays.getValue()); // in days	
					
		Date lastAcceptedStatDate = dateTools.lastAcceptedStatDate(String.valueOf(keepStatDataInDaysValue));
		
		// Delete all lines before lastAcceptedStatDate in meetingroom_stats & meetingroom_daily_occupancy
		meetingRoomDailyRepository.deleteByDay(lastAcceptedStatDate);
		meetingRoomStatsRepository.deleteByBeginOccupancyDate(lastAcceptedStatDate);
		
		LOGGER.info("TaskManager.purgeMeetingRoomStatsDataMethod is executed");
		
		LOGGER.debug(" End TaskManager.purgeMeetingRoomStatsDataMethod method : " + new Date());
	}
	
	
	@Override
	public void checkTeachinTimeOut() {
		
		LOGGER.debug(" Begin TaskManager.checkTeachinTimeOut method : " + new Date());
		
		try {
		TeachinSensorDao teachin = teachinRepository.findByTeachinStatus();

		if ( (teachin.getTeachinStatus().equals(E_TeachinStatus.INITIALIZING.toString())) || (teachin.getTeachinStatus().equals(E_TeachinStatus.RUNNING.toString())) )  {
			LOGGER.debug(" There is teachin in state : " + teachin.getTeachinStatus());
			// - Calculate Date with TEACHIN_TIMEOUT parameter
			ConfigurationDao teachinTimeOut = configRepository.findByKey(E_ConfigurationKey.TEACHIN_TIMEOUT.toString());
			int teachinTimeoutValue = Integer.valueOf(teachinTimeOut.getValue()); // in minutes	
			LOGGER.debug(" TeachinTimeoutValue is : " + teachinTimeoutValue);
			
			Date teachinMaxDate = dateTools.teachinDateDelayBeforeTimeOut(teachin.getTeachinDate(), teachinTimeoutValue);
			LOGGER.debug(" teachinMaxDate is : " + teachinMaxDate);
			
			if (teachinMaxDate.before(new Date())) {
				LOGGER.info(" teachinMaxDate is befor actuelle date : " + new Date());
				// set ENDED
				teachin.setTeachinStatus(E_TeachinStatus.ENDED.toString());
				teachinRepository.updateTeachinStatus(teachin);
				LOGGER.info("TaskManager.checkTeachinTimeOut teachin status is set to ENDED ");
			} 
		}
		
		} catch(IncorrectResultSizeDataAccessException e ) {
			// Table teachin_sensors is empty
	    }
		
		LOGGER.debug(" End TaskManager.checkTeachinTimeOut method : " + new Date());
		
	}

	@Override
	public void processDailyStats() {
		
		LOGGER.debug(" Begin TaskManager.processDailyStats method : " + new Date());
		
		List<RoomDailyOccupancyDao> roomDailyList = new ArrayList<RoomDailyOccupancyDao>();
			
		// 1 - Get Date with DATE_BEGIN_DAY & DATE_END_DAY parameters
		ConfigurationDao beginDay = configRepository.findByKey(E_ConfigurationKey.DATE_BEGIN_DAY.toString());
		String  beginDayValue = beginDay.getValue(); // in hh:mm
		ConfigurationDao endDay = configRepository.findByKey(E_ConfigurationKey.DATE_END_DAY.toString());
		String  endDayValue = endDay.getValue(); // in hh:mm
		
		// 2 - Process the Dates
		Date beginDayDate = dateTools.dateBeginDay(beginDayValue);
		Date endDayDate = dateTools.dateEndDay(endDayValue);
		
		if (dateTools.isWorkingDay(beginDayDate)) { // process only dates in working days
			
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
			
			LOGGER.info("TaskManager.processDailyStats is executed & saveRoomDaily in table. The day is a working one !!!");
			
		}
		
		LOGGER.debug(" end TaskManager.processDailyStats method : " + new Date());
		
	}
	
	@Override
	public void processMeetingRoomDailyStats() {
		LOGGER.debug(" Begin TaskManager.processDailyStats method : " + new Date());
		
		List<MeetingRoomDailyOccupancyDao> meetingRoomDailyList = new ArrayList<MeetingRoomDailyOccupancyDao>();
			
		// 1 - Get Date with DATE_BEGIN_DAY & DATE_END_DAY parameters
		ConfigurationDao beginDay = configRepository.findByKey(E_ConfigurationKey.DATE_BEGIN_DAY.toString());
		String  beginDayValue = beginDay.getValue(); // in hh:mm
		ConfigurationDao endDay = configRepository.findByKey(E_ConfigurationKey.DATE_END_DAY.toString());
		String  endDayValue = endDay.getValue(); // in hh:mm
		
		// 2 - Process the Dates
		Date beginDayDate = dateTools.dateBeginDay(beginDayValue);
		Date endDayDate = dateTools.dateEndDay(endDayValue);
		
		if (dateTools.isWorkingDay(beginDayDate)) { // process only dates in working days
			
			// 3 - find used MeetingRoomStats in the current day
			MeetingRoomStatDao meetingRoomStat = new MeetingRoomStatDao();
			meetingRoomStat.setBeginOccupancyDate(beginDayDate);
			meetingRoomStat.setEndOccupancyDate(endDayDate);
			meetingRoomStat.setMeetingRoomInfo(E_MeetingRoomInfo.UNOCCUPIED.toString());
			List<MeetingRoomStatDao> meetingRoomSt = meetingRoomStatsRepository.findAllOccupiedDailyMeetingRoomStats(meetingRoomStat);
			
			// 4 - cumulate the stats by meetingroomId
			for (MeetingRoomStatDao rstat : meetingRoomSt) { // the meetingroomStats are order by meetingroomId 1,2,3,....
					Integer index = getMeetingRoomInList(rstat.getMeetingroomId(), meetingRoomDailyList);
					if (index != -1) {
						// calculate occupancyDuration
						Long duration = dateTools.calculateDuration(rstat.getBeginOccupancyDate(), rstat.getEndOccupancyDate());
						MeetingRoomDailyOccupancyDao meetingRoomGet = meetingRoomDailyList.get(index);
						// ------ update Occupancy Duration for existing meeting Room (cumulate) ------
						meetingRoomGet.setOccupancyDuration(meetingRoomGet.getOccupancyDuration() + duration);
						//
					} else {
						// add entry
						MeetingRoomDailyOccupancyDao meetingRoomEntry = new MeetingRoomDailyOccupancyDao();
						// calculate occupancyDuration
						Long duration = dateTools.calculateDuration(rstat.getBeginOccupancyDate(), rstat.getEndOccupancyDate());
						meetingRoomEntry.setMeetingroomId(rstat.getMeetingroomId());
						meetingRoomEntry.setOccupancyDuration(duration);
						// ------ Add new meeting room in the list ----
						meetingRoomDailyList.add(meetingRoomEntry);
					}
			}
			
			// 5 - save in Table meetingroom_daily_occupancy
			for (MeetingRoomDailyOccupancyDao meetingRoomDailyOccupancyDao : meetingRoomDailyList) {
				meetingRoomDailyRepository.saveMeetingRoomDaily(meetingRoomDailyOccupancyDao);	
			}
			
			LOGGER.info("TaskManager.processMeetingRoomDailyStats is executed & saveMeetingRoomDaily in table. The day is a working one !!!");
			
		}
		
		LOGGER.debug(" end TaskManager.processMeetingRoomDailyStats method : " + new Date());
		
	}
	
	@Override
	public void checkAgentDashboardTimeOut() {
		ConfigurationDao intervalAgentTimeout = configRepository.findByKey(E_ConfigurationKey.AGENT_STATUS_TIMEOUT.toString());
		List<AgentDao> listAgents = agentRepository.findAgentsInTimeout(intervalAgentTimeout.getValue()); 
		
		for(AgentDao agent : listAgents){
			if(E_AgentStatus.ONLINE.toString().equals(agent.getStatus())){
				agent.setStatus(E_AgentStatus.OFFLINE.toString());
				
				// update Agent Alert
				alertManager.updateAgentAlert(agent.getId(), agent.getStatus());
				
				agentRepository.updateAgentStatusForTimeout(agent);
			}
		}
		
		ConfigurationDao intervalDashboardTimeout = configRepository.findByKey(E_ConfigurationKey.DASHBOARD_STATUS_TIMEOUT.toString());
		List<DashboardDao> listDashboards = dashboardRepository.findDashboardsInTimeout(intervalDashboardTimeout.getValue()); 
		
		for(DashboardDao dashboard : listDashboards){
			if(E_DashboardStatus.ONLINE.toString().equals(dashboard.getStatus())){
				dashboard.setStatus(E_DashboardStatus.OFFLINE.toString());
				
				// update Dashboard Alert
				alertManager.updateDashboardAlert(dashboard.getId(), dashboard.getStatus());
				
				dashboardRepository.updateDashboardStatusForTimeout(dashboard);
			}
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
	
	/**
	 * isRoomInList
	 * @param meetingRoomId
	 * @param meetingRoomDailyList
	 * @return
	 */
	private Integer getMeetingRoomInList(Integer meetingRoomId, List<MeetingRoomDailyOccupancyDao> meetingRoomDailyList) {
		boolean state = false;
		Integer index = -1;
		if (!meetingRoomDailyList.isEmpty()) {
			for (MeetingRoomDailyOccupancyDao meetingRoomDaily : meetingRoomDailyList) {
				index = index + 1;
				if (meetingRoomId == meetingRoomDaily.getMeetingroomId()) {
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
