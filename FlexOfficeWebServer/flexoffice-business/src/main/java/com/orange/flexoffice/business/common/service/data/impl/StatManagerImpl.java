package com.orange.flexoffice.business.common.service.data.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.common.enums.EnumViewType;
import com.orange.flexoffice.business.common.service.data.StatManager;
import com.orange.flexoffice.business.common.utils.DateTools;
import com.orange.flexoffice.dao.common.model.data.ConfigurationDao;
import com.orange.flexoffice.dao.common.model.data.RoomDailyOccupancyDao;
import com.orange.flexoffice.dao.common.model.data.RoomDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_ConfigurationKey;
import com.orange.flexoffice.dao.common.model.enumeration.E_RoomType;
import com.orange.flexoffice.dao.common.model.object.MultiStatDto;
import com.orange.flexoffice.dao.common.model.object.MultiStatSetDto;
import com.orange.flexoffice.dao.common.model.object.RoomDailyOccupancyDto;
import com.orange.flexoffice.dao.common.model.object.RoomDailyTypeDto;
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
	
	@Override
	public MultiStatSetDto getOccupancyStats(Long from, Long to, String viewtype) {
		
		MultiStatSetDto multiStatSet = new MultiStatSetDto();
		
		// 1 - Get Room Daily Data requested by From & To parameters
		RoomDailyOccupancyDto parameters = new RoomDailyOccupancyDto(); 
		Date fromDate = new Date(from);
		Date toDate = new Date(to);
		parameters.setFromDate(fromDate);
		parameters.setToDate(toDate);
		List<RoomDailyOccupancyDao> dailyRoomsList = roomDailyRepository.findRequestedRoomsDailyOccupancy(parameters);
		
		if ((dailyRoomsList != null)&&(!dailyRoomsList.isEmpty())) {
			// 2 - Get startdate & enddate
			RoomDailyOccupancyDao firstEntry = dailyRoomsList.get(0);
			Date startdate = firstEntry.getDay();
			multiStatSet.setStartdate(startdate.getTime());  // set startdate
			
			RoomDailyOccupancyDao endEntry = dailyRoomsList.get(dailyRoomsList.size()-1);
			Date enddate = endEntry.getDay();
			multiStatSet.setEnddate(enddate.getTime());  // set enddate
			
			// 3 - Get categories 
			List<String> categories = getCategories();
			multiStatSet.setCategories(categories); // set categories
			
			// 4 - Get data object
			List<MultiStatDto> multiStat = getMultiStat(viewtype, dailyRoomsList);
			multiStatSet.setData(multiStat);
		}

		return multiStatSet;
	}
	
	/**
	 * getCategories
	 * @return
	 */
	private List<String> getCategories() {
		
		List<String> list = new ArrayList<String>();
		
		E_RoomType[] types = E_RoomType.values();
		for (E_RoomType e_RoomType : types) {
			list.add(e_RoomType.toString());
		}
		
		return list;
	}
	
	/**
	 * getMultiStat
	 * @param viewtype
	 * @param dailyRoomsList
	 * @return
	 */
	private List<MultiStatDto> getMultiStat(String viewtype, List<RoomDailyOccupancyDao> dailyRoomsList) {
		// list with MultiStatDto (label, values)
		List<MultiStatDto> multiStatListReturned = new ArrayList<MultiStatDto>();
		
		if (viewtype.equals(EnumViewType.DAY.toString())) {
			
			// 0 - create list with MultiStatDto (roomType, occupancyDuration, day)
			List<MultiStatDto> multiStatList = new ArrayList<MultiStatDto>();
			
			// 1 - Make distinct daily List
			List<Date> distinctDayList = new ArrayList<Date>();
			for (RoomDailyOccupancyDao daily : dailyRoomsList) {
				Date formattedDaily = dateTools.beginOfDay(daily.getDay());
				if (!dateTools.isDateInList(distinctDayList, formattedDaily)) {
					distinctDayList.add(formattedDaily);
				}
			}
			
			// 2 - Get List of RoomDailyTypeDto (roomType, occupancyDuration & Day) from DB
			List<RoomDailyTypeDto> roomslist = roomDailyRepository.findRoomsDailyAndType();
			
			// 3 - Make MultiStatDto List for (day, occupancyDuration & roomType )
			for (Date date : distinctDayList) {
				for (RoomDailyTypeDto roomDailyTypeDto : roomslist) {
					if ((roomDailyTypeDto.getDay().after(dateTools.beginOfDay(date)))&& (roomDailyTypeDto.getDay().before(dateTools.endOfDay(date)))) {  // comptabiliser la ligne
						Integer index = getMultiStatDtoInList(date, roomDailyTypeDto.getType(),  multiStatList);
						if (index != -1) { // update multiStatDto
							MultiStatDto sdto = multiStatList.get(index);
							sdto.setOccupancyDuration(sdto.getOccupancyDuration() + roomDailyTypeDto.getOccupancyDuration());
						} else { // create new multiStatDto
							MultiStatDto multiStatDto = new MultiStatDto();
							multiStatDto.setDay(date);
							multiStatDto.setOccupancyDuration(roomDailyTypeDto.getOccupancyDuration());
							multiStatDto.setRoomType(E_RoomType.valueOf(roomDailyTypeDto.getType()));
							
							multiStatList.add(multiStatDto); // add entry
						}
					}	
				}
			}
			
			// 4 - Construct multiStatListReturned list
			// TODO
			
		} else if (viewtype.equals(EnumViewType.WEEK.toString())) {
			
		} else if (viewtype.equals(EnumViewType.MONTH.toString())) {
			
		}
		
		return multiStatListReturned;
	}
	
	/**
	 * getStatInList
	 * @param roomId
	 * @param simpleStatList
	 * @return
	 */
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
	
	/**
	 * getDayInList
	 * @param day
	 * @param multiStatList
	 * @return
	 */
	private Integer getDayInList(Date day, List<MultiStatDto> multiStatList) {
		boolean state = false;
		Integer index = -1;
		if (!multiStatList.isEmpty()) {
			for (MultiStatDto statMulti : multiStatList) {
				index = index + 1;
				if (day.getTime() == statMulti.getDay().getTime()) {
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
	 * getMultiStatDtoInList
	 * @param dto
	 * @param multiStatList
	 * @return
	 */
	private Integer getMultiStatDtoInList(Date date, String roomType, List<MultiStatDto> multiStatList) {
		boolean state = false;
		Integer index = -1;
		if (!multiStatList.isEmpty()) {
			for (MultiStatDto statMulti : multiStatList) {
				index = index + 1;
				if ((date.getTime() == statMulti.getDay().getTime())&&(roomType.equals(statMulti.getRoomType()))) {
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
