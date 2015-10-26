package com.orange.flexoffice.business.gatewayapi.service;

import java.util.List;
import java.util.Map;

import com.orange.flexoffice.business.gatewayapi.service.support.Constraints;
import com.orange.flexoffice.dao.gatewayapi.model.ReperioElement;

/**
 * Defines ranking operations.
 * Currently, only items ranking is available.
 * 
 * @author Guillaume Mouricou
 *
 */
public interface RankingService {

	/**
	 * User preference + thematic matrix
	 * 
	 * @param user
	 * @param items
	 * @return
	 */
	List<ReperioElement> doThematicRanking(String userId, List<String> itemIds);
	
	/**
	 * User preference + thematic matrix + constraints
	 * 
	 * @param user
	 * @param items
	 * @param criteria
	 * @return
	 */
	List<ReperioElement> doThematicRanking(String userId, List<String> itemIds, Constraints criteria);
	
	/**
	 * Explicit preferences + thematic matrix
	 * 
	 * @param preferences
	 * @param items
	 * @return
	 */
	List<ReperioElement> doThematicRanking(Map<String, Float> preferences, List<String> itemIds);
	
	/**
	 * Explicit preferences + thematic matrix + constraints
	 * 
	 * @param preferences
	 * @param items
	 * @param criteria
	 * @return
	 */
	List<ReperioElement> doThematicRanking(Map<String, Float> preferences, List<String> itemIds, Constraints criteria);
	
	List<ReperioElement> doCollaborativeRanking(String userId, List<String> itemIds);
	
	List<ReperioElement> doCollaborativeRanking(String userId, List<String> itemIds, Constraints criteria);
}
