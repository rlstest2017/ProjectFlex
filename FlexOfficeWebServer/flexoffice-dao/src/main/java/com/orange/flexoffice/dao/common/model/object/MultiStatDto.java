package com.orange.flexoffice.dao.common.model.object;

import java.util.ArrayList;
import java.util.List;

/**
 * MultiStatDto
 * @author oab
 *
 */
public class MultiStatDto {
	
	private String label;
	private List<String> values;
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return the values
	 */
	public List<String> getValues() {
		if (values == null) {
			values = new ArrayList<String>();
        }
		return this.values;
	}	
	/**
	 * @param values the values to set
	 */
	public void setValues(List<String> values) {
		this.values = values;
	}
		
	
}
