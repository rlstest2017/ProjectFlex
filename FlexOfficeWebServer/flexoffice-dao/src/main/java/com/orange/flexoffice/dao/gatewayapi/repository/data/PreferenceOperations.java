package com.orange.flexoffice.dao.gatewayapi.repository.data;

import java.util.List;

import com.orange.flexoffice.dao.gatewayapi.model.data.Preference;

public interface PreferenceOperations {
	 
	Preference findByUserIdAndDescriptorId(String userId, String descriptorId);
	
	List<Preference> findByUserId(String userId);
	
	List<Preference> findByDescriptorId(String descriptorId);

}
