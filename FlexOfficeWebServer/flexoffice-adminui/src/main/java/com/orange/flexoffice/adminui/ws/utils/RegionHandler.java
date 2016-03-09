package com.orange.flexoffice.adminui.ws.utils;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.adminui.ws.model.LocationItem;
import com.orange.flexoffice.adminui.ws.model.ObjectFactory;
import com.orange.flexoffice.adminui.ws.model.Region;
import com.orange.flexoffice.adminui.ws.model.RegionInput;
import com.orange.flexoffice.adminui.ws.model.RegionSummary;
import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.business.common.service.data.RegionManager;
import com.orange.flexoffice.dao.common.model.data.RegionDao;
import com.orange.flexoffice.dao.common.model.object.RegionDto;
import com.orange.flexoffice.dao.common.model.object.RegionSummaryDto;

/**
 * RegionHandler
 * @author oab
 *
 */
public class RegionHandler {
	
	@Autowired
	private RegionManager regionManager;
	
	private final ObjectFactory factory = new ObjectFactory();
	private static final Logger LOGGER = Logger.getLogger(RegionHandler.class);

	/**
	 * getRegions
	 * @return
	 */
	public List<RegionSummary> getRegions() {
		LOGGER.debug( "Begin call RegionHandler.getRegions at: " + new Date() );
		List<RegionSummaryDto> dataList = regionManager.findAllRegions();
		List<RegionSummary> regionList = new ArrayList<RegionSummary>();
		for (RegionSummaryDto regionDto : dataList) {
			RegionSummary region = factory.createRegionSummary();
			region.setId(regionDto.getId().toString());
			region.setName(regionDto.getName());
			region.setCountryName(regionDto.getCountryName());
			regionList.add(region);
		}
		LOGGER.debug("List of regions : nb = " + regionList.size());
		LOGGER.debug( "End call RegionHandler.getRegions  at: " + new Date() );
		return regionList;
	}
	
	/**
	 * getRegionsByCountry
	 * @return
	 */
	public List<LocationItem> getRegionsByCountry(String countryId, boolean isFromAdminUI) {
		LOGGER.debug( "Begin call RegionHandler.getRegionsByCountry at: " + new Date() );
		List<RegionDao> dataList = regionManager.findRegionsByCountryId(countryId, true);
		List<LocationItem> regionList = new ArrayList<LocationItem>();
		for (RegionDao regionDao : dataList) {
			LocationItem region = factory.createLocationItem();
			region.setId(regionDao.getId().toString());
			region.setName(regionDao.getName());
			regionList.add(region);
		}
		LOGGER.debug("List of regions have rooms : nb = " + regionList.size());
		LOGGER.debug( "End call RegionHandler.getRegionsByCountry  at: " + new Date() );
		return regionList;
	}

	/**
	 * getRegion
	 * @param regionId
	 * @return
	 * @throws DataNotExistsException
	 */
	public Region getRegion(String regionId) throws DataNotExistsException {
		RegionDto regionDto = regionManager.find(Long.valueOf(regionId));
		Region region = factory.createRegion();
		region.setId(String.valueOf(regionDto.getId()));
		region.setName(regionDto.getName());
		LocationItem location = factory.createLocationItem();
		location.setId(regionDto.getCountryId().toString());
		location.setName(regionDto.getCountryName());
		region.setCountry(location);
		return region;
	}
	
	/**
	 * addRegion
	 * @param region
	 * @return
	 */
	public Region addRegion(RegionInput region) throws DataAlreadyExistsException {
		RegionDao regionDao = new RegionDao();
		regionDao.setName(region.getName());
		regionDao.setCountryId(Long.valueOf(region.getCountryId()));
		
		regionDao = regionManager.save(regionDao);
		Region returnedRegion = factory.createRegion();
		returnedRegion.setId(regionDao.getColumnId());
		return factory.createRegion(returnedRegion).getValue();
	}
	
	/**
	 * updateRegion
	 * @param id
	 * @param region
	 * @return
	 * @throws DataNotExistsException
	 */
	public Response updateRegion(String id, RegionInput region) throws DataNotExistsException {
		RegionDao regionDao = new RegionDao();
		regionDao.setColumnId(id);
		regionDao.setName(region.getName());
		regionDao.setCountryId(Long.valueOf(region.getCountryId()));
		regionManager.update(regionDao);
		return Response.status(Status.ACCEPTED).build();
	}
	
	/**
	 * removeRegion
	 * @param id
	 * @return
	 * @throws DataNotExistsException
	 * @throws IntegrityViolationException
	 */
	public Response removeRegion(String id) throws DataNotExistsException, IntegrityViolationException {
		regionManager.delete(Long.valueOf(id));
		return Response.noContent().build();
	}
}
