package com.orange.flexoffice.business.gatewayapi.admin;

import com.orange.flexoffice.business.gatewayapi.matrix.SimilarityMatrixType;

public class SimilarityMatrixInfo {

	private SimilarityMatrixType type;
	private boolean available;
	private long nbSimilarities;
	private float maxSimilarity;
	private float minSimilarity;
	
	public SimilarityMatrixType getType() {
		return type;
	}
	public void setType(SimilarityMatrixType type) {
		this.type = type;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	public long getNbSimilarities() {
		return nbSimilarities;
	}
	public void setNbSimilarities(long nbSimilarities) {
		this.nbSimilarities = nbSimilarities;
	}
	public float getMaxSimilarity() {
		return maxSimilarity;
	}
	public void setMaxSimilarity(float maxSimilarity) {
		this.maxSimilarity = maxSimilarity;
	}
	public float getMinSimilarity() {
		return minSimilarity;
	}
	public void setMinSimilarity(float minSimilarity) {
		this.minSimilarity = minSimilarity;
	}
	
	
}
