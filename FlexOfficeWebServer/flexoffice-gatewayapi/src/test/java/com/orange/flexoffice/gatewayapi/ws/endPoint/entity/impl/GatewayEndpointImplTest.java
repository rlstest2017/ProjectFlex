package com.orange.flexoffice.gatewayapi.ws.endPoint.entity.impl;

import static org.junit.Assert.*;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;


import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.orange.flexoffice.gatewayapi.ws.endPoint.entity.GatewayEndpoint;


public class GatewayEndpointImplTest {

	private static ClassPathXmlApplicationContext context;

	private static GatewayEndpoint gatewayEndpoint;

	@Context
	private UriInfo uriInfo;

	//private final ObjectFactory factory = new ObjectFactory();

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void initSpringContextAndDatabase() throws Exception {
		context = new ClassPathXmlApplicationContext("applicationContext-flexoffice-gatewayapi-test.xml");
		gatewayEndpoint = (GatewayEndpointImpl)context.getBean("gatewayEndpoint");
	}


	@Test
	public void initTables() throws WebApplicationException {
		// SetUp
		boolean state = gatewayEndpoint.executeGatewaysTestFile();
		 
		// Asserts
		assertEquals(true, state);
	}




}
