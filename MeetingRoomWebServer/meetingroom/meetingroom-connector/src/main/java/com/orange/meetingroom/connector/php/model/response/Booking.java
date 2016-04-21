package com.orange.meetingroom.connector.php.model.response;

/**
 * Booking
 * @author oab
 *
 */
public class Booking {

	String idReservation;
	String revisionReservation;
	String organizerFullName;
	String subject;
	Integer startDate;
	Integer endDate;
	Boolean acknowledged;
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
	 * @return the startDate
	 */
	public Integer getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Integer startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public Integer getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Integer endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the acknowledged
	 */
	public Boolean getAcknowledged() {
		return acknowledged;
	}
	/**
	 * @param acknowledged the acknowledged to set
	 */
	public void setAcknowledged(Boolean acknowledged) {
		this.acknowledged = acknowledged;
	}
	
	
}
