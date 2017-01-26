package com.dareu.web.core;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;

import javax.ejb.Stateless;


@Stateless
public class DareUtils {
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/YY");
        public static final SimpleDateFormat DETAILS_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        
	private SecureRandom random = new SecureRandom();
	
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
