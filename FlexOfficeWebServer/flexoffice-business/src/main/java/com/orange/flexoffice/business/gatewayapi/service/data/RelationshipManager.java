package com.orange.flexoffice.business.gatewayapi.service.data;

import com.orange.flexoffice.business.gatewayapi.exception.DataAlreadyExistsException;
import com.orange.flexoffice.dao.gatewayapi.model.data.Relationship;

public interface RelationshipManager {

	Relationship find(long id);

	Relationship save(Relationship rel) throws DataAlreadyExistsException;

	Relationship update(Relationship rel);

	void delete(long id);

}