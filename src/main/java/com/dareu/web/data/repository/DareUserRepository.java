/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.data.repository;

import java.util.List;

import com.dareu.web.data.entity.DareUser;
import com.dareu.web.data.entity.FriendshipRequest;
import com.dareu.web.data.exception.AuthenticationException;
import com.dareu.web.data.exception.DataAccessException;
import com.dareu.web.dto.response.entity.AccountProfile;
import com.dareu.web.dto.response.entity.Page;

/**
 *
 * @author MACARENA
 */
public interface DareUserRepository extends BaseRepository<DareUser> {

    /**
     * Update a security token
     *
     * @param token
     * @param userId
     */
    public void updateSecurityToken(String token, String userId);

    /**
     * Check if an email is available
     *
     * @param email
     * @return
     */
    public boolean isEmailAvailable(String email);

    /**
     * Register a new DareU user
     *
     * @param register
     * @return
     * @throws DataAccessException
     */
    public String registerDareUser(DareUser register) throws DataAccessException;

    /**
     * Login using a nickname and a password
     *
     * @param nickname
     * @param pass
     * @return
     * @throws AuthenticationException
     */
    public DareUser login(String nickname, String pass) throws AuthenticationException;


    /**
     * Find a user using an authentication token
     *
     * @param token
     * @return
     * @throws DataAccessException
     */
    public DareUser findUserByToken(String token) throws DataAccessException;

    /**
     * Updated a Firebase Cloud Messaging registration id using a security token
     *
     * @param regid
     * @param token
     * @throws DataAccessException
     */
    public void updateFcmRegId(String regid, String token) throws DataAccessException;
    
    
    /**
     * Checks if a user has a friendship relationship with another user
     * @param userId
     * @param anotherUserId
     * @return
     * @throws DataAccessException 
     */
    //public boolean isUserFriend(String userId, String anotherUserId)throws DataAccessException;
    
    /**
     * 
     * @param email
     * @return
     * @throws DataAccessException 
     */
    public DareUser findUserByEmail(String email)throws DataAccessException;

    /**
     * find a list of users by page
     * 
     * @param pageNumber
     * @param excludePrincipal specifies if the query contains the current user or not
     * @param userId
     * @return
     * @throws DataAccessException 
     */
    public List<DareUser> findUsersByPage(int pageNumber, boolean excludePrincipal, String userId) throws DataAccessException;  
    
    
    /**
     * returns a list of users that are not friend of the received id
     * @param pageNumber
     * @param userId
     * @return
     * @throws DataAccessException 
     */
    public Page<DareUser> discoverUsers(int pageNumber, String userId) throws DataAccessException;
    
    /**
     * Finds a FCM token using a user id 
     * @param userId
     * @return
     * @throws DataAccessException 
     */
    public String getUserFcmToken(String userId)throws DataAccessException; 

    /**
     * Returns an account profile from a user id
     * @param id
     * @return
     * @throws DataAccessException 
     */
    public AccountProfile getAccountProfile(String id) throws DataAccessException;

    /**
     * 
     * @param newEmail
     * @param token
     * @throws DataAccessException 
     */
    public void changeEmailAddress(String newEmail, String token) throws DataAccessException;

    /**
     *
     * @param googleId
     * @param user
     * @return
     */
    public DareUser loginGoogle(String googleId, String user)throws DataAccessException;
}
