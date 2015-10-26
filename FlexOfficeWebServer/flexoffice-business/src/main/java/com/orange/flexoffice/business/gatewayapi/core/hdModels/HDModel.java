package com.orange.flexoffice.business.gatewayapi.core.hdModels;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.orange.flexoffice.business.gatewayapi.core.dataAccess.BaseOfSparseVector;
import com.orange.flexoffice.business.gatewayapi.core.dataAccess.DualBaseOfSparseVector;
import com.orange.flexoffice.business.gatewayapi.core.dataAccess.KeyDictionary;
import com.orange.flexoffice.business.gatewayapi.core.dataAccess.SparseVectorSorted;
import com.orange.flexoffice.business.gatewayapi.core.dataAccess.SparseVectorTabular;
import com.orange.flexoffice.business.gatewayapi.core.mains.Parameters;

/**
 * 
 * HDModel : generic model
 * 
 * @author Laurent Candillier
 * 
 * @version 7.0 (07/01/2009)
 * 
 * 
 * ENGLISH VERSION
 * 
 * 
 * Generic class for all recommender models on High Dimensional databases
 * 
 * To each model are associated a BaseOfSparseVector, its creation time,
 * and its performances on test : error rate, with and without rounding, 
 * absolute and quadratic, precisions in providing ordered list of appreciated items,
 * number of examples in test, execution time and number of default recommendations
 * 
 * Implements a default recommendation in the function getRecommendation(u,i)
 * If the database given as input is of item type, the default recommendation is the mean rating on the item
 * If the database given as input is of user type, the default recommendation is the mean rating of the user
 * 
 * Allows to execute the test of any model with the method runTests(testDB)
 * having as input a user BaseOfSparseVector, and then a method printPerformances()
 * that prints the set of results obtained on this test set
 * 
 * Implements also the method runRealNetflix that executes the recommendations 
 * on the real test set given by Netflix,
 * as well as save and load procedures of the default model
 * 
 * Uses the parameters minRatingScale and maxRatingScale
 * 
 * 
 * VERSION FRANCAISE
 * 
 * 
 * Classe g�n�rique pour tout mod�le de recommandations sur base de donn�es de Haute Dimension
 * 
 * A chaque mod�le sont associ�s une BaseOfSparseVector, son temps de cr�ation,
 * et ses performances en test : taux d'erreur, avec et sans arrondi, 
 * absolue et quadratique, pr�cisions pour fournir des listes ordonn�es d'items appr�ci�s,
 * nombre d'exemples en test, temps d'ex�cution et nombre de recommandations par d�faut
 * 
 * Elle impl�mente une recommandation par d�faut dans getRecommendation(u,i)
 * Si la base fournie en entr�e est de type item, la recommandation par d�faut est la note moyenne item
 * Si la base fournie en entr�e est de type user, la recommandation par d�faut est la note moyenne user
 * 
 * Elle permet de lancer le test de tout mod�le avec la m�thode runTests(testDB)
 * qui prend en argument une BaseOfSparseVector user, puis une m�thode 
 * printPerformances() qui affiche l'ensemble des r�sultats obtenus sur ce jeu de test
 * 
 * Impl�mente aussi la m�thode runRealNetflix qui lance les recommandations 
 * sur le jeu de test r�el fourni par Netflix,
 * ainsi que des proc�dures de sauvegarde et de chargement du mod�le par d�faut, save() et load()
 * 
 * Utilise les param�tres minRatingScale et maxRatingScale
 * 
 */

public class HDModel {
	private final Logger logger = Logger.getLogger(HDModel.class);
	/**
	 * A model is associated to a BaseOfSparseVector
	 * and stores the sizes and mean ratings of the vectors
	 */
	private BaseOfSparseVector myDataBase;
	private int[] vectorSize;
	private float[] vectorMean;
	
	/**
	 * To each model are associated its learning time and performances in test :
	 * error rate, with and without rounding, absolute and quadratic, 
	 * precisions in providing ordered list of appreciated items,
	 * number of examples in test, execution time and number of default recommendations
	 */
	private long learnTime = 0;
	private float mae = 0;
	private float maen = 0;
	private float rmse = 0;
	private float rmsen = 0;
	private float precision0 = 0;
	private int testSize0 = 0;
	private float precision1 = 0;
	private int testSize1 = 0;
	private float precision2 = 0;
	private int testSize2 = 0;
	private int testSize = 0;
	private long predictionTime = 0;
	private int defaultPredictionNumber = 0;

	/**
	 * The constructor stores the associated database and initializes the parameters
	 * @param base the associated database
	 */
	public HDModel (BaseOfSparseVector base) {
		myDataBase = base;
		vectorSize = new int[base.getMaxVectorID()+1];
		vectorMean = new float[base.getMaxVectorID()+1];
	}
	
	/**
	 * @return the associated database
	 */
	public BaseOfSparseVector getDataBase () {
		return myDataBase;
	}
	
	/**
	 * @param i a vector identifier
	 * @return the mean rating on the vector
	 */
	public float getVectorMean (int i) {
		if (i <= getDataBase().getMaxVectorID()) {
			return vectorMean[i];
		}
		else{
			logger.error("HDModels - HDModel - getVectorMean(" + i + ")");
			logger.error("  => out of bounds exception : " 
					+ "i = " + i + " > " + getDataBase().getMaxVectorID());
			return 0;
		}
	}

	/**
	 * Specify the mean rating on a vector
	 * @param i a vector identifier
	 * @param m the mean rating on the vector
	 */
	public void setVectorMean (int i, float m) {
		if (i <= getDataBase().getMaxVectorID()) {
			vectorMean[i] = m;
		}
		else{
			logger.error("HDModels - HDModel - setVectorMean(" + i + "," + m + ")");
			logger.error("  => out of bounds exception : " 
					+ "i = " + i + " > " + getDataBase().getMaxVectorID());
		}
	}
	
	/**
	 * @param i a vector identifier
	 * @return true if the vector is defined (its size is positive)
	 */
	public boolean vectorIsDefined (int i) {
		if (i <= getDataBase().getMaxVectorID()) {
			return (vectorSize[i] > 0);
		}
		else{
			return false;
		}
	}
	
	/**
	 * @param i a vector identifier
	 * @return the size of the vector
	 */
	public int getVectorSize (int i) {
		if (i <= getDataBase().getMaxVectorID()) {
			return vectorSize[i];
		}
		else{
			logger.error("HDModels - HDModel - getVectorSize(" + i + ")");
			logger.error("  => out of bounds exception : " 
					+ "i = " + i + " > " + getDataBase().getMaxVectorID());
			return 0;
		}
	}

	/**
	 * Specify the size of a vector
	 * @param i a vector identifier
	 * @param s the size of the vector
	 */
	public void setVectorSize (int i, int s) {
		if (i <= getDataBase().getMaxVectorID()) {
			vectorSize[i] = s;
		}
		else{
			logger.error("HDModels - HDModel - setVectorSize(" + i + "," + s + ")");
			logger.error("  => out of bounds exception : " 
					+ "i = " + i + " > " + getDataBase().getMaxVectorID());
		}
	}
	
	/**
	 * @return the learning time of the model
	 */
	public long getLearnTime () {
		return learnTime;
	}

	/**
	 * @return the current learning time of the model
	 */
	public long getCurrentLearnTime () {
		return (System.currentTimeMillis() - learnTime);
	}

	/**
	 * Specify the learning time of the model
	 * @param t the learning time of the model
	 */
	public void setLearnTime (long t) {
		learnTime = t;
	}
	
	/**
	 * Initialize the learning time of the model
	 */
	public void initializeLearnTime () {
		learnTime = System.currentTimeMillis();
	}

	/**
	 * Finalize the learning time of the model
	 */
	public void finalizeLearnTime () {
		learnTime = System.currentTimeMillis() - learnTime;
	}

	/**
	 * @return the error rate in test (MAE)
	 */
	public float getMAE () {
		return mae;
	}

	/**
	 * @return the error rate in test with rounding (MAEN)
	 */
	public float getMAEN () {
		return maen;
	}

	/**
	 * @return the quadratic error rate in test (RMSE)
	 */
	public float getRMSE () {
		return rmse;
	}

	/**
	 * @return the quadratic error rate in test with rounding (RMSEN)
	 */
	public float getRMSEN () {
		return rmsen;
	}

	/**
	 * @return the size of the test set
	 */
	public int getTestSize () {
		return testSize;
	}

	/**
	 * @return the time to compute the predictions
	 */
	public long getPredictionTime () {
		return predictionTime;
	}
	
	/**
	 * @return the number of default predictions
	 */
	public int getDefaultPredictionNumber () {
		return defaultPredictionNumber;
	}
	
	/**
	 * Add one default prediction
	 */
	public void addOneDefaultPrediction () {
		defaultPredictionNumber++;
	}
	
	/**
	 * @return the precision of the model in predicting items of highest rating
	 */
	public float getPrecision0 () {
		return precision0;
	}
	
	/**
	 * @return the precision of the model in predicting items of rating higher than max - 1
	 */
	public float getPrecision1 () {
		return precision1;
	}
	
	/**
	 * @return the precision of the model in predicting items of rating higher than max - 2
	 */
	public float getPrecision2 () {
		return precision2;
	}
	
	/**
	 * @return the size of the test set in predictions of highest ratings
	 */
	public int getTestSize0 () {
		return testSize0;
	}
	
	/**
	 * @return the size of the test set in predictions of ratings higher than max - 1
	 */
	public int getTestSize1 () {
		return testSize1;
	}
	
	/**
	 * @return the size of the test set in predictions of ratings higher than max - 2
	 */
	public int getTestSize2 () {
		return testSize2;
	}

	/**
	 * @return the mean size of vectors
	 */
	public int getMeanVectorSize () {
		return getDataBase().getValueNumber() / getDataBase().getSparseVectorNumber();
	}
	
	/**
	 * @return the median size of vectors
	 */
	public int getMedianVectorSize () {
		TreeSet<Integer> sizes = new TreeSet<Integer>();
		Iterator<Integer> it = getDataBase().getVectorIDiterator();
		while (it.hasNext()) {
			Integer i = it.next();
			sizes.add(getVectorSize(i));
		}
		int median = 0;
		it = sizes.iterator();
		for (int i=0 ; i<sizes.size()/2 ; i++) {
			median = it.next();
		}
		return median;
	}
	
	/**
	 * @param sampleSize a sample set size
	 * @param minVectorSize a minimal vector size
	 * @return a sample set with size sampleSize of vectors having their size > minVectorSize
	 */
	public HashSet<Integer> getRandomSampleSetWithMinimalSize (int sampleSize, int minVectorSize) {
		
		HashSet<Integer> sampleSet = new HashSet<Integer>();
		sampleSize = Math.min(sampleSize , getDataBase().getSparseVectorNumber());
		int nextSearchCount = 0;

		while (sampleSet.size() < sampleSize) {
			Integer id = getDataBase().getRandomVectorID();
			
			// select vectors having their size > minVectorSize
			if ( (! sampleSet.contains(id)) && (getVectorSize(id) > minVectorSize) ) {
				sampleSet.add(id);
				nextSearchCount = 0;
			}

			// avoid infinite loop
			nextSearchCount++;
			if (nextSearchCount > 100) {
				minVectorSize--;
				nextSearchCount = 0;				
			}
		}
		
		return sampleSet;
	}
	
	/**
	 * @param sampleSize a sample set size
	 * @param maxVectorSize a maximal vector size
	 * @return a sample set with size sampleSize of vectors having their size < maxVectorSize
	 */
	public HashSet<Integer> getRandomSampleSetWithMaximalSize (int sampleSize, int maxVectorSize) {
		
		HashSet<Integer> sampleSet = new HashSet<Integer>();
		sampleSize = Math.min(sampleSize , getDataBase().getSparseVectorNumber());
		int nextSearchCount = 0;

		while (sampleSet.size() < sampleSize) {
			Integer id = getDataBase().getRandomVectorID();
			
			// select vectors having their size < maxVectorSize
			if ( (! sampleSet.contains(id)) && (getVectorSize(id) < maxVectorSize) ) {
				sampleSet.add(id);
				nextSearchCount = 0;
			}

			// avoid infinite loop
			nextSearchCount++;
			if (nextSearchCount > 100) {
				maxVectorSize++;
				nextSearchCount = 0;				
			}
		}
		
		return sampleSet;
	}
	
	/**
	 * Save the table of best recommendations in the sample set
	 * @param recoNumber number of recommendations given in output
	 * @param sampleSet sample set of items to be considered
	 * @param fileName the name of the file to store the table
	 * @param myBase dual database associated
	 */	
	public void saveBestRecoTable (int recoNumber, HashSet<Integer> sampleSet, 
			String fileName, DualBaseOfSparseVector myBase) {

		BufferedWriter output = null;
		
		try{
			//Initialize
			
			long t = System.currentTimeMillis();
			float sumPredictions = 0;
			int countPredictions = 0;
			
			output = new BufferedWriter(new FileWriter(fileName));
			output.write("userID");
			for (int n=1 ; n<=recoNumber ; n++) {
				output.write("\t" + "itemID" + n + "\t" + "recoValue" + n);
			}
			output.write("\n");

			// For each user
		
			Iterator<Integer> ut = myBase.getUsersIterator();
			while (ut.hasNext()) {
				
				Integer u = ut.next();
				SparseVectorTabular user1 = myBase.getUser(u);
				SparseVectorSorted user = new SparseVectorSorted();
				user.loadFromSparseVectorTabular(user1);
				output.write(myBase.getUserExternalKey(u));

				// Score the given items

				TreeSet<Recommendation> predictions = 
					new TreeSet<Recommendation>(new RecoComparator());
				Iterator<Integer> it = sampleSet.iterator();
				while (it.hasNext()) {
					Integer i = it.next();
					predictions.add(getRecommendation(u,i));
				}
		
				// Store the best scores
		
				int currentRecoNumber = 0;
				Iterator<Recommendation> jt = predictions.iterator();
				while ( jt.hasNext() && (currentRecoNumber < recoNumber) ) {
					Recommendation r = jt.next();
					if (! user.hasValue(r.getID())) {
						float pred = (float)Math.round(r.getRating() * 100) / 100;
						output.write("\t" + myBase.getItemExternalKey(r.getID()) + "\t" + pred);
						sumPredictions += pred;
						countPredictions++;
						currentRecoNumber++;
					}
				}
				
				output.write("\n");
			}
			
			if (logger.isInfoEnabled()) {
				logger.info(fileName);
				logger.info("  * " + recoNumber + " recommendations");
				logger.info("  * " + (System.currentTimeMillis()-t) + " ms");
				logger.info("  * mean prediction = " + (sumPredictions/countPredictions));
			}
		} catch (IOException e) {
			logger.error("HDModels - HDModel - saveBestRecoTable");
			logger.error("  => IO exception when saving to file " + fileName);
		} finally {
			try {
				if (output != null) {
					output.close();
				}
			} catch (IOException ex) {
				logger.error("I/O error: " + ex.getMessage());
			}
		}
	}
	
	/**
	 * Learn the sizes and mean ratings of the vectors
	 */
	public void learn () {
		
		if (logger.isInfoEnabled()) {
			logger.info("  HDModel - learn");
		}

		Iterator<Integer> it = getDataBase().getVectorIDiterator();		
		while (it.hasNext()) {
			Integer i = it.next();
			SparseVectorTabular v = getDataBase().getSparseVector(i);
			setVectorMean(i,v.getMean());
			setVectorSize(i,v.getSize());
		}
	}

	/**
	 * @param u a user identifier
	 * @param i an item identifier
	 * @return the default recommendation of user u on item i
	 */
	public Recommendation getRecommendation (Integer u, Integer i) {
		addOneDefaultPrediction();
		float reco;
		if (getDataBase().isVectorOfItemType() && vectorIsDefined(i)) {
			reco = getVectorMean(i);
		}
		else if (getDataBase().isVectorOfUserType() && vectorIsDefined(u)) {
			reco = getVectorMean(u);
		}
		else{
			reco = Parameters.minRatingScale + 
				(float)(Parameters.maxRatingScale - Parameters.minRatingScale) / 2;
		}
		return new Recommendation(i,reco,0);
	}

	/**
	 * Execute the performance tests of the model
	 * @param testDB the test database of user type
	 */
	public void runTests (BaseOfSparseVector testDB) {

		if (testDB.isVectorOfItemType()) {
			logger.error("HDModels - HDModel - runTests");
			logger.error("  => wrong database type : need to be user-based");
		}
		
		predictionTime = System.currentTimeMillis();
		defaultPredictionNumber = 0;
		testSize = 0;
		mae = 0;
		maen = 0;
		rmse = 0;
		rmsen = 0;
		precision0 = 0;
		testSize0 = 0;
		precision1 = 0;
		testSize1 = 0;
		precision2 = 0;
		testSize2 = 0;

		Iterator<Integer> ut = testDB.getVectorIDiterator();
		while (ut.hasNext()) {
			Integer u = ut.next();
			SparseVectorTabular user = testDB.getSparseVector(u);
			testSize += user.getSize();
			
			// order the test ratings and predictions
			
			TreeSet<Recommendation> ratings 
				= new TreeSet<Recommendation>(new RecoComparator());
			TreeSet<Recommendation> predictions 
				= new TreeSet<Recommendation>(new RecoComparator());
			for (int index=0 ; index<user.getSize() ; index++) {
				int i = user.getAttributeIndex(index);
				float n = user.getValueIndex(index);
				ratings.add(new Recommendation(i,n,1));
				Recommendation r = getRecommendation(u,i);
				predictions.add(r);
				mae += Math.abs(n - r.getRating());
				maen += Math.abs(n - (float)Math.floor(r.getRating() + 0.5));
				rmse += Math.pow(n - r.getRating() , 2);
				rmsen += Math.pow(n - (float)Math.floor(r.getRating() + 0.5) , 2);
			}
			
			// for the highest ratings, those higher than max-1 and higher than max-2
			
			for (int maxRating=Parameters.maxRatingScale ; 
				maxRating >= Parameters.maxRatingScale - 2 ; maxRating--) {
				
				// create the set of best items for the user

				HashSet<Integer> bestItems = new HashSet<Integer>();
				Iterator<Recommendation> it = ratings.iterator();
				float last = Parameters.maxRatingScale;
				while (it.hasNext() && last >= maxRating) {
					Recommendation r = it.next();
					last = r.getRating();
					if (last >= maxRating) {
						bestItems.add(r.getID());
						if (maxRating == Parameters.maxRatingScale) {
							testSize0++;
						}
						else if (maxRating == Parameters.maxRatingScale - 1) {
							testSize1++;
						}
						else if (maxRating == Parameters.maxRatingScale - 2) {
							testSize2++;
						}
					}
				}
				
				// compute the precision of the predicted list
				
				it = predictions.iterator();
				for (int i=0 ; i<bestItems.size() ; i++) {
					if (bestItems.contains((it.next()).getID())) {
						if (maxRating == Parameters.maxRatingScale) {
							precision0++;
						}
						else if (maxRating == Parameters.maxRatingScale - 1) {
							precision1++;
						}
						else if (maxRating == Parameters.maxRatingScale - 2) {
							precision2++;
						}
					}
				}
			}
		}
		
		mae /= testSize;
		maen /= testSize;
		rmse = (float)Math.sqrt(rmse/testSize);
		rmsen = (float)Math.sqrt(rmsen/testSize);
		precision0 /= testSize0;
		precision1 /= testSize1;
		precision2 /= testSize2;
		predictionTime = System.currentTimeMillis() - predictionTime;
	}
	
	/**
	 * Print the performances in test of the model
	 */
	public void printPerformances () {
		if (logger.isInfoEnabled()) {
		logger.info("Performance :\n  MAE = " + getMAE() 
				+ " and MAEN = " + getMAEN() + "\n  RMSE = " 
				+ getRMSE() + " and RMSEN = " + getRMSEN() + "\n  "
				+ "Precision " + getPrecision0() + " for " + getTestSize0() 
				+ " ratings >= " + Parameters.maxRatingScale + "\n  "
				+ "Precision " + getPrecision1() + " for " + getTestSize1() 
				+ " ratings >= " + (Parameters.maxRatingScale - 1) + "\n  "
				+ "Precision " + getPrecision2() + " for " + getTestSize2() 
				+ " ratings >= " + (Parameters.maxRatingScale - 2) + "\n  "
				+ getDefaultPredictionNumber() + " default predictions for " 
				+ getTestSize() + " tests in " + getPredictionTime() + " ms");		
		}
	}

	/**
	 * Save the performances in test of the model
	 * @param fileName the name of the file to store the performances
	 */
	public void savePerformances (String fileName) {
		
		BufferedWriter buf = null;
		try{
			buf = new BufferedWriter(new FileWriter(fileName));
			buf.write("Performances du mod�le sur " + getTestSize() + " exemples tests\n");
			buf.write(" * prediction time = " + getPredictionTime() + " ms\n");		
			buf.write(" * default predictions = " + getDefaultPredictionNumber() + "\n");
			buf.write(" * MAE = " + getMAE() + "\n");
			buf.write(" * MAEN = " + getMAEN() + "\n");
			buf.write(" * RMSE = " + getRMSE() + "\n");
			buf.write(" * RMSEN = " + getRMSEN() + "\n");
			buf.write(" * Precision " + getPrecision0() + " for " + getTestSize0() 
					+ " ratings >= " + Parameters.maxRatingScale + "\n");
			buf.write(" * Precision " + getPrecision1() + " for " + getTestSize1() 
					+ " ratings >= " + (Parameters.maxRatingScale - 1) + "\n");
			buf.write(" * Precision " + getPrecision2() + " for " + getTestSize2() 
					+ " ratings >= " + (Parameters.maxRatingScale - 2) + "\n");
		} catch (IOException e) {
			logger.error("HDModels - HDModel - savePerformances");
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
	 * Execute the qualify test on Netflix
	 * @param inputFileName the name of the qualifying input file
	 * @param outputFileName the name of the results output file
	 * @param userDico dictionary of correspondences between native and internal user keys
	 * @param itemDico dictionary of correspondences between native and internal item keys
	 */
	public void runQualifyNetflix (String inputFileName, 
			String outputFileName, KeyDictionary userDico, KeyDictionary itemDico) {
		
		BufferedReader input = null;
		BufferedWriter output = null;
		try{
			predictionTime = System.currentTimeMillis();
			defaultPredictionNumber = 0;
			testSize = 0;
			
			File d = (new File(outputFileName)).getParentFile();
			if (! d.isDirectory()) {
				d.mkdir();
			}

			input = new BufferedReader(new FileReader(inputFileName));
			output = new BufferedWriter(new FileWriter(outputFileName));
			String line;
			String itemID = "";
			
			while ( (line = input.readLine()) != null ) {
				if (line.contains(":")) {
					StringTokenizer tok = new StringTokenizer(line,":",false);
					itemID = tok.nextToken();
					output.write(itemID + ":\n");
				}
				else{
					StringTokenizer tok = new StringTokenizer(line,",",false);
					String userID = tok.nextToken();
					float prediction = getRecommendation(userDico.getInternalKey(userID),
							itemDico.getInternalKey(itemID)).getRating();
					prediction = 1 + 4 * (prediction - Parameters.minRatingScale)
						/ (Parameters.maxRatingScale - Parameters.minRatingScale);
					output.write(prediction + "\n");
					testSize++;
				}
			}
			predictionTime = System.currentTimeMillis() - predictionTime;
			
			if (logger.isInfoEnabled()) {
				logger.info("Qualify Predictions :");
				logger.info(" * time = " + getPredictionTime() + " ms");
				logger.info(" * test size = " + getTestSize());
				logger.info(" * prediction = " + (getTestSize()-getDefaultPredictionNumber()));
				logger.info(" * default number = " + getDefaultPredictionNumber());
			}
		} catch (IOException e) {
			logger.error("HDModels - HDModel - runQualifyNetflix");
			logger.error("  => IO exception when reading from file " 
					+ inputFileName + " and writing on file " + outputFileName);
		} finally {
			try {
				if (input != null) {
					input.close();	
				}

			} catch (IOException e) {
				logger.error("I/O error: " + e.getMessage());
			}				
			try {		
				if (output != null) {
					output.close();
				} 
			} catch (IOException e) {
				logger.error("I/O error: " + e.getMessage());
			}
		}
	}

	/**
	 * Execute the probe test on Netflix
	 * @param inputFileName the name of the probe input file
	 * @param outputFileName the name of the probe results output file
	 * @param userDico dictionary of correspondences between native and internal user keys
	 * @param itemDico dictionary of correspondences between native and internal item keys
	 */
	public void runProbeNetflix (String inputFileName, 
			String outputFileName, KeyDictionary userDico, KeyDictionary itemDico ) {
		
		BufferedReader input = null;
		BufferedWriter output = null;
		BufferedReader rF = null;
		try{
			predictionTime = System.currentTimeMillis();
			defaultPredictionNumber = 0;
			testSize = 0;
			
			File d = (new File(outputFileName)).getParentFile();
			if (! d.isDirectory()) {
				d.mkdir();
			}
			input = new BufferedReader(new FileReader(inputFileName));
			output = new BufferedWriter(new FileWriter(outputFileName));
			rF = new BufferedReader(new FileReader(Parameters.netflixRMSEFile));
			String line;
			while ((line = rF.readLine()) != null) {
				output.write(line+"\n");
			}
			int itemID = 0;
		
			while ( (line = input.readLine()) != null ) {
				if (line.contains(":")) {
					StringTokenizer tok = new StringTokenizer(line,":",false);
					itemID = itemDico.getInternalKey(tok.nextToken());
				}
				else{
					int userID = userDico.getInternalKey(line);
					float prediction = getRecommendation(userID,itemID).getRating();
					prediction = Parameters.normalizeRating(prediction);
					int index = Arrays.binarySearch(getDataBase().getSparseVector(itemID).getAttributes(),userID);					
					output.write(Parameters.normalizeRating(getDataBase().getSparseVector(itemID).getValueIndex(index)) + "," + prediction + "\n");
					testSize++;
				}
			}

			predictionTime = System.currentTimeMillis() - predictionTime;
			
			if (logger.isInfoEnabled()) {
				logger.info("Probe Predictions :");
				logger.info(" * time = " + getPredictionTime() + " ms");
				logger.info(" * test size = " + getTestSize());
				logger.info(" * prediction = " + (getTestSize()-getDefaultPredictionNumber()));
				logger.info(" * default number = " + getDefaultPredictionNumber());
			}
		} catch (IOException e) {
			logger.error("HDModels - HDModel - runProbeNetflix");
			logger.error("  => IO exception when reading from file " 
					+ inputFileName + " and writing on file " + outputFileName);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) { 
					logger.error("I/O error: " + e.getMessage());
				}
			}
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) { 
					logger.error("I/O error: " + e.getMessage());
				}
			}
			if (rF != null) {
				try {
					rF.close();
				} catch (IOException e) { 
					logger.error("I/O error: " + e.getMessage());
				}
			}
		}
	}

	
	/**
	 * Print the informations associated to the model
	 */
	public void print () {
		
		int notDefinedVectorNumber = 0;
		
		float meanVectorMean = 0;
		float minVectorMean = (float)1E300;
		float maxVectorMean = 0;

		int meanVectorSize = 0;
		int minVectorSize = (int)1E300;
		int maxVectorSize = 0;

		Iterator<Integer> it = getDataBase().getVectorIDiterator();
		while (it.hasNext()) {
		
			Integer i = it.next();

			if (vectorIsDefined(i)) {

				meanVectorMean += getVectorMean(i);
				if (getVectorMean(i) < minVectorMean) {
					minVectorMean = getVectorMean(i);
				}
				if (getVectorMean(i) > maxVectorMean) {
					maxVectorMean = getVectorMean(i);
				}
				
				meanVectorSize += getVectorSize(i);
				if (getVectorSize(i) < minVectorSize) {
					minVectorSize = getVectorSize(i);
				}
				if (getVectorSize(i) > maxVectorSize) {
					maxVectorSize = getVectorSize(i);
				}
			}
			
			else{
				notDefinedVectorNumber++;
			}
		}
		
		meanVectorMean /= getDataBase().getSparseVectorNumber();
		meanVectorSize /= getDataBase().getSparseVectorNumber();
		if (logger.isInfoEnabled()) {
			logger.info(" * mean vector mean = " + meanVectorMean);
			logger.info(" * vector mean in [" + minVectorMean + "," + maxVectorMean + "]");
			logger.info(" * mean vector size = " + meanVectorSize);
			logger.info(" * vector size in [" + minVectorSize + "," + maxVectorSize + "]");
			if (notDefinedVectorNumber > 0) {
				logger.info(" * not defined vector number = " + notDefinedVectorNumber);
			}
		}
	}

	/**
	 * @return the name of the file used to store the model
	 */
	public String getDefaultModelFileName () {
		File d = new File(Parameters.engineModelDirectory);
		if (! d.isDirectory()) {
			d.mkdir();
		}
		return Parameters.engineModelDirectory + "default." + getDataBase().getVectorType();
	}
	
	/**
	 * Save the model
	 */
	public void save () {
		BufferedWriter buf = null;
		try {

			String fileName = getDefaultModelFileName();
			buf = new BufferedWriter(new FileWriter(fileName));
			
			Iterator<Integer> it = getDataBase().getVectorIDiterator();
			while (it.hasNext()) {
				Integer i = it.next();
				if (vectorIsDefined(i)) {
					buf.write(i + "\t" + getVectorMean(i) + "\t" + getVectorSize(i) + "\n");
				}
			}
		

		} catch (IOException e) {
			logger.error("HDModels - HDModel - save");
			logger.error("  => IO exception when saving default model");
		} finally {
			try {
				buf.close();
			} catch (IOException e) {
				logger.error("I/O error: " + e.getMessage());
			}
		}
	}

	/**
	 * Save the model with external keys
	 * @param dico the dictionary of correspondences between native and internal keys
	 */
	public void save (KeyDictionary dico) {
		BufferedWriter buf = null;
		try {

			String fileName = getDefaultModelFileName();
			buf = new BufferedWriter(new FileWriter(fileName));
			
			Iterator<Integer> it = getDataBase().getVectorIDiterator();
			while (it.hasNext()) {
				Integer i = it.next();
				if (vectorIsDefined(i)) {
					buf.write(dico.getExternalKey(i) + "\t" + getVectorMean(i) + "\t" + getVectorSize(i) + "\n");
				}
			}
		} catch (IOException e) {
			logger.error("HDModels - HDModel - save");
			logger.error("  => IO exception when saving default model");
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
	 * @param buf the buffer used to store the informations
	 */
	public void saveInfos (BufferedWriter buf) {
		
		int notDefinedVectorNumber = 0;
		
		float meanVectorMean = 0;
		float minVectorMean = (float)1E300;
		float maxVectorMean = 0;

		int meanVectorSize = 0;
		int minVectorSize = (int)1E300;
		int maxVectorSize = 0;

		Iterator<Integer> it = getDataBase().getVectorIDiterator();
		while (it.hasNext()) {
		
			Integer i = it.next();

			if (vectorIsDefined(i)) {

				meanVectorMean += getVectorMean(i);
				if (getVectorMean(i) < minVectorMean) {
					minVectorMean = getVectorMean(i);
				}
				if (getVectorMean(i) > maxVectorMean) {
					maxVectorMean = getVectorMean(i);
				}
				
				meanVectorSize += getVectorSize(i);
				if (getVectorSize(i) < minVectorSize) {
					minVectorSize = getVectorSize(i);
				}
				if (getVectorSize(i) > maxVectorSize) {
					maxVectorSize = getVectorSize(i);
				}
			}
			
			else{
				notDefinedVectorNumber++;
			}
		}
		
		meanVectorMean /= getDataBase().getSparseVectorNumber();
		meanVectorSize /= getDataBase().getSparseVectorNumber();
		
		try{

			buf.write(meanVectorMean + " mean vector mean\n");
			buf.write(minVectorMean + " min vector mean\n");
			buf.write(maxVectorMean + " max vector mean\n");
			buf.write(meanVectorSize + " mean vector size\n");
			buf.write(minVectorSize + " min vector size\n");
			buf.write(maxVectorSize + " max vector size\n");
			if (notDefinedVectorNumber > 0) {
				buf.write(notDefinedVectorNumber + " not defined vector number\n");
			}

		} catch (IOException e) {
			logger.error("HDModels - HDModel - saveInfos");
			logger.error("  => IO exception when saving default information");
		}
	}

	/**
	 * Load the model
	 */
	public void load () {
		BufferedReader buf = null;
		try {

			String fileName = getDefaultModelFileName();
			buf = new BufferedReader(new FileReader(fileName));
			
			String line;
			while ( (line = buf.readLine()) != null ) {
				StringTokenizer tok = new StringTokenizer(line,"\t",false);
				Integer i = new Integer(tok.nextToken());
				Float m = new Float(tok.nextToken());
				setVectorMean(i,m);
				Integer s = new Integer(tok.nextToken());
				setVectorSize(i,s);
			}

		} catch (IOException e) {
			logger.error("HDModels - HDModel - load");
			logger.error("  => IO exception when loading default model");
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
	 * @param dico the dictionary of correspondences between native and internal keys
	 */
	public void load (KeyDictionary dico) {
		BufferedReader buf = null;
		try {

			String fileName = getDefaultModelFileName();
			buf = new BufferedReader(new FileReader(fileName));
			
			String line;
			while ( (line = buf.readLine()) != null ) {
				StringTokenizer tok = new StringTokenizer(line,"\t",false);
				Integer i = dico.getInternalKey(tok.nextToken());
				Float m = new Float(tok.nextToken());
				setVectorMean(i,m);
				Integer s = new Integer(tok.nextToken());
				setVectorSize(i,s);
			}

		} catch (IOException e) {
			logger.error("HDModels - HDModel - load");
			logger.error("  => IO exception when loading default model");
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
