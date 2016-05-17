package com.orange.meetingroom.business.service.enums;

/**
 * EnumErrorModel enum 
 * the mapping between code and error message
 * @author oab
 */
public enum EnumErrorModel {
	ERROR_1("500","une erreur métier s'est produite"),
	ERROR_2("A001","agentMacAddress is not found"),
	ERROR_3("A002","agentMacAddress is not paired to a meetingroom"),
	ERROR_4("D001","dashboardMacAddress is not found"),
	ERROR_5("M001","reservation is failed, Timeslot was not available or Internal error"),
	ERROR_6("M002","cancel reservation is failed"),
	ERROR_7("M003","confirm reservation is failed");;
	
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
    EnumErrorModel(final String c, final String v) {
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
    public static EnumErrorModel fromValue(String v) {
        for (EnumErrorModel enumeration : EnumErrorModel.values()) {
            if (enumeration.value.equals(v)) {
                return enumeration;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
