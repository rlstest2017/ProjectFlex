package com.orange.meetingroom.business.connector.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.orange.meetingroom.business.connector.FlexOfficeConnectorManager;
import com.orange.meetingroom.connector.flexoffice.FlexOfficeConnectorClient;
import com.orange.meetingroom.connector.flexoffice.model.request.DashboardInput;
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
	public SystemReturn getSystem() throws Exception {
		return flexofficeConnector.getSystem();
	}

	@Override
	public List<String> getMeetingRoomsInTimeOut() throws Exception {
		return flexofficeConnector.getMeetingRoomsInTimeOut();
	}

	@Override
	public List<String> getDashboardXMLConfigFilesName(DashboardInput params) throws Exception {
		return flexofficeConnector.getDashboardXMLConfigFilesName(params);
	}

}
