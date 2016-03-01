package com.orange.flexoffice.adminui.ws.endPoint.data;

import static com.orange.flexoffice.adminui.ws.ParamsConst.COUNTRY_ID_PARAM;
import static com.orange.flexoffice.adminui.ws.PathConst.COUNTRY_ID_PATH;
import static com.orange.flexoffice.adminui.ws.PathConst.COUNTRY_PATH;
import static com.orange.flexoffice.adminui.ws.PathConst.REGION_PATH;
import static com.orange.flexoffice.adminui.ws.PathConst.LOCATION_PATH;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.orange.flexoffice.adminui.ws.model.LocationItem;

/**
 * Defines all operations available for a resource "location".
 */
@Path(LOCATION_PATH)
public interface LocationEndpoint {
	
	//-----------------------------------------------------
	//					Location methods
	//-----------------------------------------------------
	/**
	 * Get countries.
	 * 
	 * @return location item list.
	 * 
	 * @see LocationItem
	 */
	@GET
	@Path(COUNTRY_PATH)
	@Produces(MediaType.APPLICATION_JSON)
	List<LocationItem> getCountries();

	/**
	 * Get regions for specific country.
	 * 
	 * @param countryId
	 *            the country ID
	 * 
	 * @return information about a specific country.
	 * 
	 * @see Country
	 */
	@GET
	@Path(COUNTRY_PATH + COUNTRY_ID_PATH + REGION_PATH)
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	List<LocationItem> getRegions(@PathParam(COUNTRY_ID_PARAM) String countryId);

	
}



