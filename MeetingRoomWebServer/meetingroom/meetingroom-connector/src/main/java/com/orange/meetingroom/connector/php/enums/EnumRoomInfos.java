package com.orange.meetingroom.connector.php.enums;

/**
 * EnumRoomInfos
 * @author oab
 *
 */
public enum EnumRoomInfos {
	
	INFOS("Infos"),
	CURRENT_DATE("CurrentDate"),
	ERROR_FLAG("ErrorFlag"),
	MESSAGE("Message"),
	ROOMS("Rooms"),
	ROOM_DETAILS("RoomDetails"),
	BOOKINGS("Bookings");
		
   /**
    * status value
    */
   private final String value;
       
   /**  
    * @param c code
    * @param v value
    */
   EnumRoomInfos(final String v) {
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
   public static EnumRoomInfos fromValue(String v) {
       for (EnumRoomInfos enumeration : EnumRoomInfos.values()) {
           if (enumeration.value.equals(v)) {
               return enumeration;
           }
       }
       throw new IllegalArgumentException(v);
   }

}
