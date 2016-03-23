package com.orange.flexoffice.business.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.FormatFlagsConversionMismatchException;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * DateTools
 * @author oab
 *
 */
public class DateTools {
	
	private static final Logger LOGGER = Logger.getLogger(DateTools.class);

	/**
	 * isWorkingDay
	 * @return
	 */
	public Boolean isWorkingDay(Date day) {
		Boolean state = true;
		Calendar cal = Calendar.getInstance();
		cal.setTime(day);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
			state = false;
		}
		return state;
	}
	
	/**
	 * lastConnexionDate
	 * @param lastConnexionDuration
	 * @return
	 */
	public Date lastConnexionDate(String lastConnexionDuration) {
		Date now = new Date();
		LOGGER.debug("Date now :" + now);
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.DAY_OF_YEAR, -Integer.valueOf(lastConnexionDuration)); // <--15 jour -->
		Date lastConnexionDate = cal.getTime();
		LOGGER.debug("last accepted Date " +lastConnexionDuration +" days later: " + lastConnexionDate);
		return lastConnexionDate;
	}
	
	/**
	 * lastAcceptedStatDate
	 * @param keepStatDataInDaysValue
	 * @return
	 */
	public Date lastAcceptedStatDate(String keepStatDataInDaysValue) {
		Date now = new Date();
		LOGGER.debug("Date now :" + now);
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.DAY_OF_YEAR, -Integer.valueOf(keepStatDataInDaysValue)); // <--365 jour -->
		Date lastAcceptedStatDate = cal.getTime();
		LOGGER.debug("last accepted Date " +keepStatDataInDaysValue +" days later: " + lastAcceptedStatDate);
		return lastAcceptedStatDate;
	}
	
	/**
	 * reservationDateDelayBeforeTimeOut 
	 * Current Date + booking duration
	 * @param bookingDurationValue
	 * @return
	 */
	public Date reservationDateDelayBeforeTimeOut(Date reservationDate, int bookingDurationValue) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(reservationDate);
		cal.add(Calendar.SECOND, bookingDurationValue); // <-- 300 secondes -->
		return cal.getTime();
	}
	
	/**
	 * calculate the max authorized date since last teachinDate + 15 minutes
	 * @param teachinDate
	 * @param teachinTimeoutValue
	 * @return
	 */
	public Date teachinDateDelayBeforeTimeOut(Date teachinDate, int teachinTimeoutValue) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(teachinDate);
		cal.add(Calendar.MINUTE, teachinTimeoutValue); // <-- 15 minutes -->
		return cal.getTime();
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
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hours));
		cal.set(Calendar.MINUTE, Integer.valueOf(minutes));
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
		return cal.getTime();
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
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hours));
		cal.set(Calendar.MINUTE, Integer.valueOf(minutes));
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
		return cal.getTime();
	}
	
	// hh:00; mm:00; ss:00
	/**
	 * beginOfDay
	 * @param dailyDate
	 * @return
	 */
	public Date beginOfDay(Date dailyDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dailyDate);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
		return cal.getTime();
	}
	
	// hh:23; mm:59; ss:59
	/**
	 * endOfDay
	 * @param dailyDate
	 * @return
	 */
	public Date endOfDay(Date dailyDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dailyDate);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND,59);
		cal.set(Calendar.MILLISECOND,0);
		return cal.getTime();
	}
		
	/**
	 * calculateDuration in seconds
	 * @return
	 */
	public Long calculateDuration(Date beginOccupancyDate, Date endOccupancyDate) {
		Long occupancyDuration = endOccupancyDate.getTime()-beginOccupancyDate.getTime();
		occupancyDuration = occupancyDuration/1000;
		return occupancyDuration;
	}
	
	/**
	 * isDateInList
	 * @param dateList
	 * @param toCompare
	 * @return
	 */
	public Boolean isDateInList(List<Date> dateList, Date toCompare) {
		Boolean state = false;
		for (Date date : dateList) {
			if (date.getTime() == toCompare.getTime()) {
				state = true;
				break;
			}
		}
		
		return state;
	}
	
	/**
	 * isWeekInList
	 * @param dateList
	 * @param toCompare
	 * @return
	 */
	public Boolean isWeekInList(List<Date> dateList, Date toCompare) {
		Boolean state = false;
		
		Calendar cal = Calendar.getInstance();
	    cal.setTime(toCompare);
	    int yearToCompare = cal.get(Calendar.YEAR);
	    int monthToCompare = cal.get(Calendar.MONTH);
	    int weekToCompare = cal.get(Calendar.WEEK_OF_MONTH);
	    
		for (Date date : dateList) {
			cal.setTime(date);
		    int year = cal.get(Calendar.YEAR);
		    int month = cal.get(Calendar.MONTH);
		    int week = cal.get(Calendar.WEEK_OF_MONTH);
			if ((monthToCompare == month)&&(yearToCompare == year)&&(weekToCompare == week)) {
				state = true;
				break;
			}
		}
		
		return state;
	}
	
	/**
	 * isMonthInList
	 * @param dateList
	 * @param toCompare
	 * @return
	 */
	public Boolean isMonthInList(List<Date> dateList, Date toCompare) {
		Boolean state = false;
		
		Calendar cal = Calendar.getInstance();
	    cal.setTime(toCompare);
	    int yearToCompare = cal.get(Calendar.YEAR);
	    int monthToCompare = cal.get(Calendar.MONTH);
	    
		for (Date date : dateList) {
			cal.setTime(date);
		    int year = cal.get(Calendar.YEAR);
		    int month = cal.get(Calendar.MONTH);
			if ((monthToCompare == month)&&(yearToCompare == year)) {
				state = true;
				break;
			}
		}
		
		return state;
	}
	
	/**
	 * getDateFromString
	 * @param dateInString
	 * @return
	 */
	public Date getDateFromString(String dateInString) {
		try {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		Date date = null;
		if ("0".equals(dateInString)) { // default value
			date = new Date(0L);
		} else {
		date = formatter.parse(dateInString);
		}
		return date;
		
		} catch (ParseException e) {
			LOGGER.error("Format Date exeption in DateTools.getDateFromString :" + dateInString);
			throw new FormatFlagsConversionMismatchException(e.getMessage(), ' ');
		}
	}
	
	/**
	 * getFirstDayOfMonth
	 * @param dateInString
	 * @param dateParameter
	 * @return
	 */
	public Date getFirstDayOfMonth(String dateInString, Date dateParameter) {
		Calendar cal = Calendar.getInstance();
		Date date = null;
		Date dayDate = null;
		if (dateParameter == null) {
			date = getDateFromString(dateInString);
		} else {
			date = dateParameter;
		}
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		if (year != 1970) {
			cal.set(Calendar.DAY_OF_MONTH, 01);
			cal.set(Calendar.HOUR_OF_DAY, 00);
			cal.set(Calendar.MINUTE, 00);
			cal.set(Calendar.SECOND,00);
			cal.set(Calendar.MILLISECOND,0);
			dayDate = cal.getTime();
		} else {
			dayDate = date;
		}
		
		return dayDate;
	}

	/**
	 * getLastDayOfMonth
	 * @param dateInString
	 * @return
	 */
	public Date getLastDayOfMonth(String dateInString, Date dateParameter) {
		Calendar cal = Calendar.getInstance();
		Date date = null;
		if (dateParameter == null) {
			date = getDateFromString(dateInString);
		} else {
			date = dateParameter;
		}
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND,59);
		cal.set(Calendar.MILLISECOND,0);
		Date dayDate = cal.getTime();
		
		return dayDate;
	}
	
	/**
	 * getFirstDayOfWeek
	 * @param dateInString
	 * @param dateParameter
	 * @return
	 */
	public Date getFirstDayOfWeek(String dateInString, Date dateParameter) {
		Calendar cal = Calendar.getInstance();
		Date date = null;
		Date dayDate = null;
		if (dateParameter == null) {
			date = getDateFromString(dateInString);
		} else {
			date = dateParameter;
		}
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		if (year != 1970) {
			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			cal.set(Calendar.HOUR_OF_DAY, 00);
			cal.set(Calendar.MINUTE, 00);
			cal.set(Calendar.SECOND,00);
			cal.set(Calendar.MILLISECOND,0);
			dayDate = cal.getTime();
		} else {
			dayDate = date;
		}
		
		return dayDate;
	}
	
	/**
	 * getLastDayOfWeek
	 * @param dateInString
	 * @param dateParameter
	 * @return
	 */
	public Date getLastDayOfWeek(String dateInString, Date dateParameter) {
		Calendar cal = Calendar.getInstance();
		Date date = null;
		if (dateParameter == null) {
			date = getDateFromString(dateInString);
		} else {
			date = dateParameter;
		}
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND,59);
		cal.set(Calendar.MILLISECOND,0);
		Date dayDate = cal.getTime();
		
		return dayDate;
	}
	
	/**
	 * getNumberOfWeek
	 * @param date
	 * @return
	 */
	public int getNumberOfWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int num = cal.get(Calendar.WEEK_OF_YEAR);
		return num;
	}
	
	/**
	 * nbJoursOuvrableByMonth
	 * @param d1
	 * @param d2
	 * @param notionJourFerie
	 * @param priseCompteLundi
	 * @param priseCompteMardi
	 * @param priseCompteMercredi
	 * @param priseCompteJeudi
	 * @param priseCompteVendredi
	 * @param priseCompteSamedi
	 * @param priseCompteDimanche
	 * @return
	 */
	public int nbJoursOuvrable(Date d1, Date d2, boolean notionJourFerie,
			boolean priseCompteLundi, boolean priseCompteMardi,
			boolean priseCompteMercredi, boolean priseCompteJeudi,
			boolean priseCompteVendredi, boolean priseCompteSamedi,
			boolean priseCompteDimanche) {

		LOGGER.debug("d1 in nbJoursOuvrable() is :" + d1);
		LOGGER.debug("d2 in nbJoursOuvrable() is :" + d2);
		
		if (d2.compareTo(d1) <= 0) {
			return 0;
		}
		
		// Tableau des jours a prendre en compte
		Boolean[] joursPrisEncompte = new Boolean[] { priseCompteDimanche,
				priseCompteLundi, priseCompteMardi, priseCompteMercredi,
				priseCompteJeudi, priseCompteVendredi, priseCompteSamedi };

		GregorianCalendar date1 = new GregorianCalendar();
		date1.setTime(d1);
		GregorianCalendar date2 = new GregorianCalendar();
		date2.setTime(d2);

		LOGGER.debug("date1 in nbJoursOuvrable() is :" + date1.getTime());
		LOGGER.debug("date2 in nbJoursOuvrable() is :" + date2.getTime());
		
		// Récupération des jours fériés
		List<Date> joursFeries = new ArrayList<Date>();
		for (int i = date1.get(GregorianCalendar.YEAR); i <= date2
				.get(GregorianCalendar.YEAR); i++) {
			joursFeries.addAll(getJourFeries(i));
		}

		// Calcul du nombre de jour
		int nbJour = 0;
		while (date1.before(date2) || date1.equals(date2)) {
			if (!notionJourFerie || !joursFeries.contains(date1.getTime())) {
				if (joursPrisEncompte[date1.get(GregorianCalendar.DAY_OF_WEEK) - 1]) {
					nbJour++;
					LOGGER.debug("nbjour incremented is :" + nbJour);
					LOGGER.debug("date1 incremented is :" + date1.getTime());
				}
			}

			date1.add(GregorianCalendar.DAY_OF_MONTH, 1);
			LOGGER.debug("date1 after add calendar is :" + date1.getTime());
		}

		return nbJour;
	}
		
	/**
	 * getJourFeries
	 * @param annee
	 * @return
	 */
	private List<Date> getJourFeries(int annee) {
		List<Date> datesFeries = new ArrayList<Date>();

		// Jour de l'an
		GregorianCalendar jourAn = new GregorianCalendar(annee, 0, 1);
		datesFeries.add(jourAn.getTime());

		// Lundi de pacques
		GregorianCalendar pacques = calculLundiPacques(annee);
		datesFeries.add(pacques.getTime());

		// Fete du travail
		GregorianCalendar premierMai = new GregorianCalendar(annee, 4, 1);
		datesFeries.add(premierMai.getTime());

		// 8 mai
		GregorianCalendar huitMai = new GregorianCalendar(annee, 4, 8);
		datesFeries.add(huitMai.getTime());

		// Ascension (= pâques + 38 jours)
		GregorianCalendar ascension = new GregorianCalendar(annee,
				pacques.get(GregorianCalendar.MONTH),
				pacques.get(GregorianCalendar.DAY_OF_MONTH));
		ascension.add(GregorianCalendar.DAY_OF_MONTH, 38);
		datesFeries.add(ascension.getTime());

		// Pentecôte (= pâques + 49 jours)
		GregorianCalendar pentecote = new GregorianCalendar(annee,
				pacques.get(GregorianCalendar.MONTH),
				pacques.get(GregorianCalendar.DAY_OF_MONTH));
		pentecote.add(GregorianCalendar.DAY_OF_MONTH, 49);
		datesFeries.add(pentecote.getTime());

		// Fête Nationale
		GregorianCalendar quatorzeJuillet = new GregorianCalendar(annee, 6, 14);
		datesFeries.add(quatorzeJuillet.getTime());

		// Assomption
		GregorianCalendar assomption = new GregorianCalendar(annee, 7, 15);
		datesFeries.add(assomption.getTime());

		// La Toussaint
		GregorianCalendar toussaint = new GregorianCalendar(annee, 10, 1);
		datesFeries.add(toussaint.getTime());

		// L'Armistice
		GregorianCalendar armistice = new GregorianCalendar(annee, 10, 11);
		datesFeries.add(armistice.getTime());

		// Noël
		GregorianCalendar noel = new GregorianCalendar(annee, 11, 25);
		datesFeries.add(noel.getTime());

		return datesFeries;
	}

	/**
	 * calculLundiPacques
	 * @param annee
	 * @return
	 */
	private GregorianCalendar calculLundiPacques(int annee) {
		int a = annee / 100;
		int b = annee % 100;
		int c = (3 * (a + 25)) / 4;
		int d = (3 * (a + 25)) % 4;
		int e = (8 * (a + 11)) / 25;
		int f = (5 * a + b) % 19;
		int g = (19 * f + c - e) % 30;
		int h = (f + 11 * g) / 319;
		int j = (60 * (5 - d) + b) / 4;
		int k = (60 * (5 - d) + b) % 4;
		int m = (2 * j - k - g + h) % 7;
		int n = (g - h + m + 114) / 31;
		int p = (g - h + m + 114) % 31;
		int jour = p + 1;
		int mois = n;

		GregorianCalendar date = new GregorianCalendar(annee, mois - 1, jour);
		date.add(GregorianCalendar.DAY_OF_MONTH, 1);
		return date;
	}
	
	/*
	public static void main(String[] args) throws ParseException {
		DateTools date = new DateTools();
		System.out.println("stardate is:" + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date(1449702000000L)));
		System.out.println("enddate is:" + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date(1450047600000L)));
		
		Date valueBegin = date.getFirstDayOfWeek("2015-12-23T15:00:22.806Z", null);
		System.out.println("Begin month date is : " + valueBegin);
		Date valueEnd = date.getLastDayOfWeek("2015-12-23T15:00:22.806Z", null);
		System.out.println("End month date is : " + valueEnd);
		
		int nb = date.nbJoursOuvrable(valueBegin, valueEnd, true, true, true, true, true, true, false, false);
		System.out.println("NB : " + nb);
		
		int num = date.getNumberOfWeek(new Date(1451602799000L));
		System.out.println("num : " + num);

	}*/
}
