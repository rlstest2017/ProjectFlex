package com.orange.flexoffice.meetingroomapi.ws.endPoint.entity.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.business.common.enums.EnumErrorModel;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.service.data.MeetingRoomManager;
import com.orange.flexoffice.business.common.service.data.TestManager;
import com.orange.flexoffice.dao.common.model.data.MeetingRoomDao;
import com.orange.flexoffice.meetingroomapi.ws.endPoint.entity.MeetingRoomApiEndpoint;
import com.orange.flexoffice.meetingroomapi.ws.model.MeetingRoomInput;
import com.orange.flexoffice.meetingroomapi.ws.utils.ErrorMessageHandler;

/**
 * MeetingRoomApiEndpointImpl
 * @author oab
 *
 */
public class MeetingRoomApiEndpointImpl implements MeetingRoomApiEndpoint {

	private static final Logger LOGGER = Logger.getLogger(MeetingRoomApiEndpointImpl.class);
	
	@Autowired
	private MeetingRoomManager meetingRoomManager;
	@Autowired
	private ErrorMessageHandler errorMessageHandler;
	@Autowired
	private TestManager testManager;

	@Override
	public List<String> getTimeout() {
		LOGGER.debug( "Begin call MeetingRoomApiEndpointImpl.getTimeout at: " + new Date() );
		
		try{
			List<MeetingRoomDao> lstMeetingRooms = meetingRoomManager.getTimeout();
			List<String> lstReturned = new ArrayList<String>();
			
			for(MeetingRoomDao meetingRoomDao :lstMeetingRooms){
				lstReturned.add(meetingRoomDao.getExternalId());
			}
			LOGGER.debug( "End call MeetingRoomApiEndpointImpl.getTimeout  at: " + new Date() );
			
			return lstReturned;
		} catch (RuntimeException e){
			LOGGER.debug("RuntimeException in MeetingRoomApiEndpoint.getTimeout with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}
	}

	@Override
	public Response updateData(String externalId, MeetingRoomInput meetingRoom) {
		LOGGER.debug( "Begin call MeetingRoomApiEndpointImpl.updateData at: " + new Date() );
		
		try {
			MeetingRoomDao meetingRoomDao = new MeetingRoomDao();
			meetingRoomDao.setExternalId(externalId);
			meetingRoomDao.setStatus(meetingRoom.getMeetingRoomStatus().toString());
			meetingRoomDao.setStartDate(new Date(meetingRoom.getStartDate() * 1000));
			meetingRoomDao.setEndDate(new Date(meetingRoom.getEndDate() * 1000));
			meetingRoomDao.setOrganizerLabel(meetingRoom.getOrganizerLabel());
			
			meetingRoomManager.updateData(meetingRoomDao);
			
		} catch (DataNotExistsException e) {
			LOGGER.debug("DataNotExistsException in MeetingRoomApiEndpoint.updateData with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_71, Response.Status.NOT_FOUND));
		} catch (RuntimeException e){
			LOGGER.debug("RuntimeException in MeetingRoomApiEndpoint.updateData with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}
		
		LOGGER.debug( "End call MeetingRoomApiEndpointImpl.updateData  at: " + new Date() );
		
		return Response.ok().build();
	}
	
	@Override
	public boolean executeInitTestFile() {
		return testManager.executeInitTestFile();
	}
}
