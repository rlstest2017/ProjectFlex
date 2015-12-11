package com.orange.flexoffice.adminui.ws.endPoint.data;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import static com.orange.flexoffice.adminui.ws.PathConst.ROOMS_PATH;
import static com.orange.flexoffice.adminui.ws.PathConst.OCCUPANCY_PATH;
import static com.orange.flexoffice.adminui.ws.PathConst.POPULAR_PATH;
import static com.orange.flexoffice.adminui.ws.PathConst.STATS_PATH;
import static com.orange.flexoffice.adminui.ws.ParamsConst.FROM_PARAM;
import static com.orange.flexoffice.adminui.ws.ParamsConst.TO_PARAM;
import static com.orange.flexoffice.adminui.ws.ParamsConst.VIEW_TYPE_PARAM;

import java.util.List;

import com.orange.flexoffice.adminui.ws.model.MultiStatSet;
import com.orange.flexoffice.adminui.ws.model.SimpleStat;

/**
 * Defines all operations available for a resource "stats".
 */
@Path(STATS_PATH)
public interface StatEndpoint {
	
	/**
	 * Get popular stats.
	 * 
	 * @return List<SimpleStat>.
	 * 
	 * @see Stat
	 */
	@GET
	@Path(ROOMS_PATH + POPULAR_PATH)
	@Produces(MediaType.APPLICATION_JSON)
	List<SimpleStat> getPopularStats();
	
	/**
	 * Get occupancy stats.
	 * 
	 * @return List<MultiStatSet>.
	 * 
	 * @see Stat
	 */
	@GET
	@Path(ROOMS_PATH + OCCUPANCY_PATH)
	@Produces(MediaType.APPLICATION_JSON)
	MultiStatSet getOccupancyStats(@QueryParam(FROM_PARAM) Integer from, @QueryParam(TO_PARAM) Integer to, @QueryParam(VIEW_TYPE_PARAM) String viewtype);
		
	// used for tests
	boolean executeInitTestFile();
	
	
	
}



