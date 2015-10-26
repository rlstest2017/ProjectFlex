package com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc;

import static com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc.metadata.RelationshipFriendStatMetadata.*;

import org.springframework.stereotype.Repository;

@Repository
public class RelationshipFriendStatRepository extends AbstractStatRepository {

	@Override
	protected String getTableName() {
		return RELATIONSHIP_FRIEND_STAT_TABLE_NAME;
	}
	
}
