package com.orange.meetingroom.business.connector.utils;

import java.text.ParseException;
import java.util.Date;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * DateTools
 * @author oab
 *
 */
@Component
public class DateTools {
	
	private static final Logger LOGGER = Logger.getLogger(DateTools.class);

	/**
	 * isTime1BeforeTime2
	 * @param time1
	 * @param time2
	 * @param ackTime
	 * @return
	 */
	public Boolean isTime1BeforeTime2(Integer time1, Integer time2, Integer ackTime) {
		final Integer ONE_MINUTE_IN_MILLIS = 60000;//millisecs
		Boolean status = false;
		
		Date date1 = new Date(time1 * 1000L); // booking start Date
		Date date2 = new Date(time2 * 1000L); // php server current Date
		Date date1AfterAddingAckTime; // booking start Date + ackTime in minutes
		
		if (ackTime != 0) {
			long calculatedTime = date1.getTime() + (ackTime * ONE_MINUTE_IN_MILLIS);
			date1AfterAddingAckTime = new Date(calculatedTime);
		} else {
			date1AfterAddingAckTime = date1;
		}
		
		if (date1AfterAddingAckTime.before(date2)) {
			status = true;
		}
		
		return status;
	}
	
	
	
	public static void main(String[] args) throws ParseException {
		
		//System.out.println("currentTime" + date);
		
		DateTools dateTool = new DateTools();
		Integer debut = 1464078960; // 10:38:00
		Integer fin = 1464079800; // 10:50:00
		
		dateTool.isTime1BeforeTime2(debut, fin, 10);
		

	}
}
