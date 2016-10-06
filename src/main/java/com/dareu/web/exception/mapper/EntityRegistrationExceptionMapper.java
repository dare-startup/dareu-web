package com.dareu.web.exception.mapper;

import java.util.Date;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.dareu.web.core.DareUtils;
import com.dareu.web.data.response.ApplicationErrorResponse;
import com.dareu.web.exception.DareuException;
import com.dareu.web.exception.EntityRegistrationException;

@Provider
public class EntityRegistrationExceptionMapper implements ExceptionMapper<EntityRegistrationException>{

	@Override
	public Response toResponse(EntityRegistrationException ex) {
		return Response
				.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity(new ApplicationErrorResponse(ex.getMessage(), 
						DareUtils.DATE_FORMAT.format(new Date()), ex.getErrorCode().getValue()))
				.type(MediaType.APPLICATION_JSON)
				.build(); 
	}

	

}
