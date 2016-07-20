package com.orange.flexoffice.adminui.ws.endPoint.entity.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
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

import com.orange.flexoffice.adminui.ws.endPoint.entity.AgentEndpoint;
import com.orange.flexoffice.adminui.ws.endPoint.support.ObjectFactory;
import com.orange.flexoffice.adminui.ws.model.Agent;
import com.orange.flexoffice.adminui.ws.model.AgentInput;
import com.orange.flexoffice.adminui.ws.model.AgentInput2;
import com.orange.flexoffice.adminui.ws.model.AgentOutput;
import com.orange.flexoffice.adminui.ws.model.AgentSummary;
import com.orange.flexoffice.adminui.ws.model.ECommandModel;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.dao.common.model.object.AgentDto;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AgentEndpointImplTest {

	static {
	    try {
	      Log4jConfigurer.initLogging( "classpath:log4j-flexoffice-adminui-test.xml" );
	    }
	    catch( FileNotFoundException ex ) {
	      System.err.println( "Cannot Initialize log4j" );
	    }
	  }
	
	private static ClassPathXmlApplicationContext context;

	private static AgentEndpoint agentEndpoint;

	@Context
	private UriInfo uriInfo;

	private final ObjectFactory factory = new ObjectFactory();
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void initSpringContextAndDatabase() throws Exception {
		context = new ClassPathXmlApplicationContext("classpath:applicationContext-flexoffice-adminui-test.xml");
		agentEndpoint = (AgentEndpointImpl)context.getBean("agentEndpoint");
	}


	@Test
	public void TestA_initTables() {
		// SetUp
		boolean state = agentEndpoint.executeInitTestFile();
		 
		// Asserts
		assertEquals(true, state);
	}

	
	@Test
	public void TestB_getAgents() {
		// Test
		List<AgentSummary> agents =	agentEndpoint.getAgents();
		
		// Asserts
		assertEquals(5, agents.size());
	}

	@Test
	public void TestC_getAgentByAgentByMacAddress() {
		// SetUp
		String macAddress = "FF:EE:ZZ:AA:GG:PP";
		
		// Test
		Agent agents = agentEndpoint.getAgent(macAddress);
				
		// Asserts
		assertNotNull(agents.getMeetingroom());
				
	}
	
	@Test
	public void TestC1_getAgentDataNotExistsException() {
		// Setup
		boolean expectedResult = false;
		String macAddress = "ZZ:NN:MM:KK:HH:RR";
	
		// Test
		try {
			agentEndpoint.getAgent(macAddress);

		} catch (WebApplicationException e) {
			expectedResult = true;
		}

		// Assert
		assertEquals(true, expectedResult);	
	}
	
	@Test
	public void TestD_addAgent() {
		// Setup
		final AgentInput agent = factory.createHmiAgent("AA:DD:SS:PP:SS:MM", "agent 10", "agent 10 test", ECommandModel.NONE);
		
		// Test
		final AgentOutput response = agentEndpoint.addAgent(agent);
		
		// Assert
		assertNotNull(response.getMacAddress());
		
	}
	
	@Test
	public void TestE_addAgentDataAlreadyExistsException() {
		// Setup
		boolean expectedResult = false;
		
		// Test
		try {
			// Setup
			final AgentDto agentOut = agentEndpoint.findByMacAddress("FF:EE:ZZ:AA:GG:PP");
			if (agentOut != null) {
				
				final AgentInput agent = factory.createHmiAgent(agentOut.getMacAddress(), agentOut.getName(), agentOut.getDescription(), ECommandModel.NONE);

				agentEndpoint.addAgent(agent);
			}
			
		} catch(DataNotExistsException e ) {
		
		} catch (WebApplicationException e) {
			expectedResult = true;
		}

		// Assert
		assertEquals(true, expectedResult);	
	}
	
	@Test
	public void TestF_updateAgent() throws WebApplicationException {
		// Setup
		boolean expectedResult = false;
		final AgentInput2 agentIn = factory.createHmi2Agent("agent 11", "agent 11 test", ECommandModel.OFFLINE);

		try {
			final AgentDto agentOut = agentEndpoint.findByMacAddress("AA:BS:CC:AA:GG:PP");

			// Test
			Response response = agentEndpoint.updateAgent(agentOut.getMacAddress(), agentIn);

			// Assert
			assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());
			expectedResult = true;

		} catch(DataNotExistsException e ) {
		}

		// Assert
		assertEquals(true, expectedResult);	
	}
	
	@Test
	public void TestG_updateAgentDataNotExistsException() {
		// Setup
		boolean expectedResult = false;

		// Test
		try {
			// Setup
			final AgentInput2 agentIn = factory.createHmi2Agent("agent 12", "agent 12 test", ECommandModel.ONLINE);
			
			agentEndpoint.updateAgent("ZZ:BS:CC:AA:GG:PP", agentIn);

		} catch (WebApplicationException e) {
			expectedResult = true;
		}

		// Assert
		assertEquals(true, expectedResult);	
	}
	
	@Test
	public void TestH_removeAgentDataNotExistsException() {
		// Setup
		boolean expectedResult = false;
		String macAddress = "AA:TT:ZZ:AA:GG:PP";
		
		// Test
		try {
			// Test
			agentEndpoint.removeAgent(macAddress);
			
		} catch (WebApplicationException e) {
			expectedResult = true;	
		}

		// Assert
		assertEquals(true, expectedResult);	
	}

	@Test
	public void TestI_removeAgentAssociatedToMeetingRoom()  {
		// Setup
		boolean expectedResult = false;
		String macAddress = "FF:TS:ZZ:AA:GG:PP";
		
		try {
			// Test
			agentEndpoint.removeAgent(macAddress);
			
		} catch(WebApplicationException e ) {
			expectedResult = true;
		}
		
		// Assert
		assertEquals(true, expectedResult);	
	}
	
	@Test
	public void TestI2_removeAgentNotAssociatedToMeetingRoom()  {
		// Setup
		boolean expectedResult = true;
		String macAddress = "AA:BS:CC:AA:GG:PP";
		
		try {
			// Test
			agentEndpoint.removeAgent(macAddress);
			
		} catch(WebApplicationException e ) {
			expectedResult = true;
		}
		
		// Assert
		assertEquals(true, expectedResult);	
	}
}
