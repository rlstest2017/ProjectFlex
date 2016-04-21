package com.orange.meetingroom.connector.php.utils;

import java.text.ParseException;

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
		urlEncodeParameters.append( "&" );
		urlEncodeParameters.append( "MaxBookings="+params.getMaxBookings());
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
	 */
	public String updateBookingParametersToUrlEncode(UpdateBookingParameters params) {
		
		final StringBuilder urlEncodeParameters = new StringBuilder( 1000 );
		urlEncodeParameters.append( "RoomID="+params.getRoomID());
		urlEncodeParameters.append( "&" );
		urlEncodeParameters.append( "IDReservation="+params.getIdReservation());
		urlEncodeParameters.append( "&" );
		urlEncodeParameters.append( "RevisionReservation="+params.getRevisionReservation());
		urlEncodeParameters.append( "&" );
		urlEncodeParameters.append( "EndDate="+params.getEndDate()); 
		urlEncodeParameters.append( "&" );
		urlEncodeParameters.append( "format="+params.getFormat()); 
		
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
	
	/**
	 * for tests only
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		DataTools data = new DataTools();
		GetAgentBookingsParameters params = new GetAgentBookingsParameters();
		params.setFormat("json");
		params.setRoomID("brehat.rennes@microsoft.cad.aql.fr");
		params.setForceUpdateCache("false");
		String returnMetho = data.getAgentBookingsParametersToUrlEncode(params);
		System.out.println("return is :" + returnMetho);
		
		GetDashboardBookingsParameters paramsDash = new GetDashboardBookingsParameters();
		paramsDash.setFormat("json");
		paramsDash.setMaxBookings("2");
		paramsDash.setRoomGroupID("rg_oab_full");
		paramsDash.setStartDate("0");
		String returnDashMethod = data.getDashboardBookingsParametersToUrlEncode(paramsDash);
		System.out.println("return dash is :" + returnDashMethod);
		
		SetBookingParameters paramsSet = new SetBookingParameters();
		paramsSet.setRoomID("brehat.rennes@microsoft.cad.aql.fr");
		paramsSet.setOrganizerFullName("");
		paramsSet.setSubject("");
		paramsSet.setFormat("json");
		paramsSet.setStartDate("1461060000");
		paramsSet.setEndDate("1461060600");
		paramsSet.setAcknowledged("1");
		String returnSetMethod = data.setBookingParametersToUrlEncode(paramsSet);
		System.out.println("return set is :" + returnSetMethod);
		
		UpdateBookingParameters paramsUpdate = new UpdateBookingParameters();
		paramsUpdate.setRoomID("brehat.rennes@microsoft.cad.aql.fr");
		paramsUpdate.setIdReservation("AAAiAGJyZWhhdC5yZW5uZXNAbWljcm9zb2Z0LmNhZC5hcWwuZnIARgAAAAAAJjiq1ulLK0Kj6vNsTnRuywcAQopQvd4yGUaRbVXWgALbzwAAAAfOdQAAQopQvd4yGUaRbVXWgALbzwAAkZg7ggAA");
		paramsUpdate.setRevisionReservation("DwAAABYAAABCilC93jIZRpFtVdaAAtvPAACRmK21");
		paramsUpdate.setFormat("json");
		paramsUpdate.setEndDate("1461060600");
		String returnUpdateMethod = data.updateBookingParametersToUrlEncode(paramsUpdate);
		System.out.println("return update is :" + returnUpdateMethod);
	}
}
