package com.orange.flexoffice.dao.common.repository.data;

import com.orange.flexoffice.dao.common.model.data.ConfigurationDao;

/**
 * ConfigurationDaoOperations
 * @author oab
 *
 */
public interface ConfigurationDaoOperations {
	
	ConfigurationDao findByKey(String key);
	
}
