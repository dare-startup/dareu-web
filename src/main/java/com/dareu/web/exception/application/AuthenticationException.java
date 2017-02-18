package com.dareu.web.exception.application;

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
