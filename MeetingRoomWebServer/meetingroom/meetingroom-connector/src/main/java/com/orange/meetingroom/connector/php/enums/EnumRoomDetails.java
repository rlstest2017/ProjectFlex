package com.orange.meetingroom.connector.php.enums;

/**
 * EnumRoomDetails
 * @author oab
 *
 */
public enum EnumRoomDetails {
	
	ROOM_ID("RoomID"),
	ROOM_NAME("RoomName"),
	ROOM_LOCATION("RoomLocation");

	/**
	* status value
	*/
	private final String value;
	       
	   /**  
	    * @param c code
	    * @param v value
	    */
	EnumRoomDetails(final String v) {
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
	   public static EnumRoomDetails fromValue(String v) {
	       for (EnumRoomDetails enumeration : EnumRoomDetails.values()) {
	           if (enumeration.value.equals(v)) {
	               return enumeration;
	           }
	       }
	       throw new IllegalArgumentException(v);
	   }
}
