package com.orange.meetingroom.gui.ws.endPoint.data.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.FileNotFoundException;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.util.Log4jConfigurer;

import com.orange.meetingroom.gui.ws.endPoint.data.SystemEndpoint;
import com.orange.meetingroom.gui.ws.model.SystemCurrentDate;
import com.orange.meetingroom.gui.ws.model.SystemRemoteMacAddress;
import com.orange.meetingroom.gui.ws.model.SystemReturn;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SystemEndpointImplTest {
	
	static {
	    try {
	      Log4jConfigurer.initLogging( "classpath:log4j-meetingroom-gui-test.xml" );
	    }
	    catch( FileNotFoundException ex ) {
	      System.err.println( "Cannot Initialize log4j" );
	    }
	  }

	private static final Logger LOGGER = Logger.getLogger(SystemEndpointImplTest.class);

	private static ClassPathXmlApplicationContext context;
	private static SystemEndpoint systemEndpoint;
	
	@BeforeClass
	public static void initSpringContextAndDatabase() throws Exception {
		context = new ClassPathXmlApplicationContext("classpath*:applicationContext-meetingroom-gui-test.xml");
		systemEndpoint = (SystemEndpointImpl)context.getBean("systemEndpoint");
	}
	
	@Test
	public void TestA() {
		// SetUp
		boolean expectedResult = false;
		try {
			
			SystemReturn output = systemEndpoint.getSystem();
			
			// Asserts
			assertEquals("7", output.getHourStart().toString());
			
			
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
			SystemCurrentDate returnedDate = systemEndpoint.getSystemCurrentDate();
			assertEquals("1465379549", returnedDate.getCurrentDate().toString());
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
		boolean expectedResult = false;
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRemoteAddr("192.168.142.219");
		
		try {
			SystemRemoteMacAddress returnedData = systemEndpoint.getSystemRemoteMacAddress(request);
			assertNotEquals(null , returnedData);
			
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());
		}	
		// Asserts
		assertEquals(false, expectedResult);
	}
}
