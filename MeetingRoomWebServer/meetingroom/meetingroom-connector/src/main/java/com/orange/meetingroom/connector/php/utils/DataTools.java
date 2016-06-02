package com.orange.meetingroom.connector.php.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import com.orange.meetingroom.connector.php.model.request.GetAgentBookingsParameters;
import com.orange.meetingroom.connector.php.model.request.GetDashboardBookingsParameters;
import com.orange.meetingroom.connector.php.model.request.SetBookingParameters;
import com.orange.meetingroom.connector.php.model.request.UpdateBookingParameters;

/**
 * DataTools
 * @author oab
 *
 */
public class DataTools {

	/**
	 * getAgentBookingsParametersToUrlEncode
	 * @param params
	 * @return
	 */
	public String getCurrentDateParameterToUrlEncode() {
		final StringBuilder urlEncodeParameters = new StringBuilder( 1000 );
		urlEncodeParameters.append( "format=json");
		return urlEncodeParameters.toString();
	}

	/**
	 * getAgentBookingsParametersToUrlEncode
	 * @param params
	 * @return
	 */
	public String getAgentBookingsParametersToUrlEncode(GetAgentBookingsParameters params) {
		final StringBuilder urlEncodeParameters = new StringBuilder( 1000 );
		urlEncodeParameters.append( "format="+params.getFormat());
		urlEncodeParameters.append( "&" );
		urlEncodeParameters.append( "RoomID="+params.getRoomID());
		urlEncodeParameters.append( "&" );
		urlEncodeParameters.append( "ForceUpdateCache="+params.getForceUpdateCache());
		urlEncodeParameters.append( "&" );
		urlEncodeParameters.append( "_="+System.currentTimeMillis()); // in milliseconds !!!
		return urlEncodeParameters.toString();
	}

	/**
	 * getDashboardBookingsParametersToUrlEncode
	 * @param params
	 * @return
	 */
	public String getDashboardBookingsParametersToUrlEncode(GetDashboardBookingsParameters params) {
		final StringBuilder urlEncodeParameters = new StringBuilder( 1000 );
		urlEncodeParameters.append( "format="+params.getFormat());
		if (params.getMaxBookings() != null) {
			urlEncodeParameters.append( "&" );
			urlEncodeParameters.append( "MaxBookings="+params.getMaxBookings());
		}
		urlEncodeParameters.append( "&" );
		urlEncodeParameters.append( "StartDate="+params.getStartDate());
		urlEncodeParameters.append( "&" );
		urlEncodeParameters.append( "RoomGroupID="+params.getRoomGroupID());
		urlEncodeParameters.append( "&" );
		urlEncodeParameters.append( "_="+System.currentTimeMillis()); // in milliseconds !!!
		return urlEncodeParameters.toString();
	}

	/**
	 * setBookingParametersToUrlEncode
	 * @param params
	 * @return
	 */
	public String setBookingParametersToUrlEncode(SetBookingParameters params) {
		final StringBuilder urlEncodeParameters = new StringBuilder( 1000 );
		urlEncodeParameters.append( "RoomID="+params.getRoomID());
		urlEncodeParameters.append( "&" );
		urlEncodeParameters.append( "OrganizerFullName="+params.getOrganizerFullName());
		urlEncodeParameters.append( "&" );
		urlEncodeParameters.append( "Subject="+params.getSubject());
		urlEncodeParameters.append( "&" );
		urlEncodeParameters.append( "format="+params.getFormat());
		urlEncodeParameters.append( "&" );
		urlEncodeParameters.append( "StartDate="+params.getStartDate());
		urlEncodeParameters.append( "&" );
		urlEncodeParameters.append( "EndDate="+params.getEndDate()); 
		urlEncodeParameters.append( "&" );
		urlEncodeParameters.append( "Acknowledged="+params.getAcknowledged()); 
		return urlEncodeParameters.toString();
	}
	
	/**
	 * updateBookingParametersToUrlEncode
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String updateBookingParametersToUrlEncode(UpdateBookingParameters params) throws UnsupportedEncodingException {
		final StringBuilder urlEncodeParameters = new StringBuilder( 1000 );
		urlEncodeParameters.append( "RoomID="+params.getRoomID());
		urlEncodeParameters.append( "&" );
		urlEncodeParameters.append( "IDReservation="+URLEncoder.encode(params.getIdReservation(), "UTF-8"));
		urlEncodeParameters.append( "&" );
		urlEncodeParameters.append( "RevisionReservation="+URLEncoder.encode(params.getRevisionReservation(), "UTF-8"));
		urlEncodeParameters.append( "&" );
		urlEncodeParameters.append( "format="+params.getFormat());
		if (params.getEndDate() != null) { // to close meeting
			urlEncodeParameters.append( "&" );
			urlEncodeParameters.append( "EndDate="+params.getEndDate());
		} else if (params.getStartDate() != null) { // to confirm meeting
			urlEncodeParameters.append( "&" );
			urlEncodeParameters.append( "StartDate="+params.getStartDate());
			urlEncodeParameters.append( "&" );
			urlEncodeParameters.append( "Acknowledged="+params.getAcknowledged());
			urlEncodeParameters.append( "&" );
			urlEncodeParameters.append( "Subject="+params.getSubject());
		}
		return urlEncodeParameters.toString();
	}

	/**
	 * constructOrganizerFullName
	 * @param organizer
	 * @param organizeFullName
	 * @param organizerMail
	 * @param creator
	 * @param creatorFullName
	 * @param creatorEmail
	 * @return
	 */
	public String constructOrganizerFullName(String organizer, String organizerFullName, String organizerMail,
			String creator, String creatorFullName, String creatorEmail) {
		String constructedOrganizer = "";
		// Ordre de priorité décroissante => creatorFullName < creator < organizerFullName < organizer < creatorEmail < organizerMail
		if (!creatorFullName.isEmpty()) {
			constructedOrganizer = creatorFullName;
		} else if (!creator.isEmpty()) {
			constructedOrganizer = creator;
		} else if (!organizerFullName.isEmpty()) {
			constructedOrganizer = organizerFullName;
		} else if (!organizer.isEmpty()) {
			constructedOrganizer = organizer;
		} else if (!creatorEmail.isEmpty()) {
			constructedOrganizer = creatorEmail;
		} else if (!organizerMail.isEmpty()) {
			constructedOrganizer = organizerMail;
		}
		return constructedOrganizer;
	}
	
}
