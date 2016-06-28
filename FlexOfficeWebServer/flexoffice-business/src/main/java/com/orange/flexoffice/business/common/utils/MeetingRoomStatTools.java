package com.orange.flexoffice.business.common.utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.business.common.enums.EnumViewType;
import com.orange.flexoffice.dao.common.model.enumeration.E_MeetingRoomType;
import com.orange.flexoffice.dao.common.model.object.MeetingRoomSimpleStatDto;
import com.orange.flexoffice.dao.common.model.object.MultiStatDto;

/**
 * MeetingRoomStatTools
 * @author oab
 *
 */
public class MeetingRoomStatTools {
	
	@Autowired
	private DateTools dateTools;

	/**
	 * getCategories
	 * @return
	 */
	public List<String> getCategories() {
		
		List<String> list = new ArrayList<String>();
		
		E_MeetingRoomType[] types = E_MeetingRoomType.values();
		for (E_MeetingRoomType eMeetingRoomType : types) {
			list.add(eMeetingRoomType.toString());
		}
		
		return list;
	}
	
	/**
	 * getStatInList
	 * @param meetingRoomId
	 * @param meetingRoomSimpleStatList
	 * @return
	 */
	public Integer getStatInList(Integer meetingRoomId, List<MeetingRoomSimpleStatDto> meetingRoomSimpleStatList) {
		boolean state = false;
		Integer index = -1;
		if (!meetingRoomSimpleStatList.isEmpty()) {
			for (MeetingRoomSimpleStatDto statSimple : meetingRoomSimpleStatList) {
				index = index + 1;
				if (meetingRoomId == statSimple.getMeetingRoomId()) {
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
		float averageOfRates = 0;
		if (viewtype.equals(EnumViewType.DAY.toString())) {
			averageOfRates = (((float)multiStatDto.getOccupancyDuration()*100)/((float)duration*(float)nbRoomsByType.get(multiStatDto.getRoomType().toString())));
		} else {
			averageOfRates = (((float)multiStatDto.getOccupancyDuration()*100)/((float)duration*(float)multiStatDto.getNbDaysDuration()*(float)nbRoomsByType.get(multiStatDto.getRoomType().toString())));
		}
				
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
	public void updateReturnedMultiStatDto(MultiStatDto multiStatDtoReturned, MultiStatDto multiStatDto, Long duration, Map<String, Long> nbRoomsByType, String viewtype) {
		// 1 - calculate average of rate 
		float averageOfRates = 0;
		if (viewtype.equals(EnumViewType.DAY.toString())) {
			averageOfRates = (((float)multiStatDto.getOccupancyDuration()*100)/((float)duration*(float)nbRoomsByType.get(multiStatDto.getRoomType().toString())));
		} else {
			averageOfRates = (((float)multiStatDto.getOccupancyDuration()*100)/((float)duration*(float)multiStatDto.getNbDaysDuration()*(float)nbRoomsByType.get(multiStatDto.getRoomType().toString())));
		}
		
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
		
		Map<String, Long> nbMeetingRoomsByType = new HashMap<String, Long>();
	
		E_MeetingRoomType[] types = E_MeetingRoomType.values();
		for (E_MeetingRoomType eMeetingRoomType : types) {
			nbMeetingRoomsByType.put(eMeetingRoomType.toString(), 5L);
		}
		
		//System.out.println("Box nb salles réservables :" + nbMeetingRoomsByType.get(E_MeetingRoomType.BOX.toString()));
		
	}
	
	
}
