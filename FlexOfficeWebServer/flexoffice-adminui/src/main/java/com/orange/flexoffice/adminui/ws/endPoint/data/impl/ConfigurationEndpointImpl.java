package com.orange.flexoffice.adminui.ws.endPoint.data.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.SAXException;

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
import com.orange.flexoffice.business.common.exception.InvalidParametersException;
import com.orange.flexoffice.business.common.service.data.TestManager;

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
	private TestManager testManager;
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
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_58, Response.Status.NOT_FOUND));
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
			if (building.getNbFloors() == null) { // cityId & name are checked by postgresDB engine
				LOGGER.debug("Parameter nbFloors is null");
				throw new InvalidParametersException("Parameter nbFloors is null");
			}
			return buildingHandler.addBuilding(building);

		} catch (DataAlreadyExistsException e) {
			LOGGER.debug("DataAlreadyExistsException in ConfigurationEndpoint.addBuilding with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_55, Response.Status.METHOD_NOT_ALLOWED));
		} catch (DataNotExistsException e) {
			LOGGER.debug("DataNotExistsException in ConfigurationEndpoint.addBuilding with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_55, Response.Status.INTERNAL_SERVER_ERROR));
		} catch (InvalidParametersException ex1){
			LOGGER.debug("InvalidParametersException in ConfigurationEndpoint.updateBuilding with message :", ex1);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_55, Response.Status.METHOD_NOT_ALLOWED));
		} catch (RuntimeException ex) {
			LOGGER.debug("RuntimeException in ConfigurationEndpoint.addBuilding with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		} catch (IOException | JAXBException e) {
			LOGGER.debug("RuntimeException in ConfigurationEndpoint.addBuilding with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		} finally {
			LOGGER.debug( "End call ConfigurationEndpoint.addBuilding at: " + new Date() );
		}
	}
	
	@Override
	public Response updateBuilding(String id, BuildingInput building) {
		LOGGER.debug( "Begin call ConfigurationEndpoint.updateBuilding at: " + new Date() );
		try {
			if (building.getNbFloors() == null) { // cityId & name are checked by postgresDB engine
				LOGGER.debug("Parameter nbFloors is null");
				throw new InvalidParametersException("Parameter nbFloors is null");
			}
			return buildingHandler.updateBuilding(id, building);
		} catch (DataNotExistsException e){
			LOGGER.debug("DataNotExistsException in ConfigurationEndpoint.updateBuilding with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_56, Response.Status.NOT_FOUND));
		} catch (InvalidParametersException ex1){
			LOGGER.debug("InvalidParametersException in ConfigurationEndpoint.updateBuilding with message :", ex1);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_56, Response.Status.METHOD_NOT_ALLOWED));
		} catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in ConfigurationEndpoint.updateBuilding with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		} catch (IOException | JAXBException | SAXException | ParserConfigurationException e) {
			LOGGER.debug("ConfigurationEndpoint.updateBuilding : Meeting Room xml meeting room file in error", e);
			LOGGER.error("ConfigurationEndpoint.updateBuilding : Meeting Room xml meeting room file in error");
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
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_57, Response.Status.NOT_FOUND));
		} catch (IntegrityViolationException e){
			LOGGER.debug("IntegrityViolationException in ConfigurationEndpoint.removeBuilding with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_57, Response.Status.METHOD_NOT_ALLOWED));
		} catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in ConfigurationEndpoint.removeBuilding with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		} catch (IOException | JAXBException e) {
			LOGGER.debug("RuntimeException in ConfigurationEndpoint.removeBuilding with message :", e);
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
		return cityHandler.getCities();
	}
	
	@Override
	public City getCity(String cityId) {
		LOGGER.debug( "Begin call ConfigurationEndpoint.getCity at: " + new Date() );
		try {
			return cityHandler.getCity(cityId);
		} catch (DataNotExistsException e){
			LOGGER.debug("DataNotExistsException in ConfigurationEndpoint.getCity with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_53, Response.Status.NOT_FOUND));
		} catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in ConfigurationEndpoint.getCity with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		} finally {
			LOGGER.debug( "End call ConfigurationEndpoint.getCity  at: " + new Date() );
		}
	}
	
	@Override
	public City addCity(CityInput city) {
		LOGGER.debug( "Begin call ConfigurationEndpoint.addCity at: " + new Date() );
		try {
			return cityHandler.addCity(city);
		} catch (DataAlreadyExistsException e) {
			LOGGER.debug("DataNotExistsException in ConfigurationEndpoint.addCity with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_50, Response.Status.METHOD_NOT_ALLOWED));
		} catch (RuntimeException ex) {
			LOGGER.debug("RuntimeException in ConfigurationEndpoint.addCity with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		} finally {
			LOGGER.debug( "End call ConfigurationEndpoint.addCity at: " + new Date() );
		}
	}
	
	@Override
	public Response updateCity(String id, CityInput city) {
		LOGGER.debug( "Begin call ConfigurationEndpoint.updateCity at: " + new Date() );
		try {
			return cityHandler.updateCity(id, city);
		} catch (DataNotExistsException e){
			LOGGER.debug("DataNotExistsException in ConfigurationEndpoint.updateCity with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_51, Response.Status.NOT_FOUND));
		} catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in ConfigurationEndpoint.updateCity with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		} finally {
			LOGGER.debug( "End call ConfigurationEndpoint.updateCity at: " + new Date() );
		}
	}
	@Override
	public Response removeCity(String id) {
		LOGGER.debug( "Begin call ConfigurationEndpoint.removeCity at: " + new Date() );
		try {
			return cityHandler.removeCity(id);
		} catch (DataNotExistsException e){
			LOGGER.debug("DataNotExistsException in ConfigurationEndpoint.removeCity with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_52, Response.Status.NOT_FOUND));
		} catch (IntegrityViolationException e){
			LOGGER.debug("IntegrityViolationException in ConfigurationEndpoint.removeCity with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_52, Response.Status.METHOD_NOT_ALLOWED));
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
		return regionHandler.getRegions();
	}
	
	@Override
	public Region getRegion(String regionId) {
		LOGGER.debug( "Begin call ConfigurationEndpoint.getRegion at: " + new Date() );
		try {
			return regionHandler.getRegion(regionId);
		} catch (DataNotExistsException e){
			LOGGER.debug("DataNotExistsException in ConfigurationEndpoint.getRegion with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_48, Response.Status.NOT_FOUND));
		} catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in ConfigurationEndpoint.getRegion with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		} finally {
			LOGGER.debug( "End call ConfigurationEndpoint.getRegion  at: " + new Date() );
		}
	}
	
	@Override
	public Region addRegion(RegionInput region) {
		LOGGER.debug( "Begin call ConfigurationEndpoint.addRegion at: " + new Date() );
		try {
			return regionHandler.addRegion(region);
		} catch (DataAlreadyExistsException e) {
			LOGGER.debug("DataNotExistsException in ConfigurationEndpoint.addRegion with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_45, Response.Status.METHOD_NOT_ALLOWED));
		} catch (RuntimeException ex) {
			LOGGER.debug("RuntimeException in ConfigurationEndpoint.addRegion with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		} finally {
			LOGGER.debug( "End call ConfigurationEndpoint.addRegion at: " + new Date() );
		}
	}
	
	@Override
	public Response updateRegion(String id, RegionInput region) {
		LOGGER.debug( "Begin call ConfigurationEndpoint.updateRegion at: " + new Date() );
		try {
			return regionHandler.updateRegion(id, region);
		} catch (DataNotExistsException e){
			LOGGER.debug("DataNotExistsException in ConfigurationEndpoint.updateRegion with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_46, Response.Status.NOT_FOUND));
		} catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in ConfigurationEndpoint.updateRegion with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		} finally {
			LOGGER.debug( "End call ConfigurationEndpoint.updateRegion at: " + new Date() );
		}
	}
	
	@Override
	public Response removeRegion(String id) {
		LOGGER.debug( "Begin call ConfigurationEndpoint.removeRegion at: " + new Date() );
		try {
			return regionHandler.removeRegion(id);
		} catch (DataNotExistsException e){
			LOGGER.debug("DataNotExistsException in ConfigurationEndpoint.removeRegion with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_47, Response.Status.NOT_FOUND));
		} catch (IntegrityViolationException e){
			LOGGER.debug("IntegrityViolationException in ConfigurationEndpoint.removeRegion with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_47, Response.Status.METHOD_NOT_ALLOWED));
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
		return countryHandler.getCountries();
	}
	
	@Override
	public LocationItem getCountry(String countryId) {
		LOGGER.debug( "Begin call ConfigurationEndpoint.getCountry at: " + new Date() );
		try {
			return countryHandler.getCountry(countryId);
		} catch (DataNotExistsException e){
			LOGGER.debug("DataNotExistsException in ConfigurationEndpoint.getCountry with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_43, Response.Status.NOT_FOUND));
		} catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in ConfigurationEndpoint.getCountry with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		} finally {
			LOGGER.debug( "End call ConfigurationEndpoint.getCountry  at: " + new Date() );
		}
	}
	
	@Override
	public LocationItem addCountry(LocationInput country) {
		LOGGER.debug( "Begin call ConfigurationEndpoint.addCountry at: " + new Date() );
		try {
			return countryHandler.addCountry(country);
		} catch (DataAlreadyExistsException e) {
			LOGGER.debug("DataNotExistsException in ConfigurationEndpoint.addCountry with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_40, Response.Status.METHOD_NOT_ALLOWED));
		} catch (RuntimeException ex) {
			LOGGER.debug("RuntimeException in ConfigurationEndpoint.addCountry with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		} finally {
			LOGGER.debug( "End call ConfigurationEndpoint.addCountry at: " + new Date() );
		}
	}
	
	@Override
	public Response updateCountry(String id, LocationInput country) {
		LOGGER.debug( "Begin call ConfigurationEndpoint.updateCountry at: " + new Date() );
		try {
			return countryHandler.updateCountry(id, country);
		} catch (DataNotExistsException e){
			LOGGER.debug("DataNotExistsException in ConfigurationEndpoint.updateCountry with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_41, Response.Status.NOT_FOUND));
		} catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in ConfigurationEndpoint.updateCountry with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		} finally {
			LOGGER.debug( "End call ConfigurationEndpoint.updateCountry at: " + new Date() );
		}
	}
	
	@Override
	public Response removeCountry(String id) {
		LOGGER.debug( "Begin call ConfigurationEndpoint.removeCountry at: " + new Date() );
		try {
			return countryHandler.removeCountry(id);
			
		} catch (DataNotExistsException e){
			LOGGER.debug("DataNotExistsException in ConfigurationEndpoint.removeCountry with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_42, Response.Status.NOT_FOUND));
		} catch (IntegrityViolationException e){
			LOGGER.debug("IntegrityViolationException in ConfigurationEndpoint.removeCountry with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_42, Response.Status.METHOD_NOT_ALLOWED));
		} catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in ConfigurationEndpoint.removeCountry with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		} finally {
			LOGGER.debug( "End call ConfigurationEndpoint.removeCountry at: " + new Date() );
		}
	}

	@Override
	public boolean executeInitTestFile() {
		return testManager.executeInitTestFile();
	}
	
}
