package com.orange.flexoffice.dao.common.utils;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.util.Base64Utils;

/**
 * TokenTools
 * @author oab
 *
 */
public class TokenTools {
	
	private static final Logger LOGGER = Logger.getLogger(TokenTools.class);
	
	/**
	 * createAccessToken
	 * @param email
	 * @param password
	 * @return
	 */
	public String createAccessToken(String email, String password) {
		String keySource = null;
		if (password != null) {
			keySource = email + ":" + password + ":" + (new Date()).getTime();
		} else {
			keySource = email + ":" + (new Date()).getTime();
		}
		LOGGER.debug("Original keySource is " + keySource);
		String token = Base64Utils.encodeToString(keySource.getBytes());
		LOGGER.debug("Generated keySource is " + token);
		return token;
	}
	
	/**
	 * createExpiredDate
	 * @return
	 */
	public Date createExpiredDate() {
		Date now = new Date();
		LOGGER.debug("Date now :" + now);
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.DAY_OF_YEAR, 1); // <--1 jour -->
		Date tomorrow = cal.getTime();
		LOGGER.debug("Date tomorrow :" + tomorrow);		
		return tomorrow;
	}

}
