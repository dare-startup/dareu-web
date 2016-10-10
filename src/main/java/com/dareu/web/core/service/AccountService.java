/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.core.service;

import javax.ws.rs.core.Response;

import com.dareu.web.data.request.FriendshipRequest;
import com.dareu.web.data.request.FriendshipRequestResponse;
import com.dareu.web.data.request.SigninRequest;
import com.dareu.web.data.request.SignupRequest;
import com.dareu.web.exception.AuthenticationException;
import com.dareu.web.exception.EntityRegistrationException;
import com.dareu.web.exception.InternalApplicationException;
import com.dareu.web.exception.InvalidRequestException;
/**
 *
 * @author MACARENA
 */
public interface AccountService {
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
     * Find friends using a user authentication token
     * @param authorizationHeader
     * @return
     * @throws AuthenticationException
     * @throws InternalApplicationException
     */
    public Response findFriends(String authorizationHeader)throws AuthenticationException, InternalApplicationException;
    
    /**
     * Request a friendship with another user
     * @param request
     * @return
     * @throws InvalidRequestException
     * @throws InternalApplicationException
     */
    public Response requestFriendship(FriendshipRequest request)throws InvalidRequestException, InternalApplicationException;
    
    
    /**
     * Process a friendhip request response between two users
     * @param response
     * @return
     * @throws InvalidRequestException
     * @throws InternalApplicationException
     */
    public Response friendshipResponse(FriendshipRequestResponse response)throws InvalidRequestException, InternalApplicationException;
    
    
    /**
     * Update a FCM reg id from an existing user using the authentication token
     * @param regId
     * @param auth
     * @return
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws InternalApplicationException
     */
    public Response updateRegId(String regId, String auth)throws InvalidRequestException, InternalApplicationException;
}
