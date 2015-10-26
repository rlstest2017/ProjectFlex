package com.orange.flexoffice.business.gatewayapi.matrix;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orange.flexoffice.business.gatewayapi.service.support.SourceManager;
import com.orange.flexoffice.business.gatewayapi.source.SourceType;
import com.orange.flexoffice.dao.gatewayapi.repository.similarity.ItemCollabRepository;

@Service
public class SimilarityMatrixManager {

	private Map<SimilarityMatrixType, SimilarityMatrix> matrices;
	
	@Autowired
	private SourceManager sourceRepository;
	
	@Autowired
	private ItemCollabRepository itemCollabRepository;
	
	@PostConstruct
	public void init() {
		matrices = new HashMap<SimilarityMatrixType, SimilarityMatrix>();
		
		matrices.put(SimilarityMatrixType.SIMI_ITEM_LOGS, new SimilarityMatrix(
				sourceRepository.findByType(SourceType.LOGS),
				100,
				false,
				0f,
				itemCollabRepository
			));
	}
	
	public SimilarityMatrix findByType(SimilarityMatrixType type) {
		return matrices.get(type);
	}
	
}
