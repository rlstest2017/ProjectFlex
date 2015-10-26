package com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc;

import static com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc.metadata.LogUserStatMetadata.LOG_USER_STAT_TABLE_NAME;

import org.springframework.stereotype.Repository;

@Repository
public class LogUserStatRepository extends AbstractStatRepository {

	@Override
	protected String getTableName() {
		return LOG_USER_STAT_TABLE_NAME;
	}

}