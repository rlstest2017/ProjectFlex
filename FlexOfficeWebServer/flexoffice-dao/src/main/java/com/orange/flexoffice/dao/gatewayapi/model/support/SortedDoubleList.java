package com.orange.flexoffice.dao.gatewayapi.model.support;

import java.util.ArrayList;

public class SortedDoubleList<T> {

	private ArrayList<Float> values;
	private ArrayList<T> ids;
	private int maxArraySize;

	public SortedDoubleList(int maxSize) {
		if (values == null) {
			values = new ArrayList<Float>();
			ids = new ArrayList<T>();
		} else {
			values.clear();
			ids.clear();
		}
		maxArraySize = maxSize;
	}

	/**
	 * Sub-method used for recommendation and item-items
	 * 
	 * @param id
	 * @param value
	 */
	public void add(T id, float value) {
		int pos = 0;
		while (pos < values.size() && value < values.get(pos))
			pos++;
		if (pos < maxArraySize) {
			values.add(pos, value);
			ids.add(pos, id);
			if (values.size() > maxArraySize) {
				values.remove(maxArraySize);
				ids.remove(maxArraySize);
			}
		}
	}

	public T getIdAt(int position) {
		if (position > -1 && position < ids.size()) {
			return ids.get(position);
		} else {
			return null;
		}
	}

	public float getValueAt(int position) {
		if (position > -1 && position < values.size()) {
			return values.get(position);
		} else {
			return 0f;
		}
	}

	public int size() {
		return ids.size();
	}

	public ArrayList<T> getIds() {
		return ids;
	}

}
