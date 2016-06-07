package com.orange.meetingroom.gui.ws.endPoint.data.impl;

import java.math.BigInteger;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.meetingroom.business.connector.FlexOfficeConnectorManager;
import com.orange.meetingroom.business.connector.PhpConnectorManager;
import com.orange.meetingroom.business.service.SystemManager;
import com.orange.meetingroom.business.service.enums.EnumErrorModel;
import com.orange.meetingroom.connector.exception.FlexOfficeInternalServerException;
import com.orange.meetingroom.connector.exception.MeetingRoomInternalServerException;
import com.orange.meetingroom.connector.exception.PhpInternalServerException;
import com.orange.meetingroom.connector.flexoffice.model.response.SystemConnectorReturn;
import com.orange.meetingroom.connector.php.model.response.SystemCurrentDateConnectorReturn;
import com.orange.meetingroom.gui.ws.endPoint.data.SystemEndpoint;
import com.orange.meetingroom.gui.ws.model.ObjectFactory;
import com.orange.meetingroom.gui.ws.model.SystemCurrentDate;
import com.orange.meetingroom.gui.ws.model.SystemRemoteMacAddress;
import com.orange.meetingroom.gui.ws.model.SystemReturn;
import com.orange.meetingroom.gui.ws.utils.ErrorMessageHandler;

/**
 * SystemEndpointImpl
 * @author oab
 *
 */
public class SystemEndpointImpl implements SystemEndpoint {

	private static final Logger LOGGER = Logger.getLogger(SystemEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();
	
	@Autowired
	private FlexOfficeConnectorManager flexOfficeConnectorManager;
	@Autowired
	private PhpConnectorManager phpConnectorManager;
	@Autowired
	private SystemManager systemManager;
	@Autowired
	private ErrorMessageHandler errorMessageHandler;

	@Override
	public SystemReturn getSystem() {
		
		try {
			LOGGER.debug( "Begin call getSystem() method for SystemEndpoint at: " + new Date() );
			
			SystemConnectorReturn data = flexOfficeConnectorManager.getSystem();
			
			SystemReturn system = factory.createSystemReturn();
			
			if (data == null) {
				LOGGER.debug("systemConnectorReturn object is null");
			} else {
				system.setAckTime(BigInteger.valueOf(data.getAckTime()));
				system.setAgentTimeout(BigInteger.valueOf(data.getAgentTimeout()));
				system.setCanShowOrganizer(data.getCanShowOrganizer());
				system.setCanShowSubject(data.getCanShowSubject());
				system.setDashboardTimeout(BigInteger.valueOf(data.getDashboardTimeout()));
				system.setDurationStep(BigInteger.valueOf(data.getDurationStep()));
				system.setHourEnd(BigInteger.valueOf(data.getHourEnd()));
				system.setHourStart(BigInteger.valueOf(data.getHourStart()));
				system.setInactivityTime(BigInteger.valueOf(data.getInactivityTime()));
				system.setMaxDuration(BigInteger.valueOf(data.getMaxDuration()));
				system.setMeetingRoomTimeout(BigInteger.valueOf(data.getMeetingRoomTimeout()));
				system.setNbRoomsPerPage(BigInteger.valueOf(data.getNbRoomsPerPage()));
				system.setOrganizerMandatory(data.getOrganizerMandatory());
				system.setPagesShiftInterval(BigInteger.valueOf(data.getPagesShiftInterval()));
				system.setSubjectMandatory(data.getSubjectMandatory());
				system.setUserCanCancel(data.getUserCanCancel());
				system.setWsRefreshInterval(BigInteger.valueOf(data.getWsRefreshInterval()));
				system.setVirtualKeyboard(data.getVirtualKeyboard());
				system.setKeyboardLang(data.getKeyboardLang());
				system.setDashboardStartDate(BigInteger.valueOf(data.getDashboardStartDate()));
				system.setDashboardMaxBookings(BigInteger.valueOf(data.getDashboardMaxBookings()));
				system.setSynchroTime(BigInteger.valueOf(data.getSynchroTime()));
				
			}
					
			LOGGER.debug( "End call getSystem() method for SystemEndpoint at: " + new Date() );
			
			return factory.createSystemReturn(system).getValue();
			
		} catch (FlexOfficeInternalServerException | MeetingRoomInternalServerException  | RuntimeException e) {
			LOGGER.debug("RuntimeException in getSystem() SystemEndpointImpl with message :" + e.getMessage(), e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_1, Response.Status.INTERNAL_SERVER_ERROR));
		}
		
	}

	@Override
	public SystemCurrentDate getSystemCurrentDate() {
		try {
			LOGGER.debug( "Begin call getSystemCurrentDate() method for SystemEndpoint at: " + new Date() );
			
			SystemCurrentDateConnectorReturn data = phpConnectorManager.getCurrentDate();
			
			SystemCurrentDate systemCurrentDate = factory.createSystemCurrentDate();
			
			if (data == null) {
				LOGGER.debug("SystemCurrentDateConnectorReturn object is null");
			} else {
				if (data.getCurrentDate() != null) {
					systemCurrentDate.setCurrentDate(BigInteger.valueOf(data.getCurrentDate()));
				}
			}
			
		LOGGER.debug( "End call getSystemCurrentDate() method for SystemEndpoint at: " + new Date() );
		
		return factory.createSystemCurrentDate(systemCurrentDate).getValue();
		
		} catch (PhpInternalServerException | MeetingRoomInternalServerException  | RuntimeException e) {
			LOGGER.debug("RuntimeException in getSystemCurrentDate() SystemEndpointImpl with message :" + e.getMessage(), e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_1, Response.Status.INTERNAL_SERVER_ERROR));
		}
	}

	@Override
	public SystemRemoteMacAddress getSystemRemoteMacAddress(HttpServletRequest req) {
		try {
		LOGGER.debug( "Begin call getSystemRemoteMacAddress() method for SystemEndpoint at: " + new Date() );
		SystemRemoteMacAddress remoteMacAddress = factory.createSystemRemoteMacAddress();
		
		String ipAddress = req.getRemoteHost();
		String macAddress = systemManager.getRemoteMacAddress(ipAddress);
		remoteMacAddress.setMacAddress(macAddress);
		
		LOGGER.debug( "End call getSystemRemoteMacAddress() method for SystemEndpoint at: " + new Date() );
		
		return factory.createSystemRemoteMacAddress(remoteMacAddress).getValue();
		
		} catch (MeetingRoomInternalServerException e) {
			LOGGER.debug("MeetingRoomInternalServerException in getSystemRemoteMacAddress() SystemEndpointImpl with message :" + e.getMessage(), e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_1, Response.Status.INTERNAL_SERVER_ERROR));
		}
		
	}

}
