package com.orange.meetingroom.gui.ws.endPoint.entity.impl;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;


import com.orange.meetingroom.gui.ws.endPoint.entity.MeetingRoomEndpoint;
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
	
	@BeforeClass
	public static void initSpringContextAndDatabase() throws Exception {
		context = new ClassPathXmlApplicationContext("classpath*:applicationContext-meetingroom-gui-test.xml");
		meetingRoomEndpoint = (MeetingRoomEndpointImpl)context.getBean("meetingRoomEndpoint");
	}
	
	@Test
	public void TestA() {
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
	public void TestC() {
		// SetUp
		Boolean output = meetingRoomEndpoint.checkMeetingRoomsStatusTimeOutTestMethod();
		// Asserts
		assertEquals(true, output);
	
	}

}
