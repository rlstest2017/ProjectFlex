package com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc;

import static com.orange.flexoffice.dao.gatewayapi.repository.stat.jdbc.metadata.CharacteristicItemStatMetadata.*;

import org.springframework.stereotype.Repository;

@Repository
public class CharacteristicItemStatRepository extends AbstractStatRepository {

	@Override
	protected String getTableName() {
		return CHARACTERISTIC_ITEM_STAT_TABLE_NAME;
	}
	
}
