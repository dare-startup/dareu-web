package com.dareu.web.core;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.ejb.Stateless;


@Stateless
public class DareUtils {
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/YY");
        public static final SimpleDateFormat DETAILS_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        
        static{
            DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("PST"));
            DETAILS_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("PST"));
        }
	private final SecureRandom random = new SecureRandom();
	
	public DareUtils(){
		
	}
	
	/**
	 * generates a token for a new user session
	 * @return
	 */
	public String getNextSessionToken(){
		return new BigInteger(130, random).toString(); 
	}
}
