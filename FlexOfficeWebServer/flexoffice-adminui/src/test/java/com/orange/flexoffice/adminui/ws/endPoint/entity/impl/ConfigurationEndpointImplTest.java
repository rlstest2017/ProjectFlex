package com.orange.flexoffice.adminui.ws.endPoint.entity.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;

import com.orange.flexoffice.adminui.ws.endPoint.data.ConfigurationEndpoint;
import com.orange.flexoffice.adminui.ws.endPoint.data.impl.ConfigurationEndpointImpl;
import com.orange.flexoffice.adminui.ws.model.Building;
import com.orange.flexoffice.adminui.ws.model.BuildingInput;
import com.orange.flexoffice.adminui.ws.model.BuildingItem;
import com.orange.flexoffice.adminui.ws.model.BuildingSummary;
import com.orange.flexoffice.adminui.ws.model.City;
import com.orange.flexoffice.adminui.ws.model.CityInput;
import com.orange.flexoffice.adminui.ws.model.CitySummary;
import com.orange.flexoffice.adminui.ws.model.LocationInput;
import com.orange.flexoffice.adminui.ws.model.LocationItem;
import com.orange.flexoffice.adminui.ws.model.Region;
import com.orange.flexoffice.adminui.ws.model.RegionInput;
import com.orange.flexoffice.adminui.ws.model.RegionSummary;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConfigurationEndpointImplTest {
	static {
	    try {
	      Log4jConfigurer.initLogging( "classpath:log4j-flexoffice-adminui-test.xml" );
	    }
	    catch( FileNotFoundException ex ) {
	      System.err.println( "Cannot Initialize log4j" );
	    }
	  }

	private static ClassPathXmlApplicationContext context;

	private static ConfigurationEndpoint configurationEndpoint;
	
	@Context
	private UriInfo uriInfo;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void initSpringContextAndDatabase() throws Exception {
		context = new ClassPathXmlApplicationContext("applicationContext-flexoffice-adminui-test.xml");
		configurationEndpoint = (ConfigurationEndpointImpl)context.getBean("configurationEndpoint");
	}


	@Test
	public void TestA_initTables() {
		// SetUp
		boolean state = configurationEndpoint.executeInitTestFile();

		// Asserts
		assertEquals(true, state);
	}

	//-----------------------------------------------------------------
	//							COUNTRY TESTS
	//-----------------------------------------------------------------
	@Test
	public void TestB_getCountries() {
		// Test
		List<LocationItem> countries = configurationEndpoint.getCountries();
		// Asserts
		assertEquals(3, countries.size());
	}

	@Test
	public void TestC_addCountry() throws WebApplicationException {
		// Setup
		final LocationInput country = new LocationInput();
		country.setName("country 4");
		// Test
		final LocationItem response = configurationEndpoint.addCountry(country);
		// Asserts
		assertNotNull(response.getId());
		// Test
		List<LocationItem> countries = configurationEndpoint.getCountries();
		// Asserts
		assertEquals(4, countries.size());
	}

	@Test
	public void TestD_addCountryAlreadyExists() {
		// Setup
		boolean expectedResult = false;
		final LocationInput country = new LocationInput();
		country.setName("country 1");
		try {
			// Test
			configurationEndpoint.addCountry(country);
		} catch (WebApplicationException e) {
			expectedResult = true;
		}
		// Asserts
		assertEquals(true, expectedResult);	
	}

	@Test
	public void TestE_getCountryByCountryId() {
		// Test
		LocationItem country = configurationEndpoint.getCountry("1");
		// Asserts
		assertEquals("country 1", country.getName());
	}
	
	@Test
	public void TestF_getWrongCountryDataNotExistsException() {
		// Setup
		boolean expectedResult = false;
		// Test
		try {
			// Test
			configurationEndpoint.getCountry("125");
		} catch (WebApplicationException e) {
			expectedResult = true;
		}
		// Asserts
		assertEquals(true, expectedResult);	
	}
	
	@Test
	public void TestG_updateCountry() throws WebApplicationException {
		// Setup
		final LocationInput country = new LocationInput();
		country.setName("country 3 update");
		// Test
		final Response response = configurationEndpoint.updateCountry("3", country);
		// Asserts
		assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void TestK_removeCountry() throws WebApplicationException {
		// Test
		Response response = configurationEndpoint.removeCountry("3");
		// Assert
		assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void TestKA_removeCountryDataNotExistsException() {
		// Setup
		boolean expectedResult = false;
		// Test
		try {
			// Test
			configurationEndpoint.removeCountry("-1");
			
		} catch (WebApplicationException e) {
			expectedResult = true;	
		}

		// Assert
		assertEquals(true, expectedResult);	
	}
	
	@Test
	public void TestKB_removeCountryIntegrityViolationException() {
		// Setup
		boolean expectedResult = false;
		// Test
		try {
			// Test
			configurationEndpoint.removeCountry("1");
			
		} catch (WebApplicationException e) {
			expectedResult = true;	
		}

		// Assert
		assertEquals(true, expectedResult);	
	}
	//-----------------------------------------------------------------
	//							REGION TESTS
	//-----------------------------------------------------------------
	@Test
	public void TestL_getRegions() {
		// Test
		List<RegionSummary> regions = configurationEndpoint.getRegions();
		// Asserts
		assertEquals(3, regions.size());
	}
	
	@Test
	public void TestM_addRegion() throws WebApplicationException {
		// Setup
		final RegionInput region = new RegionInput();
		region.setName("region 4");
		region.setCountryId("2");
		// Test
		final Region response = configurationEndpoint.addRegion(region);
		// Asserts
		assertNotNull(response.getId());
	}
	
	@Test
	public void TestN_getRegionByRegionId() {
		// Test
		Region region = configurationEndpoint.getRegion("1");
		// Asserts
		assertEquals("region 1", region.getName());
	}
	
	@Test
	public void TestO_updateRegion() throws WebApplicationException {
		// Setup
		final RegionInput region = new RegionInput();
		region.setName("region 3 update");
		region.setCountryId("1");
		// Test
		final Response response = configurationEndpoint.updateRegion("3", region);
		// Asserts
		assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void TestP_removeRegion() throws WebApplicationException {
		// Test
		Response response = configurationEndpoint.removeRegion("3");
		// Assert
		assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
	}

	@Test
	public void TestPA_removeRegionDataNotExistsException() {
		// Setup
		boolean expectedResult = false;
		// Test
		try {
			// Test
			configurationEndpoint.removeRegion("-1");
		} catch (WebApplicationException e) {
			expectedResult = true;	
		}
		// Assert
		assertEquals(true, expectedResult);	
	}
	
	@Test
	public void TestPB_removeRegionIntegrityViolationException() {
		// Setup
		boolean expectedResult = false;
		// Test
		try {
			// Test
			configurationEndpoint.removeRegion("1");
		} catch (WebApplicationException e) {
			expectedResult = true;	
		}
		// Assert
		assertEquals(true, expectedResult);	
	}

	//-----------------------------------------------------------------
	//							CITIES TESTS
	//-----------------------------------------------------------------
	@Test
	public void TestQ_getCities() {
		// Test
		List<CitySummary> cities = configurationEndpoint.getCities();
		// Asserts
		assertEquals(3, cities.size());
	}
	
	@Test
	public void TestR_addCity() throws WebApplicationException {
		// Setup
		final CityInput city = new CityInput();
		city.setName("city 4");
		city.setRegionId("2");
		// Test
		final City response = configurationEndpoint.addCity(city);
		// Asserts
		assertNotNull(response.getId());
	}
	
	@Test
	public void TestS_getCityByCityId() {
		// Test
		City city = configurationEndpoint.getCity("1");
		// Asserts
		assertEquals("city 1", city.getName());
	}
	
	@Test
	public void TestT_updateCity() throws WebApplicationException {
		// Setup
		final CityInput city = new CityInput();
		city.setName("city 3 update");
		city.setRegionId("1");
		// Test
		final Response response = configurationEndpoint.updateCity("3", city);
		// Asserts
		assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void TestU_removeCity() throws WebApplicationException {
		// Test
		Response response = configurationEndpoint.removeCity("3");
		// Assert
		assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void TestUA_removeCityDataNotExistsException() {
		// Setup
		boolean expectedResult = false;
		// Test
		try {
			// Test
			configurationEndpoint.removeCity("-1");
		} catch (WebApplicationException e) {
			expectedResult = true;	
		}
		// Assert
		assertEquals(true, expectedResult);	
	}
	
	@Test
	public void TestUB_removeCityIntegrityViolationException() {
		// Setup
		boolean expectedResult = false;
		// Test
		try {
			// Test
			configurationEndpoint.removeCity("1");
		} catch (WebApplicationException e) {
			expectedResult = true;	
		}
		// Assert
		assertEquals(true, expectedResult);	
	}

	//-----------------------------------------------------------------
	//							BUILDINGS TESTS
	//-----------------------------------------------------------------
	@Test
	public void TestV_getBuildings() {
		// Test
		List<BuildingSummary> buildings = configurationEndpoint.getBuildings();
		// Asserts
		assertEquals(3, buildings.size());
	}
	
	@Test
	public void TestW_addBuilding() throws WebApplicationException {
		// Setup
		final BuildingInput building = new BuildingInput();
		building.setName("building 4");
		building.setCityId("2");
		building.setAddress("05 rue de la gloire 35980 Rennes");
		building.setNbFloors(BigInteger.valueOf(10l));
		// Test
		final BuildingItem response = configurationEndpoint.addBuilding(building);
		// Asserts
		assertNotNull(response.getBuildingId());
	}

	@Test
	public void TestX_getBuildingByBuildingId() {
		// Test
		Building building = configurationEndpoint.getBuilding("1");
		// Asserts
		assertEquals("building 1", building.getName());
	}

	@Test
	public void TestY_updateBuilding() throws WebApplicationException {
		// Setup
		final BuildingInput building = new BuildingInput();
		building.setName("building 4 update");
		building.setCityId("2");
		building.setAddress("05 rue de la gloire 35000 Rennes");
		building.setNbFloors(BigInteger.valueOf(10l));
		String id = null;
		List<BuildingSummary> lst = configurationEndpoint.getBuildings();
		for(BuildingSummary buildingSummary : lst){
			if (buildingSummary.getCityName().equals("building 4"));
			id = buildingSummary.getId();
		}
		// Test
		final Response response = configurationEndpoint.updateBuilding(id, building);
		// Asserts
		assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void TestZ_removeBuilding() throws WebApplicationException {
		String id = null;
		List<BuildingSummary> lst = configurationEndpoint.getBuildings();
		for(BuildingSummary buildingSummary : lst){
			if (buildingSummary.getName().equals("building 4 update")){
				id = buildingSummary.getId();
				break;
			}
		}
		// Test
		Response response = configurationEndpoint.removeBuilding(id);
		// Assert
		assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
	}

	@Test
	public void TestZA_removeBuildingDataNotExistsException() {
		// Setup
		boolean expectedResult = false;
		// Test
		try {
			// Test
			configurationEndpoint.removeBuilding("-1");
		} catch (WebApplicationException e) {
			expectedResult = true;	
		}
		// Assert
		assertEquals(true, expectedResult);	
	}
	
	@Test
	public void TestZB_removeBuildingIntegrityViolationException() {
		// Setup
		boolean expectedResult = false;
		// Test
		try {
			// Test
			configurationEndpoint.removeBuilding("1");
		} catch (WebApplicationException e) {
			expectedResult = true;	
		}
		// Assert
		assertEquals(true, expectedResult);	
	}


}
