package com.orange.meetingroom.business.connector.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.orange.meetingroom.business.connector.FlexOfficeConnectorManager;
import com.orange.meetingroom.connector.flexoffice.FlexOfficeConnectorClient;
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

}
