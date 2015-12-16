package com.orange.flexoffice.business.common.utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	
	/**
	 * getMultiStatLabelInList
	 * @param label
	 * @param multiStatListReturned
	 * @return
	 */
	public Integer getMultiStatLabelInList(String label, List<MultiStatDto> multiStatListReturned) {
		boolean state = false;
		Integer index = -1;
		if (!multiStatListReturned.isEmpty()) {
			for (MultiStatDto statMulti : multiStatListReturned) {
				index = index + 1;
				if (label.equals(statMulti.getLabel())) {
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
	public MultiStatDto createReturnedMultiStatDto(MultiStatDto multiStatDto, Long duration) {
		
		MultiStatDto multiStatDtoReturned = new MultiStatDto();
		
		// 1 - set label
		multiStatDtoReturned.setLabel(String.valueOf(multiStatDto.getDay().getTime()));
		
		// 2 - init values
		List<String> values = new ArrayList<String>();
		int size = getCategories().size();
		for (int i=0; i < size; i++) {values.add("0");}
		multiStatDtoReturned.setValues(values);
		// 3 - calculate rate
		float rate = ((float)multiStatDto.getOccupancyDuration()*100/(float)duration);
		
		// 4 - update value rate
		int index = getRoomTypeIndex(multiStatDto.getRoomType().toString());
		multiStatDtoReturned.getValues().get(index).replace("0", String.valueOf(rate));
		
		return multiStatDtoReturned;
	}
	
	/**
	 * 
	 * @param multiStatDto
	 * @param duration
	 * @return
	 */
	public void updateReturnedMultiStatDto(MultiStatDto multiStatDtoReturned, MultiStatDto multiStatDto, Long duration) {
		// 1 - calculate rate
		float rate = ((float)multiStatDto.getOccupancyDuration()*100/(float)duration);
		
		// 4 - update value rate
		int index = getRoomTypeIndex(multiStatDto.getRoomType().toString());
		multiStatDtoReturned.getValues().get(index).replace("0", String.valueOf(rate));
	}
	
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
		
		System.out.println("formatted timestamp is:" );
				
	}
	
}
