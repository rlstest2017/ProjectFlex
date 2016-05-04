package com.orange.flexoffice.business.common.service.data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.orange.flexoffice.business.common.exception.DataAlreadyExistsException;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.dao.common.model.data.BuildingDao;
import com.orange.flexoffice.dao.common.model.object.BuildingDto;
import com.orange.flexoffice.dao.common.model.object.BuildingSummaryDto;

/**
 * BuildingManager
 * @author oab
 *
 */
public interface BuildingManager {
	/**
	 * findAllBuildings method used by adminui
	 * @return
	 */
	List<BuildingSummaryDto> findAllBuildings();

	/**
	 * findBuildingsByCityId method used by adminui & userui
	 * @return
	 */
	List<BuildingDao> findBuildingsByCityId(String cityId, boolean isFromAdminUI);

	/**
	 * Finds a building by its ID.
	 * method used by adminui
	 * @param buildingId
	 * 		  the {@link buildingId} ID
	 * @return a {@link BuildingDto}
	 */
	BuildingDto find(long buildingId)  throws DataNotExistsException;

	/**
	 * Saves a {@link BuildingDao}
	 * method used by adminui
	 * @param BuildingDao
	 * 		  the new {@link BuildingDao}
	 * @return a saved {@link BuildingDao}
	 * @throws DataAlreadyExistsException 
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 * @throws JAXBException 
	 * @throws IOException 
	 * @throws DataNotExistsException 
	 */
	BuildingDao save(BuildingDao building) throws DataAlreadyExistsException, UnsupportedEncodingException, FileNotFoundException, IOException, JAXBException, DataNotExistsException;

	/**
	 * Updates a {@link BuildingDao}
	 * method used by adminui
	 * @param BuildingDao
	 * 		  the new {@link BuildingDao}
	 * @return a saved {@link BuildingDao}
	 * @throws JAXBException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	BuildingDao update(BuildingDao building) throws DataNotExistsException, IOException, JAXBException, ParserConfigurationException, SAXException;

	/**
	 * Delete a building
	 * method used by adminui
	 * @param buildingId 
	 * 		  a building ID
	 */
	void delete(long buildingId) throws DataNotExistsException, IntegrityViolationException;
	
}