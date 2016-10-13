package com.dareu.web.core.service;

import javax.ws.rs.core.Response;

import com.dareu.web.data.request.CreateCategoryRequest;
import com.dareu.web.data.request.CreateDareRequest;
import com.dareu.web.exception.InternalApplicationException;
import com.dareu.web.exception.InvalidRequestException;

public interface DareService {
	
	/**
	 * Creates a new dare
	 * @param request
	 * @return
	 * @throws InvalidRequestException
	 * @throws InternalApplicationException
	 */
	public Response createNewDare(CreateDareRequest request)throws InvalidRequestException ,InternalApplicationException;

	/**
	 * Creates a new dare category 
	 * TODO: Should be authenticated...
	 * @param request
	 * @return
	 * @throws InvalidRequestException
	 * @throws InternalApplicationException
	 */
	public Response createNewCategory(CreateCategoryRequest request)throws InvalidRequestException, InternalApplicationException;
	
	
	/**
	 * Returns a list with available categories
	 * @return
	 * @throws InternalApplicationException
	 */
	public Response getCategories()throws InternalApplicationException; 
}
