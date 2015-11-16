package com.orange.flexoffice.adminui.ws.endPoint.entity;


import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.orange.flexoffice.adminui.ws.ParamsConst.GATEWAY_MAC_ADDRESS_PARAM;
import static com.orange.flexoffice.adminui.ws.ParamsConst.ROOM_ID_PARAM;
import static com.orange.flexoffice.adminui.ws.PathConst.GATEWAY_MAC_ADDRESS_PATH;
import static com.orange.flexoffice.adminui.ws.PathConst.ROOMS_PATH;
import static com.orange.flexoffice.adminui.ws.PathConst.ROOM_ID_PATH;

import java.util.List;

import com.orange.flexoffice.adminui.ws.model.GatewayInput;
import com.orange.flexoffice.adminui.ws.model.GatewayOutput;
import com.orange.flexoffice.adminui.ws.model.Room;
import com.orange.flexoffice.adminui.ws.model.RoomInput1;
import com.orange.flexoffice.adminui.ws.model.RoomOutput;
import com.orange.flexoffice.adminui.ws.model.RoomSummary;
import com.orange.flexoffice.adminui.ws.model.User;
import com.orange.flexoffice.dao.common.model.data.RoomDao;
import com.orange.flexoffice.dao.common.model.data.UserDao;

/**
 * Defines all operations available for a resource "room".
 */
@Path(ROOMS_PATH)
public interface RoomEndpoint {
	
	/**
	 * Gets users.
	 * 
	 * @return user.
	 * 
	 * @see User
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	List<RoomSummary> getRooms();
	
	/**
	 * Gets information on a specific room.
	 * 
	 * @param roomId
	 *            the room ID
	 * 
	 * @return information about a specific room.
	 * 
	 * @see Room
	 */
	@GET
	@Path(ROOM_ID_PATH)
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Room getRoom(@PathParam(ROOM_ID_PARAM) String roomId);

	
	
	/**
	 * Add a new room.
	 * 
	 * @param room
	 * 			  the new room
	 * @return If ok, a <code>Response</code> with a status code 201 Create & Location.
	 * 
	 * @see Room
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RoomOutput addRoom(RoomInput1 room);
	
	/**
	 * Modifies a specific room.
	 * To identify a room, a room id is required.
	 * 
	 * @param id
	 * 			  a room id. 
	 * @param room
	 *            the new <code>room</code>
	 * @return If ok, a <code>Response</code> with a status code 201 Create & Location.
	 * 
	 * @see Response
	 */
	@PUT
	@Path(GATEWAY_MAC_ADDRESS_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Response updateRoom(@PathParam(ROOM_ID_PARAM)String id, RoomInput1 room);

	/**
	 * Deletes a specific room.
	 * To identify a room, a room id is required. 
	 * 
	 * @param id
	 * 			  a room id. 
	 * @return If ok, a <code>Response</code> with a status code 204 No Content.
	 * 
	 * @see Response
	 */
	@DELETE
	@Path(GATEWAY_MAC_ADDRESS_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Response removeRoom(@PathParam(ROOM_ID_PARAM)String id);

	
	// Used for tests
	RoomDao findByName(String name);
}



