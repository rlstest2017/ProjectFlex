package com.orange.flexoffice.gatewayapi.ws.endPoint.entity.impl;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

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

import com.orange.flexoffice.gatewayapi.ws.endPoint.entity.SensorApiEndpoint;
import com.orange.flexoffice.gatewayapi.ws.endPoint.entity.GatewayApiEndpoint;
import com.orange.flexoffice.gatewayapi.ws.model.EOccupancyInfo;
import com.orange.flexoffice.gatewayapi.ws.model.ESensorStatus;
import com.orange.flexoffice.gatewayapi.ws.model.SensorInput;
import com.orange.flexoffice.gatewayapi.ws.model.SensorNewSummary;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SensorApiEndpointImplTest {

	static {
	    try {
	      Log4jConfigurer.initLogging( "classpath:log4j-flexoffice-gatewayapi-test.xml" );
	    }
	    catch( FileNotFoundException ex ) {
	      System.err.println( "Cannot Initialize log4j" );
	    }
	}
	
	private static ClassPathXmlApplicationContext context;

	private static SensorApiEndpoint sensorEndpoint;

	private static GatewayApiEndpoint gatewayEndpoint;

	@Context
	private UriInfo uriInfo;


	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void initSpringContextAndDatabase() throws Exception {
		context = new ClassPathXmlApplicationContext("applicationContext-flexoffice-gatewayapi-test.xml");
		sensorEndpoint = (SensorApiEndpointImpl)context.getBean("sensorEndpoint");
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
	public void TestB_addSensorMotion() throws WebApplicationException {
		// Setup
		final SensorNewSummary sensorInput = new SensorNewSummary();
		sensorInput.setId("Sensor from Gateway Id 1");
		sensorInput.setProfile("A5-07-01");
		sensorInput.setGatewayId("1");

		// Test
		final Response response = sensorEndpoint.addSensor(sensorInput);

		// Asserts
		assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());

	}
	

	@Test
	public void TestC_addSensorTemperature() throws WebApplicationException {
		// Setup
		final SensorNewSummary sensorInput = new SensorNewSummary();
		sensorInput.setId("Sensor from Gateway Id 2");
		sensorInput.setProfile("A5-04-01");
		sensorInput.setGatewayId("1");

		// Test
		final Response response = sensorEndpoint.addSensor(sensorInput);

		// Asserts
		assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());

	}

	@Test
	public void TestD_addSensorAlreadyExists() throws WebApplicationException {
		// Setup
		boolean expectedResult = false;
		final SensorNewSummary sensorInput = new SensorNewSummary();
		sensorInput.setId("Sensor from Gateway Id 2");
		sensorInput.setProfile("A5-04-01");
		sensorInput.setGatewayId("1");

		try {
			// Test
			sensorEndpoint.addSensor(sensorInput);
			expectedResult = true;
		} catch (WebApplicationException e) {
			
		}
		// Asserts
		assertEquals(true, expectedResult);	
	}
	
	@Test
	public void TestE_updateSensor() {
		// Setup
		boolean expectedResult = false;

		try {
			// Setup
			final SensorInput sensorInput = new SensorInput();
			sensorInput.setSensorStatus(ESensorStatus.ONLINE);

			// Test
			final Response response = sensorEndpoint.updateSensor("Sensor from Gateway Id 1", sensorInput);

			// Asserts
			assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());

			expectedResult = true;

		} catch(WebApplicationException e ) {
		}

		// Assert
		assertEquals(true, expectedResult);	
	}
	
	@Test
	public void TestF_updateSensorCreateAlert() {
		// Setup
		boolean expectedResult = false;

		try {
			// Setup
			final SensorInput sensorInput = new SensorInput();
			sensorInput.setSensorStatus(ESensorStatus.OFFLINE);

			// Test
			final Response response = sensorEndpoint.updateSensor("ident 1", sensorInput);

			// Asserts
			assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());

			expectedResult = true;

		} catch(WebApplicationException e ) {
		}

		// Assert
		assertEquals(true, expectedResult);	
	}

	@Test
	public void TestG_updateSensorUpdateAlert() {
		// Setup
		boolean expectedResult = false;

		try {
			// Setup
			final SensorInput sensorInput = new SensorInput();
			sensorInput.setSensorStatus(ESensorStatus.UNSTABLE_VOLTAGE);

			// Test
			final Response response = sensorEndpoint.updateSensor("ident 1", sensorInput);

			// Asserts
			assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());

			expectedResult = true;

		} catch(WebApplicationException e ) {
		}

		// Assert
		assertEquals(true, expectedResult);	
	}

	@Test
	public void TestH_updateSensorUpdateAlert() {
		// Setup
		boolean expectedResult = false;

		try {
			// Setup
			final SensorInput sensorInput = new SensorInput();
			sensorInput.setSensorStatus(ESensorStatus.UNSTABLE_RSSI);

			// Test
			final Response response = sensorEndpoint.updateSensor("ident 1", sensorInput);

			// Asserts
			assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());

			expectedResult = true;

		} catch(WebApplicationException e ) {
		}

		// Assert
		assertEquals(true, expectedResult);	
	}

	@Test
	public void TestI_updateSensorSameAlert() {
		// Setup
		boolean expectedResult = false;

		try {
			// Setup
			final SensorInput sensorInput = new SensorInput();
			sensorInput.setSensorStatus(ESensorStatus.UNSTABLE_RSSI);

			// Test
			final Response response = sensorEndpoint.updateSensor("ident 1", sensorInput);

			// Asserts
			assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());

			expectedResult = true;

		} catch(WebApplicationException e ) {
		}

		// Assert
		assertEquals(true, expectedResult);	
	}
	
	@Test
	public void TestJ_updateSensorDeleteAlertAndOccupancyInfoOCCUPIED() {
		// Setup
		boolean expectedResult = false;

		try {
			// Setup
			final SensorInput sensorInput = new SensorInput();
			sensorInput.setSensorStatus(ESensorStatus.ONLINE);
			sensorInput.setOccupancyInfo(EOccupancyInfo.OCCUPIED);

			// Test
			final Response response = sensorEndpoint.updateSensor("ident 1", sensorInput);// ident 1 !!!!

			// Asserts
			assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());

			expectedResult = true;

		} catch(WebApplicationException e ) {
		}

		// Assert
		assertEquals(true, expectedResult);	
	}
	
	@Test
	public void TestK_updateSensorOccupancyInfoINCHANGED() {
		// Setup
		boolean expectedResult = false;

		try {
			// Setup
			final SensorInput sensorInput = new SensorInput();
			sensorInput.setSensorStatus(ESensorStatus.ONLINE);
			sensorInput.setOccupancyInfo(EOccupancyInfo.UNOCCUPIED);

			// Test
			final Response response = sensorEndpoint.updateSensor("ident 2", sensorInput);// ident 2 !!!!

			// Asserts
			assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());

			expectedResult = true;

		} catch(WebApplicationException e ) {
		}

		// Assert
		assertEquals(true, expectedResult);	
	}

	@Test
	public void TestL_updateSensorOccupancyInfoCHANGED() {
		// Setup
		boolean expectedResult = false;

		try {
			// Setup
			final SensorInput sensorInput = new SensorInput();
			sensorInput.setSensorStatus(ESensorStatus.ONLINE);
			sensorInput.setOccupancyInfo(EOccupancyInfo.UNOCCUPIED);

			// Test
			final Response response = sensorEndpoint.updateSensor("ident 1", sensorInput);// ident 1 !!!!

			// Asserts
			assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());

			expectedResult = true;

		} catch(WebApplicationException e ) {
		}

		// Assert
		assertEquals(true, expectedResult);	
	}
	
	@Test
	public void TestM_updateSensorNotApparedCreateAlert() {
		// Setup
		boolean expectedResult = false;

		try {
			// Setup
			final SensorInput sensorInput = new SensorInput();
			sensorInput.setSensorStatus(ESensorStatus.UNSTABLE_RSSI);

			// Test
			final Response response = sensorEndpoint.updateSensor("Sensor from Gateway Id 1", sensorInput);

			// Asserts
			assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());

			expectedResult = true;

		} catch(WebApplicationException e ) {
		}

		// Assert
		assertEquals(true, expectedResult);	
	}

	@Test
	public void TestN_updateSensorNotApparedDeleteAlert() {
		// Setup
		boolean expectedResult = false;

		try {
			// Setup
			final SensorInput sensorInput = new SensorInput();
			sensorInput.setSensorStatus(ESensorStatus.OFFLINE);

			// Test
			final Response response = sensorEndpoint.updateSensor("Sensor from Gateway Id 1", sensorInput);

			// Asserts
			assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());

			expectedResult = true;

		} catch(WebApplicationException e ) {
		}

		// Assert
		assertEquals(true, expectedResult);	
	}

	@Test
	public void TestO_updateSensorDataNotExistsException() {
		// Setup
		boolean expectedResult = false;
		
		// Test
		try {
			// Setup
			final SensorInput sensorInput = new SensorInput();
			sensorInput.setSensorStatus(ESensorStatus.ONLINE);

			// Test
			sensorEndpoint.updateSensor("Sensor from Gateway Id 10055", sensorInput);
			
		} catch (WebApplicationException e) {
			expectedResult = true;
		}

		// Assert
		assertEquals(true, expectedResult);	
	}

}
