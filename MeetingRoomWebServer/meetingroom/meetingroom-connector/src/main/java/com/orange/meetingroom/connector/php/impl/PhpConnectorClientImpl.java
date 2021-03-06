package com.orange.meetingroom.connector.php.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.meetingroom.connector.exception.DataNotExistsException;
import com.orange.meetingroom.connector.exception.MeetingRoomInternalServerException;
import com.orange.meetingroom.connector.exception.MethodNotAllowedException;
import com.orange.meetingroom.connector.exception.PhpInternalServerException;
import com.orange.meetingroom.connector.php.PhpConnectorClient;
import com.orange.meetingroom.connector.php.enums.EnumBookingDetails;
import com.orange.meetingroom.connector.php.enums.EnumRoomDetails;
import com.orange.meetingroom.connector.php.enums.EnumRoomInfos;
import com.orange.meetingroom.connector.php.model.request.GetAgentBookingsParameters;
import com.orange.meetingroom.connector.php.model.request.GetDashboardBookingsParameters;
import com.orange.meetingroom.connector.php.model.request.SetBookingParameters;
import com.orange.meetingroom.connector.php.model.request.UpdateBookingParameters;
import com.orange.meetingroom.connector.php.model.response.BookingConnectorReturn;
import com.orange.meetingroom.connector.php.model.response.BookingSummary;
import com.orange.meetingroom.connector.php.model.response.MeetingRoomConnectorReturn;
import com.orange.meetingroom.connector.php.model.response.MeetingRoomBookingsConnectorReturn;
import com.orange.meetingroom.connector.php.model.response.MeetingRoomDetailsConnectorReturn;
import com.orange.meetingroom.connector.php.model.response.MeetingRoomsConnectorReturn;
import com.orange.meetingroom.connector.php.model.response.SystemCurrentDateConnectorReturn;
import com.orange.meetingroom.connector.php.utils.DataTools;

/**
 * PhpConnectorClientImpl
 * @author oab
 *
 */
@Service("PhpConnectorClient")
public class PhpConnectorClientImpl implements PhpConnectorClient {
	
	private static final Logger LOGGER = Logger.getLogger(PhpConnectorClientImpl.class);
	
	//Create a new instance of http client which will connect to REST api over network
	@Autowired
	private CloseableHttpClient httpClient;
	@Autowired
	private DataTools dataTools;
	@Value("${php.get.bookings}")
	private String phpGetBookingsURL;
	@Value("${php.set.bookings}")
	private String phpSetBookingsURL;
	@Value("${php.update.bookings}")
	private String phpUpdateBookingsURL;
	
	//**************************************************************************
    //************************* METHODS  ***************************************
    //**************************************************************************
	
	/**
	 * getCurrentDate
	 * @return SystemCurrentDateConnectorReturn
	 * @throws PhpInternalServerException 
	 * @throws MeetingRoomInternalServerException 
	 */
	@SuppressWarnings("unchecked")
	public SystemCurrentDateConnectorReturn getCurrentDate() throws PhpInternalServerException, MeetingRoomInternalServerException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "Begin call getCurrentDate() method");
		}
		HttpGet getRequest = null;
		try {
			SystemCurrentDateConnectorReturn returnedCurrentDate = new SystemCurrentDateConnectorReturn();
			String request = phpGetBookingsURL + "?" + dataTools.getCurrentDateParameterToUrlEncode(); 
			getRequest = new HttpGet(request);
			//Send the request; It will immediately return the response in HttpResponse object
			LOGGER.info("The getRequest in getCurrentDate() method is : " + getRequest);
			HttpResponse response = httpClient.execute(getRequest);
			//verify the valid error code first
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				LOGGER.error("Internal error produce in PHP server, with error code: " + statusCode);
				throw new PhpInternalServerException("Internal error produce in Php server, with error code: " + statusCode);
			}
			//Now pull back the response object
			HttpEntity httpEntity = response.getEntity();
			String apiOutput = EntityUtils.toString(httpEntity);
			// parse the JSON response
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> mp = mapper.readValue(apiOutput,new TypeReference<Map<String, Object>>() {
			});
			// get currentDate
			Integer currentDate = (Integer)((Map<String, Object>)mp.get(EnumRoomInfos.INFOS.value())).get(EnumRoomInfos.CURRENT_DATE.value());
			returnedCurrentDate.setCurrentDate(currentDate);
			return returnedCurrentDate;
			
		} catch (ClientProtocolException e) {
			LOGGER.error("Error in httpClient.execute() method, with message: " + e.getMessage(), e);
			throw new MeetingRoomInternalServerException("Error in httpClient.execute() method, with message: " + e.getMessage());
		} catch (IOException e) {
			LOGGER.error("Error in EntityUtils.toString() method, with message: " + e.getMessage(), e);
			throw new MeetingRoomInternalServerException("Error in EntityUtils.toString() method, with message: " + e.getMessage());
		} finally	{
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug( "End call getCurrentDate() method");
			}
			if (getRequest != null) {
				getRequest.releaseConnection();
			}
		}
	}
	
	
	@SuppressWarnings("unchecked")
	/**
	 * getBookingsFromAgent
	 * @param GetAgentBookingsParameters params
	 * @return MeetingRoomBookings
	 * @throws Exception
	 */
	public MeetingRoomConnectorReturn getBookingsFromAgent(GetAgentBookingsParameters params) throws MeetingRoomInternalServerException, PhpInternalServerException, DataNotExistsException, MethodNotAllowedException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "Begin call getBookingsFromAgent(GetAgentBookingsParameters params) method");
		}
		MeetingRoomConnectorReturn meetingroom = new MeetingRoomConnectorReturn();
		MeetingRoomBookingsConnectorReturn meetingRoomBookings = new MeetingRoomBookingsConnectorReturn();
		// HttpClientBuilder builder = HttpClientBuilder.create();
	    // CloseableHttpClient httpClient = builder.build(); 
		HttpGet getRequest = null;
	    try	{
			//HttpGet getRequest = new HttpGet("http://192.168.103.193/services/GetBookings.php?format=json&RoomID=brehat.rennes@microsoft.cad.aql.fr&ForceUpdateCache=false&_=1461057699231");
			String request = phpGetBookingsURL + "?" + dataTools.getAgentBookingsParametersToUrlEncode(params);
			getRequest = new HttpGet(request);
			//Set the API media type in http accept header
			getRequest.addHeader("accept", "application/json");
			//Send the request; It will immediately return the response in HttpResponse object
			LOGGER.info("The getRequest in getBookingsFromAgent(...) method is : " + getRequest);
			HttpResponse response = httpClient.execute(getRequest);
			//verify the valid error code first
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				LOGGER.error("Internal error produce in PHP server, with error code: " + statusCode);
				throw new PhpInternalServerException("Internal error produce in Php server, with error code: " + statusCode);
			}
			//Now pull back the response object
			HttpEntity httpEntity = response.getEntity();
			String apiOutput = EntityUtils.toString(httpEntity);
			//LOGGER.info("apiOutput in getBookingsFromAgent(...) method is : " + apiOutput);
			// parse the JSON response
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> mp = mapper.readValue(apiOutput,new TypeReference<Map<String, Object>>() {
			});
			// get currentDate
			Integer currentDate = (Integer)((Map<String, Object>)mp.get(EnumRoomInfos.INFOS.value())).get(EnumRoomInfos.CURRENT_DATE.value());
			meetingroom.setCurrentDate(currentDate);
			
			Map<String, Map<String, Object>> roomLevelOneMap = (Map<String, Map<String, Object>>)mp.get(EnumRoomInfos.ROOMS.value());
			for (Entry<String, Map<String, Object>> element : roomLevelOneMap.entrySet()) {
				try {
					Boolean errorFlag = (Boolean)element.getValue().get(EnumRoomInfos.ERROR_FLAG.value());
					String message = (String)element.getValue().get(EnumRoomInfos.MESSAGE.value());
					if (errorFlag) { // errorFlag = true
						LOGGER.error("errorFlag is true in Php Server return with message:" + message);
						throw new MethodNotAllowedException("errorFlag is true in Php Server return with message:" + message);
					}
				} catch (RuntimeException e ) { // {"Infos":{"CurrentDate":1461938475},"Rooms":{"toto":false}}
					LOGGER.error("meetingRoomExternalId is not exist or Connexion to Exchange Server is impossible." , e);
					throw new DataNotExistsException("meetingRoomExternalId is not exist or Connexion to Exchange Server is impossible: "  + e.getMessage());
				}
			}
			
			Map<String, Map<String, Map<String, Object>>> roomMap = (Map<String, Map<String, Map<String, Object>>>)mp.get(EnumRoomInfos.ROOMS.value());
			for (Entry<String, Map<String, Map<String, Object>>> element : roomMap.entrySet()) {
				try {	
					MeetingRoomDetailsConnectorReturn details = new MeetingRoomDetailsConnectorReturn(); 
					String meetingRoomExternalId = (String)element.getValue().get(EnumRoomInfos.ROOM_DETAILS.value()).get(EnumRoomDetails.ROOM_ID.value());
					String meetingRoomExternalName = (String)element.getValue().get(EnumRoomInfos.ROOM_DETAILS.value()).get(EnumRoomDetails.ROOM_NAME.value());
					String meetingRoomExternalLocation = (String)element.getValue().get(EnumRoomInfos.ROOM_DETAILS.value()).get(EnumRoomDetails.ROOM_LOCATION.value());
					
					details.setMeetingRoomExternalId(meetingRoomExternalId);
					details.setMeetingRoomExternalName(meetingRoomExternalName);
					details.setMeetingRoomExternalLocation(meetingRoomExternalLocation);
					
					meetingRoomBookings.setMeetingRoomDetails(details);
					meetingroom.setMeetingRoom(meetingRoomBookings);
				} catch (RuntimeException e) {
					LOGGER.error("Error when parsing RoomDetails element, with message: " + e.getMessage(), e);
					throw new MeetingRoomInternalServerException("Error when parsing RoomDetails element, with message: " + e.getMessage());
				}	
			}
			
			Map<String, Map<String, Map<String, Map<String, Object>>>> roomMapBookings = (Map<String, Map<String, Map<String, Map<String, Object>>>>)mp.get(EnumRoomInfos.ROOMS.value());
			for (Entry<String, Map<String, Map<String, Map<String, Object>>>> elementBooking : roomMapBookings.entrySet()) {
				try {
					Map<String, Map<String, Object>> bookings =  (Map<String, Map<String, Object>>)elementBooking.getValue().get(EnumRoomInfos.BOOKINGS.value());
					for (Entry<String, Map<String, Object>> book : bookings.entrySet()) {
						BookingConnectorReturn booking = new BookingConnectorReturn();
						String idReservation = (String)book.getValue().get(EnumBookingDetails.ID_RESERVATION.value());
						String revisionReservation = (String)book.getValue().get(EnumBookingDetails.REVISION_RESERVATION.value());
						String organizer = (String)book.getValue().get(EnumBookingDetails.ORGANIZER.value());
						String organizeFullName = (String)book.getValue().get(EnumBookingDetails.ORGANIZER_FULL_NAME.value());
						String organizerMail = (String)book.getValue().get(EnumBookingDetails.ORGANIZER_EMAIL.value());
						String creator = (String)book.getValue().get(EnumBookingDetails.CREATOR.value());
						String creatorFullName = (String)book.getValue().get(EnumBookingDetails.CREATOR_FULL_NAME.value());
						String creatorEmail = (String)book.getValue().get(EnumBookingDetails.CREATOR_EMAIL.value());
						String subject = (String)book.getValue().get(EnumBookingDetails.SUBJECT.value());
						Integer startDate = (Integer)book.getValue().get(EnumBookingDetails.START_DATE.value());
						Integer endDate = (Integer)book.getValue().get(EnumBookingDetails.END_DATE.value());
						Boolean acknowledged = (Boolean)book.getValue().get(EnumBookingDetails.ACKNOWLEDGED.value());
					
						booking.setIdReservation(idReservation);
						booking.setRevisionReservation(revisionReservation);
						booking.setSubject(subject);
						booking.setStartDate(startDate);
						booking.setEndDate(endDate);
						booking.setAcknowledged(acknowledged);
						
						String constructedOrganizer = dataTools.constructOrganizerFullName(organizer, organizeFullName, organizerMail, creator, creatorFullName, creatorEmail);
						booking.setOrganizerFullName(constructedOrganizer);		
					
						meetingroom.getMeetingRoom().getBookings().add(booking);
					}
				} catch (java.lang.ClassCastException e) {
					// if not bookings, PHP returns ( "Bookings": []) witch produce this exception
					LOGGER.debug("No bookings found:" + e.getMessage(), e);
				} catch (RuntimeException e) {
					LOGGER.error("Error when parsing Bookings element, with message: " + e.getMessage(), e);
					throw new MeetingRoomInternalServerException("Error when parsing Bookings element, with message: " + e.getMessage());
				}	
			}			
			
			return meetingroom;
			
		} catch (ClientProtocolException ex) {
			LOGGER.error("Error in httpClient.execute() method, with message: " + ex.getMessage(), ex);
			throw new MeetingRoomInternalServerException("Error in httpClient.execute() method, with message: " + ex.getMessage());
		} catch (IOException e) {
			LOGGER.error("Error in EntityUtils.toString() method, with message: " + e.getMessage(), e);
			throw new MeetingRoomInternalServerException("Error in EntityUtils.toString() method, with message: " + e.getMessage());
		} finally	{
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug( "End call getBookingsFromAgent(GetAgentBookingsParameters params) method");
			}
			if (getRequest != null) {
				getRequest.releaseConnection();
			}
			//Important: Close the connect
			//httpClient.close();
		}
	}

	@SuppressWarnings("unchecked")
	/**
	 * getBookingsFromDashboard
	 * @param GetDashboardBookingsParameters params
	 * @throws Exception
	 */
	public MeetingRoomsConnectorReturn getBookingsFromDashboard(GetDashboardBookingsParameters params) throws MeetingRoomInternalServerException, PhpInternalServerException, MethodNotAllowedException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "Begin call getBookingsFromDashboard(GetDashboardBookingsParameters params) method");
		}
		MeetingRoomsConnectorReturn meetingrooms = new MeetingRoomsConnectorReturn();
		List<MeetingRoomBookingsConnectorReturn> meetingRoomBookingsList = new ArrayList<MeetingRoomBookingsConnectorReturn>(); 
		HttpGet getRequest = null;
		try	{
			//HttpGet getRequest = new HttpGet("http://192.168.103.193/services/GetBookings.php?format=json&MaxBookings=2&StartDate=0&RoomGroupID=rg_oab_full&_=1461061105469");
			String request = phpGetBookingsURL + "?" + dataTools.getDashboardBookingsParametersToUrlEncode(params);
			getRequest = new HttpGet(request);
			//Set the API media type in http accept header
			getRequest.addHeader("accept", "application/json");
			//Send the request; It will immediately return the response in HttpResponse object
			LOGGER.info("The getRequest in getBookingsFromAgent(...) method is : " + getRequest);
			HttpResponse response = httpClient.execute(getRequest);
			//verify the valid error code first
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				LOGGER.error("Internal error produce in PHP server, with error code: " + statusCode);
				throw new PhpInternalServerException("Internal error produce in Php server, with error code: " + statusCode);
			}
			//Now pull back the response object
			HttpEntity httpEntity = response.getEntity();
			String apiOutput = EntityUtils.toString(httpEntity);
			//LOGGER.info("apiOutput in getBookingsFromDashboard(...) method is : " + apiOutput);
			// parse the JSON response
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> mp = mapper.readValue(apiOutput,new TypeReference<Map<String, Object>>() {
			});
			// get currentDate
			Integer currentDate = (Integer)((Map<String, Object>)mp.get(EnumRoomInfos.INFOS.value())).get(EnumRoomInfos.CURRENT_DATE.value());
			meetingrooms.setCurrentDate(currentDate);
			try {
				Map<String, Map<String, Map<String, Object>>> roomMap = (Map<String, Map<String, Map<String, Object>>>)mp.get(EnumRoomInfos.ROOMS.value());
				for (Entry<String, Map<String, Map<String, Object>>> element : roomMap.entrySet()) {
					try {
						MeetingRoomBookingsConnectorReturn meetingRoomBookings = new MeetingRoomBookingsConnectorReturn();
						MeetingRoomDetailsConnectorReturn details = new MeetingRoomDetailsConnectorReturn(); 
						String meetingRoomExternalId = (String)element.getValue().get(EnumRoomInfos.ROOM_DETAILS.value()).get(EnumRoomDetails.ROOM_ID.value());
						String meetingRoomExternalName = (String)element.getValue().get(EnumRoomInfos.ROOM_DETAILS.value()).get(EnumRoomDetails.ROOM_NAME.value());
						String meetingRoomExternalLocation = (String)element.getValue().get(EnumRoomInfos.ROOM_DETAILS.value()).get(EnumRoomDetails.ROOM_LOCATION.value());
						details.setMeetingRoomExternalId(meetingRoomExternalId);
						details.setMeetingRoomExternalName(meetingRoomExternalName);
						details.setMeetingRoomExternalLocation(meetingRoomExternalLocation);
						meetingRoomBookings.setMeetingRoomDetails(details);
						meetingRoomBookingsList.add(meetingRoomBookings);
					} catch (RuntimeException e) {
						// if not roomId, PHP returns ( "Rooms": {"toto": false }, {...}) witch produce this exception
						LOGGER.error("no roomId is found in exchange server ", e);
						throw new MethodNotAllowedException("no roomId is found in exchange server: "  + e.getMessage());
					}
				}
				
				Map<String, Map<String, Map<String, Map<String, Object>>>> roomMapBookings = (Map<String, Map<String, Map<String, Map<String, Object>>>>)mp.get(EnumRoomInfos.ROOMS.value());
				int incr = 0; // get data from meetingRoomBookingsList for update bookings in the correct meetingRoomBookings object
				try {
					for (Entry<String, Map<String, Map<String, Map<String, Object>>>> elementBooking : roomMapBookings.entrySet()) {
						MeetingRoomBookingsConnectorReturn meetingRoomBookings = meetingRoomBookingsList.get(incr);
						incr = incr + 1;
						try {
							Map<String, Map<String, Object>> bookings =  (Map<String, Map<String, Object>>)elementBooking.getValue().get(EnumRoomInfos.BOOKINGS.value());
							for (Entry<String, Map<String, Object>> book : bookings.entrySet()) {
								BookingConnectorReturn booking = new BookingConnectorReturn();
								String idReservation = (String)book.getValue().get(EnumBookingDetails.ID_RESERVATION.value());
								String revisionReservation = (String)book.getValue().get(EnumBookingDetails.REVISION_RESERVATION.value());
								String organizer = (String)book.getValue().get(EnumBookingDetails.ORGANIZER.value());
								String organizeFullName = (String)book.getValue().get(EnumBookingDetails.ORGANIZER_FULL_NAME.value());
								String organizerMail = (String)book.getValue().get(EnumBookingDetails.ORGANIZER_EMAIL.value());
								String creator = (String)book.getValue().get(EnumBookingDetails.CREATOR.value());
								String creatorFullName = (String)book.getValue().get(EnumBookingDetails.CREATOR_FULL_NAME.value());
								String creatorEmail = (String)book.getValue().get(EnumBookingDetails.CREATOR_EMAIL.value());
								String subject = (String)book.getValue().get(EnumBookingDetails.SUBJECT.value());
								Integer startDate = (Integer)book.getValue().get(EnumBookingDetails.START_DATE.value());
								Integer endDate = (Integer)book.getValue().get(EnumBookingDetails.END_DATE.value());
								Boolean acknowledged = (Boolean)book.getValue().get(EnumBookingDetails.ACKNOWLEDGED.value());
								booking.setIdReservation(idReservation);
								booking.setRevisionReservation(revisionReservation);
								booking.setSubject(subject);
								booking.setStartDate(startDate);
								booking.setEndDate(endDate);
								booking.setAcknowledged(acknowledged);
								String constructedOrganizer = dataTools.constructOrganizerFullName(organizer, organizeFullName, organizerMail, creator, creatorFullName, creatorEmail);
								booking.setOrganizerFullName(constructedOrganizer);		
								meetingRoomBookings.getBookings().add(booking);
							}
						} catch (java.lang.ClassCastException e) {
							// if not bookings, PHP returns ( "Bookings": []) witch produce this exception
							LOGGER.debug("No bookings found.", e);
						} catch (RuntimeException e) {
							LOGGER.error("Error when parsing Bookings element, with message: " + e.getMessage(), e);
							throw new MeetingRoomInternalServerException("Error when parsing Bookings element, with message: " + e.getMessage());
						}	
					}
				} catch (RuntimeException e) {
					// if not roomId, PHP returns ( "Rooms": {"toto": false }) witch produce this exception
					LOGGER.error("error when parsing rooms: " + e.getMessage(), e);
					throw new MeetingRoomInternalServerException("error when parsing rooms: " + e.getMessage());
				}
				
				meetingrooms.setMeetingRooms(meetingRoomBookingsList);

			} catch (java.lang.ClassCastException e) {
				// if not rooms, PHP returns ( "Rooms": []) witch produce this exception
				LOGGER.debug("no rooms found.", e);
			}
			
			return meetingrooms;

		} catch (ClientProtocolException ex) {
			LOGGER.error("Error in httpClient.execute() method, with message: " + ex.getMessage(), ex);
			throw new MeetingRoomInternalServerException("Error in httpClient.execute() method, with message: " + ex.getMessage());
		} catch (IOException e) {
			LOGGER.error("Error in EntityUtils.toString() method, with message: " + e.getMessage(), e);
			throw new MeetingRoomInternalServerException("Error in EntityUtils.toString() method, with message: " + e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug( "End call getBookingsFromDashboard(GetDashboardBookingsParameters params) method");
			}
			if (getRequest != null) {
				getRequest.releaseConnection();
			}
		}
	}

	/**
	 * setBooking
	 * @param SetBookingParameters params
	 * @throws MeetingRoomInternalServerException 
	 * @throws MethodNotAllowedException 
	 * @throws PhpInternalServerException 
	 * @throws Exception
	 */
	public BookingSummary setBooking(SetBookingParameters params) throws MeetingRoomInternalServerException, MethodNotAllowedException, PhpInternalServerException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "Begin call setBooking(SetBookingParameters params) method");
		}
		
		HttpPost postRequest = null;
		
		try	{
		BookingSummary bookingSummary = new BookingSummary();
		// construct writer using SetBookingParameters
		// String writer = "RoomID=brehat.rennes@microsoft.cad.aql.fr&OrganizerFullName=&Subject=&format=json&StartDate=1461060000&EndDate=1461060600&Acknowledged=1";
		String writer = dataTools.setBookingParametersToUrlEncode(params);
			//Define a postRequest request
			//HttpPost postRequest = new HttpPost("http://192.168.103.193/services/SetBooking.php");
			postRequest = new HttpPost(phpSetBookingsURL);
			//Set the API media type in http content-type header
			postRequest.addHeader("content-type", "application/x-www-form-urlencoded");
			//Set the request post body
			StringEntity userEntity = new StringEntity(writer);
			postRequest.setEntity(userEntity);
			//Send the request; It will immediately return the response in HttpResponse object if any
			LOGGER.info("The postRequest in setBooking(...) method is : " + postRequest);
			HttpResponse response = httpClient.execute(postRequest);
			//verify the valid error code first
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				LOGGER.error("Internal error produce in PHP server, with error code: " + statusCode);
				throw new PhpInternalServerException("Internal error produce in Php server, with error code: " + statusCode);
			}
			//Now pull back the response object
			HttpEntity httpEntity = response.getEntity();
			String apiOutput = EntityUtils.toString(httpEntity);
			LOGGER.info("apiOutput in setBooking(...) method is : " + apiOutput);
			// parse the JSON response
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> mp = mapper.readValue(apiOutput,new TypeReference<Map<String, Object>>() {
			});
			Boolean errorFlag = (Boolean)mp.get(EnumRoomInfos.ERROR_FLAG.value());
			if (errorFlag) {
				String errorMessage = (String)mp.get(EnumRoomInfos.MESSAGE.value());
				LOGGER.error("error when set booking:" + errorMessage);
				throw new MethodNotAllowedException("error when set booking:" + errorMessage);
			} else {
				String idReservation = (String)mp.get(EnumBookingDetails.ID_RESERVATION.value());
				String revisionReservation = (String)mp.get(EnumBookingDetails.REVISION_RESERVATION.value());
				bookingSummary.setIdReservation(idReservation);
				bookingSummary.setRevisionReservation(revisionReservation);
			}
			
			return bookingSummary;
			
		} catch (ClientProtocolException ex) {
			LOGGER.error("Error in httpClient.execute() method, with message: " + ex.getMessage(), ex);
			throw new MeetingRoomInternalServerException("Error in httpClient.execute() method, with message: " + ex.getMessage());
		} catch (UnsupportedEncodingException e) {
			LOGGER.debug( "UnsupportedEncodingException exception in setBooking(SetBookingParameters params) method");
			throw new MeetingRoomInternalServerException("Error in url encode method, with message: " + e.getMessage());
		} catch (IOException e) {
			LOGGER.error("Error in EntityUtils.toString() method, with message: " + e.getMessage(), e);
			throw new MeetingRoomInternalServerException("Error in EntityUtils.toString() method, with message: " + e.getMessage());
		} finally	{
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug( "End call setBooking(SetBookingParameters params) method");
			}
			if (postRequest != null) {
				postRequest.releaseConnection();
			}
		}
	}
	
	/**
	 * updateBooking
	 * @param UpdateBookingParameters params
	 * @throws MeetingRoomInternalServerException 
	 * @throws MethodNotAllowedException 
	 * @throws PhpInternalServerException 
	 * @throws Exception
	 */
	public BookingSummary updateBooking(UpdateBookingParameters params) throws MeetingRoomInternalServerException, MethodNotAllowedException, PhpInternalServerException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "Begin call updateBooking(UpdateBookingParameters params) method");
		}
		BookingSummary bookingSummary = new BookingSummary();
		// construct the writer from UpdateBookingParameters
		// String writer = "RoomID=brehat.rennes@microsoft.cad.aql.fr&IDReservation=AAAiAGJyZWhhdC5yZW5uZXNAbWljcm9zb2Z0LmNhZC5hcWwuZnIARgAAAAAAJjiq1ulLK0Kj6vNsTnRuywcAQopQvd4yGUaRbVXWgALbzwAAAAfOdQAAQopQvd4yGUaRbVXWgALbzwAAkZg7ggAA&RevisionReservation=DwAAABYAAABCilC93jIZRpFtVdaAAtvPAACRmK21&EndDate=1461060745&format=json";
		String writer;
		HttpPost postRequest = null;
		try	{
			writer = dataTools.updateBookingParametersToUrlEncode(params);
			// Define a postRequest request
			// HttpPost postRequest = new HttpPost("http://192.168.103.193/services/UpdateBooking.php");
			postRequest = new HttpPost(phpUpdateBookingsURL);
			//Set the API media type in http content-type header
			postRequest.addHeader("content-type", "application/x-www-form-urlencoded");
			//Set the request post body
			StringEntity userEntity = new StringEntity(writer);
			postRequest.setEntity(userEntity);
			//Send the request; It will immediately return the response in HttpResponse object if any
			LOGGER.info("The postRequest in updateBooking(...) method is : " + postRequest);
			HttpResponse response = httpClient.execute(postRequest);
			//verify the valid error code first
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				LOGGER.error("Internal error produce in PHP server, with error code: " + statusCode);
				throw new PhpInternalServerException("Internal error produce in Php server, with error code: " + statusCode);
			}
			//Now pull back the response object
			HttpEntity httpEntity = response.getEntity();
			String apiOutput = EntityUtils.toString(httpEntity);
			LOGGER.info("apiOutput in updateBooking(...) method is : " + apiOutput);
			// parse the JSON response
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> mp = mapper.readValue(apiOutput,new TypeReference<Map<String, Object>>() {
			});
			Boolean errorFlag = (Boolean)mp.get(EnumRoomInfos.ERROR_FLAG.value());
			if (errorFlag) {
				String errorMessage = (String)mp.get(EnumRoomInfos.MESSAGE.value());
				LOGGER.error("error when update booking:" + errorMessage);
				throw new MethodNotAllowedException("error when update booking:" + errorMessage);
			} else {
				String idReservation = (String)mp.get(EnumBookingDetails.ID_RESERVATION.value());
				String revisionReservation = (String)mp.get(EnumBookingDetails.REVISION_RESERVATION.value());
				bookingSummary.setIdReservation(idReservation);
				bookingSummary.setRevisionReservation(revisionReservation);
			}
			
			return bookingSummary;
		
		} catch (ClientProtocolException e) {
			LOGGER.error("Error in httpClient.execute() method, with message: " + e.getMessage(), e);
			throw new MeetingRoomInternalServerException("Error in httpClient.execute() method, with message: " + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			LOGGER.debug( "UnsupportedEncodingException exception in updateBooking(UpdateBookingParameters params) method");
			throw new MeetingRoomInternalServerException("Error in url encode method, with message: " + e.getMessage());
		} catch (IOException e) {
			LOGGER.error("Error in EntityUtils.toString() method, with message: " + e.getMessage(), e);
			throw new MeetingRoomInternalServerException("Error in EntityUtils.toString() method, with message: " + e.getMessage());
		}  finally	{
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug( "End call updateBooking(UpdateBookingParameters params) method");
			}
			if (postRequest != null) {
				postRequest.releaseConnection();
			}
		}
	}
	
}
