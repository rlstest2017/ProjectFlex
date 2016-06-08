package com.orange.meetingroom.gui.ws.endPoint.entity.impl;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;

import com.orange.meetingroom.gui.ws.endPoint.entity.AgentEndpoint;
import com.orange.meetingroom.gui.ws.endPoint.entity.impl.AgentEndpointImpl;
import com.orange.meetingroom.gui.ws.model.AgentInput;
import com.orange.meetingroom.gui.ws.model.AgentOutput;
import com.orange.meetingroom.gui.ws.model.EAgentStatus;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AgentEndpointImplTest {

	static {
	    try {
	      Log4jConfigurer.initLogging( "classpath:log4j-meetingroom-gui-test.xml" );
	    }
	    catch( FileNotFoundException ex ) {
	      System.err.println( "Cannot Initialize log4j" );
	    }
	  }

	private static final Logger LOGGER = Logger.getLogger(AgentEndpointImplTest.class);

	private static ClassPathXmlApplicationContext context;
	private static AgentEndpoint agentEndpoint;
	
	@BeforeClass
	public static void initSpringContextAndDatabase() throws Exception {
		context = new ClassPathXmlApplicationContext("classpath*:applicationContext-meetingroom-gui-test.xml");
		agentEndpoint = (AgentEndpointImpl)context.getBean("agentEndpoint");
	}
	
	@Test
	public void TestA() {
		// SetUp
		boolean expectedResult = false;
		try {
			AgentInput params = new AgentInput();
			params.setAgentStatus(EAgentStatus.ECONOMIC);
			AgentOutput output = agentEndpoint.updateAgent("RL:RL:RL:RL:RL:RL", params);
			
			// Asserts
			assertEquals("[TEST]gardian.paris@microsoft.cad.aql.fr", output.getMeetingRoomExternalId().toString());
			
			
		} catch (Exception e) {
			expectedResult = true;
			LOGGER.error(e.getMessage());
		}	
		// Asserts
		assertEquals(false, expectedResult);
	}
	
	

}
