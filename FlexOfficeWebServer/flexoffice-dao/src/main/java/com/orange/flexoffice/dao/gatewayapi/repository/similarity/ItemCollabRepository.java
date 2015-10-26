package com.orange.flexoffice.dao.gatewayapi.repository.similarity;

import org.springframework.stereotype.Repository;

@Repository
public class ItemCollabRepository extends AbstractSimilarityRepository {

	private static final String TABLE_NAME = "simi_item_collab";
	
	public ItemCollabRepository() {
		super(TABLE_NAME);
	}

}
