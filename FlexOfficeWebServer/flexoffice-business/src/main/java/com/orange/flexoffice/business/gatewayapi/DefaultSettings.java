package com.orange.flexoffice.business.gatewayapi;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.orange.flexoffice.business.gatewayapi.recommendation.ScoringMethods;
import com.orange.flexoffice.business.gatewayapi.recommendation.SimilarityMethods;

/**
 * Reperio default parameters 
 */
public class DefaultSettings {
	
	private static final Logger logger = Logger.getLogger(DefaultSettings.class);
	
	private static Properties properties;
	
	// Load default setting from reperio.properties
	static {
		InputStream propStream = DefaultSettings.class.getClassLoader().getResourceAsStream("reperio.properties");
		properties = new Properties();
		try {
			properties.load(propStream);
		
		} catch (IOException e) {
			logger.error("I/O error: " + e.getMessage());
		} finally {
			if(propStream != null) {
				try {
					propStream.close();
				} catch (IOException e) {
					logger.error("I/O error: " + e.getMessage());
				}
			}
		}
	}
	
	private static String getProperty(String property) {
		return properties.getProperty(property);
	}
	
	// Default range of score in Reperio.
	public final static float DEFAULT_MIN_RATING = Float.valueOf(getProperty("reperio.ratingSystem.minRating"));
	public final static float DEFAULT_MAX_RATING = Float.valueOf(getProperty("reperio.ratingSystem.maxRating"));
	public final static float DEFAULT_MEANS_RATING = Float.valueOf(getProperty("reperio.ratingSystem.meansRating"));
	
	// Used for the Fast personalised recommendation of items based on seeds of the user's Profile
	public final static int DEFAULT_NB_CANDIDATES_BY_SEEDS = Integer.valueOf(getProperty("reperio.recommendation.nbCandidatesBySeeds"));
	// Default scoring method
	public final static ScoringMethods DEFAULT_SCORING_METHOD = ScoringMethods.valueOf(getProperty("reperio.scoringMethod"));
	// Default similarity method
	public final static SimilarityMethods DEFAULT_SIMILARITY_METHOD = SimilarityMethods.valueOf(getProperty("reperio.similarityMethod"));

	// Default cache size used by sources.
	public final static int DEFAULT_SOURCE_CACHE_DEPTH = Integer.valueOf(getProperty("reperio.source.cacheDepth"));
	// Should automatically load statistics?
	public final static boolean DEFAULT_SOURCE_LOAD_STATS = Boolean.valueOf(getProperty("reperio.source.loadStats"));
	
	// Default KNN size of similarity matrix.
	public final static int DEFAULT_MATRIX_KNN_SIZE = Integer.valueOf(getProperty("reperio.similarityMatrix.knnSize"));
	// Should automatically load similarities? 
	public final static boolean DEFAULT_MATRIX_LOADS_SIMI = Boolean.valueOf(getProperty("reperio.source.loadStats"));
	

}
