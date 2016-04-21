package com.orange.meetingroom.connector.php.model.request;

public class UpdateBookingParameters {
	
	String roomID;
	String idReservation;
	String revisionReservation;
	String format;
	String endDate;
	
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
	 * @return the idReservation
	 */
	public String getIdReservation() {
		return idReservation;
	}
	/**
	 * @param idReservation the idReservation to set
	 */
	public void setIdReservation(String idReservation) {
		this.idReservation = idReservation;
	}
	/**
	 * @return the revisionReservation
	 */
	public String getRevisionReservation() {
		return revisionReservation;
	}
	/**
	 * @param revisionReservation the revisionReservation to set
	 */
	public void setRevisionReservation(String revisionReservation) {
		this.revisionReservation = revisionReservation;
	}
	
	
}
