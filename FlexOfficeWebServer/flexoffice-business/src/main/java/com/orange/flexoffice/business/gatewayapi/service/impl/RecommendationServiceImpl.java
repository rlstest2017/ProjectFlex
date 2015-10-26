package com.orange.flexoffice.business.gatewayapi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.gatewayapi.matrix.SimilarityMatrix;
import com.orange.flexoffice.business.gatewayapi.matrix.SimilarityMatrixManager;
import com.orange.flexoffice.business.gatewayapi.matrix.SimilarityMatrixType;
import com.orange.flexoffice.business.gatewayapi.recommendation.Recommendation;
import com.orange.flexoffice.business.gatewayapi.service.RecommendationService;
import com.orange.flexoffice.business.gatewayapi.service.support.SourceManager;
import com.orange.flexoffice.business.gatewayapi.source.Source;
import com.orange.flexoffice.business.gatewayapi.source.SourceType;
import com.orange.flexoffice.dao.gatewayapi.model.RatingSystem;
import com.orange.flexoffice.dao.gatewayapi.model.ReperioElement;
import com.orange.flexoffice.dao.gatewayapi.model.data.Preference;

@Service("recommendationService")
@Transactional
public class RecommendationServiceImpl implements RecommendationService {
	
	@Autowired
	private SourceManager sourceRepository;
	@Autowired
	private SimilarityMatrixManager similarityMatrixRepository;
	@Autowired
	private Recommendation recommendation;
	@Autowired
	private RatingSystem ratingSystem;
	
	/**
	 * Gets a personalised list of recommended items.
	 * This recommendation is based on a content-based filtering system
	 * that uses item metadata and user preferences.
	 * <p>
	 * Some elements must be present in the current universe:
	 * <ul>
	 * <li>a source of type {@link SourceType#CATALOG}
	 * <li>a source of type {@link SourceType#PREFERENCES}
	 * </ul>
	 * </p>
	 * 
	 * @param userId
	 * 		  a user ID
	 * @param nb
	 * 		  the number of desired recommendations
	 * @return a list of {@link ReperioElement}
	 */
	@Override
	public List<ReperioElement> doThematicRecommendation(String userId, int nb) {
		Source catalog = sourceRepository.findByType(SourceType.CHARACTERISTICS);
		Source prefs = sourceRepository.findByType(SourceType.PREFERENCES);
		
		return recommendation.recoUsingPreferences(nb, userId, catalog, prefs, ratingSystem);
	}


	@Override
	public List<ReperioElement> doThematicRecommendation(
			List<Preference> preferences, int nb) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Gets a personalised list of recommended items.
	 * This recommendation is based on a collaborative filtering system
	 * that uses logs of users.
	 * <p>
	 * Some elements must be present in the current universe:
	 * <ul>
	 * <li>a source of type {@link SourceType#LOGS}</li>
	 * <li>a similarity matrix of type {@link SimilarityMatrixType#SIMI_ITEM_LOGS}</li>
	 * </ul></p>
	 * 
	 * @param userId
	 * 		  a user ID
	 * @param nb
	 * 		  the number of desired recommendations
	 * @return a list of {@link ReperioElement}
	 */
	@Override
	public List<ReperioElement> doCollaborativeRecommendation(String userId,
			int nb) {
		Source source = sourceRepository.findByType(SourceType.LOGS);
		SimilarityMatrix matrix = similarityMatrixRepository.findByType(SimilarityMatrixType.SIMI_ITEM_LOGS);
		
		return recommendation.recoRowsForColumn(false, 8, nb, userId, source, matrix, ratingSystem);
	}

	@Override
	public List<ReperioElement> doHybridRecommendation(String userId, int nb) {
		// TODO Auto-generated method stub
		return null;
	}

}
