package com.orange.flexoffice.userui.ws.endPoint.entity;


import static com.orange.flexoffice.userui.ws.ParamsConst.BUILDING_ID_PARAM;
import static com.orange.flexoffice.userui.ws.ParamsConst.CITY_ID_PARAM;
import static com.orange.flexoffice.userui.ws.ParamsConst.COUNTRY_ID_PARAM;
import static com.orange.flexoffice.userui.ws.ParamsConst.FLOOR_PARAM;
import static com.orange.flexoffice.userui.ws.ParamsConst.LATEST_PARAM;
import static com.orange.flexoffice.userui.ws.ParamsConst.REGION_ID_PARAM;
import static com.orange.flexoffice.userui.ws.ParamsConst.ROOM_ID_PARAM;
import static com.orange.flexoffice.userui.ws.ParamsConst.ROOM_KIND_PARAM;
import static com.orange.flexoffice.userui.ws.ParamsConst.TOKEN_HEADER_PARAM;
import static com.orange.flexoffice.userui.ws.PathConst.CANCEL_PATH;
import static com.orange.flexoffice.userui.ws.PathConst.RESERVE_PATH;
import static com.orange.flexoffice.userui.ws.PathConst.ROOMS_PATH;
import static com.orange.flexoffice.userui.ws.PathConst.ROOM_ID_PATH;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.orange.flexoffice.userui.ws.model.Room;
import com.orange.flexoffice.userui.ws.model.RoomSummary;

/**
 * Defines all operations available for a resource "user".
 */
@Path(ROOMS_PATH)
public interface RoomEndpoint {

	/**
	 * Gets rooms.
	 * 
	 * @return room summary list.
	 * 
	 * @see RoomSummary
	 */
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	List<RoomSummary> getRooms(@HeaderParam(TOKEN_HEADER_PARAM) String auth, @QueryParam(LATEST_PARAM) Boolean latest, @QueryParam(COUNTRY_ID_PARAM) String countryId
			, @QueryParam(REGION_ID_PARAM) String regionId, @QueryParam(CITY_ID_PARAM) String cityId, @QueryParam(BUILDING_ID_PARAM) String buildingId, @QueryParam(FLOOR_PARAM) Integer floor);

	/**
	 * Gets information on a specific room.
	 * 
	 * @param roomId the room ID
	 * @param kind kind of room (flexoffice/meetingroom)
	 * 
	 * @return information about a specific room.
	 * 
	 * @see Room
	 */
	@GET
	@Path(ROOM_ID_PATH)
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Room getRoom(@PathParam(ROOM_ID_PARAM) String roomId, @QueryParam(ROOM_KIND_PARAM) String kind);

	/**
	 * Reserve a room.
	 * 
	 * @param roomId
	 *            the room ID
	 *            
	 * @return If ok, a <code>Response</code> with a status code 200.
	 * 
	 * @see Response
	 */
	@POST
	@Path(ROOM_ID_PATH + RESERVE_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Room reserveRoom(@HeaderParam(TOKEN_HEADER_PARAM) String auth, @PathParam(ROOM_ID_PARAM) String roomId);

	/**
	 * Cancel reservation of a room.
	 * 
	 * @param roomId
	 *            the room ID
	 *            
	 * @return If ok, a <code>Response</code> with a status code 200.
	 * 
	 * @see Response
	 */
	@POST
	@Path(ROOM_ID_PATH + CANCEL_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Room cancelRoom(@HeaderParam(TOKEN_HEADER_PARAM) String auth, @PathParam(ROOM_ID_PARAM) String roomId);


	
	/** Initialise tests data in DB
	 * 
	 * @return true if successfully done
	 */
	boolean executeInitTestFile();
	
	/**
	 * Initialise RoomStats tests data in DB
	 * @return true if successfully done
	 */
	boolean initRoomStatsTable();
}



