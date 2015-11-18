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

import com.orange.flexoffice.adminui.ws.endPoint.entity.GatewayEndpoint;
import com.orange.flexoffice.adminui.ws.endPoint.entity.UserEndpoint;
import com.orange.flexoffice.adminui.ws.endPoint.entity.impl.UserEndpointImpl;
import com.orange.flexoffice.adminui.ws.endPoint.support.ObjectFactory;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.dao.common.model.data.UserDao;
import com.orange.flexoffice.adminui.ws.model.User;
import com.orange.flexoffice.adminui.ws.model.UserInput;
import com.orange.flexoffice.adminui.ws.model.UserSummary;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserEndPointImplTest {

	static {
	    try {
	      Log4jConfigurer.initLogging( "classpath:log4j-flexoffice-adminui-test.xml" );
	    }
	    catch( FileNotFoundException ex ) {
	      System.err.println( "Cannot Initialize log4j" );
	    }
	  }

	private static ClassPathXmlApplicationContext context;

	private static UserEndpoint userEndpoint;

	private static GatewayEndpoint gatewayEndpoint;

	@Context
	private UriInfo uriInfo;

	private final ObjectFactory factory = new ObjectFactory();

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void initSpringContextAndDatabase() throws Exception {
		context = new ClassPathXmlApplicationContext("applicationContext-flexoffice-adminui-test.xml");
		userEndpoint = (UserEndpointImpl)context.getBean("userEndpoint");
		gatewayEndpoint = (GatewayEndpointImpl)context.getBean("gatewayEndpoint");
	}


	@Test
	public void TestA_initTables() {
		// SetUp
		boolean state = gatewayEndpoint.executeInitTestFile();
		 
		// Assert
		assertEquals(true, state);
	}

	@Test
	public void TestB_cleanUserTable() throws WebApplicationException {

		// Test
		List<UserSummary> response = userEndpoint.getUsers();

		if (response.size() > 0) {
			for(final UserSummary user: response) {
				userEndpoint.removeUser(user.getId());
			}			
		}		

		response = userEndpoint.getUsers();

		// Assert
		assertEquals(0, response.size());
	}

	@Test
	public void TestC_addUser() throws WebApplicationException {
		// Setup
		final UserInput userHmi = factory.createHmiUser("firstNameTest1", "lastNameTest1", "emailTest1");

		// Test
		final User response = userEndpoint.addUser(userHmi);

		// Assert
		assertNotNull(response.getId());
	}

	@Test
	public void TestD_getUser() throws WebApplicationException {
		// Setup
		boolean expectedResult = false;
		final UserInput expecteduser = factory.createHmiUser("firstNameTest1", "lastNameTest1", "emailTest1");
		
		try {
			final UserDao user = userEndpoint.findByUserMail("emailTest1");

			// Test
			final User userFromDB = userEndpoint.getUser(user.getColumnId());

			// Assert
			assertEquals(expecteduser.getFirstName(), userFromDB.getFirstName());	
			assertEquals(expecteduser.getLastName(), userFromDB.getLastName());	
			assertEquals(expecteduser.getEmail(), userFromDB.getEmail());	

			expectedResult = true;

		} catch(DataNotExistsException e ) {
		}


		// Assert
		assertEquals(true, expectedResult);	
	}


	@Test
	public void TestE_getUsers() throws WebApplicationException {

		// Test
		final List<UserSummary> response = userEndpoint.getUsers();

		// Assert
		assertEquals(1, response.size());
	}


	@Test
	public void TestF_addUserDataAlreadyExistsException() {
		// Setup
		boolean expectedResult = false;
		
		// Test
		try {
			// Setup
			final UserDao user = userEndpoint.findByUserMail("emailTest1");
			if (user != null) {
				
				UserInput userHmi = factory.createHmiUser(user.getFirstName(), user.getLastName(), user.getEmail());

				userEndpoint.addUser(userHmi);
			}
			
		} catch(DataNotExistsException e ) {
		
		} catch (WebApplicationException e) {
			expectedResult = true;
		}

		// Assert
		assertEquals(true, expectedResult);	
	}

	
	@Test
	public void TestG_updateUser() throws WebApplicationException {
		// Setup
		boolean expectedResult = false;
		final UserInput userHmi = factory.createHmiUser("firstNameTest2", "lastNameTest2", "emailTest2");

		try {
			final UserDao user = userEndpoint.findByUserMail("emailTest1");


			// Test
			Response response = userEndpoint.updateUser(user.getColumnId(), userHmi);

			// Assert
			assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());
			expectedResult = true;

		} catch(DataNotExistsException e ) {
		}

		// Assert
		assertEquals(true, expectedResult);	
	}

	@Test
	public void TestH_updateUserDataNotExistsException() {
		// Setup
		boolean expectedResult = false;

		// Test
		try {
			// Setup
			final UserInput userHmi = factory.createHmiUser("firstNameTest3", "lastNameTest3", "emailTest3");
			final UserDao user = userEndpoint.findByUserMail("emailTest2");

			userEndpoint.updateUser(user.getColumnId()+"1", userHmi);

		} catch(DataNotExistsException e ) {

		} catch (WebApplicationException e) {
			expectedResult = true;
		}

		// Assert
		assertEquals(true, expectedResult);	
	}
	
	@Test
	public void TestI_removeUserDataNotExistsException() {
		// Setup
		boolean expectedResult = false;

		// Test
		try {
			// Setup
			final UserDao user = userEndpoint.findByUserMail("emailTest2");

			// Test
			userEndpoint.removeUser(user.getColumnId()+"1");
			
		} catch(DataNotExistsException e ) {

		} catch (WebApplicationException e) {
			expectedResult = true;
		}

		// Assert
		assertEquals(true, expectedResult);	
	}


	@Test
	public void TestJ_removeUser() throws WebApplicationException {
		// Setup
		boolean expectedResult = false;

		try {
			final UserDao user = userEndpoint.findByUserMail("emailTest2");

			// Test
			Response response = userEndpoint.removeUser(user.getColumnId());

			// Assert
			assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
			expectedResult = true;

		} catch(DataNotExistsException e ) {
		}
		
		// Assert
		assertEquals(true, expectedResult);	
	}
	

	@Test
	public void TestK_getWrongUser() {
		// Setup
		boolean expectedResult = false;
		
		try {
			// Test
			userEndpoint.getUser("1899898985");

		} catch(WebApplicationException e ) {
			expectedResult = true;
		}


		// Assert
		assertEquals(true, expectedResult);	
	}



}
