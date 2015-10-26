package com.orange.flexoffice.business.gatewayapi.service.data;

import com.orange.flexoffice.business.gatewayapi.exception.DataAlreadyExistsException;
import com.orange.flexoffice.dao.gatewayapi.model.data.Preference;

public interface PreferenceManager {

	Preference find(long id);

	Preference save(Preference pref) throws DataAlreadyExistsException;

	Preference update(Preference pref);

	void delete(long id);

}