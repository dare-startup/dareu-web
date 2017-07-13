package com.dareu.web.exception.mapper;

import com.dareu.web.core.DareUtils;
import java.util.Date;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.dareu.web.core.service.DareuAssembler;
import com.dareu.web.core.service.MessagingService;
import com.dareu.web.dto.response.ApplicationErrorResponse;
import com.dareu.web.exception.application.ErrorCode;
import com.dareu.web.exception.application.InternalApplicationException;
import com.messaging.dto.email.EmailType;
import org.apache.log4j.Logger;

@Provider
public class InternalApplicationExceptionMapper implements ExceptionMapper<InternalApplicationException>{

	private final Logger log = Logger.getLogger(getClass());

	@Inject
	private MessagingService messagingService;

	@Inject
	private DareuAssembler assembler;

	@Override
	public Response toResponse(InternalApplicationException ex) {
		log.info(String.format("Exception thrown, message=\n{%s} ", ex.getMessage()));

		//send error message
		messagingService.sendEmailMessage(assembler.assembleErrorEmailRequest(ex), EmailType.ERROR);
		return Response
				.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity(new ApplicationErrorResponse("Something went wrong, try again", 
						DareUtils.DATE_FORMAT.format(
								new Date()), ErrorCode.INTERNAL_ERROR.getValue()))
				.build();
		
	}

}
