package com.orange.flexoffice.dao.gatewayapi.repository.stat;

import java.util.List;

import com.orange.flexoffice.dao.gatewayapi.model.Stat;
import com.orange.flexoffice.dao.gatewayapi.repository.support.StatExtractor;

public interface StatOperations {

	Stat findOne(Long id);

	Stat findByObjectId(String objectId);
	
	List<Stat> findAll();
	
	List<Stat> findByBestAvg(int nb);
	
	List<String> findAllObjectIds();

	Stat save(Stat stat);
	
	Stat update(Stat stat);
	
	void delete(Long id);
	
	Long count();
	
	void forEach(StatExtractor se);
}
