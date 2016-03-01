package com.orange.flexoffice.adminui.ws.endPoint.data.impl;

import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.orange.flexoffice.adminui.ws.endPoint.data.ConfigurationEndpoint;
import com.orange.flexoffice.adminui.ws.model.Building;
import com.orange.flexoffice.adminui.ws.model.BuildingInput;
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

/**
 * ConfigurationEndpointImpl
 * @author oab
 *
 */
public class ConfigurationEndpointImpl implements ConfigurationEndpoint {
	
	private static final Logger LOGGER = Logger.getLogger(ConfigurationEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();
	
	@Override
	public List<BuildingSummary> getBuildings() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Building getBuilding(String buildingId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Response addBuilding(BuildingInput building) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Response updateBuilding(String id, BuildingInput building) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Response removeBuilding(String id) {
		// TODO Auto-generated method stub
		return null;
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
