package com.orange.meetingroom.connector.php.model.response;

/**
 * BookingSummary
 * @author oab
 *
 */
public class BookingSummary {

	String idReservation;
	String revisionReservation;
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
