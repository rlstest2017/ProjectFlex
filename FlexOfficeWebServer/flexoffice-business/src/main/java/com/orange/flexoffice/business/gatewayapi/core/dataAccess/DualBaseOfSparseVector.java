package com.orange.flexoffice.business.gatewayapi.core.dataAccess;

import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * 
 * DualBaseOfSparseVector : dual database of sparse vector
 * 
 * @author Laurent Candillier
 * 
 * @version 7.0 (07/01/2009)
 * 
 * 
 * ENGLISH VERSION
 * 
 * 
 * Contains 2 databases users and items and their dictionaries
 * 
 * Use the method loadFromSQL() to load it from the SQL database
 * 
 * 
 * VERSION FRANCAISE
 * 
 * 
 * Contient 2 bases de donn�es users et items et leurs dictionnaires
 * 
 * Utiliser la proc�dure loadFromSQL() pour la charger depuis la base de donn�es SQL
 * 
 */

public class DualBaseOfSparseVector {
	private final Logger logger = Logger.getLogger(DualBaseOfSparseVector.class);
	
	/**
	 * A DualBaseOfSparseVector contains
	 * a dictionary of user identifiers,
	 * a dictionary of item identifiers,
	 * a database of users,
	 * a database of items,
	 * and a loading time
	 */
	private KeyDictionary userDictionary;
	private KeyDictionary itemDictionary;
	private BaseOfSparseVector userBase;
	private BaseOfSparseVector itemBase;
	private long loadTime;
	
	/**
	 * The constructor initializes the parameters
	 */
	public DualBaseOfSparseVector() {
		userDictionary = new KeyDictionary();
		itemDictionary = new KeyDictionary();
		userBase = new BaseOfSparseVector("user");
		itemBase = new BaseOfSparseVector("item");
		loadTime = 0;
	}

	/**
	 * @return the dictionary of users identifiers
	 */
	public KeyDictionary getUserDictionary () {
		return userDictionary;
	}

	/**
	 * @return the dictionary of items identifiers
	 */
	public KeyDictionary getItemDictionary () {
		return itemDictionary;
	}

	/**
	 * @param id a user external identifier
	 * @return the corresponding internal identifier
	 */
	public Integer getUserInternalKey (String id) {
		return userDictionary.getInternalKey(id);
	}
	
	/**
	 * @param id an item external identifier
	 * @return the corresponding internal identifier
	 */
	public Integer getItemInternalKey (String id) {
		return itemDictionary.getInternalKey(id);
	}
	
	/**
	 * @param i a user internal identifier
	 * @return the corresponding external identifier
	 */
	public String getUserExternalKey (Integer i) {
		return userDictionary.getExternalKey(i);
	}
	
	/**
	 * @param i an item internal identifier
	 * @return the corresponding external identifier
	 */
	public String getItemExternalKey (Integer i) {
		return itemDictionary.getExternalKey(i);
	}
	
	/**
	 * @return the users database
	 */
	public BaseOfSparseVector getUserBase () {
		return userBase;
	}

	/**
	 * @return the items database
	 */
	public BaseOfSparseVector getItemBase () {
		return itemBase;
	}

	/**
	 * @return the number of users
	 */
	public int getUserNumber () {
		return userBase.getSparseVectorNumber();
	}

	/**
	 * @return the number of items
	 */
	public int getItemNumber () {
		return itemBase.getSparseVectorNumber();
	}

	/**
	 * @return an iterator on the users identifiers
	 */
	public Iterator<Integer> getUsersIterator () {
		return userBase.getVectorIDiterator();
	}
	
	/**
	 * @return an iterator on the items identifiers
	 */
	public Iterator<Integer> getItemsIterator () {
		return itemBase.getVectorIDiterator();
	}
	
	/**
	 * @param i a user identifier
	 * @return the corresponding user vector
	 */
	public SparseVectorTabular getUser (Integer i) {
		return userBase.getSparseVector(i);
	}
	
	/**
	 * @param i an item identifier
	 * @return the corresponding item vector
	 */
	public SparseVectorTabular getItem (Integer i) {
		return itemBase.getSparseVector(i);
	}

	/**
	 * @param i a user identifier
	 * @return the existence of the corresponding user vector
	 */
	public boolean hasUser (Integer i) {
		return userBase.hasSparseVector(i);
	}
	
	/**
	 * @param i an item identifier
	 * @return the existence of the corresponding item vector
	 */
	public boolean hasItem (Integer i) {
		return itemBase.hasSparseVector(i);
	}

	/**
	 * Add a user to the database
	 * @param i the user identifier
	 * @param v the user vector
	 */
	public void addUser (Integer i, SparseVectorTabular v) {
		userBase.addSparseVector(i,v);
	}
	
	/**
	 * Add an item to the database
	 * @param i the item identifier
	 * @param v the item vector
	 */
	public void addItem (Integer i, SparseVectorTabular v) {
		itemBase.addSparseVector(i,v);
	}
	
	/**
	 * @return the number of values in the database
	 */
	public int getValueNumber () {
		int r = userBase.getValueNumber();
		if (r==0)
			r = itemBase.getValueNumber();
		return r;
	}
	
	/**
	 * @return the loading time of the dual database
	 */
	public long getLoadTime () {
		return loadTime;
	}
	
	/**
	 * Specify the loading time of the dual database
	 * @param t the loading time of the dual database
	 */
	public void setLoadTime (long t) {
		loadTime = t;
	}
	
	/**
	 * Print informations
	 */
	public void print () {
		if (logger.isInfoEnabled()) {
			logger.info("DualBaseOfSparseVector");
			logger.info(" * " + getLoadTime() + " ms to load");
			logger.info(" * " + getUserNumber() + " users");
			logger.info(" * " + getItemNumber() + " items");
			logger.info(" * " + getValueNumber() + " ratings");
		}
	}
}
