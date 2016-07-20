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
	public Boolean isTime1BeforeOrEqualsTime2(Integer time1, Integer time2, Integer ackTime) {
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
		if (date1AfterAddingAckTime.before(date2) || date1AfterAddingAckTime.equals(date2)) {
			status = true;
		}
		
		return status;
	}
	
	/**
	 * processStartDate
	 * @param startTime
	 * @param nbSeconds
	 * @return
	 */
	public Integer processDate(Integer startTime, Integer nbSeconds) {
		Timestamp later = new Timestamp(startTime + (nbSeconds));
		return (int)(long)(later.getTime());
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
	
	/**
	 * DayWithHour
	 * @param dailyDate
	 * @return
	 */
	// hh:Hour; mm:00; ss:00
	public Integer DayWithHour(Integer Hour) {
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		cal.set(Calendar.HOUR_OF_DAY, Hour);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
		return (int)(long)(cal.getTime().getTime()/1000L);
	}
	
	public static void main(String[] args) {
		DateTools tool = new DateTools();
		//Integer startTime = 1466571600;
		//Integer result = tool.processDate(startTime, 10);
		Integer today = tool.DayWithHour(20);
		System.out.println("today is:" + today);
		Date date1 = new Date(today * 1000L); // booking start Date
		Date date2 = new Date(today * 1000L); // php server current Date
		if (date1.before(date2) || date1.equals(date2)) {
			System.out.println("date1 is:" + date1);
			System.out.println("date2 is:" + date2);
		}
	}

}
