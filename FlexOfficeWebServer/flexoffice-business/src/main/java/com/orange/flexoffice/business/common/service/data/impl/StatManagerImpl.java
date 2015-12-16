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
import com.orange.flexoffice.business.common.utils.StatTools;
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
	@Autowired
	private StatTools statTools;
	
	@Override
	@Transactional(readOnly=true)
	public List<SimpleStatDto> getPopularStats() {
		
		LOGGER.debug("Begin method StatManager.getPopularStats");
		List<SimpleStatDto> simpleStatList = new ArrayList<SimpleStatDto>(); 
				
		// 1 - Calculate day duration between beginDayDate & endDayDate in seconds
		Long duration = calculateDayDuration();
		LOGGER.debug("duration betwwen beginDayDate and endDayDate :" + duration);
		
		// 2 - Find All RoomDailOccupancy DATA order by roomId
		List<RoomDailyOccupancyDao> roomDailyList = roomDailyRepository.findAllRoomsDailyOccupancy();
		
		LOGGER.debug("roomDailyList size :" + roomDailyList.size());
		
		// 3 - cumulate & calculate the rates by roomId
		for (RoomDailyOccupancyDao roomDailyOccupancyDao : roomDailyList) { // the statsDaily are order by roomId 1,2,3,....
			Integer index = statTools.getStatInList(roomDailyOccupancyDao.getRoomId(), simpleStatList);
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
		
		// 4 - Calculate the rates & Get RoomNames in the simpleStatList
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
	public MultiStatSetDto getOccupancyStats(String from, String to, String viewtype) {
		
		MultiStatSetDto multiStatSet = new MultiStatSetDto();
		
		// 1 - Get Room Daily Data requested by From & To parameters
		RoomDailyOccupancyDto parameters = new RoomDailyOccupancyDto();
		Date fromDate = dateTools.getDateFromString(from);;
		Date toDate = dateTools.getDateFromString(to);
		parameters.setFromDate(fromDate);
		parameters.setToDate(toDate);
		List<RoomDailyOccupancyDao> dailyRoomsList = roomDailyRepository.findRequestedRoomsDailyOccupancy(parameters);

		// 2 - Get categories 
		List<String> categories = statTools.getCategories();
		multiStatSet.setCategories(categories); // set categories

		if ((dailyRoomsList != null)&&(!dailyRoomsList.isEmpty())) {
			// 3 - Get startdate & enddate
			RoomDailyOccupancyDao firstEntry = dailyRoomsList.get(0);
			Date startdate = firstEntry.getDay();
			multiStatSet.setStartdate(startdate.getTime());  // set startdate
			
			RoomDailyOccupancyDao endEntry = dailyRoomsList.get(dailyRoomsList.size()-1);
			Date enddate = endEntry.getDay();
			multiStatSet.setEnddate(enddate.getTime());  // set enddate
			
			// 4 - Get data object
			List<MultiStatDto> multiStat = getMultiStat(viewtype, dailyRoomsList, categories.size());
			multiStatSet.setData(multiStat);
		} else {
			multiStatSet.setStartdate(fromDate.getTime());  // set startdate fromDate
			multiStatSet.setEnddate(toDate.getTime());  // set enddate toDate
		}

		return multiStatSet;
	}
	
	
	
	/**
	 * getMultiStat
	 * @param viewtype
	 * @param dailyRoomsList
	 * @return
	 */
	private List<MultiStatDto> getMultiStat(String viewtype, List<RoomDailyOccupancyDao> dailyRoomsList, int sizeCategories) {
		// List with MultiStatDto (label, values) to returned
		List<MultiStatDto> multiStatListReturned = new ArrayList<MultiStatDto>();
		
		// Calculate day duration between beginDayDate & endDayDate in seconds
		Long duration = calculateDayDuration();
		LOGGER.debug("duration betwwen beginDayDate and endDayDate :" + duration);
				
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
						Integer index = statTools.getMultiStatDtoInList(date, roomDailyTypeDto.getType(),  multiStatList);
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
			
			// 4 - Construct multiStatListReturned list (label, values)
			for (MultiStatDto multiStatDto : multiStatList) {
				Integer index = statTools.getMultiStatLabelInList(String.valueOf(multiStatDto.getDay().getTime()), multiStatListReturned);
				if (index != -1) { // update multiStatDto
					statTools.updateReturnedMultiStatDto(multiStatListReturned.get(index), multiStatDto, duration);
				} else { // create new multiStatDto
					MultiStatDto multiStatDtoReturned = statTools.createReturnedMultiStatDto(multiStatDto, duration);
					multiStatListReturned.add(multiStatDtoReturned);
				}
			}
			
		} else if (viewtype.equals(EnumViewType.WEEK.toString())) {
			
		} else if (viewtype.equals(EnumViewType.MONTH.toString())) {
			
		}
		
		return multiStatListReturned;
	}
	
		
	/**
	 * calculateDayDuration
	 * @return
	 */
	private Long calculateDayDuration() {
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

		return duration;
	}
	

}
