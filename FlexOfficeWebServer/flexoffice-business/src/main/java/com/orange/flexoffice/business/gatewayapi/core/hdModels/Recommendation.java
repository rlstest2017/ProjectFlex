package com.orange.flexoffice.business.gatewayapi.core.hdModels;

/**
 * 
 * Recommendation
 * 
 * @author Laurent Candillier
 * 
 * @version 7.0 (07/01/2009)
 * 
 * A recommendation is composed of an item identifier, a predicted rating and a trust measure
 *
 */

public class Recommendation {

	/**
	 * A recommendation is composed of an item identifier, a predicted rating and a trust measure
	 */
	private int id;
	private float rating;
	private float trust;

	/**
	 * The constructor stores the identifier, rating and trust measure
	 * @param i the item identifier
	 * @param r the predicted rating
	 * @param t the trust measure
	 */
	public Recommendation (int i, float r, float t) {
		id = i;
		rating = r;
		trust = t;
	}
	
	/**
	 * @return the identifier of the recommended item
	 */
	public int getID () {
		return id;
	}
	
	/**
	 * @return the predicted rating
	 */
	public float getRating () {
		return rating;
	}
	
	/**
	 * @return the trust measure associated to the recommendation
	 */
	public float getTrustMeasure () {
		return trust;
	}
}
