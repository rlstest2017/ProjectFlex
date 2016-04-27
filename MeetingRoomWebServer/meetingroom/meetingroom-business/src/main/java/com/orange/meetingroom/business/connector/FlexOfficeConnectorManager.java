package com.orange.meetingroom.business.connector;

import java.util.List;

import com.orange.meetingroom.connector.flexoffice.model.request.DashboardInput;
import com.orange.meetingroom.connector.flexoffice.model.response.SystemReturn;

/**
 * FlexOfficeConnectorManager
 * @author oab
 *
 */
public interface FlexOfficeConnectorManager {
	
	public SystemReturn getSystem() throws Exception;
	
	public List<String>  getMeetingRoomsInTimeOut() throws Exception;
	
	public List<String> getDashboardXMLConfigFilesName(DashboardInput params) throws Exception; 

}
