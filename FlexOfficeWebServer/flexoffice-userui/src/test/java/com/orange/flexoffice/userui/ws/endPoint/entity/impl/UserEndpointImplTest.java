package com.orange.flexoffice.userui.ws.endPoint.entity.impl;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import javax.ws.rs.WebApplicationException;
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
import com.orange.flexoffice.userui.ws.model.User;
import com.orange.flexoffice.userui.ws.model.UserInput;

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
	public void TestB_getUserCurrentWithoutRoomId() {
		// SetUp
		String token = "Zmlyc3QubGFzdDVAdGVzdC5jb206dGVzdDoxNDQ4NTI5MDc2ODQ0";
		
		// Test
		User user = userEndpoint.getUserCurrent(token);

		// Asserts
		assertEquals("first.last5@test.com:test", user.getEmail());
		
	}

	@Test
	public void TestC_getUserCurrentWithRoomId() {
		// SetUp
		String token = "Zmlyc3QubGFzdDFAdGVzdC5jb206cGFzczoxNDQ4NjEzNjU2MDk4";
		
		// Test
		User user = userEndpoint.getUserCurrent(token);

		// Asserts
		assertEquals("first.last1@test.com:pass", user.getEmail());
		
	}

	@Test
	public void TestD_getUserCurrentBadToken() {
		// SetUp
		String badToken = "RAsalyc3QubGFzdDFAdGVzdC5jb206cGFaerdtNDQ4NjEzNjU2Medz";
		boolean expectedResult = false;
		
		// Test
		try {
			userEndpoint.getUserCurrent(badToken);
		} catch (WebApplicationException e) {
			expectedResult = true;
		}

		// Asserts
		assertEquals(true, expectedResult);
		
	}
	
	@Test
	public void TestE_logout() {
		// Setup
		String token = "Zmlyc3QubGFzdDVAdGVzdC5jb206dGVzdDoxNDQ4NTI5MDc2ODQ0";
		
		// Test
		Response response = userEndpoint.logout(token);

		// Asserts
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
	}

	@Test
	public void TestF_login() {
		// Setup
		//String authorization = "Basic Zmlyc3QubGFzdDVAdGVzdC5jb206dGVzdA==";
		UserInput user = new UserInput();
		user.setEmail("first.last5@test.com:test");
		
		// Test
		Response response = userEndpoint.login(null, user);

		// Asserts
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
	}

	@Test
	public void TestG_loginNotExistWithoutUserInput() {
		// Setup
		// first.last10@test.com:pass
		//String authorization = "Basic Zmlyc3QubGFzdDEwQHRlc3QuY29tOnBhc3M=";
		UserInput user = new UserInput();
		user.setEmail("first.last10@test.com:pass");
		
		// Test
		Response response = userEndpoint.login(null, user);

		// Asserts
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
	}

	@Test
	public void TestH_loginNotExistWithUserInput() {
		// Setup
		// first.last15@test.com:pass
		// String authorization = "Basic Zmlyc3QubGFzdDE1QHRlc3QuY29tOnBhc3M=";
		UserInput user = new UserInput();
		user.setEmail("first.last15@test.com:pass");
		user.setFirstName("firstTest");
		user.setLastName("LastTest");
		
		// Test
		Response response = userEndpoint.login(null, user);

		// Asserts
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void TestI_loginNotExistWithLastName() {
		// Setup
		// first.last20@test.com:pass
		//String authorization = "Basic Zmlyc3QubGFzdDIwQHRlc3QuY29tOnBhc3M=";
		UserInput user = new UserInput();
		user.setEmail("first.last20@test.com:pass");
		user.setLastName("LastTest");
		
		// Test
		Response response = userEndpoint.login(null, user);

		// Asserts
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
	}

	@Test
	public void TestJ_loginBadFirstNameLength() {
		// Setup
		boolean expectedResult = false;
		//String authorization = "Basic Zmlyc3QubGFzdDVAdGVzdC5jb206dGVzdA==";
		UserInput user = new UserInput();
		user.setEmail("first.last5@test.com:test");
		// 101 caractères
		user.setFirstName("A1531236541235874123569874154236mfpesndealoszmape124531236541235874123569874154236mfpesndealoszmapeza");		
		// Test
		try {
			// Test
			userEndpoint.login(null, user);

		} catch (WebApplicationException e) {
			expectedResult = true;
		}

		// Asserts
		assertEquals(true, expectedResult);	
	}

	@Test
	public void TestK_loginBadLastNameLength() {
		// Setup
		boolean expectedResult = false;
		//String authorization = "Basic Zmlyc3QubGFzdDVAdGVzdC5jb206dGVzdA==";
		UserInput user = new UserInput();
		user.setEmail("first.last5@test.com:test");
		// 101 caractères
		user.setLastName("A1531236541235874123569874154236mfpesndealoszmape124531236541235874123569874154236mfpesndealoszmapeza");
		
		// Test
		try {
			// Test
			userEndpoint.login(null, user);

		} catch (WebApplicationException e) {
			expectedResult = true;
		}

		// Asserts
		assertEquals(true, expectedResult);	
	}

	@Test
	public void TestL_loginBadEmailLength() {
		// Setup
		boolean expectedResult = false;
		// user 101 caractères : test@gmail.comaldeosmzpaodenslezaqlpmasleomapeznsltesttiogmail.comaldeosmzpaodenslezaqlpmasleomapeznsl 
		//String authorization = "Basic dGVzdEBnbWFpbC5jb21hbGRlb3NtenBhb2RlbnNsZXphcWxwbWFzbGVvbWFwZXpuc2x0ZXN0dGlvZ21haWwuY29tYWxkZW9zbXpwYW9kZW5zbGV6YXFscG1hc2xlb21hcGV6bnNs";
		UserInput user = new UserInput();
		user.setEmail("test@gmail.comaldeosmzpaodenslezaqlpmasleomapeznsltesttiogmail.comaldeosmzpaodenslezaqlpmasleomapeznsl");
		// Test
		try {
			// Test
			userEndpoint.login(null, user);
		} catch (WebApplicationException e) {
			expectedResult = true;
		}

		// Asserts
		assertEquals(true, expectedResult);	
	}

	
	
}
