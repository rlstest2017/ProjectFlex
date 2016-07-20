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
import com.orange.flexoffice.adminui.ws.endPoint.entity.RoomEndpoint;
import com.orange.flexoffice.adminui.ws.model.ERoomType;
import com.orange.flexoffice.adminui.ws.model.GatewayInput2;
import com.orange.flexoffice.adminui.ws.model.Room;
import com.orange.flexoffice.adminui.ws.model.RoomInput1;
import com.orange.flexoffice.adminui.ws.model.RoomOutput;
import com.orange.flexoffice.adminui.ws.model.RoomSummary;
import com.orange.flexoffice.adminui.ws.task.AdminUiTasks;
import com.orange.flexoffice.adminui.ws.utils.InitOfflineGatewaysAlerts;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.dao.common.model.data.RoomDao;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RoomEndpointImplTest {

	static {
	    try {
	      Log4jConfigurer.initLogging( "classpath:log4j-flexoffice-adminui-test.xml" );
	    }
	    catch( FileNotFoundException ex ) {
	      System.err.println( "Cannot Initialize log4j" );
	    }
	  }

	private static ClassPathXmlApplicationContext context;

	private static RoomEndpoint roomEndpoint;

	private static GatewayEndpoint gatewayEndpoint;

	private static AdminUiTasks adminUiTasks;
	
	private static InitOfflineGatewaysAlerts initOfflineGatewaysAlerts;

	@Context
	private UriInfo uriInfo;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void initSpringContextAndDatabase() throws Exception {
		context = new ClassPathXmlApplicationContext("applicationContext-flexoffice-adminui-test.xml");
		roomEndpoint = (RoomEndpointImpl)context.getBean("roomEndpoint");
		gatewayEndpoint = (GatewayEndpointImpl)context.getBean("gatewayEndpoint");
		adminUiTasks = (AdminUiTasks)context.getBean("adminUiTasks");
		initOfflineGatewaysAlerts = (InitOfflineGatewaysAlerts)context.getBean("initOfflineGatewaysAlerts");
	}


	@Test
	public void TestA_initTables() {
		// SetUp
		boolean state = gatewayEndpoint.executeInitTestFile();

		// Asserts
		assertEquals(true, state);
	}


	@Test
	public void TestB_getRooms() {
		// Test
		List<RoomSummary> rooms = roomEndpoint.getRooms();

		// Asserts
		assertEquals(5, rooms.size());
	}

	@Test
	public void TestC_getRoomByRoomId1() {

		// Test
		Room room = roomEndpoint.getRoom("1");

		// Asserts
		assertEquals(2, room.getSensors().size());
	}

	@Test
	public void TestD_getRoomByRoomId2() {

		// Test
		Room room = roomEndpoint.getRoom("2");

		// Asserts
		assertEquals(1, room.getSensors().size());
	}



	@Test
	public void TestE_getWrongRoomDataNotExistsException() {
		// Setup
		boolean expectedResult = false;

		// Test
		try {
			// Test
			roomEndpoint.getRoom("125");

		} catch (WebApplicationException e) {
			expectedResult = true;
		}

		// Asserts
		assertEquals(true, expectedResult);	
	}

	@Test
	public void TestF_addRoom() throws WebApplicationException {
		// Setup
		final RoomInput1 roomInput = new RoomInput1();
		roomInput.setName("RoomTest1");
		roomInput.setBuildingId("1");
		roomInput.setFloor(BigInteger.valueOf(5));
		roomInput.setCapacity(BigInteger.valueOf(4));
		roomInput.setDesc("RoomDescription1");
		roomInput.setType(ERoomType.BOX);
		roomInput.setBuildingId("1");
		final GatewayInput2 gateway = new GatewayInput2();
		gateway.setMacAddress("FF:EE:ZZ:AA:GG:PP");
		roomInput.setGateway(gateway);

		// Test
		final RoomOutput response = roomEndpoint.addRoom(roomInput);

		// Asserts
		assertNotNull(response.getId());


		// Test
		List<RoomSummary> rooms = roomEndpoint.getRooms();

		// Asserts
		assertEquals(6, rooms.size());
	}

	@Test
	public void TestG_addRoomAlreadyExists() {
		// Setup
		boolean expectedResult = false;
		final RoomInput1 roomInput = new RoomInput1();
		roomInput.setName("RoomTest1");
		roomInput.setBuildingId("1");
		roomInput.setCapacity(BigInteger.valueOf(4));
		roomInput.setDesc("RoomDescription1");
		roomInput.setType(ERoomType.BOX);
		roomInput.setBuildingId("1");
		final GatewayInput2 gateway = new GatewayInput2();
		gateway.setMacAddress("FF:EE:ZZ:AA:GG:PP");
		roomInput.setGateway(gateway);

		try {
			// Test
			roomEndpoint.addRoom(roomInput);

		} catch (WebApplicationException e) {
			expectedResult = true;
		}
		// Asserts
		assertEquals(true, expectedResult);	
	}

	@Test
	public void TestH_updateRoom() throws WebApplicationException {

		// Setup
		boolean expectedResult = false;
		
		try {
			RoomDao user = roomEndpoint.findByName("RoomTest1");

			// Setup
			final RoomInput1 roomInput = new RoomInput1();
			roomInput.setName("RoomTest1");
			roomInput.setBuildingId("2");
			roomInput.setFloor(BigInteger.valueOf(15));
			roomInput.setCapacity(BigInteger.valueOf(4));
			roomInput.setDesc("RoomDescription-modified");
			roomInput.setType(ERoomType.BOX);
			final GatewayInput2 gateway = new GatewayInput2();
			gateway.setMacAddress("FF:EE:ZZ:AA:GG:PP");
			roomInput.setGateway(gateway);

			// Test
			final Response response = roomEndpoint.updateRoom(user.getColumnId(), roomInput);

			// Asserts
			assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());


			// Test
			List<RoomSummary> rooms = roomEndpoint.getRooms();

			// Asserts
			assertEquals(6, rooms.size());
			expectedResult = true;
			
		} catch(DataNotExistsException e ) {
		}

		// Asserts
		assertEquals(true, expectedResult);	
	}
	


	@Test
	public void TestI_updateRoomDataNotExistsException() {
		// Setup
		boolean expectedResult = false;
		
		try {
			final RoomDao user = roomEndpoint.findByName("RoomTest1");


			try {
				// Setup
				final RoomInput1 roomInput = new RoomInput1();
				roomInput.setName("RoomTest1");
				roomInput.setBuildingId("2");
				roomInput.setFloor(BigInteger.valueOf(15));
				roomInput.setCapacity(BigInteger.valueOf(4));
				roomInput.setDesc("RoomDescription1");
				roomInput.setType(ERoomType.BOX);
				final GatewayInput2 gateway = new GatewayInput2();
				gateway.setMacAddress("FF:EE:ZZ:AA:GG:PP");
				roomInput.setGateway(gateway);

				// Test
				String wrongId = user.getColumnId() + "1";
				roomEndpoint.updateRoom(wrongId, roomInput);

			} catch (WebApplicationException e) {
				expectedResult = true;
			}

		} catch(DataNotExistsException e ) {
		}

		// Asserts
		assertEquals(true, expectedResult);	
	}
	
	
	@Test
	public void TestJ_removeRoomDataNotExistsException() {
		// Setup
		boolean expectedResult = false;
		try {
			final RoomDao user = roomEndpoint.findByName("RoomTest1");


			try {
				// Setup
				String wrongId = user.getColumnId() + "1";

				// Test
				roomEndpoint.removeRoom(wrongId);

			} catch (WebApplicationException e) {
				expectedResult = true;
			}

		} catch(DataNotExistsException e ) {
		}

		// Asserts
		assertEquals(true, expectedResult);	
	}


	@Test
	public void TestK_removeRoom() throws WebApplicationException {
		// Setup
		boolean expectedResult = false;
		try {
			final RoomDao user = roomEndpoint.findByName("RoomTest1");


			// Test
			Response response = roomEndpoint.removeRoom(user.getColumnId());

			// Assert
			assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
			expectedResult = true;

		} catch(DataNotExistsException e ) {
		}
		
		// Assert
		assertEquals(true, expectedResult);	
	}

	@Test
	public void TestL_removeRoomHasSensors()  {
		// Setup
		boolean expectedResult = false;
		String id  = "1";
		
		try {
			// Test
			roomEndpoint.removeRoom(id);
			
		} catch(WebApplicationException e ) {
			expectedResult = true;
		}
		
		// Assert
		assertEquals(true, expectedResult);	
	}
	
	@Test
	public void TestM_removeRoomIsReserved()  {
		// Setup
		boolean expectedResult = false;
		String id  = "2";
		
		try {
			// Test
			roomEndpoint.removeRoom(id);
			
		} catch(WebApplicationException e ) {
			expectedResult = true;
		}
		
		// Assert
		assertEquals(true, expectedResult);	
	}

	@Test
	public void TestN_initRoomStatsTable() {
		// SetUp
		boolean state = roomEndpoint.initRoomStatsTable();

		// Asserts
		assertEquals(true, state);
	}

	@Test
	public void TestO_processDailyStatsTask() {
		// SetUp
		boolean state = adminUiTasks.processDailyStatsTestMethod();

		// Asserts
		assertEquals(true, state);
	}
	
	@Test
	public void TestP_purgeStatsTask() {
		// SetUp
		boolean state = adminUiTasks.purgeStatsDataTestMethod();

		// Asserts
		assertEquals(true, state);
	}
	
	@Test
	public void TestQ_updateOFFLINEGatewaysAlerts() {
		// SetUp
		boolean state = initOfflineGatewaysAlerts.updateOFFLINEGatewaysAlertsForTest();

		// Asserts
		assertEquals(true, state);
	}
	
}
