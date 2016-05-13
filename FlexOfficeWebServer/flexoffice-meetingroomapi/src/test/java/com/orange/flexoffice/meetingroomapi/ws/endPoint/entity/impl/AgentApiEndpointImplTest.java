package com.orange.flexoffice.meetingroomapi.ws.endPoint.entity.impl;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;

import com.orange.flexoffice.meetingroomapi.ws.endPoint.entity.AgentApiEndpoint;
import com.orange.flexoffice.meetingroomapi.ws.model.AgentInput;
import com.orange.flexoffice.meetingroomapi.ws.model.AgentOutput;
import com.orange.flexoffice.meetingroomapi.ws.model.EAgentStatus;
import com.orange.flexoffice.meetingroomapi.ws.model.ECommandModel;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AgentApiEndpointImplTest {

	static {
	    try {
	      Log4jConfigurer.initLogging( "classpath:log4j-flexoffice-meetingroomapi-test.xml" );
	    }
	    catch( FileNotFoundException ex ) {
	      System.err.println( "Cannot Initialize log4j" );
	    }
	}
	
	private static ClassPathXmlApplicationContext context;

	private static AgentApiEndpoint agentEndpoint;

	@Context
	private UriInfo uriInfo;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void initSpringContextAndDatabase() throws Exception {
		context = new ClassPathXmlApplicationContext("applicationContext-flexoffice-meetingroomapi-test.xml");
		agentEndpoint = (AgentApiEndpointImpl)context.getBean("agentEndpoint");
	}


	@Test
	public void TestA_initTables() {
		// SetUp
		boolean state = agentEndpoint.executeInitTestFile();
		 
		// Asserts
		assertEquals(true, state);
	}

	
	@Test
	public void TestB_UpdateStatus() {
		String macAddress = "FF:TS:ZZ:AA:GG:PP";
		AgentInput agent = new AgentInput();
		agent.setAgentStatus(EAgentStatus.ECONOMIC);
		
		// Test
		AgentOutput agentOutput =	agentEndpoint.updateStatus(macAddress, agent);
		
		// Asserts
		assertEquals("4@id.fr", agentOutput.getMeetingRoomExternalId());
		assertEquals(ECommandModel.ECONOMIC, agentOutput.getCommand());
	}

	@Test
	public void TestC_UpdateStatusWithAgentNotAssociatedToMeetingRoom() {
		String macAddress = "AA:BS:CC:AA:GG:PP";
		AgentInput agent = new AgentInput();
		agent.setAgentStatus(EAgentStatus.ECONOMIC);
		
		// Test
		AgentOutput agentOutput =	agentEndpoint.updateStatus(macAddress, agent);
		
		// Asserts
		assertEquals("0", agentOutput.getMeetingRoomExternalId());
		assertEquals(ECommandModel.ECONOMIC, agentOutput.getCommand());
	}
}
