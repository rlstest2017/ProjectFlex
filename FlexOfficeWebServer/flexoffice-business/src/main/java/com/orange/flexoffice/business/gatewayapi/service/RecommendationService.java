package com.orange.flexoffice.business.gatewayapi.service;

import java.util.List;

import com.orange.flexoffice.dao.gatewayapi.model.ReperioElement;
import com.orange.flexoffice.dao.gatewayapi.model.data.Preference;

/**
 * Defines recommendation operations.
 * 
 * @author Guillaume Mouricou
 *
 */
public interface RecommendationService {
	
	/**
	 * User's preferences + thematic matrix
	 * 
	 * @param user
	 * @param nb
	 * @return
	 */
	List<ReperioElement> doThematicRecommendation(String userId, int nb);
	
	/**
	 * Explicit list of preferences + thematic matrix
	 * 
	 * @param preferences
	 * @param nb
	 * @return
	 */
	List<ReperioElement> doThematicRecommendation(List<Preference> preferences, int nb);
	
	/**
	 * User logs + collab matrix
	 * 
	 * @param user
	 * @param nb
	 * @return
	 */
	List<ReperioElement> doCollaborativeRecommendation(String userId, int nb);
	
	/**
	 * User logs + thematic matrix
	 * 
	 * @param user
	 * @param nb
	 * @return
	 */
	List<ReperioElement> doHybridRecommendation(String userId, int nb);

}
