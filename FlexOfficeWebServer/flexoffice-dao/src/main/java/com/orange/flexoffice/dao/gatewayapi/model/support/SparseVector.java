package com.orange.flexoffice.dao.gatewayapi.model.support;

import java.util.Map;

public class SparseVector {

	private String objectId;
	private Map<String, Float> listObjects;
	private Float meanVector;

	/**
	 * constructor, constructs a new SparseVector from an items list
	 * 
	 * @param listObjects
	 */
	public SparseVector(String objectId, Map<String, Float> listObjects) {
		this.objectId = objectId;
		this.listObjects = listObjects;
	}

	/**
	 * get SparseVector's item id
	 * 
	 * @return itemId of the sparseVector
	 */
	public String getObjectId() {
		return objectId;
	}

	/**
	 * set the item id of the sparseVector
	 * 
	 * @param objectId
	 */
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	/**
	 * get the list of items of the SparseVector
	 * 
	 * @return listItems
	 */
	public Map<String, Float> getListObjects() {
		return listObjects;
	}

	/**
	 * set the list of items of the SparseVector
	 * 
	 * @param listObjects
	 */
	public void setListObjects(Map<String, Float> listObjects) {
		this.listObjects = listObjects;
	}

	/**
	 * get the mean rating
	 * 
	 * @return the mean rating of the SparseVector
	 */
	public float getMeanVector() {
		return meanVector;
	}

	/**
	 * set the mean rating of the SparseVector
	 */
	public void setMeanVector(float avg) {
		meanVector = avg;
	}
}
