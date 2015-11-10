package com.orange.flexoffice.business.dto;

import java.io.Serializable;

/**
 * Message
 * @author oab
 *
 */
public class Message implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = -56903174858492113L;

    private String name;
    
    private String value;

    /**
     * Constructor 
     * @param name
     * @param value
     */
    public Message(String name, String value){
        this.name = name;
        this.value = value;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

        
    

}
