package com.orange.meetingroom.connector.php;

import java.io.IOException;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.meetingroom.connector.exception.DataNotExistsException;
import com.orange.meetingroom.connector.exception.FlexOfficeInternalServerException;
import com.orange.meetingroom.connector.exception.MeetingRoomInternalServerException;
import com.orange.meetingroom.connector.exception.MethodNotAllowedException;
import com.orange.meetingroom.connector.exception.PhpInternalServerException;
import com.orange.meetingroom.connector.php.model.request.GetAgentBookingsParameters;
import com.orange.meetingroom.connector.php.model.request.GetDashboardBookingsParameters;
import com.orange.meetingroom.connector.php.model.request.SetBookingParameters;
import com.orange.meetingroom.connector.php.model.request.UpdateBookingParameters;
import com.orange.meetingroom.connector.php.model.response.Booking;
import com.orange.meetingroom.connector.php.model.response.BookingSummary;
import com.orange.meetingroom.connector.php.model.response.MeetingRoom;
import com.orange.meetingroom.connector.php.model.response.MeetingRoomBookings;
import com.orange.meetingroom.connector.php.model.response.MeetingRoomDetails;
import com.orange.meetingroom.connector.php.model.response.MeetingRooms;
import com.orange.meetingroom.connector.php.utils.DataTools;

/**
 * PhpConnectorClient
 * @author oab
 *
 */
public class PhpConnectorClient {
	
	private static final Logger LOGGER = Logger.getLogger(PhpConnectorClient.class);
	
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
	
	@SuppressWarnings("unchecked")
	/**
	 * getBookingsFromAgent
	 * @param GetAgentBookingsParameters params
	 * @return MeetingRoomBookings
	 * @throws Exception
	 */
	public MeetingRoom getBookingsFromAgent(GetAgentBookingsParameters params) throws MeetingRoomInternalServerException, PhpInternalServerException, DataNotExistsException, MethodNotAllowedException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "Begin call getBookingsFromAgent(GetAgentBookingsParameters params) method");
		}
		MeetingRoom meetingroom = new MeetingRoom();
		MeetingRoomBookings meetingRoomBookings = new MeetingRoomBookings();
//		HttpClientBuilder builder = HttpClientBuilder.create();
//	    CloseableHttpClient httpClient = builder.build(); 
	    try	{
			//HttpGet getRequest = new HttpGet("http://192.168.103.193/services/GetBookings.php?format=json&RoomID=brehat.rennes@microsoft.cad.aql.fr&ForceUpdateCache=false&_=1461057699231");
			// TODO decoment String request = phpGetBookingsURL + "?" + dataTools.getAgentBookingsParametersToUrlEncode(params);
			//String request = "http://192.168.103.193:3000/getAgentBookingsKO1"; // TODO delete only for testing
			String request = "http://192.168.103.193:3000/getAgentBookingsKO3"; // TODO delete only for testing
			
			HttpGet getRequest = new HttpGet(request);
			
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
						
			// parse the JSON response
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> mp = mapper.readValue(apiOutput,new TypeReference<Map<String, Object>>() {});
			
			// get currentDate
			Integer currentDate = (Integer)((Map<String, Object>)mp.get("Infos")).get("CurrentDate");
			meetingroom.setCurrentDate(currentDate);
			
			Map<String, Map<String, Object>> roomLevelOneMap = (Map<String, Map<String, Object>>)mp.get("Rooms");
			for (Entry<String, Map<String, Object>> element : roomLevelOneMap.entrySet()) {
				try {
				Boolean errorFlag = (Boolean)element.getValue().get("ErrorFlag");
				String message = (String)element.getValue().get("Message");
				if (errorFlag) { // errorFlag = true
					LOGGER.error("errorFlag is true in Php Server return with message:" + message);
					throw new MethodNotAllowedException("errorFlag is true in Php Server return with message:" + message);
				}
				} catch (RuntimeException e ) { // {"Infos":{"CurrentDate":1461938475},"Rooms":{"toto":false}}
					LOGGER.error("meetingRoomExternalId is not exist or Connexion to Exchange Server is impossible");
					throw new DataNotExistsException("meetingRoomExternalId is not exist or Connexion to Exchange Server is impossible");
				}

			}
				
				
			
			Map<String, Map<String, Map<String, Object>>> roomMap = (Map<String, Map<String, Map<String, Object>>>)mp.get("Rooms");
			
			for (Entry<String, Map<String, Map<String, Object>>> element : roomMap.entrySet()) {
					
					MeetingRoomDetails details = new MeetingRoomDetails(); 
					String meetingRoomExternalId = (String)element.getValue().get("RoomDetails").get("RoomID");
					String meetingRoomExternalName = (String)element.getValue().get("RoomDetails").get("RoomName");
					String meetingRoomExternalLocation = (String)element.getValue().get("RoomDetails").get("RoomLocation");
					
					details.setMeetingRoomExternalId(meetingRoomExternalId);
					details.setMeetingRoomExternalName(meetingRoomExternalName);
					details.setMeetingRoomExternalLocation(meetingRoomExternalLocation);
					
					meetingRoomBookings.setMeetingRoomDetails(details);
					meetingroom.setMeetingRoom(meetingRoomBookings);
			}
			
			Map<String, Map<String, Map<String, Map<String, Object>>>> roomMapBookings = (Map<String, Map<String, Map<String, Map<String, Object>>>>)mp.get("Rooms");
			
			for (Entry<String, Map<String, Map<String, Map<String, Object>>>> elementBooking : roomMapBookings.entrySet()) {
				
				try {
					Map<String, Map<String, Object>> bookings =  (Map<String, Map<String, Object>>)elementBooking.getValue().get("Bookings");
				
					for (Entry<String, Map<String, Object>> book : bookings.entrySet()) {
						Booking booking = new Booking();
						String idReservation = (String)book.getValue().get("IDReservation");
						String revisionReservation = (String)book.getValue().get("RevisionReservation");
						String organizer = (String)book.getValue().get("Organizer");
						String organizeFullName = (String)book.getValue().get("OrganizerFullName");
						String organizerMail = (String)book.getValue().get("OrganizerEmail");
						String creator = (String)book.getValue().get("Creator");
						String creatorFullName = (String)book.getValue().get("CreatorFullName");
						String creatorEmail = (String)book.getValue().get("CreatorEmail");
						String subject = (String)book.getValue().get("Subject");
						Integer startDate = (Integer)book.getValue().get("StartDate");
						Integer endDate = (Integer)book.getValue().get("EndDate");
						Boolean acknowledged = (Boolean)book.getValue().get("Acknowledged");
					
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
				}
				
			}			
			
			return meetingroom;
			
		} catch (ClientProtocolException ex) {
			LOGGER.error("Error in httpClient.execute() method, with message: " + ex.getMessage());
			throw new MeetingRoomInternalServerException("Error in httpClient.execute() method, with message: " + ex.getMessage());
		} catch (IOException e) {
			LOGGER.error("Error in EntityUtils.toString() method, with message: " + e.getMessage());
			throw new MeetingRoomInternalServerException("Error in EntityUtils.toString() method, with message: " + e.getMessage());
	
		} finally	{
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug( "End call getBookingsFromAgent(GetAgentBookingsParameters params) method");
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
	public MeetingRooms getBookingsFromDashboard(GetDashboardBookingsParameters params) throws Exception {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "Begin call getBookingsFromDashboard(GetDashboardBookingsParameters params) method");
		}
		MeetingRooms meetingrooms = new MeetingRooms();
		List<MeetingRoomBookings> meetingRoomBookingsList = new ArrayList<MeetingRoomBookings>(); 
//		HttpClientBuilder builder = HttpClientBuilder.create();
//	    CloseableHttpClient httpClient = builder.build();
		try	{
			//HttpGet getRequest = new HttpGet("http://192.168.103.193/services/GetBookings.php?format=json&MaxBookings=2&StartDate=0&RoomGroupID=rg_oab_full&_=1461061105469");
			String request = phpGetBookingsURL + "?" + dataTools.getDashboardBookingsParametersToUrlEncode(params);
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
			
			// parse the JSON response
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> mp = mapper.readValue(apiOutput,new TypeReference<Map<String, Object>>() {});
			
			// get currentDate
			Integer currentDate = (Integer)((Map<String, Object>)mp.get("Infos")).get("CurrentDate");
			meetingrooms.setCurrentDate(currentDate);
			
			Map<String, Map<String, Map<String, Object>>> roomMap = (Map<String, Map<String, Map<String, Object>>>)mp.get("Rooms");
			for (Entry<String, Map<String, Map<String, Object>>> element : roomMap.entrySet()) {
				MeetingRoomBookings meetingRoomBookings = new MeetingRoomBookings();
				MeetingRoomDetails details = new MeetingRoomDetails(); 
				String meetingRoomExternalId = (String)element.getValue().get("RoomDetails").get("RoomID");
				String meetingRoomExternalName = (String)element.getValue().get("RoomDetails").get("RoomName");
				String meetingRoomExternalLocation = (String)element.getValue().get("RoomDetails").get("RoomLocation");
				details.setMeetingRoomExternalId(meetingRoomExternalId);
				details.setMeetingRoomExternalName(meetingRoomExternalName);
				details.setMeetingRoomExternalLocation(meetingRoomExternalLocation);
				meetingRoomBookings.setMeetingRoomDetails(details);
				meetingRoomBookingsList.add(meetingRoomBookings);
			}
			
			Map<String, Map<String, Map<String, Map<String, Object>>>> roomMapBookings = (Map<String, Map<String, Map<String, Map<String, Object>>>>)mp.get("Rooms");
			int incr = 0; // get data from meetingRoomBookingsList for update bookings in the correct meetingRoomBookings object
			for (Entry<String, Map<String, Map<String, Map<String, Object>>>> elementBooking : roomMapBookings.entrySet()) {
				MeetingRoomBookings meetingRoomBookings = meetingRoomBookingsList.get(incr);
				incr = incr + 1;
				try {
					Map<String, Map<String, Object>> bookings =  (Map<String, Map<String, Object>>)elementBooking.getValue().get("Bookings");
					for (Entry<String, Map<String, Object>> book : bookings.entrySet()) {
						Booking booking = new Booking();
						String idReservation = (String)book.getValue().get("IDReservation");
						String revisionReservation = (String)book.getValue().get("RevisionReservation");
						String organizer = (String)book.getValue().get("Organizer");
						String organizeFullName = (String)book.getValue().get("OrganizerFullName");
						String organizerMail = (String)book.getValue().get("OrganizerEmail");
						String creator = (String)book.getValue().get("Creator");
						String creatorFullName = (String)book.getValue().get("CreatorFullName");
						String creatorEmail = (String)book.getValue().get("CreatorEmail");
						String subject = (String)book.getValue().get("Subject");
						Integer startDate = (Integer)book.getValue().get("StartDate");
						Integer endDate = (Integer)book.getValue().get("EndDate");
						Boolean acknowledged = (Boolean)book.getValue().get("Acknowledged");
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
				}
			}
			
			meetingrooms.setMeetingRooms(meetingRoomBookingsList);
			return meetingrooms;
			
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug( "End call getBookingsFromDashboard(GetDashboardBookingsParameters params) method");
			}
			//Important: Close the connect
			//httpClient.close();
		}
	}

	/**
	 * setBooking
	 * @param SetBookingParameters params
	 * @throws Exception
	 */
	public BookingSummary setBooking(SetBookingParameters params) throws Exception {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "Begin call setBooking(SetBookingParameters params) method");
		}
		BookingSummary bookingSummary = new BookingSummary();
//		HttpClientBuilder builder = HttpClientBuilder.create();
//	    CloseableHttpClient httpClient = builder.build();
		// construct writer using SetBookingParameters
		// String writer = "RoomID=brehat.rennes@microsoft.cad.aql.fr&OrganizerFullName=&Subject=&format=json&StartDate=1461060000&EndDate=1461060600&Acknowledged=1";
		String writer = dataTools.setBookingParametersToUrlEncode(params); 
		try	{
			//Define a postRequest request
			//HttpPost postRequest = new HttpPost("http://192.168.103.193/services/SetBooking.php");
			HttpPost postRequest = new HttpPost(phpSetBookingsURL);
			
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
				throw new RuntimeException("Failed with HTTP error code : " + statusCode);
			}
			
			//Now pull back the response object
			HttpEntity httpEntity = response.getEntity();
			String apiOutput = EntityUtils.toString(httpEntity);
						
			// parse the JSON response
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> mp = mapper.readValue(apiOutput,new TypeReference<Map<String, Object>>() {});
			Boolean errorFlag = (Boolean)mp.get("ErrorFlag");
			if (errorFlag) {
				String errorMessage = (String)mp.get("Message");
				throw new PhpInternalServerException(errorMessage);
			} else {
				String idReservation = (String)mp.get("IDReservation");
				String revisionReservation = (String)mp.get("RevisionReservation");
				bookingSummary.setIdReservation(idReservation);
				bookingSummary.setRevisionReservation(revisionReservation);
			}
			
			return bookingSummary;
		}
		finally	{
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug( "End call setBooking(SetBookingParameters params) method");
			}
			//Important: Close the connect
			//httpClient.close();
		}
	}
	
	/**
	 * updateBooking
	 * @param UpdateBookingParameters params
	 * @throws Exception
	 */
	public BookingSummary updateBooking(UpdateBookingParameters params) throws Exception {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "Begin call updateBooking(UpdateBookingParameters params) method");
		}
		BookingSummary bookingSummary = new BookingSummary();
//		HttpClientBuilder builder = HttpClientBuilder.create();
//	    CloseableHttpClient httpClient = builder.build();

		// construct the writer from UpdateBookingParameters
		// String writer = "RoomID=brehat.rennes@microsoft.cad.aql.fr&IDReservation=AAAiAGJyZWhhdC5yZW5uZXNAbWljcm9zb2Z0LmNhZC5hcWwuZnIARgAAAAAAJjiq1ulLK0Kj6vNsTnRuywcAQopQvd4yGUaRbVXWgALbzwAAAAfOdQAAQopQvd4yGUaRbVXWgALbzwAAkZg7ggAA&RevisionReservation=DwAAABYAAABCilC93jIZRpFtVdaAAtvPAACRmK21&EndDate=1461060745&format=json";
		String writer = dataTools.updateBookingParametersToUrlEncode(params);
		try	{
			// Define a postRequest request
			// HttpPost postRequest = new HttpPost("http://192.168.103.193/services/UpdateBooking.php");
			HttpPost postRequest = new HttpPost(phpUpdateBookingsURL);
			
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
				throw new RuntimeException("Failed with HTTP error code : " + statusCode);
			}
			
			//Now pull back the response object
			HttpEntity httpEntity = response.getEntity();
			String apiOutput = EntityUtils.toString(httpEntity);
						
			// parse the JSON response
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> mp = mapper.readValue(apiOutput,new TypeReference<Map<String, Object>>() {});
			Boolean errorFlag = (Boolean)mp.get("ErrorFlag");
			if (errorFlag) {
				String errorMessage = (String)mp.get("Message");
				throw new PhpInternalServerException(errorMessage);
			} else {
				String idReservation = (String)mp.get("IDReservation");
				String revisionReservation = (String)mp.get("RevisionReservation");
				bookingSummary.setIdReservation(idReservation);
				bookingSummary.setRevisionReservation(revisionReservation);
			}
			
			return bookingSummary;
		}
		finally	{
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug( "End call updateBooking(UpdateBookingParameters params) method");
			}
			//Important: Close the connect
			//httpClient.close();
		}
	}
	
}
