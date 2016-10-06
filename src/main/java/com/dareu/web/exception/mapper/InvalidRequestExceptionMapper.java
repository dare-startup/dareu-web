/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.exception.mapper;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.dareu.web.core.DareUtils;
import com.dareu.web.data.response.ApplicationErrorResponse;
import com.dareu.web.data.response.BadRequestResponse;
import com.dareu.web.exception.InvalidRequestException;

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

	/**
	 * 
	 */
	@Override
    public Response toResponse(InvalidRequestException ex) {
        //return response
        return Response
				.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity(new ApplicationErrorResponse(ex.getMessage(), 
						DareUtils.DATE_FORMAT.format(new Date()), ex.getErrorCode().getValue()))
				.type(MediaType.APPLICATION_JSON)
				.build(); 
    }
    
}
