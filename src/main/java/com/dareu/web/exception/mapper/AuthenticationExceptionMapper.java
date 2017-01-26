package com.dareu.web.exception.mapper;

import com.dareu.web.core.DareUtils;
import java.util.Date;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.dareu.web.dto.response.AuthenticationResponse;
import com.dareu.web.data.exception.AuthenticationException;

@Provider
public class AuthenticationExceptionMapper implements ExceptionMapper<AuthenticationException>{

	/**
	 * returns an authentication response object
	 */
	@Override
	public Response toResponse(AuthenticationException ex) {
		return Response.status(Response.Status.UNAUTHORIZED)
				.entity(new AuthenticationResponse(null, DareUtils.DATE_FORMAT.format(new Date()),ex.getMessage()))
				.build(); 
	}

}
