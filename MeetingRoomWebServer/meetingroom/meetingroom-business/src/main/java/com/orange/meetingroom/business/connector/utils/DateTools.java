package com.orange.meetingroom.business.connector.utils;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import org.springframework.stereotype.Component;

/**
 * DateTools
 * @author oab
 *
 */
@Component
public class DateTools {
	
	private static final Integer ONE_MINUTE_IN_MILLIS = 60000; //millisecs	
	
	/**
	 * isTime1BeforeTime2
	 * @param time1
	 * @param time2
	 * @param ackTime
	 * @return
	 */
	public Boolean isTime1BeforeTime2(Integer time1, Integer time2, Integer ackTime) {
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
	
	/**
	 * processStartDate
	 * @param nbSeconds
	 * @return
	 */
	public Integer processStartDate(Integer nbSeconds) {
		Date beginOfDay = beginOfDay();
		Timestamp later = new Timestamp(beginOfDay.getTime() + (nbSeconds * 1000L));
		return (int)(long)(later.getTime()/1000L);
	}
	
	/**
	 * beginOfDay
	 * @param dailyDate
	 * @return
	 */
	// hh:00; mm:00; ss:00
	private Date beginOfDay() {
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
		return cal.getTime();
	}

}
