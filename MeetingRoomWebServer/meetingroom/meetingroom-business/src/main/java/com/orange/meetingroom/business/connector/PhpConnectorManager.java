package com.orange.meetingroom.business.connector;

import com.orange.meetingroom.connector.php.model.request.GetAgentBookingsParameters;
import com.orange.meetingroom.connector.php.model.response.MeetingRoomBookings;

/**
 * PhpConnectorManager
 * @author oab
 *
 */
public interface PhpConnectorManager {
	
	public MeetingRoomBookings getBookingsFromAgent(GetAgentBookingsParameters params) throws Exception;

}
