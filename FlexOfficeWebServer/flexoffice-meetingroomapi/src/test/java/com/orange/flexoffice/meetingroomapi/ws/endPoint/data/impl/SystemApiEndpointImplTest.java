package com.orange.flexoffice.meetingroomapi.ws.endPoint.data.impl;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.math.BigInteger;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;

import com.orange.flexoffice.meetingroomapi.ws.endPoint.data.SystemApiEndpoint;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SystemApiEndpointImplTest {

	static {
	    try {
	      Log4jConfigurer.initLogging( "classpath:log4j-flexoffice-meetingroomapi-test.xml" );
	    }
	    catch( FileNotFoundException ex ) {
	    	System.err.println( "Cannot Initialize log4j" );
	    }
	}
	
	private static ClassPathXmlApplicationContext context;

	private static SystemApiEndpoint systemEndpoint;

	@Context
	private UriInfo uriInfo;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void initSpringContextAndDatabase() throws Exception {
		context = new ClassPathXmlApplicationContext("applicationContext-flexoffice-meetingroomapi-test.xml");
		systemEndpoint = (SystemApiEndpointImpl)context.getBean("systemEndpoint");
	}


	@Test
	public void TestA_initTables() {
		// SetUp
		boolean state = systemEndpoint.executeInitTestFile();
		 
		// Asserts
		assertEquals(true, state);
	}

	
	@Test
	public void TestB_GetSystem() {		
		// Test
		com.orange.flexoffice.meetingroomapi.ws.model.System system =	systemEndpoint.getSystem();
		
		// Asserts
		assertEquals(BigInteger.valueOf(3), system.getAgentTimeout());
		assertEquals(null, system.getCanShowOrganizer());
	}
}
