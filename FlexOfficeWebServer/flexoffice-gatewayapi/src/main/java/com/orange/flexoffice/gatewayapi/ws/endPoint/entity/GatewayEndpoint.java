package com.orange.flexoffice.gatewayapi.ws.endPoint.entity;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.orange.flexoffice.gatewayapi.ws.model.GatewaySummary;
import com.orange.flexoffice.gatewayapi.ws.model.Room;

import static com.orange.flexoffice.gatewayapi.ws.ParamsConst.GATEWAY_ID_PARAM;
import static com.orange.flexoffice.gatewayapi.ws.PathConst.GATEWAYS_PATH;
import static com.orange.flexoffice.gatewayapi.ws.PathConst.GATEWAY_ID_PATH;
import static com.orange.flexoffice.gatewayapi.ws.PathConst.INFO_PATH;

import java.util.List;

/**
 * Defines all operations available for a resource "gateway".
 */
@Path(GATEWAYS_PATH)
public interface GatewayEndpoint {
	
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
	List<Room> getGateway(@PathParam(GATEWAY_ID_PARAM) String gatewayId);
	
	
}



