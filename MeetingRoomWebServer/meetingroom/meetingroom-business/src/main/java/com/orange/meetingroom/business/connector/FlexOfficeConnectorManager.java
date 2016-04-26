package com.orange.meetingroom.business.connector;

import com.orange.meetingroom.connector.flexoffice.model.response.SystemReturn;

/**
 * FlexOfficeConnectorManager
 * @author oab
 *
 */
public interface FlexOfficeConnectorManager {
	
	public SystemReturn getSystem() throws Exception;

}
