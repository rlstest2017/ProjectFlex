package com.orange.flexoffice.dao.common.model.data;

/**
 * InitForTestDao
 * @author oab
 *
 */
public class InitForTestDao extends AbstractData {
	
	@Override
	public void setColumnId(String columnId) {
		setId(Long.valueOf(columnId));
	}
	@Override
	public String getColumnId() {
		return getId().toString();
	}
	

}
