package com.orange.flexoffice.business.gatewayapi.service.data;

import com.orange.flexoffice.business.gatewayapi.exception.DataAlreadyExistsException;
import com.orange.flexoffice.dao.gatewayapi.model.data.Characteristic;

public interface CharacteristicManager {

	/**
	 * Finds a {@link Characteristic} by its ID.
	 * 
	 * @param id
	 * 		  the {@link Characteristic} ID
	 * @return a {@link Characteristic}
	 */
	Characteristic find(long id);

	/**
	 * Saves a {@link Characteristic}.
	 * 
	 * @param charac
	 * 		  the new {@link Characteristic}
	 * @return the saved {@link Characteristic}
	 * @throws DataAlreadyExistsException 
	 */
	Characteristic save(Characteristic charac)
			throws DataAlreadyExistsException;

	/**
	 * Updates a {@link Characteristic}.
	 * 
	 * @param charac
	 * 		  the {@link Characteristic}
	 * @return the saved {@link Characteristic}
	 */
	Characteristic update(Characteristic charac);

	/**
	 * Deletes a {@link Characteristic}.
	 * 
	 * @param id 
	 * 		  a {@link Characteristic} ID
	 */
	void delete(long id);

}