package com.orange.flexoffice.dao.gatewayapi.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RatingSystem {

	@Value("${reperio.ratingSystem.maxRating}")
	private float maxRating;
	@Value("${reperio.ratingSystem.minRating}")
	private float minRating;
	@Value("${reperio.ratingSystem.meansRating}")
	private float meansRating;
	
	public float getMaxRating() {
		return maxRating;
	}
	public void setMaxRating(float maxRating) {
		this.maxRating = maxRating;
	}
	public float getMinRating() {
		return minRating;
	}
	public void setMinRating(float minRating) {
		this.minRating = minRating;
	}
	public float getMeansRating() {
		return meansRating;
	}
	public void setMeansRating(float meansRating) {
		this.meansRating = meansRating;
	}
	
	

}
