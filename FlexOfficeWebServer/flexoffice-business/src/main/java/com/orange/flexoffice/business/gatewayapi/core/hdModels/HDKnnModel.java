package com.orange.flexoffice.business.gatewayapi.core.hdModels;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.orange.flexoffice.business.gatewayapi.core.dataAccess.BaseOfSparseVector;
import com.orange.flexoffice.business.gatewayapi.core.dataAccess.KeyDictionary;
import com.orange.flexoffice.business.gatewayapi.core.dataAccess.SparseVectorSorted;
import com.orange.flexoffice.business.gatewayapi.core.dataAccess.SparseVectorTabular;
import com.orange.flexoffice.business.gatewayapi.core.mains.Parameters;
import com.orange.flexoffice.business.gatewayapi.matrix.SimilarityMatrix;

import com.orange.flexoffice.dao.gatewayapi.model.Similarity;
import com.orange.flexoffice.dao.gatewayapi.repository.similarity.SimilarityRepository;

/**
 * 
 * HDKnnModel : Nearest Neighbor Model : extends HDModel
 * 
 * @author Laurent Candillier
 * 
 * @version 7.0 (07/01/2009)
 * 
 * 
 *          ENGLISH VERSION
 * 
 * 
 *          K Nearest Neighbor model for High Dimensional databases
 * 
 *          Specify the database and the neighborhood size to be considered
 *          during construction, then execute the learn() or
 *          learnFromClustering(clustering) method for its creation, then
 *          specify the dual database with the setDualDataBase(dual) method,
 *          that also handles the ordering of neighbors in ascending order of
 *          their identifiers, so that predictions are faster, before using the
 *          getRecommendation(u,i) function
 * 
 *          In learnFromClustering(clustering), we only consider as potential
 *          neighbors the members of a same cluster, except if the cluster
 *          contains fewer members than the desired number of neighbors
 * 
 *          Uses parameters minRatingScale and maxRatingScale
 * 
 *          Implements save and load methods depending on parameters
 *          engineModelDirectory, the type of vector handled (item or user) and
 *          the considered neighborhood size
 * 
 * 
 *          VERSION FRANCAISE
 * 
 * 
 *          Mod�le K plus proches voisins pour bases de donn�es de Haute
 *          Dimension
 * 
 *          Sp�cifier la base de donn�es et la taille de voisinage � consid�rer
 *          lors de la construction, puis lancer la m�thode learn() ou
 *          learnFromClustering(clustering) pour sa cr�ation, puis sp�cifier la
 *          base de donn�es duale avec la m�thode setDualDataBase(dual), qui
 *          s'occupe aussi d'ordonner les voisins par identifiants croissants
 *          pour acc�l�rer les pr�dictions, avant d'utiliser la m�thode
 *          getRecommendation(u,i)
 * 
 *          Dans learnFromClustering(clustering), on ne consid�re comme voisins
 *          potentiels que les membres d'un m�me cluster, � moins que le cluster
 *          contienne moins de membres que le nombre de voisins d�sir�s pour le
 *          mod�le
 * 
 *          Utilise les param�tres minRatingScale et maxRatingScale
 * 
 *          Impl�mente des proc�dures de sauvegarde et de chargement save() et
 *          load() d�pendant des param�tres engineModelDirectory, du type de
 *          vecteur trait� (item ou user) et de la taille de voisinage
 *          consid�r�e
 * 
 */

public class HDKnnModel extends HDModel {
	private final Logger logger = Logger.getLogger(HDKnnModel.class);
	
	private SimilarityRepository similarityRepository;

	/**
	 * A HDKnnModel is composed of a considered neighborhood size, a
	 * neighborhood matrix, a vector similarity matrix, a table of indexes of
	 * least similar vectors, a table of minimum similarities to the vectors, a
	 * dual database for the recommendations, and a MySQL similarity table
	 */
	private int neighborNumber;
	private int[][] neighborMatrix;
	private float[][] similarityMatrix;
	private int[] minIndex;
	private float[] minSimilarity;
	private BaseOfSparseVector myDualDataBase;

	/**
	 * The constructor stores tha database and associated neighborhood size, and
	 * initializes the parameters
	 * 
	 * @param base
	 *            the associated database
	 * @param K
	 *            the number of neighbors considered per vector
	 */
	public HDKnnModel(BaseOfSparseVector base, int K, SimilarityRepository similarityRepository) {
		super(base);
		if (K < base.getSparseVectorNumber()) {
			neighborNumber = K;
		} else {
			neighborNumber = base.getSparseVectorNumber() - 1;
		}
		neighborMatrix = new int[base.getMaxVectorID() + 1][getNeighborNumber()];
		similarityMatrix = new float[base.getMaxVectorID() + 1][getNeighborNumber()];
		minSimilarity = new float[base.getMaxVectorID() + 1];
		minIndex = new int[base.getMaxVectorID() + 1];
		for (int i = 0; i <= base.getMaxVectorID(); i++) {
			for (int k = 0; k < getNeighborNumber(); k++) {
				neighborMatrix[i][k] = -1;
			}
			minSimilarity[i] = -2;
		}
		
		this.similarityRepository = similarityRepository;
	}

	/**
	 * @return the number of neighbors considered per vector
	 */
	public int getNeighborNumber() {
		return neighborNumber;
	}

	/**
	 * @param i
	 *            a vector identifier
	 * @return the minimum similarity with this vector (among the nearest
	 *         neighbors)
	 */
	public float getMinSimilarity(int i) {
		if (i <= getDataBase().getMaxVectorID()) {
			return minSimilarity[i];
		} else {
			logger.error("HDModels - HDKnnModel - getMinSimilarity(" + i + ")");
			logger.error("  => out of bounds exception : " + "i = " + i + " > " + getDataBase().getMaxVectorID());
			return 0;
		}
	}

	/**
	 * Specify the minimum similarity with this vector (among the nearest
	 * neighbors)
	 * 
	 * @param i
	 *            a vector identifier
	 * @param s
	 *            the minimum similarity
	 */
	public void setMinSimilarity(int i, float s) {
		if (i <= getDataBase().getMaxVectorID()) {
			minSimilarity[i] = s;
		} else {
			logger.error("HDModels - HDKnnModel - setMinSimilarity(" + i + "," + s + ")");
			logger.error("  => out of bounds exception : " + "i = " + i + " > " + getDataBase().getMaxVectorID());
		}
	}

	/**
	 * @param i
	 *            a vector identifier
	 * @return the index of the neighbor having minimum similarity with this
	 *         vector
	 */
	public int getMinIndex(int i) {
		if (i <= getDataBase().getMaxVectorID()) {
			return minIndex[i];
		} else {
			logger.error("HDModels - HDKnnModel - getMinIndex(" + i + ")");
			logger.error("  => out of bounds exception : " + "i = " + i + " > " + getDataBase().getMaxVectorID());
			return 0;
		}
	}

	/**
	 * Specify the index of the neighbor having minimum similarity with this
	 * vector
	 * 
	 * @param i
	 *            a vector identifier
	 * @param k
	 *            the index of the neighbor having minimum similarity with this
	 *            vector
	 */
	public void setMinIndex(int i, int k) {
		if (i <= getDataBase().getMaxVectorID()) {
			minIndex[i] = k;
		} else {
			logger.error("HDModels - HDKnnModel - setMinIndex(" + i + "," + k + ")");
			logger.error("  => out of bounds exception : " + "i = " + i + " > " + getDataBase().getMaxVectorID());
		}
	}

	/**
	 * @param i
	 *            a vector identifier
	 * @param k
	 *            a neighbor index
	 * @return the identifier of the corresponding neighbor
	 */
	public int getNeighbor(int i, int k) {
		if ((i <= getDataBase().getMaxVectorID()) && (k < getNeighborNumber())) {
			if (neighborMatrix[i][k] < 0) {
				logger.error("HDModels - HDKnnModel - getNeighbor(" + i + "," + k + ")");
				logger.error("  => null exception : neighborMatrix[" + i + "][" + k + "] < 0");
				return 0;
			} else {
				return neighborMatrix[i][k];
			}
		} else {
			logger.error("HDModels - HDKnnModel - getNeighbor(" + i + "," + k + ")");
			logger.error("  => out of bounds exception : " + "i = " + i + " > " + getDataBase().getMaxVectorID() + " || k = " + k + " >= "
					+ getNeighborNumber());
			return 0;
		}
	}

	/**
	 * Specify the neighbor of a vector
	 * 
	 * @param i
	 *            a vector identifier
	 * @param k
	 *            a neighbor index
	 * @param j
	 *            the neighbor identifier
	 */
	public void setNeighbor(int i, int k, int j) {
		if ((i <= getDataBase().getMaxVectorID()) && (k < getNeighborNumber())) {
			neighborMatrix[i][k] = j;
		} else {
			logger.error("HDModels - HDKnnModel - setNeighbor(" + i + "," + k + "," + j + ")");
			logger.error("  => out of bounds exception : " + "i = " + i + " > " + getDataBase().getMaxVectorID() + " || k = " + k + " >= "
					+ getNeighborNumber());
		}
	}

	/**
	 * @param i
	 *            a vector identifier
	 * @param k
	 *            a neighbor index
	 * @return the similarity between the corresponding vectors
	 */
	public float getSimilarity(int i, int k) {
		if ((i <= getDataBase().getMaxVectorID()) && (k < getNeighborNumber())) {
			if (neighborMatrix[i][k] < 0) {
				logger.error("HDModels - HDKnnModel - getSimilarity(" + i + "," + k + ")");
				logger.error("  => null exception : neighborMatrix[" + i + "][" + k + "] < 0");
				return 0;
			} else {
				return similarityMatrix[i][k];
			}
		} else {
			logger.error("HDModels - HDKnnModel - getSimilarity(" + i + "," + k + ")");
			logger.error("  => out of bounds exception : " + "i = " + i + " > " + getDataBase().getMaxVectorID() + " || k = " + k + " >= "
					+ getNeighborNumber());
			return 0;
		}
	}

	/**
	 * Specify the similarity between 2 vectors
	 * 
	 * @param i
	 *            a vector identifier
	 * @param k
	 *            a neighbor index
	 * @param s
	 *            the similarity between the corresponding vectors
	 */
	public void setSimilarity(int i, int k, float s) {
		if ((i <= getDataBase().getMaxVectorID()) && (k < getNeighborNumber())) {
			similarityMatrix[i][k] = s;
		} else {
			logger.error("HDModels - HDKnnModel - setSimilarity(" + i + "," + k + "," + s + ")");
			logger.error("  => out of bounds exception : " + "i = " + i + " > " + getDataBase().getMaxVectorID() + " || k = " + k + " >= "
					+ getNeighborNumber());
		}
	}

	/**
	 * @param i
	 *            a vector identifier
	 * @return the number of neighbors of this vector
	 */
	public int getNeighborNumber(int i) {
		if (getMinSimilarity(i) == -2) {
			return getMinIndex(i);
		} else {
			return getNeighborNumber();
		}
	}

	/**
	 * Update the least similar neighbor to a vector
	 * 
	 * @param i
	 *            a vector identifier
	 */
	public void updateFurthestNeighbor(int i) {
		setMinSimilarity(i, getSimilarity(i, 0));
		setMinIndex(i, 0);
		for (int k = 1; k < getNeighborNumber(); k++) {
			if (getSimilarity(i, k) < getMinSimilarity(i)) {
				setMinSimilarity(i, getSimilarity(i, k));
				setMinIndex(i, k);
			}
		}
	}

	/**
	 * Add a neighbor
	 * 
	 * @param i
	 *            the identifier of the concerned vector
	 * @param j
	 *            the identifier of the neighbor vector
	 * @param s
	 *            the similarity between i and j
	 */
	public void addNeighbor(int i, int j, float s) {
		// Add the neighbor in the empty place
		if (getMinSimilarity(i) == -2) {
			int k = getMinIndex(i);
			setNeighbor(i, k, j);
			setSimilarity(i, k, s);
			if (k == getNeighborNumber() - 1) {
				updateFurthestNeighbor(i);
			} else {
				setMinIndex(i, k + 1);
			}
		}
		// Or add the neighbor in the place of the least similar if it is more
		// similar
		else if (s > getMinSimilarity(i)) {
			setNeighbor(i, getMinIndex(i), j);
			setSimilarity(i, getMinIndex(i), s);
			updateFurthestNeighbor(i);
		}
	}

	/**
	 * Learn the neighborhood model
	 */
	public void learn() {
		initializeLearnTime();
		int count = 0;
		Iterator<Integer> it = getDataBase().getVectorIDiterator();
		while (it.hasNext()) {

			Integer i = it.next();
			SparseVectorTabular v1 = getDataBase().getSparseVector(i);
			setVectorMean(i, v1.getMean());
			setVectorSize(i, v1.getSize());

			Iterator<Integer> jt = getDataBase().getVectorIDiterator();
			while (jt.hasNext()) {
				Integer j = jt.next();
				if (j.compareTo(i) > 0) {
					SparseVectorTabular v2 = getDataBase().getSparseVector(j);
					float sim = getDataBase().getSimilarityMix(v1, v2);
					addNeighbor(i, j, sim);
					addNeighbor(j, i, sim);
				}
			}
			if (logger.isInfoEnabled() && (++count % 1000 == 0)) {
				logger.info("  HDKnnModel - learn");
				logger.info("    -> " + count + " vectors treated in " + getCurrentLearnTime() + " ms");
			}
		}
		finalizeLearnTime();
	}

	/**
	 * Learn the neighborhood model using random sampling
	 */
	public void learnSpeed (int lower, int higher) {
		
		initializeLearnTime();
		
		int count = 0;
		Iterator<Integer> it = getDataBase().getVectorIDiterator();
		while (it.hasNext()) {

			Integer i = it.next();
			SparseVectorTabular v1 = getDataBase().getSparseVector(i);
			setVectorMean(i,v1.getMean());
			setVectorSize(i,v1.getSize());
			
			Iterator<Integer> jt = getDataBase().getVectorIDiterator();
			Integer j = 0;
			while (j<getDataBase().getSparseVectorNumber()) {
				//Display.displaySystemDateMsg("I:"+i+"=>J:"+j+"("+getDataBase().getSparseVectorNumber()+")");
				if (j.compareTo(i) != 0) {
					SparseVectorTabular v2 = getDataBase().getSparseVector(j);
					float sim = getDataBase().getSimilarityMix(v1,v2);
					addNeighbor(i,j,sim);
					addNeighbor(j,i,sim);
				}
				//int lower = 1;
				//int higher = 20;
				int pas = (int)(Math.random() * (higher-lower)) + lower;
				j = j + pas;
			}
			if (Parameters.traceOn && (++count % 1000 == 0)) {
				System.out.println("  HDKnnModel - learn");
				System.out.println("    -> " + count + " vectors treated in " + getCurrentLearnTime() + " ms");
			}
		}
		
		finalizeLearnTime();
	}
	/**
	 * Sort the neighbors in ascending order of their identifiers
	 */
	public void sortNeighbors() {

		Iterator<Integer> it = getDataBase().getVectorIDiterator();
		while (it.hasNext()) {

			Integer i = it.next();
			SparseVectorSorted v = new SparseVectorSorted();
			for (int k = 0; k < getNeighborNumber(i); k++) {
				v.setValue(getNeighbor(i, k), getSimilarity(i, k));
			}

			Iterator<Integer> jt = v.getAttributesIterator();
			int k = 0;
			setMinSimilarity(i, (float) 1E300);
			while (jt.hasNext()) {
				Integer j = jt.next();
				float sim = v.getValue(j);
				setNeighbor(i, k, j);
				setSimilarity(i, k, sim);
				if (sim < getMinSimilarity(i)) {
					setMinSimilarity(i, sim);
					setMinIndex(i, k);
				}
				k++;
			}
			if (k < getNeighborNumber()) {
				setMinSimilarity(i, -2);
			}
		}
	}

	/**
	 * Specify the dual database used for the recommendations and sort neighbors
	 * in ascending order of their identifiers
	 * 
	 * @param db
	 *            the dual database
	 */
	public void setDualDataBase(BaseOfSparseVector db) {
		myDualDataBase = db;
		sortNeighbors();
	}

	/**
	 * @return the dual database
	 */
	public BaseOfSparseVector getDualDataBase() {
		return myDualDataBase;
	}

	/**
	 * @param u
	 *            a user identifier
	 * @param i
	 *            an item identifier
	 * @return the recommendation for u on i
	 */
	public Recommendation getRecommendation(Integer u, Integer i) {
		if (getDualDataBase() == null) {
			logger.error("HDModels - HDKnnModel - getRecommendation(" + u + "," + i + ")");
			logger.error("  => need to set dual database");
			return null;
		}
		if (getDataBase().isVectorOfItemType()) {
			return getItemKnnRecommendation(u, i);
		} else {
			return getUserKnnRecommendation(u, i);
		}
	}

	/**
	 * Recommandation in the case of Knn Item Model
	 * 
	 * @param u
	 *            a user identifier
	 * @param i
	 *            an item identifier
	 * @return the recommendation for u on i
	 */
	public Recommendation getItemKnnRecommendation(Integer u, Integer i) {
		if (vectorIsDefined(i)) {
			if (getDualDataBase().hasSparseVector(u)) {
				SparseVectorTabular user = getDualDataBase().getSparseVector(u);
				float weightedSum = 0;
				float similaritySum = 0;
				int index = 0;
				for (int k = 0; k < getNeighborNumber(i); k++) {
					int j = getNeighbor(i, k);
					while ((index < user.getSize()) && (user.getAttributeIndex(index) < j)) {
						index++;
					}
					if (index < user.getSize()) {
						if (user.getAttributeIndex(index) == j) {
							weightedSum += getSimilarity(i, k) * (user.getValueIndex(index) - getVectorMean(j));
							similaritySum += Math.abs(getSimilarity(i, k));
						}
					}
				}
				if (similaritySum > 0) {
					float reco = Math.max(Parameters.minRatingScale,
							Math.min(Parameters.maxRatingScale, getVectorMean(i) + weightedSum / similaritySum));
					return new Recommendation(i, reco, similaritySum);
				} else {
					// return super.getRecommendation(u,i);
					addOneDefaultPrediction();
					float reco = (getVectorMean(i) + getDualDataBase().getSparseVector(u).getMean()) / 2;
					return new Recommendation(i, reco, 0);
				}
			} else {
				// return super.getRecommendation(u,i);
				addOneDefaultPrediction();
				float reco = (getVectorMean(i) + getDualDataBase().getSparseVector(u).getMean()) / 2;
				return new Recommendation(i, reco, 0);
			}
		} else {
			// return super.getRecommendation(u,i);
			addOneDefaultPrediction();
			float reco = (getVectorMean(i) + getDualDataBase().getSparseVector(u).getMean()) / 2;
			return new Recommendation(i, reco, 0);
		}
	}

	/**
	 * Recommandation in the case of Knn User Model
	 * 
	 * @param u
	 *            a user identifier
	 * @param i
	 *            an item identifier
	 * @return the recommendation for u on i
	 */
	public Recommendation getUserKnnRecommendation(Integer u, Integer i) {
		if (vectorIsDefined(u)) {
			if (getDualDataBase().hasSparseVector(i)) {
				SparseVectorTabular item = getDualDataBase().getSparseVector(i);
				float weightedSum = 0;
				float similaritySum = 0;
				int index = 0;
				for (int k = 0; k < getNeighborNumber(u); k++) {
					int v = getNeighbor(u, k);
					while ((index < item.getSize()) && (item.getAttributeIndex(index) < v)) {
						index++;
					}
					if (index < item.getSize()) {
						if (item.getAttributeIndex(index) == v) {
							weightedSum += getSimilarity(u, k) * (item.getValueIndex(index) - getVectorMean(v));
							similaritySum += Math.abs(getSimilarity(u, k));
						}
					}
				}
				if (similaritySum > 0) {
					float reco = Math.max(Parameters.minRatingScale,
							Math.min(Parameters.maxRatingScale, getVectorMean(u) + weightedSum / similaritySum));
					return new Recommendation(i, reco, similaritySum);
				} else {
					return super.getRecommendation(u, i);
				}
			} else {
				return super.getRecommendation(u, i);
			}
		} else {
			return super.getRecommendation(u, i);
		}
	}

	/**
	 * Immediate recommendation in the case of Knn item model
	 * 
	 * @param user
	 *            a user vector
	 * @param i
	 *            an item identifier
	 * @return the recommendation for the user on i
	 */
	public Recommendation getImmediateRecommendation(SparseVectorSorted user, Integer i) {
		if (getDataBase().isVectorOfUserType()) {
			logger.error("HDModels - HDKnnModel - getImmediateRecommendation(" + i + ")");
			logger.error("  => wrong model type : need to be item-based");
			return null;
		}
		if (vectorIsDefined(i)) {
			float weightedSum = 0;
			float similaritySum = 0;
			for (int k = 0; k < getNeighborNumber(i); k++) {
				int j = getNeighbor(i, k);
				if (user.hasValue(j)) {
					weightedSum += getSimilarity(i, k) * (user.getValue(j) - getVectorMean(j));
					similaritySum += Math.abs(getSimilarity(i, k));
				}
			}
			if (similaritySum > 0) {
				float reco = Math.max(Parameters.minRatingScale,
						Math.min(Parameters.maxRatingScale, getVectorMean(i) + weightedSum / similaritySum));
				return new Recommendation(i, reco, similaritySum);
			} else {
				return new Recommendation(i, getVectorMean(i), 0);
			}
		} else {
			return new Recommendation(i, user.getMean(), 0);
		}
	}

	/**
	 * Print the informations associated to the model
	 */
	public void print() {

		int meanNeighborNumber = 0;
		float meanSimilarity = 0;
		float minSimilarity = (float) 1E300;
		float maxSimilarity = -(float) 1E300;

		Iterator<Integer> it = getDataBase().getVectorIDiterator();
		while (it.hasNext()) {

			Integer i = it.next();

			for (int k = 0; k < getNeighborNumber(i); k++) {
				meanNeighborNumber++;
				meanSimilarity += getSimilarity(i, k);
				if (getSimilarity(i, k) < minSimilarity) {
					minSimilarity = getSimilarity(i, k);
				}
				if (getSimilarity(i, k) > maxSimilarity) {
					maxSimilarity = getSimilarity(i, k);
				}
			}
		}

		meanSimilarity /= meanNeighborNumber;
		meanNeighborNumber /= getDataBase().getSparseVectorNumber();

		if (logger.isInfoEnabled()) {
			logger.info("HDKnnModel " + getDataBase().getVectorType() + " " + getNeighborNumber() + " :");
			logger.info(" * learn time = " + getLearnTime() + " ms");
			logger.info(" * mean neighbor number = " + meanNeighborNumber);
			logger.info(" * mean similarity = " + meanSimilarity);
			logger.info(" * similarity in [" + minSimilarity + "," + maxSimilarity + "]");
		}

		super.print();
	}

	/**
	 * @return the name of the file used to store the model
	 */
	public String getModelFileName() {
		File d = new File(Parameters.engineModelDirectory);
		if (!d.isDirectory()) {
			d.mkdir();
		}
		return Parameters.engineModelDirectory + "HDKnn_" + getDataBase().getVectorType() + "_K" + getNeighborNumber();
	}

	/**
	 * Save the model
	 */
	public void save() {
		String fileName = getModelFileName();
		saveKnnModel(fileName + ".knn");
		saveInfos(fileName + ".infos");
		super.save();
	}

	/**
	 * Save the neighborhood model
	 * 
	 * @param fileName
	 *            the name of the file used to store the model
	 */
	public void saveKnnModel(String fileName) {
		BufferedWriter buf = null;
		try {

			buf = new BufferedWriter(new FileWriter(fileName));

			Iterator<Integer> it = getDataBase().getVectorIDiterator();
			while (it.hasNext()) {
				Integer i = it.next();
				buf.write("Vector " + i + "\n");
				for (int k = 0; k < getNeighborNumber(i); k++) {
					buf.write(getNeighbor(i, k) + "\t" + getSimilarity(i, k) + "\n");
				}
			}

		} catch (IOException e) {
			logger.error("HDModels - HDKnnModel - saveKnnModel");
			logger.error("  => IO exception when saving on file " + fileName);
		} finally {
			try {
				if (buf != null) {
					buf.close();
				}
			} catch (IOException e) {
				logger.error("I/O error: " + e.getMessage());
			}
		}
	}

	/**
	 * Save the informations associated to the model
	 * 
	 * @param fileName
	 *            the name of the file used to store the informations
	 */
	public void saveInfos(String fileName) {

		int meanNeighborNumber = 0;
		float meanSimilarity = 0;
		float minSimilarity = (float) 1E300;
		float maxSimilarity = -(float) 1E300;

		Iterator<Integer> it = getDataBase().getVectorIDiterator();
		while (it.hasNext()) {

			Integer i = it.next();

			for (int k = 0; k < getNeighborNumber(i); k++) {
				meanNeighborNumber++;
				meanSimilarity += getSimilarity(i, k);
				if (getSimilarity(i, k) < minSimilarity) {
					minSimilarity = getSimilarity(i, k);
				}
				if (getSimilarity(i, k) > maxSimilarity) {
					maxSimilarity = getSimilarity(i, k);
				}
			}
		}

		meanSimilarity /= meanNeighborNumber;
		meanNeighborNumber /= getDataBase().getSparseVectorNumber();

		BufferedWriter buf = null;
		try {
			buf = new BufferedWriter(new FileWriter(fileName));

			buf.write(getLearnTime() + " ms to learn model\n");
			buf.write(getNeighborNumber() + " " + getDataBase().getVectorType() + " neighbors\n");
			buf.write(meanNeighborNumber + " mean neighbor number\n");
			buf.write(meanSimilarity + " mean similarity\n");
			buf.write(minSimilarity + " min similarity\n");
			buf.write(maxSimilarity + " max similarity\n");
			super.saveInfos(buf);

		} catch (IOException e) {
			logger.error("HDModels - HDKnnModel - saveInfos");
			logger.error("  => IO exception when saving on file " + fileName);
		} finally {
			try {
				if (buf != null) {
					buf.close();
				}
			} catch (IOException e) {
				logger.error("I/O error: " + e.getMessage());
			}
		}
	}

	/**
	 * Save the model using external keys
	 * 
	 * @param dico
	 *            the dictionary of correspondences between native and internal
	 *            keys
	 */
	public void save(KeyDictionary dico) {
		String fileName = getModelFileName();
		saveKnnModel(fileName + ".knn", dico);
		saveInfos(fileName + ".infos");
		super.save(dico);
	}

	/**
	 * Save the neighborhood model using external keys
	 * 
	 * @param fileName
	 *            the name of the file used to store the model
	 * @param dico
	 *            the dictionary of correspondences between native and internal
	 *            keys
	 */
	public void saveKnnModel(String fileName, KeyDictionary dico) {
		BufferedWriter buf = null;
		try {

			buf = new BufferedWriter(new FileWriter(fileName));

			Iterator<Integer> it = getDataBase().getVectorIDiterator();
			while (it.hasNext()) {
				Integer i = it.next();
				buf.write("Vector " + dico.getExternalKey(i) + "\n");
				for (int k = 0; k < getNeighborNumber(i); k++) {
					buf.write(dico.getExternalKey(getNeighbor(i, k)) + "\t" + getSimilarity(i, k) + "\n");
				}
			}

		} catch (IOException e) {
			logger.error("HDModels - HDKnnModel - saveKnnModel");
			logger.error("  => IO exception when saving on file " + fileName);
		} finally {
			try {
				if (buf != null) {
					buf.close();
				}
			} catch (IOException e) {
				logger.error("I/O error: " + e.getMessage());
			}
		}
	}

	/**
	 * Save as SQL tables the neighborhood model using external keys
	 * 
	 * @param fileName
	 *            the name of the file used to store the model
	 * @param dico
	 *            the dictionary of correspondences between native and internal
	 *            keys
	 */
	public void saveKnnModelCSV(String fileName, KeyDictionary dico) {
		BufferedWriter buf = null;
		try {
			buf = new BufferedWriter(new FileWriter(fileName));

			Iterator<Integer> it = getDataBase().getVectorIDiterator();
			while (it.hasNext()) {
				Integer i = it.next();
				String id = dico.getExternalKey(i);
				for (int k = 0; k < getNeighborNumber(i); k++) {
					buf.write(id + "\t" + dico.getExternalKey(getNeighbor(i, k)) + "\t" + getSimilarity(i, k) + "\n");
				}
			}

		} catch (IOException e) {
			logger.error("HDModels - HDKnnModel - saveKnnModelSQL");
			logger.error("  => IO exception when saving on file " + fileName);
		} finally {
			try {
				if (buf != null) {
					buf.close();
				}
			} catch (IOException e) {
				logger.error("I/O error: " + e.getMessage());
			}
		}
	}
	
	/**
	 * Save as SQL tables the neighborhood model using external keys
	 * @param fileName the name of the file used to store the model
	 * @param dico the dictionary of correspondences between native and internal keys
	 */
	public void saveKnnModelSQL (SimilarityMatrix matrix, KeyDictionary dico) {
		Iterator<Integer> it = getDataBase().getVectorIDiterator();
		while (it.hasNext()) {
			Integer i = it.next();
			String id = dico.getExternalKey(i);
			for (int k = 0; k < getNeighborNumber(i); k++) {
				float sim = getSimilarity(i, k);
				if (sim > matrix.getThreshold()) {
					Similarity similarity = new Similarity();
					similarity.setObjectId(id);
					similarity.setSimilarObjectId(dico.getExternalKey(getNeighbor(i, k)));
					similarity.setValue(sim);
					similarityRepository.save(similarity);
				}
			}
		}
	}

	/**
	 * Load the model
	 */
	public void load() {
		String fileName = getModelFileName();
		loadKnnModel(fileName + ".knn");
		loadInfos(fileName + ".infos");
		super.load();
	}

	/**
	 * Load the neighborhood model
	 * 
	 * @param fileName
	 *            the name of the file used to store the model
	 */
	public void loadKnnModel(String fileName) {
		BufferedReader buf = null;

		try {
			String line;
			buf = new BufferedReader(new FileReader(fileName));

			// Neighborhood model

			int currentVector = 0;
			while ((line = buf.readLine()) != null) {

				// Change the concerned vector

				if (line.startsWith("Vector")) {
					StringTokenizer tok = new StringTokenizer(line, " ", false);
					try {
						tok.nextToken();
						currentVector = new Integer(tok.nextToken());
					} catch (NumberFormatException e) {
						logger.error("HDModels - HDKnnModel - loadKnnModel");
						logger.error("  => class cast exception when loading from file " + fileName);
					}
				}

				// Add a neighbor to the current vector

				else {
					StringTokenizer tok = new StringTokenizer(line, "\t", false);
					try {
						Integer j = new Integer(tok.nextToken());
						Float s = new Float(tok.nextToken());
						addNeighbor(currentVector, j, s);
					} catch (NumberFormatException e) {
						logger.error("HDModels - HDKnnModel - loadKnnModel");
						logger.error("  => class cast exception when loading from file " + fileName);
					}
				}
			}

		} catch (IOException e) {
			logger.error("HDModels - HDKnnModel - loadKnnModel");
			logger.error("  => IO exception when loading from file " + fileName);
		} finally {
			try {
				if (buf != null) {
					buf.close();
				}
			} catch (IOException e) {
				logger.error("I/O error: " + e.getMessage());
			}
		}
	}

	/**
	 * Load the model from external keys
	 * 
	 * @param dico
	 *            the dictionary of correspondences between native and internal
	 *            keys
	 */
	public void load(KeyDictionary dico) {
		String fileName = getModelFileName();
		loadKnnModel(fileName + ".knn", dico);
		loadInfos(fileName + ".infos");
		super.load(dico);
	}

	/**
	 * Load the neighborhood model from external keys
	 * 
	 * @param fileName
	 *            the name of the file used to store the model
	 * @param dico
	 *            the dictionary of correspondences between native and internal
	 *            keys
	 */
	public void loadKnnModel(String fileName, KeyDictionary dico) {
		BufferedReader buf = null;
		try {

			buf = new BufferedReader(new FileReader(fileName));

			String line;

			// Neighborhood model

			int currentVector = 0;
			while ((line = buf.readLine()) != null) {

				// Change the concerned vector

				if (line.startsWith("Vector")) {
					StringTokenizer tok = new StringTokenizer(line, " ", false);
					try {
						tok.nextToken();
						currentVector = dico.getInternalKey(tok.nextToken());
					} catch (NumberFormatException e) {
						logger.error("HDModels - HDKnnModel - loadKnnModel");
						logger.error("  => class cast exception when loading from file " + fileName);
					}
				}

				// Add a neighbor to the current vector

				else {
					StringTokenizer tok = new StringTokenizer(line, "\t", false);
					try {
						Integer i = dico.getInternalKey(tok.nextToken());
						Float s = new Float(tok.nextToken());
						addNeighbor(currentVector, i, s);
					} catch (NumberFormatException e) {
						logger.error("HDModels - HDKnnModel - loadKnnModel");
						logger.error("  => class cast exception when loading from file " + fileName);
					}
				}
			}

		} catch (IOException e) {
			logger.error("HDModels - HDKnnModel - loadKnnModel");
			logger.error("  => IO exception when loading from file " + fileName);
		} finally {
			try {
				if (buf != null) {
					buf.close();
				}
			} catch (IOException e) {
				logger.error("I/O error: " + e.getMessage());
			}
		}
	}

	/**
	 * Load the informations associated to the model
	 * 
	 * @param fileName
	 *            the name of the file used to store the informations
	 */
	public void loadInfos(String fileName) {
		BufferedReader buf = null;
		try {

			buf = new BufferedReader(new FileReader(fileName));

			String line;

			// Creation time of the model

			if ((line = buf.readLine()) != null) {
				StringTokenizer tok = new StringTokenizer(line, " ", false);
				try {
					setLearnTime(new Long(tok.nextToken()));
				} catch (NumberFormatException e) {
					logger.error("HDModels - HDModel - loadInfos");
					logger.error("  => class cast exception when loading information from file " + fileName);
				}
			}

		} catch (IOException e) {
			logger.error("HDModels - HDModel - loadInfos");
			logger.error("  => IO exception when loading information from file " + fileName);
		} finally {
			try {
				if (buf != null) {
					buf.close();
				}
			} catch (IOException e) {
				logger.error("I/O error: " + e.getMessage());
			}
		}
	}
}
