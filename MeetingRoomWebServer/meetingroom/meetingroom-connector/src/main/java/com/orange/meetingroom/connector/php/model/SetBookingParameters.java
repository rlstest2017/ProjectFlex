package com.orange.meetingroom.connector.php.model;

public class SetBookingParameters {
	
	String roomID;
	String organizerFullName;
	String subject;
	String format;
	String startDate;
	String endDate;
	String acknowledged;
	
	/**
	 * @return the roomID
	 */
	public String getRoomID() {
		return roomID;
	}
	/**
	 * @param roomID the roomID to set
	 */
	public void setRoomID(String roomID) {
		this.roomID = roomID;
	}
	/**
	 * @return the organizerFullName
	 */
	public String getOrganizerFullName() {
		return organizerFullName;
	}
	/**
	 * @param organizerFullName the organizerFullName to set
	 */
	public void setOrganizerFullName(String organizerFullName) {
		this.organizerFullName = organizerFullName;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}
	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}
	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the acknowledged
	 */
	public String getAcknowledged() {
		return acknowledged;
	}
	/**
	 * @param acknowledged the acknowledged to set
	 */
	public void setAcknowledged(String acknowledged) {
		this.acknowledged = acknowledged;
	}
	
}
