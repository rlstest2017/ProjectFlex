package com.orange.flexoffice.business.common.utils;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * DateTools
 * @author oab
 *
 */
public class DateTools {
	
	private static final Logger LOGGER = Logger.getLogger(DateTools.class);

	/**
	 * lastConnexionDate
	 * @return
	 */
	public Date lastConnexionDate(String lastConnexionDuration) {
		Date now = new Date();
		LOGGER.debug("Date now :" + now);
		//System.out.println("now :"+ now);
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.DAY_OF_YEAR, -Integer.valueOf(lastConnexionDuration)); // <--15 jour -->
		Date lastConnexionDate = cal.getTime();
		LOGGER.debug("last accepted Date " +lastConnexionDuration +" days later: " + lastConnexionDate);
		//System.out.println("lastConnexionDate :"+ lastConnexionDate);
		return lastConnexionDate;
	}
	
	/**
	 * reservationDateDelayBeforeTimeOut 
	 * Current Date + booking duration
	 * @param bookingDurationValue
	 * @return
	 */
	public Date reservationDateDelayBeforeTimeOut(Date reservationDate, int bookingDurationValue) {
		//System.out.println("reservationDate :"+ reservationDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(reservationDate);
		cal.add(Calendar.SECOND, bookingDurationValue); // <-- 300 secondes -->
		Date bookingDate = cal.getTime();
		//System.out.println("bookingDate :"+ bookingDate);
		return bookingDate; 
	}
	
	/**
	 * dateBeginDay
	 * @param beginDayValue
	 * @return
	 */
	public Date dateBeginDay(String beginDayValue) {
		String[] values = beginDayValue.split(":");
		String hours = values[0];
		String minutes = values[1];
		Date now = new Date();
		//System.out.println("now :"+ now);
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hours));
		cal.set(Calendar.MINUTE, Integer.valueOf(minutes));
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
		Date beginDayDate = cal.getTime();
		//System.out.println("beginDayDate :"+ beginDayDate);
		return beginDayDate;
	}
	
	/**
	 * dateEndDay
	 * @param endDayValue
	 * @return
	 */
	public Date dateEndDay(String endDayValue) {
		String[] values = endDayValue.split(":");
		String hours = values[0];
		String minutes = values[1];
		Date now = new Date();
		//System.out.println("now :"+ now);
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hours));
		cal.set(Calendar.MINUTE, Integer.valueOf(minutes));
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
		Date endDayDate = cal.getTime();
		//System.out.println("endDayDate :"+ endDayDate);
		return endDayDate;
	}
	
	public static void main(String[] args) {
		DateTools date = new DateTools();
		//date.lastConnexionDate("15");
		//date.reservationDateDelayBeforeTimeOut(new Date(), 300);
		//date.dateBeginDay("07:30");
		date.dateEndDay("20:00");
	}
	
}
