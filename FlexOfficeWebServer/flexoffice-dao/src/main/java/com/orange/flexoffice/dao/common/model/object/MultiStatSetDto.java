package com.orange.flexoffice.dao.common.model.object;

import java.util.ArrayList;
import java.util.List;

/**
 * MultiStatSetDto
 * @author oab
 *
 */
public class MultiStatSetDto {

    protected long startdate;
    protected long enddate;
    protected List<String> categories;
    protected List<MultiStatDto> data;

    /**
     * Gets the value of the startdate property.
     * 
     */
    public long getStartdate() {
        return startdate;
    }

    /**
     * Sets the value of the startdate property.
     * 
     */
    public void setStartdate(long value) {
        this.startdate = value;
    }

    /**
     * Gets the value of the enddate property.
     * 
     */
    public long getEnddate() {
        return enddate;
    }

    /**
     * Sets the value of the enddate property.
     * 
     */
    public void setEnddate(long value) {
        this.enddate = value;
    }

    /**
     * Gets the value of the categories property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the categories property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCategories().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getCategories() {
        if (categories == null) {
            categories = new ArrayList<String>();
        }
        return this.categories;
    }

    /**
	 * @param categories the categories to set
	 */
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	
    /**
     * Gets the value of the data property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the data property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MultiStat }
     * 
     * 
     */
    public List<MultiStatDto> getData() {
        if (data == null) {
            data = new ArrayList<MultiStatDto>();
        }
        return this.data;
    }

	/**
	 * @param data the data to set
	 */
	public void setData(List<MultiStatDto> data) {
		this.data = data;
	}

    
}
