package com.orange.flexoffice.business.userui.service.data;

import com.orange.flexoffice.business.gatewayapi.exception.DataAlreadyExistsException;
import com.orange.flexoffice.dao.userui.model.data.UserFlexoffice;

public interface UserFlexofficeManager {

	/**
	 * Finds a {@link UserFlexoffice} by its ID.
	 * 
	 * @param UserFlexofficeId
	 * 		  the {@link UserFlexoffice} ID
	 * @return a {@link UserFlexoffice}
	 */
	UserFlexoffice find(long UserFlexofficeId);

	/**
	 * Saves a {@link UserFlexoffice}
	 * 
	 * @param UserFlexoffice
	 * 		  the new {@link UserFlexoffice}
	 * @return a saved {@link UserFlexoffice}
	 * @throws DataAlreadyExistsException 
	 */
	UserFlexoffice save(UserFlexoffice UserFlexoffice) throws DataAlreadyExistsException;

	/**
	 * Updates a {@link UserFlexoffice}
	 * 
	 * @param UserFlexoffice
	 * 		  the new {@link UserFlexoffice}
	 * @return a saved {@link UserFlexoffice}
	 */
	UserFlexoffice update(UserFlexoffice UserFlexoffice);

	/**
	 * Deletes a {@link UserFlexoffice}.
	 * 
	 * @param id 
	 * 		  a {@link UserFlexoffice} ID
	 */
	void delete(long id);

}