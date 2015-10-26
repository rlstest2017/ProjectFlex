package com.orange.flexoffice.business.gatewayapi.recommendation;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.orange.flexoffice.business.gatewayapi.core.dataAccess.DualBaseOfSparseVector;
import com.orange.flexoffice.business.gatewayapi.matrix.SimilarityMatrix;
import com.orange.flexoffice.business.gatewayapi.source.Source;
import com.orange.flexoffice.dao.gatewayapi.model.RatingSystem;
import com.orange.flexoffice.dao.gatewayapi.model.support.SparseVector;

/**
 * Set of scoring methods
 * 
 * @author Damien Hembert
 * @author Patrik Losquin
 * 
 */
@Component
public class Scoring {
	@Autowired
	private Similarity similarity;
	private int nbDefaultScoringCalls = 0;

	public void setSimilarity(Similarity similarity) {
		this.similarity = similarity;
	}

	/**
	 * Scores an item for an user using the item mean rating from a source.
	 * 
	 * Universe requirements : 
	 *  - a source corresponding to users logs 
	 *  - a similarity matrix of items
	 * 
	 * @param itemId
	 *            the item to score
	 * @param userId
	 *            the id of the user
	 * @param universe
	 *            the current universe
	 * 
	 * @return a float between universe.min_rating and universe.max_rating
	 */
	public float score(String itemId, String userId, Source logs, SimilarityMatrix matrix,
			RatingSystem ratingSytem) {

		if ((itemId == null) || (!logs.getAvgRowsRatings().containsKey(itemId))) {
			return defaultItemScoring(itemId, userId, logs, ratingSytem.getMeansRating());
		}

		if (!logs.getAvgColumnsRatings().containsKey(userId)) {
			return defaultItemScoring(itemId, userId, logs, ratingSytem.getMeansRating());
		}
		// get list of items rated for the user
		SparseVector column = logs.getColumn(userId);
		// get the ratings for this item

		float result;

		float totalSimilaritySum = 0;
		float totalRatingSimSum = 0;
		float currentSim = 0;

		// we try to perform the score using the similarities of the rated items
		// of the user
		Map<String, Float> ratingList = column.getListObjects();
		for (String currentRow : ratingList.keySet()) {
			currentSim = similarity.getSimilarityFromMatrix(userId, currentRow, matrix);
			totalSimilaritySum += Math.abs(currentSim);
			totalRatingSimSum += (currentSim) * (ratingList.get(currentRow) - logs.getAvgRow(currentRow));
		}

		if (totalSimilaritySum != 0) { // scoring OK : we succeed in connecting
										// the item with items in the user's
										// Profile

			result = logs.getAvgRow(itemId) + totalRatingSimSum / totalSimilaritySum;
			if (result > ratingSytem.getMaxRating()) {
				result = ratingSytem.getMaxRating();
			} else if (result < ratingSytem.getMinRating()) {
				result = ratingSytem.getMinRating();
			}
		}
		// intelligent scoring impossible : no connexion between the item and
		// the items in the user Profile
		else {
			result = defaultItemScoring(itemId, userId, logs, ratingSytem.getMeansRating());
		}
		return result;
	}

	/**
	 * Like {@link Scoring#score(String, String, Universe)}, scores an item for an user using 
	 * the item mean rating from a source.
	 * 
	 * Uses a {@link DualBaseOfSparseVector} representation of users logs to optimize the 
	 * execution time.
	 * 
	 * Universe requirements : 
	 *  - a source corresponding to users logs 
	 *  - a similarity matrix of items
	 * 
	 * @param itemId
	 *            the item to score
	 * @param userId
	 *            the id of the user
	 * @param universe
	 *            the current universe
	 * 
	 * @return a float between universe.min_rating and universe.max_rating
	 */
	public float scoreCore(String itemId, String userId, DualBaseOfSparseVector learn, Source logs, SimilarityMatrix matrix,
			RatingSystem ratingSystem) {

		if ((itemId == null) || (!logs.getAvgRowsRatings().containsKey(itemId))) {
			return defaultItemScoring(itemId, userId, logs, ratingSystem.getMeansRating());
		}

		if (!logs.getAvgColumnsRatings().containsKey(userId)) {
			return defaultItemScoring(itemId, userId, logs, ratingSystem.getMeansRating());
		}

		float result;

		float totalSimilaritySum = 0;
		float totalRatingSimSum = 0;
		float currentSim = 0;

		// we try to perform the score using the similarities of the rated items
		// of the user
		int uid = learn.getUserInternalKey(userId);
		int[] it = learn.getUser(uid).getAttributes();
		int j = 0;
		for (int i : it) {
			String currentRow = learn.getItemExternalKey(i);
			currentSim = similarity.getSimilarityFromMatrix(itemId,
					currentRow, matrix);
			totalSimilaritySum += Math.abs(currentSim);
			totalRatingSimSum += (currentSim)
					* (learn.getUser(uid).getValueIndex(j) - logs
							.getAvgRow(currentRow));
			j++;
		}

		if (totalSimilaritySum != 0) { // scoring OK : we succeed in connecting
										// the item with items in the user's
										// Profile

			result = logs.getAvgRow(itemId) + totalRatingSimSum / totalSimilaritySum;
			if (result > ratingSystem.getMaxRating()) {
				result = ratingSystem.getMaxRating();
			} else if (result < ratingSystem.getMinRating()) {
				result = ratingSystem.getMinRating();
			}
		}
		// intelligent scoring impossible : no connexion between the item and
		// the items in the user Profile
		else {
			result = defaultItemScoring(itemId, userId, logs, ratingSystem.getMeansRating());
		}
		return result;
	}

	/**
	 * Scores an item using a catalog of items and the user preferences.
	 * 
	 * Universe requirements: 
	 *  - a source corresponding to users preferences 
	 *  - a source corresponding to the items catalog
	 * 
	 * @param itemId
	 *            the item to score
	 * @param userId
	 *            the user id
	 * @param universe
	 *            the current universe
	 * 
	 * @return an unbounded float.
	 */
	public float scoreUsingPreferences(String userId, String itemId, Source preferences, Source catalog) {

		Map<String, Float> ratedList = preferences.getColumn(userId)
				.getListObjects();

		return scoreUsingExplicitPreferences(itemId, ratedList, catalog);
	}

	/**
	 * Scores an item using a catalog and an explicit map of preferences.
	 * 
	 * Universe requirements:
	 *  - a source corresponding to the items catalog
	 * 
	 * @param itemId
	 * 			  the item to score
	 * @param preferences
	 * 			  an explicit map of preferences
	 * @param universe
	 * 			  the current universe
	 * 
	 * @return an unbounded float.
	 */
	public float scoreUsingExplicitPreferences(String itemId, Map<String, Float> preferences, Source catalog) {
		Map<String, Float> weightList = catalog.getColumn(itemId).getListObjects();
		float sumRating = 0;
		for (String current : weightList.keySet()) {
			Float rating = preferences.get(current);
			Float weight = weightList.get(current);
			if ((rating != null) && (weight > 0)) {
				sumRating += rating * weight;
			}
		}
		return sumRating;
	}

	/**
	 * Default rating prediction method: (avg(user)+avg(item))/2.
	 * 
	 * Universe requirement:
	 *  - a source corresponding to users logs
	 * 
	 * @param item
	 *            The SparseVector of the item
	 * @param user
	 *            The SparseVector of the user
	 *            
	 * @return a float between universe.min_rating and universe.max_rating
	 */
	private float defaultItemScoring(String itemId, String userId, Source logs,
			float meansRating) {

		nbDefaultScoringCalls++;

		if (userId != null && itemId != null) {
			float avg_rating_column = meansRating;
			Float avg_rating_column_tmp = logs.getAvgColumn(userId);
			if (avg_rating_column_tmp != null) {
				avg_rating_column = avg_rating_column_tmp;
			}

			float avg_rating_row = meansRating;
			Float avg_rating_row_tmp = logs.getAvgRow(itemId);
			if (avg_rating_row_tmp != null) {
				avg_rating_row = avg_rating_row_tmp;
			}

			return (avg_rating_column + avg_rating_row) / 2;
		}

		if (userId != null) {
			float avg_rating_column = meansRating;
			Float avg_rating_column_tmp = logs.getAvgColumn(userId);
			if (avg_rating_column_tmp != null) {
				avg_rating_column = avg_rating_column_tmp;
			}
			return avg_rating_column;
		}

		if (itemId != null) {
			float avg_rating_row = meansRating;
			Float avg_rating_row_tmp = logs.getAvgRow(itemId);
			if (avg_rating_row_tmp != null) {
				avg_rating_row = avg_rating_row_tmp;
			}
			return avg_rating_row;
		}

		return meansRating;
	}

	/**
	 * Reset the number of default item scoring calls.
	 */
	public void resetNbDefaultItemScoringCalls() {
		nbDefaultScoringCalls = 0;
	}

	/**
	 * Return the number of default item scoring calls since last reset.
	 * 
	 * @return the number of default item scoring calls.
	 */
	public int getNbDefaultItemScoringCalls() {
		return nbDefaultScoringCalls;
	}
}