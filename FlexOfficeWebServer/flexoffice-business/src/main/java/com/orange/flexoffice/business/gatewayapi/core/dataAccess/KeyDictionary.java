package com.orange.flexoffice.business.gatewayapi.core.dataAccess;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * 
 * KeyDictionary : dictionary of correspondences between internal and external keys
 * 
 * @author Laurent Candillier
 * 
 * @version 7.0 (07/01/2009)
 * 
 * 
 * ENGLISH VERSION
 * 
 * 
 * This class associates an external key of type string with an internal integer key
 * 
 * Keys given in input are cleaned by the method getCleanKey()
 * 
 * Keys in output are distributed sequentially
 * 
 * 
 * VERSION FRANCAISE
 * 
 * 
 * Cette classe associe une clé externe de type chaine de caractères avec une clé interne entière
 * 
 * Les clés en entrées sont nettoyées avec la méthode getCleanKey()
 * 
 * Les clés en sortie sont distribuées séquentiellement
 * 
 */

public class KeyDictionary {
	private final Logger logger = Logger.getLogger(KeyDictionary.class);
	
	/**
	 * The key dictionary is implemented by 2 HashMap
	 */
	private HashMap<String,Integer> map;
	private HashMap<Integer,String> reverse;
		
	/**
	 * The constructor initializes the 2 HashMap
	 */
	public KeyDictionary () {
		map = new HashMap<String,Integer>();
		reverse = new HashMap<Integer,String>();
	}

	/**
	 * @return the number of keys in the dictionary
	 */
	public int getKeyNumber () {
		return map.size();
	}

	/**
	 * @return an iterator on the external keys of the dictionary
	 */
	public Iterator<String> getExternalKeyIterator () {
		return map.keySet().iterator();
	}

	/**
	 * @return an iterator on the internal keys of the dictionary
	 */
	public Iterator<Integer> getInternalKeyIterator () {
		return map.values().iterator();
	}

	/**
	 * @param s an external key
	 * @return true if the external key exists in the dictionary
	 */
	public boolean hasInternalKey (String s) {
		return map.containsKey(s);
	}
	
	/**
	 * @param i an internal key
	 * @return true if the internal key exists in the dictionary
	 */
	public boolean hasExternalKey (Integer i) {
		return map.containsValue(i);
	}
	
	/**
	 * Add a key to the dictionary
	 * @param s the external key
	 * @param i the internal key
	 */
	public void addInternalKey (String s, Integer i) {
		map.put(s,i);
		reverse.put(i,s);
	}
	
	/**
	 * @param key an external key
	 * @return the corresponding internal key as integer
	 */
	public int getInternalKey (String key) {
		// clean the key
		String s = getCleanKey(key);
		Integer k = map.get(s);
		// create a new internal key if necessary (first key is 0)
		if (k == null) {
			k = map.size();
			addInternalKey(s,k);
		}
		// return the internal key
		return k;
	}
	
	/**
	 * @param key an external key
	 * @return the cleaned key
	 */
	public String getCleanKey (String key) {
		String result = key;
		result = result.replaceAll("\t"," ");
		result = result.replaceAll("\n"," ");
		result = result.replaceAll("\r"," ");
		while (result.indexOf("  ") >= 0) {
			result = result.replaceAll("  "," ");
		}
		result = result.trim();
		return result;
	}
	
	/**
	 * @param i an internal key
	 * @return the corresponding external key, or stop execution
	 */
	public String getExternalKey (Integer i) {
		String s = reverse.get(i);
		if (s == null) {
			logger.error("DataAccess - KeyDictionary - getExternalKey(" + i + ")");
			logger.error("  => no external key exists for integer " + i);
		}
		return s;
	}
	
}
