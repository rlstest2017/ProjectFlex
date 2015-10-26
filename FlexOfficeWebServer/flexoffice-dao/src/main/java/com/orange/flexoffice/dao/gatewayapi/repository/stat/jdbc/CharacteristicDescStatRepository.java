package com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc;

import static com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc.metadata.CharacteristicDescStatMetadata.*;

import org.springframework.stereotype.Repository;

@Repository
public class CharacteristicDescStatRepository extends AbstractStatRepository {

	@Override
	protected String getTableName() {
		return CHARACTERISTIC_DESC_STAT_TABLE_NAME;
	}
	
}
