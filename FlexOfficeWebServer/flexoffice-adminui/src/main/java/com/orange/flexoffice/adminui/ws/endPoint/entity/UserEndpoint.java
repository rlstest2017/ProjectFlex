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

import static com.orange.flexoffice.adminui.ws.ParamsConst.USER_ID_PARAM;
import static com.orange.flexoffice.adminui.ws.PathConst.USERS_PATH;
import static com.orange.flexoffice.adminui.ws.PathConst.USER_ID_PATH;

import java.util.List;
import com.orange.flexoffice.adminui.ws.model.User;
import com.orange.flexoffice.adminui.ws.model.UserSummary;

/**
 * Defines all operations available for a resource "user".
 */
@Path(USERS_PATH)
public interface UserEndpoint {

	/**
	 * Gets users.
	 * 
	 * @return user.
	 * 
	 * @see User
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	List<UserSummary> getUsers();
	
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
	User getUser(@PathParam(USER_ID_PARAM) String userId);

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
	User addUser(User user);
	
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
	@Produces(MediaType.APPLICATION_JSON)
	Response updateUser(@PathParam(USER_ID_PARAM)String id, User user);

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
	@Produces(MediaType.APPLICATION_JSON)
	Response removeUser(@PathParam(USER_ID_PARAM)String id);
	
	
}



