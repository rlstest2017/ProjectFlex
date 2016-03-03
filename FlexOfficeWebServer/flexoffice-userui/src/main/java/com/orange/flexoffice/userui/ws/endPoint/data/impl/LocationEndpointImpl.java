package com.orange.flexoffice.userui.ws.endPoint.data.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.business.common.service.data.BuildingManager;
import com.orange.flexoffice.business.common.service.data.CityManager;
import com.orange.flexoffice.business.common.service.data.CountryManager;
import com.orange.flexoffice.business.common.service.data.RegionManager;
import com.orange.flexoffice.dao.common.model.data.CountryDao;
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
		List<CountryDao> dataList = countryManager.findAllCountries(); //TODO change !!!
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
		//List<RegionSummaryDto> dataList = regionManager.findAllRegions();
		// TODO only have rooms !!
		List<LocationItem> regionList = new ArrayList<LocationItem>();
//		for (RegionSummaryDto regionDto : dataList) {
//			RegionSummary region = factory.createRegionSummary();
//			region.setId(regionDto.getId().toString());
//			region.setName(regionDto.getName());
//			region.setCountryName(regionDto.getCountryName());
//			regionList.add(region);
//		}
		LOGGER.debug("List of regions have rooms : nb = " + regionList.size());
		LOGGER.debug( "End call LocationEndpointImpl.getRegions  at: " + new Date() );
		return regionList;
	}
	
	@Override
	public List<LocationItem> getCities(String regionId) { // only have rooms !!
		LOGGER.debug( "Begin call LocationEndpointImpl.getCities at: " + new Date() );
		//List<CitySummaryDto> dataList = cityManager.findAllCities();
		// TODO only have rooms !!
		List<LocationItem> cityList = new ArrayList<LocationItem>();
//		for (CitySummaryDto cityDto : dataList) {
//			CitySummary city = factory.createCitySummary();
//			city.setId(cityDto.getId().toString());
//			city.setName(cityDto.getName());
//			city.setCountryName(cityDto.getCountryName());
//			city.setRegionName(cityDto.getRegionName());
//			cityList.add(city);
//		}
		LOGGER.debug("List of cities have rooms : nb = " + cityList.size());
		LOGGER.debug( "End call LocationEndpointImpl.getCities  at: " + new Date() );
		return cityList;
	}
	
	@Override
	public List<BuildingItem> getBuildings(String cityId) { // only have rooms !!
		LOGGER.debug( "Begin call LocationEndpointImpl.getBuildings at: " + new Date() );
		//List<BuildingSummaryDto> dataList = buildingManager.findAllBuildings();
		// TODO only have rooms !!
		List<BuildingItem> buildingList = new ArrayList<BuildingItem>();
//		for (BuildingSummaryDto buildingDto : dataList) {
//			BuildingSummary building = factory.createBuildingSummary();
//			building.setId(buildingDto.getId().toString());
//			building.setName(buildingDto.getName());
//			building.setAddress(buildingDto.getAddress());
//			building.setCountryName(buildingDto.getCountryName());
//			building.setRegionName(buildingDto.getRegionName());
//			building.setCityName(buildingDto.getCityName());
//			building.setNbFloors(BigInteger.valueOf(buildingDto.getNbFloors()));
//			buildingList.add(building);
//		}
		LOGGER.debug("List of buildings have rooms: nb = " + buildingList.size());
		LOGGER.debug( "End call LocationEndpointImpl.getBuildings  at: " + new Date() );
		return buildingList;
	}

	
}
