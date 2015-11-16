package com.orange.flexoffice.adminui.ws.endPoint.entity.impl;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;


import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;

import com.orange.flexoffice.adminui.ws.endPoint.entity.GatewayEndpoint;
import com.orange.flexoffice.adminui.ws.model.GatewayOutput2;
import com.orange.flexoffice.adminui.ws.model.GatewaySummary;


public class GatewayEndpointImplTest {

	static {
	    try {
	      Log4jConfigurer.initLogging( "classpath:log4j-flexoffice-adminui-test.xml" );
	    }
	    catch( FileNotFoundException ex ) {
	      System.err.println( "Cannot Initialize log4j" );
	    }
	  }
	
	private static ClassPathXmlApplicationContext context;

	private static GatewayEndpoint gatewayEndpoint;

	@Context
	private UriInfo uriInfo;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void initSpringContextAndDatabase() throws Exception {
		context = new ClassPathXmlApplicationContext("applicationContext-flexoffice-adminui-test.xml");
		gatewayEndpoint = (GatewayEndpointImpl)context.getBean("gatewayEndpoint");
	}


	@Test
	public void initTables() {
		// SetUp
		boolean state = gatewayEndpoint.executeInitTestFile();
		 
		// Asserts
		assertEquals(true, state);
	}

	
	@Test
	public void getGateways() {
		// Test
		List<GatewaySummary> gateways =	gatewayEndpoint.getGateways();
		
		// Asserts
		assertEquals(2, gateways.size());
	}

	@Test
	public void getGatewayByGatewayByMacAddress() {
		// SetUp
		String macAddress = "FF:EE:ZZ:AA:GG:PP";
		
		// Test
		GatewayOutput2 gateways = gatewayEndpoint.getGateway(macAddress);
				
		// Asserts
		assertEquals(2, gateways.getRooms().size());
				
	}

}
