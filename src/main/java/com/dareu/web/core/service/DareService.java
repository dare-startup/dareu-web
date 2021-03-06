package com.dareu.web.core.service;

import com.dareu.web.dto.request.*;

import javax.ws.rs.core.Response;

import com.dareu.web.exception.application.InternalApplicationException;
import com.dareu.web.exception.application.InvalidRequestException;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

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
     * Creates a new dare category
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

    /**
     * Returns a page of dares created by the user corresponding to the authentication header
     * @param auth
     * @param pageNumber
     * @return 
     * @throws com.dareu.web.exception.application.InternalApplicationException 
     * @throws com.dareu.web.exception.application.InvalidRequestException 
     */
    public Response findCreatedDares(String auth, int pageNumber)throws InternalApplicationException, InvalidRequestException;
    
    /**
     * Return a DareDescription currently active
     * @param auth
     * @return
     * @throws InternalApplicationException 
     */
    public Response getCurrentActiveDare(String auth)throws InternalApplicationException;
    
    /**
     * Creates a new dare response and saves a new dare video 
     * @param input
     * @param auth
     * @return
     * @throws InternalApplicationException 
     */
    public Response uploadDareResponse(MultipartFormDataInput input, String auth)throws InternalApplicationException; 

    /**
     * Flag a dare using a comment
     * @param request
     * @param auth
     * @return
     * @throws InternalApplicationException
     * @throws InvalidRequestException 
     */
    public Response flagDare(FlagDareRequest request, String auth)throws InternalApplicationException, InvalidRequestException;

    /**
     * Find a page of dare responses 
     * @param pageNumber
     * @param auth
     * @return
     * @throws InternalApplicationException
     * @throws InvalidRequestException 
     */
    public Response findResponses(int pageNumber, String auth)throws InternalApplicationException, InvalidRequestException;

    /**
     * 
     * @param dareId
     * @param auth
     * @return
     * @throws InternalApplicationException
     * @throws InvalidRequestException 
     */
    public Response setDareExpired(String dareId, String auth)throws InternalApplicationException, InvalidRequestException;

    /**
     * Get hottest responses
     * @param pageNumber
     * @return
     * @throws InternalApplicationException 
     */
    public Response hotResponses(int pageNumber)throws InternalApplicationException;

    /**
     * Returns a page of channel responses
     * @param pageNumber
     * @return
     * @throws InternalApplicationException 
     */
    public Response channelResponses(int pageNumber, String token) throws InternalApplicationException;

    /**
     * 
     * @param responseId
     * @return
     * @throws InternalApplicationException
     * @throws InvalidRequestException 
     */
    public Response findResponseDescription(String responseId, String token)throws InternalApplicationException, InvalidRequestException;

    /**
     * 
     * @param request
     * @param token
     * @return
     * @throws InternalApplicationException
     * @throws InvalidRequestException 
     */
    public Response createResponseComment(NewCommentRequest request, String token) throws InternalApplicationException, InvalidRequestException;

    /**
     * Find a response comments page
     * @param pageNumber
     * @param responseId
     * @return
     * @throws InternalApplicationException 
     * @throws com.dareu.web.exception.application.InvalidRequestException 
     */
    public Response findResponseComments(int pageNumber, String responseId, String token) throws InternalApplicationException, InvalidRequestException;

    /**
     * Increment the number of views by 1
     * @param responseId
     * @return 
     * @throws com.dareu.web.exception.application.InternalApplicationException 
     * @throws com.dareu.web.exception.application.InvalidRequestException 
     */
    public Response viewedResponse(String responseId)throws InternalApplicationException, InvalidRequestException;
    

    /**
     * Clap or un-clap a response
     * @param request
     * @param token
     * @return
     * @throws InternalApplicationException
     * @throws InvalidRequestException 
     */
    public Response clapResponse(ClapRequest request, String token) throws InternalApplicationException, InvalidRequestException;

    /**
     * Find a comment description 
     * @param commentId
     * @return
     * @throws InternalApplicationException
     * @throws InvalidRequestException 
     */
    public Response findResponseComment(String commentId, String token) throws InternalApplicationException, InvalidRequestException;

    /**
     * Anchor a dare response to a user
     * @param token
     * @return 
     * @throws com.dareu.web.exception.application.InternalApplicationException 
     * @throws com.dareu.web.exception.application.InvalidRequestException 
     */
    public Response anchorContent(String responseId, String token) throws InternalApplicationException, InvalidRequestException;

    /**
     * Unpin an anchored response
     * @param anchoredContentId
     * @param token
     * @return
     * @throws InternalApplicationException
     * @throws InvalidRequestException 
     */
    public Response unpinAnchoredContent(String anchoredContentId, String token)throws InternalApplicationException, InvalidRequestException;
    
    /**
     * Get a user anchored content
     * @param pageNumber
     * @param token
     * @return
     * @throws InternalApplicationException
     * @throws InvalidRequestException 
     */
    public Response getAnchoredContent(int pageNumber, String token)throws InternalApplicationException, 
                                                            InvalidRequestException;

    /**
     * Clap a response comment
     * @param commentId
     * @param token
     * @return
     * @throws InternalApplicationException
     * @throws InvalidRequestException
     */
    public Response clapResponseComment(String commentId, String token) throws InternalApplicationException, InvalidRequestException;

    /**
     * Edit a category
     * @param request
     * @return
     * @throws InvalidRequestException
     * @throws InternalApplicationException
     */
    public Response editCategory(EditCategoryRequest request)throws InvalidRequestException, InternalApplicationException;

    /**
     * Get a page of flagged dares
     * @param pageNumber
     * @return
     * @throws InternalApplicationException
     */
    //public Response findFlaggedDares(int pageNumber)throws InternalApplicationException;
}
