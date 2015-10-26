package com.orange.flexoffice.dao.gateway.repository.data;

import java.util.List;

import com.orange.flexoffice.dao.gatewayapi.model.data.Relationship;

public interface RelationshipOperations extends DataOperations<Relationship> {
	
	Relationship findByUserIdAndFriendId(String userId, String friendId);
	
	List<Relationship> findByUserId(String userId);
	
	List<Relationship> findByFriendId(String friendId);

}
