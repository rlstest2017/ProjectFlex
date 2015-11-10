package com.orange.flexoffice.dao.common.repository.data;

import java.util.List;

import com.orange.flexoffice.dao.common.model.data.AlertDao;

/**
 * AlertDaoOperations
 * @author oab
 *
 */
public interface AlertDaoOperations {
	
	List<AlertDao> findAllAlerts();
	
	
}
