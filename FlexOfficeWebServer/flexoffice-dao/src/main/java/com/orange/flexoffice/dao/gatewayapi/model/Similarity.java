package com.orange.flexoffice.dao.gatewayapi.model;

public class Similarity {
	
	private Long id;
	private String objectId;
	private String similarObjectId;
	private Float value;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public String getSimilarObjectId() {
		return similarObjectId;
	}
	public void setSimilarObjectId(String similarObjectId) {
		this.similarObjectId = similarObjectId;
	}
	public Float getValue() {
		return value;
	}
	public void setValue(Float value) {
		this.value = value;
	}

}
