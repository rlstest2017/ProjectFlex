package com.orange.flexoffice.userui.ws.endPoint.data;

import static com.orange.flexoffice.userui.ws.ParamsConst.COUNTRY_ID_PARAM;
import static com.orange.flexoffice.userui.ws.ParamsConst.REGION_ID_PARAM;
import static com.orange.flexoffice.userui.ws.ParamsConst.CITY_ID_PARAM;

import static com.orange.flexoffice.userui.ws.PathConst.COUNTRY_ID_PATH;
import static com.orange.flexoffice.userui.ws.PathConst.REGION_ID_PATH;
import static com.orange.flexoffice.userui.ws.PathConst.CITY_ID_PATH;
import static com.orange.flexoffice.userui.ws.PathConst.BUILDING_PATH;
import static com.orange.flexoffice.userui.ws.PathConst.COUNTRY_PATH;
import static com.orange.flexoffice.userui.ws.PathConst.REGION_PATH;
import static com.orange.flexoffice.userui.ws.PathConst.CITY_PATH;
import static com.orange.flexoffice.userui.ws.PathConst.LOCATION_PATH;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.orange.flexoffice.userui.ws.model.BuildingItem;
import com.orange.flexoffice.userui.ws.model.LocationItem;

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

	/**
	 * Get cities for specific region.
	 * 
	 * @param regionId
	 *            the region ID
	 * 
	 * @return information about a specific region.
	 * 
	 * @see Region
	 */
	@GET
	@Path(REGION_PATH + REGION_ID_PATH + CITY_PATH)
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	List<LocationItem> getCities(@PathParam(REGION_ID_PARAM) String regionId);

	/**
	 * Get buildings for specific city.
	 * 
	 * @param cityId
	 *            the city ID
	 * 
	 * @return information about a specific city.
	 * 
	 * @see City
	 */
	@GET
	@Path(CITY_PATH + CITY_ID_PATH + BUILDING_PATH)
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	List<BuildingItem> getBuildings(@PathParam(CITY_ID_PARAM) String cityId);

}



