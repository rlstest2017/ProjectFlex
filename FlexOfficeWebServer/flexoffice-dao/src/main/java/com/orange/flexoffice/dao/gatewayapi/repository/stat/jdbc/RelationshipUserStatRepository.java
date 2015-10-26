package com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc;

import static com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc.metadata.RelationshipUserStatMetadata.RELATIONSHIP_USER_STAT_TABLE_NAME;

import org.springframework.stereotype.Repository;

@Repository
public class RelationshipUserStatRepository extends AbstractStatRepository {

	@Override
	protected String getTableName() {
		return RELATIONSHIP_USER_STAT_TABLE_NAME;
	}
	
}
