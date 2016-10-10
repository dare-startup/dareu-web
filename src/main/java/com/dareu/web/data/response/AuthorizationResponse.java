package com.dareu.web.data.response;

import java.util.Date;

import com.dareu.web.core.DareUtils;

public class AuthorizationResponse {
	private String message;
	private String datetime; 

	public AuthorizationResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AuthorizationResponse(String message) {
		super();
		this.message = message;
		this.datetime = DareUtils.DATE_FORMAT.format(new Date()); 
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	} 
	
	
}
