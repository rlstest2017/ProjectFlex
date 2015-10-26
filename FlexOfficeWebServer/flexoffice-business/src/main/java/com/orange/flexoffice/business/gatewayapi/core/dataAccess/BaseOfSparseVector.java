package com.orange.flexoffice.business.gatewayapi.core.dataAccess;

import java.util.HashSet;
import java.util.Iterator;

import org.apache.log4j.Logger;


/**
 * BaseOfSparseVector : database of sparse vector
 * 
 * @author Laurent Candillier
 * 
 * 
 * 
 * @version 8.0 August 2011, Frank Meyer
 * ADD the possibilty to insert in a BaseOfSparseVector directly the logs, one by one
 * 
 * 
 * 
 * 
 * @version 7.0 (07/01/2009)
 * 
 * 
 * ENGLISH VERSION
 * 
 * 
 * This class allows to handle a database of sparse vectors
 * 
 * During the construction of the database, specify the type of vector handled (item or user)
 * 
 * To create a database
 *  1) execute the constructor with the type of vector handled : 
 *  	new BaseOfSparseVector(String)
 *  2) initialize the data table by specifying the max number of vectors
 *  	in the database : initializeDataset(int)
 *  3) add the vectors one by one, and store the identifier list
 *  	with the method addSparseVector(int,SparseVectorTabular)
 *  
 * 
 * VERSION FRANCAISE
 * 
 * 
 * Cette classe permet de g�rer une base de vecteurs creux
 * 
 * Lors de la construction de la base, sp�cifier le type de vecteur trait� (item ou user)
 * 
 * Pour cr�er une base de donn�es
 *  1) lancer le constructeur avec type de vecteur trait� : 
 *  	new BaseOfSparseVector(String)
 *  2) initialiser le tableau des donn�es en sp�cifiant le nombre max 
 *  	de vecteurs de la base : initializeDataset(int)
 *  3) ajouter les vecteurs un � un, et stocker la liste des identifiants
 *  	avec la m�thode addSparseVector(int,SparseVectorTabular)
 *  
 */

public class BaseOfSparseVector {
	private final Logger logger = Logger.getLogger(BaseOfSparseVector.class);
	
	/**
	 * We associate to a BaseOfSparseVector 
	 * a type of vector (item or user),
	 * a boolean specifying the type of database (true for item and false for user),
	 * the max identifiers of the vectors and attributes,
	 * the total number of values in the database,
	 * the set of identifiers of the vectors considered,
	 * and the set of vectors considered stored in RAM
	 */
	private String vectorType;
	private boolean isVectorOfItemType;
	private int maxVectorID;
	private int maxAttributeID;
	private HashSet<Integer> vectorIDset;
	private SparseVectorTabular[] dataset;
	
	/**
	 * The constructor stores the type of vector handled and initializes the parameters
	 * @param vectType the type of vector handled (item or user)
	 */
	public BaseOfSparseVector (String vectType) {
		setVectorType(vectType);
		setMaxVectorID(0);
		setMaxAttributeID(0);
		vectorIDset = new HashSet<Integer>();
	}
	
	/**
	 * Initialize the table of data
	 * @param vectorNumber the number of vectors in the database
	 */
	public void initializeDataset (int vectorNumber) {
		dataset = new SparseVectorTabular[vectorNumber];
	}
	
	/**
	 * @return the type of vector in the database (item or user)
	 */
	public String getVectorType () {
		return vectorType;
	}

	/**
	 * Specify the type of vector in the database (item or user)
	 * @param vectType the type of vector in the database
	 */
	public void setVectorType (String vectType) {
		if (vectType.equals("item")) {
			isVectorOfItemType = true;
		}
		else if (vectType.equals("user")) {
			isVectorOfItemType = false;
		}
		else{
			if (logger.isInfoEnabled()) {
				logger.info("DataAccess - BaseOfSparseVector - setVectorType(" + vectType + ")");
				logger.info("  => vector type is not of type item nor of type user");
			}
		}
		vectorType = vectType;
	}
	
	/**
	 * @return true if the database is of item type
	 */
	public boolean isVectorOfItemType () {
		return isVectorOfItemType;
	}
	
	/**
	 * @return true if the database is of user type
	 */
	public boolean isVectorOfUserType () {
		return ! isVectorOfItemType;
	}
	
	/**
	 * @return the max identifier of the vectors
	 */
	public int getMaxVectorID () {
		return maxVectorID;
	}

	/**
	 * Specify the max identifier of the vectors
	 * @param m the max identifier of the vectors
	 */
	public void setMaxVectorID (int m) {
		maxVectorID = m;
	}
	
	/**
	 * @return the max identifier of the attributes
	 */
	public int getMaxAttributeID () {
		return maxAttributeID;
	}

	/**
	 * Specify the max identifier of the attributes
	 * @param m the max identifier of the attributes
	 */
	public void setMaxAttributeID (int m) {
		maxAttributeID = m;
	}
	
	/**
	 * @return the total number of values in the database
	 * 
	 * MODIFICATION of August 2011
	 */
	public int getValueNumber () {
		//old code: return valueNumber
		int count=0;
		for (int i=0; i<this.dataset.length;i++) {
			SparseVectorTabular s=this.dataset[i];
			if (s!=null) {
				count=count+s.getSize();
			}
		}
		return count;
	}

	/**
	 * @return the set of identifiers of the vectors in the database
	 */
	public HashSet<Integer> getVectorIDset () {
		return vectorIDset;
	}

	/**
	 * Specify the set of identifiers of vectors to be considered
	 * @param set the set of identifiers of vectors to be considered
	 */
	public void setVectorIDset (HashSet<Integer> set) {
		vectorIDset = set;
	}

	/**
	 * Add a vector identifier
	 * @param i the vector identifier
	 */
	public void addVectorID (Integer i) {
		getVectorIDset().add(i);
	}
	
	/**
	 * @return the number of vectors considered
	 */
	public int getSparseVectorNumber () {
		return getVectorIDset().size();
	}

	/**
	 * @return an iterator on the identifiers of vectors considered
	 */
	public Iterator<Integer> getVectorIDiterator () {
		return getVectorIDset().iterator();
	}
	
	/**
	 * @param i a vector identifier
	 * @return true if the vector exists
	 */
	public boolean hasSparseVector (Integer i) {
		return getVectorIDset().contains(i);
	}

	/**
	 * Add a vector to the database
	 * @param id the vector identifier
	 * @param v the sparse vector to be added
	 */
	public void addSparseVector (Integer i, SparseVectorTabular v) {
		if (i < dataset.length) {
			dataset[i] = v;
		}
		else{
			if (logger.isInfoEnabled()) {
				logger.info("DataAccess - BaseOfSparseVector - addSparseVector(" + i + ")");
				logger.info("  => out of bounds exception : " + i + " >= " + dataset.length);
			}
		}
		addVectorID(i);
		if (i > getMaxVectorID()) {
			setMaxVectorID(i);
		}
		if ((v.getSize() > 0) && (v.getAttributeIndex(v.getSize()-1) > getMaxAttributeID())) {
			setMaxAttributeID(v.getAttributeIndex(v.getSize()-1));
		}
		//setValueNumber(getValueNumber() + v.getSize());  OBSOLETE AFTER changes of August 2011
	}
	
	/**
	 * @param i a vector identifier
	 * @return the corresponding sparse vector
	 */
	public SparseVectorTabular getSparseVector (Integer i) {
		if (i < dataset.length) {
			return dataset[i];
		}
		else{
			System.out.println("DataAccess - BaseOfSparseVector - getSparseVector(" + i + ")");
			System.out.println("  => out of bounds exception : " + i + " >= " + dataset.length);
			return new SparseVectorTabular(0);
		}
	}
	
	
	
	/**
	 * @param i a vector identifier
	 * @return the corresponding sparse vector, or null if this vector does not exist
	 */
	public SparseVectorTabular getSparseVectorWithoutError (Integer i) {
		if (i < dataset.length) {
			return dataset[i];
		}
		else{
			return null;
		}
	}
	
	
	
	
	
	/**
	 * @return a vector identifier selected randomly
	 */
	public Integer getRandomVectorID () {
		Integer i = (int)(Math.random() * (getMaxVectorID() + 1));
		while (! hasSparseVector(i)) {
			i = (int)(Math.random() * (getMaxVectorID() + 1));
		}
		return i;
	}
	
	/**
	 * @param u vector 1
	 * @param v vector 2
	 * @return the similarity between the 2 vectors
	 * corresponds to the mean of similarities of jaccard and weighted pearson 
	 */
	public float getSimilarityMix (SparseVectorTabular u, SparseVectorTabular v) {
		
		// x is the vector of minimal size
		SparseVectorTabular x;
		SparseVectorTabular y;
		if (u.getSize() < v.getSize()) {
			x = u;
			y = v;
		}
		else{
			x = v;
			y = u;
		}
		
		// compute the similarity
		int count = 0;
		float f = 0;
		float s1 = 0;
		float s2 = 0;
		int index2 = 0;
		for (int index=0 ; index<x.getSize() ; index++) {
			int i = x.getAttributeIndex(index);
			while ( (index2 < y.getSize()) && (y.getAttributeIndex(index2) < i) ) {
				index2++;
			}
			if (index2 < y.getSize()) {
				if (y.getAttributeIndex(index2) == i) {
					count++;
					f += (x.getValueIndex(index) - x.getMean()) 
						* (y.getValueIndex(index2) - y.getMean());
					s1 += Math.pow(x.getValueIndex(index) - x.getMean() , 2);
					s2 += Math.pow(y.getValueIndex(index2) - y.getMean() , 2);
				}
			}
			else{
				index = x.getSize();
			}
		}
		
		// return
		float jaccard = (float)count / (x.getSize() + y.getSize() - count);
		if ( (s1 > 0) && (s2 > 0) ) {
			float pearson = f / (float)Math.sqrt(s1 * s2);
			return jaccard * (1 + pearson) / 2;
		}
		else{
			return jaccard / 2;
		}
	}
	
	/**
	 * ADD directly logs to the Base
	 * 
	 * You MUST use postSortAllVector() at the end of the importation if you don't use postSorting
	 * 
	 * @param itemID
	 * @param userID
	 * @param value
	 * @postSorting : true : do the sorting of the impacted vector, false, just add (very faster)
	 */
	
	public void addAsAlog(int itemID, int userID, float value, boolean postSorting) {
		
		
		//System.out.println("adding itemID:"+itemID+"  userID:"+userID+"   value="+value);
		
		SparseVectorTabular myVector;  // sert de tampon pour ajouter 1 log
											// use as buffer to add 1 log
		
		
		
		myVector=this.getSparseVectorWithoutError(itemID);  // return null if it does not exist, otherwise the current vector 
		
		if (myVector==null) {      // CAS 1: new vector to create with 1 first log
			myVector=new SparseVectorTabular(100);
			this.addSparseVector(itemID, myVector);
		}	
		
		myVector.setValueWithoutError(userID, value);
		
		
		//System.out.println("vector:");
		//myVector.printLine();


		if (postSorting) {
			myVector=this.postSortVector(myVector);
			myVector.printLine();
			this.addSparseVector(itemID, myVector);
		}
		
	}
	
	public SparseVectorTabular postSortVector(SparseVectorTabular s) {
		if (s==null) {
			if (logger.isInfoEnabled()) {
				logger.info("warning, some items are null in the BaseOfSparseVector");
			}
			s=new SparseVectorTabular(1);
		}
		SparseVectorSorted v=new SparseVectorSorted();
		v.loadFromSparseVectorTabular(s);
		SparseVectorTabular w=new SparseVectorTabular(s.getSize());
		w.loadFromSparseVectorSorted(v);
		return w;
	}
	
	public void postSortAllVector() {
		if (logger.isInfoEnabled()) {
			logger.info("Sorting, number of sparse vector:"+this.getSparseVectorNumber());
		}
		for (int i=0;i<this.getSparseVectorNumber();i++) {
			this.dataset[i]=this.postSortVector(this.dataset[i]);
			if (i%1000==0 && logger.isInfoEnabled()) {
				logger.info("sorting, current item:"+i);
			}
		}
	}
	
	public void print() {
		for (int i=0;i<=this.getMaxVectorID();i++) {
			SparseVectorTabular s=this.getSparseVector(i);
			System.out.print("row:\t"+i+":\t");
			if (s!=null) {
				s.printLine();
			}
		}
	}
	
}
