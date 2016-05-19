package com.orange.flexoffice.business.common.service.data;

import java.util.List;

import com.orange.flexoffice.business.common.exception.DataNotExistsException;

public interface MeetingRoomGroupsConfigurationManager {

	
	/**
	 * Finds a list of {@link String}
	 * @param macAddress the {@link macAddress} 
	 * @return a {@link List<String>}
	 */
	List<String> getConfigurationFiles(String macAddress) throws DataNotExistsException;

}