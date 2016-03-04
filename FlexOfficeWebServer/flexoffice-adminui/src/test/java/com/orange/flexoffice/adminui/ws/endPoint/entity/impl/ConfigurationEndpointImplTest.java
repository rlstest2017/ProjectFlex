package com.orange.flexoffice.adminui.ws.endPoint.entity.impl;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;

import com.orange.flexoffice.adminui.ws.endPoint.data.ConfigurationEndpoint;
import com.orange.flexoffice.adminui.ws.endPoint.data.impl.ConfigurationEndpointImpl;
import com.orange.flexoffice.adminui.ws.model.LocationInput;
import com.orange.flexoffice.adminui.ws.model.LocationItem;
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
	
}
