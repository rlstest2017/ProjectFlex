package com.orange.flexoffice.dao.gatewayapi.model;

public class Stat {
	
	private Long id;
	private String objectId;
	private Float average = 0f;
	private Float sum = 0f;
	private Integer count = 0;
	
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
	public Float getAverage() {
		return average;
	}
	public void setAverage(Float average) {
		this.average = average;
	}
	public Float getSum() {
		return sum;
	}
	public void setSum(Float sum) {
		this.sum = sum;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	
	/**
	 * Adds a new rating.
	 * 
	 * @param rating
	 * 		  The rating to add
	 */
	public void addRating(Float rating) {
		setCount(getCount() + 1);
		setSum(getSum() + rating);
		setAverage(getSum() / getCount());
	}
	
	/**
	 * Removes a rating.
	 * 
	 * @param rating
	 * 		  The rating to remove
	 * 
	 * TODO Should be throws a exception if logCount is negative ?
	 */
	public void removeRating(Float rating) {
		setCount(getCount() - 1);
		if (getCount() > 0) {
			setSum(getSum() - rating);
			setAverage(getSum() / getCount());
		} else {
			setSum(0f);
			setAverage(0f);
		}
	}

}
