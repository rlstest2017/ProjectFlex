package com.orange.flexoffice.adminui.ws.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.adminui.ws.model.CitySummary;
import com.orange.flexoffice.adminui.ws.model.ObjectFactory;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.business.common.service.data.CityManager;
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
