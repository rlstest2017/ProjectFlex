package com.orange.flexoffice.business.gatewayapi.recommendation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.orange.flexoffice.business.gatewayapi.DefaultSettings;
import com.orange.flexoffice.business.gatewayapi.matrix.SimilarityMatrix;
import com.orange.flexoffice.business.gatewayapi.source.Source;
import com.orange.flexoffice.dao.gatewayapi.model.RatingSystem;
import com.orange.flexoffice.dao.gatewayapi.model.ReperioElement;
import com.orange.flexoffice.dao.gatewayapi.model.support.SortedDoubleList;
import com.orange.flexoffice.dao.gatewayapi.model.support.SparseVector;

/**
 * <strong>Main class of the Reperio Lite Recommendation Engine.</strong><br>
 * <br>
 * 
 * The first thing to do is to select the Database to use (with the setDB
 * method) Next, if you don't want to use the default Profile, you have to enter
 * the userId (with the setProfile method).
 * 
 * @author Damien Hembert
 * 
 */
@Component
public class Recommendation {
	private final Logger logger = Logger.getLogger(Recommendation.class);

	@Autowired
	private Similarity similarity;
	@Autowired
	private Scoring scoring;

	public Similarity getSimilarity() {
		return similarity;
	}

	public void setSimilarity(Similarity similarity) {
		this.similarity = similarity;
	}

	public Scoring getScoring() {
		return scoring;
	}

	public void setScoring(Scoring scoring) {
		this.scoring = scoring;
	}

	/**
	 * Get the most similar items to the chosen item from an item source. Used
	 * to provide item-to-item recommendations from the Catalog
	 * 
	 * @param id
	 *            chosen item identifier
	 * @param resultSize
	 *            number of similar items
	 * @param source
	 *            the item source
	 * @return ids of the top "resultSize" similar items
	 * 
	 *         TODO : � modifier, il y a une ambiguit� : Source sert � la fois �
	 *         d�signer la source des items, alors que la source des items est
	 *         TOUJOURS le Catalog et le mode de similarit� utilis�e, qui peut
	 *         dans ce cas �tre Collaboratif (logs-row) ou th�matique
	 *         (Catalog-row)
	 * 
	 *         Il faudrait :
	 * 
	 *         - toujours utiliser le Catalog pour la recherche ou l'extraction
	 *         d'items
	 * 
	 *         - recommer Source : SimilarMatrixMode qui normalement peut avoir
	 *         les valeurs suivantes :
	 * 
	 *         si on se restreint aux recos user-to-items actuelles, les modes
	 *         possibles sont Logs-Rows (collaboratif item-item, donc les
	 *         "lignes" des logs sont compar�es) Catalog-Rows (th�matique item-
	 *         item, donc les "lignes du catalogue sont compar�s)
	 * 
	 *         si on ne se restreint pas, les modes de recos possibles sont
	 *         pr�cis�e en annexe 1 de la doc.
	 * @throws WrongSourceTableFormatException
	 * 
	 */
	public String[] recommendGeneric(String id, int resultSize, Source source, SimilarityMatrix matrix) {
		matrix.loadSimilarities(false);
		if (matrix.isRowsMatrix()) {
			return bestSimilarFromListOfIdsGeneric(id, resultSize, source.getRowIdsList(), source, matrix);
		} else {
			return bestSimilarFromListOfIdsGeneric(id, resultSize, source.getColumnIdsList(), source, matrix);
		}
	}

	/**
	 * Get the most similar items to the chosen item from an array of item
	 * identifiers.
	 * 
	 * @param currentId
	 *            chosen item identifier
	 * @param resultSize
	 *            number of similar items
	 * @param ids
	 *            items to compare
	 * @param source
	 *            the item source
	 * @return ids of the top "resultSize" similar items
	 * @throws WrongSourceTableFormatException
	 */
	private String[] bestSimilarFromListOfIdsGeneric(String currentId, int resultSize, String[] ids, Source source,
			SimilarityMatrix matrix) {

		// init list results
		SortedDoubleList<String> sdl = new SortedDoubleList<String>(resultSize);

		// SparseVector currentItem;
		float currentSim;

		for (String id : ids) {
			if (!id.equals(currentId)) {
				currentSim = similarity.getSimilarityFromMatrix(currentId, id, matrix);
				sdl.add(id, currentSim);
			}
		}

		String[] result = new String[sdl.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = sdl.getIdAt(i);
		}

		return result;

	}

	/**
	 * Recommend items from a selected subset of the item source using the
	 * selected scoring method (see Scoring.setScoringMethod).
	 * 
	 * @param itemIds
	 *            identifiers of the items to score
	 * @param resultSize
	 *            number of recommended items
	 * @param source
	 *            the item source
	 * @return array of items identifier with the highest scores
	 */
	public String[] recommendItemItemGeneric(String[] itemIds, int resultSize, Source source, RatingSystem ratingSystem) {

		String[] listSource = source.getRowIdsList();

		SortedDoubleList<String> sdl = new SortedDoubleList<String>(resultSize);

		SparseVector currentItem;

		for (String id : itemIds) {

			float currentSim;
			// init sparseVector of itemId
			SparseVector sourceItem = source.getRow(id);

			for (String ids : listSource) {
				if (!ids.equals(id)) {
					currentItem = source.getRow(ids);
					currentSim = similarity.getSimilarityOnTheFly(sourceItem, currentItem, ratingSystem);
					sdl.add(ids, currentSim);
				}
			}
		}

		String[] result = new String[sdl.size()];
		for (int i = 0; i < result.length; i++) { 
			result[i] = sdl.getIdAt(i);
		}

		return result;
	}

	/**
	 * Recommend items from a selected subset of the item source for the
	 * selected user using the selected scoring method (see
	 * Scoring.setScoringMethod).
	 * 
	 * @param rowIds
	 *            identifiers of the items to score
	 * @param resultSize
	 *            number of recommended items
	 * @return array of items identifier with the highest scores
	 */
	public List<ReperioElement> recommendFromListOfRowsGeneric(String[] rowIds, String columnId, int resultSize, Source source,
			SimilarityMatrix matrix, RatingSystem ratingSystem) {

		ArrayList<ReperioElement> temp = new ArrayList<ReperioElement>(rowIds.length);
		

		float currentScore;

		for (String id : rowIds) {
			currentScore = scoring.score(id, columnId, source, matrix, ratingSystem);
			temp.add(new ReperioElement(id,currentScore));
		}

		Collections.sort(temp);
		int size = (temp.size()>=resultSize?resultSize:temp.size());
		
		return temp.subList(0, size);
	}
	
	public List<ReperioElement> recommendFromListOfColumnsGeneric(String[] columnIds, String rowId, int resultSize, Source source,
			SimilarityMatrix matrix, RatingSystem ratingSystem) {
		
		ArrayList<ReperioElement> temp = new ArrayList<ReperioElement>();
		

		float currentScore;

		for (String id : columnIds) {
			currentScore = scoring.score(rowId, id, source, matrix, ratingSystem);
			temp.add(new ReperioElement(id,currentScore));
		}

		Collections.sort(temp);
		int size = (temp.size()>=resultSize?resultSize:temp.size());

		return temp.subList(0, size);
	}

	/**
	 * Recommend items from a selected subset of the item source for the
	 * selected user using the selected scoring method (see
	 * Scoring.setScoringMethod).
	 * 
	 * @param rowIds
	 *            identifiers of the items to score
	 * @param resultSize
	 *            number of recommended items
	 * @return array of items identifier with the highest scores
	 * @throws WrongSourceTableFormatException
	 */
	public String[] recommendFromListOfRowsGenericCore(String[] rowIds, String columnId, int resultSize, Source source,
			SimilarityMatrix matrix, RatingSystem ratingSystem) {

		SortedDoubleList<String> sdl = new SortedDoubleList<String>(resultSize);

		float currentScore;

		for (String id : rowIds) {
			currentScore = scoring.scoreCore(id, columnId, source.getBase(), source, matrix, ratingSystem);
			sdl.add(id, currentScore);
		}

		String[] result = new String[sdl.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = sdl.getIdAt(i);
		}
		return result;
	}

	/**
	 * Recommend items from a selected subset of an item source for the selected
	 * user using the selected scoring method (see Scoring.setScoringMethod).
	 * 
	 * @param itemIds
	 *            identifiers of the items to score
	 * @param source
	 *            the item source
	 * @return array of items identifier with the highest scores
	 * @throws WrongSourceTableFormatException
	 */
//	public static String[] recommendUserItemFromListOfItemsGeneric(String[] itemIds, SimilarityMatrix matrix) {
//
//		initLists(itemIds.length);
//
//		float currentScore;
//
//		for (String id : itemIds) {
//			currentScore = Recommendation.scoreItemForCurrentUserUsingSelectedScoringMethod(id, matrix);
//			addArrayToSortByDecreasingValue(id, currentScore);
//		}
//
//		String[] result = new String[sdl.size()];
//		for (int i = 0; i < result.length; i++)
//			result[i] = sdl.getIdAt(i);
//
//		return result;
//	}

	public List<ReperioElement> recoRowsForColumn(boolean diversity, int numberOfSeeds, int numberOfReco, String columnId, Source source,
			SimilarityMatrix matrix, RatingSystem ratingSystem) {

		// matrix.loadSimilarities(false);
		HashSet<String> candidateList = new HashSet<String>();
		Float meanColumn = source.getAvgColumn(columnId);
		if (logger.isInfoEnabled()) {
			logger.info("mean:"+meanColumn);
			logger.info("COLUMN:"+columnId);
		}
		if (meanColumn == null) {
			if (logger.isInfoEnabled()) {
				logger.info("COLUMN ID " + columnId + " NOT FOUND. RETURNING " + numberOfReco + " BEST ROWS BY AVERAGE");
			}
			return source.getBestRowsByAvg(numberOfReco);
		}
		SparseVector column = source.getColumn(columnId);
		ArrayList<String> ratedRows = new ArrayList<String>();

		Iterator<Entry<String, Float>> it = column.getListObjects().entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Float> pairs = it.next();
			if ((Float) pairs.getValue() >= meanColumn) {
				ratedRows.add((String) pairs.getKey());
			}
		}

		List<String> seeds = null;
		if (diversity) {
			Collections.shuffle(ratedRows);
		}

		seeds = new ArrayList<String>(ratedRows.subList(0, (numberOfSeeds > ratedRows.size() ? ratedRows.size() : numberOfSeeds)));
		
		for (String seed : seeds) {
			if (logger.isInfoEnabled()){
				logger.info("SEED:"+seed);
			}
			String[] ratings = matrix.getSortedKNN(seed, DefaultSettings.DEFAULT_NB_CANDIDATES_BY_SEEDS);

			if (ratings != null) {
				for (String rowID : ratings) {
					if (!column.getListObjects().containsKey(rowID)) {
						candidateList.add(rowID);
						if (logger.isInfoEnabled()) {
							logger.info("CANDIDATE:"+rowID);
						}
					}
				}
			}

		}
		//Display.displaySystemDateMsg("SEEDS : "+sb.toString());
		String[] listToScore = candidateList.toArray(new String[candidateList.size()]);
		List<ReperioElement> finalRowList = recommendFromListOfRowsGeneric(listToScore, columnId, numberOfReco, source, matrix, ratingSystem);
		return finalRowList;

	}
	public List<ReperioElement> recoColumnsForRow(boolean diversity, int numberOfSeeds, int numberOfReco, String rowId, Source source,
			SimilarityMatrix matrix, RatingSystem ratingSystem) {

		// matrix.loadSimilarities(false);
		HashSet<String> candidateList = new HashSet<String>();
		Float meanRow = source.getAvgRow(rowId);
		if (logger.isInfoEnabled()) {
			logger.info("mean:"+meanRow);
			logger.info("ROW:"+rowId);
		}
		if (meanRow == null) {
			if (logger.isInfoEnabled()) {
				logger.info("ROW ID " + rowId + " NOT FOUND. RETURNING " + numberOfReco + " BEST COLUMNS BY AVERAGE");
			}
			return source.getBestColumnsByAvg(numberOfReco);
		}
		SparseVector row = source.getRow(rowId);
		ArrayList<String> ratedColumns = new ArrayList<String>();

		Iterator<Entry<String, Float>> it = row.getListObjects().entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Float> pairs = it.next();
			if ((Float) pairs.getValue() >= meanRow) {
				ratedColumns.add((String) pairs.getKey());
			}
		}

		List<String> seeds = null;
		if (diversity) {
			Collections.shuffle(ratedColumns);
		}

		seeds = new ArrayList<String>(ratedColumns.subList(0, (numberOfSeeds > ratedColumns.size() ? ratedColumns.size() : numberOfSeeds)));
		
		for (String seed : seeds) {
			if (logger.isInfoEnabled()) {
				logger.info("SEED:"+seed);
			}
			String[] ratings = matrix.getSortedKNN(seed, DefaultSettings.DEFAULT_NB_CANDIDATES_BY_SEEDS);

			if (ratings != null) {
				for (String columnID : ratings) {
					if (!row.getListObjects().containsKey(columnID)) {
						candidateList.add(columnID);
						if (logger.isInfoEnabled()) {
							logger.info("CANDIDATE:"+columnID);
						}
					}
				}
			}

		}
		//Display.displaySystemDateMsg("SEEDS : "+sb.toString());
		String[] listToScore = candidateList.toArray(new String[candidateList.size()]);
		List<ReperioElement> finalRowList = recommendFromListOfColumnsGeneric(listToScore, rowId, numberOfReco, source, matrix, ratingSystem);
		return finalRowList;

	}

//	public String[] recoRowsForColumnCore(boolean diversity, int numberOfSeeds, int numberOfReco, String columnId, Source source,
//			SimilarityMatrix matrix, RatingSystem ratingSystem) {
//
//		source.loadBase(true, true);
//		HashSet<String> candidateList = new HashSet<String>();
//		Float meanColumn = source.getAvgColumn(columnId);
//
//		if (meanColumn == null) {
//			if (logger.isInfoEnabled()) {
//				logger.info("COLUMN ID " + columnId + " NOT FOUND. RETURNING " + numberOfReco + " BEST ROWS BY AVERAGE");
//			}
//			List<ReperioElement> arc = source.getBestRowsByAvg(numberOfReco);
//			return Reperio.listOfCouplesToArray(arc);
//		}
//		SparseVectorTabular column = source.getBase().getUser(source.getBase().getUserInternalKey(columnId));
//		ArrayList<String> ratedRows = new ArrayList<String>();
//		ArrayList<String> columnL = new ArrayList<String>();
//
//		for (int i = 0; i < column.getAttributes().length; i++) {
//			String j = source.getBase().getItemExternalKey(column.getAttributeIndex(i));
//			if (column.getValueIndex(i) >= meanColumn) {
//				ratedRows.add(j);
//			}
//			columnL.add(j);
//		}
//
//		List<String> seeds = null;
//		if (diversity) {
//			Collections.shuffle(ratedRows);
//		}
//
//		seeds = new ArrayList<String>(ratedRows.subList(0, (numberOfSeeds > ratedRows.size() ? ratedRows.size() : numberOfSeeds)));
//
//		for (String seed : seeds) {
//			String[] ratings = matrix.getSortedKNN(seed, DefaultSettings.DEFAULT_NB_CANDIDATES_BY_SEEDS);
//
//			if (ratings != null) {
//				for (String rowID : ratings) {
//					if (!columnL.contains(rowID)) {
//						candidateList.add(rowID);
//					}
//				}
//			}
//
//		}
//		String[] listToScore = candidateList.toArray(new String[candidateList.size()]);
//		String[] finalRowList = recommendFromListOfRowsGenericCore(listToScore, columnId, numberOfReco, source, matrix, ratingSystem);
//		return finalRowList;
//
//	}
	
	public List<ReperioElement> recoUsingPreferences(int numberOfRecos, String columnId, Source catalog, Source descriptors, RatingSystem ratingSystem){
		ArrayList<ReperioElement> res = new ArrayList<ReperioElement>();
		String[] listItems = catalog.getColumnIdsListRowConditions(descriptors.getColumn(columnId));
		for (String string : listItems) {
			float score = scoring.scoreUsingPreferences(columnId, string, descriptors, catalog);
			if (logger.isInfoEnabled()) {
				logger.info("Scoring:"+string+"=>"+score);
			}
			if (score >= ratingSystem.getMeansRating()) {
				res.add(new ReperioElement(string, score));
			}
		}
		Collections.shuffle(res);
		Collections.sort(res);
		if(numberOfRecos<res.size()) {
			return new ArrayList<ReperioElement>(res.subList(0, numberOfRecos));
		} else {
			return res;
		}
	}
}
