package com.orange.meetingroom.connector.flexoffice;

import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.meetingroom.connector.flexoffice.enums.EnumCommand;
import com.orange.meetingroom.connector.flexoffice.model.request.AgentInput;
import com.orange.meetingroom.connector.flexoffice.model.request.DashboardInput;
import com.orange.meetingroom.connector.flexoffice.model.request.MeetingRoomData;
import com.orange.meetingroom.connector.flexoffice.model.response.AgentOutput;
import com.orange.meetingroom.connector.flexoffice.model.response.DashboardOutput;
import com.orange.meetingroom.connector.flexoffice.model.response.SystemReturn;
import com.orange.meetingroom.connector.flexoffice.utils.FlexOfficeDataTools;
import com.orange.meetingroom.connector.flexoffice.ws.PathConst;

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
	@Autowired
	private FlexOfficeDataTools dataTools;
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
		//HttpGet getRequest = new HttpGet("http://192.168.103.193:8080/flexoffice-meetingroomapi/v2/system");
		String request = flexofficeMeetingRoomAPIServerURL + PathConst.SYSTEM_PATH;
		HttpGet getRequest = new HttpGet(request);
		
		//Set the API media type in http accept header
		getRequest.addHeader("accept", "application/json");
		
		//Send the request; It will immediately return the response in HttpResponse object
		LOGGER.info("The getRequest in getSystem(...) method is : " + getRequest);
		HttpResponse response = httpClient.execute(getRequest);
		
		//verify the valid error code first
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != 200) {
			throw new RuntimeException("Failed with HTTP error code : " + statusCode);
			// TODO gérer les erreurs 404,405, 500, ...
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
	public List<String>  getMeetingRoomsInTimeOut() throws Exception {
	
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "Begin call getMeetingRoomsInTimeOut() method");
		}
		List<String> meetingRoomsExternalIdsList;
		
		try	{
		//HttpGet getRequest = new HttpGet("http://192.168.103.193:8080/flexoffice-meetingroomapi/v2/meetingrooms/timeout");
		// TODO decoment String request = flexofficeMeetingRoomAPIServerURL + PathConst.MEETINGROOMS_PATH + PathConst.TIMEOUT_PATH;
		String request = flexofficeMeetingRoomAPIServerURL + PathConst.TIMEOUT_PATH; // TODO delete only for mock !!!
		HttpGet getRequest = new HttpGet(request);
		
		//Set the API media type in http accept header
		getRequest.addHeader("accept", "application/json");
		
		//Send the request; It will immediately return the response in HttpResponse object
		LOGGER.info("The getRequest in getMeetingRoomsInTimeOut(...) method is : " + getRequest);
		HttpResponse response = httpClient.execute(getRequest);
		
		//verify the valid error code first
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != 200) {
			throw new RuntimeException("Failed with HTTP error code : " + statusCode);
			// TODO gérer les erreurs 404,405, 500, ...
		}
		
		//Now pull back the response object
		HttpEntity httpEntity = response.getEntity();
		String apiOutput = EntityUtils.toString(httpEntity);
		
		//Lets see what we got from API
		//System.out.println(apiOutput); 
					
		// parse the JSON response
		ObjectMapper mapper = new ObjectMapper();
		meetingRoomsExternalIdsList = mapper.readValue(apiOutput,new TypeReference<List<String>>() {});
		
		
		}
		finally	{
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug( "End call getMeetingRoomsInTimeOut() method");
			}
		}
	
		return meetingRoomsExternalIdsList;
	}
	
	/**
	 * getDashboardXMLConfigFilesName
	 * @param params DashboardInput
	 * @return List<String>
	 */
	public List<String> getDashboardXMLConfigFilesName(DashboardInput params) throws Exception {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "Begin call getDashboardXMLConfigFilesName(DashboardInput params) method");
		}
		List<String> xmlFilesNameList;
		
		try	{
		//HttpGet getRequest = new HttpGet("http://192.168.103.193:8080/flexoffice-meetingroomapi/v2/dashboards/{dashboardMacAddress}/config");
		// TODO decoment String request = flexofficeMeetingRoomAPIServerURL + PathConst.DASHBOARDS_PATH + "/" + params.getDashboardMacAddress() + PathConst.CONFIG_PATH;
		String request = flexofficeMeetingRoomAPIServerURL + PathConst.CONFIG_PATH; // TODO delete only for mock !!!
		HttpGet getRequest = new HttpGet(request);
		
		//Set the API media type in http accept header
		getRequest.addHeader("accept", "application/json");
		
		//Send the request; It will immediately return the response in HttpResponse object
		LOGGER.info("The getRequest in getMeetingRoomsInTimeOut(...) method is : " + getRequest);
		HttpResponse response = httpClient.execute(getRequest);
		
		//verify the valid error code first
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != 200) {
			throw new RuntimeException("Failed with HTTP error code : " + statusCode);
			// TODO gérer les erreurs 404,405, 500, ...
		}
		
		//Now pull back the response object
		HttpEntity httpEntity = response.getEntity();
		String apiOutput = EntityUtils.toString(httpEntity);
		
		//Lets see what we got from API
		//System.out.println(apiOutput); 
					
		// parse the JSON response
		ObjectMapper mapper = new ObjectMapper();
		xmlFilesNameList = mapper.readValue(apiOutput,new TypeReference<List<String>>() {});
		
		
		}
		finally	{
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug( "End call getDashboardXMLConfigFilesName(DashboardInput params) method");
			}
		}
	
		return xmlFilesNameList;
	}
	
	/**
	 * updateDashboardStatus
	 * @param params
	 * @return DashboardOutput
	 */
	public DashboardOutput updateDashboardStatus(DashboardInput params) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "Begin call updateDashboardStatus(DashboardInput params) method");
		}
		DashboardOutput dashboardOutput = new DashboardOutput();

		// construct the writer from DashboardInput
		String writer = dataTools.constructJSONDashboardStatus(params);
		try	{
			// Define a postRequest request
			HttpPut putRequest = new HttpPut(flexofficeMeetingRoomAPIServerURL);
			
			//Set the API media type in http content-type header
			putRequest.addHeader("content-type", "application/json");
			
			//Set the request post body
			StringEntity input = new StringEntity(writer);
			putRequest.setEntity(input);
			 
			//Send the request; It will immediately return the response in HttpResponse object if any
			LOGGER.info("The postRequest in updateDashboardStatus(...) method is : " + putRequest);
			HttpResponse response = httpClient.execute(putRequest);
			
			//verify the valid error code first
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				throw new RuntimeException("Failed with HTTP error code : " + statusCode);
			}
			
			//Now pull back the response object
			HttpEntity httpEntity = response.getEntity();
			String apiOutput = EntityUtils.toString(httpEntity);
						
			// parse the JSON response
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> mp = mapper.readValue(apiOutput,new TypeReference<Map<String, Object>>() {});
			String command = (String)mp.get("command");
			dashboardOutput.setCommand(EnumCommand.valueOf(command));
			
			return dashboardOutput;
		}
		finally	{
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug( "End call updateDashboardStatus(DashboardInput params) method");
			}
		}
	}
	
	/**
	 * updateAgentStatus
	 * @param params AgentInput
	 * @return AgentOutput
	 */
	public AgentOutput updateAgentStatus(AgentInput params) throws Exception {
		// TODO
		return null;
	}
	
	/**
	 * putMeetingRoomData
	 * @param params MeetingRoomData
	 */
	public void putMeetingRoomData(MeetingRoomData params) throws Exception {
		
	}
}
