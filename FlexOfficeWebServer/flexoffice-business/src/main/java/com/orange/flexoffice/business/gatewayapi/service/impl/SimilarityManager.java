package com.orange.flexoffice.business.gatewayapi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.gatewayapi.matrix.SimilarityMatrix;
import com.orange.flexoffice.business.gatewayapi.matrix.SimilarityMatrixManager;
import com.orange.flexoffice.business.gatewayapi.matrix.SimilarityMatrixType;
import com.orange.flexoffice.dao.gatewayapi.model.Similarity;

@Service
@Transactional
public class SimilarityManager {
	
	@Autowired
	private SimilarityMatrixManager similarityMatrixRepository;
	
	public List<Similarity> getSimilarObject(SimilarityMatrixType similarityMatrixType, String objectId, int nb) {
		SimilarityMatrix similarityMatrix = similarityMatrixRepository.findByType(similarityMatrixType);
		
		return similarityMatrix.getSortedSimilarities(objectId, nb);
	}
	
	/**
	 * Generates the similarity matrix of the current universe.
	 * 
	 * @param type
	 * 		  the similarity matrix type
	 */
	public void generateSimilarityMatrix(SimilarityMatrixType type) {
		generateSimilarityMatrix(type, 1);
	}
	
	/**
	 * Generates the similarity matrix of the current universe.
	 * 
	 * @param universeId
	 *		  the current universe ID
	 * @param type
	 * 		  the similarity matrix type
	 * @param speedFactor
	 *        the speed factor by which to divide the time required to generate the matrix. 
	 *        It also lowers the RMSE of the model.
	 */
	public void generateSimilarityMatrix(SimilarityMatrixType type, int speedFactor) {
		SimilarityMatrix matrix = similarityMatrixRepository.findByType(type);
	//	matrix.generateKNNTable(speedFactor);
		matrix.loadSimilarities(true);
	}

}
