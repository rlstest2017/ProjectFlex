package com.orange.flexoffice.adminui.ws.endPoint.entity.impl;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.orange.flexoffice.adminui.ws.endPoint.entity.GatewayEndpoint;
import com.orange.flexoffice.adminui.ws.endPoint.entity.RoomEndpoint;
import com.orange.flexoffice.adminui.ws.model.ERoomType;
import com.orange.flexoffice.adminui.ws.model.GatewayInput2;
import com.orange.flexoffice.adminui.ws.model.Room;
import com.orange.flexoffice.adminui.ws.model.RoomInput1;
import com.orange.flexoffice.adminui.ws.model.RoomOutput;
import com.orange.flexoffice.adminui.ws.model.RoomSummary;
import com.orange.flexoffice.adminui.ws.model.UserInput;
import com.orange.flexoffice.dao.common.model.data.RoomDao;
import com.orange.flexoffice.dao.common.model.data.UserDao;



public class RoomEndpointImplTest {

	private static ClassPathXmlApplicationContext context;

	private static RoomEndpoint roomEndpoint;

	private static GatewayEndpoint gatewayEndpoint;


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
	}


	@Test
	public void initTables() {
		// SetUp
		boolean state = gatewayEndpoint.executeGatewaysTestFile();

		// Asserts
		assertEquals(true, state);
	}


	@Test
	public void getRooms() {
		// Test
		List<RoomSummary> rooms = roomEndpoint.getRooms();

		// Asserts
		assertEquals(2, rooms.size());
	}

	@Test
	public void getRoomByRoomId1() {

		// Test
		Room room = roomEndpoint.getRoom("1");

		// Asserts
		assertEquals(2, room.getSensors().size());
	}

	@Test
	public void getRoomByRoomId2() {

		// Test
		Room room = roomEndpoint.getRoom("2");

		// Asserts
		assertEquals(1, room.getSensors().size());
	}



	@Test
	public void getWrongRoomDataNotExistsException() {
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
	public void addRoom() throws WebApplicationException {
		// Setup
		final RoomInput1 roomInput = new RoomInput1();
		roomInput.setName("RoomTest1");
		roomInput.setAddress("RoomAddress1");
		roomInput.setCapacity(BigInteger.valueOf(4));
		roomInput.setDesc("RoomDescription1");
		roomInput.setType(ERoomType.BOX);
		final GatewayInput2 gateway = new GatewayInput2();
		gateway.setId("1");
		roomInput.setGateway(gateway);

		// Test
		final RoomOutput response = roomEndpoint.addRoom(roomInput);

		// Asserts
		assertNotNull(response.getId());


		// Test
		List<RoomSummary> rooms = roomEndpoint.getRooms();

		// Asserts
		assertEquals(3, rooms.size());
	}

	@Test
	public void addRoomAlreadyExists() {
		// Setup
		boolean expectedResult = false;
		final RoomInput1 roomInput = new RoomInput1();
		roomInput.setName("RoomTest1");
		roomInput.setAddress("RoomAddress1");
		roomInput.setCapacity(BigInteger.valueOf(4));
		roomInput.setDesc("RoomDescription1");
		roomInput.setType(ERoomType.BOX);
		final GatewayInput2 gateway = new GatewayInput2();
		gateway.setId("1");
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
	public void updateRoom() throws WebApplicationException {
		// Setup
		final RoomDao user = roomEndpoint.findByName("RoomTest1");

		if (user != null) {

			// Setup
			final RoomInput1 roomInput = new RoomInput1();
			roomInput.setName("RoomTest1");
			roomInput.setAddress("RoomAddress-modified");
			roomInput.setCapacity(BigInteger.valueOf(4));
			roomInput.setDesc("RoomDescription-modified");
			roomInput.setType(ERoomType.BOX);
			final GatewayInput2 gateway = new GatewayInput2();
			gateway.setId("1");
			roomInput.setGateway(gateway);

			// Test
			final Response response = roomEndpoint.updateRoom(user.getColumnId(), roomInput);

			// Asserts
			assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());


			// Test
			List<RoomSummary> rooms = roomEndpoint.getRooms();

			// Asserts
			assertEquals(3, rooms.size());
		}
	}
	


	@Test
	public void updateRoomDataNotExistsException() {
		// Setup
		boolean expectedResult = false;
		// Setup
		final RoomDao user = roomEndpoint.findByName("RoomTest1");

		if (user != null) {

			try {
				// Setup
				final RoomInput1 roomInput = new RoomInput1();
				roomInput.setName("RoomTest1");
				roomInput.setAddress("RoomAddress-modified");
				roomInput.setCapacity(BigInteger.valueOf(4));
				roomInput.setDesc("RoomDescription1");
				roomInput.setType(ERoomType.BOX);
				final GatewayInput2 gateway = new GatewayInput2();
				gateway.setId("1");
				roomInput.setGateway(gateway);

				// Test
				String wrongId = user.getColumnId() + "1";
				roomEndpoint.updateRoom(wrongId, roomInput);

			} catch (WebApplicationException e) {
				expectedResult = true;
			}
		}

		// Asserts
		assertEquals(true, expectedResult);	
	}
	
	
	@Test
	public void removeRoomDataNotExistsException() {
		// Setup
		boolean expectedResult = false;
		final RoomDao user = roomEndpoint.findByName("RoomTest1");

		if (user != null) {

			try {
				// Setup
				String wrongId = user.getColumnId() + "1";

				// Test
				roomEndpoint.removeRoom(wrongId);

			} catch (WebApplicationException e) {
				expectedResult = true;
			}
		}

		// Asserts
		assertEquals(true, expectedResult);	
	}


	@Test
	public void removeRoom() throws WebApplicationException {
		// Setup
		boolean expectedResult = false;
		final RoomDao user = roomEndpoint.findByName("RoomTest1");

		if (user != null) {

			// Test
			Response response = roomEndpoint.removeRoom(user.getColumnId());

			// Assert
			assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
			expectedResult = true;
		}
		
		// Assert
		assertEquals(true, expectedResult);	
	}


}
