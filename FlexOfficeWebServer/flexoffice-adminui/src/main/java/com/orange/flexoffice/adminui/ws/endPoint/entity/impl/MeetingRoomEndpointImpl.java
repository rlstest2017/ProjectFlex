package com.orange.flexoffice.adminui.ws.endPoint.entity.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.adminui.ws.endPoint.entity.MeetingRoomEndpoint;
import com.orange.flexoffice.adminui.ws.model.AgentOutput;
import com.orange.flexoffice.adminui.ws.model.BuildingOutput;
import com.orange.flexoffice.adminui.ws.model.EMeetingroomStatus;
import com.orange.flexoffice.adminui.ws.model.EMeetingroomType;
import com.orange.flexoffice.adminui.ws.model.Location;
import com.orange.flexoffice.adminui.ws.model.LocationItem;
import com.orange.flexoffice.adminui.ws.model.MeetingRoom;
import com.orange.flexoffice.adminui.ws.model.MeetingRoomInput;
import com.orange.flexoffice.adminui.ws.model.MeetingRoomOutput;
import com.orange.flexoffice.adminui.ws.model.MeetingRoomSummary;
import com.orange.flexoffice.adminui.ws.model.ObjectFactory;
import com.orange.flexoffice.adminui.ws.utils.ErrorMessageHandler;
import com.orange.flexoffice.business.common.enums.EnumErrorModel;
import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.business.common.service.data.AgentManager;
import com.orange.flexoffice.business.common.service.data.BuildingManager;
import com.orange.flexoffice.business.common.service.data.MeetingRoomManager;
import com.orange.flexoffice.business.common.service.data.TestManager;
import com.orange.flexoffice.dao.common.model.data.MeetingRoomDao;
import com.orange.flexoffice.dao.common.model.object.AgentDto;
import com.orange.flexoffice.dao.common.model.object.BuildingDto;
import com.orange.flexoffice.dao.common.model.object.MeetingRoomDto;


public class MeetingRoomEndpointImpl implements MeetingRoomEndpoint {

	private static final Logger LOGGER = Logger.getLogger(MeetingRoomEndpointImpl.class);
	private final ObjectFactory factory = new ObjectFactory();

	@Autowired
	private MeetingRoomManager meetingroomManager;
	@Autowired
	private BuildingManager buildingManager;
	@Autowired
	private AgentManager agentManager;
	@Autowired
	private TestManager testManager;
	@Autowired
	private ErrorMessageHandler errorMessageHandler;

	@Override
	public List<MeetingRoomSummary> getMeetingRooms() {

		LOGGER.debug( "Begin call MeetingRoomEndpoint.getMeetingRooms at: " + new Date() );

		List<MeetingRoomDao> dataList = meetingroomManager.findAllMeetingRooms();
		
		if (dataList == null) {
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_69, Response.Status.NOT_FOUND));
		}

		List<MeetingRoomSummary> meetingroomList = new ArrayList<MeetingRoomSummary>();

		for (MeetingRoomDao meetingroomDao : dataList) {
			MeetingRoomSummary meetingroom = factory.createMeetingRoomSummary();
			meetingroom.setId(meetingroomDao.getColumnId());
			meetingroom.setName(meetingroomDao.getName());
			meetingroom.setExternalId(meetingroomDao.getExternalId());
			meetingroom.setType(EMeetingroomType.valueOf(meetingroomDao.getType().toString()));
			meetingroom.setAgent(getAgentFromId(Long.valueOf(meetingroomDao.getAgentId()), meetingroomDao.getName()));

			if (meetingroomDao.getStatus() != null) {
				meetingroom.setStatus(EMeetingroomStatus.valueOf(meetingroomDao.getStatus().toString()));
			}
			meetingroomList.add(meetingroom);
		}

		LOGGER.debug("List of meeting rooms : nb = " + meetingroomList.size());

		LOGGER.debug( "End call MeetingRoomEndpoint.getMeetingRooms  at: " + new Date() );

		return meetingroomList;
	}

	/* (non-Javadoc)
	 * @see com.orange.flexoffice.adminui.ws.endPoint.entity.MeetingRoomEndpoint#getMeetingRoom(java.lang.String)
	 */
	@Override
	public MeetingRoom getMeetingRoom(String meetingroomId) {

		LOGGER.debug( "Begin call MeetingRoomEndpoint.getMeetingRoom at: " + new Date() );

		try {
			MeetingRoomDto meetingroomDto = meetingroomManager.find(Long.valueOf(meetingroomId));

			MeetingRoom meetingroom = factory.createMeetingRoom();
			meetingroom.setId(String.valueOf(meetingroomDto.getId()));
			meetingroom.setName(meetingroomDto.getName());
			meetingroom.setType(EMeetingroomType.valueOf(meetingroomDto.getType().toString()));
			meetingroom.setAgent(getAgentFromId(Long.valueOf(meetingroomDto.getAgent().getId()), meetingroomDto.getName()));
			meetingroom.setDesc(meetingroomDto.getDescription());

			if (meetingroomDto.getCapacity() != null) {
				meetingroom.setCapacity(BigInteger.valueOf(meetingroomDto.getCapacity()));
			}			
			meetingroom.setStatus(EMeetingroomStatus.valueOf(meetingroomDto.getStatus().toString()));
			meetingroom.setOrganizerLabel(computeTenant(meetingroom.getStatus(), meetingroomDto.getOrganizerLabel(), meetingroomDto.getName()));
			meetingroom.setExternalId(meetingroomDto.getExternalId());
			
			if(meetingroomDto.getStartDate() != null)
			meetingroom.setStartDate(meetingroomDto.getStartDate().getTime());
			if(meetingroomDto.getEndDate() != null)
			meetingroom.setEndDate(meetingroomDto.getEndDate().getTime());
			
			BuildingDto buidingDto = buildingManager.find(Long.valueOf(meetingroomDto.getBuildingId()));
			Location location = factory.createLocation();
			BuildingOutput building = factory.createBuildingOutput();
			building.setId(String.valueOf(buidingDto.getId()));
			building.setName(buidingDto.getName());
			building.setAddress(buidingDto.getAddress());
			building.setNbFloors(BigInteger.valueOf(buidingDto.getNbFloors()));
			location.setBuilding(building);
			LocationItem locationCountry = factory.createLocationItem();
			locationCountry.setId(buidingDto.getCountryId().toString());
			locationCountry.setName(buidingDto.getCountryName());
			location.setCountry(locationCountry);
			LocationItem locationRegion = factory.createLocationItem();
			locationRegion.setId(buidingDto.getRegionId().toString());
			locationRegion.setName(buidingDto.getRegionName());
			location.setRegion(locationRegion);
			LocationItem locationCity = factory.createLocationItem();
			locationCity.setId(buidingDto.getCityId().toString());
			locationCity.setName(buidingDto.getCityName());
			location.setCity(locationCity);
			location.setFloor(BigInteger.valueOf(meetingroomDto.getFloor()));
			
			meetingroom.setLocation(location);
			
			LOGGER.debug( "End call RoomEndpoint.getRoom  at: " + new Date() );

			return meetingroom;

		} catch (DataNotExistsException e){
			LOGGER.debug("DataNotExistsException in MeetingRoomEndpoint.getMeetingRoom with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_73, Response.Status.NOT_FOUND));
		} catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in MeetingRoomEndpoint.getMeetingRoom with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}
	}


	/* (non-Javadoc)
	 * @see com.orange.flexoffice.adminui.ws.endPoint.entity.MeetingRoomEndpoint#addMeetingRoom
	 */
	@Override
	public MeetingRoomOutput addMeetingRoom(MeetingRoomInput meetingroomInput) {

		LOGGER.debug( "Begin call MeetingRoomEndpoint.addMeetingRoom  at: " + new Date() );
		
		try {
			
			MeetingRoomDao meetingroomDao = new MeetingRoomDao();
			meetingroomDao.setName(meetingroomInput.getName());
			meetingroomDao.setType(meetingroomInput.getType().toString());
			meetingroomDao.setBuildingId(Long.valueOf(meetingroomInput.getBuildingId()));
			meetingroomDao.setExternalId(meetingroomInput.getExternalId());
			
			if(meetingroomInput.getFloor() != null)
				meetingroomDao.setFloor(meetingroomInput.getFloor().longValue());
			
			if (meetingroomInput.getAgent() !=null) {
				AgentDto agent = agentManager.findByMacAddress(meetingroomInput.getAgent().getMacAddress());
				meetingroomDao.setAgentId(Long.valueOf(agent.getId()));
			} else {
				meetingroomDao.setAgentId(0L);
			}
			
			String desc = meetingroomInput.getDesc(); 
			if ( desc != null) { 
				meetingroomDao.setDescription(meetingroomInput.getDesc());
			}
			
			if(meetingroomInput.getCapacity() != null)
				meetingroomDao.setCapacity(meetingroomInput.getCapacity().intValue());
			
			meetingroomDao.setStatus(EMeetingroomStatus.UNKNOWN.toString());	
			
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug( "addMeetingRoom with parameters :");
				final StringBuilder message = new StringBuilder( 1000 );
				message.append( "name :" );
				message.append( meetingroomInput.getName() );
				message.append( "\n" );
				message.append( "agent id :" );
				message.append( meetingroomDao.getAgentId() );
				message.append( "\n" );
				LOGGER.debug( message.toString() );
			}

			meetingroomDao = meetingroomManager.save(meetingroomDao);
			
			MeetingRoomOutput returnedMeetingRoom = factory.createMeetingRoomOutput();
			returnedMeetingRoom.setId(meetingroomDao.getColumnId());
			returnedMeetingRoom.setName(meetingroomDao.getName());

			LOGGER.debug( "End call MeetingRoomEndpoint.addMeetingRoom at: " + new Date() );

			return factory.createMeetingRoomOutput(returnedMeetingRoom).getValue();


		} catch (DataNotExistsException e){
			LOGGER.debug("DataNotExistsException in MeetingRoomEndpoint.saveMeetingRoom with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_71, Response.Status.NOT_FOUND));
		} catch (DataAlreadyExistsException e) {
			LOGGER.debug("DataNotExistsException in MeetingRoomEndpoint.addMeetingRoom with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_70, Response.Status.METHOD_NOT_ALLOWED));
		} catch (RuntimeException ex) {
			LOGGER.debug("RuntimeException in MeetingRoomEndpoint.addMeetingRoom with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}
	}

	
	/* (non-Javadoc)
	 * @see com.orange.flexoffice.adminui.ws.endPoint.entity.MeetingRoomEndpoint#updateMeetingRoom
	 */
	@Override
	public Response updateMeetingRoom(String id, MeetingRoomInput meetingroomInput) {
		
		LOGGER.debug( "Begin call MeetingRoomEndpoint.updateMeetingRoom at: " + new Date() );

		try {
			MeetingRoomDao meetingroomDao = new MeetingRoomDao();
			meetingroomDao.setId(Long.valueOf(id));
			meetingroomDao.setName(meetingroomInput.getName());
			meetingroomDao.setType(meetingroomInput.getType().toString());
			meetingroomDao.setBuildingId(Long.valueOf(meetingroomInput.getBuildingId()));
			meetingroomDao.setExternalId(meetingroomInput.getExternalId());
			if(meetingroomInput.getFloor() != null)
			meetingroomDao.setFloor(meetingroomInput.getFloor().longValue());
	
			if (meetingroomInput.getAgent() !=null) {
				AgentDto agent = agentManager.findByMacAddressWithoutMeetingRoomInfo(meetingroomInput.getAgent().getMacAddress());
				meetingroomDao.setAgentId(Long.valueOf(agent.getId()));
			} else {
				meetingroomDao.setAgentId(0L);
			}
			
			String desc = meetingroomInput.getDesc(); 
			if ( desc != null) { 
				meetingroomDao.setDescription(meetingroomInput.getDesc());
			}
			if(meetingroomInput.getCapacity() != null)
			meetingroomDao.setCapacity(meetingroomInput.getCapacity().intValue());
			
			meetingroomDao = meetingroomManager.update(meetingroomDao);
	
			LOGGER.debug( "End call MeetingRoomEndpoint.updateMeetingRoom at: " + new Date() );
			return Response.status(Status.ACCEPTED).build();

		} catch (DataNotExistsException e){
			LOGGER.debug("DataNotExistsException in MeetingRoomEndpoint.updateMeetingRoom with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_71, Response.Status.NOT_FOUND));
		} catch (DataAlreadyExistsException e){
			LOGGER.debug("DataAlreadyExistsException in MeetingRoomEndpoint.updateMeetingRoom with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_71, Response.Status.METHOD_NOT_ALLOWED));
		}catch (RuntimeException ex){
			LOGGER.debug("RuntimeException in MeetingRoomEndpoint.updateMeetingRoom with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}

	}


	/* (non-Javadoc)
	 * @see com.orange.flexoffice.adminui.ws.endPoint.entity.MeetingRoomEndpoint#removeMeetingRoom
	 */
	@Override
	public Response removeMeetingRoom(String id) {
		
		LOGGER.debug( "Begin call MeetingRoomEndpoint.removeMeetingRoom at: " + new Date() );

		try {

			meetingroomManager.delete(Long.valueOf(id));

		} catch (DataNotExistsException e){
			
			LOGGER.debug("DataNotExistsException in MeetingRoomEndpoint.removeMeetingRoom with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_72, Response.Status.NOT_FOUND));
			
		} catch (IntegrityViolationException e){
			LOGGER.debug("IntegrityViolationException in MeetingRoomEndpoint.removeMeetingRoom with message :", e);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_72, Response.Status.METHOD_NOT_ALLOWED));
		} catch (RuntimeException ex){

			LOGGER.debug("RuntimeException in MeetingRoomEndpoint.removeMeetingRoom with message :", ex);
			throw new WebApplicationException(errorMessageHandler.createErrorMessage(EnumErrorModel.ERROR_32, Response.Status.INTERNAL_SERVER_ERROR));
		}

		LOGGER.debug( "End call MeetingRoomEndpoint.removeMeetingRoom at: " + new Date() );

		return Response.noContent().build();
	}
	
	/* (non-Javadoc)
	 * @see com.orange.flexoffice.adminui.ws.endPoint.entity.MeetingRoomEndpoint#findByName
	 */
	@Override
	public MeetingRoomDao findByName(String name) throws DataNotExistsException {
		return meetingroomManager.findByName(name);
	}

	/** Create Agent output from agent id
	 * 
	 * @param agentId
	 * @param meetingroomName
	 * 
	 * @return AgentOutput
	 */
	private AgentOutput getAgentFromId(final Long agentId, final String meetingroomName) {

		AgentOutput agent = factory.createAgentOutput();

		try {
			final AgentDto agentDto = agentManager.find(Long.valueOf(agentId));
			agent.setMacAddress(agentDto.getMacAddress());
			agent.setName(agentDto.getName());
			agent.setId(agentDto.getId());
			agent.setDesc(agentDto.getDescription());

		} catch (DataNotExistsException e) {
			LOGGER.warn("Get meetingrooms / Get meeting room id : Wrong Agent on meetingroom " + meetingroomName, e);
		}
		return agent;		
	}
	
	/** Create tenant if meeting room status is not free 
	 * 
	 * @param status
	 * @param organizerLabel
	 * @param meetingroomName
	 * 
	 * @return tenant
	 */
	private String computeTenant(final EMeetingroomStatus status, final String organizerLabel, final String meetingroomName) {

		String tenant = "";

		// Compute tenant name only if room is reserved or occupied
		if (status == EMeetingroomStatus.OCCUPIED) {
			tenant= organizerLabel;
		}

		return tenant;
	}
	
	// For test
	@Override
	public boolean initMeetingRoomStatsTable() {
		return testManager.initMeetingRoomStatsTableForAdminUI();
	}
}
