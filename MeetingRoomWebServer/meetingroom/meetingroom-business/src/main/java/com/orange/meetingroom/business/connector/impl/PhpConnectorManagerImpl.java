package com.orange.meetingroom.business.connector.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.orange.meetingroom.business.connector.PhpConnectorManager;
import com.orange.meetingroom.connector.exception.DataNotExistsException;
import com.orange.meetingroom.connector.exception.MeetingRoomInternalServerException;
import com.orange.meetingroom.connector.exception.MethodNotAllowedException;
import com.orange.meetingroom.connector.exception.PhpInternalServerException;
import com.orange.meetingroom.connector.php.PhpConnectorClient;
import com.orange.meetingroom.connector.php.model.request.GetAgentBookingsParameters;
import com.orange.meetingroom.connector.php.model.request.GetDashboardBookingsParameters;
import com.orange.meetingroom.connector.php.model.request.SetBookingParameters;
import com.orange.meetingroom.connector.php.model.request.UpdateBookingParameters;
import com.orange.meetingroom.connector.php.model.response.BookingSummary;
import com.orange.meetingroom.connector.php.model.response.MeetingRoomConnectorReturn;
import com.orange.meetingroom.connector.php.model.response.MeetingRoomsConnectorReturn;

/**
 * PhpConnectorManagerImpl
 * @author oab
 *
 */
public class PhpConnectorManagerImpl implements PhpConnectorManager {

	@Autowired
	PhpConnectorClient phpConnector;
	
	@Override
	public MeetingRoomConnectorReturn getBookingsFromAgent(GetAgentBookingsParameters params) throws MeetingRoomInternalServerException, PhpInternalServerException, DataNotExistsException, MethodNotAllowedException  {
		// TODO process the meetingRoomStatus
		// TODO call flexOfficeConnectorManager for send meetingRoomInfos (status, ...)
		// TODO process the result and send it to GUI
		return phpConnector.getBookingsFromAgent(params);
	}

	@Override
	public MeetingRoomsConnectorReturn getBookingsFromDashboard(GetDashboardBookingsParameters params) throws MeetingRoomInternalServerException, PhpInternalServerException, MethodNotAllowedException  {
		// TODO from dashboardMacAddress call flexOfficeConnectorManager for get config Files 
		// TODO call phpConnector.getBookingsFromDashboard(params) foreach config File
		// TODO process the result and send it to GUI
		return phpConnector.getBookingsFromDashboard(params);
	}

	@Override
	public BookingSummary setBooking(SetBookingParameters params) throws MeetingRoomInternalServerException, MethodNotAllowedException, PhpInternalServerException  {
		return phpConnector.setBooking(params);
	}

	@Override
	public BookingSummary updateBooking(UpdateBookingParameters params) throws MeetingRoomInternalServerException, MethodNotAllowedException, PhpInternalServerException  {
		return phpConnector.updateBooking(params);
	}
	

}
