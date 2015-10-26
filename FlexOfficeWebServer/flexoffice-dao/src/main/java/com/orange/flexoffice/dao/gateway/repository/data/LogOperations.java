package com.orange.flexoffice.dao.gateway.repository.data;

import java.util.List;

import com.orange.flexoffice.dao.gatewayapi.model.data.Log;

public interface LogOperations {
	
	Log findByUserIdAndItemId(String userId, String itemId);
	
	List<Log> findByUserId(String userId);
	
	List<Log> findByItemId(String itemId);

}
