package com.orange.meetingroom.gui.ws.endPoint.entity;


import static com.orange.meetingroom.gui.ws.ParamsConst.DASHBOARD_MAC_ADDRESS_PARAM;
import static com.orange.meetingroom.gui.ws.PathConst.DASHBOARDS_PATH;
import static com.orange.meetingroom.gui.ws.PathConst.DASHBOARD_MAC_ADDRESS_PATH;

import javax.ws.rs.Consumes;
//import javax.ws.rs.OPTIONS;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.orange.meetingroom.gui.ws.model.DashboardInput;
import com.orange.meetingroom.gui.ws.model.DashboardOutput;

/**
 * Defines all operations available for a resource "dashboard".
 */
@Path(DASHBOARDS_PATH)
public interface DashboardEndpoint {

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
	@Path(DASHBOARD_MAC_ADDRESS_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	DashboardOutput updateDashboard(@PathParam(DASHBOARD_MAC_ADDRESS_PARAM)String macAddress, DashboardInput dashboardInput);
	
}



