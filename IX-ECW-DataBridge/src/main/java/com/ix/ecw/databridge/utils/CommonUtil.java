package com.ix.ecw.databridge.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class CommonUtil.
 */

public class CommonUtil {
	private final static Logger logger = LoggerFactory.getLogger(CommonUtil.class);

	/**
	 * Gets the today date string.
	 *
	 * @return the today date string
	 */
	public static String getTodayDateString() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(Calendar.getInstance().getTime());
	}

	/**
	 * Convert date to string.
	 *
	 * @param date the date
	 * @return the string
	 */
	public static String convertDateToString(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(date);
	}
	
	/**
	 * Converts date To Fhir Format
	 * @param date
	 * @return
	 */
	public static String convertDateToFhirFormat(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'"); 
		String dateInStr = null;
		try {
			dateInStr = sdf.format(date);
		} 
		catch (Exception e) {
			logger.error("Exception in convertDateToFhirFormat of CommonUtil:: ", e);
		}
		return dateInStr;
	}
	
//	public static Date convertStringToDate(String dateInStr) {
//		SimpleDateFormat sdf = new SimpleDateFormat(""); 
//		Date date = null;
//		try {
//			if(StringUtils.isNotBlank(dateInStr)) {
//				date = sdf.parse(dateInStr);
//			}
//		} 
//		catch (Exception e) {
//			logger.error("Exception in convertStringToDate of CommonUtil:: ", e);
//		}
//		return date;
//	}
	private static final SimpleDateFormat[] FORMATS = {
	        new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'"), 
	        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
	    };
	
	public static Date convertStringToDate(String wtf) {
	    Date retval = null;
		try {
		    if (StringUtils.isNotBlank(wtf)) {
		        return null;
		    }
		    for (SimpleDateFormat sdf : FORMATS) {
		        try {
		        	logger.error(" ===================== SimpleDateFormat =============== "+sdf);
		            sdf.setLenient(false);
		            retval = sdf.parse(wtf);
		            break;
		        } catch (ParseException ex) {
		        	logger.error(" ===================== ParseException =============== "+sdf);
		            retval = null;
		            continue;
		        }
		    }
		} 
		catch (Exception e) {
			logger.error("Exception in convertStringToDate of CommonUtil:: ", e);
		}
    	logger.info(" ===================== retval =============== "+retval);

	    return retval;
	}
}
