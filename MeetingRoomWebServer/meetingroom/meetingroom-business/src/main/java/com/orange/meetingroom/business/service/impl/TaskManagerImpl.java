package com.orange.meetingroom.business.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orange.meetingroom.business.connector.utils.MeetingRoomInfoTools;
import com.orange.meetingroom.business.service.TaskManager;
import com.orange.meetingroom.connector.exception.DataNotExistsException;
import com.orange.meetingroom.connector.exception.FlexOfficeInternalServerException;
import com.orange.meetingroom.connector.exception.MeetingRoomInternalServerException;
import com.orange.meetingroom.connector.exception.MethodNotAllowedException;
import com.orange.meetingroom.connector.exception.PhpInternalServerException;
import com.orange.meetingroom.connector.flexoffice.FlexOfficeConnectorClient;
import com.orange.meetingroom.connector.flexoffice.model.request.MeetingRoomData;
import com.orange.meetingroom.connector.php.PhpConnectorClient;
import com.orange.meetingroom.connector.php.model.request.GetAgentBookingsParameters;
import com.orange.meetingroom.connector.php.model.response.MeetingRoomConnectorReturn;

/**
 * TaskManagerImpl
 * @author oab
 *
 */
@Service("TaskManager")
public class TaskManagerImpl implements TaskManager {

	private static final Logger LOGGER = Logger.getLogger(TaskManagerImpl.class);
	static final String FORMAT_JSON = "json";
	static final String FORCED_UPDATE_CACHE_TRUE = "true";

	@Autowired
	FlexOfficeConnectorClient flexofficeConnector;
	@Autowired
	PhpConnectorClient phpConnector;
	@Autowired
	MeetingRoomInfoTools meetingRoomInfoTools;

	@Override
	public void checkMeetingRoomTimeOut() {
		try {
			List<String> meetingRoomsExternalsIdList = flexofficeConnector.getMeetingRoomsInTimeOut();
			GetAgentBookingsParameters params = new GetAgentBookingsParameters();
			params.setFormat(FORMAT_JSON);
			params.setForceUpdateCache(FORCED_UPDATE_CACHE_TRUE);
			
			for (String externalId : meetingRoomsExternalsIdList) {
				try {
					params.setRoomID(externalId);
					MeetingRoomConnectorReturn metingroomreturn = phpConnector.getBookingsFromAgent(params);
					
					// process the meetingRoomStatus
					MeetingRoomData data = meetingRoomInfoTools.processMeetingRoomStatus(metingroomreturn);
					if (data != null) {
						// process the result => add MeetingRoomStatus in meetingRoomDetails and send it to GUI
						metingroomreturn.getMeetingRoom().getMeetingRoomDetails().setMeetingRoomStatus(data.getMeetingRoomStatus());
						try {
							// call flexOfficeConnectorManager for send meetingRoomInfos (status, ...)
								flexofficeConnector.updateMeetingRoomData(data);
						} catch (DataNotExistsException e) {
								LOGGER.debug("DataNotExistsException in updateMeetingRoomData() " + e.getMessage(), e);
						} catch (FlexOfficeInternalServerException e) {
							LOGGER.debug("FlexOfficeInternalServerException in updateMeetingRoomData() " + e.getMessage(), e);
						}
					} else {
						LOGGER.debug("MeetingRoomData get from processMeetingRoomStatus() is null for externalId: " + externalId);
					}
				} catch (PhpInternalServerException e) {
					LOGGER.debug("PhpInternalServerException from getBookingsFromAgent() " + e.getMessage(), e);
				} catch (DataNotExistsException e) {
					LOGGER.debug("DataNotExistsException from getBookingsFromAgent() " + e.getMessage(), e);
				} catch (MethodNotAllowedException e) {
					LOGGER.debug("MethodNotAllowedException from getBookingsFromAgent() " + e.getMessage(), e);
				}
			} // end for
			
		} catch ( FlexOfficeInternalServerException | MeetingRoomInternalServerException | RuntimeException e) {
			LOGGER.debug("Un exception was thrown from getMeetingRoomsInTimeOut() method: " + e.getMessage());
		}
	}

}
