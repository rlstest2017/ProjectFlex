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

import com.orange.flexoffice.adminui.ws.endPoint.entity.DashboardEndpoint;
import com.orange.flexoffice.adminui.ws.endPoint.support.ObjectFactory;
import com.orange.flexoffice.adminui.ws.model.Dashboard;
import com.orange.flexoffice.adminui.ws.model.DashboardInput;
import com.orange.flexoffice.adminui.ws.model.DashboardInput2;
import com.orange.flexoffice.adminui.ws.model.DashboardSummary;
import com.orange.flexoffice.adminui.ws.model.ECommandModel;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.dao.common.model.object.DashboardDto;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DashboardEndpointImplTest {

	static {
	    try {
	      Log4jConfigurer.initLogging( "classpath:log4j-flexoffice-adminui-test.xml" );
	    }
	    catch( FileNotFoundException ex ) {
	      System.err.println( "Cannot Initialize log4j" );
	    }
	  }
	
	private static ClassPathXmlApplicationContext context;

	private static DashboardEndpoint dashboardEndpoint;

	@Context
	private UriInfo uriInfo;

	private final ObjectFactory factory = new ObjectFactory();
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void initSpringContextAndDatabase() throws Exception {
		context = new ClassPathXmlApplicationContext("applicationContext-flexoffice-adminui-test.xml");
		dashboardEndpoint = (DashboardEndpointImpl)context.getBean("dashboardEndpoint");
	}


	@Test
	public void TestA_initTables() {
		// SetUp
		boolean state = dashboardEndpoint.executeInitTestFile();
		 
		// Asserts
		assertEquals(true, state);
	}

	
	@Test
	public void TestB_getDashboards() {
		// Test
		List<DashboardSummary> dashboards =	dashboardEndpoint.getDashboards();
		
		// Asserts
		assertEquals(5, dashboards.size());
	}

	@Test
	public void TestC_getDashboardByDashboardByMacAddress() {
		// SetUp
		String macAddress = "FF:EE:ZZ:AA:GG:PP";
		
		// Test
		Dashboard dashboard = dashboardEndpoint.getDashboard(macAddress);
				
		// Asserts
		assertNotNull(dashboard);
				
	}
	
	@Test
	public void TestC1_getDashboardDataNotExistsException() {
		// Setup
		boolean expectedResult = false;
		String macAddress = "ZZ:NN:MM:ZK:HH:RR";
	
		// Test
		try {
			dashboardEndpoint.getDashboard(macAddress);

		} catch (WebApplicationException e) {
			expectedResult = true;
		}

		// Assert
		assertEquals(true, expectedResult);	
	}
	
	@Test
	public void TestD_addDashboard() {
		// Setup
		final DashboardInput dashboard = factory.createHmiDashboard("AA:DD:SS:PP:SS:MM", "agent 10", "agent 10 test", ECommandModel.ONLINE, "1", "1", BigInteger.valueOf(0));
		
		// Test
		final Dashboard response = dashboardEndpoint.addDashboard(dashboard);
		
		// Assert
		assertNotNull(response.getMacAddress());
		
	}
	
	@Test
	public void TestE_addDashboardDataAlreadyExistsException() {
		// Setup
		boolean expectedResult = false;
		
		// Test
		try {
			// Setup
			final DashboardDto dashboardOut = dashboardEndpoint.findByMacAddress("FF:TT:ZZ:AA:GG:PP");
			if (dashboardOut != null) {
				
				final DashboardInput dashboard = factory.createHmiDashboard(dashboardOut.getMacAddress(), dashboardOut.getName(), dashboardOut.getDescription(), ECommandModel.ONLINE,
						dashboardOut.getCityId().toString(), dashboardOut.getBuildingId().toString(), BigInteger.valueOf(dashboardOut.getFloor()));

				dashboardEndpoint.addDashboard(dashboard);
			}
			
		} catch(DataNotExistsException e ) {
		
		} catch (WebApplicationException e) {
			expectedResult = true;
		}

		// Assert
		assertEquals(true, expectedResult);	
	}
	
	@Test
	public void TestF_updateDashboard() throws WebApplicationException {
		// Setup
		boolean expectedResult = false;
		final DashboardInput2 dashboardIn = factory.createHmi2Dashboard("dashboard 11", "dashboard 11 test", ECommandModel.OFFLINE, "1", "1", BigInteger.valueOf(0));

		try {
			final DashboardDto dashboardOut = dashboardEndpoint.findByMacAddress("AA:DD:SS:PP:SS:MM");

			// Test
			Response response = dashboardEndpoint.updateDashboard(dashboardOut.getMacAddress(), dashboardIn);

			// Assert
			assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());
			expectedResult = true;

		} catch(DataNotExistsException e ) {
		}

		// Assert
		assertEquals(true, expectedResult);	
	}
	
	@Test
	public void TestG_updateDashboardDataNotExistsException() {
		// Setup
		boolean expectedResult = false;

		// Test
		try {
			// Setup
			final DashboardInput2 dashboardIn = factory.createHmi2Dashboard("dashboard 12", "dashboard 12 test", ECommandModel.ONLINE, "2", "1", BigInteger.valueOf(0));
			
			dashboardEndpoint.updateDashboard("ZZ:BS:CC:AA:GG:PP", dashboardIn);

		} catch (WebApplicationException e) {
			expectedResult = true;
		}

		// Assert
		assertEquals(true, expectedResult);	
	}
	
	@Test
	public void TestH_removeDashboard() throws WebApplicationException {
		// Setup
		boolean expectedResult = false;

		try {
			final DashboardDto dashboardOut = dashboardEndpoint.findByMacAddress("AA:DD:SS:PP:SS:MM");

			// Test
			Response response = dashboardEndpoint.removeDashboard(dashboardOut.getMacAddress());

			// Assert
			assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
			expectedResult = true;

		} catch(DataNotExistsException e ) {
		}
		
		// Assert
		assertEquals(true, expectedResult);	
	}
	
	@Test
	public void TestI_removeDashboardDataNotExistsException() {
		// Setup
		boolean expectedResult = false;
		String macAddress = "AA:TT:ZZ:AA:GG:PP";
		
		// Test
		try {
			// Test
			dashboardEndpoint.removeDashboard(macAddress);
			
		} catch (WebApplicationException e) {
			expectedResult = true;	
		}

		// Assert
		assertEquals(true, expectedResult);	
	}
}
