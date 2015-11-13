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

import static com.orange.flexoffice.adminui.ws.ParamsConst.GATEWAY_ID_PARAM;
import static com.orange.flexoffice.adminui.ws.PathConst.GATEWAYS_PATH;
import static com.orange.flexoffice.adminui.ws.PathConst.GATEWAY_ID_PATH;

import java.util.List;

import com.orange.flexoffice.adminui.ws.model.Gateway;
import com.orange.flexoffice.adminui.ws.model.GatewayInput1;
import com.orange.flexoffice.adminui.ws.model.GatewayInput2;
import com.orange.flexoffice.adminui.ws.model.GatewayOutput;
import com.orange.flexoffice.adminui.ws.model.GatewayOutput2;
import com.orange.flexoffice.adminui.ws.model.GatewaySummary;

/**
 * Defines all operations available for a resource "gateway".
 */
@Path(GATEWAYS_PATH)
public interface GatewayEndpoint {
	
	/**
	 * Gets gateways.
	 * 
	 * @return List<GatewaySummary>.
	 * 
	 * @see GatewaySummary
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
	@Path(GATEWAY_ID_PATH)
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	GatewayOutput2 getGateway(@PathParam(GATEWAY_ID_PARAM) String gatewayId);
	
	/**
	 * Add a new gateway.
	 * 
	 * @param gateway
	 * 			  the new gateway
	 * @return If ok, a <code>Response</code> with a status code 201 Create & Location.
	 * 
	 * @see GatewayOutput
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	GatewayOutput addGateway(GatewayInput1 gateway);
	
	/**
	 * Modifies a specific gateway.
	 * To identify a gateway, a gateway id is required.
	 * 
	 * @param id
	 * 			  a gateway id. 
	 * @param gateway
	 *            the new <code>gateway</code>
	 * @return If ok, a <code>Response</code> with a status code 201 Create & Location.
	 * 
	 * @see Response
	 */
	@PUT
	@Path(GATEWAY_ID_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Response updateGateway(@PathParam(GATEWAY_ID_PARAM)String id, GatewayInput2 gateway);

	/**
	 * Deletes a specific gateway.
	 * To identify a gateway, a gateway id is required. 
	 * 
	 * @param id
	 * 			  a gateway id. 
	 * @return If ok, a <code>Response</code> with a status code 204 No Content.
	 * 
	 * @see Response
	 */
	@DELETE
	@Path(GATEWAY_ID_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Response removeGateway(@PathParam(GATEWAY_ID_PARAM)String id);
	
	// used for tests
	boolean executeGatewaysTestFile();
	
}



