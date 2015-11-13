package com.orange.flexoffice.adminui.ws.endPoint.entity.impl;

import static org.junit.Assert.*;

import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;


import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.orange.flexoffice.adminui.ws.endPoint.entity.GatewayEndpoint;
import com.orange.flexoffice.adminui.ws.model.GatewayOutput2;
import com.orange.flexoffice.adminui.ws.model.GatewaySummary;


public class GatewayEndpointImplTest {

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
		boolean state = gatewayEndpoint.executeGatewaysTestFile();
		 
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
	public void getGatewayByGatewayId() {
		// SetUp
		String gatewayId = "1";
		
		// Test
		GatewayOutput2 gateways = gatewayEndpoint.getGateway(gatewayId);
				
		// Asserts
		assertEquals(2, gateways.getRooms().size());
				
	}

}