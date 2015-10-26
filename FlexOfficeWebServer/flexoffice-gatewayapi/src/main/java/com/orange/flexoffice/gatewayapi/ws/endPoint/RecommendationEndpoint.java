package com.orange.flexoffice.gatewayapi.ws.endPoint;

import static com.orange.flexoffice.gatewayapi.ws.ParamsConst.NB_PARAM;
import static com.orange.flexoffice.gatewayapi.ws.ParamsConst.USER_ID_PARAM;
import static com.orange.flexoffice.gatewayapi.ws.PathConst.COLLABORATIVE_PATH;
import static com.orange.flexoffice.gatewayapi.ws.PathConst.RECOMMENDATIONS_PATH;
import static com.orange.flexoffice.gatewayapi.ws.PathConst.THEMATIC_PATH;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.xml.bind.JAXBElement;

import com.orange.flexoffice.gatewayapi.ws.model.XmlRecommendations;

@Path(RECOMMENDATIONS_PATH)
public interface RecommendationEndpoint {

	/**
	 * Recommends for a user a list of items. This recommendation is based on
	 * thematic data, ie preferences of user and the catalog of item are used to
	 * compute recommendations.
	 * 
	 * @param userId
	 * 			a user ID
	 * @param nb
	 * 			the number of recommendations
	 * @return a list of recommendations.
	 * 
	 * @see Recommendations
	 * @see Recommendation
	 */
	@GET
	@Path(THEMATIC_PATH)
	JAXBElement<XmlRecommendations> getThematicReco(
		@QueryParam(USER_ID_PARAM)String userId,
		@QueryParam(NB_PARAM)int nb);
	
	
	/**
	 * Recommends for a user a list of items. This recommendation is based on
	 * collaborative data, ie logs are used to compute recommendations.
	 * 
	 * @param userId
	 * 			a user ID
	 * @param nb
	 * 			the number of recommendations
	 * @return a list of recommendations.
	 * 
	 * @see Recommendations
	 * @see Recommendation
	 */
	@GET
	@Path(COLLABORATIVE_PATH)
	JAXBElement<XmlRecommendations> getCollaborativeReco(
		@QueryParam(USER_ID_PARAM)String userId,
		@QueryParam(NB_PARAM)int nb);
}
