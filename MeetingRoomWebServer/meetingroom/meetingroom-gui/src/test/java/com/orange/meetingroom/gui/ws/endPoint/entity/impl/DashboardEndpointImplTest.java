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

import com.orange.meetingroom.gui.ws.endPoint.entity.DashboardEndpoint;
import com.orange.meetingroom.gui.ws.model.DashboardInput;
import com.orange.meetingroom.gui.ws.model.DashboardOutput;
import com.orange.meetingroom.gui.ws.model.EDashboardStatus;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DashboardEndpointImplTest {

	static {
	    try {
	      Log4jConfigurer.initLogging( "classpath:log4j-meetingroom-gui-test.xml" );
	    }
	    catch( FileNotFoundException ex ) {
	      System.err.println( "Cannot Initialize log4j" );
	    }
	  }

	private static final Logger LOGGER = Logger.getLogger(DashboardEndpointImplTest.class);

	private static ClassPathXmlApplicationContext context;
	private static DashboardEndpoint dashboardEndpoint;
	
	@BeforeClass
	public static void initSpringContextAndDatabase() throws Exception {
		context = new ClassPathXmlApplicationContext("classpath*:applicationContext-meetingroom-gui-test.xml");
		dashboardEndpoint = (DashboardEndpointImpl)context.getBean("dashboardEndpoint");
	}
	
	@Test
	public void TestA() {
		// SetUp
		boolean expectedResult = false;
		try {
			DashboardInput params = new DashboardInput();
			params.setDashboardStatus(EDashboardStatus.ONLINE);
			DashboardOutput output = dashboardEndpoint.updateDashboard("rl:rl:rl:rl:rl:rl", params);
			
			// Asserts
			assertEquals("NONE", output.getCommand().toString());
			
			
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());
		}	
		// Asserts
		assertEquals(false, expectedResult);
	}
	
	@Test
	public void TestB_DASHBOARD_NOT_FOUND() {
		// SetUp
		boolean expectedResult = false;
		try {
			DashboardInput params = new DashboardInput();
			params.setDashboardStatus(EDashboardStatus.ONLINE);
			dashboardEndpoint.updateDashboard("tk:rl:rl:rl:rl:rl", params);
			
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error("TestB_DASHBOARD_NOT_FOUND : " +e.getMessage());
		}	
		// Asserts
		assertEquals(true, expectedResult);
	}
	
	
	
}
