package com.dareu.web.core.service;

import javax.ws.rs.core.Response;

import com.dareu.web.dto.request.CreateCategoryRequest;
import com.dareu.web.dto.request.CreateDareRequest;
import com.dareu.web.dto.request.DareConfirmationRequest;
import com.dareu.web.exception.InternalApplicationException;
import com.dareu.web.exception.InvalidRequestException;

public interface DareService {

    /**
     * Creates a new dare
     *
     * @param request
     * @param authenticationToken
     * @return
     * @throws InvalidRequestException
     * @throws InternalApplicationException
     */
    public Response createNewDare(CreateDareRequest request, String authenticationToken) throws InvalidRequestException, InternalApplicationException;

    /**
     * Creates a new dare category TODO: Should be authenticated...
     *
     * @param request
     * @return
     * @throws InvalidRequestException
     * @throws InternalApplicationException
     */
    public Response createNewCategory(CreateCategoryRequest request) throws InvalidRequestException, InternalApplicationException;

    /**
     * Returns a list with available categories
     *
     * @param pageNumber
     * @return
     * @throws InternalApplicationException
     */
    public Response getCategories(int pageNumber) throws InternalApplicationException;


    /**
     * find all dares using a page number
     * @param pageNumber
     * @return
     * @throws InternalApplicationException 
     */
    public Response findUnapprovedDares(int pageNumber) throws InternalApplicationException;
    
    /**
     * Finds a user unaccepted dare
     * @param auth
     * @return
     * @throws InternalApplicationException 
     */
    public Response findUnacceptedDare(String auth)throws InternalApplicationException;
    
    /**
     * confirms if a dare is accepted or declined
     * @param request
     * @return
     * @throws InternalApplicationException
     * @throws InvalidRequestException 
     */
    public Response confirmDareRequest(DareConfirmationRequest request)throws InternalApplicationException, InvalidRequestException;
    
    /**
     * Returns a list of dare descriptions using a page number
     * @param pageNumber
     * @param authToken
     * @return
     * @throws InternalApplicationException 
     */
    public Response discoverDares(int pageNumber, String authToken)throws InternalApplicationException;
    
    /**
     * 
     * @param dareId
     * @return
     * @throws InternalApplicationException
     * @throws InvalidRequestException 
     */
    public Response findDareDescription(String dareId)throws InternalApplicationException, InvalidRequestException; 
}
