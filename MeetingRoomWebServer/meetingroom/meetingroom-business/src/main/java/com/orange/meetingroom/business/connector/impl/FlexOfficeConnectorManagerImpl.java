package com.orange.meetingroom.business.connector.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.orange.meetingroom.business.connector.FlexOfficeConnectorManager;
import com.orange.meetingroom.connector.exception.DataNotExistsException;
import com.orange.meetingroom.connector.exception.FlexOfficeInternalServerException;
import com.orange.meetingroom.connector.exception.MeetingRoomInternalServerException;
import com.orange.meetingroom.connector.exception.MethodNotAllowedException;
import com.orange.meetingroom.connector.flexoffice.FlexOfficeConnectorClient;
import com.orange.meetingroom.connector.flexoffice.model.request.AgentInput;
import com.orange.meetingroom.connector.flexoffice.model.request.DashboardInput;
import com.orange.meetingroom.connector.flexoffice.model.request.MeetingRoomData;
import com.orange.meetingroom.connector.flexoffice.model.response.AgentOutput;
import com.orange.meetingroom.connector.flexoffice.model.response.DashboardOutput;
import com.orange.meetingroom.connector.flexoffice.model.response.SystemReturn;

/**
 * FlexOfficeConnectorManagerImpl
 * @author oab
 *
 */
public class FlexOfficeConnectorManagerImpl implements FlexOfficeConnectorManager {

	@Autowired
	FlexOfficeConnectorClient flexofficeConnector;

	@Override
	public SystemReturn getSystem() throws FlexOfficeInternalServerException, MeetingRoomInternalServerException {
		return flexofficeConnector.getSystem();
	}

	@Override
	public List<String> getMeetingRoomsInTimeOut() throws FlexOfficeInternalServerException, MeetingRoomInternalServerException {
		return flexofficeConnector.getMeetingRoomsInTimeOut();
	}

	@Override
	public List<String> getDashboardXMLConfigFilesName(DashboardInput params) throws FlexOfficeInternalServerException, MeetingRoomInternalServerException, DataNotExistsException {
		return flexofficeConnector.getDashboardXMLConfigFilesName(params);
	}

	@Override
	public DashboardOutput updateDashboardStatus(DashboardInput params) throws DataNotExistsException, FlexOfficeInternalServerException, MeetingRoomInternalServerException {
		return flexofficeConnector.updateDashboardStatus(params);
	}

	@Override
	public AgentOutput updateAgentStatus(AgentInput params) throws MethodNotAllowedException, DataNotExistsException, FlexOfficeInternalServerException, MeetingRoomInternalServerException {
		return flexofficeConnector.updateAgentStatus(params);
	}

	@Override
	public void updateMeetingRoomData(MeetingRoomData params) throws FlexOfficeInternalServerException, MeetingRoomInternalServerException, DataNotExistsException {
		flexofficeConnector.updateMeetingRoomData(params);
	}

}
