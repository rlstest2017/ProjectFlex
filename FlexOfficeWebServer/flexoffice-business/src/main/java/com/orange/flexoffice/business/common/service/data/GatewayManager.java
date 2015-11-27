package com.orange.flexoffice.business.common.service.data;

import java.util.List;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.business.gatewayapi.dto.GatewayCommand;
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
	List<RoomDto> findGatewayRooms(String gatewayMacAddress);
	
	/**
	 * Finds a {@link GatewayDto} by its ID.
	 * method used by adminui
	 * @param gatewayId
	 * 		  the {@link gatewayId} ID
	 * @return a {@link GatewayDto}
	 */
	GatewayDto find(long gatewayId)  throws DataNotExistsException;
	
	/**
	 * Finds a {@link GatewayDto} by its macAddress.
	 * method used by adminui
	 * @param macAddress
	 * 		  the {@link gatewayId} macAddress
	 * @return a {@link GatewayDto}
	 */
	GatewayDto findByMacAddress(String macAddress)  throws DataNotExistsException;
	
	/**
	 * Saves a {@link GatewayDao}
	 * method used by adminui
	 * @param GatewayDao
	 * 		  the new {@link GatewayDao}
	 * @return a saved {@link GatewayDao}
	 * @throws DataAlreadyExistsException 
	 */
	GatewayDao save(GatewayDao gatewayDao) throws DataAlreadyExistsException;

	/**
	 * Updates a {@link GatewayDao}
	 * method used by adminui
	 * @param GatewayDao
	 * 		  the new {@link GatewayDao}
	 * @return a saved {@link GatewayDao}
	 */
	GatewayDao update(GatewayDao gatewayDao) throws DataNotExistsException;

	/**
	 * Updates a {@link GatewayDao}
	 * method used by gatewayapi
	 * @param GatewayDao
	 * 		  the new {@link GatewayDao}
	 * @return a command {@link GatewayCommand}
	 */
	GatewayCommand updateStatus(GatewayDao gatewayDao) throws DataNotExistsException;
	
	/**
	 * Deletes a {@link GatewayDto}.
	 * method used by adminui
	 * @param id 
	 * 		  a {@link GatewayDto} macAddress
	 */
	void delete(String macAddress) throws DataNotExistsException, IntegrityViolationException;
	
}