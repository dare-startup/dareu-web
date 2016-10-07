package com.dareu.web.exception.mapper;

import java.util.Date;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.dareu.web.core.DareUtils;
import com.dareu.web.data.response.ApplicationErrorResponse;
import com.dareu.web.exception.ErrorCode;
import com.dareu.web.exception.InternalApplicationException;

@Provider
public class InternalApplicationExceptionMapper implements ExceptionMapper<InternalApplicationException>{

	@Inject
	private Logger log; 
	
	@Override
	public Response toResponse(InternalApplicationException ex) {
		log.info(String.format("Exception thrown, message=\n{%s} ", ex.getMessage()));
		return Response
				.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity(new ApplicationErrorResponse("Something went wrong, try again", 
						DareUtils.DATE_FORMAT.format(
								new Date()), ErrorCode.INTERNAL_ERROR.getValue()))
				.build();
		
	}

}
