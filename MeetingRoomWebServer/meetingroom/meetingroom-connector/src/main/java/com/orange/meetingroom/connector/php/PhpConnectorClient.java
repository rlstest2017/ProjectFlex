package com.orange.meetingroom.connector.php;

import com.orange.meetingroom.connector.exception.DataNotExistsException;
import com.orange.meetingroom.connector.exception.MeetingRoomInternalServerException;
import com.orange.meetingroom.connector.exception.MethodNotAllowedException;
import com.orange.meetingroom.connector.exception.PhpInternalServerException;
import com.orange.meetingroom.connector.php.model.request.GetAgentBookingsParameters;
import com.orange.meetingroom.connector.php.model.request.GetDashboardBookingsParameters;
import com.orange.meetingroom.connector.php.model.request.SetBookingParameters;
import com.orange.meetingroom.connector.php.model.request.UpdateBookingParameters;
import com.orange.meetingroom.connector.php.model.response.BookingSummary;
import com.orange.meetingroom.connector.php.model.response.MeetingRoomConnectorReturn;
import com.orange.meetingroom.connector.php.model.response.MeetingRoomsConnectorReturn;
import com.orange.meetingroom.connector.php.model.response.SystemCurrentDateConnectorReturn;

/**
 * PhpConnectorClient
 * @author oab
 *
 */
public interface PhpConnectorClient {
	
	public SystemCurrentDateConnectorReturn getCurrentDate() throws PhpInternalServerException, MeetingRoomInternalServerException;

	public MeetingRoomConnectorReturn getBookingsFromAgent(GetAgentBookingsParameters params) throws MeetingRoomInternalServerException, PhpInternalServerException, DataNotExistsException, MethodNotAllowedException;
	
	public MeetingRoomsConnectorReturn getBookingsFromDashboard(GetDashboardBookingsParameters params) throws MeetingRoomInternalServerException, PhpInternalServerException, MethodNotAllowedException;
	
	public BookingSummary setBooking(SetBookingParameters params) throws MeetingRoomInternalServerException, MethodNotAllowedException, PhpInternalServerException;
	
	public BookingSummary updateBooking(UpdateBookingParameters params) throws MeetingRoomInternalServerException, MethodNotAllowedException, PhpInternalServerException;
	
	
}
