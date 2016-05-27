package com.orange.meetingroom.connector.php.model.response;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="GetBookings")
public class XMLGetBookings
{
    List<XMLGetBookingsInfos> bookingsInfos;

	/**
	 * @param bookingsInfos the bookingsInfos to set
	 */
    @XmlElement( name = "XMLGetBookingsInfos" )
	public void setBookingsInfos(List<XMLGetBookingsInfos> bookingsInfos) {
		this.bookingsInfos = bookingsInfos;
	}
	/**
	 * @return the bookingsInfos
	 */
	public List<XMLGetBookingsInfos> getBookingsInfos() {
		return bookingsInfos;
	}
    
	
}   
