package com.orange.flexoffice.adminui.ws.utils;


import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.orange.flexoffice.adminui.ws.model.ObjectFactory;
import com.orange.flexoffice.business.common.exception.DataNotExistsException;
import com.orange.flexoffice.business.common.exception.IntegrityViolationException;
import com.orange.flexoffice.business.common.service.data.CountryManager;

/**
 * CountryHandler
 * @author oab
 *
 */
public class CountryHandler {
	
	@Autowired
	private CountryManager countryManager;
	private final ObjectFactory factory = new ObjectFactory();
	private static final Logger LOGGER = Logger.getLogger(CountryHandler.class);
	
	/**
	 * removeCountry
	 * @param id
	 * @return
	 * @throws DataNotExistsException
	 * @throws IntegrityViolationException
	 */
	public Response removeCountry(String id) throws DataNotExistsException, IntegrityViolationException {
		countryManager.delete(Long.valueOf(id));
		return Response.noContent().build();
	}
}
