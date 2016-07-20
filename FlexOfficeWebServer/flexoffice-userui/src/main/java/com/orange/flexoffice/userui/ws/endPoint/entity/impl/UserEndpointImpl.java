package com.orange.flexoffice.userui.ws.endPoint.entity.impl;

import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.util.Date;

import javax.naming.AuthenticationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.userui.ws.utils.ErrorMessageHandler;
import com.orange.flexoffice.userui.ws.model.Token;
import com.orange.flexoffice.userui.ws.model.User;
import com.orange.flexoffice.userui.ws.model.UserContext;
import com.orange.flexoffice.userui.ws.model.UserInput;
import com.orange.flexoffice.userui.ws.model.UserPreferences;
import com.orange.flexoffice.business.common.enums.EnumErrorModel;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.service.data.BuildingManager;
import com.orange.flexoffice.business.common.service.data.CityManager;
import com.orange.flexoffice.business.common.service.data.CountryManager;
import com.orange.flexoffice.business.common.service.data.PreferenceUserManager;
import com.orange.flexoffice.business.common.service.data.RegionManager;
import com.orange.flexoffice.business.common.service.data.SystemManager;
import com.orange.flexoffice.business.common.service.data.TestManager;
import com.orange.flexoffice.business.common.service.data.UserManager;
import com.orange.flexoffice.dao.common.model.data.CountryDao;
import com.orange.flexoffice.dao.common.model.data.PreferencesDao;
import com.orange.flexoffice.dao.common.model.data.UserDao;
import com.orange.flexoffice.dao.common.model.object.BuildingDto;
import com.orange.flexoffice.dao.common.model.object.CityDto;
import com.orange.flexoffice.dao.common.model.object.RegionDto;
import com.orange.flexoffice.dao.common.model.object.UserDto;
import com.orange.flexoffice.userui.ws.endPoint.entity.UserEndpoint;
import com.orange.flexoffice.userui.ws.model.ELocationItemType;
import com.orange.flexoffice.userui.ws.model.LocationItem;
import com.orange.flexoffice.userui.ws.model.ObjectFactory;

/**
 * UserEndpointImpl
 * @author oab
 *
 */
public class UserEndpointImpl implements UserEndpoint {
	
	private static final Logger LOGGER = Logger.getLogger(UserEndpointImpl.class);
	//------------------------------------------------
	// WARNING !!! : If this value is modified in DB (length of firstName, lastName or email fields of users table)
	// the value must be modified too !!!
	//------------------------------------------------
	private static final int LOGIN_INFOS_LENGTH = 100;
	private final ObjectFactory factory = new ObjectFactory();
	
	@Autowired
	private UserManager userManager;
	@Autowired
	private PreferenceUserManager preferenceManager;
	@Autowired
	private CountryManager countryManager;
	@Autowired
	private RegionManager regionManager;
	@Autowired
	private CityManager cityManager;
	@Autowired
	private BuildingManager buildingManager;
	@Autowired
	private SystemManager systemManager;
	@Autowired
	private TestManager testManager;
	@Autowired
	private ErrorMessageHandler errorMessageHandler;
	
	@Override
	public User getUserCurrent(String auth) {
		LOGGER.debug( "Begin call UserUi.UserEndpoint.getUserCurrent at: " + new Date() );

		try {
			UserDto data = userManager.findByUserAccessToken(auth);

			User user = factory.createUser();
			user.setId(data.getId());
			user.setEmail(data.getEmail());
			
			if (data.getFirstName() != null) {
				user.setFirstName(data.getFirstName());
			} else {
				data.setFirstName("");
			}
			
			if (data.getLastName() != null) {
				user.setLastName(data.getLastName());
			} else {
				data.setLastName("");
			}
			
			if (data.getRoomId() != null) {
				user.setRoomId(data.getRoomId());
			}
			
			// add context
			user.setContext(getUserContext(Long.valueOf(data.getId())));
			
			
			LOGGER.debug( "End call UserEndpoint.getUser at: " + new Date() );

			return factory.createUser(user).getValue();

		} catch (AuthenticationException e){
			LOGGER.debug("DataNotExistsException in UserUi.UserEndpoint.getUserCurrent with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_34, Response.Status.UNAUTHORIZED));
		} catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in UserUi.UserEndpoint.getUserCurrent with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}
	}

	//security="none", not filtered by spring-security, then I must to check origin header parameter
	@Override
	public Response login(/*String auth,*/String origin, UserInput user) {
		try {
			// Optional firstName, lastName defined in Body for create a new user
			UserDao userToCreate = null;
			if (user != null) {
				userToCreate = new UserDao();
				String email = user.getEmail();
				if  (email != null) {
					userToCreate.setEmail(email.trim());
				} else {
					LOGGER.error("email in UserEndpoint.login() method is null.");
					throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_91, Response.Status.METHOD_NOT_ALLOWED));
				}
				String firstName = user.getFirstName();
				if (firstName != null) {
					if (firstName.trim().length() > LOGIN_INFOS_LENGTH) {
						LOGGER.debug("Invalid firstName length in UserEndpoint.login() method.");
						throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_5, Response.Status.METHOD_NOT_ALLOWED));
					}
					userToCreate.setFirstName(firstName.trim());
				}
				String lastName = user.getLastName();
				if  (lastName != null) {
					if (lastName.trim().length() > LOGIN_INFOS_LENGTH) {
						LOGGER.debug("Invalid lastName length in UserEndpoint.login() method.");
						throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_5, Response.Status.METHOD_NOT_ALLOWED));
					}
					userToCreate.setLastName(lastName.trim());
				}
			}
			
			UserDao userToken = systemManager.processLogin(null, false, userToCreate, LOGIN_INFOS_LENGTH);
			Token token = factory.createToken();
			token.setAccessToken(userToken.getAccessToken());
			token.setExpiredDate(userToken.getExpiredTokenDate().getTime());
			token.setIsCreatedFromUserUi(userToken.getIsCreatedFromUserui());
					
			if (origin != null) {
				LOGGER.debug("Origin value is :" + origin);
				return Response.ok(token).status(200)
			            .header("Access-Control-Allow-Origin", "*")
			            .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
			            .header("Access-Control-Allow-Credentials", "true")
			            .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
			            .header("Access-Control-Max-Age", "1209600")
			            .build();
			} else {
	        	LOGGER.debug("Origin value is null");
	        	return Response.status(200).entity(token).build();
	        }
		} catch (DataNotExistsException e) {
				LOGGER.debug("DataNotExistsException in login() UserEndpointImpl with message :" + e.getMessage(), e);
				throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_34, Response.Status.METHOD_NOT_ALLOWED));
		} catch (AuthenticationException e) {
				LOGGER.debug("AuthenticationException in login() UserEndpointImpl with message :" + e.getMessage(), e);
				throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_33, Response.Status.UNAUTHORIZED));
				
		} catch (InvalidParameterException e) {
			LOGGER.debug("InvalidParameterException in login() UserEndpointImpl with message :" + e.getMessage(), e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_5, Response.Status.METHOD_NOT_ALLOWED));
			
		} catch (RuntimeException ex) {
				LOGGER.debug("RuntimeException in login() UserEndpointImpl with message :" + ex.getMessage(), ex);
				throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}
	}

	@Override
	public Response logout(String token) {
		systemManager.processLogout(token);
		return Response.status(200).build();
	}

	@Override
	public Response options() {
	    return Response.ok("")
	            .header("Access-Control-Allow-Origin", "*")
	            .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization, x-auth-token")
	            .header("Access-Control-Allow-Credentials", "true")
	            .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
	            .header("Access-Control-Max-Age", "1209600")
	            .build();
	}

	/** Initialize tests data in DB
	 * 
	 * @return true if successfully done
	 */
	@Override
	public boolean executeInitTestFile() {
		return testManager.executeInitTestFile();
	}

	/**
	 * getUserContext
	 * @param userId
	 * @return
	 */
	private UserContext getUserContext(long userId) {
		UserContext userContext = factory.createUserContext();
		userContext.setUseLocationExplorer(preferenceManager.useLocationExplorer());
		try {
		PreferencesDao preferences =preferenceManager.findByUserId(userId);
		UserPreferences userPref = factory.createUserPreferences();
			if (preferences.getCountryId()!=null) {
				LocationItem countryItem = factory.createLocationItem();
				CountryDao country = countryManager.find(preferences.getCountryId());
				countryItem.setId(country.getColumnId());
				countryItem.setName(country.getName());
				countryItem.setType(ELocationItemType.COUNTRY);
				userPref.setCountry(countryItem);
			}
			if (preferences.getRegionId()!=null) {
				LocationItem regionItem = factory.createLocationItem();
				RegionDto region = regionManager.find(preferences.getRegionId());
				regionItem.setId(region.getId().toString());
				regionItem.setName(region.getName());
				regionItem.setType(ELocationItemType.REGION);
				userPref.setRegion(regionItem);
			}
			if (preferences.getCityId()!=null) {
				LocationItem cityItem = factory.createLocationItem();
				CityDto city = cityManager.find(preferences.getCityId());
				cityItem.setId(city.getId().toString());
				cityItem.setName(city.getName());
				cityItem.setType(ELocationItemType.CITY);
				userPref.setCity(cityItem);
			}
			if (preferences.getBuildingId()!=null) {
				LocationItem buildingItem = factory.createLocationItem();
				BuildingDto building = buildingManager.find(preferences.getBuildingId());
				buildingItem.setId(building.getId().toString());
				buildingItem.setName(building.getName());
				buildingItem.setType(ELocationItemType.BUILDING);
				userPref.setBuilding(buildingItem);
				userPref.setFloor(BigInteger.valueOf(preferences.getFloor()));
			}
			userContext.setUserPreferences(userPref);
		} catch (DataNotExistsException e) {
			LOGGER.debug("DataNotExistsException in getUserContext().");	
		}
		
		return userContext;
	}
	
}
