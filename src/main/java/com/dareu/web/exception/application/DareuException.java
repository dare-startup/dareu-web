package com.dareu.web.exception.application;

public class DareuException extends Exception{
	
	private ErrorCode errorCode;
	
	public DareuException(){
		super(); 
	}
	
	
	
	public DareuException(String message, Throwable cause, ErrorCode errorCode) {
		super(message, cause);
		this.errorCode = errorCode; 
	}



	public DareuException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode; 
	}
	
	public ErrorCode getErrorCode(){
		return this.errorCode; 
	}
}
