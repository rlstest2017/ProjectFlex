package com.orange.meetingroom.gui.ws.endPoint.entity.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.meetingroom.business.connector.PhpConnectorManager;
import com.orange.meetingroom.business.connector.utils.ConfHashMapFactoryBean;
import com.orange.meetingroom.business.connector.utils.DateTools;
import com.orange.meetingroom.business.service.enums.EnumErrorModel;
import com.orange.meetingroom.business.service.exception.DateNotInSlotTimeException;
import com.orange.meetingroom.connector.exception.DataNotExistsException;
import com.orange.meetingroom.connector.exception.FlexOfficeInternalServerException;
import com.orange.meetingroom.connector.exception.MeetingRoomInternalServerException;
import com.orange.meetingroom.connector.exception.MethodNotAllowedException;
import com.orange.meetingroom.connector.exception.PhpInternalServerException;
import com.orange.meetingroom.connector.flexoffice.enums.EnumSystemInMap;
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
import com.orange.meetingroom.gui.ws.task.MeetingRoomGuiTasks;
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
	static final String ACKNOWLEDGED_DEFAULT = "0";
	static final String ACKNOWLEDGED_CONFIRMED = "1";
	static final String DEFAULT_BOOK_FROM_AGENT_OR_DASHBOARD_EN = "Quick";
	static final String DEFAULT_BOOK_FROM_AGENT_OR_DASHBOARD_FR = "automatique";

	String dashboardMaxBookingsParam = "2"; // number of bookings to get from now() 
	 
	// if this date is after now(), PHP server get maxBookings from this date
	// if startDate = 0 or before now(), PHP server get maxBookings from now()
	// dashboardStartDateBookingsParam is date in milliseconds
	String dashboardStartDateBookingsParam = "0";
	
	Integer ackTime = 0;
	
	@Autowired
	private ConfHashMapFactoryBean confHashMapFactoryBean;
	@Autowired
	private PhpConnectorManager phpConnectorManager;
	@Autowired
	private ErrorMessageHandler errorMessageHandler;
	// for testing
	@Autowired
	private MeetingRoomGuiTasks meetingRoomTask;
	@Autowired
	DateTools dateTools;
	
	/**
	 * @return the dashboardMaxBookingsParam
	 */
	private String getDashboardMaxBookingsParam() {
		Map<String, Integer> configMap = confHashMapFactoryBean.getObject();
		if (configMap.containsKey(EnumSystemInMap.DASHBOARD_MAX_BOOKINGS.toString())) {
			this.dashboardMaxBookingsParam = configMap.get(EnumSystemInMap.DASHBOARD_MAX_BOOKINGS.toString()).toString();
		}
		return this.dashboardMaxBookingsParam;
	}

	/**
	 * @return the dashboardStartDateBookingsParam
	 */
	private String getDashboardStartDateBookingsParam() {
		Map<String, Integer> configMap = confHashMapFactoryBean.getObject();
		if (configMap.containsKey(EnumSystemInMap.DASHBOARD_START_DATE.toString())) {
			// process startDate
			Integer startDate = 0;
			String nbSeconds = configMap.get(EnumSystemInMap.DASHBOARD_START_DATE.toString()).toString();
			if (Integer.valueOf(nbSeconds) != 0) {
					startDate = dateTools.processStartDate(Integer.valueOf(nbSeconds));
					this.dashboardStartDateBookingsParam = startDate.toString();
			} else {
				this.dashboardStartDateBookingsParam = "0";
			}
			
		}
		return this.dashboardStartDateBookingsParam;
	}
	
	/**
	 * getAckTime
	 * @return
	 */
	private Integer setAckTime() {
		Map<String, Integer> configMap = confHashMapFactoryBean.getObject();
		if (configMap.containsKey(EnumSystemInMap.ACK_TIME.toString())) {
			this.ackTime = configMap.get(EnumSystemInMap.ACK_TIME.toString());
		}
		return this.ackTime;
	}
	
	/**
	 * processSubject
	 * @param book
	 * @return
	 */
	private String processSubject(String subjectParam) {
		
		String subject = subjectParam;
		
		if (!subject.isEmpty()) {
			String[] subjectArray = subject.split("-");
			if (subjectArray != null) {
				String sub = subjectArray[0];
				for (int i=1; i< subjectArray.length; i++) {
					if (!subjectArray[i].contains(DEFAULT_BOOK_FROM_AGENT_OR_DASHBOARD_EN) && !subjectArray[i].contains(DEFAULT_BOOK_FROM_AGENT_OR_DASHBOARD_FR)) {
						sub = sub +"-"+subjectArray[i];
					}
				}
				subject = sub.trim(); 
				
			}
		} 
		
		return subject;
	}
	
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
					book.setSubject(processSubject(bookConnector.getSubject()));
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
	public MeetingRooms getBookings(String dashboardMacAddress) {
		try {
			MeetingRooms meetingrooms = factory.createMeetingRooms();
			
			GetDashboardBookingsParameters params = new GetDashboardBookingsParameters();
			params.setDashboardMacAddress(dashboardMacAddress);
			params.setMaxBookings(getDashboardMaxBookingsParam());
			params.setStartDate(getDashboardStartDateBookingsParam());
			params.setFormat(FORMAT_JSON);
			
			MeetingRoomsConnectorReturn meetingroomsreturn;
				meetingroomsreturn = phpConnectorManager.getBookingsFromDashboard(params);
			
			
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
						
						meetingRoomBookings.setMeetingRoomDetails(details);
						
						List<BookingConnectorReturn> bookingsReturnList = meetingroomReturn.getBookings();
								
						for (BookingConnectorReturn bookConnector : bookingsReturnList) {
							Booking book = factory.createBooking();
							book.setIDReservation(bookConnector.getIdReservation());
							book.setRevisionReservation(bookConnector.getRevisionReservation());
							book.setOrganizerFullName(bookConnector.getOrganizerFullName());
							book.setSubject(processSubject(bookConnector.getSubject()));
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
			} catch (DataNotExistsException e) {
				LOGGER.debug("DataNotExistsException in getBookings() MeetingRoomEndpointImpl with message :" + e.getMessage(), e);
				throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_4, Response.Status.NOT_FOUND));
			} catch (MeetingRoomInternalServerException | FlexOfficeInternalServerException |PhpInternalServerException | RuntimeException e) {
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
		
		setAckTime(); // set AckTime properties from Config hashMap from DataBase System Table
		
		if (this.ackTime == 0) {
			params.setAcknowledged(ACKNOWLEDGED_CONFIRMED);
		} else {
			params.setAcknowledged(ACKNOWLEDGED_DEFAULT);
		}
		
		BookingSummary outputSummary = phpConnectorManager.setBooking(params);
		
		if (outputSummary != null) {
			output.setIDReservation(outputSummary.getIdReservation());
			output.setRevisionReservation(outputSummary.getRevisionReservation());
		} else {
			LOGGER.debug("BookingSummary returned is null !!!");
		}
		
		return factory.createBookingSetOutput(output).getValue();
		
		} catch (DateNotInSlotTimeException e) {
			LOGGER.debug("DateNotInSlotTimeException in setBooking() MeetingRoomEndpointImpl with message :" + e.getMessage(), e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_11, Response.Status.METHOD_NOT_ALLOWED));
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
			params.setEndDate(new Date().toString()); // only endDate to cancel request !!!
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
			params.setStartDate(new Date().toString()); // only startDate to confirm request !!!
			params.setAcknowledged(ACKNOWLEDGED_CONFIRMED);
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
	
	// only for testing
	@Override
	public boolean checkMeetingRoomsStatusTimeOutTestMethod() {
		return meetingRoomTask.checkMeetingRoomsStatusTimeOutTestMethod();
	}
	
}
