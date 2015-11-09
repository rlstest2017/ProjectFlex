package com.orange.flexoffice.adminui.ws.endPoint.entity.impl;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.unitils.easymock.EasyMockUnitils.refEq;
import static org.unitils.reflectionassert.ReflectionComparatorMode.IGNORE_DEFAULTS;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.datatype.DatatypeConfigurationException;

import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import org.unitils.easymock.annotation.Mock;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;

import com.orange.flexoffice.adminui.ws.endPoint.entity.impl.UserEndpointImpl;
import com.orange.flexoffice.adminui.ws.endPoint.support.ObjectFactory;
import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.service.data.impl.UserManagerImpl;
import com.orange.flexoffice.dao.common.model.data.UserDao;
import com.orange.flexoffice.adminui.ws.endPoint.entity.UserEndpoint;
import com.orange.flexoffice.adminui.ws.model.User;

public class UserEndPointImplTest extends UnitilsJUnit4 {
	
	@TestedObject
	private UserEndpointImpl userEndpoint;
	@Mock
	@InjectIntoByType
	private UserManagerImpl userManagerMock;
	@Mock
	@InjectIntoByType
	private UriInfo uriInfoMock;
	@Mock
	private UriBuilder uriBuilderMock;

	private final ObjectFactory factory = new ObjectFactory();
		
	@Test
	public void getUser() throws URISyntaxException, DatatypeConfigurationException {
		// Setup
		//URI exceptedUri = new URI("http://mockUri");
		UserDao user = factory.createUser(null, "firstNameTest1", "lastNameTest1", "emailTest1");
		
		// Mock expectations
		expect(userManagerMock.find(1l)).andReturn(user);
		expect(uriInfoMock.getBaseUriBuilder()).andReturn(uriBuilderMock);
		EasyMockUnitils.replay();
		
		// Test
		User userHmi = userEndpoint.getUser("1");
		
		// Asserts
		assertEquals("firstNameTest1", userHmi.getFirstName());
		assertEquals("lastNameTest1", userHmi.getLastName());
		assertEquals("emailTest1", userHmi.getEmail());
		EasyMockUnitils.verify();
	}
	
	@Test
	public void addUser() throws URISyntaxException, DataAlreadyExistsException {
		// Setup
		User userHmi = factory.createHmiUser(null, "firstNameTest1", "lastNameTest1", "emailTest1");
		UserDao user = factory.createUser(null, "firstNameTest1", "lastNameTest1", "emailTest1");
		UserDao savedUser = factory.createUser(1l, "firstNameTest1", "lastNameTest1", "emailTest1");
		URI exceptedUri = new URI("http://mockUri");
		
		// Mock expectations
		expect(userManagerMock.save(refEq(user, IGNORE_DEFAULTS))).andReturn(savedUser);
		expect(uriInfoMock.getAbsolutePathBuilder()).andReturn(uriBuilderMock);
		expect(uriBuilderMock.path(UserEndpoint.class, "getUser")).andReturn(uriBuilderMock);
		expect(uriBuilderMock.build(1)).andReturn(exceptedUri);
		EasyMockUnitils.replay();
		
		// Test
		User response = userEndpoint.addUser(userHmi);
		
		// Asserts
		assertEquals("1", response.getId());
		EasyMockUnitils.verify();
	}
	
	
	@Test(expected = DataAlreadyExistsException.class)
	public void addUserDataAlreadyExistsException() throws DataAlreadyExistsException {
		// Setup
		User userHmi = factory.createHmiUser(null, "firstNameTest1", "lastNameTest1", "emailTest1");
		UserDao user = factory.createUser(null, "firstNameTest1", "lastNameTest1", "emailTest1");
		
		// Mock expectations
		expect(userManagerMock.save(refEq(user, IGNORE_DEFAULTS)))
			.andThrow(new DataAlreadyExistsException("user already exists"));
		EasyMockUnitils.replay();
		
		// Test
		try {
			userEndpoint.addUser(userHmi);
		} finally {
			EasyMockUnitils.verify();
		}
	}
	
	@Test
	public void updateUser() throws URISyntaxException, DataNotExistsException {
		// Setup
		User userHmi = factory.createHmiUser(null, "firstNameTest1", "lastNameTest1", "emailTest1");
		UserDao user = factory.createUser(null, "firstNameTest1", "lastNameTest1", "emailTest1");
		UserDao updatedUser = factory.createUser(1l, "firstNameTest1", "lastNameTest1", "emailTest1");
		URI exceptedUri = new URI("http://mockUri");
		
		// Mock expectations
		expect(userManagerMock.update(refEq(user))).andReturn(updatedUser);
		expect(uriInfoMock.getAbsolutePathBuilder()).andReturn(uriBuilderMock);
		expect(uriBuilderMock.build()).andReturn(exceptedUri);
		EasyMockUnitils.replay();
		
		// Test
		Response response = userEndpoint.updateUser("1", userHmi);
		
		// Asserts
		// Asserts
		assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
		assertTrue(response.getMetadata().containsKey("Location"));
		
		String returnUri = (String) response.getMetadata().get("Location").get(0);
		assertEquals(exceptedUri.toString(), returnUri);
		EasyMockUnitils.verify();
	}
	
	@Test
	public void removeUser() {
		// Mock expectations
		try {
			userManagerMock.delete(1l);
		} catch (DataNotExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		EasyMockUnitils.replay();
		
		// Test
		Response response = userEndpoint.removeUser("1");
		
		// Asserts
		assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());
		EasyMockUnitils.verify();
	}
	

}
