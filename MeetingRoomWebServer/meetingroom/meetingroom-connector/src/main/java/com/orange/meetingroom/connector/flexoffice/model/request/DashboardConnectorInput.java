package com.orange.meetingroom.connector.flexoffice.model.request;


import com.orange.meetingroom.connector.flexoffice.enums.EnumDashboardStatus;

/**
 * DashboardInput
 * @author oab
 *
 */
public class DashboardConnectorInput {
	
	String dashboardMacAddress;
	EnumDashboardStatus dashboardStatus;
	
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
	/**
	 * @return the dashboardStatus
	 */
	public EnumDashboardStatus getDashboardStatus() {
		return dashboardStatus;
	}
	/**
	 * @param dashboardStatus the dashboardStatus to set
	 */
	public void setDashboardStatus(EnumDashboardStatus dashboardStatus) {
		this.dashboardStatus = dashboardStatus;
	}
		
	
}
