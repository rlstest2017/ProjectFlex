package com.orange.flexoffice.adminui.ws.endPoint.entity.impl;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;

import com.orange.flexoffice.adminui.ws.endPoint.data.MeetingRoomStatEndpoint;
import com.orange.flexoffice.adminui.ws.endPoint.data.impl.MeetingRoomStatEndpointImpl;
import com.orange.flexoffice.adminui.ws.model.MultiStatSet;
import com.orange.flexoffice.adminui.ws.model.SimpleStat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MeetingRoomStatEndpointImplTest {

	static {
	    try {
	      Log4jConfigurer.initLogging( "classpath:log4j-flexoffice-adminui-test.xml" );
	    }
	    catch( FileNotFoundException ex ) {
	      System.err.println( "Cannot Initialize log4j" );
	    }
	  }
	
	private static ClassPathXmlApplicationContext context;

	private static MeetingRoomStatEndpoint meetingRoomStatEndpoint;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void initSpringContextAndDatabase() throws Exception {
		context = new ClassPathXmlApplicationContext("applicationContext-flexoffice-adminui-test.xml");
		meetingRoomStatEndpoint = (MeetingRoomStatEndpointImpl)context.getBean("meetingRoomStatEndpoint");
	}


	@Test
	public void TestA_initTables() {
		// SetUp
		boolean state = meetingRoomStatEndpoint.executeInitTestFile();
		 
		// Asserts
		assertEquals(true, state);
	}

	@Test
	public void TestB_initMeetingRoomDailyOccupancyTables() {
		// SetUp
		boolean state = meetingRoomStatEndpoint.initMeetingRoomDailyOccupancyTable();
		 
		// Asserts
		assertEquals(true, state);
	}
	
	@Test
	public void TestC_statPopular() {
		// Test
		List<SimpleStat> simpleStatList = meetingRoomStatEndpoint.getPopularStats();
	
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
		MultiStatSet statSet =	meetingRoomStatEndpoint.getOccupancyStats(from, to, viewtype);
		
		// Asserts
		assertEquals(4, statSet.getData().size());
	}
	
	@Test
	public void TestE_initMeetingRoomMonthlyOccupancyTables() {
		// SetUp
		boolean state = meetingRoomStatEndpoint.initMeetingRoomMonthlyOccupancyTable();
		 
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
		MultiStatSet statSet =	meetingRoomStatEndpoint.getOccupancyStats(from, to, viewtype);
		
		// Asserts
		assertEquals(4, statSet.getData().size());
	}
	
	@Test
	public void TestG_statOccupancyWeekly() {
		// SetUp
		String from="0";
		String to="2015-12-16T15:00:22.806Z";
		String viewtype = "WEEK";
		
		// Test
		MultiStatSet statSet =	meetingRoomStatEndpoint.getOccupancyStats(from, to, viewtype);
		
		// Asserts
		assertEquals(5, statSet.getData().size());
	}
	
	@Test
	public void TestH_statExportFile() {
		
		// Test
		boolean statSet =	meetingRoomStatEndpoint.getTestFile();
		
		// Asserts
		assertEquals(true, statSet);
	}
}
