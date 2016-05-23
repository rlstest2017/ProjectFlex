package com.orange.meetingroom.gui.ws.endPoint.data.impl;

import java.math.BigInteger;
import java.util.Date;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.meetingroom.business.connector.FlexOfficeConnectorManager;
import com.orange.meetingroom.business.service.enums.EnumErrorModel;
import com.orange.meetingroom.connector.exception.FlexOfficeInternalServerException;
import com.orange.meetingroom.connector.exception.MeetingRoomInternalServerException;
import com.orange.meetingroom.connector.flexoffice.model.response.SystemConnectorReturn;
import com.orange.meetingroom.gui.ws.endPoint.data.SystemEndpoint;
import com.orange.meetingroom.gui.ws.model.ObjectFactory;
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
				system.setHourStart(BigInteger.valueOf(data.getHourEnd()));
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
			}
					
			LOGGER.debug( "End call getSystem() method for SystemEndpoint at: " + new Date() );
			
			return factory.createSystemReturn(system).getValue();
			
		} catch (FlexOfficeInternalServerException | MeetingRoomInternalServerException  | RuntimeException e) {
			LOGGER.debug("RuntimeException in getSystem() SystemEndpointImpl with message :" + e.getMessage(), e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_1, Response.Status.INTERNAL_SERVER_ERROR));
		}
		
	}

	@Override
	public Response options() {
	    return Response.ok("")
	            .header("Access-Control-Allow-Origin", "*")
	            .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization, x-auth-token")
	            .header("Access-Control-Allow-Credentials", "true")
	            .header("Access-Control-Allow-Methods", "OPTIONS, HEAD")
	            .header("Access-Control-Max-Age", "1209600")
	            .build();
	}

}
