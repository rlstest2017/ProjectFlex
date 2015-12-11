package com.orange.flexoffice.business.common.service.data;

import java.util.List;

import com.orange.flexoffice.dao.common.model.object.MultiStatSetDto;
import com.orange.flexoffice.dao.common.model.object.SimpleStatDto;

/**
 * StatManager
 * @author oab
 *
 */
public interface StatManager {

	/**
	 * getPopularStats
	 * @return List<SimpleStatDto>
	 */
	List<SimpleStatDto> getPopularStats();
	
	/**
	 * getOccupancyStats
	 * @param from
	 * @param to
	 * @param viewtype
	 * @return
	 */
	MultiStatSetDto getOccupancyStats(Integer from, Integer to, String viewtype);
	
}