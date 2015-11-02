package com.orange.flexoffice.dao.userui.repository.data;

import java.util.Collection;
import java.util.List;

import com.orange.flexoffice.dao.userui.repository.support.DataExtractor;
import com.orange.flexoffice.dao.userui.model.data.Data;


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
