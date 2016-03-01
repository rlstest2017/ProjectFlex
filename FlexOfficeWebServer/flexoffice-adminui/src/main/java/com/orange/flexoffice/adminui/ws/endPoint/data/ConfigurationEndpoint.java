package com.orange.flexoffice.adminui.ws.endPoint.data;


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

import com.orange.flexoffice.adminui.ws.model.Building;
import com.orange.flexoffice.adminui.ws.model.BuildingInput;
import com.orange.flexoffice.adminui.ws.model.BuildingSummary;
import com.orange.flexoffice.adminui.ws.model.City;
import com.orange.flexoffice.adminui.ws.model.CityInput;
import com.orange.flexoffice.adminui.ws.model.CitySummary;
import com.orange.flexoffice.adminui.ws.model.LocationInput;
import com.orange.flexoffice.adminui.ws.model.LocationItem;
import com.orange.flexoffice.adminui.ws.model.Region;
import com.orange.flexoffice.adminui.ws.model.RegionInput;
import com.orange.flexoffice.adminui.ws.model.RegionSummary;

import static com.orange.flexoffice.adminui.ws.PathConst.CONFIGURATION_PATH;
import static com.orange.flexoffice.adminui.ws.PathConst.BUILDING_PATH;
import static com.orange.flexoffice.adminui.ws.PathConst.BUILDING_ID_PATH;
import static com.orange.flexoffice.adminui.ws.PathConst.CITY_PATH;
import static com.orange.flexoffice.adminui.ws.PathConst.CITY_ID_PATH;
import static com.orange.flexoffice.adminui.ws.PathConst.REGION_PATH;
import static com.orange.flexoffice.adminui.ws.PathConst.REGION_ID_PATH;
import static com.orange.flexoffice.adminui.ws.PathConst.COUNTRY_PATH;
import static com.orange.flexoffice.adminui.ws.PathConst.COUNTRY_ID_PATH;

import static com.orange.flexoffice.adminui.ws.ParamsConst.BUILDING_ID_PARAM;
import static com.orange.flexoffice.adminui.ws.ParamsConst.CITY_ID_PARAM;
import static com.orange.flexoffice.adminui.ws.ParamsConst.REGION_ID_PARAM;
import static com.orange.flexoffice.adminui.ws.ParamsConst.COUNTRY_ID_PARAM;

import java.util.List;

/**
 * Defines all operations available for a resource "configuration".
 */
@Path(CONFIGURATION_PATH)
public interface ConfigurationEndpoint {
	
	//-----------------------------------------------------
	//					BUILDING methods
	//-----------------------------------------------------
	/**
	 * Get buildings.
	 * 
	 * @return building summary list.
	 * 
	 * @see BuildingSummary
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	List<BuildingSummary> getBuildings();

	/**
	 * Get information on a specific building.
	 * 
	 * @param buildingId
	 *            the building ID
	 * 
	 * @return information about a specific building.
	 * 
	 * @see Building
	 */
	@GET
	@Path(BUILDING_PATH + BUILDING_ID_PATH)
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Building getBuilding(@PathParam(BUILDING_ID_PARAM) String buildingId);

	/**
	 * Add a new building.
	 * 
	 * @param building
	 * 			  the new building
	 * @return If ok, a <code>Response</code> with a status code 201 Create & Location.
	 * 
	 * @see BuildingInput
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Response addBuilding(BuildingInput building);

	/**
	 * Put information on a specific building.
	 * 
	 * @param id
	 *            the building ID
	 * @param building
	 *            the BuildingInput
	 * 
	 * @return information about a specific building.
	 * 
	 * @see Building
	 */
	@PUT
	@Path(BUILDING_PATH + BUILDING_ID_PATH)
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Response updateBuilding(@PathParam(BUILDING_ID_PARAM) String id, BuildingInput building);

	/**
	 * Deletes a specific building.
	 * To identify a building, a building id is required. 
	 * 
	 * @param id
	 * 			  a building id. 
	 * @return If ok, a <code>Response</code> with a status code 204 No Content.
	 * 
	 * @see Response
	 */
	@DELETE
	@Path(BUILDING_PATH + BUILDING_ID_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Response removeBuilding(@PathParam(BUILDING_ID_PARAM)String id);

	//-----------------------------------------------------
	//					CITY methods
	//-----------------------------------------------------
	/**
	 * Get cities.
	 * 
	 * @return city summary list.
	 * 
	 * @see CitySummary
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	List<CitySummary> getCities();

	/**
	 * Gets information on a specific city.
	 * 
	 * @param cityId
	 *            the city ID
	 * 
	 * @return information about a specific city.
	 * 
	 * @see City
	 */
	@GET
	@Path(CITY_PATH + CITY_ID_PATH)
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	City getCity(@PathParam(CITY_ID_PARAM) String cityId);

	/**
	 * Add a new city.
	 * 
	 * @param city
	 * 			  the new city
	 * @return If ok, a <code>Response</code> with a status code 201 Create & Location.
	 * 
	 * @see CityInput
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Response addCity(CityInput building);

	/**
	 * Put information on a specific city.
	 * 
	 * @param id
	 *            the city ID
	 * @param building
	 *            the BuildingInput
	 * 
	 * @return information about a specific building.
	 * 
	 * @see Building
	 */
	@PUT
	@Path(CITY_PATH + CITY_ID_PATH)
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Response updateCity(@PathParam(CITY_ID_PARAM) String id, CityInput city);

	/**
	 * Deletes a specific city.
	 * To identify a city, a city id is required. 
	 * 
	 * @param id
	 * 			  a city id. 
	 * @return If ok, a <code>Response</code> with a status code 204 No Content.
	 * 
	 * @see Response
	 */
	@DELETE
	@Path(CITY_PATH + CITY_ID_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Response removeCity(@PathParam(CITY_ID_PARAM)String id);

	//-----------------------------------------------------
	//					REGION methods
	//-----------------------------------------------------
	/**
	 * Get regions.
	 * 
	 * @return region summary list.
	 * 
	 * @see RegionSummary
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	List<RegionSummary> getRegions();

	/**
	 * Gets information on a specific region.
	 * 
	 * @param regionId
	 *            the region ID
	 * 
	 * @return information about a specific region.
	 * 
	 * @see Region
	 */
	@GET
	@Path(REGION_PATH + REGION_ID_PATH)
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Region getRegion(@PathParam(REGION_ID_PARAM) String regionId);

	/**
	 * Add a new region.
	 * 
	 * @param region
	 * 			  the new region
	 * @return If ok, a <code>Response</code> with a status code 201 Create & Location.
	 * 
	 * @see RegionInput
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Response addRegion(RegionInput region);

	/**
	 * Put information on a specific region.
	 * 
	 * @param id
	 *            the region ID
	 * @param region
	 *            the RegionInput
	 * 
	 * @return information about a specific region.
	 * 
	 * @see Region
	 */
	@PUT
	@Path(REGION_PATH + REGION_ID_PATH)
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Response updateRegion(@PathParam(REGION_ID_PARAM) String id, RegionInput region);

	/**
	 * Deletes a specific region.
	 * To identify a region, a region id is required. 
	 * 
	 * @param id
	 * 			  a region id. 
	 * @return If ok, a <code>Response</code> with a status code 204 No Content.
	 * 
	 * @see Response
	 */
	@DELETE
	@Path(REGION_PATH + REGION_ID_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Response removeRegion(@PathParam(REGION_ID_PARAM)String id);

	//-----------------------------------------------------
	//					COUNTRY methods
	//-----------------------------------------------------
	/**
	 * Get countries.
	 * 
	 * @return location item list.
	 * 
	 * @see LocationItem
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	List<LocationItem> getCountries();
	
	/**
	 * Gets information on a specific country.
	 * 
	 * @param countryId
	 *            the rcountry ID
	 * 
	 * @return information about a specific country.
	 * 
	 * @see Country
	 */
	@GET
	@Path(COUNTRY_PATH + COUNTRY_ID_PATH)
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	LocationItem getCountry(@PathParam(COUNTRY_ID_PARAM) String countryId);

	/**
	 * Add a new country.
	 * 
	 * @param country
	 * 			  the new country
	 * @return If ok, a <code>Response</code> with a status code 201 Create & Location.
	 * 
	 * @see LocationItem
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Response addCountry(LocationItem country);

	/**
	 * Put information on a specific country.
	 * 
	 * @param id
	 *            the country ID
	 * @param country
	 *            the LocationInput
	 * 
	 * @return information about a specific country.
	 * 
	 * @see LocationInput
	 */
	@PUT
	@Path(COUNTRY_PATH + COUNTRY_ID_PATH)
	@Consumes (MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Response updateCountry(@PathParam(COUNTRY_ID_PARAM) String id, LocationInput region);

	/**
	 * Deletes a specific country.
	 * To identify a country, a country id is required. 
	 * 
	 * @param id
	 * 			  a country id. 
	 * @return If ok, a <code>Response</code> with a status code 204 No Content.
	 * 
	 * @see Response
	 */
	@DELETE
	@Path(COUNTRY_PATH + COUNTRY_ID_PATH)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Response removeCountry(@PathParam(COUNTRY_ID_PARAM)String id);

	
}



