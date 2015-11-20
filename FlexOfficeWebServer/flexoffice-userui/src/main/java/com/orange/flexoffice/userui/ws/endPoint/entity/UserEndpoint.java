package com.orange.flexoffice.userui.ws.endPoint.entity;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static com.orange.flexoffice.userui.ws.PathConst.USERS_PATH;
import static com.orange.flexoffice.userui.ws.PathConst.USER_ID_PATH;

import com.orange.flexoffice.userui.ws.model.UserSummary;

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
	@Produces(MediaType.APPLICATION_JSON)
	UserSummary getUserCurrent();


	
	/** Initialize tests data in DB
	 * 
	 * @return true if successfully done
	 */
	boolean executeInitTestFile();
}



