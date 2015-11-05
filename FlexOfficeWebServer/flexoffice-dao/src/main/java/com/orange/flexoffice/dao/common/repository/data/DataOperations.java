package com.orange.flexoffice.dao.common.repository.data;

import java.util.Collection;
import java.util.List;

import com.orange.flexoffice.dao.common.repository.support.DataExtractor;
import com.orange.flexoffice.dao.common.model.data.Data;


public interface DataOperations<T extends Data> {
	
	T findOne(Long id);
	
	void delete(Long id);
	
	Long count();
	
	List<Data> findByColumnId(String columnId);
	
	List<String> findColumnIdsRowConditions(Collection<String> rowIds);
	
	void forEach(DataExtractor dataExtractor);

}
