package com.dareu.web.data.exception;

public class AuthenticationException extends Exception{

	public AuthenticationException() {
		super();
	}

	public AuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthenticationException(String message) {
		super(message);
	}
	
}
