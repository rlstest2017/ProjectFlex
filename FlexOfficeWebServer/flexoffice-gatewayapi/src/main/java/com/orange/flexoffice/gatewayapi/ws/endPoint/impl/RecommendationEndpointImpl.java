package com.orange.flexoffice.gatewayapi.ws.endPoint.impl;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.business.gatewayapi.service.RecommendationService;
import com.orange.flexoffice.dao.gatewayapi.model.ReperioElement;
import com.orange.flexoffice.gatewayapi.ws.endPoint.RecommendationEndpoint;
import com.orange.flexoffice.gatewayapi.ws.model.ObjectFactory;
import com.orange.flexoffice.gatewayapi.ws.model.XmlItemRef;
import com.orange.flexoffice.gatewayapi.ws.model.XmlRecommendation;
import com.orange.flexoffice.gatewayapi.ws.model.XmlRecommendations;

public class RecommendationEndpointImpl implements RecommendationEndpoint {
	private final Logger logger = Logger.getLogger(RecommendationEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();
	
	@Autowired
	private RecommendationService recommendationService;
	
	/**
	 * Return a thematic recommendation.
	 * 
	 * @see RecommendationEndpoint#getThematicReco(String, int)
	 */
	public JAXBElement<XmlRecommendations> getThematicReco(String userId, int nb) {
		XmlRecommendations xmlRecommendations = factory.createXmlRecommendations();
		if (nb == 0) {
			if (logger.isInfoEnabled()) {
				logger.info("No recommendations are returned because the query param 'kItems' is not specified or is equal to '0'");
			}
			return factory.createRecommendations(xmlRecommendations);
		}
		
		List<ReperioElement> elements = recommendationService.doThematicRecommendation(userId, nb);
		
		// Computes recommendations
		for (ReperioElement reperioElement : elements) {
			XmlItemRef xmlItem = factory.createXmlItemRef();
			xmlItem.setId(reperioElement.getId());
			
			XmlRecommendation xmlReco = factory.createXmlRecommendation();
			xmlReco.setItem(xmlItem);
			xmlReco.setRating(reperioElement.getValue());
			
			xmlRecommendations.getRecommendation().add(xmlReco);
		}
		return factory.createRecommendations(xmlRecommendations);
	}

	/**
	 * Return a collaborative recommendation.
	 * 
	 * @see {@link RecommendationEndpoint#getCollaborativeReco(String, int)}
	 */
	public JAXBElement<XmlRecommendations>  getCollaborativeReco(String userId, int nb) {
		XmlRecommendations xmlRecommendations = factory.createXmlRecommendations();
		if (nb == 0) {
			if (logger.isInfoEnabled()) {
				logger.info("No recommendations are returned because the query param 'kItems' is not specified or is equal to '0'");
			}
			return factory.createRecommendations(xmlRecommendations);
		}
		
		List<ReperioElement> elements = recommendationService.doCollaborativeRecommendation(userId,  nb);
		
		// Computes predictions
		for (ReperioElement reperioElement : elements) {
			XmlItemRef xmlItem = factory.createXmlItemRef();
			xmlItem.setId(reperioElement.getId());
			
			XmlRecommendation xmlReco = factory.createXmlRecommendation();
			xmlReco.setItem(xmlItem);
			xmlReco.setRating(reperioElement.getValue());
			
			xmlRecommendations.getRecommendation().add(xmlReco);
		}

		return factory.createRecommendations(xmlRecommendations);
	}


}
