package com.orange.meetingroom.business.connector.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.orange.meetingroom.business.connector.PhpConnectorManager;
import com.orange.meetingroom.connector.php.PhpConnectorClient;
import com.orange.meetingroom.connector.php.model.request.GetAgentBookingsParameters;
import com.orange.meetingroom.connector.php.model.response.MeetingRoomBookings;

/**
 * PhpConnectorManagerImpl
 * @author oab
 *
 */
public class PhpConnectorManagerImpl implements PhpConnectorManager {

	@Autowired
	PhpConnectorClient phpConnector;
	
	@Override
	public MeetingRoomBookings getBookingsFromAgent(GetAgentBookingsParameters params) throws Exception {
		return phpConnector.getBookingsFromAgent(params);
	}
	

}
