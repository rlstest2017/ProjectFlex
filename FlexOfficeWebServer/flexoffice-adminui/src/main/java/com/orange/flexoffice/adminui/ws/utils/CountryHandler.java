package com.orange.flexoffice.adminui.ws.utils;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.adminui.ws.model.LocationInput;
import com.orange.flexoffice.adminui.ws.model.LocationItem;
import com.orange.flexoffice.adminui.ws.model.ObjectFactory;
import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.business.common.service.data.CountryManager;
import com.orange.flexoffice.dao.common.model.data.CountryDao;

/**
 * CountryHandler
 * @author oab
 *
 */
public class CountryHandler {
	
	@Autowired
	private CountryManager countryManager;
	
	private final ObjectFactory factory = new ObjectFactory();
	private static final Logger LOGGER = Logger.getLogger(CountryHandler.class);
	
	/**
	 * getCountries
	 * @return
	 */
	public List<LocationItem> getCountries() {
		LOGGER.debug( "Begin call ConfigurationEndpoint.getCountries at: " + new Date() );
		List<CountryDao> dataList = countryManager.findAllCountries();
		List<LocationItem> countryList = new ArrayList<LocationItem>();
		for (CountryDao countryDao : dataList) {
			LocationItem country = factory.createLocationItem();
			country.setId(countryDao.getId().toString());
			country.setName(countryDao.getName());
			countryList.add(country);
		}
		LOGGER.debug("List of countries : nb = " + countryList.size());
		LOGGER.debug( "End call ConfigurationEndpoint.getCountries  at: " + new Date() );
		return countryList;
	}
	
	/**
	 * getCountriesHaveRooms
	 * @return
	 */
	public List<LocationItem> getCountriesLocation() {
		LOGGER.debug( "Begin call ConfigurationEndpoint.getCountriesHaveRooms at: " + new Date() );
		List<CountryDao> dataList = countryManager.findAllCountries();
		List<LocationItem> countryList = new ArrayList<LocationItem>();
		for (CountryDao countryDao : dataList) {
			LocationItem country = factory.createLocationItem();
			country.setId(countryDao.getId().toString());
			country.setName(countryDao.getName());
			countryList.add(country);
		}
		LOGGER.debug("List of countries have rooms : nb = " + countryList.size());
		LOGGER.debug( "End call ConfigurationEndpoint.getCountriesHaveRooms  at: " + new Date() );
		return countryList;
	}
	
	/**
	 * getCountry
	 * @param countryId
	 * @return
	 * @throws DataNotExistsException
	 */
	public LocationItem getCountry(String countryId) throws DataNotExistsException {
		CountryDao countryDao = countryManager.find(Long.valueOf(countryId));
		LocationItem country = factory.createLocationItem();
		country.setId(String.valueOf(countryDao.getId()));
		country.setName(countryDao.getName());
		return country;
	}

	/**
	 * addCountry
	 * @param country
	 * @return
	 */
	public LocationItem addCountry(LocationInput country) throws DataAlreadyExistsException {
		CountryDao countryDao = new CountryDao();
		countryDao.setName(country.getName());
		
		countryDao = countryManager.save(countryDao);
		LocationItem returnedCountry = factory.createLocationItem();
		returnedCountry.setId(countryDao.getColumnId());
		return factory.createLocationItem(returnedCountry).getValue();
	}
	
	/**
	 * updateCountry
	 * @param id
	 * @param country
	 * @return
	 * @throws DataNotExistsException
	 */
	public Response updateCountry(String id, LocationInput country) throws DataNotExistsException {
		CountryDao countryDao = new CountryDao();
		countryDao.setName(country.getName());
		countryManager.update(countryDao);
		return Response.status(Status.ACCEPTED).build();
	}

	/**
	 * removeCountry
	 * @param id
	 * @return
	 * @throws DataNotExistsException
	 * @throws IntegrityViolationException
	 */
	public Response removeCountry(String id) throws DataNotExistsException, IntegrityViolationException {
		countryManager.delete(Long.valueOf(id));
		return Response.noContent().build();
	}
}
