package com.orange.flexoffice.userui.ws.endPoint.entity.impl;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;

import com.orange.flexoffice.userui.ws.endPoint.entity.UserEndpoint;
import com.orange.flexoffice.userui.ws.model.UserSummary;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserEndpointImplTest {

	static {
	    try {
	      Log4jConfigurer.initLogging( "classpath:log4j-flexoffice-userui-test.xml" );
	    }
	    catch( FileNotFoundException ex ) {
	      System.err.println( "Cannot Initialize log4j" );
	    }
	  }

	private static ClassPathXmlApplicationContext context;

	private static UserEndpoint userEndpoint;



	@Context
	private UriInfo uriInfo;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void initSpringContextAndDatabase() throws Exception {
		context = new ClassPathXmlApplicationContext("applicationContext-flexoffice-userui-test.xml");
		userEndpoint = (UserEndpointImpl)context.getBean("userEndpoint");
	}


	@Test
	public void TestA_initTables() {
		// SetUp
		boolean state = userEndpoint.executeInitTestFile();

		// Asserts
		assertEquals(true, state);
	}
	
	@Test
	public void TestB_getUserCurrent() {
		// SetUp
		String token = "Zmlyc3QubGFzdDVAdGVzdC5jb206dGVzdDoxNDQ4NTI5MDc2ODQ0";
		
		// Test
		UserSummary user = userEndpoint.getUserCurrent(token);

		// Asserts
		assertEquals("first.last5@test.com:test", user.getEmail());
		
	}

	@Test
	public void TestC_logout() {
		// Setup
		String token = "Zmlyc3QubGFzdDVAdGVzdC5jb206dGVzdDoxNDQ4NTI5MDc2ODQ0";
		
		// Test
		Response response = userEndpoint.logout(token);

		// Asserts
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
	}

	@Test
	public void TestD_login() {
		// Setup
		String authorization = "Basic Zmlyc3QubGFzdDVAdGVzdC5jb206dGVzdA==";
		
		// Test
		Response response = userEndpoint.login(authorization, null, null);

		// Asserts
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
	}
	
	

}
