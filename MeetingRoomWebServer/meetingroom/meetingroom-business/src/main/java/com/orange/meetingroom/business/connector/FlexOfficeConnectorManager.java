package com.orange.meetingroom.business.connector;

import java.util.List;

import com.orange.meetingroom.connector.exception.DataNotExistsException;
import com.orange.meetingroom.connector.exception.FlexOfficeInternalServerException;
import com.orange.meetingroom.connector.exception.MeetingRoomInternalServerException;
import com.orange.meetingroom.connector.exception.MethodNotAllowedException;
import com.orange.meetingroom.connector.flexoffice.model.request.AgentConnectorInput;
import com.orange.meetingroom.connector.flexoffice.model.request.DashboardConnectorInput;
import com.orange.meetingroom.connector.flexoffice.model.request.MeetingRoomData;
import com.orange.meetingroom.connector.flexoffice.model.response.AgentConnectorOutput;
import com.orange.meetingroom.connector.flexoffice.model.response.DashboardConnectorOutput;
import com.orange.meetingroom.connector.flexoffice.model.response.SystemConnectorReturn;

/**
 * FlexOfficeConnectorManager
 * @author oab
 *
 */
public interface FlexOfficeConnectorManager {
	
	public SystemConnectorReturn getSystem() throws FlexOfficeInternalServerException, MeetingRoomInternalServerException;
	
	public List<String>  getMeetingRoomsInTimeOut() throws FlexOfficeInternalServerException, MeetingRoomInternalServerException;
	
	public List<String> getDashboardXMLConfigFilesName(DashboardConnectorInput params) throws FlexOfficeInternalServerException, MeetingRoomInternalServerException, DataNotExistsException; 
	
	public DashboardConnectorOutput updateDashboardStatus(DashboardConnectorInput params) throws DataNotExistsException, FlexOfficeInternalServerException, MeetingRoomInternalServerException;
	
	public AgentConnectorOutput updateAgentStatus(AgentConnectorInput params) throws MethodNotAllowedException, DataNotExistsException, FlexOfficeInternalServerException, MeetingRoomInternalServerException;
	
	public void updateMeetingRoomData(MeetingRoomData params) throws FlexOfficeInternalServerException, MeetingRoomInternalServerException, DataNotExistsException;
	
}
