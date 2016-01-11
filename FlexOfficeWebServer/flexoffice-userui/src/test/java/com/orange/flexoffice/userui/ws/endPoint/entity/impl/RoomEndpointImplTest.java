package com.orange.flexoffice.userui.ws.endPoint.entity.impl;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;

import com.orange.flexoffice.userui.ws.endPoint.entity.RoomEndpoint;
import com.orange.flexoffice.userui.ws.model.Room;
import com.orange.flexoffice.userui.ws.model.RoomSummary;
import com.orange.flexoffice.userui.ws.task.CheckReservationTimeOutTask;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RoomEndpointImplTest {

	static {
	    try {
	      Log4jConfigurer.initLogging( "classpath:log4j-flexoffice-userui-test.xml" );
	    }
	    catch( FileNotFoundException ex ) {
	      System.err.println( "Cannot Initialize log4j" );
	    }
	  }

	private static ClassPathXmlApplicationContext context;

	private static RoomEndpoint roomEndpoint;

	private static CheckReservationTimeOutTask checkReservationTimeOutTask;


	@Context
	private UriInfo uriInfo;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void initSpringContextAndDatabase() throws Exception {
		context = new ClassPathXmlApplicationContext("applicationContext-flexoffice-userui-test.xml");
		roomEndpoint = (RoomEndpointImpl)context.getBean("roomEndpoint");
		checkReservationTimeOutTask = (CheckReservationTimeOutTask)context.getBean("checkReservationTimeOutTask");
	}


	@Test
	public void TestA_initTables() {
		// SetUp
		boolean state = roomEndpoint.executeInitTestFile();

		// Asserts
		assertEquals(true, state);
	}


	@Test
	public void TestB_getRooms() {
		// SetUp
		String token = "Zmlyc3QubGFzdDVAdGVzdC5jb206dGVzdDoxNDQ4NTI5MDc2ODQ0";
		
		// Test
		List<RoomSummary> rooms = roomEndpoint.getRooms(token, false);

		// Asserts
		assertEquals(5, rooms.size());
	}

	@Test
	public void TestC_getLatestRooms() {
		// SetUp
		String token = "Zmlyc3QubGFzdDFAdGVzdC5jb206cGFzczoxNDQ4NjEzNjU2MDk4";
		
		// Test
		List<RoomSummary> rooms = roomEndpoint.getRooms(token, true);

		// Asserts
		assertEquals(3, rooms.size());
	}

	@Test
	public void TestD_getRoomByRoomId1() {

		// Test
		Room room = roomEndpoint.getRoom("1");

		// Asserts
		assertEquals("room 1", room.getName());
	}

	@Test
	public void TestE_getRoomByRoomId2() {

		// Test
		Room room = roomEndpoint.getRoom("2");

		// Asserts
		assertEquals("room 2", room.getName());
	}



	@Test
	public void TestF_getWrongRoom() {
		// Setup
		boolean expectedResult = false;

		// Test
		try {
			// Test
			roomEndpoint.getRoom("125");

		} catch (WebApplicationException e) {
			expectedResult = true;
		}

		// Asserts
		assertEquals(true, expectedResult);	
	}


	@Test
	public void TestG_reserveRoom() {
		// Setup
		boolean expectedResult = false;
		String token = "Zmlyc3QubGFzdDVAdGVzdC5jb206dGVzdDoxNDQ4NTI5MDc2ODQ0";
		try {
			// Test
			roomEndpoint.reserveRoom(token, "1");

			System.out.println("test");
			// Asserts
			assertEquals(false, expectedResult);
			expectedResult = true;
			
		} catch(WebApplicationException e ) {
		}
		// Asserts
		assertEquals(true, expectedResult);	
	}
	
	@Test
	public void TestH_reserveRoomAlreadyReserved() {
		// Setup
		boolean expectedResult = false;
		String token = "Zmlyc3QubGFzdDVAdGVzdC5jb206dGVzdDoxNDQ4NTI5MDc2ODQ0";
		try {
			// Test
			roomEndpoint.reserveRoom(token, "2");
					
		} catch(WebApplicationException e ) {
			expectedResult = true;
		}
		// Asserts
		assertEquals(true, expectedResult);	
	}

	@Test
	public void TestI_cancelRoom() {
		// Setup
		boolean expectedResult = false;
		String token = "Zmlyc3QubGFzdDVAdGVzdC5jb206dGVzdDoxNDQ4NTI5MDc2ODQ0";
		try {
			// Test
			roomEndpoint.cancelRoom(token, "1");

			// Asserts
			assertEquals(false, expectedResult);
			expectedResult = true;
			
		} catch(WebApplicationException e ) {
		}
		// Asserts
		assertEquals(true, expectedResult);	
	}
	
	@Test
	public void TestJ_reserveRoomNotExists() {
		// Setup
		boolean expectedResult = false;
		String token = "Zmlyc3QubGFzdDVAdGVzdC5jb206dGVzdDoxNDQ4NTI5MDc2ODQ0";
		try {
			// Test
			roomEndpoint.reserveRoom(token, "19889898");
			
		} catch(WebApplicationException e ) {
			expectedResult = true;
		}
		// Asserts
		assertEquals(true, expectedResult);	
	}
	
	@Test
	public void TestK_cancelRoomNotExists() {
		// Setup
		boolean expectedResult = false;
		String token = "Zmlyc3QubGFzdDVAdGVzdC5jb206dGVzdDoxNDQ4NTI5MDc2ODQ0";
		try {
			// Test
			roomEndpoint.cancelRoom(token, "19889898");
			
		} catch(WebApplicationException e ) {
			expectedResult = true;
		}
		// Asserts
		assertEquals(true, expectedResult);	
	}
	
	@Test
	public void TestL_initRoomStatsTable() {
		// SetUp
		boolean state = roomEndpoint.initRoomStatsTable();

		// Asserts
		assertEquals(true, state);
	}

	@Test
	public void TestM_checkReservedRoomsTask() {
		// SetUp
		boolean state = checkReservationTimeOutTask.checkReservationTestMethod();

		// Asserts
		assertEquals(true, state);
	}


}
