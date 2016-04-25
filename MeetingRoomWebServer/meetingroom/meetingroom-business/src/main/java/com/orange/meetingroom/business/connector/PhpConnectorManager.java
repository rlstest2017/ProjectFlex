package com.orange.meetingroom.business.connector;

import com.orange.meetingroom.connector.php.model.request.GetAgentBookingsParameters;
import com.orange.meetingroom.connector.php.model.request.GetDashboardBookingsParameters;
import com.orange.meetingroom.connector.php.model.request.SetBookingParameters;
import com.orange.meetingroom.connector.php.model.request.UpdateBookingParameters;
import com.orange.meetingroom.connector.php.model.response.BookingSummary;
import com.orange.meetingroom.connector.php.model.response.MeetingRoom;
import com.orange.meetingroom.connector.php.model.response.MeetingRooms;

/**
 * PhpConnectorManager
 * @author oab
 *
 */
public interface PhpConnectorManager {
	
	public MeetingRoom getBookingsFromAgent(GetAgentBookingsParameters params) throws Exception;
	
	public MeetingRooms getBookingsFromDashboard(GetDashboardBookingsParameters params) throws Exception;

	public BookingSummary setBooking(SetBookingParameters params) throws Exception;
	
	public BookingSummary updateBooking(UpdateBookingParameters params) throws Exception;
	
}
