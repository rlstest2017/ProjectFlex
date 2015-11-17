package com.orange.flexoffice.adminui.ws.endPoint.entity.impl;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.math.BigInteger;
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
import com.orange.flexoffice.adminui.ws.model.GatewayInput2;
import com.orange.flexoffice.adminui.ws.model.Sensor;
import com.orange.flexoffice.adminui.ws.model.SensorInput1;
import com.orange.flexoffice.adminui.ws.model.SensorOutput;
import com.orange.flexoffice.adminui.ws.model.SensorSummary;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.dao.common.model.data.SensorDao;

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

//	@Test
//	public void TestF_addSensor() throws WebApplicationException {
//		// Setup
//		final SensorInput1 sensorInput = new SensorInput1();
//		sensorInput.setName("SensorTest1");
//		sensorInput.setAddress("SensorAddress1");
//		sensorInput.setCapacity(BigInteger.valueOf(4));
//		sensorInput.setDesc("SensorDescription1");
//		sensorInput.setType(ESensorType.BOX);
//		final GatewayInput2 gateway = new GatewayInput2();
//		gateway.setId("1");
//		sensorInput.setGateway(gateway);
//
//		// Test
//		final SensorOutput response = sensorEndpoint.addSensor(sensorInput);
//
//		// Asserts
//		assertNotNull(response.getId());
//
//
//		// Test
//		List<SensorSummary> sensors = sensorEndpoint.getSensors();
//
//		// Asserts
//		assertEquals(3, sensors.size());
//	}
//
//	@Test
//	public void TestG_addSensorAlreadyExists() {
//		// Setup
//		boolean expectedResult = false;
//		final SensorInput1 sensorInput = new SensorInput1();
//		sensorInput.setName("SensorTest1");
//		sensorInput.setAddress("SensorAddress1");
//		sensorInput.setCapacity(BigInteger.valueOf(4));
//		sensorInput.setDesc("SensorDescription1");
//		sensorInput.setType(ESensorType.BOX);
//		final GatewayInput2 gateway = new GatewayInput2();
//		gateway.setId("1");
//		sensorInput.setGateway(gateway);
//
//		try {
//			// Test
//			sensorEndpoint.addSensor(sensorInput);
//
//		} catch (WebApplicationException e) {
//			expectedResult = true;
//		}
//		// Asserts
//		assertEquals(true, expectedResult);	
//	}
//
//	@Test
//	public void TestH_updateSensor() throws WebApplicationException {
//
//		// Setup
//		boolean expectedResult = false;
//		
//		try {
//			SensorDao user = sensorEndpoint.findByName("SensorTest1");
//
//			// Setup
//			final SensorInput1 sensorInput = new SensorInput1();
//			sensorInput.setName("SensorTest1");
//			sensorInput.setAddress("SensorAddress-modified");
//			sensorInput.setCapacity(BigInteger.valueOf(4));
//			sensorInput.setDesc("SensorDescription-modified");
//			sensorInput.setType(ESensorType.BOX);
//			final GatewayInput2 gateway = new GatewayInput2();
//			gateway.setId("1");
//			sensorInput.setGateway(gateway);
//
//			// Test
//			final Response response = sensorEndpoint.updateSensor(user.getColumnId(), sensorInput);
//
//			// Asserts
//			assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());
//
//
//			// Test
//			List<SensorSummary> sensors = sensorEndpoint.getSensors();
//
//			// Asserts
//			assertEquals(3, sensors.size());
//			expectedResult = true;
//			
//		} catch(DataNotExistsException e ) {
//		}
//
//		// Asserts
//		assertEquals(true, expectedResult);	
//	}
//	
//
//
//	@Test
//	public void TestI_updateSensorDataNotExistsException() {
//		// Setup
//		boolean expectedResult = false;
//		
//		try {
//			final SensorDao user = sensorEndpoint.findByName("SensorTest1");
//
//
//			try {
//				// Setup
//				final SensorInput1 sensorInput = new SensorInput1();
//				sensorInput.setName("SensorTest1");
//				sensorInput.setAddress("SensorAddress-modified");
//				sensorInput.setCapacity(BigInteger.valueOf(4));
//				sensorInput.setDesc("SensorDescription1");
//				sensorInput.setType(ESensorType.BOX);
//				final GatewayInput2 gateway = new GatewayInput2();
//				gateway.setId("1");
//				sensorInput.setGateway(gateway);
//
//				// Test
//				String wrongId = user.getColumnId() + "1";
//				sensorEndpoint.updateSensor(wrongId, sensorInput);
//
//			} catch (WebApplicationException e) {
//				expectedResult = true;
//			}
//
//		} catch(DataNotExistsException e ) {
//		}
//
//		// Asserts
//		assertEquals(true, expectedResult);	
//	}
//	
//	
//	@Test
//	public void TestJ_removeSensorDataNotExistsException() {
//		// Setup
//		boolean expectedResult = false;
//		try {
//			final SensorDao user = sensorEndpoint.findByName("SensorTest1");
//
//
//			try {
//				// Setup
//				String wrongId = user.getColumnId() + "1";
//
//				// Test
//				sensorEndpoint.removeSensor(wrongId);
//
//			} catch (WebApplicationException e) {
//				expectedResult = true;
//			}
//
//		} catch(DataNotExistsException e ) {
//		}
//
//		// Asserts
//		assertEquals(true, expectedResult);	
//	}
//
//
//	@Test
//	public void TestK_removeSensor() throws WebApplicationException {
//		// Setup
//		boolean expectedResult = false;
//		try {
//			final SensorDao user = sensorEndpoint.findByName("SensorTest1");
//
//
//			// Test
//			Response response = sensorEndpoint.removeSensor(user.getColumnId());
//
//			// Assert
//			assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
//			expectedResult = true;
//
//		} catch(DataNotExistsException e ) {
//		}
//		
//		// Assert
//		assertEquals(true, expectedResult);	
//	}


}
