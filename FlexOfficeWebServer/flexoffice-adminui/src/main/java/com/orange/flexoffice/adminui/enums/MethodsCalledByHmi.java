package com.orange.flexoffice.adminui.enums;

/**
 * MethodsCalledByHmi enum 
 * the mapping between methods called in HMI client with Ids
 * used in switch(ids) in controllers
 * @author oab
 * 
 * 
 * ******************** Callable methods Number between [1-102] , next 103 ********************
 */
public enum MethodsCalledByHmi {
    /**
     * methods called in SystemManageServlet 
     */
	/* GET */
	SYSTEM(0, "/system"),
    SYSTEM_LOGIN(1, "/login"),
    SYSTEM_LOGOUT(2, "/logout"),
    SYSTEM_TECHIN(3, "/teachin"),
    /* POST */
    SYSTEM_TECHIN_INIT(4, "/teachin/init"),
    SYSTEM_TECHIN_submit(5, "/teachin/submit"),
    SYSTEM_TECHIN_CANCEL(6, "/teachin/cancel");
    
	/**
     * methods called in GatewaysManageServlet 
     */
	
	
	/**
     * status code
     */
    private final Integer code;
    
    /**
     * status value
     */
    private final String value;
        
    /**  
     * @param c code
     * @param v value
     */
    MethodsCalledByHmi(final Integer c, final String v) {
        code = c;
        value = v;
    }

        /**
     * @return the code
     */
    public Integer code() {
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
    public static MethodsCalledByHmi fromValue(String v) {
        for (MethodsCalledByHmi enumeration : MethodsCalledByHmi.values()) {
            if (enumeration.value.equals(v)) {
                return enumeration;
            }
        }
        throw new IllegalArgumentException(v);
    }
    
    /**
     * @param aCode the code
     * @return the corresponding enum by code
     */
    /**
    public static MethodsCalledByHmi fromCode(Integer aCode) {
        for (MethodsCalledByHmi enumeration : MethodsCalledByHmi.values()) {
            if (enumeration.code==aCode) {
                return enumeration;
            }
        }
        throw new IllegalArgumentException(aCode.toString());
    }
    */

}
