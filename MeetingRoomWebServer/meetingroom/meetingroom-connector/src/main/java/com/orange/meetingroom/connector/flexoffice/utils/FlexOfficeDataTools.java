package com.orange.meetingroom.connector.flexoffice.utils;

import com.orange.meetingroom.connector.flexoffice.model.request.AgentInput;
import com.orange.meetingroom.connector.flexoffice.model.request.DashboardInput;
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
	public String constructJSONDashboardStatus(DashboardInput params) {
		
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
	public String constructJSONAgentStatus(AgentInput params) {
		
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
	 */
	public String constructJSONMeetingRoomData(MeetingRoomData params) {
		
		final StringBuilder inputJson = new StringBuilder( 1000 );
		inputJson.append( "{\"" );
		inputJson.append( "meetingRoomStatus\":\""+params.getMeetingRoomStatus().toString());
		inputJson.append( "\",\"startDate\":"+params.getStartDate());
		inputJson.append( ",\"endDate\":"+params.getEndDate());
		inputJson.append( ",\"organizerLabel\":\""+params.getOrganizerLabel());
		inputJson.append( "\"}" );
		return inputJson.toString();
	}

}
