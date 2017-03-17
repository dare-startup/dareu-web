package com.dareu.web.exception.mapper;

import com.dareu.web.core.DareUtils;
import java.util.Date;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.dareu.web.dto.response.ApplicationErrorResponse;
import com.dareu.web.exception.application.DareuException;
import com.dareu.web.exception.EntityRegistrationException;
import org.apache.log4j.Logger;

@Provider
public class EntityRegistrationExceptionMapper implements ExceptionMapper<EntityRegistrationException>{

	@Inject
	private Logger log;

	@Override
	public Response toResponse(EntityRegistrationException ex) {
		log.info("Entity registration exception: " + ex.getMessage());
		return Response
				.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity(new ApplicationErrorResponse(ex.getMessage(), 
						DareUtils.DATE_FORMAT.format(new Date()), ex.getErrorCode().getValue()))
				.type(MediaType.APPLICATION_JSON)
				.build(); 
	}

	

}
