package com.orange.meetingroom.business.connector.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.orange.meetingroom.business.connector.FlexOfficeConnectorManager;
import com.orange.meetingroom.connector.exception.DataNotExistsException;
import com.orange.meetingroom.connector.exception.FlexOfficeInternalServerException;
import com.orange.meetingroom.connector.exception.MeetingRoomInternalServerException;
import com.orange.meetingroom.connector.exception.MethodNotAllowedException;
import com.orange.meetingroom.connector.flexoffice.FlexOfficeConnectorClient;
import com.orange.meetingroom.connector.flexoffice.model.request.AgentConnectorInput;
import com.orange.meetingroom.connector.flexoffice.model.request.DashboardConnectorInput;
import com.orange.meetingroom.connector.flexoffice.model.request.MeetingRoomData;
import com.orange.meetingroom.connector.flexoffice.model.response.AgentConnectorOutput;
import com.orange.meetingroom.connector.flexoffice.model.response.DashboardConnectorOutput;
import com.orange.meetingroom.connector.flexoffice.model.response.SystemConnectorReturn;

/**
 * FlexOfficeConnectorManagerImpl
 * @author oab
 *
 */
public class FlexOfficeConnectorManagerImpl implements FlexOfficeConnectorManager {

	@Autowired
	FlexOfficeConnectorClient flexofficeConnector;

	@Override
	public SystemConnectorReturn getSystem() throws FlexOfficeInternalServerException, MeetingRoomInternalServerException {
		return flexofficeConnector.getSystem();
	}

	@Override
	public List<String> getMeetingRoomsInTimeOut() throws FlexOfficeInternalServerException, MeetingRoomInternalServerException {
		return flexofficeConnector.getMeetingRoomsInTimeOut();
	}

	@Override
	public List<String> getDashboardXMLConfigFilesName(DashboardConnectorInput params) throws FlexOfficeInternalServerException, MeetingRoomInternalServerException, DataNotExistsException {
		return flexofficeConnector.getDashboardXMLConfigFilesName(params);
	}

	@Override
	public DashboardConnectorOutput updateDashboardStatus(DashboardConnectorInput params) throws DataNotExistsException, FlexOfficeInternalServerException, MeetingRoomInternalServerException {
		return flexofficeConnector.updateDashboardStatus(params);
	}

	@Override
	public AgentConnectorOutput updateAgentStatus(AgentConnectorInput params) throws MethodNotAllowedException, DataNotExistsException, FlexOfficeInternalServerException, MeetingRoomInternalServerException {
		return flexofficeConnector.updateAgentStatus(params);
	}

	@Override
	public void updateMeetingRoomData(MeetingRoomData params) throws FlexOfficeInternalServerException, MeetingRoomInternalServerException, DataNotExistsException {
		flexofficeConnector.updateMeetingRoomData(params);
	}

}
