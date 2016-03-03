package com.orange.flexoffice.adminui.ws.endPoint.data.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.adminui.ws.endPoint.data.LocationEndpoint;
import com.orange.flexoffice.adminui.ws.model.BuildingItem;
import com.orange.flexoffice.adminui.ws.model.LocationItem;
import com.orange.flexoffice.adminui.ws.model.ObjectFactory;
import com.orange.flexoffice.adminui.ws.utils.BuildingHandler;
import com.orange.flexoffice.adminui.ws.utils.CityHandler;
import com.orange.flexoffice.adminui.ws.utils.CountryHandler;
import com.orange.flexoffice.adminui.ws.utils.RegionHandler;

/**
 * LocationEndpointImpl
 * @author oab
 *
 */
public class LocationEndpointImpl implements LocationEndpoint {
	
	private static final Logger LOGGER = Logger.getLogger(LocationEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();
	
	@Autowired
	private BuildingHandler buildingHandler;
	@Autowired
	private CityHandler cityHandler;
	@Autowired
	private RegionHandler regionHandler;
	@Autowired
	private CountryHandler countryHandler;
	
	@Override
	public List<LocationItem> getCountries() {
		return countryHandler.getCountriesLocation();
	}
	
	@Override
	public List<LocationItem> getRegions(String countryId) {
		return regionHandler.getRegionsByCountry(countryId);
	}
	
	@Override
	public List<LocationItem> getCities(String regionId) {
		return cityHandler.getCitiesByRegion(regionId);
	}
	
	@Override
	public List<BuildingItem> getBuildings(String cityId) {
		return buildingHandler.getBuildingsByCity(cityId);
	}

	
}
