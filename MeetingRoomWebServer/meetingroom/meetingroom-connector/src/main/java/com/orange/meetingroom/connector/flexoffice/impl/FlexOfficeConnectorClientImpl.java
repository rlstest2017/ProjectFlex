package com.orange.meetingroom.connector.flexoffice.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.meetingroom.connector.exception.DataNotExistsException;
import com.orange.meetingroom.connector.exception.FlexOfficeInternalServerException;
import com.orange.meetingroom.connector.exception.MeetingRoomInternalServerException;
import com.orange.meetingroom.connector.exception.MethodNotAllowedException;
import com.orange.meetingroom.connector.flexoffice.FlexOfficeConnectorClient;
import com.orange.meetingroom.connector.flexoffice.enums.EnumCommand;
import com.orange.meetingroom.connector.flexoffice.model.request.AgentConnectorInput;
import com.orange.meetingroom.connector.flexoffice.model.request.DashboardConnectorInput;
import com.orange.meetingroom.connector.flexoffice.model.request.MeetingRoomData;
import com.orange.meetingroom.connector.flexoffice.model.response.AgentConnectorOutput;
import com.orange.meetingroom.connector.flexoffice.model.response.DashboardConnectorOutput;
import com.orange.meetingroom.connector.flexoffice.model.response.SystemConnectorReturn;
import com.orange.meetingroom.connector.flexoffice.utils.FlexOfficeDataTools;
import com.orange.meetingroom.connector.flexoffice.ws.PathConst;

/**
 * FlexOfficeConnectorClientImpl
 * @author oab
 *
 */
@Service("FlexOfficeConnectorClient")
public class FlexOfficeConnectorClientImpl implements FlexOfficeConnectorClient {

	private static final Logger LOGGER = Logger.getLogger(FlexOfficeConnectorClientImpl.class);

	//Create a new instance of http client which will connect to REST api over network
	@Autowired
	private CloseableHttpClient httpClient;
	@Autowired
	private FlexOfficeDataTools flexofficeDataTools;
	@Value("${flexoffice.meetingroomapi.server}")
	private String flexofficeMeetingRoomAPIServerURL;

	//**************************************************************************
    //************************* METHODS  ***************************************
    //**************************************************************************
	/**
	 * getSystem
	 * @return SystemReturn
	 * @throws FlexOfficeInternalServerException
	 * @throws MeetingRoomInternalServerException
	 */
	public SystemConnectorReturn getSystem() throws FlexOfficeInternalServerException, MeetingRoomInternalServerException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "Begin call getSystem() method");
		}
		SystemConnectorReturn systemReturn = new SystemConnectorReturn();
		HttpGet getRequest = null;
		try	{
		//HttpGet getRequest = new HttpGet("http://192.168.103.193:8080/flexoffice-meetingroomapi/v2/system");
		String request = flexofficeMeetingRoomAPIServerURL + PathConst.SYSTEM_PATH;
		getRequest = new HttpGet(request);
		//Set the API media type in http accept header
		getRequest.addHeader("accept", "application/json");
		//Send the request; It will immediately return the response in HttpResponse object
		LOGGER.info("The getRequest in getSystem(...) method is : " + getRequest);
		HttpResponse response = httpClient.execute(getRequest);
		//verify the valid error code first
		int statusCode = response.getStatusLine().getStatusCode();
		if ((statusCode != 200) && (statusCode != 201) && (statusCode != 202)) {
			LOGGER.error("Internal error produce in FlexOffice, with error code: " + statusCode);
			throw new FlexOfficeInternalServerException("Internal error produce in FlexOffice, with error code: " + statusCode);
		}
		//Now pull back the response object
		HttpEntity httpEntity = response.getEntity();
		String apiOutput = EntityUtils.toString(httpEntity);
		// parse the JSON response
		ObjectMapper mapper = new ObjectMapper();
		//JSON from URL to Object
		systemReturn = mapper.readValue(apiOutput, SystemConnectorReturn.class);
		} catch (ClientProtocolException ex) {
			LOGGER.error("Error in httpClient.execute() method, with message: " + ex.getMessage(), ex);
			throw new MeetingRoomInternalServerException("Error in httpClient.execute() method, with message: " + ex.getMessage());
		} catch (IOException e) {
			LOGGER.error("Error in EntityUtils.toString() method, with message: " + e.getMessage(), e);
			throw new MeetingRoomInternalServerException("Error in EntityUtils.toString() method, with message: " + e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug( "End call getSystem() method");
			}
			if (getRequest != null) {
				getRequest.releaseConnection();
			}
		}
		
		return systemReturn;
	}
	
	/**
	 * getMeetingRoomsInTimeOut
	 * @return List<String> 
	 * @throws FlexOfficeInternalServerException
	 * @throws MeetingRoomInternalServerException
	 */
	public List<String>  getMeetingRoomsInTimeOut() throws FlexOfficeInternalServerException, MeetingRoomInternalServerException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "Begin call getMeetingRoomsInTimeOut() method");
		}
		List<String> meetingRoomsExternalIdsList;
		HttpGet getRequest = null;
		try	{
		//HttpGet getRequest = new HttpGet("http://192.168.103.193:8080/flexoffice-meetingroomapi/v2/meetingrooms/timeout");
		String request = flexofficeMeetingRoomAPIServerURL + PathConst.MEETINGROOMS_PATH + PathConst.TIMEOUT_PATH;
		getRequest = new HttpGet(request);
		//Set the API media type in http accept header
		getRequest.addHeader("accept", "application/json");
		//Send the request; It will immediately return the response in HttpResponse object
		LOGGER.info("The getRequest in getMeetingRoomsInTimeOut(...) method is : " + getRequest);
		HttpResponse response = httpClient.execute(getRequest);
		//verify the valid error code first
		int statusCode = response.getStatusLine().getStatusCode();
		if ((statusCode != 200) && (statusCode != 201) && (statusCode != 202)) {
			LOGGER.error("Internal error produce in FlexOffice, with error code: " + statusCode);
			throw new FlexOfficeInternalServerException("Internal error produce in FlexOffice, with error code: " + statusCode);
		}
		//Now pull back the response object
		HttpEntity httpEntity = response.getEntity();
		String apiOutput = EntityUtils.toString(httpEntity);
		// parse the JSON response
		ObjectMapper mapper = new ObjectMapper();
		meetingRoomsExternalIdsList = mapper.readValue(apiOutput,new TypeReference<List<String>>() {
		});
		} catch (ClientProtocolException ex) {
			LOGGER.error("Error in httpClient.execute() method, with message: " + ex.getMessage(), ex);
			throw new MeetingRoomInternalServerException("Error in httpClient.execute() method, with message: " + ex.getMessage());
		} catch (IOException e) {
			LOGGER.error("Error in EntityUtils.toString() method, with message: " + e.getMessage(), e);
			throw new MeetingRoomInternalServerException("Error in EntityUtils.toString() method, with message: " + e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug( "End call getMeetingRoomsInTimeOut() method");
			}
			if (getRequest != null) {
				getRequest.releaseConnection();
			}
		}
	
		return meetingRoomsExternalIdsList;
	}

	/**
	 * getDashboardXMLConfigFilesName
	 * @param params DashboardInput
	 * @return List<String> 
	 * @throws FlexOfficeInternalServerException
	 * @throws MeetingRoomInternalServerException
	 * @throws DataNotExistsException
	 */
	public List<String> getDashboardXMLConfigFilesName(DashboardConnectorInput params) throws FlexOfficeInternalServerException, MeetingRoomInternalServerException, DataNotExistsException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "Begin call getDashboardXMLConfigFilesName(DashboardInput params) method");
		}
		List<String> xmlFilesNameList;
		HttpGet getRequest = null;
		try	{
		//HttpGet getRequest = new HttpGet("http://192.168.103.193:8080/flexoffice-meetingroomapi/v2/dashboards/{dashboardMacAddress}/config");
		String request = flexofficeMeetingRoomAPIServerURL + PathConst.DASHBOARDS_PATH + "/" + URLEncoder.encode(params.getDashboardMacAddress(), "UTF-8") + PathConst.CONFIG_PATH;
		getRequest = new HttpGet(request);
		//Set the API media type in http accept header
		getRequest.addHeader("accept", "application/json");
		//Send the request; It will immediately return the response in HttpResponse object
		LOGGER.info("The getRequest in getMeetingRoomsInTimeOut(...) method is : " + getRequest);
		HttpResponse response = httpClient.execute(getRequest);
		//verify the valid error code first
		int statusCode = response.getStatusLine().getStatusCode();
		if ((statusCode != 200) && (statusCode != 201) && (statusCode != 202)) {
			if (statusCode == 404) {
				LOGGER.error("dashboardMacAddress #: " + params.getDashboardMacAddress() + " is not found in FlexOffice DataBase");
				throw new DataNotExistsException("dashboardMacAddress #: " + params.getDashboardMacAddress() + " is not found in FlexOffice DataBase");
			} else {
				LOGGER.error("Internal error produce in FlexOffice, with error code: " + statusCode);
				throw new FlexOfficeInternalServerException("Internal error produce in FlexOffice, with error code: " + statusCode);
			}
		}
		//Now pull back the response object
		HttpEntity httpEntity = response.getEntity();
		String apiOutput = EntityUtils.toString(httpEntity);
		// parse the JSON response
		ObjectMapper mapper = new ObjectMapper();
		xmlFilesNameList = mapper.readValue(apiOutput,new TypeReference<List<String>>() {
		});
		} catch (ClientProtocolException ex) {
			LOGGER.error("Error in httpClient.execute() method, with message: " + ex.getMessage(), ex);
			throw new MeetingRoomInternalServerException("Error in httpClient.execute() method, with message: " + ex.getMessage());
		} catch (IOException e) {
			LOGGER.error("Error in EntityUtils.toString() method, with message: " + e.getMessage(), e);
			throw new MeetingRoomInternalServerException("Error in EntityUtils.toString() method, with message: " + e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug( "End call getDashboardXMLConfigFilesName(DashboardInput params) method");
			}
			if (getRequest != null) {
				getRequest.releaseConnection();
			}
		}
	
		return xmlFilesNameList;
	}

	/**
	 * updateDashboardStatus
	 * @param params DashboardInput
	 * @return DashboardOutput
	 * @throws FlexOfficeInternalServerException
	 * @throws MeetingRoomInternalServerException
	 * @throws DataNotExistsException
	 */
	public DashboardConnectorOutput updateDashboardStatus(DashboardConnectorInput params) throws FlexOfficeInternalServerException, MeetingRoomInternalServerException, DataNotExistsException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "Begin call updateDashboardStatus(DashboardInput params) method");
		}
		DashboardConnectorOutput dashboardOutput = new DashboardConnectorOutput();
		// construct the writer from DashboardInput
		String writer = flexofficeDataTools.constructJSONDashboardStatus(params);
		HttpPut putRequest = null;
		try	{
			// Define a putRequest request
			String request = flexofficeMeetingRoomAPIServerURL + PathConst.DASHBOARDS_PATH +"/" + URLEncoder.encode(params.getDashboardMacAddress(), "UTF-8");
			putRequest = new HttpPut(request);
			//Set the API media type in http content-type header
			putRequest.addHeader("content-type", "application/json");
			//Set the request post body
			StringEntity input = new StringEntity(writer);
			putRequest.setEntity(input);
			//Send the request; It will immediately return the response in HttpResponse object if any
			LOGGER.info("The putRequest in updateDashboardStatus(...) method is : " + putRequest);
			HttpResponse response = httpClient.execute(putRequest);
			//verify the valid error code first
			int statusCode = response.getStatusLine().getStatusCode();
			if ((statusCode != 200) && (statusCode != 201) && (statusCode != 202)) {
				if (statusCode == 404) {
					LOGGER.error("dashboardMacAddress #: " + params.getDashboardMacAddress() + " is not found in FlexOffice DataBase");
					throw new DataNotExistsException("dashboardMacAddress #: " + params.getDashboardMacAddress() + " is not found in FlexOffice DataBase");
				} else {
					LOGGER.error("Internal error produce in FlexOffice, with error code: " + statusCode);
					throw new FlexOfficeInternalServerException("Internal error produce in FlexOffice, with error code: " + statusCode);
				}
			}
			//Now pull back the response object
			HttpEntity httpEntity = response.getEntity();
			String apiOutput = EntityUtils.toString(httpEntity);
			// parse the JSON response
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> mp = mapper.readValue(apiOutput,new TypeReference<Map<String, Object>>() {
			});
			String command = (String)mp.get("command");
			dashboardOutput.setCommand(EnumCommand.valueOf(command));
			
			return dashboardOutput;
			
		} catch (ClientProtocolException ex) {
			LOGGER.error("Error in httpClient.execute() method, with message: " + ex.getMessage(), ex);
			throw new MeetingRoomInternalServerException("Error in httpClient.execute() method, with message: " + ex.getMessage());
		} catch (IOException e) {
			LOGGER.error("Error in EntityUtils.toString() method, with message: " + e.getMessage(), e);
			throw new MeetingRoomInternalServerException("Error in EntityUtils.toString() method, with message: " + e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug( "End call updateDashboardStatus(DashboardInput params) method");
			}
			if (putRequest != null) {
				putRequest.releaseConnection();
			}
		}
	}
	
	/**
	 * updateAgentStatus
	 * @param params AgentInput
	 * @return AgentOutput
	 * @throws MethodNotAllowedException 
	 * @throws DataNotExistsException 
	 * @throws FlexOfficeInternalServerException 
	 * @throws MeetingRoomInternalServerException 
	 * @throws Exception
	 */
	public AgentConnectorOutput updateAgentStatus(AgentConnectorInput params) throws MethodNotAllowedException, DataNotExistsException, FlexOfficeInternalServerException, MeetingRoomInternalServerException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "Begin call updateAgentStatus(AgentInput params) method");
		}
		AgentConnectorOutput agentOutput = new AgentConnectorOutput();
		// construct the writer from AgentInput
		String writer = flexofficeDataTools.constructJSONAgentStatus(params);
		HttpPut putRequest = null;
		try	{
			// Define a putRequest request
			String request = flexofficeMeetingRoomAPIServerURL + PathConst.AGENTS_PATH +"/" + URLEncoder.encode(params.getAgentMacAddress(), "UTF-8");
			putRequest = new HttpPut(request);
			//Set the API media type in http content-type header
			putRequest.addHeader("content-type", "application/json");
			//Set the request post body
			StringEntity input = new StringEntity(writer);
			putRequest.setEntity(input);
			//Send the request; It will immediately return the response in HttpResponse object if any
			LOGGER.info("The putRequest in updateAgentStatus(...) method is : " + putRequest);
			HttpResponse response = httpClient.execute(putRequest);
			//verify the valid error code first
			int statusCode = response.getStatusLine().getStatusCode();
			if ((statusCode != 200) && (statusCode != 201) && (statusCode != 202)) {
				if (statusCode == 404) {
					LOGGER.error("agentMacAddress #: " + params.getAgentMacAddress() + " is not found in FlexOffice DataBase");
					throw new DataNotExistsException("agentMacAddress #: " + params.getAgentMacAddress() + " is not found in FlexOffice DataBase");
				} else {
					LOGGER.error("Internal error produce in FlexOffice, with error code: " + statusCode);
					throw new FlexOfficeInternalServerException("Internal error produce in FlexOffice, with error code: " + statusCode);
				}
			}
			//Now pull back the response object
			HttpEntity httpEntity = response.getEntity();
			String apiOutput = EntityUtils.toString(httpEntity);
			// parse the JSON response
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> mp = mapper.readValue(apiOutput,new TypeReference<Map<String, Object>>() {
			});
			
			String meetingRoomExternalId = (String)mp.get("meetingRoomExternalId");
			if (meetingRoomExternalId == null) {
				LOGGER.error("agentMacAddress #: " + params.getAgentMacAddress() + " is not not paired to a meetingroom");
				throw new MethodNotAllowedException("agentMacAddress #: " + params.getAgentMacAddress() + " is not paired to a meetingroom");
			} 
			String command = (String)mp.get("command");
			agentOutput.setMeetingRoomExternalId(meetingRoomExternalId);
			agentOutput.setCommand(EnumCommand.valueOf(command));
			
			return agentOutput;
			
		} catch (ClientProtocolException ex) {
			LOGGER.error("Error in httpClient.execute() method, with message: " + ex.getMessage(), ex);
			throw new MeetingRoomInternalServerException("Error in httpClient.execute() method, with message: " + ex.getMessage());
		} catch (IOException e) {
			LOGGER.error("Error in EntityUtils.toString() method, with message: " + e.getMessage(), e);
			throw new MeetingRoomInternalServerException("Error in EntityUtils.toString() method, with message: " + e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug( "End call updateAgentStatus(AgentInput params) method");
			}
			if (putRequest != null) {
				putRequest.releaseConnection();
			}
		}	
	}

	/**
	 * updateMeetingRoomData
	 * @param params MeetingRoomData
	 * @throws FlexOfficeInternalServerException
	 * @throws MeetingRoomInternalServerException
	 * @throws DataNotExistsException
	 */
	public void updateMeetingRoomData(MeetingRoomData params) throws FlexOfficeInternalServerException, MeetingRoomInternalServerException, DataNotExistsException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Begin call updateMeetingRoomData(MeetingRoomData params) method");
		}
		HttpPut putRequest = null;
		try	{
		// construct the writer from MeetingRoomData
		String writer = flexofficeDataTools.constructJSONMeetingRoomData(params);
			// Define a putRequest request
			String request = flexofficeMeetingRoomAPIServerURL + PathConst.MEETINGROOMS_PATH +"/" + URLEncoder.encode(params.getMeetingRoomExternalId(), "UTF-8");
			putRequest = new HttpPut(request);
			//Set the API media type in http content-type header
			putRequest.addHeader("content-type", "application/json");
			//Set the request post body
			StringEntity input = new StringEntity(writer);
			putRequest.setEntity(input);
			//Send the request; It will immediately return the response in HttpResponse object if any
			LOGGER.info("The putRequest in updateMeetingRoomData(...) method is : " + putRequest);
			LOGGER.info("The writer in putRequest in updateMeetingRoomData(...) method is : " + writer);
			HttpResponse response = httpClient.execute(putRequest);
			//verify the valid error code first
			int statusCode = response.getStatusLine().getStatusCode();
			if ((statusCode != 200) && (statusCode != 201) && (statusCode != 202)) {
				if (statusCode == 404) {
					LOGGER.error("meetingRoomExternalId #: " + params.getMeetingRoomExternalId() + " is not found in FlexOffice DataBase");
					throw new DataNotExistsException("meetingRoomExternalId #: " + params.getMeetingRoomExternalId() + " is not found in FlexOffice DataBase");
				} else {
					LOGGER.error("Internal error produce in FlexOffice, with error code: " + statusCode);
					throw new FlexOfficeInternalServerException("Internal error produce in FlexOffice, with error code: " + statusCode);
				}
			}
		} catch (ClientProtocolException ex) {
			LOGGER.error("Error in httpClient.execute() method, with message: " + ex.getMessage(), ex);
			throw new MeetingRoomInternalServerException("Error in httpClient.execute() method, with message: " + ex.getMessage());
		} catch (UnsupportedEncodingException e) {
			LOGGER.debug( "UnsupportedEncodingException exception in updateMeetingRoomData() method");
			throw new MeetingRoomInternalServerException("Error in url encode method, with message: " + e.getMessage());
		} catch (IOException e) {
			LOGGER.error("Error in EntityUtils.toString() method, with message: " + e.getMessage(), e);
			throw new MeetingRoomInternalServerException("Error in EntityUtils.toString() method, with message: " + e.getMessage());
		} finally {	
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug( "End call updateMeetingRoomData(MeetingRoomData params) method");
			}
			if (putRequest != null) {
				putRequest.releaseConnection();
			}
		}
	}
}
