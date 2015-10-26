package com.orange.flexoffice.dao.gatewayapi.model.support;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.orange.flexoffice.dao.gatewayapi.model.support.SparseVector;



/**
 * This class provides methods to process cache items in fifo mode
 * 
 * @author Losquin Patrick
 */

public class Cache {

	private int cacheDepth;
	private Map<String, SparseVector> listSV;

	/**
	 * Allows to create a cache to store <code>SparseVector</code>
	 * 
	 * @param length the cache size.
	 */
	public Cache(int length) {
		cacheDepth = length;
		listSV = new LinkedHashMap<String, SparseVector>(cacheDepth + 1, .75F, true) {
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean removeEldestEntry(Entry<String, SparseVector> eldest) {
				return size() > cacheDepth;
			}

		};

	}

	/**
	 * Returns the cached <code>SparseVector</code> associated to the specified id.
	 * 
	 * @param id an object id
	 * @return A <code>SparseVector</code> or <code>null</code> if no data is cached for this id.
	 */
	public SparseVector getSV(String id) {
		return listSV.get(id);
	}

	/**
	 * Caches a <code>SparseVector</code>. 
	 * In the cache, the <code>SparseVector</code> is linked to the specified object id.
	 * This id is useful to retrieve the <code>SparseVector</code> later.
	 * 
	 * @param id an object id
	 * @param sv a <code>sparseVector</code>
	 */
	public void setSV(String id, SparseVector sv) {
		listSV.put(id, sv);
	}

	/**
	 * Returns true if a <code>SparseVector</code> is cached for the specified id.
	 * 
	 * @param id
	 * @return True, if a SparseVector is cached for this id.
	 */
	public boolean isCached(String id) {
		return listSV.containsKey(id);
	}

}
