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

		} catch (WebApplicationException e) {
			expectedResult = true;
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
			sensorInput.setSensorStatus(ESensorStatus.UNSTABLE);

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
	public void TestF_updateSensorDataNotExistsException() {
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