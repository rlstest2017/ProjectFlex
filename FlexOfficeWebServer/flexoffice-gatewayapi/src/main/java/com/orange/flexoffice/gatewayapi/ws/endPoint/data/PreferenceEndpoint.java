package com.orange.flexoffice.gatewayapi.ws.endPoint.data;

import static com.orange.flexoffice.gatewayapi.ws.ParamsConst.PREFERENCE_ID_PARAM;
import static com.orange.flexoffice.gatewayapi.ws.PathConst.PREFERENCES_PATH;
import static com.orange.flexoffice.gatewayapi.ws.PathConst.PREFERENCE_ID_PATH;

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

import com.orange.flexoffice.business.gatewayapi.exception.DataAlreadyExistsException;
import com.orange.flexoffice.gatewayapi.ws.model.XmlPreference;

/**
 * Specifies all operations available on the preference resource.
 * 
 * Each preference is identified thanks a user ID, a descriptor Type and a
 * descriptor Value.
 */
@Path(PREFERENCES_PATH)
public interface PreferenceEndpoint {

	/**
	 * Gets information on a specific preference.
	 * 
	 * @param id
	 * 		  a preference ID
	 * 
	 * @return a {@link Preference}
	 */
	@GET
	@Path(PREFERENCE_ID_PATH)
	@Produces(MediaType.APPLICATION_XML)
	JAXBElement<XmlPreference> getPreference(@PathParam(PREFERENCE_ID_PARAM) String id);

	/**
	 * Creates a new preference.
	 * 
	 * @param preference
	 *            the new <code>preference</code>
	 * @return If ok, a <code>Response</code> with a status code 201 Create &
	 *         Location.
	 * 
	 * @see XmlPreference
	 * @see Response
	 */
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	Response addPreference(XmlPreference preference) throws DataAlreadyExistsException;

	/**
	 * Modifies a specific preference.
	 * 
	 * @param id
	 * 		  a preference ID
	 * @param preference
	 * 		  a new <code>Preference</code>
	 * @return If ok, a <code>Response</code> with a status code 201 Create & Location.
	 *
	 * @see Response
	 * @see XmlPreference
	 */
	@PUT
	@Path(PREFERENCE_ID_PATH)
	@Consumes(MediaType.APPLICATION_XML)
	Response updatePreference(
			@PathParam(PREFERENCE_ID_PARAM) String id,
			XmlPreference preference);

	/**
	 * Removes a specific preference.
	 * 
	 * @param id
	 * 		  a preference ID
	 * @return If ok, a <code>Response</code> with a status code 204 No Content.
	 * 
	 * @see Response
	 */
	@DELETE
	@Path(PREFERENCE_ID_PATH)
	@Consumes(MediaType.APPLICATION_XML)
	Response removePreference(
			@PathParam(PREFERENCE_ID_PARAM) String id);

}
