package com.dareu.web.exception;

public class InternalApplicationException extends DareuException{

	public InternalApplicationException() {
		super();
	}

	public InternalApplicationException(String message, Throwable cause) {
		super(message, cause, ErrorCode.INTERNAL_ERROR);
	}

	public InternalApplicationException(String message) {
		super(message, ErrorCode.INTERNAL_ERROR);
	}

}
