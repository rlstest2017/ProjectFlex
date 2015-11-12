package com.orange.flexoffice.business.common.service.data;

import java.util.List;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.dao.common.model.data.GatewayDao;
import com.orange.flexoffice.dao.common.model.object.GatewayDto;
import com.orange.flexoffice.dao.common.model.object.RoomDto;

/**
 * GatewayManager
 * @author oab
 *
 */
public interface GatewayManager {
	/**
	 * findAllGateways method used by adminui & gatewayapi
	 * @return
	 */
	List<GatewayDao> findAllGateways();
	
	/**
	 * finGatewayRooms method used by gatewayapi
	 * @param gatewayId
	 * @return
	 */
	List<RoomDto> findGatewayRooms(long gatewayId);
	
	/**
	 * Finds a {@link GatewayDto} by its ID.
	 * method used by adminui
	 * @param gatewayId
	 * 		  the {@link gatewayId} ID
	 * @return a {@link GatewayDto}
	 */
	GatewayDto find(long gatewayId)  throws DataNotExistsException;
	
	/**
	 * Saves a {@link GatewayDto}
	 * method used by adminui
	 * @param GatewayDto
	 * 		  the new {@link GatewayDto}
	 * @return a saved {@link GatewayDto}
	 * @throws DataAlreadyExistsException 
	 */
	GatewayDto save(GatewayDto gatewayDto) throws DataAlreadyExistsException;

	/**
	 * Updates a {@link GatewayDto}
	 * method used by adminui
	 * @param GatewayDto
	 * 		  the new {@link GatewayDto}
	 * @return a saved {@link GatewayDto}
	 */
	GatewayDto update(GatewayDto gatewayDto) throws DataNotExistsException;

	/**
	 * Deletes a {@link GatewayDto}.
	 * method used by adminui
	 * @param id 
	 * 		  a {@link GatewayDto} ID
	 */
	void delete(long id) throws DataNotExistsException;

	// used for tests
	boolean executeGatewaysTestFile();
	
	
	
}