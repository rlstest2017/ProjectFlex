package com.orange.flexoffice.meetingroomapi.ws.endPoint.entity.impl;

import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.business.common.service.data.DashboardManager;
import com.orange.flexoffice.business.common.service.data.RoomManager;
import com.orange.flexoffice.meetingroomapi.ws.endPoint.entity.MeetingRoomApiEndpoint;
import com.orange.flexoffice.meetingroomapi.ws.model.MeetingRoomInput;
import com.orange.flexoffice.meetingroomapi.ws.utils.ErrorMessageHandler;

/**
 * DashboardApiEndpointImpl
 * @author oab
 *
 */
public class MeetingRoomApiEndpointImpl implements MeetingRoomApiEndpoint {

	private static final Logger LOGGER = Logger.getLogger(MeetingRoomApiEndpointImpl.class);
	private static final String DEFAULT_PROFILE = "UNKNOWN";
	
	@Autowired
	private DashboardManager dashboardManager;
	@Autowired
	private RoomManager roomManager;

	@Autowired
	private ErrorMessageHandler errorMessageHandler;

	@Override
	public List<String> getTimeout() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response updateData(String identifier, MeetingRoomInput meetingRoom) {
		// TODO Auto-generated method stub
		return null;
	}



	/*@Override
	public Response updateSensor(String identifier, SensorInput sensor)  {
		
		LOGGER.debug( "Begin call SensorEndpoint.updateSensor at: " + new Date() );
		
		try {
			
			if (ESensorStatus.TEACHIN_ERROR.toString().equals(sensor.getSensorStatus().toString())) { // process teachin error
	
				sensorManager.processTeachinSensor(identifier, null, false);
					
			} else { // process updateSensor
				SensorDao sensorDao = sensorManager.find(identifier);
				sensorDao.setStatus(sensor.getSensorStatus().toString());
				if (sensor.getOccupancyInfo() != null) {
					sensorDao.setOccupancyInfo(sensor.getOccupancyInfo().toString());
				}
				
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("updateSensor with room parameters :");
					final StringBuilder message = new StringBuilder( 1000 );
					message.append( "room Id :" );
					message.append( sensorDao.getRoomId() );
					message.append( "\n" );
					message.append( "sensor room Temperature Info :" );
					message.append( sensor.getTemperature() );
					message.append( "\n" );
					message.append( "sensor room Humidity Info :" );
					message.append( sensor.getHumidity() );
					message.append( "\n" );
					message.append( "sensor Occupancy Info :" );
					message.append( sensor.getOccupancyInfo() );
					message.append( "\n" );
					message.append( "sensor Status :" );
					message.append( sensor.getSensorStatus() );
					message.append( "\n" );
					message.append( "sensor Identifier :" );
					message.append( identifier  );
					LOGGER.debug( message.toString() );
				}
				
				RoomDao roomDao = null;
				if ((sensorDao.getRoomId() != null) && (sensorDao.getRoomId() != 0)) {
					// get room 
					roomDao = roomManager.findByRoomId(Long.valueOf(sensorDao.getRoomId()));
					if (sensor.getTemperature() != null) {
						roomDao.setTemperature(sensor.getTemperature());
					} 
					if (sensor.getHumidity() != null) {
						roomDao.setHumidity(sensor.getHumidity());
					} 
					
					LOGGER.debug("RoomDao is instanciated");
				}
		
				sensorManager.updateStatus(sensorDao, roomDao);
			}
		} catch (DataNotExistsException e){

			LOGGER.debug("DataNotExistsException in SensorEndpoint.updateSensor with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_12, Response.Status.NOT_FOUND));

		} catch (RuntimeException ex){

			LOGGER.debug("RuntimeException in SensorEndpoint.updateSensor with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}

		LOGGER.debug( "End call SensorEndpoint.updateSensor at: " + new Date() );

		return Response.status(Status.ACCEPTED).build();
	}
*/

	/**
	 * computeType
	 * @param currentProfile
	 * @return
	 * @throws WrongProfileException
	 */
	/*private String computeType(final String currentProfile) throws WrongProfileException {
		String type = DEFAULT_PROFILE;
		EnumAcceptedProfile[] values = EnumAcceptedProfile.values();
		for (EnumAcceptedProfile profile : values) {
			if (profile.code().equalsIgnoreCase(currentProfile)) {
				type = profile.value();
				break;
			}
		}
		 
		if (type.equals(DEFAULT_PROFILE)) {
			throw new WrongProfileException("wrong profile is detected !!!");
		} 

		return type;
	}*/


}
