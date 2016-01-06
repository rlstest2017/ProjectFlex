package com.orange.flexoffice.dao.common.repository.data;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import com.orange.flexoffice.dao.common.model.data.GatewayDao;

/**
 * GatewayDaoOperations
 * @author oab
 *
 */
public interface GatewayDaoOperations {
	
	List<GatewayDao> findAllGateways();
	
	GatewayDao findByGatewayId(Long gatewayId);
	
	GatewayDao findByMacAddress(String macAddress) throws IncorrectResultSizeDataAccessException;
	
	GatewayDao updateGatewayStatus(GatewayDao data);
	
	GatewayDao updateGatewayCommand(GatewayDao data);
	
	GatewayDao saveGateway(GatewayDao data) throws DataIntegrityViolationException;
	
	GatewayDao updateGateway(GatewayDao data);
	
	void deleteByMacAddress(String macAddress); 
}
