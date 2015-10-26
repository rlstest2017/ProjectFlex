package com.orange.flexoffice.business.gatewayapi.core.dataAccess;

import java.util.TreeMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * 
 * SparseVectorSorted : sparse vector sorted in ascending order of its attributes
 * 
 * @author Laurent Candillier
 * 
 * @version 7.0 (07/01/2009)
 * 
 * 
 * ENGLISH VERSION
 * 
 * 
 * A sparse vector is represented by a TreeMap whose keys are integer identifiers
 * sorted in ascending order, and whose values are floats
 * 
 * 
 * VERSION FRANCAISE
 * 
 * 
 * Un vecteur creux est repr�sent� par une table de hachage
 * dont les cl�s sont des identifiants entiers, tri�s par ordre croissant,
 * et les valeurs sont des r�els
 * 
 */

public class SparseVectorSorted {
	private final Logger logger = Logger.getLogger(SparseVectorSorted.class);

	/** 
	 * A sparse vector is represented by a TreeMap whose keys are integer identifiers
	 * sorted in ascending order, and whose values are floats
	 * Its mean value is also given
	 */
	private TreeMap<Integer,Float> map;
	private float mean;
	
	/**
	 * The constructor initializes the TreeMap and mean value
	 */
	public SparseVectorSorted () {
		map = new TreeMap<Integer,Float>();
		mean = 0;
	}

	/**
	 * Load vector from a SparseVectorTabular
	 * @param v the given SparseVectorTabular
	 */
	public void loadFromSparseVectorTabular (SparseVectorTabular v) {
		for (int index=0 ; index<v.getSize() ; index++) {
			setValue(v.getAttributeIndex(index) , v.getValueIndex(index));
		}
	}
	
	/**
	 * @return the vector size
	 */
	public int getSize () {
		return map.size();
	}
	
	/**
	 * @param i an attribute identifier
	 * @return true if a value exists for the identifier
	 */
	public boolean hasValue (Integer i) {
		return map.containsKey(i);
	}
	
	/**
	 * @param i an attribute identifier
	 * @return the value for the identifier, or stop execution
	 */
	public float getValue (Integer i) {
		Float f = map.get(i);
		if (f == null) {
			if (logger.isInfoEnabled()) {
				logger.info("DataAccess - SparseVectorSorted - getValue(" + i + ")");
				logger.info("  => no vector value for ID " + i);
			}
			return 0;
		}
		else{
			return f;
		}
	}
	
	/**
	 * @return the mean value of the vector
	 */
	public float getMean () {
		return mean;
	}
	
	/**
	 * Add a value to the vector and update its mean value
	 * @param i an attribute identifier
	 * @param f the new value
	 */
	public void setValue (Integer i, float f) {
		if (hasValue(i)) {
			mean = (mean * getSize() + f - getValue(i)) / getSize();
		}
		else{
			mean = ( mean * getSize() + f ) / ( getSize() + 1 );
		}
		map.put(i,f);
	}
	
	/**
	 * @return an iterator on the vector attributes
	 */
	public Iterator<Integer> getAttributesIterator () {
		return map.keySet().iterator();
	}
	
	/**
	 * Print the vector
	 */
	public void print () {
		if (logger.isInfoEnabled()) {
			logger.info("Mean\t" + getMean());
		}
		Iterator<Integer> it = getAttributesIterator();
		while (it.hasNext()) {
			Integer i = it.next();
			if (logger.isInfoEnabled()) {
				logger.info(i + "\t" + getValue(i));
			}
		}
	}
}
