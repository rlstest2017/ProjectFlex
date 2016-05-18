package com.orange.meetingroom.connector.php.model.request;

/**
 * GetDashboardBookingsParameters
 * @author oab
 *
 */
public class GetDashboardBookingsParameters {
	
	String startDate;
	String format;
	String maxBookings;
	String roomGroupID;
	String dashboardMacAddress;
	
	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}
	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}
	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the maxBookings
	 */
	public String getMaxBookings() {
		return maxBookings;
	}
	/**
	 * @param maxBookings the maxBookings to set
	 */
	public void setMaxBookings(String maxBookings) {
		this.maxBookings = maxBookings;
	}
	/**
	 * @return the roomGroupID
	 */
	public String getRoomGroupID() {
		return roomGroupID;
	}
	/**
	 * @param roomGroupID the roomGroupID to set
	 */
	public void setRoomGroupID(String roomGroupID) {
		this.roomGroupID = roomGroupID;
	}
	/**
	 * @return the dashboardMacAddress
	 */
	public String getDashboardMacAddress() {
		return dashboardMacAddress;
	}
	/**
	 * @param dashboardMacAddress the dashboardMacAddress to set
	 */
	public void setDashboardMacAddress(String dashboardMacAddress) {
		this.dashboardMacAddress = dashboardMacAddress;
	}
				
	
}
