package com.orange.flexoffice.adminui.ws.endPoint.entity.impl;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;

import com.orange.flexoffice.adminui.ws.endPoint.data.StatEndpoint;
import com.orange.flexoffice.adminui.ws.endPoint.data.impl.StatEndpointImpl;
import com.orange.flexoffice.adminui.ws.model.MultiStatSet;
import com.orange.flexoffice.adminui.ws.model.SimpleStat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StatEndpointImplTest {

	static {
	    try {
	      Log4jConfigurer.initLogging( "classpath:log4j-flexoffice-adminui-test.xml" );
	    }
	    catch( FileNotFoundException ex ) {
	      System.err.println( "Cannot Initialize log4j" );
	    }
	  }
	
	private static ClassPathXmlApplicationContext context;

	private static StatEndpoint statEndpoint;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void initSpringContextAndDatabase() throws Exception {
		context = new ClassPathXmlApplicationContext("applicationContext-flexoffice-adminui-test.xml");
		statEndpoint = (StatEndpointImpl)context.getBean("statEndpoint");
	}


	@Test
	public void TestA_initTables() {
		// SetUp
		boolean state = statEndpoint.executeInitTestFile();
		 
		// Asserts
		assertEquals(true, state);
	}

	@Test
	public void TestB_initRoomDailyOccupancyTables() {
		// SetUp
		boolean state = statEndpoint.initRoomDailyOccupancyTable();
		 
		// Asserts
		assertEquals(true, state);
	}
	
	@Test
	public void TestC_statPopular() {
		// Test
		List<SimpleStat> simpleStatList = statEndpoint.getPopularStats();
	
		// Asserts
		assertEquals(3, simpleStatList.size());
	}
	
	@Test
	public void TestD_statOccupancyDaily() {
		// SetUp
		String from="0";
		String to="2015-12-16T15:00:22.806Z";
		String viewtype = "DAY";
		
		// Test
		MultiStatSet statSet =	statEndpoint.getOccupancyStats(from, to, viewtype);
		System.out.println("Test app");
		
		// Asserts
		assertEquals(4, statSet.getData().size());
	}
	
	@Test
	public void TestE_initRoomMonthlyOccupancyTables() {
		// SetUp
		boolean state = statEndpoint.initRoomMonthlyOccupancyTable();
		 
		// Asserts
		assertEquals(true, state);
	}
	
	@Test
	public void TestF_statOccupancyMonthly() {
		// SetUp
		String from="0";
		String to="2015-12-16T15:00:22.806Z";
		String viewtype = "MONTH";
		
		// Test
		MultiStatSet statSet =	statEndpoint.getOccupancyStats(from, to, viewtype);
		System.out.println("Test app");
		
		// Asserts
		assertEquals(4, statSet.getData().size());
	}
}
