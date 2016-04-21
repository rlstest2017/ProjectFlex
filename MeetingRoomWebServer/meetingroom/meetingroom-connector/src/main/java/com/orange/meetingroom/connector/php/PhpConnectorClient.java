package com.orange.meetingroom.connector.php;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.meetingroom.connector.php.model.GetAgentBookingsParameters;
import com.orange.meetingroom.connector.php.model.GetDashboardBookingsParameters;
import com.orange.meetingroom.connector.php.model.SetBookingParameters;
import com.orange.meetingroom.connector.php.model.UpdateBookingParameters;
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
	private DefaultHttpClient httpClient;
	@Autowired
	private DataTools dataTools;
	@Autowired
	private String phpGetBookingsURL;
	@Autowired
	private String phpSetBookingsURL;
	@Autowired
	private String phpUpdateBookingsURL;
	
	//**************************************************************************
    //************************* METHODS  ***************************************
    //**************************************************************************
	
	@SuppressWarnings("unchecked")
	public void getBookingsFromAgent(GetAgentBookingsParameters params) throws Exception 
	{
		try
		{
			//HttpGet getRequest = new HttpGet("http://192.168.103.193/services/GetBookings.php?format=json&RoomID=brehat.rennes@microsoft.cad.aql.fr&ForceUpdateCache=false&_=1461057699231");
			String request = phpGetBookingsURL + "?" + dataTools.getAgentBookingsParametersToUrlEncode(params);
			HttpGet getRequest = new HttpGet(request);
			
			//Set the API media type in http accept header
			getRequest.addHeader("accept", "application/json");
			 
			//Send the request; It will immediately return the response in HttpResponse object
			HttpResponse response = httpClient.execute(getRequest);
			
			//verify the valid error code first
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) 
			{
				throw new RuntimeException("Failed with HTTP error code : " + statusCode);
			}
			
			//Now pull back the response object
			HttpEntity httpEntity = response.getEntity();
			String apiOutput = EntityUtils.toString(httpEntity);
			
			//Lets see what we got from API
			//System.out.println(apiOutput); 
						
			// parse the JSON response
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> mp = mapper.readValue(apiOutput,new TypeReference<Map<String, Object>>() {});
			
			// TODO get currentDate
			
			Map<String, Map<String, Map<String, Map<String, Object>>>> roomsMap = (Map<String, Map<String, Map<String, Map<String, Object>>>>)mp.get("Rooms");
			
			for (Entry<String, Map<String, Map<String, Map<String, Object>>>> element : roomsMap.entrySet()) {
				//System.out.println("key is :" + element.getKey());
				System.out.println("RoomName is :" + element.getValue().get("RoomDetails").get("RoomName"));
				System.out.println("RoomID is :" + element.getValue().get("RoomDetails").get("RoomID"));
				System.out.println("RoomLocation is :" + element.getValue().get("RoomDetails").get("RoomLocation"));
				
				Map<String, Map<String, Object>> bookings =  (Map<String, Map<String, Object>>)element.getValue().get("Bookings");
				
				for (Entry<String, Map<String, Object>> book : bookings.entrySet()) {
					System.out.println("*** IDReservation is :" + book.getValue().get("IDReservation"));
					System.out.println("*** RevisionReservation is :" + book.getValue().get("RevisionReservation"));
					System.out.println("*** Organizer is :" + book.getValue().get("Organizer"));
					System.out.println("*** OrganizerFullName is :" + book.getValue().get("OrganizerFullName"));
					System.out.println("*** OrganizerEmail is :" + book.getValue().get("OrganizerEmail"));
					System.out.println("*** Creator is :" + book.getValue().get("Creator"));
					System.out.println("*** CreatorFullName is :" + book.getValue().get("CreatorFullName"));
					System.out.println("*** CreatorEmail is :" + book.getValue().get("CreatorEmail"));
					System.out.println("*** Subject is :" + book.getValue().get("Subject"));
					System.out.println("*** StartDate is :" + book.getValue().get("StartDate"));
					System.out.println("*** EndDate is :" + book.getValue().get("EndDate"));
				}
				
			}
		}
		finally
		{
			//Important: Close the connect
			httpClient.getConnectionManager().shutdown();
		}
	}

	@SuppressWarnings("unchecked")
	public void getBookingsFromDashboard(GetDashboardBookingsParameters params) throws Exception 
	{
		try
		{
			//HttpGet getRequest = new HttpGet("http://192.168.103.193/services/GetBookings.php?format=json&MaxBookings=2&StartDate=0&RoomGroupID=rg_oab_full&_=1461061105469");
			String request = phpGetBookingsURL + "?" + dataTools.getDashboardBookingsParametersToUrlEncode(params);
			HttpGet getRequest = new HttpGet(request);
			
			//Set the API media type in http accept header
			getRequest.addHeader("accept", "application/json");
			 
			//Send the request; It will immediately return the response in HttpResponse object
			HttpResponse response = httpClient.execute(getRequest);
			
			//verify the valid error code first
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) 
			{
				throw new RuntimeException("Failed with HTTP error code : " + statusCode);
			}
			
			//Now pull back the response object
			HttpEntity httpEntity = response.getEntity();
			String apiOutput = EntityUtils.toString(httpEntity);
			
			// Lets see what we got from API
			// System.out.println(apiOutput); 
						
			// parse the JSON response
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> mp = mapper.readValue(apiOutput,new TypeReference<Map<String, Object>>() {});
			
			// TODO get currentDate
			
			Map<String, Map<String, Map<String, Map<String, Object>>>> roomsMap = (Map<String, Map<String, Map<String, Map<String, Object>>>>)mp.get("Rooms");
			
			for (Entry<String, Map<String, Map<String, Map<String, Object>>>> element : roomsMap.entrySet()) {
				//System.out.println("key is :" + element.getKey());
				System.out.println("RoomName is :" + element.getValue().get("RoomDetails").get("RoomName"));
				System.out.println("RoomID is :" + element.getValue().get("RoomDetails").get("RoomID"));
				System.out.println("RoomLocation is :" + element.getValue().get("RoomDetails").get("RoomLocation"));
				try {
				Map<String, Map<String, Object>> bookings =  (Map<String, Map<String, Object>>)element.getValue().get("Bookings");
				
				
				for (Entry<String, Map<String, Object>> book : bookings.entrySet()) {
					System.out.println("*** IDReservation is :" + book.getValue().get("IDReservation"));
					System.out.println("*** RevisionReservation is :" + book.getValue().get("RevisionReservation"));
					System.out.println("*** Organizer is :" + book.getValue().get("Organizer"));
					System.out.println("*** OrganizerFullName is :" + book.getValue().get("OrganizerFullName"));
					System.out.println("*** OrganizerEmail is :" + book.getValue().get("OrganizerEmail"));
					System.out.println("*** Creator is :" + book.getValue().get("Creator"));
					System.out.println("*** CreatorFullName is :" + book.getValue().get("CreatorFullName"));
					System.out.println("*** CreatorEmail is :" + book.getValue().get("CreatorEmail"));
					System.out.println("*** Subject is :" + book.getValue().get("Subject"));
					System.out.println("*** StartDate is :" + book.getValue().get("StartDate"));
					System.out.println("*** EndDate is :" + book.getValue().get("EndDate"));
				}
				} catch (java.lang.ClassCastException e) {
					// if not bookings, PHP returns ( "Bookings": []) witch produce this exception
				}
				
			}

		}
		finally
		{
			//Important: Close the connect
			httpClient.getConnectionManager().shutdown();
		}
	}

	public void setBooking(SetBookingParameters params) throws Exception 
	{
		
		// construct writer using SetBookingParameters
		// String writer = "RoomID=brehat.rennes@microsoft.cad.aql.fr&OrganizerFullName=&Subject=&format=json&StartDate=1461060000&EndDate=1461060600&Acknowledged=1";
		String writer = dataTools.setBookingParametersToUrlEncode(params); 
				
		try
		{
			//Define a postRequest request
			//HttpPost postRequest = new HttpPost("http://192.168.103.193/services/SetBooking.php");
			HttpPost postRequest = new HttpPost(phpSetBookingsURL);
			
			//Set the API media type in http content-type header
			postRequest.addHeader("content-type", "application/x-www-form-urlencoded");
			
			//Set the request post body
			StringEntity userEntity = new StringEntity(writer);
			postRequest.setEntity(userEntity);
			 
			//Send the request; It will immediately return the response in HttpResponse object if any
			HttpResponse response = httpClient.execute(postRequest);
			
			//verify the valid error code first
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) 
			{
				throw new RuntimeException("Failed with HTTP error code : " + statusCode);
			}
		}
		finally
		{
			//Important: Close the connect
			httpClient.getConnectionManager().shutdown();
		}
	}
	
	public void updateBooking(UpdateBookingParameters params) throws Exception  
	{
		// construct the writer from UpdateBookingParameters
		// String writer = "RoomID=brehat.rennes@microsoft.cad.aql.fr&IDReservation=AAAiAGJyZWhhdC5yZW5uZXNAbWljcm9zb2Z0LmNhZC5hcWwuZnIARgAAAAAAJjiq1ulLK0Kj6vNsTnRuywcAQopQvd4yGUaRbVXWgALbzwAAAAfOdQAAQopQvd4yGUaRbVXWgALbzwAAkZg7ggAA&RevisionReservation=DwAAABYAAABCilC93jIZRpFtVdaAAtvPAACRmK21&EndDate=1461060745&format=json";
		String writer = dataTools.updateBookingParametersToUrlEncode(params);
				
		try
		{
			// Define a postRequest request
			// HttpPost postRequest = new HttpPost("http://192.168.103.193/services/UpdateBooking.php");
			HttpPost postRequest = new HttpPost(phpUpdateBookingsURL);
			
			
			//Set the API media type in http content-type header
			postRequest.addHeader("content-type", "application/x-www-form-urlencoded");
			
			//Set the request post body
			StringEntity userEntity = new StringEntity(writer);
			postRequest.setEntity(userEntity);
			 
			//Send the request; It will immediately return the response in HttpResponse object if any
			HttpResponse response = httpClient.execute(postRequest);
			
			//verify the valid error code first
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) 
			{
				throw new RuntimeException("Failed with HTTP error code : " + statusCode);
			}
		}
		finally
		{
			//Important: Close the connect
			httpClient.getConnectionManager().shutdown();
		}
	}
	
	/**
	 * used only for tests
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		// Get request
		// getBookingsFromAgent();

		// Get request
		// getBookingsFromDashboard();

		// Post request
	    // setBooking();
		
		// Post request
		// updateBooking();
	}
	
}
