package com.orange.flexoffice.gatewayapi.ws.endPoint.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.business.gatewayapi.service.RankingService;
import com.orange.flexoffice.dao.gatewayapi.model.ReperioElement;
import com.orange.flexoffice.gatewayapi.ws.endPoint.RankingEndpoint;
import com.orange.flexoffice.gatewayapi.ws.model.ObjectFactory;
import com.orange.flexoffice.gatewayapi.ws.model.XmlItemRef;
import com.orange.flexoffice.gatewayapi.ws.model.XmlPrediction;
import com.orange.flexoffice.gatewayapi.ws.model.XmlPredictions;

public class RankingEndpointImpl implements RankingEndpoint {
	private final Logger logger = Logger.getLogger(RankingEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();
	
	@Autowired
	private RankingService rankingService;
	
	/**
	 * Get a thematic ranking of items.
	 * @throws UnknownUniverseException 
	 * 
	 * @see {@link RankingEndpoint#getThematicRanking(String, List, List)}
	 */
	public JAXBElement<XmlPredictions> getThematicRanking(String userId, List<String> preferences, List<String> items) {
		XmlPredictions xmlPredictions = factory.createXmlPredictions();
		
		if (items == null || items.isEmpty()) {
			if (logger.isInfoEnabled()) {
				logger.info("No items to rank.");
			}
			return factory.createPredictions(xmlPredictions);
		}
		
		List<ReperioElement> elements = null;
		
		if (userId != null) {
			elements = rankingService.doThematicRanking(userId, items);
		} else {
			if (preferences != null) {
				// Converts Preferences to a SparseVector
				Map<String, Float> prefs = new HashMap<String, Float>();
				for (String preference : preferences) {
					int lastIdx = preference.lastIndexOf(":");
					prefs.put(preference.substring(0, lastIdx), Float.valueOf(preference.substring(lastIdx + 1)));
				}
				//elements = rankingManager.rankRowsUsingExplicitPreferences(prefs, ids);
				elements = rankingService.doThematicRanking(prefs, items);
			}
		}
		
		if (elements != null) {
			for (ReperioElement reperioElement : elements) {
				XmlItemRef xmlItem = factory.createXmlItemRef();
				xmlItem.setId(reperioElement.getId());
				
				XmlPrediction xmlPrediction = factory.createXmlPrediction();
				xmlPrediction.setItem(xmlItem);
				xmlPrediction.setRating(reperioElement.getValue());
				xmlPredictions.getPrediction().add(xmlPrediction);
			}
		}

		return factory.createPredictions(xmlPredictions);
	}

	/**
	 * Get a collaborative ranking of items.
	 * @throws UnknownUniverseException 
	 * 
	 * @see RankingEndpoint#getCollaborativeRanking(String, String, Items)
	 */
	public JAXBElement<XmlPredictions> getCollaborativeRanking(String userId, List<String> items) {
		XmlPredictions xmlPredictions = factory.createXmlPredictions();
		if (items == null || items.isEmpty()) {
			if (logger.isInfoEnabled()) {
				logger.info("No items to rank.");
			}
			return factory.createPredictions(xmlPredictions);
		}

		List<ReperioElement> elements = rankingService.doCollaborativeRanking(userId, items);
		
		// Computes predictions
		for (ReperioElement reperioElement : elements) {
			XmlItemRef xmlItem = factory.createXmlItemRef();
			xmlItem.setId(reperioElement.getId());
			
			XmlPrediction xmlPrediction = factory.createXmlPrediction();
			xmlPrediction.setItem(xmlItem);
			xmlPrediction.setRating(reperioElement.getValue());
			xmlPredictions.getPrediction().add(xmlPrediction);
		}
		return factory.createPredictions(xmlPredictions);
	}

}
