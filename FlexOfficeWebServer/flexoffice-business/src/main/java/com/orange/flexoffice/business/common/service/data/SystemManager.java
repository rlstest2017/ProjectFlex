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
	 * 
	 * @param auth
	 * @param isFromAdminUi
	 * @param object  used in UserUI interface UserDao parameter
	 * @return
	 * @throws DataNotExistsException
	 * @throws AuthenticationException
	 */
	UserDao processLogin(String auth, Boolean isFromAdminUi, Object object, int infosDBLength) throws DataNotExistsException, AuthenticationException;
	
	void processLogout(String accessToken); 
	
	/**
	 * checkToken if token is in BD & not expired return true, else false
	 * @param token
	 * @return
	 */
	Boolean checkToken(String token);
	
	/**
	 * deleteAllTeachinSensors
	 */
	void deleteAllTeachinSensors();
}