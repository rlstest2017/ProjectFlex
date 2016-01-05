package com.orange.flexoffice.dao.common.repository.data;

import java.util.List;

import org.springframework.dao.IncorrectResultSizeDataAccessException;

import com.orange.flexoffice.dao.common.model.data.Data;


public interface DataOperations<T extends Data> {
	
	T findOne(Long id) throws IncorrectResultSizeDataAccessException;
	
	void delete(Long id);
	
	Long count();
	
	List<Data> findByColumnId(String columnId);
	
}
