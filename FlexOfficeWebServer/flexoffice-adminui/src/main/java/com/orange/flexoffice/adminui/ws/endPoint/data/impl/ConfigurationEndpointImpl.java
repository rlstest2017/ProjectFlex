package com.orange.flexoffice.adminui.ws.endPoint.data.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.adminui.ws.endPoint.data.ConfigurationEndpoint;
import com.orange.flexoffice.adminui.ws.model.Building;
import com.orange.flexoffice.adminui.ws.model.BuildingInput;
import com.orange.flexoffice.adminui.ws.model.BuildingItem;
import com.orange.flexoffice.adminui.ws.model.BuildingSummary;
import com.orange.flexoffice.adminui.ws.model.City;
import com.orange.flexoffice.adminui.ws.model.CityInput;
import com.orange.flexoffice.adminui.ws.model.CitySummary;
import com.orange.flexoffice.adminui.ws.model.LocationInput;
import com.orange.flexoffice.adminui.ws.model.LocationItem;
import com.orange.flexoffice.adminui.ws.model.ObjectFactory;
import com.orange.flexoffice.adminui.ws.model.Region;
import com.orange.flexoffice.adminui.ws.model.RegionInput;
import com.orange.flexoffice.adminui.ws.model.RegionSummary;
import com.orange.flexoffice.adminui.ws.utils.ErrorMessageHandler;
import com.orange.flexoffice.business.common.enums.EnumErrorModel;
import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.business.common.service.data.BuildingManager;
import com.orange.flexoffice.dao.common.model.data.BuildingDao;
import com.orange.flexoffice.dao.common.model.object.BuildingDto;
import com.orange.flexoffice.dao.common.model.object.BuildingSummaryDto;

/**
 * ConfigurationEndpointImpl
 * @author oab
 *
 */
public class ConfigurationEndpointImpl implements ConfigurationEndpoint {
	
	private static final Logger LOGGER = Logger.getLogger(ConfigurationEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();
	
	@Autowired
	private BuildingManager buildingManager;
	@Autowired
	private ErrorMessageHandler errorMessageHandler;

	
	@Override
	public List<BuildingSummary> getBuildings() {
		LOGGER.debug( "Begin call ConfigurationEndpoint.getBuildings at: " + new Date() );

		List<BuildingSummaryDto> dataList = buildingManager.findAllBuildings();

		List<BuildingSummary> buildingList = new ArrayList<BuildingSummary>();

		for (BuildingSummaryDto buildingDto : dataList) {
			BuildingSummary building = factory.createBuildingSummary();
			building.setId(buildingDto.getId().toString());
			building.setName(buildingDto.getName());
			building.setAddress(buildingDto.getAddress());
			building.setCountryName(buildingDto.getCountryName());
			building.setRegionName(buildingDto.getRegionName());
			building.setCityName(buildingDto.getCityName());
			building.setNbFloors(BigInteger.valueOf(buildingDto.getNbFloors()));

			buildingList.add(building);
		}

		LOGGER.debug("List of buildings : nb = " + buildingList.size());

		LOGGER.debug( "End call ConfigurationEndpoint.getBuildings  at: " + new Date() );

		return buildingList;
	}
	
	@Override
	public Building getBuilding(String buildingId) {
		LOGGER.debug( "Begin call ConfigurationEndpoint.getBuilding at: " + new Date() );

		try {
			BuildingDto buidingDto = buildingManager.find(Long.valueOf(buildingId));

			Building building = factory.createBuilding();
			
			building.setId(String.valueOf(buidingDto.getId()));
			building.setName(buidingDto.getName());
			// TODO to complete ...
			
			LOGGER.debug( "End call ConfigurationEndpoint.getBuilding  at: " + new Date() );

			return building;

		} catch (DataNotExistsException e){

			LOGGER.debug("DataNotExistsException in ConfigurationEndpoint.getBuilding with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_31, Response.Status.NOT_FOUND));

		} catch (RuntimeException ex){

			LOGGER.debug("RuntimeException in ConfigurationEndpoint.getBuilding with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));

		}
	}
	
	@Override
	public BuildingItem addBuilding(BuildingInput building) {
		LOGGER.debug( "Begin call ConfigurationEndpoint.addBuilding at: " + new Date() );
		
		try {
			
		BuildingDao buildingDao = new BuildingDao();
		buildingDao.setName(building.getName());
		buildingDao.setAddress(building.getAddress());
		buildingDao.setCityId(Long.valueOf(building.getCityId()));
		buildingDao.setNbFloors(building.getNbFloors().longValue());
		
		buildingDao = buildingManager.save(buildingDao);
		
		BuildingItem returnedBuilding = factory.createBuildingItem();
		returnedBuilding.setBuildingId(buildingDao.getColumnId());


		LOGGER.debug( "End call ConfigurationEndpoint.addBuilding at: " + new Date() );

		return factory.createBuildingItem(returnedBuilding).getValue();


		} catch (DataAlreadyExistsException e) {
			
			LOGGER.debug("DataNotExistsException in ConfigurationEndpoint.addBuilding with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_28, Response.Status.METHOD_NOT_ALLOWED));

		} catch (RuntimeException ex) {

			LOGGER.debug("RuntimeException in ConfigurationEndpoint.addBuilding with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}
	}
	
	@Override
	public Response updateBuilding(String id, BuildingInput building) {
		LOGGER.debug( "Begin call ConfigurationEndpoint.updateBuilding at: " + new Date() );

		try {
			
			BuildingDao buildingDao = new BuildingDao();
			buildingDao.setName(building.getName());
			buildingDao.setAddress(building.getAddress());
			buildingDao.setCityId(Long.valueOf(building.getCityId()));
			buildingDao.setNbFloors(building.getNbFloors().longValue());
		
			buildingManager.update(buildingDao);


		LOGGER.debug( "End call ConfigurationEndpoint.updateBuilding at: " + new Date() );
		return Response.status(Status.ACCEPTED).build();

		} catch (DataNotExistsException e){
			LOGGER.debug("DataNotExistsException in ConfigurationEndpoint.updateBuilding with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_29, Response.Status.NOT_FOUND));
		} catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in ConfigurationEndpoint.updateBuilding with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}
	}
	
	@Override
	public Response removeBuilding(String id) {
		LOGGER.debug( "Begin call ConfigurationEndpoint.removeBuilding at: " + new Date() );

		try {

			buildingManager.delete(Long.valueOf(id));

		} catch (DataNotExistsException e){
			
			LOGGER.debug("DataNotExistsException in ConfigurationEndpoint.removeBuilding with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_30, Response.Status.NOT_FOUND));
			
		} catch (IntegrityViolationException e){
			LOGGER.debug("IntegrityViolationException in ConfigurationEndpoint.removeBuilding with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_30, Response.Status.METHOD_NOT_ALLOWED));
		} catch (RuntimeException ex){

			LOGGER.debug("RuntimeException in ConfigurationEndpoint.removeBuilding with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}

		LOGGER.debug( "End call ConfigurationEndpoint.removeBuilding at: " + new Date() );

		return Response.noContent().build();
	}
	
	@Override
	public List<CitySummary> getCities() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public City getCity(String cityId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Response addCity(CityInput building) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Response updateCity(String id, CityInput city) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Response removeCity(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<RegionSummary> getRegions() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Region getRegion(String regionId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Response addRegion(RegionInput region) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Response updateRegion(String id, RegionInput region) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Response removeRegion(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<LocationItem> getCountries() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public LocationItem getCountry(String countryId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Response addCountry(LocationItem country) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Response updateCountry(String id, LocationInput region) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Response removeCountry(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	

	
}
