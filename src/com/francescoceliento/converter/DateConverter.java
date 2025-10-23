package com.francescoceliento.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Manages date conversion.
 * @author @francescoceliento@github.com
 *
 */
public class DateConverter {
	
	/**
	 * Function to convert java.util.Date to String.
	 * @author @francescoceliento@github.com
	 *
	 * @param date
	 * @param pattern
	 * @return
	 */
    public static String dateToString(Date date, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

    /**
     * Function to convert String to java.util.Date 
     * @author @francescoceliento@github.com
     *
     * @param dateString
     * @param pattern
     * @return Date
     * @throws ParseException
     */   
    public static Date stringToDateWithThrow(String dateString, String pattern) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.parse(dateString);
    }
    
    /**
     * Function to convert String to java.util.Date with internal exception handling
     * @author @francescoceliento@github.com
     *
     * @param dateString
     * @param pattern
     * @return Date
     */
    public static Date stringToDate(String dateString, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            System.err.println("Errore nella conversione della data: " + e.getMessage());
            return null; // Restituisce null in caso di errore
        }
    }

    

}
