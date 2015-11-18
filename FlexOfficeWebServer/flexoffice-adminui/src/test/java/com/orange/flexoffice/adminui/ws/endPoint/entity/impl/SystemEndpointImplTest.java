package com.orange.flexoffice.adminui.ws.endPoint.entity.impl;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;

import com.orange.flexoffice.adminui.ws.endPoint.data.SystemEndpoint;
import com.orange.flexoffice.adminui.ws.endPoint.data.impl.SystemEndpointImpl;

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
	public void TestB_getSystem() {
		// Test
		com.orange.flexoffice.adminui.ws.model.System system =	systemEndpoint.getSystem();
		
		// Asserts
		assertEquals(2, system.getGatewayCount().intValue());
		assertEquals(1, system.getUserCount().intValue());
		assertEquals(2, system.getRoomCount().intValue());
	}


}
