package com.sw.protection.backend.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Declare formatters witch is need in business logic.
 * 
 * @author dinuka
 */
public class Formatters {

    private static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";

    /**
     * Format the given date
     * 
     * @param date
     *            - Date given by the runtime
     * @return - formatted date string.
     */
    public static String formatDate(Date date) {
	DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
	return dateFormat.format(date);
    }
}
