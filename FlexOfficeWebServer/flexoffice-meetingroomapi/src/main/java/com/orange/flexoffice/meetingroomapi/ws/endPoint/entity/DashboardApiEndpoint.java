package com.orange.flexoffice.meetingroomapi.ws.endPoint.entity;

import static com.orange.flexoffice.meetingroomapi.ws.ParamsConst.DASHBOARD_MAC_ADDRESS_PARAM;
import static com.orange.flexoffice.meetingroomapi.ws.PathConst.CONFIG_PATH;
import static com.orange.flexoffice.meetingroomapi.ws.PathConst.DASHBOARDS_PATH;
import static com.orange.flexoffice.meetingroomapi.ws.PathConst.DASHBOARD_ID_PATH;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.orange.flexoffice.meetingroomapi.ws.model.DashboardInput;
import com.orange.flexoffice.meetingroomapi.ws.model.DashboardOutput;

/**
 * Defines all operations available for a resource "dashboard".
 */
@Path(DASHBOARDS_PATH)
public interface DashboardApiEndpoint {
	
	
	/**
	 * get list of configuration files names for PHP requests (GroupRoomID)
	 * 
	 * @return String
	 * 
	 */
	@GET
	@Path(DASHBOARD_ID_PATH + CONFIG_PATH)
	@Produces(MediaType.APPLICATION_JSON)
	List<String> getConfig(@PathParam(DASHBOARD_MAC_ADDRESS_PARAM)String macAddress);

	
	/**
	 * update dashboard status
	 * 
	 * @param id a dashboard id. 
	 * @param the updated dashbaord <code>dashboard</code>
	 *            
	 * @return If ok, a <code>DashboardOutput</code> with a status code 201.
	 * 
	 * @see DashboardOutput
	 */
	@PUT
	@Path(DASHBOARD_ID_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	DashboardOutput updateStatus(@PathParam(DASHBOARD_MAC_ADDRESS_PARAM)String identifier, DashboardInput dashboard);

}



