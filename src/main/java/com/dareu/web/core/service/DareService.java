package com.dareu.web.core.service;

import javax.ws.rs.core.Response;

import com.dareu.web.data.request.CreateDareRequest;
import com.dareu.web.exception.InternalApplicationException;

public interface DareService {
	public Response createNewDare(CreateDareRequest request)throws InternalApplicationException; 
}
