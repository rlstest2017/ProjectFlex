package com.orange.flexoffice.userui.ws.endPoint.entity.impl;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;

import com.orange.flexoffice.userui.ws.endPoint.entity.RoomEndpoint;
import com.orange.flexoffice.userui.ws.model.Room;
import com.orange.flexoffice.userui.ws.model.RoomSummary;
import com.orange.flexoffice.business.common.service.data.TestManager;

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

	@Autowired
	private TestManager testManager;


	@Context
	private UriInfo uriInfo;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void initSpringContextAndDatabase() throws Exception {
		context = new ClassPathXmlApplicationContext("applicationContext-flexoffice-userui-test.xml");
		roomEndpoint = (RoomEndpointImpl)context.getBean("roomEndpoint");
	}


//	@Test
//	public void TestA_initTables() {
//		// SetUp
//		boolean state = testManager.executeInitTestFile();
//
//		// Asserts
//		assertEquals(true, state);
//	}


	@Test
	public void TestB_getRooms() {
		// Test
		List<RoomSummary> rooms = roomEndpoint.getRooms();

		// Asserts
		assertEquals(2, rooms.size());
	}

	@Test
	public void TestC_getRoomByRoomId1() {

		// Test
		Room room = roomEndpoint.getRoom("1");

		// Asserts
		assertEquals("room 1", room.getName());
	}

	@Test
	public void TestD_getRoomByRoomId2() {

		// Test
		Room room = roomEndpoint.getRoom("2");

		// Asserts
		assertEquals("room 2", room.getName());
	}



	@Test
	public void TestE_getWrongRoomDataNotExistsException() {
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
	public void TestF_reserveRoom() throws WebApplicationException {

		// Setup
		boolean expectedResult = false;
		
		try {
			// Test
			final Response response = roomEndpoint.reserveRoom("1");

			// Asserts
			assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());
			expectedResult = true;
			
		} catch(WebApplicationException e ) {
		}

		// Asserts
		assertEquals(true, expectedResult);	
	}
	

}
