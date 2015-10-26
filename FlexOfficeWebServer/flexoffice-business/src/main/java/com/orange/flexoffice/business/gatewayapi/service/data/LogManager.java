package com.orange.flexoffice.business.gatewayapi.service.data;

import com.orange.flexoffice.business.gatewayapi.exception.DataAlreadyExistsException;
import com.orange.flexoffice.dao.gatewayapi.model.data.Log;

public interface LogManager {

	/**
	 * Finds a {@link Log} by its ID.
	 * 
	 * @param logId
	 * 		  the {@link Log} ID
	 * @return a {@link Log}
	 */
	Log find(long logId);

	/**
	 * Saves a {@link Log}
	 * 
	 * @param log
	 * 		  the new {@link Log}
	 * @return a saved {@link Log}
	 * @throws DataAlreadyExistsException 
	 */
	Log save(Log log) throws DataAlreadyExistsException;

	/**
	 * Updates a {@link Log}
	 * 
	 * @param log
	 * 		  the new {@link Log}
	 * @return a saved {@link Log}
	 */
	Log update(Log log);

	/**
	 * Deletes a {@link Log}.
	 * 
	 * @param id 
	 * 		  a {@link Log} ID
	 */
	void delete(long id);

}