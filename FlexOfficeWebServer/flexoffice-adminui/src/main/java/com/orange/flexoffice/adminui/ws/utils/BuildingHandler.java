package com.orange.flexoffice.adminui.ws.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.SAXException;

import com.orange.flexoffice.adminui.ws.model.Building;
import com.orange.flexoffice.adminui.ws.model.BuildingInput;
import com.orange.flexoffice.adminui.ws.model.BuildingItem;
import com.orange.flexoffice.adminui.ws.model.BuildingSummary;
import com.orange.flexoffice.adminui.ws.model.LocationItem;
import com.orange.flexoffice.adminui.ws.model.ObjectFactory;
import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.business.common.exception.InvalidParametersException;
import com.orange.flexoffice.business.common.service.data.BuildingManager;
import com.orange.flexoffice.dao.common.model.data.BuildingDao;
import com.orange.flexoffice.dao.common.model.object.BuildingDto;
import com.orange.flexoffice.dao.common.model.object.BuildingSummaryDto;

/**
 * BuildingHandler
 * @author oab
 *
 */
public class BuildingHandler {
	
	@Autowired
	private BuildingManager buildingManager;
	
	private final ObjectFactory factory = new ObjectFactory();
	private static final Logger LOGGER = Logger.getLogger(BuildingHandler.class);
	
	/**
	 * getBuildings
	 * @return
	 */
	public List<BuildingSummary> getBuildings() {
		LOGGER.debug( "Begin call BuildingHandler.getBuildings at: " + new Date() );
		List<BuildingSummaryDto> dataList = buildingManager.findAllBuildings();
		List<BuildingSummary> buildingList = new ArrayList<BuildingSummary>();
		for (BuildingSummaryDto buildingDto : dataList) {
			BuildingSummary building = factory.createBuildingSummary();
			building.setId(buildingDto.getId().toString());
			building.setName(buildingDto.getName());
			building.setAddress(buildingDto.getAddress());
			building.setCountryName(buildingDto.getCountryName());
			building.setRegionName(buildingDto.getRegionName());
			building.setCityName(buildingDto.getCityName());
			building.setNbFloors(BigInteger.valueOf(buildingDto.getNbFloors()));
			buildingList.add(building);
		}
		LOGGER.debug("List of buildings : nb = " + buildingList.size());
		LOGGER.debug( "End call BuildingHandler.getBuildings  at: " + new Date() );
		return buildingList;
	}
    
	/**
	 * getBuildingsByCity
	 * @return
	 */
	public List<BuildingItem> getBuildingsByCity(String cityId) {
		LOGGER.debug( "Begin call BuildingHandler.getBuildingsByCity at: " + new Date() );
		List<BuildingDao> dataList = buildingManager.findBuildingsByCityId(cityId, true);
		List<BuildingItem> buildingList = new ArrayList<BuildingItem>();
		for (BuildingDao buildingDao : dataList) {
			BuildingItem building = factory.createBuildingItem();
			building.setBuildingId(buildingDao.getId().toString());
			building.setBuildingName(buildingDao.getName());
			building.setNbFloors(BigInteger.valueOf(buildingDao.getNbFloors()));
			buildingList.add(building);
		}
		LOGGER.debug("List of buildings have rooms: nb = " + buildingList.size());
		LOGGER.debug( "End call BuildingHandler.getBuildingsByCity  at: " + new Date() );
		return buildingList;
	}
	
	/**
	 * getBuilding
	 * @param buildingId
	 * @return
	 * @throws DataNotExistsException
	 */
	public Building getBuilding(String buildingId) throws DataNotExistsException {
		BuildingDto buidingDto = buildingManager.find(Long.valueOf(buildingId));
		Building building = factory.createBuilding();
		building.setId(String.valueOf(buidingDto.getId()));
		building.setName(buidingDto.getName());
		building.setAddress(buidingDto.getAddress());
		LocationItem locationCountry = factory.createLocationItem();
		locationCountry.setId(buidingDto.getCountryId().toString());
		locationCountry.setName(buidingDto.getCountryName());
		building.setCountry(locationCountry);
		LocationItem locationRegion = factory.createLocationItem();
		locationRegion.setId(buidingDto.getRegionId().toString());
		locationRegion.setName(buidingDto.getRegionName());
		building.setRegion(locationRegion);
		LocationItem locationCity = factory.createLocationItem();
		locationCity.setId(buidingDto.getCityId().toString());
		locationCity.setName(buidingDto.getCityName());
		building.setCity(locationCity);
		building.setNbFloors(BigInteger.valueOf(buidingDto.getNbFloors()));
		return building;
	}
	
	/**
	 * addBuilding
	 * @param building
	 * @return
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 * @throws JAXBException 
	 * @throws IOException 
	 * @throws DataNotExistsException 
	 */
	
	public BuildingItem addBuilding(BuildingInput building) throws DataAlreadyExistsException, UnsupportedEncodingException, FileNotFoundException, IOException, JAXBException, DataNotExistsException {
		BuildingDao buildingDao = new BuildingDao();
		buildingDao.setName(building.getName());
		buildingDao.setAddress(building.getAddress());
		buildingDao.setCityId(Long.valueOf(building.getCityId()));
		buildingDao.setNbFloors(building.getNbFloors().longValue());
		buildingDao = buildingManager.save(buildingDao);
		BuildingItem returnedBuilding = factory.createBuildingItem();
		returnedBuilding.setBuildingId(buildingDao.getColumnId());
		return factory.createBuildingItem(returnedBuilding).getValue();
	}
	
	/**
	 * updateBuilding
	 * @param id
	 * @param building
	 * @return
	 * @throws DataNotExistsException
	 * @throws JAXBException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws InvalidParametersException 
	 */
	public Response updateBuilding(String id, BuildingInput building) throws DataNotExistsException, IOException, JAXBException, ParserConfigurationException, SAXException, InvalidParametersException {
		BuildingDao buildingDao = new BuildingDao();
		buildingDao.setColumnId(id);
		buildingDao.setName(building.getName());
		buildingDao.setAddress(building.getAddress());
		buildingDao.setCityId(Long.valueOf(building.getCityId()));
		buildingDao.setNbFloors(building.getNbFloors().longValue());
		buildingManager.update(buildingDao);
		return Response.status(Status.ACCEPTED).build();
	}
	
	/**
	 * removeBuilding
	 * @param id
	 * @return
	 * @throws DataNotExistsException
	 * @throws IntegrityViolationException
	 * @throws JAXBException 
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public Response removeBuilding(String id) throws DataNotExistsException, IntegrityViolationException, NumberFormatException, IOException, JAXBException {
		buildingManager.delete(Long.valueOf(id));
		return Response.noContent().build();
	}
	
	
}
