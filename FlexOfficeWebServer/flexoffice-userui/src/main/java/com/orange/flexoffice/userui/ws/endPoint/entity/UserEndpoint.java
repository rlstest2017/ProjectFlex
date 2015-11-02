package com.orange.flexoffice.userui.ws.endPoint.entity;


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
import javax.xml.bind.JAXBElement;

import static com.orange.flexoffice.userui.ws.ParamsConst.USER_ID_PARAM;
import static com.orange.flexoffice.userui.ws.PathConst.USERS_PATH;
import static com.orange.flexoffice.userui.ws.PathConst.USER_ID_PATH;

import com.orange.flexoffice.business.gatewayapi.exception.DataAlreadyExistsException;
import com.orange.flexoffice.userui.ws.model.XmlUser;

/**
 * Defines all operations available for a resource "user".
 */
@Path(USERS_PATH)
public interface UserEndpoint {

	/**
	 * Gets information on a specific user.
	 * 
	 * @param userId
	 *            the user ID
	 * 
	 * @return information about a specific user.
	 * 
	 * @see User
	 */
	@GET
	@Path(USER_ID_PATH)
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	XmlUser getUser(@PathParam(USER_ID_PARAM) String userId);

	/**
	 * Add a new user.
	 * 
	 * @param user
	 * 			  the new user
	 * @return If ok, a <code>Response</code> with a status code 201 Create & Location.
	 * 
	 * @see XmlUser
	 * @see Response
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	XmlUser addUser(XmlUser user) throws DataAlreadyExistsException;
	
	/**
	 * Modifies a specific user.
	 * To identify a user, a user id is required.
	 * 
	 * @param id
	 * 			  a user id. 
	 * @param user
	 *            the new <code>user</code>
	 * @return If ok, a <code>Response</code> with a status code 201 Create & Location.
	 * 
	 * @see XmlUser
	 * @see Response
	 */
	@PUT
	@Path(USER_ID_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	Response updateUser(@PathParam(USER_ID_PARAM)String id, XmlUser user);

	/**
	 * Deletes a specific user.
	 * To identify a user, a user id is required. 
	 * 
	 * @param id
	 * 			  a user id. 
	 * @return If ok, a <code>Response</code> with a status code 204 No Content.
	 * 
	 * @see Response
	 */
	@DELETE
	@Path(USER_ID_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	Response removeUser(@PathParam(USER_ID_PARAM)String id);
	
	
}



