package com.orange.flexoffice.dao.gateway.repository.data;

import java.util.List;

import com.orange.flexoffice.dao.gatewayapi.model.data.Characteristic;

public interface CharacteristicOperations {
	
	Characteristic findByItemIdAndDescriptorId(String itemId, String descriptorId);
	
	List<Characteristic> findByItemId(String itemId);
	
	List<Characteristic> findByDescriptorId(String descriptorId);

}
