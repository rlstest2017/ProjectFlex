package com.orange.meetingroom.business.connector;

import java.util.List;

import com.orange.meetingroom.connector.flexoffice.model.request.AgentInput;
import com.orange.meetingroom.connector.flexoffice.model.request.DashboardInput;
import com.orange.meetingroom.connector.flexoffice.model.response.AgentOutput;
import com.orange.meetingroom.connector.flexoffice.model.response.DashboardOutput;
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
	
	public DashboardOutput updateDashboardStatus(DashboardInput params) throws Exception;
	
	public AgentOutput updateAgentStatus(AgentInput params) throws Exception;
	
}
