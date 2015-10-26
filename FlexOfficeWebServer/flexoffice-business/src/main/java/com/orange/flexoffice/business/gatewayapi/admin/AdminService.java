package com.orange.flexoffice.business.gatewayapi.admin;

import com.orange.flexoffice.business.gatewayapi.matrix.SimilarityMatrixType;
import com.orange.flexoffice.business.gatewayapi.source.SourceType;

public interface AdminService {
	
	/**
	 * Allows to generate a similarity matrix.
	 * 
	 * @param type
	 * @param knn
	 * @param speedFactor
	 */
	void generateSimilarityMatrix(SimilarityMatrixType type, int knn, int speedFactor);
	
	/**
	 * Returns information about a data source.
	 * 
	 * @param type
	 */
	SourceInfo getSourceStatistics(SourceType type);
	
	/**
	 * Returns information about a matrix of similarities.
	 * 
	 * @param type
	 */
	SimilarityMatrixInfo getSimilarityStatistics(SimilarityMatrixType type);

}
