/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.exception.mapper;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.dareu.web.data.response.BadRequestResponse;
import com.dareu.web.exception.InvalidRequestException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author MACARENA
 */
@Provider
public class InvalidRequestExceptionMapper implements ExceptionMapper<InvalidRequestException>{

    public Response toResponse(InvalidRequestException exception) {
        //return response
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        		.entity(new BadRequestResponse(exception.getMessage(), 
        				new SimpleDateFormat("").format(new Date()), 500))
        		.build(); 
    }
    
}
