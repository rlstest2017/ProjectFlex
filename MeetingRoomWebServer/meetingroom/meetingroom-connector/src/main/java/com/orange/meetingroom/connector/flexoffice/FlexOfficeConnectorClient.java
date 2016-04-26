package com.orange.meetingroom.connector.flexoffice;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.meetingroom.connector.flexoffice.model.request.AgentInput;
import com.orange.meetingroom.connector.flexoffice.model.request.DashboardInput;
import com.orange.meetingroom.connector.flexoffice.model.request.MeetingRoomData;
import com.orange.meetingroom.connector.flexoffice.model.response.AgentOutput;
import com.orange.meetingroom.connector.flexoffice.model.response.DashboardOutput;
import com.orange.meetingroom.connector.flexoffice.model.response.SystemReturn;

/**
 * FlexOfficeConnectorClient
 * @author oab
 *
 */
public class FlexOfficeConnectorClient {

	private static final Logger LOGGER = Logger.getLogger(FlexOfficeConnectorClient.class);

	//Create a new instance of http client which will connect to REST api over network
	@Autowired
	private CloseableHttpClient httpClient;
	@Value("${flexoffice.meetingroomapi.server}")
	private String flexofficeMeetingRoomAPIServerURL;

	//**************************************************************************
    //************************* METHODS  ***************************************
    //**************************************************************************
	/**
	 * getSystem
	 * @return System
	 */
	public SystemReturn getSystem() throws Exception {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "Begin call getSystem() method");
		}
		SystemReturn systemReturn = new SystemReturn();
		try	{
		//HttpGet getRequest = new HttpGet("http://192.168.103.193:8080/flexoffice-meetingroomapi/v2");
		String request = flexofficeMeetingRoomAPIServerURL + "/system";
		HttpGet getRequest = new HttpGet(request);
		
		//Set the API media type in http accept header
		getRequest.addHeader("accept", "application/json");
		
		//Send the request; It will immediately return the response in HttpResponse object
		LOGGER.info("The getRequest in getBookingsFromAgent(...) method is : " + getRequest);
		HttpResponse response = httpClient.execute(getRequest);
		
		//verify the valid error code first
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != 200) {
			throw new RuntimeException("Failed with HTTP error code : " + statusCode);
		}
		
		//Now pull back the response object
		HttpEntity httpEntity = response.getEntity();
		String apiOutput = EntityUtils.toString(httpEntity);
		
		//Lets see what we got from API
		//System.out.println(apiOutput); 
					
		// TODO parse the JSON response
		ObjectMapper mapper = new ObjectMapper();
		//JSON from URL to Object
		systemReturn = mapper.readValue(apiOutput, SystemReturn.class);
		
		}
		finally	{
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug( "End call getSystem() method");
			}
		}
		
		return systemReturn;
	}
	
	/**
	 * getMeetingRoomsInTimeOut
	 * @return List<String>
	 */
	public List<String>  getMeetingRoomsInTimeOut() {
		// TODO
		return null;
	}
	
	/**
	 * getDashboardXMLConfigFilesName
	 * @param params DashboardInput
	 * @return List<String>
	 */
	public List<String> getDashboardXMLConfigFilesName(DashboardInput params) {
		// TODO
		return null;
	}
	
	/**
	 * updateDashboardStatus
	 * @param params
	 * @return DashboardOutput
	 */
	public DashboardOutput updateDashboardStatus(DashboardInput params) {
		// TODO
		return null;
	}
	
	/**
	 * updateAgentStatus
	 * @param params AgentInput
	 * @return AgentOutput
	 */
	public AgentOutput updateAgentStatus(AgentInput params) {
		// TODO
		return null;
	}
	
	/**
	 * putMeetingRoomData
	 * @param params MeetingRoomData
	 */
	public void putMeetingRoomData(MeetingRoomData params) {
		
	}
}
