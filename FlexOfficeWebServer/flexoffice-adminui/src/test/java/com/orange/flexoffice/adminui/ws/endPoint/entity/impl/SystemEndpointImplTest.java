package com.orange.flexoffice.adminui.ws.endPoint.entity.impl;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;

import com.orange.flexoffice.adminui.ws.endPoint.data.SystemEndpoint;
import com.orange.flexoffice.adminui.ws.endPoint.data.impl.SystemEndpointImpl;
import com.orange.flexoffice.adminui.ws.model.ETeachinStatus;
import com.orange.flexoffice.adminui.ws.model.Teachin;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SystemEndpointImplTest {

	static {
	    try {
	      Log4jConfigurer.initLogging( "classpath:log4j-flexoffice-adminui-test.xml" );
	    }
	    catch( FileNotFoundException ex ) {
	      System.err.println( "Cannot Initialize log4j" );
	    }
	  }
	
	private static ClassPathXmlApplicationContext context;

	private static SystemEndpoint systemEndpoint;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void initSpringContextAndDatabase() throws Exception {
		context = new ClassPathXmlApplicationContext("applicationContext-flexoffice-adminui-test.xml");
		systemEndpoint = (SystemEndpointImpl)context.getBean("systemEndpoint");
	}


	@Test
	public void TestA_initTables() {
		// SetUp
		boolean state = systemEndpoint.executeInitTestFile();
		 
		// Asserts
		assertEquals(true, state);
	}

	@Test
	public void TestAB_initTeachinSensorsTable() {
		// SetUp
		boolean state = systemEndpoint.initTeachinSensorsTable();
		 
		// Asserts
		assertEquals(true, state);
	}
	
	@Test
	public void TestAC_getTeachin() {
		// SetUp
		Teachin teachin = systemEndpoint.getTeachin();
		 
		// Asserts
		assertEquals(teachin.getStatus(), ETeachinStatus.INITIALIZING);
	}
	
	@Test
	public void TestB_getSystem() {
		// Test
		com.orange.flexoffice.adminui.ws.model.System system =	systemEndpoint.getSystem();
		
		// Asserts
		assertEquals(3, system.getGatewayCount().intValue());
		assertEquals(4, system.getUserCount().intValue());
		assertEquals(5, system.getRoomCount().intValue());
	}

	@Test
	public void TestC_logout() {
		// Setup
		String token = "Zmlyc3QubGFzdDVAdGVzdC5jb206dGVzdDoxNDQ4NTI5MDc2ODQ0";
		
		// Test
		Response response = systemEndpoint.logout(token);

		// Asserts
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
	}

	@Test
	public void TestD_login() {
		// Setup
		String authorization = "Basic YWRtaW5Ab2FiLmNvbTpmbGV4b2ZmaWNl";
		
		// Test
		Response response = systemEndpoint.login(authorization, null);

		// Asserts
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
	}

	@Test
	public void TestE_initTeachin() {
		// Setup
		String token = "Zmlyc3QubGFzdDFAdGVzdC5jb206cGFzczoxNDQ4NjEzNjU2MDk4";
		String roomId = "1";
		
		// Test
		Response response = systemEndpoint.initTeachin(token, roomId);

		// Asserts
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void TestF_initTeachinTeachinAlreadyExist() {
		// Setup
		boolean expectedResult = false;
		String token = "Zmlyc3QubGFzdDFAdGVzdC5jb206cGFzczoxNDQ4NjEzNjU2MDk4";
		String roomId = "3";
		
		try {
			// Test
			systemEndpoint.initTeachin(token, roomId);
			
		} catch (WebApplicationException e) {
			expectedResult = true;
		}
		
		// Asserts
		assertEquals(true, expectedResult);
	}
	
	@Test
	public void TestG_cancelTeachin() {
		// Test
		Response response = systemEndpoint.cancelTeachin();

		// Asserts
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void TestH_cancelTeachinNotActive() {
		// Setup
		boolean expectedResult = false;
		
		try {
			// Test
			systemEndpoint.cancelTeachin();
			
		} catch (WebApplicationException e) {
			expectedResult = true;
		}
		
		// Asserts
		assertEquals(true, expectedResult);	
	}
	
	@Test
	public void TestI_setTeachinSensorsTable() {
		// SetUp
		boolean state = systemEndpoint.setTeachinSensorsTable();
		 
		// Asserts
		assertEquals(true, state);
	}
	
	@Test
	public void TestJ_getTeachin() {
		// SetUp
		Teachin teachin = systemEndpoint.getTeachin();
		 
		// Asserts
		assertEquals(teachin.getStatus(), ETeachinStatus.RUNNING);
	}
	
	@Test
	public void TestK_submitTeachin() {
		// SetUp
		List<String> sensorIdentifiers = new ArrayList<String>();
		sensorIdentifiers.add("ident 1");
		sensorIdentifiers.add("ident 2");
		sensorIdentifiers.add("ident 3");
		
		systemEndpoint.submitTeachin(sensorIdentifiers);
		 
		// Asserts
		assertEquals("ENDED", ETeachinStatus.ENDED.toString());
	}
	
}
