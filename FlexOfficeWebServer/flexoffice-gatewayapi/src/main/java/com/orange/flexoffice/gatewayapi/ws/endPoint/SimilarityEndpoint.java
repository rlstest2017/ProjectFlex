package com.orange.flexoffice.gatewayapi.ws.endPoint;

import static com.orange.flexoffice.gatewayapi.ws.ParamsConst.NB_PARAM;
import static com.orange.flexoffice.gatewayapi.ws.ParamsConst.OBJECT_ID_PARAM;
import static com.orange.flexoffice.gatewayapi.ws.ParamsConst.SPEED_FACTOR;
import static com.orange.flexoffice.gatewayapi.ws.ParamsConst.TYPE_PARAM;
import static com.orange.flexoffice.gatewayapi.ws.PathConst.GENERATE_PATH;
import static com.orange.flexoffice.gatewayapi.ws.PathConst.SIMILARITIES_PATH;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;

import com.orange.flexoffice.business.gatewayapi.matrix.SimilarityMatrixType;
import com.orange.flexoffice.gatewayapi.ws.model.XmlSimilarities;

@Path(SIMILARITIES_PATH)
public interface SimilarityEndpoint {
	
	/**
	 * Gets <code>nb</code> object similar to <code>objectId</code>.
	 * 
	 * The caller must specified the similarity matrix used to compute results.
	 * Each similarity matrix works with a specific object type. For example,
	 * {@link SimilarityMatrixType.SIMI_ITEM_LOGS} works with item ID.
	 * 
	 * @param universeId
	 * 		the current universe ID
	 * @param type
	 * 		the similarity matrix type
	 * @param objectId
	 * 		if function of <code>type</code>, a user ID, an item ID or a descriptor ID
	 * @param count
	 * 		the number of similarity
	 * 
	 * @return
	 * 		A list of similarities
	 */
	@GET
	JAXBElement<XmlSimilarities> getSimilarObject(
		@QueryParam(TYPE_PARAM)SimilarityMatrixType type,
		@QueryParam(OBJECT_ID_PARAM)String objectId,
		@QueryParam(NB_PARAM)int count);
	
	/**
	 * Generate the similarity matrix.
	 * 
	 * @param universeId
	 * 		the current universe ID
	 * @param type
	 * 		the similarity matrix type
	 * @param speedFactor
	 * 		an optional speed factor
	 * 
	 * @return If ok, a <code>Response</code> with a status code 200
	 */
	@GET
	@Path(GENERATE_PATH)
	Response generateSimilarities(
		@QueryParam(TYPE_PARAM)SimilarityMatrixType type,
		@QueryParam(SPEED_FACTOR)int speedFactor);
	

}
