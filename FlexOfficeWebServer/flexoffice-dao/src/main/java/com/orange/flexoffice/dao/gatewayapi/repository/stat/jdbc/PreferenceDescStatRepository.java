package com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc;

import static com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc.metadata.PreferenceDescStatMetadata.*;

import org.springframework.stereotype.Repository;

@Repository
public class PreferenceDescStatRepository extends AbstractStatRepository {

	@Override
	protected String getTableName() {
		return PREFERENCE_DESC_STAT_TABLE_NAME;
	}

}
