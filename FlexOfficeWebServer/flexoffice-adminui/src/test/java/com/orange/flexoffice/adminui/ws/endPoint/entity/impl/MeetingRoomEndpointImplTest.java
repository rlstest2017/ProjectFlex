package com.orange.flexoffice.adminui.ws.endPoint.entity.impl;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.math.BigInteger;
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

import com.orange.flexoffice.adminui.ws.endPoint.entity.AgentEndpoint;
import com.orange.flexoffice.adminui.ws.endPoint.entity.MeetingRoomEndpoint;
import com.orange.flexoffice.adminui.ws.model.AgentInput3;
import com.orange.flexoffice.adminui.ws.model.EMeetingroomType;
import com.orange.flexoffice.adminui.ws.model.MeetingRoom;
import com.orange.flexoffice.adminui.ws.model.MeetingRoomInput;
import com.orange.flexoffice.adminui.ws.model.MeetingRoomSummary;
import com.orange.flexoffice.adminui.ws.task.AdminUiTasks;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.dao.common.model.data.MeetingRoomDao;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MeetingRoomEndpointImplTest {

	static {
	    try {
	      Log4jConfigurer.initLogging( "classpath:log4j-flexoffice-adminui-test.xml" );
	    }
	    catch( FileNotFoundException ex ) {
	      System.err.println( "Cannot Initialize log4j" );
	    }
	  }

	private static ClassPathXmlApplicationContext context;

	private static MeetingRoomEndpoint meetingroomEndpoint;

	private static AgentEndpoint agentEndpoint;

	private static AdminUiTasks adminUiTasks;
	
	@Context
	private UriInfo uriInfo;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void initSpringContextAndDatabase() throws Exception {
		context = new ClassPathXmlApplicationContext("applicationContext-flexoffice-adminui-test.xml");
		meetingroomEndpoint = (MeetingRoomEndpointImpl)context.getBean("meetingroomEndpoint");
		agentEndpoint = (AgentEndpointImpl)context.getBean("agentEndpoint");
		adminUiTasks = (AdminUiTasks)context.getBean("adminUiTasks");
	}


	@Test
	public void TestA_initTables() {
		// SetUp
		boolean state = agentEndpoint.executeInitTestFile();

		// Asserts
		assertEquals(true, state);
	}


	@Test
	public void TestB_getMeetingRooms() {
		// Test
		List<MeetingRoomSummary> meetingrooms = meetingroomEndpoint.getMeetingRooms();

		// Asserts
		assertEquals(5, meetingrooms.size());
	}

	@Test
	public void TestC_getMeetingRoomByRoomId1() {

		// Test
		MeetingRoom meetingroom = meetingroomEndpoint.getMeetingRoom("1");

		// Asserts
		assertEquals("1", meetingroom.getAgent().getId());
	}

	@Test
	public void TestD_getMeetingRoomByRoomId2() {

		// Test
		MeetingRoom meetingroom = meetingroomEndpoint.getMeetingRoom("2");

		// Asserts
		assertEquals("2", meetingroom.getAgent().getId());
	}



	@Test
	public void TestE_getWrongMeetingRoomDataNotExistsException() {
		// Setup
		boolean expectedResult = false;

		// Test
		try {
			// Test
			meetingroomEndpoint.getMeetingRoom("125");

		} catch (WebApplicationException e) {
			expectedResult = true;
		}

		// Asserts
		assertEquals(true, expectedResult);	
	}

	@Test
	public void TestF_addMeetingRoomAlreadyExists() {
		// Setup
		boolean expectedResult = false;
		final MeetingRoomInput meetingroomInput = new MeetingRoomInput();
		meetingroomInput.setName("MeetingRoomTest1");
		meetingroomInput.setBuildingId("1");
		meetingroomInput.setCapacity(BigInteger.valueOf(4));
		meetingroomInput.setDesc("MeetingRoomDescription1");
		meetingroomInput.setType(EMeetingroomType.BOX);
		meetingroomInput.setExternalId("ext@id");
		final AgentInput3 agent = new AgentInput3();
		agent.setMacAddress("FF:EE:ZZ:AA:GG:PP");
		meetingroomInput.setAgent(agent);

		try {
			// Test
			meetingroomEndpoint.addMeetingRoom(meetingroomInput);

		} catch (WebApplicationException e) {
			expectedResult = true;
		}
		// Asserts
		assertEquals(true, expectedResult);	
	}

	@Test
	public void TestG_updateMeetingRoom() throws WebApplicationException {

		// Setup
		boolean expectedResult = false;
		
		try {
			// Setup
			final MeetingRoomInput meetingroomInput = new MeetingRoomInput();
			meetingroomInput.setName("MeetingRoomTest1");
			meetingroomInput.setBuildingId("2");
			meetingroomInput.setFloor(BigInteger.valueOf(15));
			meetingroomInput.setCapacity(BigInteger.valueOf(4));
			meetingroomInput.setDesc("MeetingRoomDescription-modified");
			meetingroomInput.setType(EMeetingroomType.BOX);
			meetingroomInput.setExternalId("ext@id");
			final AgentInput3 agent = new AgentInput3();
			agent.setMacAddress("FF:EE:ZZ:AA:GG:PP");
			meetingroomInput.setAgent(agent);;
			
			MeetingRoomDao meetingroom = meetingroomEndpoint.findByName("meeting room 1");

			// Test
			final Response response = meetingroomEndpoint.updateMeetingRoom(meetingroom.getId().toString(), meetingroomInput);

			// Asserts
			assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());

			// Test
			List<MeetingRoomSummary> meetingrooms = meetingroomEndpoint.getMeetingRooms();

			// Asserts
			assertEquals(5, meetingrooms.size());
			expectedResult = true;
			
		} catch(DataNotExistsException e ) {
		}

		// Asserts
		assertEquals(true, expectedResult);	
	}
	


	@Test
	public void TestH_updateMeetingRoomDataNotExistsException() {
		// Setup
		boolean expectedResult = false;
		
		try {
			// Setup
			final MeetingRoomInput meetingroomInput = new MeetingRoomInput();
			meetingroomInput.setName("MeetingRoomTest1");
			meetingroomInput.setBuildingId("2");
			meetingroomInput.setFloor(BigInteger.valueOf(15));
			meetingroomInput.setCapacity(BigInteger.valueOf(4));
			meetingroomInput.setDesc("MeetingRoomDescription1");
			meetingroomInput.setType(EMeetingroomType.BOX);
			final AgentInput3 agent = new AgentInput3();
			agent.setMacAddress("FF:EE:ZZ:AA:GG:PP");
			meetingroomInput.setAgent(agent);;

			// Test
			String wrongId = "a1";
			meetingroomEndpoint.updateMeetingRoom(wrongId, meetingroomInput);

		} catch (WebApplicationException e) {
			expectedResult = true;
		}

		// Asserts
		assertEquals(true, expectedResult);	
	}
	
	
	@Test
	public void TestI_removeMeetingRoomDataNotExistsException() {
		// Setup
		boolean expectedResult = false;
		try {
			final MeetingRoomDao meetingroom = meetingroomEndpoint.findByName("MeetingRoomTest1");

			try {
				// Setup
				String wrongId = meetingroom.getColumnId() + "1";

				// Test
				meetingroomEndpoint.removeMeetingRoom(wrongId);

			} catch (WebApplicationException e) {
				expectedResult = true;
			}

		} catch(DataNotExistsException e ) {
		}

		// Asserts
		assertEquals(true, expectedResult);	
	}


	@Test
	public void TestJ_removeMeetingRoom() throws WebApplicationException {
		// Setup
		boolean expectedResult = false;
		try {
			final MeetingRoomDao meetingroom = meetingroomEndpoint.findByName("meeting room 5");


			// Test
			Response response = meetingroomEndpoint.removeMeetingRoom(meetingroom.getColumnId());

			// Assert
			assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
			expectedResult = true;

		} catch(DataNotExistsException e ) {
		}
		
		// Assert
		assertEquals(true, expectedResult);	
	}

	/*@Test
	public void TestN_initRoomStatsTable() {
		// SetUp
		boolean state = meetingroomEndpoint.initRoomStatsTable();

		// Asserts
		assertEquals(true, state);
	}

	@Test
	public void TestO_processDailyStatsTask() {
		// SetUp
		boolean state = adminUiTasks.processDailyStatsTestMethod();

		// Asserts
		assertEquals(true, state);
	}
	
	@Test
	public void TestP_purgeStatsTask() {
		// SetUp
		boolean state = adminUiTasks.purgeStatsDataTestMethod();

		// Asserts
		assertEquals(true, state);
	}
	
	@Test
	public void TestQ_updateOFFLINEGatewaysAlerts() {
		// SetUp
		boolean state = initOfflineGatewaysAlerts.updateOFFLINEGatewaysAlertsForTest();

		// Asserts
		assertEquals(true, state);
	}*/
	
}
