package com.orange.flexoffice.business.gatewayapi.recommendation;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.orange.flexoffice.business.gatewayapi.DefaultSettings;
import com.orange.flexoffice.business.gatewayapi.matrix.SimilarityMatrix;
import com.orange.flexoffice.dao.gatewayapi.model.RatingSystem;
import com.orange.flexoffice.dao.gatewayapi.model.support.SparseVector;

/**
 * Similarity methods (between items, between descriptors, between users)
 * 
 * @author Damien Hembert
 * 
 */
@Component
public class Similarity {

	private SimilarityMethods similarityMethod = DefaultSettings.DEFAULT_SIMILARITY_METHOD;

	/**
	 * Select the method to use to compute similarities (default is
	 * SIMILARITY_WEIGHTED_JACCARD)
	 * 
	 * @param similarityMethod
	 *            SIMILARITY_JACCARD or SIMILARITY_WEIGHTED_JACCARD
	 */
	public void setSimilarityMethod(SimilarityMethods similarityMethod) {
		this.similarityMethod = similarityMethod;
	}

	/**
	 * Item-item similarity using the selected similarity method (see
	 * setSelectedSimilarityMethod)
	 * 
	 * @param item1
	 * @param item2
	 * @return Similarity (float between 0 and 1)
	 */

	public float getSimilarityOnTheFly(SparseVector sv1, SparseVector sv2, RatingSystem ratingSystem) {
		return getSimilarity(sv1, sv2, similarityMethod, ratingSystem);
	}

	/**
	 * Return similarity between two items from in-memory loaded similarity
	 * matrix If there is no similarity in the matrix, then similarity is
	 * supposed to be 0
	 * 
	 * @param objectId
	 *            The first item identifier
	 * @param similarId
	 *            The second item identifier
	 * @return The similarity between <i>item</i> and <i>similar</i>
	 */
	public float getSimilarityFromMatrix(String objectId, String similarId, SimilarityMatrix matrix) {

		if (matrix.getSimilarities().containsKey(objectId)) {
			if (matrix.getSimilarities().get(objectId).containsKey(similarId)) {
				return matrix.getSimilarities().get(objectId).get(similarId);
			} else {
				return 0f;
			}
		} else {
			return 0f;
		}
	}

	/**
	 * Item-item similarity using the similarity method in parameter
	 * 
	 * @param item1
	 * @param item2
	 * @param similarityMethod
	 *            (SIMILARITY_JACCARD or SIMILARITY_WEIGHTED_JACCARD)
	 * @return Similarity (float between 0 and 1)
	 */
	private float getSimilarity(SparseVector sv1, SparseVector sv2, SimilarityMethods similarityMethod, RatingSystem ratingSystem) {
		switch (similarityMethod) {
		case JACCARD:
			return getSimilarityJaccard(sv1, sv2);
		case WEIGHTED_JACCARD:
			return getSimilarityWeightedJaccard(sv1, sv2);
		case EXTENDED_PEARSON:
			return getSimilarityExtendedPearson(sv1, sv2, ratingSystem);
		case MIX:
			return getSimilarityMix(sv1, sv2, ratingSystem);
		case PEARSON:
			return getSimilarityPearson(sv1, sv2, ratingSystem);
		case WPEARSON:
			return getSimilarityWPearson(sv1, sv2, ratingSystem);
		}
		return 0f;
	}

	/**
	 * get similarity using the JACCARD method
	 * 
	 * @param SparseVector
	 *            sv1
	 * @param SparseVector
	 *            sv2
	 * @return Similarity (float between 0 and 1)
	 */
	private float getSimilarityJaccard(SparseVector sv1, SparseVector sv2) {
		float result = 0f;

		// Shared ratings divided by total ratings

		int sharedValues = 0;
		int totalValues = 0;

		totalValues = sv1.getListObjects().size();

		Map<String, Float> list1 = sv1.getListObjects();
		Map<String, Float> list2 = sv2.getListObjects();

		for (String key : list2.keySet()) {
			if (!list1.containsKey(key)) {
				totalValues++;
			} else {
				sharedValues++;
			}
		}

		result = (float) sharedValues / (float) totalValues;

		return result;
	}

	/**
	 * Item-item similarity using the WEIGHTED JACCARD method <blockquote>
	 * P(d,i) = weight of the desc <i>d</i> for the item <i>i</i> </blockquote>
	 * <blockquote> Sim(item1,item2) = Sum(min(P(d,item1),P(d,item2))) /
	 * Sum(max(P(d,item1),P(d,item2))) </blockquote>
	 * 
	 * @param item1
	 * @param item2
	 * @return Similarity (float between 0 and 1)
	 */
	private float getSimilarityWeightedJaccard(SparseVector sv1, SparseVector sv2) {
		float result = 0f;

		Map<String, Float> list1 = sv1.getListObjects();
		Map<String, Float> list2 = sv2.getListObjects();

		String[] keys = new String[list1.size() + list2.size()];
		int nbKey = 0;

		for (String key : list1.keySet()) {
			keys[nbKey] = key;
			nbKey++;
		}
		for (String key : list2.keySet()) {
			if (!list1.containsKey(key)) {
				keys[nbKey] = key;
				nbKey++;
			}
		}

		float topSum = 0;
		float bottomSum = 0;
		float p1, p2;

		for (int i = 0; i < nbKey; i++) {
			if (list1.containsKey(keys[i])) {
				p1 = list1.get(keys[i]);
			} else {
				p1 = 0f;
			}
			if (list2.containsKey(keys[i])) {
				p2 = list2.get(keys[i]);
			} else {
				p2 = 0;
			}
			if (p1 > p2) {
				topSum += p2;
				bottomSum += p1;
			} else {
				topSum += p1;
				bottomSum += p2;
			}
		}
		result = topSum / bottomSum;

		return result;
	}

	/**
	 * Return the Extended Pearson similarity between item1 and item2
	 * 
	 * @param item1
	 * @param item2
	 * @param ratingAttribId
	 *            : ID of the virtual attribute used for users'ratings
	 * @return
	 */
	private float getSimilarityExtendedPearson(SparseVector sv1, SparseVector sv2, RatingSystem ratingSystem) {

		float result = 0f;

		Map<String, Float> listValue1 = sv1.getListObjects();
		Map<String, Float> listValue2 = sv2.getListObjects();

		String[] keys = new String[listValue1.size() + listValue2.size()];

		int nbKeys = 0;

		float meanRatingItem1 = 0f;
		int nbRatingItem1 = 0;

		for (String key : listValue1.keySet()) {
			keys[nbKeys] = key;
			meanRatingItem1 += listValue1.get(key);
			nbRatingItem1++;
			nbKeys++;
		}

		if (nbKeys > 0) {
			meanRatingItem1 /= nbRatingItem1;
		} else {
			meanRatingItem1 = ratingSystem.getMeansRating();
		}
		
		float meanRatingItem2 = 0f;
		int nbRatingItem2 = 0;

		for (String key : listValue2.keySet()) {
			meanRatingItem2 += listValue2.get(key);
			nbRatingItem2++;
			if (!listValue1.containsKey(key)) {
				keys[nbKeys] = key;
				nbKeys++;
			}
		}

		if (nbRatingItem2 > 0) {
			meanRatingItem2 /= nbRatingItem2;
		} else {
			meanRatingItem2 = ratingSystem.getMeansRating();
		}

		float i1part = 0;
		float i2part = 0;

		float topSum = 0;
		float bottomSum1 = 0;
		float bottomSum2 = 0;
		float u1 = 0;
		float u2 = 0;

		for (int i = 0; i < nbKeys; i++) {
			if (listValue1.containsKey(keys[i])) {
				u1 = listValue1.get(keys[i]);
			} else {
				u1 = meanRatingItem1;
			}

			if (listValue2.containsKey(keys[i])) {
				u2 = listValue2.get(keys[i]);
			} else {
				u2 = meanRatingItem2;
			}

			i1part = u1 - meanRatingItem1;
			i2part = u2 - meanRatingItem2;

			topSum += i1part * i2part;
			bottomSum1 += i1part * i1part;
			bottomSum2 += i2part * i2part;
		}

		if (bottomSum1 != 0 && bottomSum2 != 0) {
			result = topSum / ((float) (Math.sqrt(bottomSum1 * bottomSum2)));
		}
		
		return result;
	}

	/**
	 * Return the "classical" Pearson similarity between item1 and item2 BE
	 * CARREFUL : this similarity has a strong bias when item1 and item2 share
	 * few common descriptors
	 * 
	 * @param item1
	 * @param item2
	 * @param ratingAttribId
	 *            : ID of the virtual attribute used for users'ratings
	 * @return
	 */
	private float getSimilarityPearson(SparseVector sv1, SparseVector sv2, RatingSystem ratingSystem) {
		float result = 0f;

		Map<String, Float> listValue1 = sv1.getListObjects();
		Map<String, Float> listValue2 = sv2.getListObjects();

		String[] keys = new String[listValue1.size() + listValue2.size()];

		int nbKeys = 0;

		float meanRatingItem1 = 0f;
		int nbRatingItem1 = 0;

		for (String key : listValue1.keySet()) {
			keys[nbKeys] = key;
			meanRatingItem1 += listValue1.get(key);
			nbRatingItem1++;
			nbKeys++;
		}

		if (nbKeys > 0) {
			meanRatingItem1 /= nbRatingItem1;
		} else {
			meanRatingItem1 = ratingSystem.getMeansRating();
		}

		float meanRatingItem2 = 0f;
		int nbRatingItem2 = 0;

		for (String key : listValue2.keySet()) {
			meanRatingItem2 += listValue2.get(key);
			nbRatingItem2++;
			if (!listValue1.containsKey(key)) {
				keys[nbKeys] = key;
				nbKeys++;
			}
		}

		if (nbRatingItem2 > 0) {
			meanRatingItem2 /= nbRatingItem2;
		} else {
			meanRatingItem2 = ratingSystem.getMeansRating();
		}

		float i1part = 0;
		float i2part = 0;

		float topSum = 0;
		float bottomSum1 = 0;
		float bottomSum2 = 0;
		float u1 = 0;
		float u2 = 0;

		for (int i = 0; i < nbKeys; i++) {

			boolean vectorsHaveCommonKey = true;

			if (listValue1.containsKey(keys[i])) {
				u1 = listValue1.get(keys[i]);
			} else {
				u1 = meanRatingItem1;
				vectorsHaveCommonKey = false;
			}
			if (listValue2.containsKey(keys[i])) {
				u2 = listValue2.get(keys[i]);
			} else {
				u2 = meanRatingItem2;
				vectorsHaveCommonKey = false;
			}

			i1part = u1 - meanRatingItem1;
			i2part = u2 - meanRatingItem2;

			topSum += i1part * i2part;

			if (vectorsHaveCommonKey) {
				bottomSum1 += i1part * i1part;
				bottomSum2 += i2part * i2part;
			}
		}

		if (bottomSum1 != 0 && bottomSum2 != 0) {
			result = topSum / ((float) (Math.sqrt(bottomSum1 * bottomSum2)));
		}
		return result;
	}

	/**
	 * Return the Mix similarity between 2 Sparse Vectors
	 * 
	 * @param SparseVector
	 *            1
	 * @param SparseVector
	 *            2
	 * @return the similarity between 0 and 1
	 */
	private float getSimilarityMix(SparseVector sv1, SparseVector sv2, RatingSystem ratingSystem) {
		float result = 0f;

		float pearson = getSimilarityExtendedPearson(sv1, sv2, ratingSystem);
		float jaccard = getSimilarityJaccard(sv1, sv2);

		result = jaccard * (1 + pearson) / 2;

		return result;
	}

	/**
	 * Return the weightedPearson similarity between 2 Sparse Vectors
	 * 
	 * @param SparseVector
	 *            sv1
	 * @param SparseVector
	 *            sv2
	 * @return similarity value
	 */

	private float getSimilarityWPearson(SparseVector sv1, SparseVector sv2, RatingSystem ratingSystem) {
		float result = 0f;

		float pearson = getSimilarityPearson(sv1, sv2, ratingSystem);
		float jaccard = getSimilarityJaccard(sv1, sv2);

		result = jaccard * pearson;

		return result;
	}

}