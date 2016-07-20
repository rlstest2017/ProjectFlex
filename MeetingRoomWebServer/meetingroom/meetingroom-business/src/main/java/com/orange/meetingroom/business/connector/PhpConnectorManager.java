package com.orange.meetingroom.business.connector;

import com.orange.meetingroom.business.service.exception.DateNotInSlotTimeException;
import com.orange.meetingroom.connector.exception.DataNotExistsException;
import com.orange.meetingroom.connector.exception.FlexOfficeInternalServerException;
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
 * PhpConnectorManager
 * @author oab
 *
 */
public interface PhpConnectorManager {
	
	public SystemCurrentDateConnectorReturn getCurrentDate() throws PhpInternalServerException, MeetingRoomInternalServerException;
	
	public MeetingRoomConnectorReturn getBookingsFromAgent(GetAgentBookingsParameters params) throws MeetingRoomInternalServerException, PhpInternalServerException, DataNotExistsException, MethodNotAllowedException;
	
	public MeetingRoomsConnectorReturn getBookingsFromDashboard(GetDashboardBookingsParameters params) throws MeetingRoomInternalServerException, PhpInternalServerException, MethodNotAllowedException, FlexOfficeInternalServerException, DataNotExistsException;

	public BookingSummary setBooking(SetBookingParameters params) throws MeetingRoomInternalServerException, MethodNotAllowedException, PhpInternalServerException, DateNotInSlotTimeException;
	
	public BookingSummary updateBooking(UpdateBookingParameters params) throws MeetingRoomInternalServerException, MethodNotAllowedException, PhpInternalServerException;
	
}
