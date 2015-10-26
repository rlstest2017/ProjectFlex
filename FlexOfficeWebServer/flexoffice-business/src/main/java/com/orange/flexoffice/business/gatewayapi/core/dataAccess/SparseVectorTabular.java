package com.orange.flexoffice.business.gatewayapi.core.dataAccess;

import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * 
 * SparseVectorTabular : sparse vector represented by tables
 * 
 * @author Laurent Candillier
 * 
 * @version 7.0 (07/01/2009)
 * 
 * 
 * ENGLISH VERSION
 * 
 * 
 * A sparse vector is represented by 2 tables :
 * one for the attribute identifiers and another one for the corresponding values
 * 
 * 
 * VERSION FRANCAISE
 * 
 * 
 * Un vecteur creux est repr�sent� par 2 tableaux :
 * l'un pour les identifiants d'attributs et l'autre pour les valeurs correspondantes
 * 
 */

public class SparseVectorTabular {
	
	private final Logger logger = Logger.getLogger(SparseVectorTabular.class);

	/** 
	 * A sparse vector is represented by 2 tables :
	 * one for the attribute identifiers and another one for the corresponding values
	 * Its maximal size, current size and mean rating are also given
	 */
	private int[] attributes;
	private float[] values;
	private int maxSize;
	private int size;
	private float mean;
	
	/**
	 * The constructor initializes the tables and associated parameters
	 * @param s the vector size
	 */
	public SparseVectorTabular (int s) {
		attributes = new int[s];
		values = new float[s];
		maxSize = s;
		size = 0;
		mean = 0;
	}
	
	/**
	 * Load from a SparseVectorSorted
	 * @param v the SparseVectorSorted
	 */
	public void loadFromSparseVectorSorted (SparseVectorSorted v) {
		size = 0;
		mean = 0;
		Iterator<Integer> it = v.getAttributesIterator();
		while (it.hasNext()) {
			Integer i = it.next();
			setValue(i,v.getValue(i));
		}
	}
	
	/**
	 * @return the vector size
	 */
	public int getSize () {
		return size;
	}
	
	/**
	 * @return the mean rating value
	 */
	public float getMean () {
		return mean;
	}
	
	/**
	 * @param index an index lower than getSize()
	 * @return the corresponding attribute identifier
	 */
	public int getAttributeIndex (int index) {
		if (index < getSize()) {
			return attributes[index];
		}
		else{
			if (logger.isInfoEnabled()) {
				logger.info("DataAccess - SparseVectorTabular - getAttributeIndex(" + index + ")");
				logger.info("  => out of bounds exception : " + index + " >= " + getSize());
			}
			return 0;
		}
	}
	/**
	 * 
	 * @return the ids array
	 */
	public int[] getAttributes() {
		return attributes;
	}
	
	/**
	 * @param index an index lower than getSize()
	 * @return the corresponding value
	 */
	public float getValueIndex (int index) {
		if (index < getSize()) {
			return values[index];
		}
		else{
			if (logger.isInfoEnabled()) {
				logger.info("DataAccess - SparseVectorTabular - getValueIndex(" + index + ")");
				logger.info("  => out of bounds exception : " + index + " >= " + getSize());
			}
			return 0;
		}
	}
	
	/**
	 * Add a value to the vector
	 * @param i an attribute identifier
	 * @param f the new value
	 */
	public void setValue (int i, float f) {
		if (size < maxSize) {
			attributes[size] = i;
			values[size] = f;
			mean = ( mean * size + f ) / ( size + 1 );
			size++;
		}
		else{
			if (logger.isInfoEnabled()) {
				logger.info("DataAccess - SparseVectorTabular - setValue(" + i + "," + f + ")");
				logger.info("  => out of bounds exception : " + size + " >= " + maxSize);
			}
		}
	}
	
	
	/**
	 * Add a value to the vector, with Pagination automatic adding
	 * @param i an attribute identifier
	 * @param f the new value
	 */
	public void setValueWithoutError (int i, float f) {
		if (size < maxSize) {
			attributes[size] = i;
			values[size] = f;
			mean = ( mean * size + f ) / ( size + 1 );
			size++;
		}
		else{
			int addSize=size+100;
			float[] addValues=new float[addSize];
			int[] addAttributes=new int[addSize];
			for (int k=0;k<addSize;k++) {
				if (k<size) {
					addValues[k]=values[k];
					addAttributes[k]=attributes[k];
				} else {
					addAttributes[k]=-1;
				}
			}
			values=addValues;
			attributes=addAttributes;
			maxSize=addSize;
			
			attributes[size] = i;
			values[size] = f;
			mean = ( mean * size + f ) / ( size + 1 );
			size++;
			
		}
	}
	
	/**
	 * Print the vector
	 */
	public void print () {
		logger.info("Mean\t" + getMean());
		for (int index=0 ; index<getSize() ; index++) {
			logger.info(getAttributeIndex(index) + "\t" + getValueIndex(index));
		}
	}
	
	
	/**
	 * Print the vector in one line
	 */
	public void printLine () {
		
		for (int index=0 ; index<getSize() ; index++) {
			logger.info("("+getAttributeIndex(index) + "," + getValueIndex(index)+")"+"\t");
		}
		logger.info("Mean:\t" + getMean());
	}
	
	
	
}
