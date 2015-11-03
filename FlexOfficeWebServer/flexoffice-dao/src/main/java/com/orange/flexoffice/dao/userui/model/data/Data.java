package com.orange.flexoffice.dao.userui.model.data;

import java.util.Date;


/**
 * Defines a generic representation 
 * @author oab
 */
public interface Data {
	
	void setId(Long id);
	
	/**
	 * Gets the technical ID of the data.
	 * 
	 * @return an ID
	 */
	Long getId();
	
	void setColumnId(String columnId);
	
	String getColumnId();
	
	void setRowId(String rowId);
	
	String getRowId();
	
	void setRating(Float rating);

	Float getRating();
	
	void setComment(String comment);
	
	/**
	 * Gets a comment.
	 * 
	 * @return a comment
	 */
	String getComment();
	
	void setTimestamp(Date timestamp);
	
	/**
	 * Gets date of the last update.
	 * 
	 * @return a {@link Date}
	 */
	Date getTimestamp();

}
