package com.orange.flexoffice.adminui.ws.endPoint.entity.impl;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;

import com.orange.flexoffice.adminui.ws.endPoint.entity.GatewayEndpoint;
import com.orange.flexoffice.adminui.ws.endPoint.support.ObjectFactory;
import com.orange.flexoffice.adminui.ws.model.GatewayInput;
import com.orange.flexoffice.adminui.ws.model.GatewayInput3;
import com.orange.flexoffice.adminui.ws.model.GatewayOutput;
import com.orange.flexoffice.adminui.ws.model.GatewayOutput2;
import com.orange.flexoffice.adminui.ws.model.GatewaySummary;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.dao.common.model.object.GatewayDto;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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

	private final ObjectFactory factory = new ObjectFactory();
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void initSpringContextAndDatabase() throws Exception {
		context = new ClassPathXmlApplicationContext("applicationContext-flexoffice-adminui-test.xml");
		gatewayEndpoint = (GatewayEndpointImpl)context.getBean("gatewayEndpoint");
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
	public void TestC_getGatewayByGatewayByMacAddress() {
		// SetUp
		String macAddress = "FF:EE:ZZ:AA:GG:PP";
		
		// Test
		GatewayOutput2 gateways = gatewayEndpoint.getGateway(macAddress);
				
		// Asserts
		assertEquals(2, gateways.getRooms().size());
				
	}
	
	@Test
	public void TestD_addGateway() {
		// Setup
		final GatewayInput3 gateway = factory.createHmiGateway("AA:DD:SS:PP:SS:MM", "gateway 10", "gateway 10 test");
		
		// Test
		final GatewayOutput response = gatewayEndpoint.addGateway(gateway);
		
		// Assert
		assertNotNull(response.getMacAddress());
		
	}
	
	@Test
	public void TestE_addGatewayDataAlreadyExistsException() {
		// Setup
		boolean expectedResult = false;
		
		// Test
		try {
			// Setup
			final GatewayDto gatewayOut = gatewayEndpoint.findByMacAddress("AA:DD:SS:PP:SS:MM");
			if (gatewayOut != null) {
				
				final GatewayInput3 gateway = factory.createHmiGateway(gatewayOut.getMacAddress(), gatewayOut.getName(), gatewayOut.getDescription());

				gatewayEndpoint.addGateway(gateway);
			}
			
		} catch(DataNotExistsException e ) {
		
		} catch (WebApplicationException e) {
			expectedResult = true;
		}

		// Assert
		assertEquals(true, expectedResult);	
	}
	
	@Test
	public void TestF_updateGateway() throws WebApplicationException {
		// Setup
		boolean expectedResult = false;
		final GatewayInput gatewayIn = factory.createHmi2Gateway("gateway 11", "gateway 11 test");

		try {
			final GatewayDto gatewayOut = gatewayEndpoint.findByMacAddress("AA:DD:SS:PP:SS:MM");

			// Test
			Response response = gatewayEndpoint.updateGateway(gatewayOut.getMacAddress(), gatewayIn);

			// Assert
			assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());
			expectedResult = true;

		} catch(DataNotExistsException e ) {
		}

		// Assert
		assertEquals(true, expectedResult);	
	}
	
	@Test
	public void TestG_updateGatewayDataNotExistsException() {
		// Setup
		boolean expectedResult = false;

		// Test
		try {
			// Setup
			final GatewayInput gatewayIn = factory.createHmi2Gateway("gateway 12", "gateway 12 test");
			
			gatewayEndpoint.updateGateway("TT:NN:MM:KK:HH:RR", gatewayIn);

		} catch (WebApplicationException e) {
			expectedResult = true;
		}

		// Assert
		assertEquals(true, expectedResult);	
	}
	
	@Test
	public void TestH_removeGateway() throws WebApplicationException {
		// Setup
		boolean expectedResult = false;

		try {
			final GatewayDto gatewayOut = gatewayEndpoint.findByMacAddress("AA:DD:SS:PP:SS:MM");

			// Test
			Response response = gatewayEndpoint.removeGateway(gatewayOut.getMacAddress());

			// Assert
			assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
			expectedResult = true;

		} catch(DataNotExistsException e ) {
		}
		
		// Assert
		assertEquals(true, expectedResult);	
	}
	
	@Test
	public void TestI_removeGatewayDataNotExistsException() {
		// Setup
		boolean expectedResult = false;

		// Test
		try {
			// Setup
			final GatewayDto gatewayOut = gatewayEndpoint.findByMacAddress("AA:DD:SS:PP:SS:MM");

			// Test
			gatewayEndpoint.removeGateway(gatewayOut.getMacAddress());
			
		} catch(DataNotExistsException e ) {
			expectedResult = true;
		} catch (WebApplicationException e) {
			
		}

		// Assert
		assertEquals(true, expectedResult);	
	}

}
