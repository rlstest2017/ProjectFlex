package com.orange.flexoffice.business.gatewayapi.admin;

import com.orange.flexoffice.business.gatewayapi.source.SourceType;

public class SourceInfo {

	private SourceType type;
	private long nbData;
	private float meanRating;
	private long nbRows;
	private long nbColumn;
	
	public SourceType getType() {
		return type;
	}
	public void setType(SourceType type) {
		this.type = type;
	}
	public long getNbData() {
		return nbData;
	}
	public void setNbData(long nbData) {
		this.nbData = nbData;
	}
	public float getMeanRating() {
		return meanRating;
	}
	public void setMeanRating(float meanRating) {
		this.meanRating = meanRating;
	}
	public long getNbRows() {
		return nbRows;
	}
	public void setNbRows(long nbRows) {
		this.nbRows = nbRows;
	}
	public long getNbColumn() {
		return nbColumn;
	}
	public void setNbColumn(long nbColumn) {
		this.nbColumn = nbColumn;
	}
	

}
