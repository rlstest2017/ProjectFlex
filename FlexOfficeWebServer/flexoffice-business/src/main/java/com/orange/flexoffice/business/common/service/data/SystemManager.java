package com.orange.flexoffice.business.common.service.data;

import javax.naming.AuthenticationException;

import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.dao.common.model.data.UserDao;
import com.orange.flexoffice.dao.common.model.object.SystemDto;

/**
 * SystemManager
 * @author oab
 *
 */
public interface SystemManager {
		
	/**
	 * Get System data
	 * @return
	 */
	SystemDto getSystem();
	
	/**
	 * Create accessToken for authentication
	 * @param auth
	 * @return
	 */
	UserDao processLogin(String auth) throws DataNotExistsException, AuthenticationException;
	
	/**
	 * checkToken if token is in BD & not expired return true, else false
	 * @param token
	 * @return
	 */
	Boolean checkToken(String token);
}