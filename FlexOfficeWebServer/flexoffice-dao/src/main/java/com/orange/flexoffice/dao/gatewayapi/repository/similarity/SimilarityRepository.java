package com.orange.flexoffice.dao.gatewayapi.repository.similarity;

import java.util.List;

import com.orange.flexoffice.dao.gatewayapi.model.Similarity;

/**
 * Interface to querying concrete statistic repositories.
 * 
 * @author Guillaume Mouricou
 */
public interface SimilarityRepository {

	/**
	 * Saves a {@link Similarity}.
	 * 
	 * @param similarity
	 * 		  the {@link Similarity} to save
	 * @return The saved {@link Similarity}.
	 */
	Similarity save(Similarity similarity);
	
	/**
	 * Updates a {@link Similarity}.
	 * 
	 * @param similarity
	 * 		  the {@link Similarity} to persist
	 * @return The updated {@link Similarity}.
	 */
	Similarity update(Similarity similarity);
	
	/**
	 * Deletes a {@link Similarity}.
	 * 
	 * @param id
	 * 		  a {@link Similarity} ID
	 */
	void delete(Long id);
	
	/**
	 * @return The number of similarities.
	 */
	Long count();
	
	/**
	 * Finds a {@link Similarity} by its ID.
	 * 
	 * @param similarity
	 * 		  the {@link Similarity} ID
	 * @return A {@link Similarity}
	 */
	Similarity findOne(Long id);
	
	/**
	 * Finds {@link Similarity} by an object ID.
	 * 
	 * @param objectId
	 * 		  the object ID
	 * @return A {@link Similarity}
	 */
	Similarity findByObjectId(String objectId);
	
	List<Similarity> findAll();
	
	List<Similarity> findAllSorted();

}
