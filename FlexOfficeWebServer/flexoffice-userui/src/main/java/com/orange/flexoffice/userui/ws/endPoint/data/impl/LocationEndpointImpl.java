package com.orange.flexoffice.userui.ws.endPoint.data.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.business.common.service.data.BuildingManager;
import com.orange.flexoffice.business.common.service.data.CityManager;
import com.orange.flexoffice.business.common.service.data.CountryManager;
import com.orange.flexoffice.business.common.service.data.RegionManager;
import com.orange.flexoffice.business.common.service.data.TestManager;
import com.orange.flexoffice.dao.common.model.data.BuildingDao;
import com.orange.flexoffice.dao.common.model.data.CityDao;
import com.orange.flexoffice.dao.common.model.data.CountryDao;
import com.orange.flexoffice.dao.common.model.data.RegionDao;
import com.orange.flexoffice.userui.ws.endPoint.data.LocationEndpoint;
import com.orange.flexoffice.userui.ws.model.BuildingItem;
import com.orange.flexoffice.userui.ws.model.LocationItem;
import com.orange.flexoffice.userui.ws.model.ObjectFactory;

/**
 * LocationEndpointImpl
 * @author oab
 *
 */
public class LocationEndpointImpl implements LocationEndpoint {
	
	private static final Logger LOGGER = Logger.getLogger(LocationEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();
	
	@Autowired
	private TestManager testManager;
	@Autowired
	private CountryManager countryManager;
	@Autowired
	private RegionManager regionManager;
	@Autowired
	private CityManager cityManager;
	@Autowired
	private BuildingManager buildingManager;
	
	@Override
	public List<LocationItem> getCountries() { // only have rooms !!
		LOGGER.debug( "Begin call LocationEndpointImpl.getCountries at: " + new Date() );
		List<CountryDao> dataList = countryManager.findCountries(true); // TODO request from UserUI isFromAdminUI=false
		List<LocationItem> countryList = new ArrayList<LocationItem>();
		for (CountryDao countryDao : dataList) {
			LocationItem country = factory.createLocationItem();
			country.setId(countryDao.getId().toString());
			country.setName(countryDao.getName());
			countryList.add(country);
		}
		LOGGER.debug("List of countries have rooms : nb = " + countryList.size());
		LOGGER.debug( "End call LocationEndpointImpl.getCountries  at: " + new Date() );
		return countryList;
	}
	
	@Override
	public List<LocationItem> getRegions(String countryId) { // only have rooms !!
		LOGGER.debug( "Begin call LocationEndpointImpl.getRegions at: " + new Date() );
		List<RegionDao> dataList = regionManager.findRegionsByCountryId(countryId, true); // TODO request from UserUI isFromAdminUI=false
		List<LocationItem> regionList = new ArrayList<LocationItem>();
		for (RegionDao regionDao : dataList) {
			LocationItem region = factory.createLocationItem();
			region.setId(regionDao.getId().toString());
			region.setName(regionDao.getName());
			regionList.add(region);
		}
		LOGGER.debug("List of regions have rooms : nb = " + regionList.size());
		LOGGER.debug( "End call LocationEndpointImpl.getRegions  at: " + new Date() );
		return regionList;
	}
	
	@Override
	public List<LocationItem> getCities(String regionId) { // only have rooms !!
		LOGGER.debug( "Begin call LocationEndpointImpl.getCities at: " + new Date() );
		List<CityDao> dataList = cityManager.findCitiesByRegionId(regionId, true); // TODO request from UserUI isFromAdminUI=false
		List<LocationItem> cityList = new ArrayList<LocationItem>();
		for (CityDao cityDto : dataList) {
			LocationItem city = factory.createLocationItem();
			city.setId(cityDto.getId().toString());
			city.setName(cityDto.getName());
			cityList.add(city);
		}
		LOGGER.debug("List of cities have rooms : nb = " + cityList.size());
		LOGGER.debug( "End call LocationEndpointImpl.getCities  at: " + new Date() );
		return cityList;
	}
	
	@Override
	public List<BuildingItem> getBuildings(String cityId) { // only have rooms !!
		LOGGER.debug( "Begin call LocationEndpointImpl.getBuildings at: " + new Date() );
		List<BuildingDao> dataList = buildingManager.findBuildingsByCityId(cityId, true); // TODO request from UserUI isFromAdminUI=false
		List<BuildingItem> buildingList = new ArrayList<BuildingItem>();
		for (BuildingDao buildingDto : dataList) {
			BuildingItem building = factory.createBuildingItem();
			building.setBuildingId(buildingDto.getId().toString());
			building.setBuildingName(buildingDto.getName());
			building.setNbFloors(BigInteger.valueOf(buildingDto.getNbFloors()));
			buildingList.add(building);
		}
		LOGGER.debug("List of buildings have rooms: nb = " + buildingList.size());
		LOGGER.debug( "End call LocationEndpointImpl.getBuildings  at: " + new Date() );
		return buildingList;
	}

	@Override
	public boolean executeInitTestFile() {
		return testManager.executeInitTestFile();
	}

	
}
