package com.orange.flexoffice.userui.ws.endPoint.entity;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.orange.flexoffice.userui.ws.PathConst.SYSTEM_PATH;
import static com.orange.flexoffice.userui.ws.PathConst.USER_PATH;
//import static com.orange.flexoffice.userui.ws.ParamsConst.AUTHORIZATION_HEADER_PARAM;
import static com.orange.flexoffice.userui.ws.ParamsConst.ORIGIN_HEADER_PARAM;
import static com.orange.flexoffice.userui.ws.ParamsConst.TOKEN_HEADER_PARAM;
import static com.orange.flexoffice.userui.ws.PathConst.LOGIN_PATH;
import static com.orange.flexoffice.userui.ws.PathConst.LOGOUT_PATH;
import static com.orange.flexoffice.userui.ws.PathConst.CURRENT_PATH;

import com.orange.flexoffice.userui.ws.model.UserInput;
import com.orange.flexoffice.userui.ws.model.UserSummary;

/**
 * Defines all operations available for a resource "user".
 */
@Path(SYSTEM_PATH)
public interface UserEndpoint {

	/**
	 * Gets information on a current user.
	 * 
	 * @return information about a specific user.
	 * 
	 * @see User
	 */
	@GET
	@Path(USER_PATH + CURRENT_PATH)
	@Produces(MediaType.APPLICATION_JSON)
	UserSummary getUserCurrent(@HeaderParam(TOKEN_HEADER_PARAM) String auth);
	
	/**
	 * Get accessToken.
	 * security="none", not filtered by spring-security, then I must to check origin header parameter 
	 * @return Token object.
	 * 
	 * @see TOken
	 */
	@POST
	@Path(LOGIN_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Response login(/*@HeaderParam(AUTHORIZATION_HEADER_PARAM) String auth,*/ @HeaderParam(ORIGIN_HEADER_PARAM) String origin, UserInput user); 
	
	/**
	 * Delete Token from DB
	 */
	@GET
	@Path(LOGOUT_PATH)
	@Produces(MediaType.APPLICATION_JSON)
	Response logout(@HeaderParam(TOKEN_HEADER_PARAM) String auth);

	@OPTIONS
	@Path("{path : .*}")
	Response options();
	
	/** Initialize tests data in DB
	 * 
	 * @return true if successfully done
	 */
	boolean executeInitTestFile();
}



