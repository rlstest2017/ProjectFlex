package com.orange.meetingroom.business.connector.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.orange.meetingroom.business.connector.PhpConnectorManager;
import com.orange.meetingroom.connector.php.PhpConnectorClient;
import com.orange.meetingroom.connector.php.model.request.GetAgentBookingsParameters;
import com.orange.meetingroom.connector.php.model.request.GetDashboardBookingsParameters;
import com.orange.meetingroom.connector.php.model.request.SetBookingParameters;
import com.orange.meetingroom.connector.php.model.request.UpdateBookingParameters;
import com.orange.meetingroom.connector.php.model.response.BookingSummary;
import com.orange.meetingroom.connector.php.model.response.MeetingRoom;
import com.orange.meetingroom.connector.php.model.response.MeetingRooms;

/**
 * PhpConnectorManagerImpl
 * @author oab
 *
 */
public class PhpConnectorManagerImpl implements PhpConnectorManager {

	@Autowired
	PhpConnectorClient phpConnector;
	
	@Override
	public MeetingRoom getBookingsFromAgent(GetAgentBookingsParameters params) throws Exception {
		return phpConnector.getBookingsFromAgent(params);
	}

	@Override
	public MeetingRooms getBookingsFromDashboard(GetDashboardBookingsParameters params) throws Exception {
		return phpConnector.getBookingsFromDashboard(params);
	}

	@Override
	public BookingSummary setBooking(SetBookingParameters params) throws Exception {
		return phpConnector.setBooking(params);
	}

	@Override
	public BookingSummary updateBooking(UpdateBookingParameters params) throws Exception {
		return phpConnector.updateBooking(params);
	}
	

}
