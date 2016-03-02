package com.orange.flexoffice.adminui.ws.utils;


import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.adminui.ws.model.ObjectFactory;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.business.common.service.data.RegionManager;

/**
 * RegionHandler
 * @author oab
 *
 */
public class RegionHandler {
	
	@Autowired
	private RegionManager regionManager;
	private final ObjectFactory factory = new ObjectFactory();
	private static final Logger LOGGER = Logger.getLogger(RegionHandler.class);
	
	/**
	 * removeRegion
	 * @param id
	 * @return
	 * @throws DataNotExistsException
	 * @throws IntegrityViolationException
	 */
	public Response removeRegion(String id) throws DataNotExistsException, IntegrityViolationException {
		regionManager.delete(Long.valueOf(id));
		return Response.noContent().build();
	}
}
