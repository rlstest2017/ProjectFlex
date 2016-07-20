package com.orange.meetingroom.business.service;

import com.orange.meetingroom.connector.exception.MeetingRoomInternalServerException;

/**
 * SystemManager
 * @author oab
 *
 */
public interface SystemManager {
	
	String getRemoteMacAddress(String ipAddress) throws MeetingRoomInternalServerException;
	
}
