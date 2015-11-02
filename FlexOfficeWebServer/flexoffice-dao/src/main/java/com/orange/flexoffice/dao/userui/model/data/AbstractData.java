package com.orange.flexoffice.dao.userui.model.data;

import java.util.Date;

/**
 * Specifies commons properties shared by the different types of data.
 * 
 * @author Guillaume Mouricou
 *
 */
public abstract class AbstractData implements Data {
	
	/**
	 * The data ID.
	 */
	private Long id;
	/**
	 * A comment.
	 */
	private String comment;
	/**
	 * Date of the last data update.
	 */
	private Date timestamp;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
