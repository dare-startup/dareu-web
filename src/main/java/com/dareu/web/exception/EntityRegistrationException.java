package com.dareu.web.exception;

import com.dareu.web.exception.application.ErrorCode;
import com.dareu.web.exception.application.DareuException;

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
