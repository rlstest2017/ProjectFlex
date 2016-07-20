package com.orange.flexoffice.business.common.service.data.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.business.common.service.data.AlertManager;
import com.orange.flexoffice.business.common.service.data.DashboardManager;
import com.orange.flexoffice.dao.common.model.data.AlertDao;
import com.orange.flexoffice.dao.common.model.data.DashboardDao;
import com.orange.flexoffice.dao.common.model.enumeration.E_CommandModel;
import com.orange.flexoffice.dao.common.model.enumeration.E_DashboardStatus;
import com.orange.flexoffice.dao.common.model.object.DashboardDto;
import com.orange.flexoffice.dao.common.repository.data.jdbc.AlertDaoRepository;
import com.orange.flexoffice.dao.common.repository.data.jdbc.DashboardDaoRepository;

/**
 * Manages {@link DashboardDto}.
 * For PROD LOG LEVEL is info then we say info & error logs.
 * 
 * @author oab
 */
@Service("DashboardManager")
@Transactional
public class DashboardManagerImpl implements DashboardManager {
	
	private static final Logger LOGGER = Logger.getLogger(DashboardManagerImpl.class);
	
	@Autowired
	private DashboardDaoRepository dashboardRepository;
	@Autowired
	private AlertDaoRepository alertRepository;
	@Autowired
	private AlertManager alertManager;
	
	@Override
	@Transactional(readOnly=true)
	public List<DashboardDao> findAllDashboards() {
		return dashboardRepository.findAllDashboards();
	}


	@Override
	@Transactional(readOnly=true)
	public DashboardDto find(long dashboardId)  throws DataNotExistsException {
		
		DashboardDao dashboardDao = dashboardRepository.findOne(dashboardId);
		
		if (dashboardDao == null) {
			LOGGER.error("dashboard by id " + dashboardId + " is not found");
			throw new DataNotExistsException("Dashboard not exist");
		}
		
		DashboardDto dto = new DashboardDto();
		dto.setId(String.valueOf(dashboardId));
		dto.setDescription(dashboardDao.getDescription());
		dto.setLastMeasureDate(dashboardDao.getLastMeasureDate());
		dto.setMacAddress(dashboardDao.getMacAddress());
		dto.setName(dashboardDao.getName());
		dto.setStatus(E_DashboardStatus.valueOf(dashboardDao.getStatus()));
		dto.setCityId(dashboardDao.getCityId());
		if (dashboardDao.getBuildingId() != null){
			dto.setBuildingId(dashboardDao.getBuildingId());
		}
		if (dashboardDao.getFloor() != null){
			dto.setFloor(dashboardDao.getFloor());
		}
		
		if (LOGGER.isDebugEnabled()) {
            LOGGER.debug( "Return find(long dashboardId) method for DashbaordManagerImpl, with parameters :");
            final StringBuilder message = new StringBuilder( 100 );
            message.append( "id :" );
            message.append( String.valueOf(dashboardId) );
            message.append( "\n" );
            message.append( "macAddress :" );
            message.append( dashboardDao.getMacAddress() );
            message.append( "\n" );
            message.append( "name :" );
            message.append( dashboardDao.getName() );
            message.append( "\n" );
            LOGGER.debug( message.toString() );
        }
		
		return dto;
	}

	@Override
	@Transactional(readOnly=true)
	public DashboardDto findByMacAddress(String macAddress)  throws DataNotExistsException {
			try {
				DashboardDao dashboardDao = dashboardRepository.findByMacAddress(macAddress);
				
				DashboardDto dto = new DashboardDto();
				dto.setId(dashboardDao.getColumnId());
				dto.setDescription(dashboardDao.getDescription());
				dto.setLastMeasureDate(dashboardDao.getLastMeasureDate());
				dto.setMacAddress(macAddress);
				dto.setName(dashboardDao.getName());
				dto.setStatus(E_DashboardStatus.valueOf(dashboardDao.getStatus()));
				dto.setCityId(dashboardDao.getCityId());
				if (dashboardDao.getBuildingId() != null){
					dto.setBuildingId(dashboardDao.getBuildingId());
				}
				if (dashboardDao.getFloor() != null){
					dto.setFloor(dashboardDao.getFloor());
				}
				
				if (dashboardDao.getCommand() != null) {
					dto.setCommand(E_CommandModel.valueOf(dashboardDao.getCommand()));
				}
				
				if (LOGGER.isDebugEnabled()) {
		            LOGGER.debug( "Return findByMacAddress(String macAddress) method for DashboardManagerImpl, with parameters :");
		            final StringBuilder message = new StringBuilder( 100 );
		            message.append( "id :" );
		            message.append( dashboardDao.getColumnId() );
		            message.append( "\n" );
		            message.append( "macAddress :" );
		            message.append( macAddress );
		            message.append( "\n" );
		            message.append( "name :" );
		            message.append( dashboardDao.getName() );
		            message.append( "\n" );
		            LOGGER.debug( message.toString() );
		        }
				
				return dto;
	
			} catch(IncorrectResultSizeDataAccessException e ) {
				LOGGER.debug("dashboard by macAddress " + macAddress + " is not found", e);
				LOGGER.error("dashboard by macAddress " + macAddress + " is not found");
				throw new DataNotExistsException("Dashboard not exist");
			}
	}

	@Override
	public DashboardDao save(DashboardDao dashboardDao) throws DataAlreadyExistsException {
		try {
			// Save AgentDao
			return dashboardRepository.saveDashboard(dashboardDao);
		} catch (DataIntegrityViolationException e) {
			LOGGER.debug("DataIntegrityViolationException in save() DashboardManagerImpl with message :" + e.getMessage(), e);
			LOGGER.error("DataIntegrityViolationException in save() DashboardManagerImpl with message :" + e.getMessage());
			throw new DataAlreadyExistsException("dashboard already exists.");
		}
	}

	@Override
	public DashboardDao update(DashboardDao dashboardDao) throws DataNotExistsException {
		try {	
			dashboardRepository.findByMacAddress(dashboardDao.getMacAddress());
			// update DashboardDao
			return dashboardRepository.updateDashboard(dashboardDao);
		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.debug("DataAccessException in update() DashboardManagerImpl with message :" + e.getMessage(), e);
			LOGGER.error("DataAccessException in update() DashboardManagerImpl with message :" + e.getMessage());
			throw new DataNotExistsException("dashboard not exist");
		}
	}

	@Override
	public DashboardDao updateStatus(DashboardDao dashboardDao) throws DataNotExistsException {
		try {	
			DashboardDao dashboard = dashboardRepository.findByMacAddress(dashboardDao.getMacAddress());
			dashboardDao.setId(dashboard.getId());
			dashboardDao.setName(dashboard.getName());
			dashboardDao.setDescription(dashboard.getDescription());
			dashboardDao.setCityId(dashboard.getCityId());
			if (dashboard.getBuildingId() != null){
				dashboardDao.setBuildingId(dashboard.getBuildingId());
			}
			if (dashboard.getFloor() != null){
				dashboardDao.setFloor(dashboard.getFloor());
			}
			if(dashboard.getCommand() == null){
				dashboardDao.setCommand(E_CommandModel.NONE.toString());
			} else {
				dashboardDao.setCommand(dashboard.getCommand());
			}
			
			// update DashboardDao
			DashboardDao dashboardToRet = dashboardRepository.updateDashboardStatus(dashboardDao);
			
			// update Dashboard Alert
			Long dashboardId = dashboardDao.getId();
			alertManager.updateDashboardAlert(dashboardId, dashboardDao.getStatus());
			
			// Set to NONE agent command after getting command to send 
			DashboardDao dashboardUpdateCommand = new DashboardDao();
			dashboardUpdateCommand.setMacAddress(dashboard.getMacAddress());
			dashboardUpdateCommand.setId(dashboard.getId());
			dashboardUpdateCommand.setName(dashboard.getName());
			dashboardUpdateCommand.setDescription(dashboard.getDescription());
			dashboardUpdateCommand.setCityId(dashboard.getCityId());
			if (dashboard.getBuildingId() != null){
				dashboardUpdateCommand.setBuildingId(dashboard.getBuildingId());
			}
			if (dashboard.getFloor() != null){
				dashboardUpdateCommand.setFloor(dashboard.getFloor());
			}
			dashboardUpdateCommand.setCommand(E_CommandModel.NONE.toString());
			dashboardRepository.updateDashboard(dashboardUpdateCommand);
			
			return dashboardToRet;
		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.debug("DataAccessException in update() DashboardManagerImpl with message :" + e.getMessage(), e);
			LOGGER.error("DataAccessException in update() DashboardManagerImpl with message :" + e.getMessage());
			throw new DataNotExistsException("Dashboard not exist");
		}
	}

	@Override
	public void delete(String macAddress) throws DataNotExistsException, IntegrityViolationException {
		try {
			DashboardDao dashboard = dashboardRepository.findByMacAddress(macAddress);
			try {
				AlertDao alert = alertRepository.findByDashboardId(dashboard.getId());
				if (alert != null) {
					// delete alert
					alertRepository.delete(alert.getId());
					// delete dashboard
					dashboardRepository.deleteByMacAddress(macAddress);
				}
			} catch(IncorrectResultSizeDataAccessException e ) {
				LOGGER.debug("dashboard by macAddress " + macAddress + " has not alert", e);
				LOGGER.info("dashbaord by macAddress " + macAddress + " has not alert");
				// delete dashboard
				dashboardRepository.deleteByMacAddress(macAddress);
			}

		} catch(IncorrectResultSizeDataAccessException e ) {
			LOGGER.debug("dashboard by macAddress " + macAddress + " is not found", e);
			LOGGER.error("dashboard by macAddress " + macAddress + " is not found");
			throw new DataNotExistsException("Agent not exist");
		} catch(DataIntegrityViolationException e ) {
			LOGGER.debug("DashboardManager.delete : Dashboard associated to a meeting room", e);
			LOGGER.error("DashboardManager.delete : Dashboard associated to a meeting room");
			throw new IntegrityViolationException("DashboardManager.delete : Dashboard associated to a meeting room");
		}
	}	
}
