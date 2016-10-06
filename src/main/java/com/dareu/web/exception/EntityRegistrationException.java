package com.dareu.web.exception;

public class EntityRegistrationException extends DareuException{

	public EntityRegistrationException() {
		super();
	}

	public EntityRegistrationException(String message, Throwable cause) {
		super(message, cause, ErrorCode.ENTITY_REGISTRATION_ERROR);
	}

	public EntityRegistrationException(String message) {
		super(message, ErrorCode.ENTITY_REGISTRATION_ERROR);
	}
	
}
