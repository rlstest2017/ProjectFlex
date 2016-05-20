package com.orange.flexoffice.meetingroomapi.ws.endPoint.entity.impl;

import static org.junit.Assert.assertEquals;

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

import com.orange.flexoffice.meetingroomapi.ws.endPoint.entity.DashboardApiEndpoint;
import com.orange.flexoffice.meetingroomapi.ws.model.DashboardInput;
import com.orange.flexoffice.meetingroomapi.ws.model.DashboardOutput;
import com.orange.flexoffice.meetingroomapi.ws.model.ECommandModel;
import com.orange.flexoffice.meetingroomapi.ws.model.EDashboardStatus;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DashboardApiEndpointImplTest {

	static {
	    try {
	      Log4jConfigurer.initLogging( "classpath:log4j-flexoffice-meetingroomapi-test.xml" );
	    }
	    catch( FileNotFoundException ex ) {
	      System.err.println( "Cannot Initialize log4j" );
	    }
	}
	
	private static ClassPathXmlApplicationContext context;

	private static DashboardApiEndpoint dashboardEndpoint;

	@Context
	private UriInfo uriInfo;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void initSpringContextAndDatabase() throws Exception {
		context = new ClassPathXmlApplicationContext("applicationContext-flexoffice-meetingroomapi-test.xml");
		dashboardEndpoint = (DashboardApiEndpointImpl)context.getBean("dashboardEndpoint");
	}


	@Test
	public void TestA_initTables() {
		// SetUp
		boolean state = dashboardEndpoint.executeInitTestFile();
		 
		// Asserts
		assertEquals(true, state);
	}

	
	@Test
	public void TestB_UpdateStatus() {
		String macAddress = "FF:EE:ZZ:AA:GG:PP";
		DashboardInput dashboard = new DashboardInput();
		dashboard.setDashboardStatus(EDashboardStatus.ECONOMIC);
		
		// Test
		DashboardOutput dashboardOutput =	dashboardEndpoint.updateStatus(macAddress, dashboard);
		
		// Assert
		assertEquals(ECommandModel.ECONOMIC, dashboardOutput.getCommand());
	}

	@Test
	public void TestC_GetConfig() {
		String macAddress = "FF:EE:ZZ:AA:GG:PP";
		
		// Test
		List<String> lst =	dashboardEndpoint.getConfig(macAddress);
		
		// Assert
		assertEquals(3, lst.size());
	}
	
	@Test
	public void TestC_GetConfig2() {
		String macAddress = "FF:TS:ZZ:AA:GG:ZZ";
		boolean expected = false;
		
		try {
			// Test
			dashboardEndpoint.getConfig(macAddress);
		} catch(WebApplicationException e){
			expected = true;
		}
		
		// Assert
		assertEquals(true, expected);
	}
	
}
