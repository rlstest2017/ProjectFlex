
package com.orange.flexoffice.business.enums;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for e-deviceType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="e-deviceType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="GATEWAY"/>
 *     &lt;enumeration value="SENSOR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "e-deviceType")
@XmlEnum
public enum EnumDeviceType {

    GATEWAY,
    SENSOR;

    public String value() {
        return name();
    }

    public static EnumDeviceType fromValue(String v) {
        return valueOf(v);
    }

}
