package com.orange.meetingroom.business.connector;

import java.util.List;

import com.orange.meetingroom.connector.exception.DataNotExistsException;
import com.orange.meetingroom.connector.exception.FlexOfficeInternalServerException;
import com.orange.meetingroom.connector.exception.MeetingRoomInternalServerException;
import com.orange.meetingroom.connector.exception.MethodNotAllowedException;
import com.orange.meetingroom.connector.flexoffice.model.request.AgentInput;
import com.orange.meetingroom.connector.flexoffice.model.request.DashboardInput;
import com.orange.meetingroom.connector.flexoffice.model.request.MeetingRoomData;
import com.orange.meetingroom.connector.flexoffice.model.response.AgentOutput;
import com.orange.meetingroom.connector.flexoffice.model.response.DashboardOutput;
import com.orange.meetingroom.connector.flexoffice.model.response.SystemReturn;

/**
 * FlexOfficeConnectorManager
 * @author oab
 *
 */
public interface FlexOfficeConnectorManager {
	
	public SystemReturn getSystem() throws FlexOfficeInternalServerException, MeetingRoomInternalServerException;
	
	public List<String>  getMeetingRoomsInTimeOut() throws FlexOfficeInternalServerException, MeetingRoomInternalServerException;
	
	public List<String> getDashboardXMLConfigFilesName(DashboardInput params) throws FlexOfficeInternalServerException, MeetingRoomInternalServerException, DataNotExistsException; 
	
	public DashboardOutput updateDashboardStatus(DashboardInput params) throws DataNotExistsException, FlexOfficeInternalServerException, MeetingRoomInternalServerException;
	
	public AgentOutput updateAgentStatus(AgentInput params) throws MethodNotAllowedException, DataNotExistsException, FlexOfficeInternalServerException, MeetingRoomInternalServerException;
	
	public void updateMeetingRoomData(MeetingRoomData params) throws FlexOfficeInternalServerException, MeetingRoomInternalServerException, DataNotExistsException;
	
}
