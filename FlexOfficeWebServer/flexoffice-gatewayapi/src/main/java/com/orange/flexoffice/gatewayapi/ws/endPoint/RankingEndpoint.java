package com.orange.flexoffice.gatewayapi.ws.endPoint;

import static com.orange.flexoffice.gatewayapi.ws.ParamsConst.ITEM_PARAM;
import static com.orange.flexoffice.gatewayapi.ws.ParamsConst.PREFERENCE_PARAM;
import static com.orange.flexoffice.gatewayapi.ws.ParamsConst.USER_ID_PARAM;
import static com.orange.flexoffice.gatewayapi.ws.PathConst.COLLABORATIVE_PATH;
import static com.orange.flexoffice.gatewayapi.ws.PathConst.RANKINGS_PATH;
import static com.orange.flexoffice.gatewayapi.ws.PathConst.THEMATIC_PATH;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.xml.bind.JAXBElement;

import com.orange.flexoffice.gatewayapi.ws.model.XmlPredictions;

@Path(RANKINGS_PATH)
public interface RankingEndpoint {
	
	/**
	 * Ranks a list of items according to a specific user or 
	 * an explicit list of preferences to rank a list of items.
	 * This rankings is based on thematic data, ie the user 
	 * preferences and the catalog of items are used to rank each item.
	 * 
	 * @param userId
	 * 			a user ID
	 * @param preferences
	 * 			an optionnal lis of preferences.
	 * @param items
	 * 			the list of items ID to rank.
	 * @return a list of predictions.
	 * 
	 * @see Predictions
	 */
	@GET
	@Path(THEMATIC_PATH)
	JAXBElement<XmlPredictions> getThematicRanking(
		@QueryParam(USER_ID_PARAM)String userId,
		@QueryParam(PREFERENCE_PARAM)List<String> preferences,
		@QueryParam(ITEM_PARAM)List<String> items);
	
	/**
	 * Ranks a list of items according to a specific user. This rankings is
	 * based on collaborative data, ie logs are used to rank each item.
	 * 
	 * @param userId
	 * 			a user ID
	 * @param items
	 * 			the list of items ID to rank
	 * @return a list of predictions.
	 * 
	 * @see Predictions
	 */
	@GET
	@Path(COLLABORATIVE_PATH)
	JAXBElement<XmlPredictions> getCollaborativeRanking(
		@QueryParam(USER_ID_PARAM)String userId,
		@QueryParam(ITEM_PARAM)List<String> items);

}
