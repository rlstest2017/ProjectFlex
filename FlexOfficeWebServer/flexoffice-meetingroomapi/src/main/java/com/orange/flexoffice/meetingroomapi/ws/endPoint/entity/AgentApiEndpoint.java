package com.orange.flexoffice.meetingroomapi.ws.endPoint.entity;

import static com.orange.flexoffice.meetingroomapi.ws.ParamsConst.AGENT_MAC_ADDRESS_PARAM;
import static com.orange.flexoffice.meetingroomapi.ws.PathConst.AGENTS_PATH;
import static com.orange.flexoffice.meetingroomapi.ws.PathConst.AGENT_ID_PATH;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.meetingroomapi.ws.model.AgentInput;
import com.orange.flexoffice.meetingroomapi.ws.model.AgentOutput;

/**
 * Defines all operations available for a resource "agent".
 */
@Path(AGENTS_PATH)
public interface AgentApiEndpoint {
	
	/**
	 * put agent status
	 * 
	 * @param id an agent id. 
	 * @param user the new <code>agent</code>
	 * @return If ok, a <code>Response</code> with a status code 201
	 * @throws DataNotExistsException 
	 * 
	 * @see AgentOutput
	 */
	@PUT
	@Path(AGENT_ID_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	AgentOutput updateStatus(@PathParam(AGENT_MAC_ADDRESS_PARAM)String macAddress, AgentInput agent) throws DataNotExistsException;

	
	// used for tests
	boolean executeInitTestFile();
}



