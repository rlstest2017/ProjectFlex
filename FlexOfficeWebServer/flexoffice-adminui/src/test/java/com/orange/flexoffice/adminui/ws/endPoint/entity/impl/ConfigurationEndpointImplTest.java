package com.orange.flexoffice.adminui.ws.endPoint.entity.impl;

import static org.junit.Assert.*;

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

import com.orange.flexoffice.adminui.ws.endPoint.data.ConfigurationEndpoint;
import com.orange.flexoffice.adminui.ws.endPoint.data.impl.ConfigurationEndpointImpl;
import com.orange.flexoffice.adminui.ws.model.LocationItem;


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

	@Test
	public void TestB_getCountries() {
		// Test
		List<LocationItem> rooms = configurationEndpoint.getCountries();

		// Asserts
		assertEquals(3, rooms.size());
	}


	
}
