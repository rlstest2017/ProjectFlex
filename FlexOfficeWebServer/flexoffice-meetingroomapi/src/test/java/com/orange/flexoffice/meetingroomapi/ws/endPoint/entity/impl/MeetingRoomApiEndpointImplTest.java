package com.orange.flexoffice.meetingroomapi.ws.endPoint.entity.impl;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;

import com.orange.flexoffice.meetingroomapi.ws.endPoint.entity.MeetingRoomApiEndpoint;
import com.orange.flexoffice.meetingroomapi.ws.model.EMeetingRoomStatus;
import com.orange.flexoffice.meetingroomapi.ws.model.MeetingRoomInput;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MeetingRoomApiEndpointImplTest {

	static {
	    try {
	      Log4jConfigurer.initLogging( "classpath:log4j-flexoffice-meetingroomapi-test.xml" );
	    }
	    catch( FileNotFoundException ex ) {
	      System.err.println( "Cannot Initialize log4j" );
	    }
	}
	
	private static ClassPathXmlApplicationContext context;

	private static MeetingRoomApiEndpoint meetingRoomApiEndpoint;
	
	@Context
	private UriInfo uriInfo;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void initSpringContextAndDatabase() throws Exception {
		context = new ClassPathXmlApplicationContext("applicationContext-flexoffice-meetingroomapi-test.xml");
		meetingRoomApiEndpoint = (MeetingRoomApiEndpointImpl)context.getBean("meetingRoomEndpoint");
	}


	@Test
	public void TestA_initTables() {
		// SetUp
		boolean state = meetingRoomApiEndpoint.executeInitTestFile();
		 
		// Asserts
		assertEquals(true, state);
	}

	
	@Test
	public void TestB_UpdateData() {
		String externalId = "1@id.fr";
		MeetingRoomInput meetingRoomInput = new MeetingRoomInput();
		meetingRoomInput.setOrganizerLabel("JUNIT 1");
		meetingRoomInput.setMeetingRoomStatus(EMeetingRoomStatus.OCCUPIED);
		meetingRoomInput.setStartDate(Long.valueOf("1460637437721"));
		meetingRoomInput.setEndDate(Long.valueOf("1460637787721"));
		
		// Test
		Response response =	meetingRoomApiEndpoint.updateData(externalId, meetingRoomInput);
		
		// Assert
		assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void TestB_UpdateData2() {
		String externalId = "6@id.fr";
		MeetingRoomInput meetingRoomInput = new MeetingRoomInput();
		meetingRoomInput.setOrganizerLabel("JUNIT 1");
		meetingRoomInput.setMeetingRoomStatus(EMeetingRoomStatus.OCCUPIED);
		meetingRoomInput.setStartDate(Long.valueOf("1460637437721"));
		meetingRoomInput.setEndDate(Long.valueOf("1460637787721"));
		
		boolean dataNotExisting = false;
		
		try{
			// Test
			meetingRoomApiEndpoint.updateData(externalId, meetingRoomInput);
		} catch (WebApplicationException e){
			dataNotExisting = true;
		}
		
		
		// Assert
		assertEquals(true, dataNotExisting);
	}

	@Test
	public void TestC_GetTimeout() {
		
		// Test
		List<String> lst = meetingRoomApiEndpoint.getTimeout();
		
		
		// Assert
		assertEquals(1, lst.size());
	}
	
}
