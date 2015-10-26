package com.orange.flexoffice.dao.gateway.repository.data;

import java.util.Collection;
import java.util.List;

import com.orange.flexoffice.dao.gatewayapi.model.data.Data;
import com.orange.flexoffice.dao.gatewayapi.repository.support.DataExtractor;


public interface DataOperations<T extends Data> {
	
	T findOne(Long id);
	
	T save(T data);
	
	T update(T data);
	
	void delete(Long id);
	
	Long count();
	
	Data findByColumnIdAndRowId(String columnId, String rowId);
	
	List<Data> findByColumnId(String columnId);
	
	List<Data> findByRowId(String rowId);
	
	List<String> findColumnIdsRowConditions(Collection<String> rowIds);
	
	void forEach(DataExtractor dataExtractor);

}
