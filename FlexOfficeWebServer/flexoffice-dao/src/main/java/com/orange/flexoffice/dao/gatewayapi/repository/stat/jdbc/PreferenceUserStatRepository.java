package com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc;

import static com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc.metadata.PreferenceUserStatMetadata.*;

import org.springframework.stereotype.Repository;

@Repository
public class PreferenceUserStatRepository extends AbstractStatRepository {

	@Override
	protected String getTableName() {
		return PREFERENCE_USER_STAT_TABLE_NAME;
	}
	
}
