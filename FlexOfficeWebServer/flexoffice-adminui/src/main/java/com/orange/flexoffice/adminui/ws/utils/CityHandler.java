package com.orange.flexoffice.adminui.ws.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.adminui.ws.model.City;
import com.orange.flexoffice.adminui.ws.model.CityInput;
import com.orange.flexoffice.adminui.ws.model.CitySummary;
import com.orange.flexoffice.adminui.ws.model.LocationItem;
import com.orange.flexoffice.adminui.ws.model.ObjectFactory;
import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.business.common.service.data.CityManager;
import com.orange.flexoffice.dao.common.model.data.CityDao;
import com.orange.flexoffice.dao.common.model.object.CityDto;
import com.orange.flexoffice.dao.common.model.object.CitySummaryDto;

/**
 * CityHandler
 * @author oab
 *
 */
public class CityHandler {
	
	@Autowired
	private CityManager cityManager;
	
	private final ObjectFactory factory = new ObjectFactory();
	private static final Logger LOGGER = Logger.getLogger(CityHandler.class);
	
	/**
	 * getCities
	 * @return
	 */
	public List<CitySummary> getCities() {
		LOGGER.debug( "Begin call ConfigurationEndpoint.getCities at: " + new Date() );
		List<CitySummaryDto> dataList = cityManager.findAllCities();
		List<CitySummary> cityList = new ArrayList<CitySummary>();
		for (CitySummaryDto cityDto : dataList) {
			CitySummary city = factory.createCitySummary();
			city.setId(cityDto.getId().toString());
			city.setName(cityDto.getName());
			city.setCountryName(cityDto.getCountryName());
			city.setRegionName(cityDto.getRegionName());
			cityList.add(city);
		}
		LOGGER.debug("List of cities : nb = " + cityList.size());
		LOGGER.debug( "End call ConfigurationEndpoint.getCities  at: " + new Date() );
		return cityList;
	}
	
	/**
	 * getCitiesHaveRooms
	 * @return
	 */
	public List<LocationItem> getCitiesByRegion(String regionId) {
		LOGGER.debug( "Begin call ConfigurationEndpoint.getCitiesHaveRooms at: " + new Date() );
		//List<CitySummaryDto> dataList = cityManager.findAllCities();
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
		LOGGER.debug( "End call ConfigurationEndpoint.getCitiesHaveRooms  at: " + new Date() );
		return cityList;
	}
	
	/**
	 * getCity
	 * @param cityId
	 * @return
	 * @throws DataNotExistsException
	 */
	public City getCity(String cityId) throws DataNotExistsException {
		CityDto cityDto = cityManager.find(Long.valueOf(cityId));
		City city = factory.createCity();
		city.setId(String.valueOf(cityDto.getId()));
		city.setName(cityDto.getName());
		LocationItem locationCountry = factory.createLocationItem();
		locationCountry.setId(cityDto.getCountryId().toString());
		locationCountry.setName(cityDto.getCountryName());
		city.setCountry(locationCountry);
		LocationItem locationRegion = factory.createLocationItem();
		locationRegion.setId(cityDto.getRegionId().toString());
		locationRegion.setName(cityDto.getRegionName());
		city.setRegion(locationRegion);
		return city;
	}

	/**
	 * addCity
	 * @param city
	 * @return
	 */
	public City addCity(CityInput city) throws DataAlreadyExistsException {
		CityDao cityDao = new CityDao();
		cityDao.setName(city.getName());
		cityDao.setRegionId(Long.valueOf(city.getRegionId()));
		cityDao = cityManager.save(cityDao);
		City returnedCity = factory.createCity();
		returnedCity.setId(cityDao.getColumnId());
		return factory.createCity(returnedCity).getValue();
	}

	/**
	 * 
	 * @param id
	 * @param city
	 * @return
	 * @throws DataNotExistsException
	 */
	public Response updateCity(String id, CityInput city) throws DataNotExistsException {
		CityDao cityDao = new CityDao();
		cityDao.setName(city.getName());
		cityDao.setRegionId(Long.valueOf(city.getRegionId()));
		cityManager.update(cityDao);
		return Response.status(Status.ACCEPTED).build();
	}
	
	/**
	 * removeCity
	 * @param id
	 * @return
	 * @throws DataNotExistsException
	 * @throws IntegrityViolationException
	 */
	public Response removeCity(String id) throws DataNotExistsException, IntegrityViolationException {
		cityManager.delete(Long.valueOf(id));
		return Response.noContent().build();
	}
		
}
