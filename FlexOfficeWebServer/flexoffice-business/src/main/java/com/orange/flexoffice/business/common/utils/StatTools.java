package com.orange.flexoffice.business.common.utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.business.common.enums.EnumViewType;

//import org.apache.log4j.Logger;

import com.orange.flexoffice.dao.common.model.enumeration.E_RoomType;
import com.orange.flexoffice.dao.common.model.object.MultiStatDto;
import com.orange.flexoffice.dao.common.model.object.SimpleStatDto;

/**
 * StatTools
 * @author oab
 *
 */
public class StatTools {
	
	//private static final Logger LOGGER = Logger.getLogger(StatTools.class);
	@Autowired
	private DateTools dateTools;

	/**
	 * getCategories
	 * @return
	 */
	public List<String> getCategories() {
		
		List<String> list = new ArrayList<String>();
		
		E_RoomType[] types = E_RoomType.values();
		for (E_RoomType e_RoomType : types) {
			list.add(e_RoomType.toString());
		}
		
		return list;
	}
	
	/**
	 * getStatInList
	 * @param roomId
	 * @param simpleStatList
	 * @return
	 */
	public Integer getStatInList(Integer roomId, List<SimpleStatDto> simpleStatList) {
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
	public Integer getDayInList(Date day, List<MultiStatDto> multiStatList) {
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
	public Integer getMultiStatDtoInList(Date date, String roomType, List<MultiStatDto> multiStatList) {
		boolean state = false;
		Integer index = -1;
		if (!multiStatList.isEmpty()) {
			for (MultiStatDto statMulti : multiStatList) {
				index = index + 1;
				if ((date.getTime() == statMulti.getDay().getTime())&&(roomType.equals(statMulti.getRoomType().toString()))) {
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
	 * getMultiStatLabelInList
	 * @param label
	 * @param multiStatListReturned
	 * @return
	 */
	public Integer getMultiStatDayInList(Long day, List<MultiStatDto> multiStatListReturned) {
		boolean state = false;
		Integer index = -1;
		if (!multiStatListReturned.isEmpty()) {
			for (MultiStatDto statMulti : multiStatListReturned) {
				index = index + 1;
				if (day == statMulti.getDay().getTime()) {
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
	 * createReturnedMultiStatDto
	 * @param multiStatDto
	 * @return
	 */
	public MultiStatDto createReturnedMultiStatDto(MultiStatDto multiStatDto, Long duration, Map<String, Long> nbRoomsByType, String viewtype) {
		
		MultiStatDto multiStatDtoReturned = new MultiStatDto();
		
		// 0 - set Day used in getMultiStatDayInList(...) method
		multiStatDtoReturned.setDay(multiStatDto.getDay());
		
		// 1 - set label
		if (viewtype.equals(EnumViewType.WEEK.toString())) {
			int num = dateTools.getNumberOfWeek(multiStatDto.getDay());
			multiStatDtoReturned.setLabel(String.valueOf(num));
		} else {
			multiStatDtoReturned.setLabel(String.valueOf(multiStatDto.getDay().getTime()));
		}
		
		// 2 - calculate average of rates
		float averageOfRates = (((float)multiStatDto.getOccupancyDuration()*100)/((float)duration*(float)multiStatDto.getNbDaysDuration()*(float)nbRoomsByType.get(multiStatDto.getRoomType().toString())));
				
		// 4 - add value rate
		int index = getRoomTypeIndex(multiStatDto.getRoomType().toString());
		List<String> values = new ArrayList<String>();
		int size = getCategories().size();
		for (int i=0; i < size; i++) {
			if (i == index) {
				values.add(String.valueOf(averageOfRates));
			} else {
				values.add("0");
			}
		}
		
		multiStatDtoReturned.setValues(values);
		
		return multiStatDtoReturned;
	}
	
	/**
	 * 
	 * @param multiStatDto
	 * @param duration
	 * @return
	 */
	public void updateReturnedMultiStatDto(MultiStatDto multiStatDtoReturned, MultiStatDto multiStatDto, Long duration, Map<String, Long> nbRoomsByType) {
		// 1 - calculate average of rate 
		float averageOfRates = (((float)multiStatDto.getOccupancyDuration()*100)/((float)duration*(float)multiStatDto.getNbDaysDuration()*(float)nbRoomsByType.get(multiStatDto.getRoomType().toString())));
		
		// 4 - update value of average of rate 
		int index = getRoomTypeIndex(multiStatDto.getRoomType().toString());
		List<String> values = new ArrayList<String>();
		int size = getCategories().size();
		for (int i=0; i < size; i++) {
			if (i == index) {
				values.add(String.valueOf(averageOfRates));
			} else {
				values.add(multiStatDtoReturned.getValues().get(i));
			}
		}
		
		multiStatDtoReturned.setValues(values);
	}
	
	/**
	 * getRoomTypeIndex
	 * @param type
	 * @return
	 */
	private Integer getRoomTypeIndex(String type) {
		Integer index = 0;
		Integer rang = 0;
		List<String> catList = getCategories();
		for (String cat : catList) {
			if (cat.equals(type)) {
				index = rang;
				break;
			}
			rang=rang +1;
		}
		
		return index;
	}
	
	/**
	 * main
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		
		Map<String, Long> nbRoomsByType = new HashMap<String, Long>();
		
//		StatTools statTools = new StatTools();
//		
//		MultiStatDto multiStatDto = new MultiStatDto();
//		multiStatDto.setDay(new Date());
//		multiStatDto.setOccupancyDuration(24l);
//		multiStatDto.setRoomType(E_RoomType.VIDEO_CONF);
//		
//		Long duration = 15l;
//		
//		MultiStatDto multiStatDtoReturned = statTools.createReturnedMultiStatDto(multiStatDto, duration, null); 
//				
//		System.out.println("multiStatDtoReturned :" + multiStatDtoReturned.getValues().get(1));
	
		E_RoomType[] types = E_RoomType.values();
		for (E_RoomType e_RoomType : types) {
			nbRoomsByType.put(e_RoomType.toString(), 5l);
		}
		
		System.out.println("Box nb salles :" + nbRoomsByType.get(E_RoomType.BOX.toString()));
		
	}
	
	
}
