package com.orange.flexoffice.gatewayapi.ws.endPoint.data;

import static com.orange.flexoffice.gatewayapi.ws.ParamsConst.RELATIONSHIP_ID_PARAM;
import static com.orange.flexoffice.gatewayapi.ws.PathConst.RELATIONSHIPS_PATH;
import static com.orange.flexoffice.gatewayapi.ws.PathConst.RELATIONSHIP_ID_PATH;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;

import com.orange.flexoffice.business.gatewayapi.exception.DataAlreadyExistsException;
import com.orange.flexoffice.gatewayapi.ws.model.XmlRelationship;

/**
 * Specifies all operations available on the relationship resource.
 * 
 * Each relationship is identified thanks 2 users ID 
 */
@Path(RELATIONSHIPS_PATH)
public interface RelationshipEndpoint {
	
	/**
	 * Gets information about a specific relationship.
	 * 
	 * @param id
	 * 		  a relationshipId
	 * @return a <code>Relationship</code>.
	 * 
	 * @see XmlRelationship
	 */
	@GET
	@Path(RELATIONSHIP_ID_PATH)
	@Produces(MediaType.APPLICATION_XML)
	JAXBElement<XmlRelationship> getRelationship(@PathParam(RELATIONSHIP_ID_PARAM)String id);
	
	/**
	 * Add a new relationship to the current universe.
	 * 
	 * @param relationship
	 * 			the new <code>XmlRelationship</code>
	 * @return If ok, a <code>Response</code> with a status code 201 Create & Location.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	Response addRelationship(XmlRelationship relationship) throws DataAlreadyExistsException;
	
	/**
	 * Modifies a specific relationship.
	 * 
	 * @param id
	 * 		  a relationshipId
	 * @param relationships
	 * 		  the new <code>Relationship</code>
	 * @return If ok, a <code>Response</code> with a status code 201 Create & Location.
	 */
	@PUT
	@Path(RELATIONSHIP_ID_PATH)
	@Consumes(MediaType.APPLICATION_XML)
	Response updateRelationship(
			@PathParam(RELATIONSHIP_ID_PARAM)String id,
			XmlRelationship relationships);
	
	/**
	 * Deletes a specific relationship.
	 * 
	 * @param id
	 * 		  a relationshipId
	 * @return If ok, a <code>Response</code> with a status code 204 No Content.
	 */
	@DELETE
	@Path(RELATIONSHIP_ID_PATH)
	Response removeRelationship(@PathParam(RELATIONSHIP_ID_PARAM)String id);

}
