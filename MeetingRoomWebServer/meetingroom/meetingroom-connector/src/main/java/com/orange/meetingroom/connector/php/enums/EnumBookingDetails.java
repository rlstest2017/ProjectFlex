package com.orange.meetingroom.connector.php.enums;

/**
 * EnumBookingDetails
 * @author oab
 *
 */
public enum EnumBookingDetails {

	ID_RESERVATION("IDReservation"),
	REVISION_RESERVATION("RevisionReservation"),
	ORGANIZER("Organizer"),
	ORGANIZER_FULL_NAME("OrganizerFullName"),
	ORGANIZER_EMAIL("OrganizerEmail"),
	CREATOR("Creator"),
	CREATOR_FULL_NAME("CreatorFullName"),
	CREATOR_EMAIL("CreatorEmail"),
	SUBJECT("Subject"),
	START_DATE("StartDate"),
	END_DATE("EndDate"),
	ACKNOWLEDGED("Acknowledged");
	
	/**
	* status value
	*/
	private final String value;
	       
	   /**  
	    * @param c code
	    * @param v value
	    */
	EnumBookingDetails(final String v) {
	       value = v;
	   }

	   /**
	    * @return the value
	    */
	   public String value() {
	       return value;
	   }

	   /**
	    * @param v the value
	    * @return the corresponding enum by value
	    */
	   public static EnumBookingDetails fromValue(String v) {
	       for (EnumBookingDetails enumeration : EnumBookingDetails.values()) {
	           if (enumeration.value.equals(v)) {
	               return enumeration;
	           }
	       }
	       throw new IllegalArgumentException(v);
	   }

}
