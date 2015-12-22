package com.orange.flexoffice.business.common.service.data.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	private Map<String, Long> nbRoomsByType = new HashMap<String, Long>();
		    
	@Override
	@Transactional(readOnly=true)
	public List<SimpleStatDto> getPopularStats() {
		
		LOGGER.debug("Begin method StatManager.getPopularStats");
		List<SimpleStatDto> simpleStatList = new ArrayList<SimpleStatDto>(); 
				
		// 1 - Calculate day duration between beginDayDate & endDayDate in seconds. Ex : [7:30 ; 20:00]=> 45000 seconds 
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
			} else {
				// add entry
				SimpleStatDto statEntry = new SimpleStatDto();
				// set Data
				statEntry.setRoomId(roomDailyOccupancyDao.getRoomId());
				statEntry.setOccupancyDuration(roomDailyOccupancyDao.getOccupancyDuration());
				
				// ------ Add new stat in the list ----
				simpleStatList.add(statEntry);
			}
		}
		
		// 4 - Calculate the rates & Get RoomNames in the simpleStatList
		
		// calculate number of ouvrable days since the first data in room_occupancy_daily to current date (new date())
		int nb = nbJoursOuvrableForPopular();
			
		for (SimpleStatDto simpleStatDto : simpleStatList) {
			// Get roomName
			RoomDao room =  roomRepository.findByRoomId(simpleStatDto.getRoomId().longValue());
			simpleStatDto.setRoomName(room.getName());

			// calculate rate
			float rate = ((float)simpleStatDto.getOccupancyDuration()*100/(float)(nb*duration));
			simpleStatDto.setRate(rate);
		}
		
		LOGGER.debug("End method StatManager.getPopularStats");
		return simpleStatList;
	}
	
	@Override
	public MultiStatSetDto getOccupancyStats(String from, String to, String viewtype) {
		
		MultiStatSetDto multiStatSet = new MultiStatSetDto();
		// 0 - Get nb Rooms by type => used for calculate the average of rates (la moyenne des taux !!!)
		E_RoomType[] types = E_RoomType.values();
		for (E_RoomType e_RoomType : types) {
			long nb = roomRepository.countRoomsByType(e_RoomType.toString());
			nbRoomsByType.put(e_RoomType.toString(), nb);
		}

		// 1 - Get Room Daily Data requested by From & To parameters
		RoomDailyOccupancyDto parameters = new RoomDailyOccupancyDto();
		Date fromDate = null;
		Date toDate = null;
		if (viewtype.equals(EnumViewType.MONTH.toString())) { 
			// transform dates fromDate => begin day of the month & toDate => end day of the month  
			fromDate = dateTools.getFirstDayOfMonth(from, null);
			toDate = dateTools.getLastDayOfMonth(to, null);
		} else if (viewtype.equals(EnumViewType.WEEK.toString())) { 
			// transform dates fromDate => begin day of the week & toDate => end day of the week  
			fromDate = dateTools.getFirstDayOfWeek(from, null);
			toDate = dateTools.getLastDayOfWeek(to, null);
		} else {
			fromDate = dateTools.getDateFromString(from);
			toDate = dateTools.getDateFromString(to);
		}
		parameters.setFromDate(fromDate);
		parameters.setToDate(toDate);
		List<RoomDailyOccupancyDao> dailyRoomsList = dailyRoomsList(parameters);
		
		// 2 - Get categories 
		List<String> categories = statTools.getCategories();
		multiStatSet.setCategories(categories); // set categories

		if ( (dailyRoomsList != null) && (!dailyRoomsList.isEmpty()) ) {
			// 3 - Get startdate & enddate
			if (viewtype.equals(EnumViewType.DAY.toString())) {
				RoomDailyOccupancyDao firstEntry = dailyRoomsList.get(0);
				Date startdate = firstEntry.getDay();
				multiStatSet.setStartdate(startdate.getTime());  // set startdate
				
				RoomDailyOccupancyDao endEntry = dailyRoomsList.get(dailyRoomsList.size()-1);
				Date enddate = endEntry.getDay();
				multiStatSet.setEnddate(enddate.getTime());  // set enddate
			} else {
				multiStatSet.setStartdate(fromDate.getTime());  // set startdate fromDate
				multiStatSet.setEnddate(toDate.getTime());  // set enddate toDate
			}
			// 4 - Get data object
			List<MultiStatDto> multiStat = getMultiStat(viewtype, dailyRoomsList, categories.size(), parameters);
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
	private List<MultiStatDto> getMultiStat(String viewtype, List<RoomDailyOccupancyDao> dailyRoomsList, int sizeCategories, RoomDailyOccupancyDto parameters) {
		// List with MultiStatDto (label, values) to returned
		List<MultiStatDto> multiStatListReturned = new ArrayList<MultiStatDto>();
		
		// Calculate day duration between beginDayDate & endDayDate in seconds
		Long duration = calculateDayDuration();
		LOGGER.debug("duration betwwen beginDayDate and endDayDate :" + duration);
				
		if (viewtype.equals(EnumViewType.DAY.toString())) {
			// Make distinct daily List
			List<Date> distinctDayList = new ArrayList<Date>();
			for (RoomDailyOccupancyDao daily : dailyRoomsList) {
				Date formattedDaily = dateTools.beginOfDay(daily.getDay());
				if (!dateTools.isDateInList(distinctDayList, formattedDaily)) {
					distinctDayList.add(formattedDaily);
				}
			}
			
			// Compute returned List
			constructReturnedList(distinctDayList, multiStatListReturned, duration, viewtype, parameters);
			
					
		} else if (viewtype.equals(EnumViewType.WEEK.toString())) {
			// Make distinct weekly List
			List<Date> distinctWeekthList = new ArrayList<Date>();
			for (RoomDailyOccupancyDao daily : dailyRoomsList) {
				Date formattedDaily = dateTools.beginOfDay(daily.getDay());
				if (!dateTools.isWeekInList(distinctWeekthList, formattedDaily)) {
					distinctWeekthList.add(formattedDaily);
				}
			}
						
			// Compute returned List
			constructReturnedList(distinctWeekthList, multiStatListReturned, duration, viewtype, parameters);
						
		} else if (viewtype.equals(EnumViewType.MONTH.toString())) {
			// Make distinct monthly List
			List<Date> distinctMonthList = new ArrayList<Date>();
			for (RoomDailyOccupancyDao daily : dailyRoomsList) {
				Date formattedDaily = dateTools.beginOfDay(daily.getDay());
				if (!dateTools.isMonthInList(distinctMonthList, formattedDaily)) {
					distinctMonthList.add(formattedDaily);
				}
			}
			
			// Compute returned List
			constructReturnedList(distinctMonthList, multiStatListReturned, duration, viewtype, parameters);
			
		}
		
		return multiStatListReturned;
	}
	
	/**
	 * constructReturnedList
	 * @param distinctDayList
	 * @param multiStatListReturned
	 */
	private void constructReturnedList(List<Date> distinctDayList, List<MultiStatDto> multiStatListReturned, Long duration, String viewtype, RoomDailyOccupancyDto parameters) {
			// 0 - create list with MultiStatDto (roomType, occupancyDuration, day)
			List<MultiStatDto> multiStatList = new ArrayList<MultiStatDto>();
			
			// 1 - Get List of RoomDailyTypeDto (roomType, occupancyDuration & Day) from DB
			List<RoomDailyTypeDto> roomslist = roomDailyRepository.findRoomsDailyAndType(parameters);
			
			// 2 - Make MultiStatDto List for (day, occupancyDuration & roomType )
			if (viewtype.equals(EnumViewType.DAY.toString())) {
				for (Date date : distinctDayList) {
					for (RoomDailyTypeDto roomDailyTypeDto : roomslist) {
						if ((roomDailyTypeDto.getDay().after(dateTools.beginOfDay(date)))&& (roomDailyTypeDto.getDay().before(dateTools.endOfDay(date)))) {  // comptabiliser la ligne
							Integer index = statTools.getMultiStatDtoInList(date, roomDailyTypeDto.getType(),  multiStatList);
							if (index != -1) { // update multiStatDto
								MultiStatDto sdto = multiStatList.get(index);
								sdto.setOccupancyDuration(sdto.getOccupancyDuration() + roomDailyTypeDto.getOccupancyDuration());
								sdto.setNbDaysDuration(sdto.getNbDaysDuration() + 1);
							} else { // create new multiStatDto
								MultiStatDto multiStatDto = new MultiStatDto();
								multiStatDto.setDay(date);
								multiStatDto.setOccupancyDuration(roomDailyTypeDto.getOccupancyDuration());
								multiStatDto.setRoomType(E_RoomType.valueOf(roomDailyTypeDto.getType()));
								multiStatDto.setNbDaysDuration(1l);
								
								multiStatList.add(multiStatDto); // add entry
							}
						}	
					}
				}
			} else if (viewtype.equals(EnumViewType.MONTH.toString())) {
				for (Date date : distinctDayList) {
					Calendar cal = Calendar.getInstance();
				    cal.setTime(date);
				    int yearToCompare = cal.get(Calendar.YEAR);
				    int monthToCompare = cal.get(Calendar.MONTH);
					for (RoomDailyTypeDto roomDailyTypeDto : roomslist) {
						cal.setTime(roomDailyTypeDto.getDay());
					    int year = cal.get(Calendar.YEAR);
					    int month = cal.get(Calendar.MONTH);
					    if ((monthToCompare == month)&&(yearToCompare == year)) {  // comptabiliser la ligne
							Integer index = statTools.getMultiStatDtoInList(date, roomDailyTypeDto.getType(),  multiStatList);
							if (index != -1) { // update multiStatDto
								MultiStatDto sdto = multiStatList.get(index);
								sdto.setOccupancyDuration(sdto.getOccupancyDuration() + roomDailyTypeDto.getOccupancyDuration());
							} else { // create new multiStatDto
								MultiStatDto multiStatDto = new MultiStatDto();
								multiStatDto.setDay(date);
								multiStatDto.setOccupancyDuration(roomDailyTypeDto.getOccupancyDuration());
								multiStatDto.setRoomType(E_RoomType.valueOf(roomDailyTypeDto.getType()));
								Date beginMonth = dateTools.getFirstDayOfMonth(null, date);
								Date endMonth = dateTools.getLastDayOfMonth(null, date);
								int nb = dateTools.nbJoursOuvrable(beginMonth, endMonth, true, true, true, true, true, true, false, false);
								multiStatDto.setNbDaysDuration((long)nb);
								
								multiStatList.add(multiStatDto); // add entry
							}
						}	
					}
				}
			} else if (viewtype.equals(EnumViewType.WEEK.toString())) {
				for (Date date : distinctDayList) {
					Calendar cal = Calendar.getInstance();
				    cal.setTime(date);
				    int yearToCompare = cal.get(Calendar.YEAR);
				    int monthToCompare = cal.get(Calendar.MONTH);
				    int weekToCompare = cal.get(Calendar.WEEK_OF_MONTH);
					for (RoomDailyTypeDto roomDailyTypeDto : roomslist) {
						cal.setTime(roomDailyTypeDto.getDay());
					    int year = cal.get(Calendar.YEAR);
					    int month = cal.get(Calendar.MONTH);
					    int week = cal.get(Calendar.WEEK_OF_MONTH);
					    if ((monthToCompare == month)&&(yearToCompare == year)&&(weekToCompare == week)) {  // comptabiliser la ligne
							Integer index = statTools.getMultiStatDtoInList(date, roomDailyTypeDto.getType(),  multiStatList);
							if (index != -1) { // update multiStatDto
								MultiStatDto sdto = multiStatList.get(index);
								sdto.setOccupancyDuration(sdto.getOccupancyDuration() + roomDailyTypeDto.getOccupancyDuration());
							} else { // create new multiStatDto
								MultiStatDto multiStatDto = new MultiStatDto();
								multiStatDto.setDay(date);
								multiStatDto.setOccupancyDuration(roomDailyTypeDto.getOccupancyDuration());
								multiStatDto.setRoomType(E_RoomType.valueOf(roomDailyTypeDto.getType()));
								Date beginWeek = dateTools.getFirstDayOfWeek(null, date);
								Date endWeek = dateTools.getLastDayOfWeek(null, date);
								int nb = dateTools.nbJoursOuvrable(beginWeek, endWeek, true, true, true, true, true, true, false, false);
								multiStatDto.setNbDaysDuration((long)nb);
								
								multiStatList.add(multiStatDto); // add entry
							}
						}	
					}
				}
			}
			
			// 3 - Construct multiStatListReturned list (label, values)
			for (MultiStatDto multiStatDto : multiStatList) {
				Integer index = statTools.getMultiStatDayInList(multiStatDto.getDay().getTime(), multiStatListReturned);
				if (index != -1) { // update multiStatDto
					statTools.updateReturnedMultiStatDto(multiStatListReturned.get(index), multiStatDto, duration, nbRoomsByType, viewtype);
				} else { // create new multiStatDto
					MultiStatDto multiStatDtoReturned = statTools.createReturnedMultiStatDto(multiStatDto, duration, nbRoomsByType, viewtype);
					multiStatListReturned.add(multiStatDtoReturned);
				}
			}
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
	
	/**
	 * dailyRoomsList
	 * @param from
	 * @param to
	 * @param viewtype
	 * @return
	 */
	private List<RoomDailyOccupancyDao> dailyRoomsList(RoomDailyOccupancyDto parameters ) {
		// 1 - Get Room Daily Data requested by From & To parameters
		List<RoomDailyOccupancyDao> dailyRoomsList = roomDailyRepository.findRequestedRoomsDailyOccupancy(parameters);
		return dailyRoomsList;
	}
	
	/**
	 * nbJoursOuvrableForPopular
	 * @return
	 */
	private int nbJoursOuvrableForPopular() {
		int returnedValue = 1;
		RoomDailyOccupancyDto parameters = new RoomDailyOccupancyDto();
		parameters.setFromDate(dateTools.getDateFromString("0"));
		parameters.setToDate(new Date());
		List<RoomDailyOccupancyDao> dailyRoomsList = dailyRoomsList(parameters);
		if ( (dailyRoomsList != null) && (!dailyRoomsList.isEmpty()) ) {
				// - Get startdate & enddate
				RoomDailyOccupancyDao firstEntry = dailyRoomsList.get(0);
				Date startdate = firstEntry.getDay();
								
				RoomDailyOccupancyDao endEntry = dailyRoomsList.get(dailyRoomsList.size()-1);
				Date enddate = endEntry.getDay();
				
				returnedValue = dateTools.nbJoursOuvrable(startdate, enddate, true, true, true, true, true, true, false, false);
				
		} else {
			returnedValue = 1; // default value (not influence in calculate of rate (division))
		}
		return returnedValue;
	}

}
