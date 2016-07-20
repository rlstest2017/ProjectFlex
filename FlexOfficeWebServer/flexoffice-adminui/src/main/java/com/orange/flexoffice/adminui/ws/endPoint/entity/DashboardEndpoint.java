package com.orange.flexoffice.adminui.ws.endPoint.entity;


import static com.orange.flexoffice.adminui.ws.ParamsConst.DASHBOARD_MAC_ADDRESS_PARAM;
import static com.orange.flexoffice.adminui.ws.PathConst.DASHBOARDS_PATH;
import static com.orange.flexoffice.adminui.ws.PathConst.DASHBOARD_MAC_ADDRESS_PATH;

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

import com.orange.flexoffice.adminui.ws.model.Dashboard;
import com.orange.flexoffice.adminui.ws.model.DashboardInput;
import com.orange.flexoffice.adminui.ws.model.DashboardInput2;
import com.orange.flexoffice.adminui.ws.model.DashboardSummary;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.dao.common.model.object.DashboardDto;

/**
 * Defines all operations available for a resource "dashboard".
 */
@Path(DASHBOARDS_PATH)
public interface DashboardEndpoint {

	/**
	 * Gets dashboards.
	 * 
	 * @return dashboard.
	 * 
	 * @see Dashboard
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	List<DashboardSummary> getDashboards();
	
	/**
	 * Gets information on a specific dashboard.
	 * 
	 * @param dashboardId  the dashboard ID
	 * 
	 * @return information about a specific dashboard.
	 * 
	 * @see Dashboard
	 */
	@GET
	@Path(DASHBOARD_MAC_ADDRESS_PATH)
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Dashboard getDashboard(@PathParam(DASHBOARD_MAC_ADDRESS_PARAM) String dashboardMacAddress);

	/**
	 * Add a new dashboard.
	 * 
	 * @param dashboard the new dashboard
	 * @return If ok, a <code>Response</code> with a status code 201 Create & Location.
	 * 
	 * @see XmlDashboard
	 * @see Response
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Dashboard addDashboard(DashboardInput dashboard);
	
	/**
	 * Modifies a specific dashboard.
	 * To identify a dashboard, a dashboard id is required.
	 * 
	 * @param id a dashboard id. 
	 * @param dashboard the new <code>dashboard</code>
	 * @return If ok, a <code>Response</code> with a status code 201 Create & Location.
	 * 
	 * @see XmlDashboard
	 * @see Response
	 */
	@PUT
	@Path(DASHBOARD_MAC_ADDRESS_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Response updateDashboard(@PathParam(DASHBOARD_MAC_ADDRESS_PARAM)String id, DashboardInput2 dashboard);

	/**
	 * Deletes a specific dashboard.
	 * To identify a dashboard, a dashboard id is required. 
	 * 
	 * @param id a dashboard id. 
	 * @return If ok, a <code>Response</code> with a status code 204 No Content.
	 * 
	 * @see Response
	 */
	@DELETE
	@Path(DASHBOARD_MAC_ADDRESS_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Response removeDashboard(@PathParam(DASHBOARD_MAC_ADDRESS_PARAM)String id);
	
	// used for tests
	boolean executeInitTestFile();
	
	// used for tests
	boolean executeDropTables();
		
	// used for tests
	DashboardDto findByMacAddress(String macAddress)  throws DataNotExistsException;
	
}



