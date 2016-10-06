package com.dareu.web.exception;

public class InternalApplicationException extends Exception{

	public InternalApplicationException() {
		super();
	}

	public InternalApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

	public InternalApplicationException(String message) {
		super(message);
	}

}
