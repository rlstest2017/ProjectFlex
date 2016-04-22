package org.meetingroom.business;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;

import com.orange.meetingroom.business.connector.PhpConnectorManager;
import com.orange.meetingroom.business.connector.impl.PhpConnectorManagerImpl;
import com.orange.meetingroom.connector.php.model.request.GetAgentBookingsParameters;
import com.orange.meetingroom.connector.php.model.response.MeetingRoomBookings;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PhpConnectorImplTest {

	static {
	    try {
	      Log4jConfigurer.initLogging( "classpath:log4j-meetingroom-phpconnector-test.xml" );
	    }
	    catch( FileNotFoundException ex ) {
	      System.err.println( "Cannot Initialize log4j" );
	    }
	  }
	
	private static ClassPathXmlApplicationContext context;
	private static PhpConnectorManager phpBusinessConnector;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void initSpringContextAndDatabase() throws Exception {
		context = new ClassPathXmlApplicationContext("applicationContext-meetingroom-business-test.xml");
		phpBusinessConnector = (PhpConnectorManagerImpl)context.getBean("phpBusinessConnector");
	}


	@Test
	public void TestA_phpGetAgentBookings() {
		// SetUp
		GetAgentBookingsParameters params = new GetAgentBookingsParameters();
		params.setFormat("json");
		params.setRoomID("brehat.rennes@microsoft.cad.aql.fr");
		params.setForceUpdateCache("false");
		try {
			MeetingRoomBookings meetingRoomBookings = phpBusinessConnector.getBookingsFromAgent(params);
			String externalId = meetingRoomBookings.getMeetingRoomDetails().getMeetingRoomExternalId();
			// Asserts
			assertEquals("brehat.rennes@microsoft.cad.aql.fr", externalId);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	 
		// Asserts
		assertEquals(true, true);
	}

		
}
