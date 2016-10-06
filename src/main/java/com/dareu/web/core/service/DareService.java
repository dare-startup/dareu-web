package com.dareu.web.core.service;

import javax.ws.rs.core.Response;

import com.dareu.web.data.request.CreateDareRequest;
import com.dareu.web.exception.InternalApplicationException;
import com.dareu.web.exception.InvalidRequestException;

public interface DareService {
	public Response createNewDare(CreateDareRequest request)throws InvalidRequestException ,InternalApplicationException; 
}
