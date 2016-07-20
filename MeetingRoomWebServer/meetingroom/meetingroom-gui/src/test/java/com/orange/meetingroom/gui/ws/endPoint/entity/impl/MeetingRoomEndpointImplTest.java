package com.orange.meetingroom.gui.ws.endPoint.entity.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.FileNotFoundException;
import java.math.BigInteger;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;

import com.orange.meetingroom.business.connector.utils.DateTools;
import com.orange.meetingroom.gui.ws.endPoint.entity.MeetingRoomEndpoint;
import com.orange.meetingroom.gui.ws.model.BookingSetInput;
import com.orange.meetingroom.gui.ws.model.BookingSetOutput;
import com.orange.meetingroom.gui.ws.model.BookingUpdateInput;
import com.orange.meetingroom.gui.ws.model.BookingUpdateOutput;
import com.orange.meetingroom.gui.ws.model.MeetingRoom;
import com.orange.meetingroom.gui.ws.model.MeetingRooms;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MeetingRoomEndpointImplTest {

	static {
	    try {
	      Log4jConfigurer.initLogging( "classpath:log4j-meetingroom-gui-test.xml" );
	    }
	    catch( FileNotFoundException ex ) {
	      System.err.println( "Cannot Initialize log4j" );
	    }
	  }

	private static final Logger LOGGER = Logger.getLogger(MeetingRoomEndpointImplTest.class);

	private static ClassPathXmlApplicationContext context;
	private static MeetingRoomEndpoint meetingRoomEndpoint;
	private static DateTools dateTools;
	@BeforeClass
	public static void initSpringContextAndDatabase() throws Exception {
		context = new ClassPathXmlApplicationContext("classpath*:applicationContext-meetingroom-gui-test.xml");
		meetingRoomEndpoint = (MeetingRoomEndpointImpl)context.getBean("meetingRoomEndpoint");
		dateTools = (DateTools)context.getBean("dateTools");
	}
	
	
	@Test
	public void TestA_CheckTimeOut() {
		// SetUp
		Boolean output = meetingRoomEndpoint.checkMeetingRoomsStatusTimeOutTestMethod();
		// Asserts
		assertEquals(true, output);
	}
	
	@Test
	public void TestA_GetMeetingRoom() {
		// SetUp
		boolean expectedResult = false;
		try {
			MeetingRoom output = meetingRoomEndpoint.getMeetingRoomBookings("[TEST]gardian.paris@microsoft.cad.aql.fr", false);
			
			// Asserts
			assertEquals("[TEST]gardian.paris@microsoft.cad.aql.fr", output.getRoom().getMeetingRoomDetails().getMeetingRoomExternalId());
			
			
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());
		}	
		// Asserts
		assertEquals(false, expectedResult);
	}

	@Test
	public void TestAA_GetMeetingRoom() { // "Subject": "prenom nom - Quick Booking" 
		// SetUp
		boolean expectedResult = false;
		try {
			MeetingRoom output = meetingRoomEndpoint.getMeetingRoomBookings("[TEST2]gardian.paris@microsoft.cad.aql.fr", false);
			
			// Asserts
			assertEquals("[TEST2]gardian.paris@microsoft.cad.aql.fr", output.getRoom().getMeetingRoomDetails().getMeetingRoomExternalId());
			
			
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());
		}	
		// Asserts
		assertEquals(false, expectedResult);
	}

	@Test
	public void TestAB_GetMeetingRoom() { // "Subject": "Booking Agent - Quick Booking" & "OrganizerFullName": "prenom nom"
		// SetUp
		boolean expectedResult = false;
		try {
			MeetingRoom output = meetingRoomEndpoint.getMeetingRoomBookings("[TEST3]gardian.paris@microsoft.cad.aql.fr", false);
			
			// Asserts
			assertEquals("[TEST3]gardian.paris@microsoft.cad.aql.fr", output.getRoom().getMeetingRoomDetails().getMeetingRoomExternalId());
			
			
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());
		}	
		// Asserts
		assertEquals(false, expectedResult);
	}
	
	@Test
	public void TestAC_GetMeetingRoom() { // "Subject": "Booking Agent - Quick Booking" & "OrganizerFullName": "delegate"
		// SetUp
		boolean expectedResult = false;
		try {
			MeetingRoom output = meetingRoomEndpoint.getMeetingRoomBookings("[TEST4]gardian.paris@microsoft.cad.aql.fr", false);
			
			// Asserts
			assertEquals("[TEST4]gardian.paris@microsoft.cad.aql.fr", output.getRoom().getMeetingRoomDetails().getMeetingRoomExternalId());
			
			
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());
		}	
		// Asserts
		assertEquals(false, expectedResult);
	}
	
	@Test
	public void TestAD_GetMeetingRoom() { // "Subject": "prenom1-prenom2 nom - Quick Booking"
		// SetUp
		boolean expectedResult = false;
		try {
			MeetingRoom output = meetingRoomEndpoint.getMeetingRoomBookings("[TEST5]gardian.paris@microsoft.cad.aql.fr", false);
			
			// Asserts
			assertEquals("[TEST5]gardian.paris@microsoft.cad.aql.fr", output.getRoom().getMeetingRoomDetails().getMeetingRoomExternalId());
			
			
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());
		}	
		// Asserts
		assertEquals(false, expectedResult);
	}
	
	@Test
	public void TestB() {
		// SetUp
		boolean expectedResult = false;
		try {
			MeetingRooms output = meetingRoomEndpoint.getBookings("rl:rl:rl:rl:rl:rl");
			// Asserts
			assertEquals("[TEST]gardian.paris@microsoft.cad.aql.fr", output.getRooms().get(0).getMeetingRoomDetails().getMeetingRoomExternalId());
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());
		}	
		// Asserts
		assertEquals(false, expectedResult);
	}
	
	@Test 
	public void TestC_phpSetBooking() {
		// SetUp
		boolean expectedResult = false;
		
		Integer HourStartBooking = dateTools.DayWithHour(10);
		Integer HourEndBooking = dateTools.DayWithHour(12);

		BookingSetInput params = new BookingSetInput();
		params.setOrganizerFullName("rachid test organisateur java");
		params.setSubject("rachid test sujet java");
		params.setStartDate(BigInteger.valueOf(HourStartBooking));  
		params.setEndDate(BigInteger.valueOf(HourEndBooking));  
		
		try {
			BookingSetOutput booking = meetingRoomEndpoint.setBooking("[TEST]gardian.paris@microsoft.cad.aql.fr", params);
			String idReservation = booking.getIDReservation();
			// Asserts
			assertNotEquals(null , idReservation);
			
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());		
		}	
		// Asserts
		assertEquals(false, expectedResult);
	}
	
	@Test 
	public void TestC_phpSetBooking_KO_BeforeBeginDate() {
		// SetUp
		boolean expectedResult = false;
		
		Integer HourStartBooking = dateTools.DayWithHour(4);
		Integer HourEndBooking = dateTools.DayWithHour(14);
		
		BookingSetInput params = new BookingSetInput();
		params.setOrganizerFullName("rachid test organisateur java");
		params.setSubject("rachid test sujet java");
		params.setStartDate(BigInteger.valueOf(HourStartBooking));  
		params.setEndDate(BigInteger.valueOf(HourEndBooking));  
		
		try {
			meetingRoomEndpoint.setBooking("[TEST]gardian.paris@microsoft.cad.aql.fr", params);
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());		
		}	
		// Asserts
		assertEquals(true, expectedResult);
	}
	
	@Test 
	public void TestC_phpSetBooking_KO_AfterEndDate() {
		// SetUp
		boolean expectedResult = false;
		
		Integer HourStartBooking = dateTools.DayWithHour(14);
		Integer HourEndBooking = dateTools.DayWithHour(23);
		
		BookingSetInput params = new BookingSetInput();
		params.setOrganizerFullName("rachid test organisateur java");
		params.setSubject("rachid test sujet java");
		params.setStartDate(BigInteger.valueOf(HourStartBooking));  
		params.setEndDate(BigInteger.valueOf(HourEndBooking));  
		
		try {
			meetingRoomEndpoint.setBooking("[TEST]gardian.paris@microsoft.cad.aql.fr", params);
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());		
		}	
		// Asserts
		assertEquals(true, expectedResult);
	}
	
	@Test 
	public void TestC_phpUpdateBooking_Confirmer() {
		// SetUp
		boolean expectedResult = false;
		BookingUpdateInput params = new BookingUpdateInput();
		params.setIDReservation("AAAiAGJyZWhhdC5yZW5uZXNAbWljcm9zb2Z0LmNhZC5hcWwuZnIARgAAAAAA4hbJIXdxqUekYUmwv7EwXgcA0rSto+je7kuOKCCGs7ckzgAAAKdqzQAA0rSto+je7kuOKCCGs7ckzgAAAKeSFQAA");
		params.setRevisionReservation("DwAAABYAAADStK2j6N7uS44oIIaztyTOAAAAp6Ku");
		params.setSubject("test RLS");
				
		try {
			BookingUpdateOutput booking = meetingRoomEndpoint.confirmBooking("[TEST]gardian.paris@microsoft.cad.aql.fr", params);
			String idReservation = booking.getIDReservation();
			// Asserts
			assertNotEquals(null , idReservation);
			
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());
		}	
		// Asserts
		assertEquals(false, expectedResult);
	}
	
	@Test 
	public void TestC_phpUpdateBooking_Annuler() {
		// SetUp
		boolean expectedResult = false;
		BookingUpdateInput params = new BookingUpdateInput();
		params.setIDReservation("AAAiAGJyZWhhdC5yZW5uZXNAbWljcm9zb2Z0LmNhZC5hcWwuZnIARgAAAAAA4hbJIXdxqUekYUmwv7EwXgcA0rSto+je7kuOKCCGs7ckzgAAAKdqzQAA0rSto+je7kuOKCCGs7ckzgAAAKeSFQAA");
		params.setRevisionReservation("DwAAABYAAADStK2j6N7uS44oIIaztyTOAAAAp6Ku");
		params.setSubject("test RLS");
				
		try {
			BookingUpdateOutput booking = meetingRoomEndpoint.cancelBooking("[TEST]gardian.paris@microsoft.cad.aql.fr", params);
			String idReservation = booking.getIDReservation();
			// Asserts
			assertNotEquals(null , idReservation);
			
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());
		}	
		// Asserts
		assertEquals(false, expectedResult);
	}
	
	
	@Test
	public void TestE_OK() {
		// SetUp
		boolean expectedResult = false;
		try {
			MeetingRoom output = meetingRoomEndpoint.getMeetingRoomBookings("test0@microsoft.cad.aql.fr", false);
			
			// Asserts
			assertEquals("[TEST]gardian.paris@microsoft.cad.aql.fr", output.getRoom().getMeetingRoomDetails().getMeetingRoomExternalId());
			
			
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());
		}	
		// Asserts
		assertEquals(false, expectedResult);
	}
	
	@Test
	public void TestE_KO1() {
		// SetUp
		boolean expectedResult = false;
		try {
			meetingRoomEndpoint.getMeetingRoomBookings("test1@microsoft.cad.aql.fr", false);
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());
		}	
		// Asserts
		assertEquals(true, expectedResult);
	}

	@Test
	public void TestE_KO2() {
		// SetUp
		boolean expectedResult = false;
		try {
			meetingRoomEndpoint.getMeetingRoomBookings("test2@microsoft.cad.aql.fr", false);
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());
		}	
		// Asserts
		assertEquals(true, expectedResult);
	}

	@Test
	public void TestE_KO3() {
		// SetUp
		boolean expectedResult = false;
		try {
			meetingRoomEndpoint.getMeetingRoomBookings("test3@microsoft.cad.aql.fr", false);
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());
		}	
		// Asserts
		assertEquals(true, expectedResult);
	}


}
