package com.orange.flexoffice.gatewayapi.ws.endPoint.impl;

import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.business.gatewayapi.matrix.SimilarityMatrixType;
import com.orange.flexoffice.business.gatewayapi.service.impl.SimilarityManager;
import com.orange.flexoffice.dao.gatewayapi.model.Similarity;
import com.orange.flexoffice.gatewayapi.ws.endPoint.SimilarityEndpoint;
import com.orange.flexoffice.gatewayapi.ws.endPoint.entity.DescriptorEndpoint;
import com.orange.flexoffice.gatewayapi.ws.endPoint.entity.ItemEndpoint;
import com.orange.flexoffice.gatewayapi.ws.endPoint.entity.UserEndpoint;
import com.orange.flexoffice.gatewayapi.ws.model.ObjectFactory;
import com.orange.flexoffice.gatewayapi.ws.model.XmlDescriptorRef;
import com.orange.flexoffice.gatewayapi.ws.model.XmlItemRef;
import com.orange.flexoffice.gatewayapi.ws.model.XmlSimilarities;
import com.orange.flexoffice.gatewayapi.ws.model.XmlSimilarity;
import com.orange.flexoffice.gatewayapi.ws.model.XmlUserRef;

public class SimilarityEndpointImpl implements SimilarityEndpoint {
	private final ObjectFactory factory = new ObjectFactory();

	@Context
	private UriInfo uriInfo;
	@Autowired
	private SimilarityManager similarityManager;

	@Override
	public JAXBElement<XmlSimilarities> getSimilarObject(SimilarityMatrixType type,	String objectId, int count) {
		XmlSimilarities xmlSimilarities = factory.createXmlSimilarities();
		List<Similarity> similarities = similarityManager.getSimilarObject(type, objectId, count);
		
		if (similarities != null && !similarities.isEmpty()) {
			switch (type) {
			case SIMI_DESC_CATALOG:
			case SIMI_DESC_PREFS:
				xmlSimilarities = simiToXmlDescSimi(similarities);
				break;
			case SIMI_ITEM_CATALOG:
			case SIMI_ITEM_LOGS:
				xmlSimilarities = simiToXmlItemSimi(similarities);
				break;
			case SIMI_USER_FRIENDS:
			case SIMI_USER_LOGS:
			case SIMI_USER_PREFS:
			case SIMI_USER_TRUSTING_USERS:
				xmlSimilarities = simiToXmlUserSimi(similarities);
				break;
			}
		}
		
		return factory.createSimilarities(xmlSimilarities);
	}
	
	@Override
	public Response generateSimilarities(SimilarityMatrixType type, int speedFactor) {
		if (speedFactor > 1) {
			similarityManager.generateSimilarityMatrix(type, speedFactor);
		} else {
			similarityManager.generateSimilarityMatrix(type);
		}
		
		return Response.ok().build();
	}

	
	/**
	 * Converts domain similarities to item oriented XML similarities.
	 *
	 * @param similarities
	 * 		a list domain similarities
	 * @return
	 * 		a list of XML similarities
	 * 
	 * @see Similarity
	 */
	private XmlSimilarities simiToXmlItemSimi(List<Similarity> similarities) {
		XmlSimilarities result = factory.createXmlSimilarities();
		
		UriBuilder builder = uriInfo.getBaseUriBuilder()
				.path(ItemEndpoint.class)
				.path(ItemEndpoint.class, "getItem");
		
		for (Similarity similarity : similarities) {
			XmlItemRef xmlObject = factory.createXmlItemRef();
			xmlObject.setId(similarity.getObjectId());
			xmlObject.setHref(builder.build(xmlObject.getId()).toString());
			
			XmlItemRef xmlSimilar = factory.createXmlItemRef();
			xmlSimilar.setId(similarity.getSimilarObjectId());
			xmlSimilar.setHref(builder.build(xmlSimilar.getId()).toString());

			XmlSimilarity xmlSimi = factory.createXmlSimilarity();
			xmlSimi.setItem(xmlObject);
			xmlSimi.setSimilarItem(xmlSimilar);
			xmlSimi.setRating(similarity.getValue());
			
			result.getSimilarity().add(xmlSimi);
		}
		return result;
	}
	
	/**
	 * Converts domain similarities to user oriented XML similarities.
	 * 
	 * @param similarities
	 * 		a list domain similarities
	 * @return
	 * 		a list of XML similarities
	 * 
	 * @see Similarity
	 */
	private XmlSimilarities simiToXmlUserSimi(List<Similarity> similarities) {
		XmlSimilarities result = factory.createXmlSimilarities();
		
		UriBuilder builder = uriInfo.getBaseUriBuilder()
				.path(UserEndpoint.class)
				.path(UserEndpoint.class, "getUser");
		
		for (Similarity similarity : similarities) {
			XmlUserRef xmlObject = factory.createXmlUserRef();
			xmlObject.setId(similarity.getObjectId());
			xmlObject.setHref(builder.build(xmlObject.getId()).toString());
			
			XmlUserRef xmlSimilar = factory.createXmlUserRef();
			xmlSimilar.setId(similarity.getSimilarObjectId());
			xmlSimilar.setHref(builder.build(xmlSimilar.getId()).toString());

			XmlSimilarity xmlSimi = factory.createXmlSimilarity();
			xmlSimi.setUser(xmlObject);
			xmlSimi.setSimilarUser(xmlSimilar);
			xmlSimi.setRating(similarity.getValue());
			
			result.getSimilarity().add(xmlSimi);
		}
		return result;
	}
	
	/**
	 * Converts domain similarities to descriptor oriented XML similarities.
	 * 
	 * @param similarities
	 * 		a list domain similarities
	 * @return
	 * 		a list of XML similarities
	 * 
	 * @see Similarity
	 * @see com.orange.flexoffice.gatewayapi.ws.model.Similarity
	 */
	private XmlSimilarities simiToXmlDescSimi (List<Similarity> similarities) {
		XmlSimilarities result = factory.createXmlSimilarities();
		
		UriBuilder builder = uriInfo.getBaseUriBuilder()
				.path(DescriptorEndpoint.class)
				.path(DescriptorEndpoint.class, "getDescriptor");
		
		for (Similarity similarity : similarities) {
			XmlDescriptorRef xmlObject = factory.createXmlDescriptorRef();
			xmlObject.setId(similarity.getObjectId());
			xmlObject.setHref(builder.build(xmlObject.getId()).toString());
			
			XmlDescriptorRef xmlSimilar = factory.createXmlDescriptorRef();
			xmlSimilar.setId(similarity.getSimilarObjectId());
			xmlSimilar.setHref(builder.build(xmlSimilar.getId()).toString());

			XmlSimilarity xmlSimi = factory.createXmlSimilarity();
			xmlSimi.setDescriptor(xmlObject);
			xmlSimi.setSimilarDescriptor(xmlSimilar);
			xmlSimi.setRating(similarity.getValue());
			
			result.getSimilarity().add(xmlSimi);
		}
		return result;
	}

}
