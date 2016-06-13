package com.orange.flexoffice.userui.ws.endPoint.entity.impl;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;

import com.orange.flexoffice.userui.ws.endPoint.data.impl.LocationEndpointImpl;
import com.orange.flexoffice.userui.ws.model.BuildingItem;
import com.orange.flexoffice.userui.ws.model.LocationItem;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocationEndpointImplTest {
	static {
	    try {
	      Log4jConfigurer.initLogging( "classpath:log4j-flexoffice-userui-test.xml" );
	    }
	    catch( FileNotFoundException ex ) {
	      System.err.println( "Cannot Initialize log4j" );
	    }
	  }

	private static ClassPathXmlApplicationContext context;

	private static LocationEndpointImpl locationEndpoint;
	
	@Context
	private UriInfo uriInfo;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void initSpringContextAndDatabase() throws Exception {
		context = new ClassPathXmlApplicationContext("applicationContext-flexoffice-userui-test.xml");
		locationEndpoint = (LocationEndpointImpl)context.getBean("locationEndpoint");
	}


	@Test
	public void TestA_initTables() {
		// SetUp
		boolean state = locationEndpoint.executeInitTestFile();

		// Asserts
		assertEquals(true, state);
	}

	//-----------------------------------------------------------------
	//							COUNTRY TESTS
	//-----------------------------------------------------------------
	@Test
	public void TestB_getCountries() {
		// Test
		List<LocationItem> countries = locationEndpoint.getCountries();
		// Asserts
		assertEquals(2, countries.size());
	}

	//-----------------------------------------------------------------
	//							REGION TESTS
	//-----------------------------------------------------------------
	@Test
	public void TestL_getRegionsByCountryId() {
		// Test
		List<LocationItem> regions = locationEndpoint.getRegions("1");
		// Asserts
		assertEquals(2, regions.size());
	}
	
	//-----------------------------------------------------------------
	//							CITIES TESTS
	//-----------------------------------------------------------------
	@Test
	public void TestQ_getCitiesByRegionId() {
		// Test
		List<LocationItem> cities = locationEndpoint.getCities("1");
		// Asserts
		assertEquals(2, cities.size());
	}
	
	//-----------------------------------------------------------------
	//							BUILDINGS TESTS
	//-----------------------------------------------------------------
	@Test
	public void TestV_getBuildingsByCityId() {
		// Test
		List<BuildingItem> buildings = locationEndpoint.getBuildings("1");
		// Asserts
		assertEquals(4, buildings.size());
	}
	
}
