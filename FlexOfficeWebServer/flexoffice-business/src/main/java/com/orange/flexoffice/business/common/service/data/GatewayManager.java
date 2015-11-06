package com.orange.flexoffice.business.common.service.data;

import java.util.List;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.dao.common.model.object.GatewayDto;

/**
 * GatewayManager
 * @author oab
 *
 */
public interface GatewayManager {

	List<GatewayDto> findAllGateways();
	
	/**
	 * Finds a {@link GatewayDto} by its ID.
	 * 
	 * @param gatewayId
	 * 		  the {@link gatewayId} ID
	 * @return a {@link GatewayDto}
	 */
	GatewayDto find(long gatewayId)  throws DataNotExistsException;

	/**
	 * Saves a {@link GatewayDto}
	 * 
	 * @param GatewayDto
	 * 		  the new {@link GatewayDto}
	 * @return a saved {@link GatewayDto}
	 * @throws DataAlreadyExistsException 
	 */
	GatewayDto save(GatewayDto gatewayDto) throws DataAlreadyExistsException;

	/**
	 * Updates a {@link GatewayDto}
	 * 
	 * @param GatewayDto
	 * 		  the new {@link GatewayDto}
	 * @return a saved {@link GatewayDto}
	 */
	GatewayDto update(GatewayDto UserFlexoffice) throws DataNotExistsException;

	/**
	 * Deletes a {@link GatewayDto}.
	 * 
	 * @param id 
	 * 		  a {@link GatewayDto} ID
	 */
	void delete(long id) throws DataNotExistsException;

}