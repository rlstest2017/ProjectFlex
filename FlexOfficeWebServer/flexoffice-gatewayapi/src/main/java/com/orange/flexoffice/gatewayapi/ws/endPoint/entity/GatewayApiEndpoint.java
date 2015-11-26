package com.orange.flexoffice.gatewayapi.ws.endPoint.entity;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.dao.common.model.object.GatewayDto;
import com.orange.flexoffice.gatewayapi.ws.model.GatewayInput;
import com.orange.flexoffice.gatewayapi.ws.model.GatewayReturn;
import com.orange.flexoffice.gatewayapi.ws.model.GatewaySummary;
import com.orange.flexoffice.gatewayapi.ws.model.Room;

import static com.orange.flexoffice.gatewayapi.ws.ParamsConst.GATEWAY_MAC_ADDRESS_PARAM;
import static com.orange.flexoffice.gatewayapi.ws.PathConst.GATEWAYS_PATH;
import static com.orange.flexoffice.gatewayapi.ws.PathConst.GATEWAY_ID_PATH;
import static com.orange.flexoffice.gatewayapi.ws.PathConst.INFO_PATH;

import java.util.List;

/**
 * Defines all operations available for a resource "gateway".
 */
@Path(GATEWAYS_PATH)
public interface GatewayApiEndpoint {
	
	/**
	 * Gets users.
	 * 
	 * @return user.
	 * 
	 * @see User
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	List<GatewaySummary> getGateways();
	
	/**
	 * Gets information on a specific gateway.
	 * 
	 * @param gatewayId
	 *            the gateway ID
	 * 
	 * @return information about a specific gateway.
	 * 
	 * @see Gateway
	 */
	@GET
	@Path(GATEWAY_ID_PATH  + INFO_PATH)
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	List<Room> getGateway(@PathParam(GATEWAY_MAC_ADDRESS_PARAM) String gatewayMacAddress);
	
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
	@Path(GATEWAY_ID_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	GatewayReturn updateGateway(@PathParam(GATEWAY_MAC_ADDRESS_PARAM)String macAddress, GatewayInput gateway);

	
	// used for tests
	boolean executeInitTestFile();

	// used for tests
	GatewayDto findByMacAddress(String macAddress)  throws DataNotExistsException;

	
}



