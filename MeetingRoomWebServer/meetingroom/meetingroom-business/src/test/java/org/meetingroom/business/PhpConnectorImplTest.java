package org.meetingroom.business;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;

import com.orange.meetingroom.business.connector.PhpConnectorManager;
import com.orange.meetingroom.business.connector.impl.PhpConnectorManagerImpl;
import com.orange.meetingroom.connector.php.model.request.GetAgentBookingsParameters;
import com.orange.meetingroom.connector.php.model.request.GetDashboardBookingsParameters;
import com.orange.meetingroom.connector.php.model.request.SetBookingParameters;
import com.orange.meetingroom.connector.php.model.request.UpdateBookingParameters;
import com.orange.meetingroom.connector.php.model.response.BookingSummary;
import com.orange.meetingroom.connector.php.model.response.MeetingRoomConnectorReturn;
import com.orange.meetingroom.connector.php.model.response.MeetingRoomsConnectorReturn;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PhpConnectorImplTest {

	static {
	    try {
	      Log4jConfigurer.initLogging( "classpath:log4j-meetingroom-business-test.xml" );
	    }
	    catch( FileNotFoundException ex ) {
	      System.err.println( "Cannot Initialize log4j" );
	    }
	  }

	private static final Logger LOGGER = Logger.getLogger(PhpConnectorImplTest.class);
	private static ClassPathXmlApplicationContext context;
	private static PhpConnectorManager phpBusinessConnector;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void initSpringContextAndDatabase() throws Exception {
		context = new ClassPathXmlApplicationContext("classpath*:applicationContext-meetingroom-business-test.xml");
		phpBusinessConnector = (PhpConnectorManagerImpl)context.getBean("phpBusinessConnector");
	}


	//@Test
	public void TestA_phpGetAgentBookings() {
		// SetUp
		boolean expectedResult = false;
		GetAgentBookingsParameters params = new GetAgentBookingsParameters();
		params.setFormat("json");
		params.setRoomID("brehat.rennes@microsoft.cad.aql.fr");
		params.setForceUpdateCache("false");
		try {
			MeetingRoomConnectorReturn meetingroom = phpBusinessConnector.getBookingsFromAgent(params);
			String externalId = meetingroom.getMeetingRoom().getMeetingRoomDetails().getMeetingRoomExternalId();
			// Asserts
			assertEquals("brehat.rennes@microsoft.cad.aql.fr", externalId);
			
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());
		}	
		// Asserts
		assertEquals(false, expectedResult);
	}

	//@Test
	public void TestAA_phpGetAgentBookingsDataNotExistException() {
		// SetUp
		boolean expectedResult = false;
		GetAgentBookingsParameters params = new GetAgentBookingsParameters();
		params.setFormat("json");
		params.setRoomID("toto");
		params.setForceUpdateCache("false");
		try {
			phpBusinessConnector.getBookingsFromAgent(params);
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());
		}	
		// Asserts
		assertEquals(true, expectedResult);
	}

	//@Test
	public void TestAB_phpGetAgentBookingsMethodNotAllowedException() {
		// SetUp
		boolean expectedResult = false;
		GetAgentBookingsParameters params = new GetAgentBookingsParameters();
		params.setFormat("json");
		params.setRoomID("toto");
		params.setForceUpdateCache("false");
		try {
			phpBusinessConnector.getBookingsFromAgent(params);
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());
		}	
		// Asserts
		assertEquals(true, expectedResult);
	}
	
	//@Test
	public void TestB_phpGetDashboardBookings() {
		// SetUp
		boolean expectedResult = false;
		GetDashboardBookingsParameters params = new GetDashboardBookingsParameters();
		params.setFormat("json");
		params.setMaxBookings("2");
		params.setStartDate("0");
		params.setRoomGroupID("rg_oab_full");
		try {
			MeetingRoomsConnectorReturn meetingrooms = phpBusinessConnector.getBookingsFromDashboard(params);
			int size = meetingrooms.getMeetingRooms().size();
			// Asserts
			assertEquals(2, size);
			
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());		
		}	 
		// Asserts
		assertEquals(false, expectedResult);
	}

	//@Test
	public void TestBA_phpGetDashboardBookings_BAD_XML_FILE() {
		// SetUp
		boolean expectedResult = false;
		GetDashboardBookingsParameters params = new GetDashboardBookingsParameters();
		params.setFormat("json");
		params.setMaxBookings("2");
		params.setStartDate("0");
		params.setRoomGroupID("rg_oab_full_bad");
		try {
			phpBusinessConnector.getBookingsFromDashboard(params);
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());		
		}
		// Asserts
		assertEquals(true, expectedResult);
	}

	//@Test
	public void TestBB_phpGetDashboardBookings_BAD_ROOMID_IN_XML_FILE() {
		// SetUp
		boolean expectedResult = false;
		GetDashboardBookingsParameters params = new GetDashboardBookingsParameters();
		params.setFormat("json");
		params.setMaxBookings("2");
		params.setStartDate("0");
		params.setRoomGroupID("rg_oab_full_bad");
		try {
			phpBusinessConnector.getBookingsFromDashboard(params);
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());		
		}
		// Asserts
		assertEquals(true, expectedResult);
	}

	//@Test 
	public void TestC_phpSetBooking() {
		// SetUp
		boolean expectedResult = false;
		SetBookingParameters params = new SetBookingParameters();
		params.setRoomID("brehat.rennes@microsoft.cad.aql.fr");
		params.setOrganizerFullName("rachid test organisateur java");
		params.setSubject("rachid test sujet java");
		params.setStartDate("1462370400"); // 25/4/2016 à 14:45:00 
		params.setEndDate("1462372200"); // 25/4/2016 à 15:45:00 
		params.setFormat("json");
		params.setAcknowledged("0");
		
		try {
			BookingSummary booking = phpBusinessConnector.setBooking(params);
			String idReservation = booking.getIdReservation();
			// Asserts
			assertNotEquals(null , idReservation);
			
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());		
		}	
		// Asserts
		assertEquals(false, expectedResult);
	}
	
	//@Test 
	public void TestCA_phpSetBooking_TimeSlot_Error() {
		// SetUp
		boolean expectedResult = false;
		SetBookingParameters params = new SetBookingParameters();
		params.setRoomID("brehat.rennes@microsoft.cad.aql.fr");
		params.setOrganizerFullName("rachid test organisateur java");
		params.setSubject("rachid test sujet java");
		params.setStartDate("1461588300"); // 25/4/2016 à 14:45:00 
		params.setEndDate("1461591900"); // 25/4/2016 à 15:45:00 
		params.setFormat("json");
		params.setAcknowledged("0");
		
		try {
			phpBusinessConnector.setBooking(params);
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());		
		}	
		// Asserts
		assertEquals(true, expectedResult);
	}
	
	// Confirmer la réunion en cours
	//@Test 
	public void TestD_phpUpdateBooking_Confirmer() {
		// SetUp
		boolean expectedResult = false;
		UpdateBookingParameters params = new UpdateBookingParameters();
		params.setRoomID("brehat.rennes@microsoft.cad.aql.fr");
		params.setIdReservation("AAAiAGJyZWhhdC5yZW5uZXNAbWljcm9zb2Z0LmNhZC5hcWwuZnIARgAAAAAA4hbJIXdxqUekYUmwv7EwXgcA0rSto+je7kuOKCCGs7ckzgAAAKdqzQAA0rSto+je7kuOKCCGs7ckzgAAAKeSFQAA");
		params.setRevisionReservation("DwAAABYAAADStK2j6N7uS44oIIaztyTOAAAAp6Ku");
		params.setStartDate("1462370700"); // 25/4/2016 à 11:22:06
		params.setFormat("json"); 
		params.setSubject("test RLS");
		params.setAcknowledged("1");
				
		try {
			BookingSummary booking = phpBusinessConnector.updateBooking(params);
			String idReservation = booking.getIdReservation();
			// Asserts
			assertNotEquals(null , idReservation);
			
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());
		}	
		// Asserts
		assertEquals(false, expectedResult);
	}
		
	// Fermer (annuler) la réunion en cours
	//@Test 
	public void TestE_phpUpdateBooking_Fermer() {
		// SetUp
		boolean expectedResult = false;
		UpdateBookingParameters params = new UpdateBookingParameters();
		params.setRoomID("brehat.rennes@microsoft.cad.aql.fr");
		params.setIdReservation("AAAiAGJyZWhhdC5yZW5uZXNAbWljcm9zb2Z0LmNhZC5hcWwuZnIARgAAAAAA4hbJIXdxqUekYUmwv7EwXgcA0rSto+je7kuOKCCGs7ckzgAAAKdqzQAA0rSto+je7kuOKCCGs7ckzgAAAKeSFwAA");
		params.setRevisionReservation("DwAAABYAAADStK2j6N7uS44oIIaztyTOAAAAp6Lb");
		params.setEndDate("1462371334"); // 04/05/2016 à 16:17:34
		params.setFormat("json"); 
				
		try {
			BookingSummary booking = phpBusinessConnector.updateBooking(params);
			String idReservation = booking.getIdReservation();
			// Asserts
			assertNotEquals(null , idReservation);
			
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());
		}
		// Asserts
		assertEquals(false, expectedResult);
	}
}
