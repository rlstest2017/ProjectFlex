package com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc;

import static com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc.metadata.LogItemStatMetadata.LOG_ITEM_STAT_TABLE_NAME;

import org.springframework.stereotype.Repository;

@Repository
public class LogItemStatRepository extends AbstractStatRepository {

	@Override
	protected String getTableName() {
		return LOG_ITEM_STAT_TABLE_NAME;
	}
	
}
