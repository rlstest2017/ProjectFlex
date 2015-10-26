package com.orange.flexoffice.business.gatewayapi.matrix;

/**
 * A similarities matrix type
 */
public enum SimilarityMatrixType {
	
	/**
	 * A matrix of similarities between items.
	 * Computations are based on the catalog.
	 */
	SIMI_ITEM_CATALOG,
	
	/**
	 * A matrix of similarities between items.
	 * Computations are based on user logs.
	 */
	SIMI_ITEM_LOGS,
	
	/**
	 * A matrix of similarities between users.
	 * Computations are based on user logs.
	 */
	SIMI_USER_LOGS,
	
	/**
	 * A matrix of similarities between users.
	 * Computations are based on user preferences.
	 */
	SIMI_USER_PREFS,
	
	/**
	 * A matrix of similarities between users.
	 * Computations are based on user friends.
	 */
	SIMI_USER_FRIENDS,
	
	/**
	 * A matrix of similarities between users.
	 * Computations are based on trusting users.
	 */
	SIMI_USER_TRUSTING_USERS,
	
	/**
	 * A matrix of similarities between descriptors.
	 * Computations are based on the catalog.
	 */
	SIMI_DESC_CATALOG,
	
	/**
	 * A matrix of similarities between descriptors.
	 * Computations are based on user preferences.
	 */
	SIMI_DESC_PREFS;

}
