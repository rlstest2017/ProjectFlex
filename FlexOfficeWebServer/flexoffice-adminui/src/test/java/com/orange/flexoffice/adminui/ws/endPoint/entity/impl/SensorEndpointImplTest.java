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
import com.orange.flexoffice.adminui.ws.endPoint.entity.SensorEndpoint;
import com.orange.flexoffice.adminui.ws.model.ESensorType;
import com.orange.flexoffice.adminui.ws.model.RoomInput2;
import com.orange.flexoffice.adminui.ws.model.Sensor;
import com.orange.flexoffice.adminui.ws.model.SensorInput1;
import com.orange.flexoffice.adminui.ws.model.SensorInput2;
import com.orange.flexoffice.adminui.ws.model.SensorOutput;
import com.orange.flexoffice.adminui.ws.model.SensorSummary;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SensorEndpointImplTest {

	static {
		try {
			Log4jConfigurer.initLogging( "classpath:log4j-flexoffice-adminui-test.xml" );
		}
		catch( FileNotFoundException ex ) {
			System.err.println( "Cannot Initialize log4j" );
		}
	}

	private static ClassPathXmlApplicationContext context;

	private static SensorEndpoint sensorEndpoint;

	private static GatewayEndpoint gatewayEndpoint;


	@Context
	private UriInfo uriInfo;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void initSpringContextAndDatabase() throws Exception {
		context = new ClassPathXmlApplicationContext("applicationContext-flexoffice-adminui-test.xml");
		sensorEndpoint = (SensorEndpointImpl)context.getBean("sensorEndpoint");
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
	public void TestB_getSensors() {
		// Test
		List<SensorSummary> sensors = sensorEndpoint.getSensors();

		// Asserts
		assertEquals(3, sensors.size());
	}

	@Test
	public void TestC_getSensorBySensorId1() {

		// Test
		Sensor sensor = sensorEndpoint.getSensor("ident 2");

		// Asserts
		assertEquals("sensor 2", sensor.getName());
	}

	@Test
	public void TestD_getSensorBySensorId2() {

		// Test
		Sensor sensor = sensorEndpoint.getSensor("ident 3");

		// Asserts
		assertEquals(ESensorType.MOTION_DETECTION, sensor.getType());
	}



	@Test
	public void TestE_getWrongSensorDataNotExistsException() {
		// Setup
		boolean expectedResult = false;

		// Test
		try {
			// Test
			sensorEndpoint.getSensor("125");

		} catch (WebApplicationException e) {
			expectedResult = true;
		}

		// Asserts
		assertEquals(true, expectedResult);	
	}

	@Test
	public void TestF_addSensor() throws WebApplicationException {
		// Setup
		final SensorInput1 sensorInput = new SensorInput1();
		sensorInput.setIdentifier("Sensor Identifier 1");
		sensorInput.setName("Sensor Test 1");
		sensorInput.setDesc("Sensor Description 1");
		sensorInput.setType(ESensorType.MOTION_DETECTION);
		// No Profile and no room

		// Test
		final SensorOutput response = sensorEndpoint.addSensor(sensorInput);

		// Asserts
		assertNotNull(response.getIdentifier());


		// Test
		List<SensorSummary> sensors = sensorEndpoint.getSensors();

		// Asserts
		assertEquals(3, sensors.size());
	}


	@Test
	public void TestG_addSensorWithRoom() throws WebApplicationException {
		// Setup
		final SensorInput1 sensorInput = new SensorInput1();
		sensorInput.setIdentifier("Sensor Identifier 2");
		sensorInput.setName("Sensor Test 2");
		sensorInput.setDesc("Sensor Description 2");
		sensorInput.setType(ESensorType.TEMPERATURE_HUMIDITY);

		RoomInput2 room = new RoomInput2();
		room.setId("1");
		sensorInput.setRoom(room);
		// No Profile

		// Test
		final SensorOutput response = sensorEndpoint.addSensor(sensorInput);

		// Asserts
		assertNotNull(response.getIdentifier());


		// Test
		List<SensorSummary> sensors = sensorEndpoint.getSensors();

		// Asserts
		assertEquals(4, sensors.size());
	}


	@Test
	public void TestH_addSensorWithProfile() throws WebApplicationException {
		// Setup
		final SensorInput1 sensorInput = new SensorInput1();
		sensorInput.setIdentifier("Sensor Identifier 3");
		sensorInput.setName("Sensor Test 3");
		sensorInput.setDesc("Sensor Description 3");
		sensorInput.setType(ESensorType.TEMPERATURE_HUMIDITY);
		sensorInput.setProfile("A5-04-01");
		RoomInput2 room = new RoomInput2();
		room.setId("2");
		sensorInput.setRoom(room);

		// Test
		final SensorOutput response = sensorEndpoint.addSensor(sensorInput);

		// Asserts
		assertNotNull(response.getIdentifier());


		// Test
		List<SensorSummary> sensors = sensorEndpoint.getSensors();

		// Asserts
		assertEquals(5, sensors.size());
	}


	@Test
	public void TestI_addSensorAlreadyExists() {
		// Setup
		boolean expectedResult = false;
		final SensorInput1 sensorInput = new SensorInput1();
		sensorInput.setIdentifier("Sensor Identifier 1");
		sensorInput.setName("Sensor Test 1");
		sensorInput.setDesc("Sensor Description 1");
		sensorInput.setType(ESensorType.MOTION_DETECTION);
		sensorInput.setProfile("Sensor Profile 1");

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
	public void TestJ_updateSensor() throws WebApplicationException {

		// Setup
		boolean expectedResult = false;

		try {
			// Setup
			final SensorInput2 sensorInput = new SensorInput2();
			sensorInput.setName("Sensor Test 1 updated");
			sensorInput.setDesc("Sensor Description 1 updated");
			sensorInput.setType(ESensorType.MOTION_DETECTION);
			sensorInput.setProfile("Sensor Profile 1");

			// Test
			final Response response = sensorEndpoint.updateSensor("Sensor Identifier 1", sensorInput);

			// Asserts
			assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());


			// Test
			List<SensorSummary> sensors = sensorEndpoint.getSensors();

			// Asserts
			assertEquals(5, sensors.size());
			expectedResult = true;

		} catch(WebApplicationException e ) {
		}

		// Asserts
		assertEquals(true, expectedResult);	
	}



	@Test
	public void TestK_updateSensorDataNotExistsException() {
		// Setup
		boolean expectedResult = false;

		try {
			final SensorInput2 sensorInput = new SensorInput2();
			sensorInput.setName("Sensor Test 1 updated");
			sensorInput.setDesc("Sensor Description 1 updated");
			sensorInput.setType(ESensorType.MOTION_DETECTION);
			sensorInput.setProfile("Sensor Profile 1");

			// Test
			sensorEndpoint.updateSensor("Sensor Wrong Identifier 1", sensorInput);

		} catch (WebApplicationException e) {
			expectedResult = true;
		}


		// Asserts
		assertEquals(true, expectedResult);	
	}


	@Test
	public void TestL_getUnpairedSensors() {
		// Test
		List<SensorSummary> sensors = sensorEndpoint.getUnpairedSensors();

		// Asserts
		assertEquals(1, sensors.size());
	}


	@Test
	public void TestM_removeSensorDataNotExistsException() {
		// Setup
		boolean expectedResult = false;
		try {
			// Test
			sensorEndpoint.removeSensor("Sensor Wrong Identifier 1");

		} catch (WebApplicationException e) {
			expectedResult = true;
		}

		// Asserts
		assertEquals(true, expectedResult);	
	}


	@Test
	public void TestN_removeSensor() throws WebApplicationException {

		// Test
		Response response = sensorEndpoint.removeSensor("Sensor Identifier 1");

		// Assert
		assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
	}


}
