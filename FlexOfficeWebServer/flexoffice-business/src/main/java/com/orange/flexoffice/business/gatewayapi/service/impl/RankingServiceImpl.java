package com.orange.flexoffice.business.gatewayapi.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.gatewayapi.matrix.SimilarityMatrix;
import com.orange.flexoffice.business.gatewayapi.matrix.SimilarityMatrixManager;
import com.orange.flexoffice.business.gatewayapi.matrix.SimilarityMatrixType;
import com.orange.flexoffice.business.gatewayapi.recommendation.Scoring;
import com.orange.flexoffice.business.gatewayapi.service.RankingService;
import com.orange.flexoffice.business.gatewayapi.service.support.Constraints;
import com.orange.flexoffice.business.gatewayapi.service.support.SourceManager;
import com.orange.flexoffice.business.gatewayapi.source.Source;
import com.orange.flexoffice.business.gatewayapi.source.SourceType;
import com.orange.flexoffice.dao.gatewayapi.model.RatingSystem;
import com.orange.flexoffice.dao.gatewayapi.model.ReperioElement;

@Service("rankingService")
@Transactional
public class RankingServiceImpl implements RankingService {
	
	@Autowired
	private SourceManager sourceRepository;
	@Autowired
	private SimilarityMatrixManager similarityMatrixRepository;
	@Autowired
	private RatingSystem ratingSystem;
	
	@Autowired
	private Scoring scoring;
	
	@Override
	public List<ReperioElement> doThematicRanking(String userId, List<String> itemIds) {
		Source logs = sourceRepository.findByType(SourceType.LOGS);
		SimilarityMatrix matrix = similarityMatrixRepository.findByType(SimilarityMatrixType.SIMI_ITEM_LOGS);
		
		if (logs == null && matrix == null) {
			return null;
		}
		
		List<ReperioElement> al = new ArrayList<ReperioElement>();
		for (String itemId : itemIds) {
			al.add(new ReperioElement(itemId, scoring.score(itemId, userId, logs, matrix, ratingSystem)));
		}
		Collections.sort(al);
		
		return al;
	}

	@Override
	public List<ReperioElement> doThematicRanking(String userId, List<String> itemIds, Constraints criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReperioElement> doThematicRanking(Map<String, Float> preferences,
			List<String> itemIds) {
		List<ReperioElement> res = new ArrayList<ReperioElement>();
		Source catalog = sourceRepository.findByType(SourceType.CHARACTERISTICS);
		
		for (String rowID : itemIds) {
			res.add(new ReperioElement(rowID, scoring.scoreUsingExplicitPreferences(rowID, preferences, catalog)));
		}
		Collections.sort(res);
		return res;
	}

	@Override
	public List<ReperioElement> doThematicRanking(Map<String, Float> preferences,
			List<String> itemIds, Constraints criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReperioElement> doCollaborativeRanking(String userId, List<String> itemIds) {
		List<ReperioElement> res = new ArrayList<ReperioElement>();
		
		Source preferences = sourceRepository.findByType(SourceType.PREFERENCES);
		Source catalog = sourceRepository.findByType(SourceType.CHARACTERISTICS);
		
		for (String rowID : itemIds) {
			res.add(new ReperioElement(rowID, scoring.scoreUsingPreferences(rowID, userId, preferences, catalog)));
		}
		Collections.sort(res);
		return res;
	}

	@Override
	public List<ReperioElement> doCollaborativeRanking(String userId,
			List<String> itemIds, Constraints criteria) {
		// TODO Auto-generated method stub
		return null;
	}

}
