package com.orange.flexoffice.adminui.ws.endPoint.entity.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.xml.datatype.DatatypeConfigurationException;


import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.AssertThrows;

import com.orange.flexoffice.adminui.ws.endPoint.entity.GatewayEndpoint;
import com.orange.flexoffice.adminui.ws.endPoint.entity.UserEndpoint;
import com.orange.flexoffice.adminui.ws.endPoint.entity.impl.UserEndpointImpl;
import com.orange.flexoffice.adminui.ws.endPoint.support.ObjectFactory;
import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.dao.common.model.data.UserDao;
import com.orange.flexoffice.adminui.ws.model.User;
import com.orange.flexoffice.adminui.ws.model.UserInput;
import com.orange.flexoffice.adminui.ws.model.UserSummary;

public class UserEndPointImplTest {

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
	public void initTables() {
		// SetUp
		boolean state = gatewayEndpoint.executeInitTestFile();
		 
		// Assert
		assertEquals(true, state);
	}

	@Test
	public void cleanUserTable() throws WebApplicationException {

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
	public void addUser() throws WebApplicationException {
		// Setup
		final UserInput userHmi = factory.createHmiUser("firstNameTest1", "lastNameTest1", "emailTest1");

		// Test
		final User response = userEndpoint.addUser(userHmi);

		// Assert
		assertNotNull(response.getId());
	}

	@Test
	public void getUser() throws WebApplicationException {
		// Setup
		boolean expectedResult = false;
		final UserInput expecteduser = factory.createHmiUser("firstNameTest1", "lastNameTest1", "emailTest1");
		final UserDao user = userEndpoint.findByUserMail("emailTest1");

		if (user != null) {
			// Test
			final User userFromDB = userEndpoint.getUser(user.getColumnId());

			// Assert
			assertEquals(expecteduser.getFirstName(), userFromDB.getFirstName());	
			assertEquals(expecteduser.getLastName(), userFromDB.getLastName());	
			assertEquals(expecteduser.getEmail(), userFromDB.getEmail());	

			expectedResult = true;
		}

		// Assert
		assertEquals(true, expectedResult);	
	}


	@Test
	public void getUsers() throws WebApplicationException {

		// Test
		final List<UserSummary> response = userEndpoint.getUsers();

		// Assert
		assertEquals(1, response.size());
	}


	@Test
	public void addUserDataAlreadyExistsException() {
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
			
		} catch (WebApplicationException e) {
			expectedResult = true;
		}

		// Assert
		assertEquals(true, expectedResult);	
	}

	
	@Test
	public void updateUser() throws WebApplicationException {
		// Setup
		boolean expectedResult = false;
		final UserInput userHmi = factory.createHmiUser("firstNameTest2", "lastNameTest2", "emailTest2");
		final UserDao user = userEndpoint.findByUserMail("emailTest1");

		if (user != null) {

			// Test
			Response response = userEndpoint.updateUser(user.getColumnId(), userHmi);

			// Assert
			assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());
			expectedResult = true;
		}
		// Assert
		assertEquals(true, expectedResult);	
	}

	@Test
	public void updateUserDataNotExistsException() {
		// Setup
		boolean expectedResult = false;

		// Test
		try {
			// Setup
			final UserInput userHmi = factory.createHmiUser("firstNameTest3", "lastNameTest3", "emailTest3");
			final UserDao user = userEndpoint.findByUserMail("emailTest2");

			if (user != null) {
				userEndpoint.updateUser(user.getColumnId()+"1", userHmi);
			}
		} catch (WebApplicationException e) {
			expectedResult = true;
		}

		// Assert
		assertEquals(true, expectedResult);	
	}
	@Test
	public void removeUserDataNotExistsException() {
		// Setup
		boolean expectedResult = false;

		// Test
		try {
			// Setup
			final UserDao user = userEndpoint.findByUserMail("emailTest2");

			if (user != null) {
				// Test
				userEndpoint.removeUser(user.getColumnId()+"1");
			}
			
		} catch (WebApplicationException e) {
			expectedResult = true;
		}

		// Assert
		assertEquals(true, expectedResult);	
	}


	@Test
	public void removeUser() throws WebApplicationException {
		// Setup
		boolean expectedResult = false;
		final UserDao user = userEndpoint.findByUserMail("emailTest2");

		if (user != null) {

			// Test
			Response response = userEndpoint.removeUser(user.getColumnId());

			// Assert
			assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
			expectedResult = true;
		}
		
		// Assert
		assertEquals(true, expectedResult);	
	}

}
