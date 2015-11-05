package com.orange.flexoffice.dao.common.repository.data;

import java.util.List;

import com.orange.flexoffice.dao.common.model.data.GatewayDao;

/**
 * GatewayDaoOperations
 * @author oab
 *
 */
public interface GatewayDaoOperations {
	
	List<GatewayDao> findAllGateways();
	
	List<GatewayDao> findByGatewayId(Long gatewayId);
	
}
