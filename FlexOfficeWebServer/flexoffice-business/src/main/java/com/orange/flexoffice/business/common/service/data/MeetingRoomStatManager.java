package com.orange.flexoffice.business.common.service.data;

import java.util.List;

import com.orange.flexoffice.dao.common.model.object.MeetingRoomSimpleStatDto;
import com.orange.flexoffice.dao.common.model.object.MultiStatSetDto;

/**
 * MeetingRoomStatManager
 * @author oab
 *
 */
public interface MeetingRoomStatManager {

	/**
	 * getPopularStats
	 * @return List<SimpleStatDto>
	 */
	List<MeetingRoomSimpleStatDto> getPopularStats();
	
	/**
	 * getOccupancyStats
	 * @param from
	 * @param to
	 * @param viewtype
	 * @return
	 */
	MultiStatSetDto getOccupancyStats(String from, String to, String viewtype);
	
}