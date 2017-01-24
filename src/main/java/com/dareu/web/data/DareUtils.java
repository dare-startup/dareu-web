package com.dareu.web.data;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;

public class DareUtils {
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/YY");
	
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
