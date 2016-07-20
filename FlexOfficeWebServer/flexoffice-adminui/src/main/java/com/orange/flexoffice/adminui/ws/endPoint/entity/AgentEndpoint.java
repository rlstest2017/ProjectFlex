package com.orange.flexoffice.adminui.ws.endPoint.entity;


import static com.orange.flexoffice.adminui.ws.ParamsConst.AGENT_MAC_ADDRESS_PARAM;
import static com.orange.flexoffice.adminui.ws.PathConst.AGENTS_PATH;
import static com.orange.flexoffice.adminui.ws.PathConst.AGENT_MAC_ADDRESS_PATH;

import java.util.List;

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

import com.orange.flexoffice.adminui.ws.model.Agent;
import com.orange.flexoffice.adminui.ws.model.AgentInput;
import com.orange.flexoffice.adminui.ws.model.AgentInput2;
import com.orange.flexoffice.adminui.ws.model.AgentOutput;
import com.orange.flexoffice.adminui.ws.model.AgentSummary;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.dao.common.model.object.AgentDto;

/**
 * Defines all operations available for a resource "agent".
 */
@Path(AGENTS_PATH)
public interface AgentEndpoint {

	/**
	 * Gets agents.
	 * 
	 * @return agent.
	 * 
	 * @see Agent
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	List<AgentSummary> getAgents();
	
	/**
	 * Gets information on a specific agent.
	 * 
	 * @param agentId  the agent ID
	 * 
	 * @return information about a specific agent.
	 * 
	 * @see Agent
	 */
	@GET
	@Path(AGENT_MAC_ADDRESS_PATH)
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Agent getAgent(@PathParam(AGENT_MAC_ADDRESS_PARAM) String agentMacAddress);

	/**
	 * Add a new agent.
	 * 
	 * @param agent the new agent
	 * @return If ok, a <code>Response</code> with a status code 201 Create & Location.
	 * 
	 * @see XmlAgent
	 * @see Response
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	AgentOutput addAgent(AgentInput agent);
	
	/**
	 * Modifies a specific agent.
	 * To identify an agent, an agent id is required.
	 * 
	 * @param id an agent id. 
	 * @param agent the new <code>agent</code>
	 * @return If ok, a <code>Response</code> with a status code 201 Create & Location.
	 * 
	 * @see XmlAgent
	 * @see Response
	 */
	@PUT
	@Path(AGENT_MAC_ADDRESS_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Response updateAgent(@PathParam(AGENT_MAC_ADDRESS_PARAM)String id, AgentInput2 agent);

	/**
	 * Deletes a specific agent.
	 * To identify an agent, an agent id is required. 
	 * 
	 * @param id an agent id. 
	 * @return If ok, a <code>Response</code> with a status code 204 No Content.
	 * 
	 * @see Response
	 */
	@DELETE
	@Path(AGENT_MAC_ADDRESS_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Response removeAgent(@PathParam(AGENT_MAC_ADDRESS_PARAM)String id);
	
	// used for tests
	boolean executeInitTestFile();
	
	// used for tests
	boolean executeDropTables();
		
	// used for tests
	AgentDto findByMacAddress(String macAddress)  throws DataNotExistsException;
}



