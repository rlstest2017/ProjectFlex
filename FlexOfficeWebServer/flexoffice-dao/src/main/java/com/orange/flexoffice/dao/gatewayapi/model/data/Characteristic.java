package com.orange.flexoffice.dao.gatewayapi.model.data;


/**
 * A characteristic describes a relation between an item and a descriptor.
 * 
 * @author Guillaume Mouricou
 *
 */
public class Characteristic extends AbstractData {
	
	/**
	 * An item ID.
	 */
	private String itemId;
	/**
	 * A descriptor ID.
	 */
	private String descriptorId;
	/**
	 * A weight.
	 */
	private Float weight;
	
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getDescriptorId() {
		return descriptorId;
	}
	public void setDescriptorId(String descriptorId) {
		this.descriptorId = descriptorId;
	}
	public Float getWeight() {
		return weight;
	}
	public void setWeight(Float weight) {
		this.weight = weight;
	}
	
	
	@Override
	public void setColumnId(String columnId) {
		setItemId(columnId);
	}
	@Override
	public String getColumnId() {
		return getItemId();
	}
	
	@Override
	public void setRowId(String rowId) {
		setDescriptorId(rowId);		
	}
	@Override
	public String getRowId() {
		return getDescriptorId();
	}

	@Override
	public void setRating(Float rating) {
		setWeight(rating);
	}
	@Override
	public Float getRating() {
		return getWeight();
	}
	
}
