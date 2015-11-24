package com.orange.flexoffice.userui.ws.endPoint.entity.impl;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

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
		// Test
		UserSummary user = userEndpoint.getUserCurrent(null, null);

		// Asserts
		assertEquals("user Email 1", user.getEmail());
	}

}
