package com.orange.flexoffice.adminui.ws.endPoint.entity.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URISyntaxException;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.xml.datatype.DatatypeConfigurationException;


import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.orange.flexoffice.adminui.ws.endPoint.entity.UserEndpoint;
import com.orange.flexoffice.adminui.ws.endPoint.entity.impl.UserEndpointImpl;
import com.orange.flexoffice.adminui.ws.endPoint.support.ObjectFactory;
import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.dao.common.model.data.UserDao;
import com.orange.flexoffice.adminui.ws.model.User;
import com.orange.flexoffice.adminui.ws.model.UserInput;

public class UserEndPointImplTest {
	
	private static ClassPathXmlApplicationContext context;
		
	private static UserEndpoint userEndpoint;
	
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
    }
	
		
	@Test
	public void addUser() throws URISyntaxException, DataAlreadyExistsException {
		// Setup
		UserInput userHmi = factory.createHmiUser("firstNameTest1", "lastNameTest1", "emailTest1");
			
		// Test
		User response = userEndpoint.addUser(userHmi);
		
		// Asserts
		assertNotNull(response.getId());
	}
	
	@Test
	public void getUser() throws URISyntaxException, DatatypeConfigurationException {
		// Setup
		boolean expectedResult = false;
		UserInput expecteduser = factory.createHmiUser("firstNameTest1", "lastNameTest1", "emailTest1");
		UserDao user = userEndpoint.findByUserMail("emailTest1");
		
		// Test
		if (user != null) {
			User userFromDB = userEndpoint.getUser(user.getColumnId());
			
			if (((expecteduser.getFirstName().compareTo(userFromDB.getFirstName())) == 0) &&
					((expecteduser.getLastName().compareTo(userFromDB.getLastName())) == 0) &&
					((expecteduser.getEmail().compareTo(userFromDB.getEmail())) == 0)
		       ){
				
				expectedResult = true;
		    }
		}
		
		// Asserts
		assertEquals(true, expectedResult);	
		
	}

//	@Test(expected = DataAlreadyExistsException.class)
//	public void addUserDataAlreadyExistsException() throws DataAlreadyExistsException {
//		// Setup
//		UserInput userHmi = factory.createHmiUser("firstNameTest1", "lastNameTest1", "emailTest1");
//				
//		// Test
//		try {
//			userEndpoint.addUser(userHmi);
//		} finally {
//		}
//	}
	
	@Test
	public void updateUser() throws URISyntaxException, DataNotExistsException {
		// Setup
		UserInput userHmi = factory.createHmiUser("firstNameTest2", "lastNameTest2", "emailTest2");
		UserDao user = userEndpoint.findByUserMail("emailTest1");
		
		// Test
		Response response = userEndpoint.updateUser(user.getColumnId(), userHmi);
	
		// Asserts
		assertEquals(Status.ACCEPTED.getStatusCode(), response.getStatus());
		
	}
	
	@Test
	public void removeUser() {
		// Setup
		UserDao user = userEndpoint.findByUserMail("emailTest2");
		
		// Test
		Response response = userEndpoint.removeUser(user.getColumnId());
		
		// Asserts
		assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
	}
	

}
