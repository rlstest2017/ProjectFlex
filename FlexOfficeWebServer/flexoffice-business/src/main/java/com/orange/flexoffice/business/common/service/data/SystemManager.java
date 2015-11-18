package com.orange.flexoffice.business.common.service.data;

import com.orange.flexoffice.dao.common.model.object.SystemDto;

/**
 * SystemManager
 * @author oab
 *
 */
public interface SystemManager {
		
	/**
	 * Get System data
	 * @return
	 */
	SystemDto getSystem();
}