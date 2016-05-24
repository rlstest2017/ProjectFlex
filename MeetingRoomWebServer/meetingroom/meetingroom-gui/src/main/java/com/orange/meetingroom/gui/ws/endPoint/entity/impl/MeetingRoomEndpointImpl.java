package com.orange.meetingroom.gui.ws.endPoint.entity.impl;

import java.math.BigInteger;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.meetingroom.business.connector.PhpConnectorManager;
import com.orange.meetingroom.business.service.enums.EnumErrorModel;
import com.orange.meetingroom.connector.exception.DataNotExistsException;
import com.orange.meetingroom.connector.exception.MeetingRoomInternalServerException;
import com.orange.meetingroom.connector.exception.MethodNotAllowedException;
import com.orange.meetingroom.connector.exception.PhpInternalServerException;
import com.orange.meetingroom.connector.php.model.request.GetAgentBookingsParameters;
import com.orange.meetingroom.connector.php.model.request.GetDashboardBookingsParameters;
import com.orange.meetingroom.connector.php.model.request.SetBookingParameters;
import com.orange.meetingroom.connector.php.model.request.UpdateBookingParameters;
import com.orange.meetingroom.connector.php.model.response.BookingConnectorReturn;
import com.orange.meetingroom.connector.php.model.response.BookingSummary;
import com.orange.meetingroom.connector.php.model.response.MeetingRoomBookingsConnectorReturn;
import com.orange.meetingroom.connector.php.model.response.MeetingRoomConnectorReturn;
import com.orange.meetingroom.connector.php.model.response.MeetingRoomDetailsConnectorReturn;
import com.orange.meetingroom.connector.php.model.response.MeetingRoomsConnectorReturn;
import com.orange.meetingroom.gui.ws.endPoint.entity.MeetingRoomEndpoint;
import com.orange.meetingroom.gui.ws.model.Booking;
import com.orange.meetingroom.gui.ws.model.BookingSetInput;
import com.orange.meetingroom.gui.ws.model.BookingSetOutput;
import com.orange.meetingroom.gui.ws.model.BookingUpdateInput;
import com.orange.meetingroom.gui.ws.model.BookingUpdateOutput;
import com.orange.meetingroom.gui.ws.model.EMeetingroomStatus;
import com.orange.meetingroom.gui.ws.model.MeetingRoom;
import com.orange.meetingroom.gui.ws.model.MeetingRoomBookings;
import com.orange.meetingroom.gui.ws.model.MeetingRoomDetails;
import com.orange.meetingroom.gui.ws.model.MeetingRooms;
import com.orange.meetingroom.gui.ws.model.ObjectFactory;
import com.orange.meetingroom.gui.ws.utils.ErrorMessageHandler;

/**
 * MeetingRoomEndpointImpl
 * @author oab
 *
 */
public class MeetingRoomEndpointImpl implements MeetingRoomEndpoint {

	private static final Logger LOGGER = Logger.getLogger(MeetingRoomEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();
	static final String FORMAT_JSON = "json";
	static final String FORCED_UPDATE_CACHE_TRUE = "true";
	static final String FORCED_UPDATE_CACHE_FALSE = "false";
	static final String ACKNOWLEDGED_DAFAULT = "0";
	static final String ACKNOWLEDGED_CONFIRM = "1";
	

	@Autowired
	private PhpConnectorManager phpConnectorManager;
	@Autowired
	private ErrorMessageHandler errorMessageHandler;

	@Override
	public MeetingRoom getMeetingRoomBookings(String meetingRoomExternalId, Boolean forceUpdateCache) {
		
		try {
		MeetingRoom meetingroom = factory.createMeetingRoom();
		
		GetAgentBookingsParameters params = new GetAgentBookingsParameters();
		params.setRoomID(meetingRoomExternalId);
		params.setFormat(FORMAT_JSON);
		if (forceUpdateCache != null) {
			if (forceUpdateCache) {
				params.setForceUpdateCache(FORCED_UPDATE_CACHE_TRUE);
			} else {
				params.setForceUpdateCache(FORCED_UPDATE_CACHE_FALSE);
			}
		} else {
			params.setForceUpdateCache(FORCED_UPDATE_CACHE_FALSE);
		}
			
		MeetingRoomConnectorReturn meetingroomreturn = phpConnectorManager.getBookingsFromAgent(params);
		
		if (meetingroomreturn != null) {
			meetingroom.setCurrentDate(BigInteger.valueOf(meetingroomreturn.getCurrentDate()));
			
			if (meetingroomreturn.getMeetingRoom() != null) {
				MeetingRoomDetailsConnectorReturn detailsReturn = meetingroomreturn.getMeetingRoom().getMeetingRoomDetails();
				
				MeetingRoomBookings meetingRoomBookings = factory.createMeetingRoomBookings();
				MeetingRoomDetails details = factory.createMeetingRoomDetails(); 
				details.setMeetingRoomExternalId(detailsReturn.getMeetingRoomExternalId());
				details.setMeetingRoomExternalName(detailsReturn.getMeetingRoomExternalName());
				details.setMeetingRoomExternalLocation(detailsReturn.getMeetingRoomExternalLocation());
				if (detailsReturn.getMeetingRoomStatus() != null) {
					details.setMeetingRoomStatus(EMeetingroomStatus.valueOf(detailsReturn.getMeetingRoomStatus().toString()));
				}
				
				meetingRoomBookings.setMeetingRoomDetails(details);
				
				List<BookingConnectorReturn> bookingsReturnList = meetingroomreturn.getMeetingRoom().getBookings();
						
				for (BookingConnectorReturn bookConnector : bookingsReturnList) {
					Booking book = factory.createBooking();
					book.setIDReservation(bookConnector.getIdReservation());
					book.setRevisionReservation(bookConnector.getRevisionReservation());
					book.setOrganizerFullName(bookConnector.getOrganizerFullName());
					book.setSubject(bookConnector.getSubject());
					book.setStartDate(BigInteger.valueOf(bookConnector.getStartDate()));
					book.setEndDate(BigInteger.valueOf(bookConnector.getEndDate()));
					book.setAcknowledged(bookConnector.getAcknowledged());
					meetingRoomBookings.getBookings().add(book);
				}
				
				meetingroom.setRoom(meetingRoomBookings);
			}
		}
		
		return factory.createMeetingRoom(meetingroom).getValue();
		
		}  catch (DataNotExistsException e) {
			LOGGER.debug("DataNotExistsException in getMeetingRoomBookings() MeetingRoomEndpointImpl with message :" + e.getMessage(), e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_8, Response.Status.NOT_FOUND));
		} catch (MethodNotAllowedException e) {
			LOGGER.debug("MethodNotAllowedException in getMeetingRoomBookings() MeetingRoomEndpointImpl with message :" + e.getMessage(), e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_9, Response.Status.METHOD_NOT_ALLOWED));
		} catch (MeetingRoomInternalServerException | PhpInternalServerException | RuntimeException e) {
			LOGGER.debug("RuntimeException in getMeetingRoomBookings() MeetingRoomEndpointImpl with message :" + e.getMessage(), e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_1, Response.Status.INTERNAL_SERVER_ERROR));
		} 
				
	}

	@Override
	public MeetingRooms getBookings(String dashboardMacAddress, Integer maxBookings, Integer startDate) {
		try {
			MeetingRooms meetingrooms = factory.createMeetingRooms();
			
			GetDashboardBookingsParameters params = new GetDashboardBookingsParameters();
			params.setDashboardMacAddress(dashboardMacAddress);
			params.setMaxBookings(maxBookings.toString());
			params.setStartDate(startDate.toString());
			params.setFormat(FORMAT_JSON);
			
			MeetingRoomsConnectorReturn meetingroomsreturn = phpConnectorManager.getBookingsFromDashboard(params);
			
			if (meetingroomsreturn != null) {
				meetingrooms.setCurrentDate(BigInteger.valueOf(meetingroomsreturn.getCurrentDate()));
				
				if (meetingroomsreturn.getMeetingRooms() != null) {
					List<MeetingRoomBookingsConnectorReturn> meetingroomList = meetingroomsreturn.getMeetingRooms();
					
					for (MeetingRoomBookingsConnectorReturn meetingroomReturn : meetingroomList) {
						MeetingRoomBookings meetingRoomBookings = factory.createMeetingRoomBookings();
						
						MeetingRoomDetailsConnectorReturn detailsReturn = meetingroomReturn.getMeetingRoomDetails();
						MeetingRoomDetails details = factory.createMeetingRoomDetails(); 
						details.setMeetingRoomExternalId(detailsReturn.getMeetingRoomExternalId());
						details.setMeetingRoomExternalName(detailsReturn.getMeetingRoomExternalName());
						details.setMeetingRoomExternalLocation(detailsReturn.getMeetingRoomExternalLocation());
						// TODO add details.setMeetingRoomStatus(detailsReturn.get);
						
						meetingRoomBookings.setMeetingRoomDetails(details);
						
						List<BookingConnectorReturn> bookingsReturnList = meetingroomReturn.getBookings();
								
						for (BookingConnectorReturn bookConnector : bookingsReturnList) {
							Booking book = factory.createBooking();
							book.setIDReservation(bookConnector.getIdReservation());
							book.setRevisionReservation(bookConnector.getRevisionReservation());
							book.setOrganizerFullName(bookConnector.getOrganizerFullName());
							book.setSubject(bookConnector.getSubject());
							book.setStartDate(BigInteger.valueOf(bookConnector.getStartDate()));
							book.setEndDate(BigInteger.valueOf(bookConnector.getEndDate()));
							book.setAcknowledged(bookConnector.getAcknowledged());
							meetingRoomBookings.getBookings().add(book);
						}
						
						meetingrooms.getRooms().add(meetingRoomBookings);
					}
					
				}
			}
			
			return factory.createMeetingRooms(meetingrooms).getValue();
			
			} catch (MethodNotAllowedException e) {
				LOGGER.debug("MethodNotAllowedException in getBookings() MeetingRoomEndpointImpl with message :" + e.getMessage(), e);
				throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_10, Response.Status.METHOD_NOT_ALLOWED));
			} catch (MeetingRoomInternalServerException | PhpInternalServerException | RuntimeException e) {
				LOGGER.debug("RuntimeException in getBookings() MeetingRoomEndpointImpl with message :" + e.getMessage(), e);
				throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_1, Response.Status.INTERNAL_SERVER_ERROR));
			}
	}

	@Override
	public BookingSetOutput setBooking(String meetingRoomExternalId, BookingSetInput bookingSetInput) {
		
		try {
		BookingSetOutput output = factory.createBookingSetOutput();
		
		SetBookingParameters params = new SetBookingParameters();
		params.setRoomID(meetingRoomExternalId);
		params.setOrganizerFullName(bookingSetInput.getOrganizerFullName());
		params.setSubject(bookingSetInput.getSubject());
		params.setFormat(FORMAT_JSON);
		params.setStartDate(bookingSetInput.getStartDate().toString());
		params.setEndDate(bookingSetInput.getEndDate().toString());
		params.setAcknowledged(ACKNOWLEDGED_DAFAULT);
		
		BookingSummary outputSummary = phpConnectorManager.setBooking(params);
		
		if (outputSummary != null) {
			output.setIDReservation(outputSummary.getIdReservation());
			output.setRevisionReservation(outputSummary.getRevisionReservation());
		} else {
			LOGGER.debug("BookingSummary returned is null !!!");
		}
		
		return factory.createBookingSetOutput(output).getValue();
		
		} catch (MethodNotAllowedException e) {
			LOGGER.debug("MethodNotAllowedException in setBooking() MeetingRoomEndpointImpl with message :" + e.getMessage(), e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_5, Response.Status.METHOD_NOT_ALLOWED));
		} catch (PhpInternalServerException | MeetingRoomInternalServerException | RuntimeException e) {
			LOGGER.debug("RuntimeException in setBooking() MeetingRoomEndpointImpl with message :" + e.getMessage(), e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_1, Response.Status.INTERNAL_SERVER_ERROR));		
		}
	}

	@Override
	public BookingUpdateOutput cancelBooking(String meetingRoomExternalId, BookingUpdateInput bookingUpdateInput) {
		try {
			BookingUpdateOutput output = factory.createBookingUpdateOutput();
			
			UpdateBookingParameters params = new UpdateBookingParameters();
			params.setRoomID(meetingRoomExternalId);
			params.setIdReservation(bookingUpdateInput.getIDReservation());
			params.setRevisionReservation(bookingUpdateInput.getRevisionReservation());
			params.setEndDate(bookingUpdateInput.getEndDate().toString()); // only endDate to cancel request !!!
			params.setFormat(FORMAT_JSON);
						
			BookingSummary outputSummary = phpConnectorManager.updateBooking(params);
			
			if (outputSummary != null) {
				output.setIDReservation(outputSummary.getIdReservation());
				output.setRevisionReservation(outputSummary.getRevisionReservation());
			} else {
				LOGGER.debug("BookingSummary returned is null !!!");
			}
			
			return factory.createBookingUpdateOutput(output).getValue();
			
			} catch (MethodNotAllowedException e) {
				LOGGER.debug("MethodNotAllowedException in cancelBooking() MeetingRoomEndpointImpl with message :" + e.getMessage(), e);
				throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_6, Response.Status.METHOD_NOT_ALLOWED));
			} catch (PhpInternalServerException | MeetingRoomInternalServerException | RuntimeException e) {
				LOGGER.debug("RuntimeException in cancelBooking() MeetingRoomEndpointImpl with message :" + e.getMessage(), e);
				throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_1, Response.Status.INTERNAL_SERVER_ERROR));		
			}	
	}

	@Override
	public BookingUpdateOutput confirmBooking(String meetingRoomExternalId, BookingUpdateInput bookingUpdateInput) {
		try {
			BookingUpdateOutput output = factory.createBookingUpdateOutput();
			
			UpdateBookingParameters params = new UpdateBookingParameters();
			params.setRoomID(meetingRoomExternalId);
			params.setIdReservation(bookingUpdateInput.getIDReservation());
			params.setRevisionReservation(bookingUpdateInput.getRevisionReservation());
			params.setStartDate(bookingUpdateInput.getStartDate().toString()); // only startDate to confirm request !!!
			params.setAcknowledged(ACKNOWLEDGED_CONFIRM);
			params.setSubject(bookingUpdateInput.getSubject());
			params.setFormat(FORMAT_JSON);
						
			BookingSummary outputSummary = phpConnectorManager.updateBooking(params);
			
			if (outputSummary != null) {
				output.setIDReservation(outputSummary.getIdReservation());
				output.setRevisionReservation(outputSummary.getRevisionReservation());
			} else {
				LOGGER.debug("BookingSummary returned is null !!!");
			}
			
			return factory.createBookingUpdateOutput(output).getValue();
			
			} catch (MethodNotAllowedException e) {
				LOGGER.debug("MethodNotAllowedException in confirmBooking() MeetingRoomEndpointImpl with message :" + e.getMessage(), e);
				throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_7, Response.Status.METHOD_NOT_ALLOWED));
			} catch (PhpInternalServerException | MeetingRoomInternalServerException | RuntimeException e) {
				LOGGER.debug("RuntimeException in confirmBooking() MeetingRoomEndpointImpl with message :" + e.getMessage(), e);
				throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_1, Response.Status.INTERNAL_SERVER_ERROR));		
			}
	}
	

}
