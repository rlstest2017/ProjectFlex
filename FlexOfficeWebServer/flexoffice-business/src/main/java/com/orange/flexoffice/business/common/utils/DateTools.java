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
	
	// hh:00; mm:00; ss:00
	/**
	 * beginOfDay
	 * @param dailyDate
	 * @return
	 */
	public Date beginOfDay(Date dailyDate) {
		
		//System.out.println("now :"+ now);
		Calendar cal = Calendar.getInstance();
		cal.setTime(dailyDate);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
		Date dayDate = cal.getTime();
		//System.out.println("dayDate :"+ dayDate);
		return dayDate;
	}
	
	// hh:23; mm:59; ss:59
		/**
		 * endOfDay
		 * @param dailyDate
		 * @return
		 */
		public Date endOfDay(Date dailyDate) {
			
			//System.out.println("now :"+ now);
			Calendar cal = Calendar.getInstance();
			cal.setTime(dailyDate);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND,59);
			cal.set(Calendar.MILLISECOND,0);
			Date dayDate = cal.getTime();
			//System.out.println("dayDate :"+ dayDate);
			return dayDate;
		}
		
	/**
	 * calculateDuration in seconds
	 * @return
	 */
	public Long calculateDuration(Date beginOccupancyDate, Date endOccupancyDate) {
		Long occupancyDuration = endOccupancyDate.getTime()-beginOccupancyDate.getTime();
		occupancyDuration = occupancyDuration/1000;
		//System.out.println("Le temps est :" + occupancyDuration);
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
		Date date = null;;
		if (dateInString.equals("0")) { // default value
			date = new Date(0l);
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
	public int nbJoursOuvrableByMonth(Date d1, Date d2, boolean notionJourFerie,
			boolean priseCompteLundi, boolean priseCompteMardi,
			boolean priseCompteMercredi, boolean priseCompteJeudi,
			boolean priseCompteVendredi, boolean priseCompteSamedi,
			boolean priseCompteDimanche) {

		if (d2.compareTo(d1) <= 0)
			return 0;

		// Tableau des jours a prendre en compte
		Boolean[] joursPrisEncompte = new Boolean[] { priseCompteDimanche,
				priseCompteLundi, priseCompteMardi, priseCompteMercredi,
				priseCompteJeudi, priseCompteVendredi, priseCompteSamedi };

		GregorianCalendar date1 = new GregorianCalendar();
		date1.setTime(d1);
		GregorianCalendar date2 = new GregorianCalendar();
		date2.setTime(d2);

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
				if (joursPrisEncompte[date1.get(GregorianCalendar.DAY_OF_WEEK) - 1])
					nbJour++;
			}

			date1.add(GregorianCalendar.DAY_OF_MONTH, 1);
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

	public static void main(String[] args) throws ParseException {
		//SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		//String dateBeginInString = "2015-12-08 18:56:25.620506";
		//String dateEndInString = "2015-12-08 18:59:35.164569";
		DateTools date = new DateTools();
		//date.lastConnexionDate("15");
		//date.reservationDateDelayBeforeTimeOut(new Date(), 300);
		//date.dateBeginDay("07:30");
		//date.dateEndDay("20:00");
		//Date dateBegin = formatter.parse(dateBeginInString);
		//Date dateEnd = formatter.parse(dateEndInString);
		//date.calculateDuration(dateBegin, dateEnd);
		//float rate = ((float)1500l*100/(float)225000l);
		
		//System.out.println("date is:" + new Date().getTime());
				
		//System.out.println("startdate is:" + new SimpleDateFormat("dd/MM/yyyy").format(new Date(1449784800166l)));
		
		System.out.println("stardate is:" + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date(0l)));
		System.out.println("enddate is:" + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date(1451602799000l)));
		
		
		Date valueBegin = date.getFirstDayOfMonth("2015-11-18T15:00:22.806Z", null);
		System.out.println("Begin month date is : " + valueBegin);
		Date valueEnd = date.getLastDayOfMonth("2015-11-28T15:00:22.806Z", null);
		System.out.println("End month date is : " + valueEnd);
		
		int nb = date.nbJoursOuvrableByMonth(valueBegin, valueEnd, true, true, true, true, true, true, false, false);
		System.out.println("NB : " + nb);
		
		//System.out.println("new startdate timestamp:" + (new SimpleDateFormat("dd/MM/yyyy").format(new Date(1450130402479l))));
		
//		Date now = new Date();
//		System.out.println("date is:" + now);
//		System.out.println("timestamp is:" + now.getTime());
//		Date formattedDate =date.beginOfDay(now);
//		System.out.println("formatted date is:" + formattedDate);
//		System.out.println("formatted timestamp is:" + formattedDate.getTime());
				
	}
	
}
