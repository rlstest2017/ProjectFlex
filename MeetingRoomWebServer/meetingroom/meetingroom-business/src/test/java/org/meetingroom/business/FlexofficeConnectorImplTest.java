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
import com.orange.meetingroom.connector.flexoffice.enums.EnumAgentStatus;
import com.orange.meetingroom.connector.flexoffice.enums.EnumDashboardStatus;
import com.orange.meetingroom.connector.flexoffice.enums.EnumMeetingRoomStatus;
import com.orange.meetingroom.connector.flexoffice.model.request.AgentConnectorInput;
import com.orange.meetingroom.connector.flexoffice.model.request.DashboardConnectorInput;
import com.orange.meetingroom.connector.flexoffice.model.request.MeetingRoomData;
import com.orange.meetingroom.connector.flexoffice.model.response.AgentConnectorOutput;
import com.orange.meetingroom.connector.flexoffice.model.response.DashboardConnectorOutput;
import com.orange.meetingroom.connector.flexoffice.model.response.SystemConnectorReturn;

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


	//@Test
	public void TestA_flexofficeGetSystem() {
		// SetUp
		boolean expectedResult = false;
		try {
			SystemConnectorReturn systemReturn = flexofficeBusinessConnector.getSystem();
			
			// Asserts
			assertEquals("37", systemReturn.getAgentTimeout().toString());
			
			
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());
		}	
		// Asserts
		assertEquals(false, expectedResult);
	}

	//@Test
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

	//@Test
	public void TestC_flexofficeGetXmlFilesNameConfig() {
		// SetUp
		boolean expectedResult = false;
		DashboardConnectorInput params = new DashboardConnectorInput();
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
	
	//@Test
	public void TestD_flexofficePutDashboardStatus() {
		// SetUp
		boolean expectedResult = false;
		DashboardConnectorInput params = new DashboardConnectorInput();
		params.setDashboardMacAddress("FF:RR:EE:SS:DD:AA");
		params.setDashboardStatus(EnumDashboardStatus.ONLINE);
		
		try {
			DashboardConnectorOutput output = flexofficeBusinessConnector.updateDashboardStatus(params);
			
			// Asserts
			assertEquals("ONLINE", output.getCommand().toString());
			
			// test 
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());
		}	
		// Asserts
		assertEquals(false, expectedResult);
	}
	
	//@Test
	public void TestE_flexofficePutAgentStatus() {
		// SetUp
		boolean expectedResult = false;
		AgentConnectorInput params = new AgentConnectorInput();
		params.setAgentMacAddress("FF:RR:EE:SS:DD:AA");
		params.setAgentStatus(EnumAgentStatus.ONLINE);
		
		try {
			AgentConnectorOutput output = flexofficeBusinessConnector.updateAgentStatus(params);
			
			// Asserts
			assertEquals("OFFLINE", output.getCommand().toString());
			assertEquals("brehat.rennes@microsoft.cad.aql.fr", output.getMeetingRoomExternalId().toString());
			
			// test 
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());
		}	
		// Asserts
		assertEquals(false, expectedResult);
	}
	
	//@Test
	public void TestF_flexofficePutMeetingRoomData() {
		// SetUp
		boolean expectedResult = false;
		MeetingRoomData params = new MeetingRoomData();
		params.setMeetingRoomExternalId("brehat.rennes@microsoft.cad.aql.fr");
		params.setStartDate(123546879);
		params.setEndDate(214536251);
		params.setOrganizerLabel("test organizer");
		params.setMeetingRoomStatus(EnumMeetingRoomStatus.FREE);
		
		try {
			flexofficeBusinessConnector.updateMeetingRoomData(params);
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());
		}	
		// Asserts
		assertEquals(false, expectedResult);
	}

}
