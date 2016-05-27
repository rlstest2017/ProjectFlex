package com.orange.meetingroom.connector.php.model.response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * XMLGetBookingsInfos
 * @author oab
 *
 */
@XmlRootElement( name = "GetBookingsInfos" )
public class XMLGetBookingsInfos {
	
	Integer currentDate;

	/**
	 * @return the currentDate
	 */
	public Integer getCurrentDate() {
		return currentDate;
	}
	/**
	 * @param currentDate the currentDate to set
	 */
	 @XmlElement(name="CurrentDate")
	public void setCurrentDate(Integer currentDate) {
		this.currentDate = currentDate;
	}

}
