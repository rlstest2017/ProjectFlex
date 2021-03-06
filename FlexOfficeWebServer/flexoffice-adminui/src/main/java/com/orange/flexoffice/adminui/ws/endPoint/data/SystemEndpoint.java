package com.orange.flexoffice.adminui.ws.endPoint.data;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import static com.orange.flexoffice.adminui.ws.PathConst.SYSTEM_PATH;
import static com.orange.flexoffice.adminui.ws.PathConst.LOGIN_PATH;
import static com.orange.flexoffice.adminui.ws.PathConst.LOGOUT_PATH;
import static com.orange.flexoffice.adminui.ws.PathConst.CANCEL_PATH;
import static com.orange.flexoffice.adminui.ws.PathConst.SUBMIT_PATH;
import static com.orange.flexoffice.adminui.ws.PathConst.TEACHIN_PATH;

import java.util.List;

import static com.orange.flexoffice.adminui.ws.PathConst.INIT_PATH;
import static com.orange.flexoffice.adminui.ws.ParamsConst.AUTHORIZATION_HEADER_PARAM;
import static com.orange.flexoffice.adminui.ws.ParamsConst.ROOM_ID_PARAM;
import static com.orange.flexoffice.adminui.ws.ParamsConst.TOKEN_HEADER_PARAM;
import static com.orange.flexoffice.adminui.ws.ParamsConst.ORIGIN_HEADER_PARAM;

import com.orange.flexoffice.adminui.ws.model.Teachin;
import com.orange.flexoffice.adminui.ws.model.System;
/**
 * Defines all operations available for a resource "system".
 */
@Path(SYSTEM_PATH)
public interface SystemEndpoint {
	
	/**
	 * Gets system.
	 * 
	 * @return System.
	 * 
	 * @see System
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	com.orange.flexoffice.adminui.ws.model.System getSystem();
	
	/**
	 * Get accessToken.
	 * 
	 * 
	 * @return Token object.
	 * 
	 * @see TOken
	 */
	@GET
	@Path(LOGIN_PATH)
	@Produces(MediaType.APPLICATION_JSON)
	Response login(@HeaderParam(AUTHORIZATION_HEADER_PARAM) String auth, @HeaderParam(ORIGIN_HEADER_PARAM) String origin); 
	
	/**
	 * Delete Token from DB
	 */
	@GET
	@Path(LOGOUT_PATH)
	@Produces(MediaType.APPLICATION_JSON)
	Response logout(@HeaderParam(TOKEN_HEADER_PARAM) String auth);
	
	/**
	 * Get Teachin infos.
	 * 
	 * @return Teachin object
	 * 
	 * @see Teachin
	 */
	@GET
	@Path(TEACHIN_PATH)
	@Produces(MediaType.APPLICATION_JSON)
	Teachin getTeachin();

	/**
	 * Init Teachin for room.
	 * 
	 * @return Teachin object
	 * 
	 * @see Teachin
	 */
	@POST
	@Path(TEACHIN_PATH + INIT_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Response initTeachin(@HeaderParam(TOKEN_HEADER_PARAM) String auth, @QueryParam(ROOM_ID_PARAM) String roomId); 
	
	/**
	 * Cancel Teachin.
	 * 
	 * @return Response
	 * 
	 * @see Response
	 */
	@POST
	@Path(TEACHIN_PATH + CANCEL_PATH)
	@Produces(MediaType.APPLICATION_JSON)
	Response cancelTeachin(); 
	
	/**
	 * Cancel Teachin.
	 * 
	 * @return Response
	 * 
	 * @see Response
	 */
	@POST
	@Path(TEACHIN_PATH + SUBMIT_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Response submitTeachin(List<String> sensorIdentifiers);  
	
	@OPTIONS
	@Path("{path : .*}")
	Response options();
	
	// used for tests
	boolean executeInitTestFile();

	// used for tests
	boolean initTeachinSensorsTable();
	
	// used for tests
	boolean setTeachinSensorsTable();
	
}



