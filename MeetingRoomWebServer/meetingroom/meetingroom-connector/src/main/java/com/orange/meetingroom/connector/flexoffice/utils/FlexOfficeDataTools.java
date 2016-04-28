package com.orange.meetingroom.connector.flexoffice.utils;

import com.orange.meetingroom.connector.flexoffice.model.request.AgentInput;
import com.orange.meetingroom.connector.flexoffice.model.request.DashboardInput;

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
	 * 
	 * @param params
	 * @return
	 */
	public String constructJSONAgentStatus(AgentInput params) {
		
		final StringBuilder inputJson = new StringBuilder( 1000 );
		inputJson.append( "{\"" );
		inputJson.append( "agentStatus\":\""+params.getAgentStatus().toString());
		inputJson.append( "\"}" );
		return inputJson.toString();
	}

}
