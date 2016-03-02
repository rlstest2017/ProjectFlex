package com.orange.flexoffice.adminui.ws.endPoint.data.impl;

import java.util.Date;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

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
import com.orange.flexoffice.adminui.ws.model.Region;
import com.orange.flexoffice.adminui.ws.model.RegionInput;
import com.orange.flexoffice.adminui.ws.model.RegionSummary;
import com.orange.flexoffice.adminui.ws.utils.BuildingHandler;
import com.orange.flexoffice.adminui.ws.utils.CityHandler;
import com.orange.flexoffice.adminui.ws.utils.CountryHandler;
import com.orange.flexoffice.adminui.ws.utils.ErrorMessageHandler;
import com.orange.flexoffice.adminui.ws.utils.RegionHandler;
import com.orange.flexoffice.business.common.enums.EnumErrorModel;
import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;

/**
 * ConfigurationEndpointImpl
 * @author oab
 *
 */
public class ConfigurationEndpointImpl implements ConfigurationEndpoint {
	
	private static final Logger LOGGER = Logger.getLogger(ConfigurationEndpointImpl.class);
	
	@Autowired
	private ErrorMessageHandler errorMessageHandler;
	@Autowired
	private BuildingHandler buildingHandler;
	@Autowired
	private CityHandler cityHandler;
	@Autowired
	private RegionHandler regionHandler;
	@Autowired
	private CountryHandler countryHandler;
	
	//-------------------------------------------------------------------------
	//						BUILDING Methods
	//-------------------------------------------------------------------------
	@Override
	public List<BuildingSummary> getBuildings() {
		return buildingHandler.getBuildings();
	}
	
	@Override
	public Building getBuilding(String buildingId) {
		LOGGER.debug( "Begin call ConfigurationEndpoint.getBuilding at: " + new Date() );
		try {
			return buildingHandler.getBuilding(buildingId);
		} catch (DataNotExistsException e){
			LOGGER.debug("DataNotExistsException in ConfigurationEndpoint.getBuilding with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_31, Response.Status.NOT_FOUND));
		} catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in ConfigurationEndpoint.getBuilding with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		} finally {
			LOGGER.debug( "End call ConfigurationEndpoint.getBuilding  at: " + new Date() );
		}
	}
	
	@Override
	public BuildingItem addBuilding(BuildingInput building) {
		LOGGER.debug( "Begin call ConfigurationEndpoint.addBuilding at: " + new Date() );
		try {
			return buildingHandler.addBuilding(building);
		} catch (DataAlreadyExistsException e) {
			LOGGER.debug("DataNotExistsException in ConfigurationEndpoint.addBuilding with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_28, Response.Status.METHOD_NOT_ALLOWED));
		} catch (RuntimeException ex) {
			LOGGER.debug("RuntimeException in ConfigurationEndpoint.addBuilding with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		} finally {
			LOGGER.debug( "End call ConfigurationEndpoint.addBuilding at: " + new Date() );
		}
	}
	
	@Override
	public Response updateBuilding(String id, BuildingInput building) {
		LOGGER.debug( "Begin call ConfigurationEndpoint.updateBuilding at: " + new Date() );
		try {
			return buildingHandler.updateBuilding(id, building);
		} catch (DataNotExistsException e){
			LOGGER.debug("DataNotExistsException in ConfigurationEndpoint.updateBuilding with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_29, Response.Status.NOT_FOUND));
		} catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in ConfigurationEndpoint.updateBuilding with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		} finally {
			LOGGER.debug( "End call ConfigurationEndpoint.updateBuilding at: " + new Date() );
		}
	}
	
	@Override
	public Response removeBuilding(String id) {
		LOGGER.debug( "Begin call ConfigurationEndpoint.removeBuilding at: " + new Date() );
		try {
			return buildingHandler.removeBuilding(id);
		} catch (DataNotExistsException e){
			LOGGER.debug("DataNotExistsException in ConfigurationEndpoint.removeBuilding with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_30, Response.Status.NOT_FOUND));
		} catch (IntegrityViolationException e){
			LOGGER.debug("IntegrityViolationException in ConfigurationEndpoint.removeBuilding with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_30, Response.Status.METHOD_NOT_ALLOWED));
		} catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in ConfigurationEndpoint.removeBuilding with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		} finally {
			LOGGER.debug( "End call ConfigurationEndpoint.removeBuilding at: " + new Date() );
		}
	}
	
	//-------------------------------------------------------------------------
	//						CITY Methods
	//-------------------------------------------------------------------------
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
		LOGGER.debug( "Begin call ConfigurationEndpoint.removeCity at: " + new Date() );
		try {
			return cityHandler.removeCity(id);
		} catch (DataNotExistsException e){
			LOGGER.debug("DataNotExistsException in ConfigurationEndpoint.removeCity with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_30, Response.Status.NOT_FOUND));
		} catch (IntegrityViolationException e){
			LOGGER.debug("IntegrityViolationException in ConfigurationEndpoint.removeCity with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_30, Response.Status.METHOD_NOT_ALLOWED));
		} catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in ConfigurationEndpoint.removeCity with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		} finally {
			LOGGER.debug( "End call ConfigurationEndpoint.removeCity at: " + new Date() );	
		}

	}
	
	//-------------------------------------------------------------------------
	//						REGION Methods
	//-------------------------------------------------------------------------
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
		LOGGER.debug( "Begin call ConfigurationEndpoint.removeRegion at: " + new Date() );
		try {
			return regionHandler.removeRegion(id);
		} catch (DataNotExistsException e){
			LOGGER.debug("DataNotExistsException in ConfigurationEndpoint.removeRegion with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_30, Response.Status.NOT_FOUND));
		} catch (IntegrityViolationException e){
			LOGGER.debug("IntegrityViolationException in ConfigurationEndpoint.removeRegion with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_30, Response.Status.METHOD_NOT_ALLOWED));
		} catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in ConfigurationEndpoint.removeRegion with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		} finally {
			LOGGER.debug( "End call ConfigurationEndpoint.removeRegion at: " + new Date() );	
		}
	}
	
	//-------------------------------------------------------------------------
	//						COUNTRY Methods
	//-------------------------------------------------------------------------
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
		LOGGER.debug( "Begin call ConfigurationEndpoint.removeCountry at: " + new Date() );
		try {
			return countryHandler.removeCountry(id);
			
		} catch (DataNotExistsException e){
			LOGGER.debug("DataNotExistsException in ConfigurationEndpoint.removeCountry with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_30, Response.Status.NOT_FOUND));
		} catch (IntegrityViolationException e){
			LOGGER.debug("IntegrityViolationException in ConfigurationEndpoint.removeCountry with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_30, Response.Status.METHOD_NOT_ALLOWED));
		} catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in ConfigurationEndpoint.removeCountry with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		} finally {
			LOGGER.debug( "End call ConfigurationEndpoint.removeCountry at: " + new Date() );
		}
	}
	
}
