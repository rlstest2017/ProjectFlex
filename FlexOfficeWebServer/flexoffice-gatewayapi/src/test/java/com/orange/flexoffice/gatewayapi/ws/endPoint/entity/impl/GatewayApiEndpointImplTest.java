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
		assertEquals(3, gateways.size());
	}

	@Test
	public void TestC_getGatewayByGatewayMacAddress() {
		// SetUp
		String gatewayMacAddress = "FF:EE:ZZ:AA:GG:PP";
		
		// Test
		List<Room> rooms = gatewayEndpoint.getGateway(gatewayMacAddress);
		
		// Asserts
		assertEquals(2, rooms.size());
				
		for (Room room : rooms) {
		 if (room.getId().toString().equals("1")) {
			 assertEquals(2, room.getSensors().size());
		 } else {
			 assertEquals(1, room.getSensors().size());
		 }
		}
	}
	
	@Test
	public void TestD_updateGateway() {
		// Setup
		final GatewayInput gatewayIn = factory.createApiGateway("ONLINE");

			// Test
			final GatewayReturn response = gatewayEndpoint.updateGateway("FF:TT:ZZ:AA:GG:PP", gatewayIn);

			// Assert
			assertEquals(ECommandModel.NONE, response.getCommand());
	}

	@Test
	public void TestE_updateGatewayCreateAlert() {
		// Setup
		final GatewayInput gatewayIn = factory.createApiGateway("OFFLINE");

			// Test
			final GatewayReturn response = gatewayEndpoint.updateGateway("FF:TT:ZZ:AA:GG:PP", gatewayIn);

			// Assert
			assertEquals(ECommandModel.NONE, response.getCommand());
	}

	@Test
	public void TestF_updateGatewayUpdateAlert() {
		// Setup
		final GatewayInput gatewayIn = factory.createApiGateway("ERROR_NO_USB_DEVICE");

			// Test
			final GatewayReturn response = gatewayEndpoint.updateGateway("FF:TT:ZZ:AA:GG:PP", gatewayIn);

			// Assert
			assertEquals(ECommandModel.NONE, response.getCommand());
	}

	@Test
	public void TestG_updateGatewayUpdateAlert() {
		// Setup
		final GatewayInput gatewayIn = factory.createApiGateway("ERROR_FIFO_FILE");

			// Test
			final GatewayReturn response = gatewayEndpoint.updateGateway("FF:TT:ZZ:AA:GG:PP", gatewayIn);

			// Assert
			assertEquals(ECommandModel.NONE, response.getCommand());
	}

	@Test
	public void TestH_updateGatewaySameAlert() {
		// Setup
		final GatewayInput gatewayIn = factory.createApiGateway("ERROR_FIFO_FILE");

			// Test
			final GatewayReturn response = gatewayEndpoint.updateGateway("FF:TT:ZZ:AA:GG:PP", gatewayIn);

			// Assert
			assertEquals(ECommandModel.NONE, response.getCommand());
	}
	
	@Test
	public void TestI_updateGatewayDeleteAlert() {
		// Setup
		final GatewayInput gatewayIn = factory.createApiGateway("ONLINE");

			// Test
			final GatewayReturn response = gatewayEndpoint.updateGateway("FF:TT:ZZ:AA:GG:PP", gatewayIn);

			// Assert
			assertEquals(ECommandModel.NONE, response.getCommand());
	}
	
	@Test
	public void TestJ_updateGatewayNotAssociatedCreateAlert() {
		// Setup
		final GatewayInput gatewayIn = factory.createApiGateway("ERROR_FIFO_FILE");

			// Test
			final GatewayReturn response = gatewayEndpoint.updateGateway("AA:BB:CC:AA:GG:PP", gatewayIn);

			// Assert
			assertEquals(ECommandModel.NONE, response.getCommand());
	}

	@Test
	public void TestK_updateGatewayNotAssociatedDeleteAlert() {
		// Setup
		final GatewayInput gatewayIn = factory.createApiGateway("OFFLINE");

			// Test
			final GatewayReturn response = gatewayEndpoint.updateGateway("AA:BB:CC:AA:GG:PP", gatewayIn);

			// Assert
			assertEquals(ECommandModel.NONE, response.getCommand());
	}

	@Test
	public void TestL_updateGatewayDataNotExistsException() {
		// Setup
		boolean expectedResult = false;
		String macAddress = "TT:NN:MM:KK:HH:RR";
		
		// Test
		try {
			// Setup
			final GatewayInput gatewayIn = factory.createApiGateway("ONLINE");
			gatewayEndpoint.updateGateway(macAddress, gatewayIn);
			
		} catch (WebApplicationException e) {
			expectedResult = true;
		}

		// Assert
		assertEquals(true, expectedResult);	
	}
	
	@Test
	public void TestM_initTeachinSensorsTable() {
		// SetUp
		boolean state = gatewayEndpoint.initTeachinSensorsTable();
		 
		// Asserts
		assertEquals(true, state);
	}

	@Test
	public void TestN_updateGatewayTeachinActiveForGatewayId() { // teachin_status='INITIALIZING'
		// Setup
		final GatewayInput gatewayIn = factory.createApiGateway("ONTEACHIN");

			// Test
			final GatewayReturn response = gatewayEndpoint.updateGateway("FF:EE:ZZ:AA:GG:PP", gatewayIn);
			// teachin_status become 'RUNNING'
			
			// Assert
			assertEquals(ECommandModel.TEACHIN, response.getCommand());
	}
	
	@Test
	public void TestO_updateGatewayTeachinSetENDED() {
		// Setup
		final GatewayInput gatewayIn = factory.createApiGateway("ERROR_NO_USB_DEVICE");

			// Test
			final GatewayReturn response = gatewayEndpoint.updateGateway("FF:EE:ZZ:AA:GG:PP", gatewayIn);
			// teachin_status become 'ENDED'
			// Assert
			assertEquals(ECommandModel.NONE, response.getCommand());
	}
	
	@Test
	public void TestP_updateGatewayTeachinActiveForGatewayIdButENDED() { // teachin_status='ENDED'
		// Setup
		final GatewayInput gatewayIn = factory.createApiGateway("ONTEACHIN");

			// Test
			final GatewayReturn response = gatewayEndpoint.updateGateway("FF:EE:ZZ:AA:GG:PP", gatewayIn);
						
			// Assert
			assertEquals(ECommandModel.STOPTEACHIN, response.getCommand());
	}
	
	@Test
	public void TestQ_updateGatewayTeachinActiveForAnotherGatewayId() {
		// Setup
		final GatewayInput gatewayIn = factory.createApiGateway("ONTEACHIN");

			// Test
			final GatewayReturn response = gatewayEndpoint.updateGateway("FF:TT:ZZ:AA:GG:PP", gatewayIn);

			// Assert
			assertEquals(ECommandModel.STOPTEACHIN, response.getCommand());
	}

}
