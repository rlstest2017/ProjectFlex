package com.orange.meetingroom.connector.flexoffice.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.orange.meetingroom.connector.flexoffice.model.request.AgentConnectorInput;
import com.orange.meetingroom.connector.flexoffice.model.request.DashboardConnectorInput;
import com.orange.meetingroom.connector.flexoffice.model.request.MeetingRoomData;

/**
 * DataTools
 * @author oab
 *
 */
public class FlexOfficeDataTools {
	
	/**
	 * constructJSONDashboardStatus
	 * @param params DashboardInput
	 * @return String
	 */
	public String constructJSONDashboardStatus(DashboardConnectorInput params) {
		
		final StringBuilder inputJson = new StringBuilder( 1000 );
		inputJson.append( "{\"" );
		inputJson.append( "dashboardStatus\":\""+params.getDashboardStatus().toString());
		inputJson.append( "\"}" );
		return inputJson.toString();
	}
	
	/**
	 * constructJSONAgentStatus
	 * @param params AgentInput
	 * @return String
	 */
	public String constructJSONAgentStatus(AgentConnectorInput params) {
		
		final StringBuilder inputJson = new StringBuilder( 1000 );
		inputJson.append( "{\"" );
		inputJson.append( "agentStatus\":\""+params.getAgentStatus().toString());
		inputJson.append( "\"}" );
		return inputJson.toString();
	}

	/**
	 * constructJSONMeetingRoomData
	 * @param params MeetingRoomData
	 * @return String
	 * @throws UnsupportedEncodingException 
	 */
	public String constructJSONMeetingRoomData(MeetingRoomData params) throws UnsupportedEncodingException {
		
		final StringBuilder inputJson = new StringBuilder( 1000 );
		inputJson.append( "{\"" );
		inputJson.append( "meetingRoomStatus\":\""+params.getMeetingRoomStatus().toString());
		inputJson.append( "\",\"startDate\":"+params.getStartDate());
		inputJson.append( ",\"endDate\":"+params.getEndDate());
		if (params.getOrganizerLabel() != null) {
			inputJson.append( ",\"organizerLabel\":\""+URLEncoder.encode(params.getOrganizerLabel(), "UTF-8"));
		} 
		inputJson.append( "\"}" );
		return inputJson.toString();
	}

}
