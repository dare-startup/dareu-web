/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.exception.mapper;

import com.dareu.web.core.DareUtils;
import java.util.Date;

import com.dareu.web.dto.response.ApplicationErrorResponse;
import com.dareu.web.exception.application.InvalidRequestException;
import org.apache.log4j.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author MACARENA
 */
@Provider
public class InvalidRequestExceptionMapper implements ExceptionMapper<InvalidRequestException>{

	private final Logger log = Logger.getLogger(getClass());

	/**
	 * 
	 */
	@Override
    public Response toResponse(InvalidRequestException ex) {
		log.info("Invalid request exception: " + ex.getMessage());
        //return response
        return Response
				.status(Response.Status.BAD_REQUEST)
				.entity(new ApplicationErrorResponse(ex.getMessage(), 
						DareUtils.DATE_FORMAT.format(new Date()), ex.getErrorCode().getValue()))
				.type(MediaType.APPLICATION_JSON)
				.build(); 
    }
    
}
