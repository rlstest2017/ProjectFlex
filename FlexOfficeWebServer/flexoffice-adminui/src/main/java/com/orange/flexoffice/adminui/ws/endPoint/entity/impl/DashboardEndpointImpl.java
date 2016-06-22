package com.orange.flexoffice.adminui.ws.endPoint.entity.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.adminui.ws.endPoint.entity.DashboardEndpoint;
import com.orange.flexoffice.adminui.ws.model.BuildingOutput;
import com.orange.flexoffice.adminui.ws.model.Dashboard;
import com.orange.flexoffice.adminui.ws.model.DashboardInput;
import com.orange.flexoffice.adminui.ws.model.DashboardInput2;
import com.orange.flexoffice.adminui.ws.model.DashboardSummary;
import com.orange.flexoffice.adminui.ws.model.EDashboardStatus;
import com.orange.flexoffice.adminui.ws.model.Location;
import com.orange.flexoffice.adminui.ws.model.LocationItem;
import com.orange.flexoffice.adminui.ws.model.ObjectFactory;
import com.orange.flexoffice.adminui.ws.utils.ErrorMessageHandler;
import com.orange.flexoffice.business.common.enums.EnumErrorModel;
import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.business.common.service.data.BuildingManager;
import com.orange.flexoffice.business.common.service.data.CityManager;
import com.orange.flexoffice.business.common.service.data.DashboardManager;
import com.orange.flexoffice.business.common.service.data.TestManager;
import com.orange.flexoffice.dao.common.model.data.DashboardDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_DashboardStatus;
import com.orange.flexoffice.dao.common.model.object.BuildingDto;
import com.orange.flexoffice.dao.common.model.object.CityDto;
import com.orange.flexoffice.dao.common.model.object.DashboardDto;


public class DashboardEndpointImpl implements DashboardEndpoint {
	
	private static final Logger LOGGER = Logger.getLogger(DashboardEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();
		
	@Autowired
	private DashboardManager dashboardManager;
	@Autowired
	private BuildingManager buildingManager;
	@Autowired
	private CityManager cityManager;
	@Autowired
	private TestManager testManager;
	@Autowired
	private ErrorMessageHandler errorMessageHandler;
	
	@Override
	public List<DashboardSummary> getDashboards() {
		LOGGER.debug( "Begin call DashboardEndpointImpl.getDashboards at: " + new Date() );
		
		try {
			List<DashboardDao> dataList = dashboardManager.findAllDashboards();
			
			if (dataList == null) {
				throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_64, Response.Status.NOT_FOUND));
			}
			
			LOGGER.debug("List of dashboards : " + dataList.size());
			
			List<DashboardSummary> dashboardList = new ArrayList<DashboardSummary>();
			
			for (DashboardDao dashboardDao : dataList) {
				DashboardSummary dashboard = factory.createDashboardSummary();
				dashboard.setMacAddress(dashboardDao.getMacAddress());
				dashboard.setName(dashboardDao.getName());
				if (dashboardDao.getStatus().equals(E_DashboardStatus.ONLINE.toString())) {
					dashboard.setStatus(EDashboardStatus.ONLINE);
				} else if (dashboardDao.getStatus().equals(E_DashboardStatus.OFFLINE.toString())) {
					dashboard.setStatus(EDashboardStatus.OFFLINE);
				} else if (dashboardDao.getStatus().equals(E_DashboardStatus.STANDBY.toString())){
					dashboard.setStatus(EDashboardStatus.STANDBY);
				} else {
					dashboard.setStatus(EDashboardStatus.ECONOMIC);
				}
				if (dashboardDao.getLastMeasureDate() != null) {
					dashboard.setLastMeasureDate(dashboardDao.getLastMeasureDate().getTime());
				}
				
				dashboardList.add(dashboard);
			}
			
			LOGGER.debug( "End call DashboardEndpointImpl.getDashboards  at: " + new Date() );
			
			return dashboardList;
		} catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in getDashboards() DashboardEndpointImpl with message :" + ex.getMessage(), ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}
	}

	@Override
	public Dashboard getDashboard(String macAddress) {
		LOGGER.debug( "Begin call DashboardEndpointImpl.getDashboard at: " + new Date() );
		
		
		try {
			DashboardDto data = dashboardManager.findByMacAddress(macAddress.toLowerCase().replaceAll("-", ":"));
			
			Dashboard dashboard = factory.createDashboard();
			dashboard.setId(data.getId());
			dashboard.setMacAddress(data.getMacAddress());
			dashboard.setName(data.getName());
			dashboard.setDesc(data.getDescription());
			if (data.getStatus() == E_DashboardStatus.ONLINE) {
				dashboard.setStatus(EDashboardStatus.ONLINE);
			} else if (data.getStatus() == E_DashboardStatus.OFFLINE) {
				dashboard.setStatus(EDashboardStatus.OFFLINE);
			} else if(data.getStatus() == E_DashboardStatus.STANDBY){
				dashboard.setStatus(EDashboardStatus.STANDBY);
			} else {
				dashboard.setStatus(EDashboardStatus.ECONOMIC);
			}
			
			if (data.getLastMeasureDate() != null) {
				dashboard.setLastMeasureDate(data.getLastMeasureDate().getTime());
			}
			
			if (data.getBuildingId() != null){
				BuildingDto buidingDto = buildingManager.find(Long.valueOf(data.getBuildingId()));
				Location location = factory.createLocation();
				BuildingOutput building = factory.createBuildingOutput();
				building.setId(String.valueOf(buidingDto.getId()));
				building.setName(buidingDto.getName());
				building.setAddress(buidingDto.getAddress());
				building.setNbFloors(BigInteger.valueOf(buidingDto.getNbFloors()));
				location.setBuilding(building);
				LocationItem locationCountry = factory.createLocationItem();
				locationCountry.setId(buidingDto.getCountryId().toString());
				locationCountry.setName(buidingDto.getCountryName());
			    location.setCountry(locationCountry);
				LocationItem locationRegion = factory.createLocationItem();
				locationRegion.setId(buidingDto.getRegionId().toString());
				locationRegion.setName(buidingDto.getRegionName());
				location.setRegion(locationRegion);
				LocationItem locationCity = factory.createLocationItem();
				locationCity.setId(buidingDto.getCityId().toString());
				locationCity.setName(buidingDto.getCityName());
				location.setCity(locationCity);
				if (data.getFloor() != null)
				location.setFloor(BigInteger.valueOf(data.getFloor()));
				
				dashboard.setLocation(location);
			} else {
				CityDto cityDto = cityManager.find(Long.valueOf(data.getCityId()));
				Location location = factory.createLocation();
				LocationItem locationCountry = factory.createLocationItem();
				locationCountry.setId(cityDto.getCountryId().toString());
				locationCountry.setName(cityDto.getCountryName());
			    location.setCountry(locationCountry);
				LocationItem locationRegion = factory.createLocationItem();
				locationRegion.setId(cityDto.getRegionId().toString());
				locationRegion.setName(cityDto.getRegionName());
				location.setRegion(locationRegion);
				LocationItem locationCity = factory.createLocationItem();
				locationCity.setId(cityDto.getId().toString());
				locationCity.setName(cityDto.getName());
				location.setCity(locationCity);
				
				dashboard.setLocation(location);
			}
			
			LOGGER.debug( "End call DashboardEndpointImpl.getDashboard  at: " + new Date() );
			
			return factory.createDashboard(dashboard).getValue();
			
		} catch (DataNotExistsException e){
			LOGGER.debug("DataNotExistsException in getDashboard() DashboardEndpointImpl with message :" + e.getMessage(), e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_68, Response.Status.NOT_FOUND));
		} catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in getDashboard() DashboardEndpointImpl with message :" + ex.getMessage(), ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}
	}

	@Override
	public Dashboard addDashboard(DashboardInput dashboard) {
		LOGGER.debug( "Begin call doPost method for DashboardEndpoint at: " + new Date() );

		DashboardDao dashboardDao = new DashboardDao();
		dashboardDao.setMacAddress(dashboard.getMacAddress().toLowerCase().replaceAll("-", ":"));
		dashboardDao.setName(dashboard.getName().trim());
		dashboardDao.setDescription(dashboard.getDesc());
		dashboardDao.setCityId(Long.valueOf(dashboard.getCityId()));
		
		if (dashboard.getBuildingId() != null){
			dashboardDao.setBuildingId(Long.valueOf(dashboard.getBuildingId()));
		}
		
		if (dashboard.getFloor() != null){
			dashboardDao.setFloor(dashboard.getFloor().longValue());
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "Begin call addDashboard(DashbaordInput dashboard) method of DashbaordEndpoint, with parameters :");
			final StringBuilder message = new StringBuilder( 1000 );
			message.append( "\n" );
			message.append( "macAddress :" );
			message.append( dashboard.getMacAddress() );
			message.append( "name :" );
			message.append( dashboard.getName() );
			message.append( "\n" );
			message.append( "desc :" );
			message.append( dashboard.getDesc() );
			message.append( "\n" );
			LOGGER.debug( message.toString() );
		}

		try {
			dashboardDao = dashboardManager.save(dashboardDao);

		} catch (DataAlreadyExistsException e) {
			LOGGER.debug("DataAlreadyExistsException in addDashboard() DashboardEndpointImpl with message :" + e.getMessage(), e);			
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_65, Response.Status.METHOD_NOT_ALLOWED));

		} catch (RuntimeException ex) {
			LOGGER.debug("RuntimeException in addDashboard() DashboardEndpointImpl with message :" + ex.getMessage(), ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}


		Dashboard returnedDashboard = factory.createDashboard();
		returnedDashboard.setMacAddress(dashboardDao.getMacAddress());

		LOGGER.debug( "End call doPost method for DashbaordEndpoint at: " + new Date() );

		return factory.createDashboard(returnedDashboard).getValue();

	}

	@Override
	public Response updateDashboard(String macAddress, DashboardInput2 dashboard) {
		LOGGER.debug( "Begin call doPut method for DashboardEndpoint at: " + new Date() );

		DashboardDao dashboardDao = new DashboardDao();
		dashboardDao.setMacAddress(macAddress.toLowerCase().replaceAll("-", ":"));
		dashboardDao.setName(dashboard.getName().trim());
		dashboardDao.setDescription(dashboard.getDesc());
		if (dashboard.getBuildingId() != null){
			dashboardDao.setBuildingId(Long.valueOf(dashboard.getBuildingId()));
			if (dashboard.getFloor() != null){
				dashboardDao.setFloor(dashboard.getFloor().longValue());
			} else {
				dashboard.setFloor(null);
			}
		} else {
			dashboard.setBuildingId(null);
		}
		dashboardDao.setCityId(Long.valueOf(dashboard.getCityId()));
		

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug( "Begin call updateDashboard(String id, DashboardInput2 dashboard) method of DashboardEndpoint, with parameters :");
			final StringBuilder message = new StringBuilder( 1000 );
			message.append( "macAddress :" );
			message.append( macAddress.toLowerCase().replaceAll("-", ":") );
			message.append( "\n" );
			message.append( "name :" );
			message.append( dashboard.getName() );
			message.append( "\n" );
			message.append( "desc :" );
			message.append( dashboard.getDesc() );
			message.append( "\n" );
			LOGGER.debug( message.toString() );
		}

		try {
			dashboardDao = dashboardManager.update(dashboardDao);

		} catch (DataNotExistsException e) {
			LOGGER.debug("DataNotExistsException in updateDashboard() DashboardEndpointImpl with message :" + e.getMessage(), e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_66, Response.Status.NOT_FOUND));
		} catch (RuntimeException ex) {
			LOGGER.debug("RuntimeException in updateDahboard() DashboardEndpointImpl with message :" + ex.getMessage(), ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}


		Dashboard returnedDashboard = factory.createDashboard();
		returnedDashboard.setMacAddress(dashboardDao.getMacAddress());

		LOGGER.debug( "End call doPut method for DashboardEndpoint at: " + new Date() );

		return Response.status(Status.ACCEPTED).build();
	}

	@Override
	public Response removeDashboard(String macAddress) {
		try {

			dashboardManager.delete(macAddress.toLowerCase().replaceAll("-", ":"));

		} catch (DataNotExistsException e){
			LOGGER.debug("DataNotExistsException in removeDashboard() DashboardEndpointImpl with message :" + e.getMessage(), e);			
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_67, Response.Status.NOT_FOUND));
			
		} catch (IntegrityViolationException e){
			LOGGER.debug("IntegrityViolationException in DashbaordEndpoint.removeDashboard with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_67, Response.Status.METHOD_NOT_ALLOWED));
		} catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in removeDashbaord() DashboardEndpointImpl with message :" + ex.getMessage(), ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}

		return Response.noContent().build();
	}

	@Override
	public boolean executeInitTestFile() {
		return testManager.executeInitTestFile();
	}

	@Override
	public boolean executeDropTables() {
		return testManager.executeDropTables();
	}

	@Override
	public DashboardDto findByMacAddress(String macAddress) throws DataNotExistsException {
		return dashboardManager.findByMacAddress(macAddress.toLowerCase().replaceAll("-", ":"));
	}
}
