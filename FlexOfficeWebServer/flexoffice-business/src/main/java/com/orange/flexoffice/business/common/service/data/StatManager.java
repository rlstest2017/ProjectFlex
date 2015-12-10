package com.orange.flexoffice.business.common.service.data;

import java.util.List;

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
	
}