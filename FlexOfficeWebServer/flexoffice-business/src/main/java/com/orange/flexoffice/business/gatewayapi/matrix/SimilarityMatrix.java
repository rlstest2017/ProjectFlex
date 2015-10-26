package com.orange.flexoffice.business.gatewayapi.matrix;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.gatewayapi.core.dataAccess.KeyDictionary;
import com.orange.flexoffice.business.gatewayapi.core.hdModels.HDKnnModel;
import com.orange.flexoffice.business.gatewayapi.source.Source;
import com.orange.flexoffice.dao.gatewayapi.model.Similarity;
import com.orange.flexoffice.dao.gatewayapi.repository.similarity.SimilarityRepository;


/**
 * This class provides methods to create a similarity matrix, computed by using
 * sub-matrixes.
 * 
 * @author Camille Blin
 */
@Transactional
public class SimilarityMatrix {
	private static Logger logger = Logger.getLogger(SimilarityMatrix.class);

	private SimilarityRepository similarityRepository;
	
	/**
	 * Number of most similar items to keep in the similarity matrix.
	 */
	private int KNN;

	/**
	 * Similarity threshold. Item similarities under this value will not be
	 * stored in the similarity matrix.
	 */
	private float threshold;

	/**
	 * Boolean parameter to determine if the matrix is created on rows(default =
	 * true) or columns(false)
	 */
	private boolean isRowsMatrix = true;
	
	private Source source;
	
	private KeyDictionary dico;
	private float[][] tabSim;
	private LinkedHashMap<String, LinkedHashMap<String, Float>> similarities;
	private boolean isAvailable = false; // true if the matrix exists
	private boolean isLoaded = false; // true if the matrix is loaded
	private boolean generatingMatrix = false; // true if the matrix is generated.

	public SimilarityMatrix(Source source, int KNN, boolean isRowsMatrix, 
			float threshold, SimilarityRepository similarityRepository) {
		
		this.KNN = KNN;
		this.threshold = threshold;
		this.isRowsMatrix = isRowsMatrix;
		this.similarityRepository = similarityRepository;
		this.source = source;
		
		loadSimilarities(true);
	}
	
	/**
	 * Returns all loaded similarities.
	 */
	public LinkedHashMap<String, LinkedHashMap<String, Float>> getSimilarities() {
		return similarities;
	}
	
	/**
	 * Returns true if this matrix is a rows matrix, ie it
	 * is based of rows of the source.
	 */
	public boolean isRowsMatrix() {
		return isRowsMatrix;
	}
	
	/**
	 * Returns the number of similarities computes for each items.
	 */
	public int getKNN() {
		return KNN;
	}
	
	/**
	 * Returns the similarity threshold.
	 */
	public float getThreshold() {
		return threshold;
	}

	/**
	 * Check if matrix has been created into the database
	 * 
	 * @return <code>true</code> if the table in the database exists and is not
	 *         empty, otherwise <code>false</code>.
	 */
	public boolean checkMatrixAvailability() {
		if (!isAvailable && !generatingMatrix) {
			isAvailable = similarityRepository.count() > 0;
		}
		
		return isAvailable;
	}

	/**
	 * Set the similarity threshold of the similarity matrix.
	 * 
	 * @param value
	 *            the threshold value
	 */
	public void setSimilarityThreshold(float value) {
		threshold = value;
	}

	/**
	 * Load the similarity matrix in memory inside a HashMap. force : if true,
	 * forces the matrix to be loaded even if already loaded
	 */
	public void loadSimilarities(boolean force) {
		if (!isLoaded || force) {
			if (logger.isInfoEnabled()) {
				logger.info("Loading Similarities");
			}
			
			similarities = new LinkedHashMap<String, LinkedHashMap<String, Float>>();
			
			List<Similarity> simis = similarityRepository.findAllSorted();
			for (Similarity similarity : simis) {
				String object = similarity.getObjectId();
				String similar = similarity.getSimilarObjectId();
				Float value =  similarity.getValue();
				
				if (!similarities.containsKey(object)) {
					similarities.put(object, new LinkedHashMap<String, Float>(KNN + 1, 1));
				}
				similarities.get(object).put(similar, value);
			}
			
			isLoaded = true;
		} else {
			if (logger.isInfoEnabled()) {
				logger.info("Matrix was Already loaded and hasn't been forced to load");
			}
		}
	}

	/**
	 * Return K nearest neighbor of id , if possible
	 * n but possibly less
	 * id can be a rowID or a columnID (depending on the type of matrix)
	 * 
	 * @param id
	 * @param n
	 * @return
	 */
	public String[] getSortedKNN(String id, int n) {
		String[] res = null;
		if (isLoaded) {
			int i = 0;
			LinkedHashMap<String, Float> tmp = similarities.get(id);
			if (tmp != null) {
				res = new String[tmp.size() < n ? tmp.size() : n];
				Iterator<String> it = tmp.keySet().iterator();
				while (it.hasNext() && i < res.length) {
					res[i] = it.next();
					i++;
				}
			}
		}
		return res;
	}
	
	/**
	 * Returns <code>n</code> (possibly less) objects similar to the specified object.
	 * Objects are ordered from the most to least similar.
	 * 
	 * @param id
	 * 		  ID of the reference object
	 * @param n
	 * 		  number of similarities
	 * @return a list of similarities
	 */
	public List<Similarity> getSortedSimilarities(String id, int n) {
		if (!isLoaded) {
			return null;
		}
		LinkedHashMap<String, Float> sims = similarities.get(id);
		List<Similarity> res = new ArrayList<Similarity>();
		if (sims != null) {
			Iterator<Entry<String, Float>> it = sims.entrySet().iterator();
			for (int i = 0; i < n && it.hasNext() ; i++) {
				Entry<String, Float> entry = it.next();
				Similarity s = new Similarity();
				s.setObjectId(id);
				s.setSimilarObjectId(entry.getKey());
				s.setValue(entry.getValue());
				res.add(s);
			}
		}
		return res;
	}

	public void setDico(KeyDictionary d) {
		this.dico = d;
		int s = d.getKeyNumber();
		this.tabSim = new float[s][s];
	}

	public void loadCore() {
		if (logger.isInfoEnabled()) {
			logger.info("Loading Similarities");
		}
		
		List<Similarity> similarities = similarityRepository.findAllSorted();
		
		for (Similarity similarity : similarities) {
			String object = similarity.getObjectId();
			String similar = similarity.getSimilarObjectId();
			Float value = similarity.getValue();
			
			int io = dico.getInternalKey(object);
			int is = dico.getInternalKey(similar);
			tabSim[io][is] = value;
		}
	}
	
	public void generateKNNTable(boolean clustering) {
		if (isRowsMatrix)
			source.loadBase(false, true);
		else {
			source.loadBase(true, false);
		}
		source.getBase().print();


		// Learn the neighborhood model

		HDKnnModel model;
		if (isRowsMatrix){
			model = new HDKnnModel(source.getBase().getItemBase(), KNN, similarityRepository);
		}
		else {
			model = new HDKnnModel(source.getBase().getUserBase(), KNN, similarityRepository);
		}
		model.learnSpeed(1,20);
		model.print();
		
		if (isRowsMatrix) {
			model.saveKnnModelSQL(this, source.getBase().getItemDictionary());
		} else
			model.saveKnnModelSQL(this, source.getBase().getUserDictionary());

	}

}