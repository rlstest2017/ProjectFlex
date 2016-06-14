package com.orange.meetingroom.business.connector.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orange.meetingroom.business.connector.PhpConnectorManager;
import com.orange.meetingroom.business.connector.utils.DateTools;
import com.orange.meetingroom.business.connector.utils.MeetingRoomInfoTools;
import com.orange.meetingroom.connector.exception.DataNotExistsException;
import com.orange.meetingroom.connector.exception.FlexOfficeInternalServerException;
import com.orange.meetingroom.connector.exception.MeetingRoomInternalServerException;
import com.orange.meetingroom.connector.exception.MethodNotAllowedException;
import com.orange.meetingroom.connector.exception.PhpInternalServerException;
import com.orange.meetingroom.connector.flexoffice.FlexOfficeConnectorClient;
import com.orange.meetingroom.connector.flexoffice.model.request.DashboardConnectorInput;
import com.orange.meetingroom.connector.flexoffice.model.request.MeetingRoomData;
import com.orange.meetingroom.connector.php.PhpConnectorClient;
import com.orange.meetingroom.connector.php.model.request.GetAgentBookingsParameters;
import com.orange.meetingroom.connector.php.model.request.GetDashboardBookingsParameters;
import com.orange.meetingroom.connector.php.model.request.SetBookingParameters;
import com.orange.meetingroom.connector.php.model.request.UpdateBookingParameters;
import com.orange.meetingroom.connector.php.model.response.BookingSummary;
import com.orange.meetingroom.connector.php.model.response.MeetingRoomBookingsConnectorReturn;
import com.orange.meetingroom.connector.php.model.response.MeetingRoomConnectorReturn;
import com.orange.meetingroom.connector.php.model.response.MeetingRoomsConnectorReturn;
import com.orange.meetingroom.connector.php.model.response.SystemCurrentDateConnectorReturn;

/**
 * PhpConnectorManagerImpl
 * @author oab
 *
 */
@Service("PhpConnectorManager")
public class PhpConnectorManagerImpl implements PhpConnectorManager {

	private static final Logger LOGGER = Logger.getLogger(PhpConnectorManagerImpl.class);
	@Autowired
	PhpConnectorClient phpConnector;
	@Autowired
	FlexOfficeConnectorClient flexOfficeConnector;
	@Autowired
	MeetingRoomInfoTools meetingRoomInfoTools;
	@Autowired
	DateTools dateTools;
	
	@Override
	public SystemCurrentDateConnectorReturn getCurrentDate() throws PhpInternalServerException, MeetingRoomInternalServerException {
		return phpConnector.getCurrentDate();
	}
	
	@Override
	public MeetingRoomConnectorReturn getBookingsFromAgent(GetAgentBookingsParameters params) throws MeetingRoomInternalServerException, PhpInternalServerException, DataNotExistsException, MethodNotAllowedException  {
		MeetingRoomConnectorReturn metingroomreturn = phpConnector.getBookingsFromAgent(params);
		// process the meetingRoomStatus
		MeetingRoomData data = meetingRoomInfoTools.processMeetingRoomStatus(metingroomreturn);
		if (data != null) {
			// process the result => add MeetingRoomStatus in meetingRoomDetails and send it to GUI
			metingroomreturn.getMeetingRoom().getMeetingRoomDetails().setMeetingRoomStatus(data.getMeetingRoomStatus());
			try {
				// call flexOfficeConnectorManager for send meetingRoomInfos (status, ...)
				flexOfficeConnector.updateMeetingRoomData(data);
			} catch (FlexOfficeInternalServerException e) {
				LOGGER.debug("FlexOfficeInternalServerException in updateMeetingRoomData()." + e.getMessage(), e);
			}
		} else {
			LOGGER.debug("MeetingRoomData get from processMeetingRoomStatus() is null !!!");
		}
		
		return metingroomreturn;
	}

	@Override
	public MeetingRoomsConnectorReturn getBookingsFromDashboard(GetDashboardBookingsParameters params) throws MeetingRoomInternalServerException, PhpInternalServerException, MethodNotAllowedException, FlexOfficeInternalServerException, DataNotExistsException {
		// from dashboardMacAddress call flexOfficeConnectorManager for get config Files 
		DashboardConnectorInput input = new DashboardConnectorInput();
		input.setDashboardMacAddress(params.getDashboardMacAddress());
		List<String> xmlFilesGroupRoomIdNames = flexOfficeConnector.getDashboardXMLConfigFilesName(input);
		
		MeetingRoomsConnectorReturn rooms = new MeetingRoomsConnectorReturn();
		
		// call phpConnector.getBookingsFromDashboard(params) foreach config File
		// process the result and send it to GUI
		for (String filename : xmlFilesGroupRoomIdNames) {
			params.setRoomGroupID(filename);
			MeetingRoomsConnectorReturn roomsList = phpConnector.getBookingsFromDashboard(params);
			rooms.setCurrentDate(roomsList.getCurrentDate());
			List<MeetingRoomBookingsConnectorReturn> meetingRooms = roomsList.getMeetingRooms();
			for (MeetingRoomBookingsConnectorReturn meetingRoom : meetingRooms) {
				rooms.getMeetingRooms().add(meetingRoom);	
			}
		}
		
		return rooms;
	}

	@Override
	public BookingSummary setBooking(SetBookingParameters params) throws MeetingRoomInternalServerException, MethodNotAllowedException, PhpInternalServerException  {
		return phpConnector.setBooking(params);
	}

	@Override
	public BookingSummary updateBooking(UpdateBookingParameters params) throws MeetingRoomInternalServerException, MethodNotAllowedException, PhpInternalServerException  {
		
		if (params.getStartDate() != null) {
			SystemCurrentDateConnectorReturn connectorreturn = phpConnector.getCurrentDate();
			Integer currentDate = connectorreturn.getCurrentDate();
			LOGGER.debug("currentDate is:" + currentDate);
			Integer returnDate = dateTools.processDate(currentDate, 10);// 10 seconds
			LOGGER.debug("returnDate is:" + returnDate);
			params.setStartDate(returnDate.toString());
		}
		
		if (params.getEndDate() != null) {
			SystemCurrentDateConnectorReturn connectorreturn = phpConnector.getCurrentDate();
			Integer currentDate = connectorreturn.getCurrentDate();
			LOGGER.debug("currentDate is:" + currentDate);
			Integer returnDate = dateTools.processDate(currentDate, 10);// 10 seconds
			LOGGER.debug("returnDate is:" + returnDate);
			params.setEndDate(returnDate.toString());
		}
		
		return phpConnector.updateBooking(params);
	}


}
