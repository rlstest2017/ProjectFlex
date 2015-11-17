package com.orange.flexoffice.gatewayapi.ws.endPoint.entity.impl;

import static org.junit.Assert.*;

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

import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.dao.common.model.object.GatewayDto;
import com.orange.flexoffice.gatewayapi.ws.endPoint.entity.GatewayApiEndpoint;
import com.orange.flexoffice.gatewayapi.ws.endPoint.support.ObjectFactory;
import com.orange.flexoffice.gatewayapi.ws.model.ECommandModel;
import com.orange.flexoffice.gatewayapi.ws.model.GatewayInput;
import com.orange.flexoffice.gatewayapi.ws.model.GatewayReturn;
import com.orange.flexoffice.gatewayapi.ws.model.GatewaySummary;
import com.orange.flexoffice.gatewayapi.ws.model.Room;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GatewayApiEndpointImplTest {

	static {
	    try {
	      Log4jConfigurer.initLogging( "classpath:log4j-flexoffice-gatewayapi-test.xml" );
	    }
	    catch( FileNotFoundException ex ) {
	      System.err.println( "Cannot Initialize log4j" );
	    }
	}
	
	private static ClassPathXmlApplicationContext context;

	private static GatewayApiEndpoint gatewayEndpoint;

	@Context
	private UriInfo uriInfo;

	private final ObjectFactory factory = new ObjectFactory();

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void initSpringContextAndDatabase() throws Exception {
		context = new ClassPathXmlApplicationContext("applicationContext-flexoffice-gatewayapi-test.xml");
		gatewayEndpoint = (GatewayApiEndpointImpl)context.getBean("gatewayEndpoint");
	}


	@Test
	public void TestA_initTables() {
		// SetUp
		boolean state = gatewayEndpoint.executeInitTestFile();
		 
		// Asserts
		assertEquals(true, state);
	}

	
	@Test
	public void TestB_getGateways() {
		// Test
		List<GatewaySummary> gateways =	gatewayEndpoint.getGateways();
		
		// Asserts
		assertEquals(2, gateways.size());
	}

	@Test
	public void TestC_getGatewayByGatewayId() {
		// SetUp
		String gatewayId = "1";
		
		// Test
		List<Room> rooms = gatewayEndpoint.getGateway(gatewayId);
		
		// Asserts
		assertEquals(2, rooms.size());
				
		for (Room room : rooms) {
		 if (room.getId().equals("1")) {
			 assertEquals(2, room.getSensors().size());
		 } else {
			 assertEquals(1, room.getSensors().size());
		 }
		}
	}
	
	@Test
	public void TestD_updateGateway() {
		// Setup
		boolean expectedResult = false;
		final GatewayInput gatewayIn = factory.createApiGateway("ONLINE");

		try {
			final GatewayDto gatewayOut = gatewayEndpoint.findByMacAddress("FF:TT:ZZ:AA:GG:PP");

			// Test
			final GatewayReturn response = gatewayEndpoint.updateGateway(gatewayOut.getId(), gatewayIn);

			// Assert
			assertEquals(ECommandModel.NONE, response.getCommand());
			expectedResult = true;

		} catch(DataNotExistsException e ) {
		}

		// Assert
		assertEquals(true, expectedResult);	

	}
	
	@Test
	public void TestE_updateGatewayDataNotExistsException() {
		// Setup
		boolean expectedResult = false;

		// Test
		try {
			// Setup
			final GatewayInput gatewayIn = factory.createApiGateway("ONLINE");
			
			final GatewayDto gatewayOut = gatewayEndpoint.findByMacAddress("TT:NN:MM:AA:HH:RR");

			gatewayEndpoint.updateGateway(gatewayOut.getId(), gatewayIn);

		} catch(DataNotExistsException e ) {
			expectedResult = true;
		} catch (WebApplicationException e) {
			
		}

		// Assert
		assertEquals(true, expectedResult);	
	}

}
