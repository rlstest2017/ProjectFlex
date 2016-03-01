package com.orange.flexoffice.userui.ws.endPoint.data.impl;

import java.util.List;

import org.apache.log4j.Logger;

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
	
	@Override
	public List<LocationItem> getCountries() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<LocationItem> getRegions(String countryId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<LocationItem> getCities(String regionId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<BuildingItem> getBuildings(String cityId) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
