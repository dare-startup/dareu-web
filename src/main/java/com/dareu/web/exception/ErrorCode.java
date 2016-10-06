package com.dareu.web.exception;

public enum ErrorCode {
	INTERNAL_ERROR(123), 
	ENTITY_REGISTRATION_ERROR(132), 
	INVALID_REQUEST_ERROR(987);
	
	private int value;
	
	ErrorCode(int value){
		this.value = value; 
	}

	public int getValue(){
		return this.value; 
	}
}
