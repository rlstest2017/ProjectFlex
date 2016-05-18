package com.orange.flexoffice.business.common.utils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.service.data.BuildingManager;
import com.orange.flexoffice.business.common.service.data.CityManager;
import com.orange.flexoffice.dao.common.model.object.BuildingDto;
import com.orange.flexoffice.dao.common.model.object.CityDto;

/**
 * AddressTools
 * @author oab
 *
 */
@Service("AddressTools")
public class AddressTools {
	@Autowired
	private CityManager cityManager;
	@Autowired
	private BuildingManager buildingManager;
	
	private static final Logger LOGGER = Logger.getLogger(AddressTools.class);
	
	public String getCountryRegionCityNamesFromCityId(Long cityId) throws NumberFormatException, DataNotExistsException{
		CityDto cityDto = cityManager.find(cityId);
		
		LOGGER.debug("getCountryRegionCityNamesFromCityId for city" + cityId);
		
		return "rg_" + cityDto.getCountryName() + "_" + cityDto.getRegionName() + "_" + cityDto.getName();
	}
	
	public String getCountryRegionCityBuildingNamesFromBuildingId(Long buildingId) throws NumberFormatException, DataNotExistsException{
		BuildingDto buildingDto = buildingManager.find(buildingId);
		
		LOGGER.debug("getCountryRegionCityBuildingNamesBuildingId for building" + buildingId);
		
		return "rg_" + buildingDto.getCountryName() + "_" + buildingDto.getRegionName() + "_" + buildingDto.getCityName() +  "_" + buildingDto.getName();
	}

}
