package com.orange.meetingroom.business.service.impl;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.orange.meetingroom.business.service.SystemManager;
import com.orange.meetingroom.connector.exception.MeetingRoomInternalServerException;

/**
 * SystemManagerImpl
 * @author oab
 *
 */
@Service("SystemManager")
public class SystemManagerImpl implements SystemManager {

	private static final Logger LOGGER = Logger.getLogger(SystemManagerImpl.class);

	@Override
	public String getRemoteMacAddress(String ipAddress) throws MeetingRoomInternalServerException {
		try {
			LOGGER.debug("ipAddress is: " + ipAddress);
			String cmd = "arp -a " + ipAddress;
			LOGGER.debug("cmd command is: " + cmd);
			
		    Scanner s = new Scanner(Runtime.getRuntime().exec(cmd).getInputStream());
		    String str = null;
		    Pattern pattern = Pattern.compile("(([0-9A-Fa-f]{2}[-:]){5}[0-9A-Fa-f]{2})|(([0-9A-Fa-f]{4}\\.){2}[0-9A-Fa-f]{4})");
		    try {
		        while (s.hasNext()) {
		            str = s.next();
		            Matcher matcher = pattern.matcher(str);
		            if (matcher.matches()) {
		                break;
		            } else {
		                str = null;
		            }
		        }
		    } finally {
		        s.close();
		    }
		    return (str != null) ? str.toLowerCase(): null;  
		} catch (IOException e) {
			LOGGER.error("Error in EntityUtils.toString() method, with message: " + e.getMessage(), e);
			throw new MeetingRoomInternalServerException("Error in EntityUtils.toString() method, with message: " + e.getMessage());
		}
	}

}
