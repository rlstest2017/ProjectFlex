package com.orange.flexoffice.business.common.enums;

/**
 * EnumAcceptedProfile enum 
 * the mapping between profile and type sensor
 * @author oab
 */
public enum EnumAcceptedProfile {
    /**
     *  
     */
  
	PROFILE_1_FORMAT_1("A5-07-01", "MOTION_DETECTION"),
	PROFILE_1_FORMAT_2("A5-7-1", "MOTION_DETECTION"),
	PROFILE_2_FORMAT_1("A5-04-01", "TEMPERATURE_HUMIDITY"),
	PROFILE_2_FORMAT_2("A5-4-1", "TEMPERATURE_HUMIDITY");
		
	
     /**
     * status code
     */
    private final String code;
    
    /**
     * status value
     */
    private final String value;
        
    /**  
     * @param c code
     * @param v value
     */
    EnumAcceptedProfile(final String c, final String v) {
        code = c;
        value = v;
    }

        /**
     * @return the code
     */
    public String code() {
        return code;
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
    public static EnumAcceptedProfile fromValue(String v) {
        for (EnumAcceptedProfile enumeration : EnumAcceptedProfile.values()) {
            if (enumeration.value.equals(v)) {
                return enumeration;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
