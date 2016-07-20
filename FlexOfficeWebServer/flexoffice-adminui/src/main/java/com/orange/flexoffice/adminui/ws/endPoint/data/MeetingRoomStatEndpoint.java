package com.orange.flexoffice.adminui.ws.endPoint.data;


import static com.orange.flexoffice.adminui.ws.ParamsConst.FROM_PARAM;
import static com.orange.flexoffice.adminui.ws.ParamsConst.TO_PARAM;
import static com.orange.flexoffice.adminui.ws.ParamsConst.VIEW_TYPE_PARAM;
import static com.orange.flexoffice.adminui.ws.PathConst.EXPORT_PATH;
import static com.orange.flexoffice.adminui.ws.PathConst.MEETINGROOMS_PATH;
import static com.orange.flexoffice.adminui.ws.PathConst.OCCUPANCY_PATH;
import static com.orange.flexoffice.adminui.ws.PathConst.POPULAR_PATH;
import static com.orange.flexoffice.adminui.ws.PathConst.STATS_PATH;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.orange.flexoffice.adminui.ws.model.MultiStatSet;
import com.orange.flexoffice.adminui.ws.model.SimpleStat;

/**
 * Defines all operations available for a meeting room resource "stats".
 */
@Path(STATS_PATH)
public interface MeetingRoomStatEndpoint {
	
	/**
	 * Get popular meeting room stats.
	 * 
	 * @return List<SimpleStat>.
	 * 
	 * @see Stat
	 */
	@GET
	@Path(MEETINGROOMS_PATH + POPULAR_PATH)
	@Produces(MediaType.APPLICATION_JSON)
	List<SimpleStat> getPopularStats();
	
	/**
	 * Get meeting room occupancy stats.
	 * 
	 * @return List<MultiStatSet>.
	 * 
	 * @see Stat
	 */
	@GET
	@Path(MEETINGROOMS_PATH + OCCUPANCY_PATH)
	@Produces(MediaType.APPLICATION_JSON)
	MultiStatSet getOccupancyStats(@QueryParam(FROM_PARAM) String from, @QueryParam(TO_PARAM) String to, @QueryParam(VIEW_TYPE_PARAM) String viewtype);
	
	
	@GET
    @Path(MEETINGROOMS_PATH + EXPORT_PATH)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
	Response getFile();
	
	// used for tests
	boolean getTestFile();
	
	// used for tests
	boolean executeInitTestFile();
	
	// used for tests
	boolean initMeetingRoomDailyOccupancyTable();
	
	// used for tests
	boolean initMeetingRoomMonthlyOccupancyTable();
	
}



