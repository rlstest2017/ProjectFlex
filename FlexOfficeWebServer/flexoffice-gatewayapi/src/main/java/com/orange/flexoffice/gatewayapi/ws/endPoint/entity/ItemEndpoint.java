package com.orange.flexoffice.gatewayapi.ws.endPoint.entity;

import static com.orange.flexoffice.gatewayapi.ws.ParamsConst.ITEM_ID_PARAM;
import static com.orange.flexoffice.gatewayapi.ws.PathConst.ITEMS_PATH;
import static com.orange.flexoffice.gatewayapi.ws.PathConst.ITEM_ID_PATH;
import static com.orange.flexoffice.gatewayapi.ws.PathConst.LOGS_PATH;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;

import com.orange.flexoffice.gatewayapi.ws.model.XmlItem;
import com.orange.flexoffice.gatewayapi.ws.model.XmlLogs;

/**
 * Defines all operations available for a resource "item".
 */
@Path(ITEMS_PATH)
public interface ItemEndpoint {
	
	/**
	 * Gets information about a specific item.
	 * 
	 * @param itemId
	 * 			a item ID
	 * @return information about a specific item.
	 * 
	 * @see Item
	 */
	@GET
	@Path(ITEM_ID_PATH)
	@Produces(MediaType.APPLICATION_XML)
	JAXBElement<XmlItem> getItem(
		@PathParam(ITEM_ID_PARAM) String itemId);
	
	/**
	 * Provides the list of logs for an specific item. Each log contains a
	 * userId, a rating and a date.
	 * 
	 * @param itemId
	 *            a item id
	 * @return A list of <code>Log</code>
	 * 
	 * @see Logs
	 */
	@GET
	@Path(ITEM_ID_PATH + LOGS_PATH)
	@Produces(MediaType.APPLICATION_XML)
	JAXBElement<XmlLogs> getLogs(@PathParam(ITEM_ID_PARAM) String itemId);
	

}
