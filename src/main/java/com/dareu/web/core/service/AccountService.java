/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.core.service;

import javax.ws.rs.core.Response;

import com.dareu.web.dto.request.SigninRequest;
import com.dareu.web.dto.request.SignupRequest;
import com.dareu.web.data.exception.AuthenticationException;
import com.dareu.web.exception.EntityRegistrationException;
import com.dareu.web.exception.InternalApplicationException;
import com.dareu.web.exception.InvalidRequestException;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
/**
 *
 * @author MACARENA
 */
public interface AccountService {
    
    /**
     * 
     * @param request
     * @return
     * @throws EntityRegistrationException
     * @throws InternalApplicationException 
     */
    public Response registerDareUser(SignupRequest request)throws EntityRegistrationException, 
    															InternalApplicationException;
    
    /**
     * Authenticates a user 
     * @param request
     * @return
     * @throws AuthenticationException
     */
    public Response authenticate(SigninRequest request)throws AuthenticationException;
    
    /**
     * Check if an email is available for registration
     * @param email
     * @return
     * @throws InternalApplicationException
     */
    public Response isEmailAvailable(String email)throws InternalApplicationException;
    
    /**
     * Check if a nickname is available for registration
     * @param nickname
     * @return
     * @throws InternalApplicationException
     */
    public Response isNicknameAvailable(String nickname)throws InternalApplicationException; 
    
    
    /**
     * Request a friendship with another user
     * @param requestedUserId
     * @return
     * @throws InvalidRequestException
     * @throws InternalApplicationException
     */
    public Response requestFriendship(String requestedUserId)throws InvalidRequestException, InternalApplicationException;
    
    
    /**
     * Process a friendhip request response between two users
     * @param userId
     * @param accepted
     * @return
     * @throws InvalidRequestException
     * @throws InternalApplicationException
     */
    public Response friendshipResponse(String userId, Boolean accepted)throws InvalidRequestException, InternalApplicationException;
    
    
    /**
     * Update a FCM reg id from an existing user using the authentication token
     * @param regId
     * @param auth
     * @return
     * @throws InvalidRequestException
     * @throws InternalApplicationException
     */
    public Response updateRegId(String regId, String auth)throws InvalidRequestException, InternalApplicationException;

    /**
     * 
     * @param auth
     * @return
     * @throws InvalidRequestException
     * @throws InternalApplicationException 
     */
    public Response getAccountImage(String auth) throws InvalidRequestException, InternalApplicationException;
    
    /**
     * return a user using an email
     * @param email
     * @return
     * @throws InvalidRequestException
     * @throws InternalApplicationException 
     */
    public Response findUserById(String email)throws InvalidRequestException, InternalApplicationException; 

    /**
     * Returns a list of users per page 
     * @param pageNumber
     * @return
     * @throws InternalApplicationException 
     */
    public Response findUsersByPage(int pageNumber) throws InternalApplicationException;

    /**
     * gets a page of user that are not friends with user principal
     * @param pageNumber
     * @return 
     * @throws com.dareu.web.exception.InternalApplicationException 
     */
    public Response discoverUsers(int pageNumber)throws InternalApplicationException;

    /**
     * Returns a page of friends related to the principal authentication
     * @param pageNumber
     * @param query
     * @return
     * @throws InternalApplicationException 
     */
    public Response findFriends(int pageNumber, String query) throws InternalApplicationException;

    /**
     * Returns details about a registered connection
     * @param friendshipId
     * @param auth
     * @return 
     * @throws com.dareu.web.exception.InternalApplicationException 
     * @throws com.dareu.web.exception.InvalidRequestException 
     */
    public Response findFriendshipDetails(String friendshipId, String auth) throws InternalApplicationException, InvalidRequestException;

    /**
     * Updates a profile image 
     * @param input
     * @param auth
     * @return 
     */
    public Response updateProfileImage(MultipartFormDataInput input, String auth)throws InternalApplicationException;
    
}
