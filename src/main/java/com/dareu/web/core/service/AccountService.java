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
import com.dareu.web.dto.request.ChangeEmailAddressRequest;
import com.dareu.web.dto.request.ContactRequest;
import com.dareu.web.exception.EntityRegistrationException;
import com.dareu.web.exception.application.InternalApplicationException;
import com.dareu.web.exception.application.InvalidRequestException;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
/**
 *
 * @author MACARENA
 */
public interface AccountService {
    
    /**
     * Returns a profile object
     * @return
     * @throws InternalApplicationException 
     */
    public Response me() throws InternalApplicationException;
    
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
     * @throws com.dareu.web.exception.application.InternalApplicationException 
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
     * @throws com.dareu.web.exception.application.InternalApplicationException 
     * @throws com.dareu.web.exception.application.InvalidRequestException 
     */
    public Response findFriendshipDetails(String friendshipId, String auth) throws InternalApplicationException, InvalidRequestException;

    /**
     * Updates a profile image 
     * @param input
     * @param auth
     * @return 
     * @throws com.dareu.web.exception.application.InternalApplicationException 
     */
    public Response updateProfileImage(MultipartFormDataInput input, String auth)throws InternalApplicationException;

    /**
     * Change an email address from a specified user
     * @param request
     * @param token
     * @return
     * @throws InvalidRequestException
     * @throws InternalApplicationException 
     */
    public Response changeEmailAddress(ChangeEmailAddressRequest request, String token)throws InvalidRequestException, InternalApplicationException;

    /**
     * Creates a new contact message
     * @param message
     * @return
     * @throws InternalApplicationException 
     * @throws com.dareu.web.exception.application.InvalidRequestException 
     */
    public Response contactMessage(ContactRequest message)throws InternalApplicationException, InvalidRequestException;

    /**
     * Get pending sent requests
     * @param pageNumber
     * @param token
     * @return
     * @throws InternalApplicationException
     * @throws InvalidRequestException 
     */
    public Response getPendingSentRequests(int pageNumber, String token)throws InternalApplicationException, InvalidRequestException;
    
    /**
     * Get pending received requests
     * @param pageNumber
     * @param token
     * @return
     * @throws InternalApplicationException
     * @throws InvalidRequestException 
     */
    public Response getPendingReceivedRequests(int pageNumber, String token)throws InternalApplicationException, InvalidRequestException;

    /**
     * Cancels a friendship requests
     * @param connId
     * @param token
     * @return
     * @throws InternalApplicationException
     * @throws InvalidRequestException 
     */
    public Response cancelFriendshipRequest(String connId, String token)throws InternalApplicationException, InvalidRequestException;
}
