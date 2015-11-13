package com.orange.flexoffice.adminui.ws.endPoint.entity.impl;

import static org.junit.Assert.*;

import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;


import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.orange.flexoffice.adminui.ws.endPoint.entity.GatewayEndpoint;
import com.orange.flexoffice.adminui.ws.endPoint.entity.RoomEndpoint;
import com.orange.flexoffice.adminui.ws.model.Room;
import com.orange.flexoffice.adminui.ws.model.RoomSummary;
import com.orange.flexoffice.adminui.ws.model.UserInput;
import com.orange.flexoffice.adminui.ws.model.UserSummary;


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

}
