package org.meetingroom.business;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;

import com.orange.meetingroom.business.connector.FlexOfficeConnectorManager;
import com.orange.meetingroom.business.connector.impl.FlexOfficeConnectorManagerImpl;
import com.orange.meetingroom.connector.flexoffice.model.request.DashboardInput;
import com.orange.meetingroom.connector.flexoffice.model.response.SystemReturn;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FlexofficeConnectorImplTest {

	static {
	    try {
	      Log4jConfigurer.initLogging( "classpath:log4j-meetingroom-business-test.xml" );
	    }
	    catch( FileNotFoundException ex ) {
	      System.err.println( "Cannot Initialize log4j" );
	    }
	  }

	private static final Logger LOGGER = Logger.getLogger(FlexofficeConnectorImplTest.class);
	private static ClassPathXmlApplicationContext context;
	private static FlexOfficeConnectorManager flexofficeBusinessConnector;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void initSpringContextAndDatabase() throws Exception {
		context = new ClassPathXmlApplicationContext("classpath*:applicationContext-meetingroom-business-test.xml");
		flexofficeBusinessConnector = (FlexOfficeConnectorManagerImpl)context.getBean("flexofficeBusinessConnector");
	}


	@Test
	public void TestA_flexofficeGetSystem() {
		// SetUp
		boolean expectedResult = false;
		try {
			SystemReturn systemReturn = flexofficeBusinessConnector.getSystem();
			
			// Asserts
			assertEquals("37", systemReturn.getAgentTimeout().toString());
			
			
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());
		}	
		// Asserts
		assertEquals(false, expectedResult);
	}

	@Test
	public void TestB_flexofficeGetMeetingRoomsTimeout() {
		// SetUp
		boolean expectedResult = false;
		try {
			List<String> listReturn = flexofficeBusinessConnector.getMeetingRoomsInTimeOut();
			
			// Asserts
			assertEquals(3, listReturn.size());
			
			
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());
		}	
		// Asserts
		assertEquals(false, expectedResult);
	}

	@Test
	public void TestC_flexofficeGetXmlFilesNameConfig() {
		// SetUp
		boolean expectedResult = false;
		DashboardInput params = new DashboardInput();
		params.setDashboardMacAddress("FF:RR:EE:SS:DD:AA");
		
		try {
			List<String> listReturn = flexofficeBusinessConnector.getDashboardXMLConfigFilesName(params);
			
			// Asserts
			assertEquals(4, listReturn.size());
			
			// test 
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());
		}	
		// Asserts
		assertEquals(false, expectedResult);
	}

}
