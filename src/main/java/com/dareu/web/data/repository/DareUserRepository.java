/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.data.repository;

import java.util.List;

import com.dareu.web.data.entity.DareUser;
import com.dareu.web.data.entity.Friendship;
import com.dareu.web.exception.AuthenticationException;
import com.dareu.web.exception.DataAccessException;

/**
 *
 * @author MACARENA
 */
public interface DareUserRepository extends BaseRepository<DareUser>{
	/**
	 * Check if a nickname is available 
	 * @param nickname
	 * @return
	 */
	public boolean isNicknameAvailable(String nickname);
	
	/**
	 * Update a security token 
	 * @param token
	 * @param userId
	 */
	public void updateSecurityToken(String token, String userId);
	
	/**
	 * Check if an email is available
	 * @param email
	 * @return
	 */
    public boolean isEmailAvailable(String email);
    
    /**
     * Register a new DareU user
     * @param register
     * @return
     * @throws DataAccessException
     */
    public String registerDareUser(DareUser register) throws DataAccessException;
    
    /**
     * Login using a facebook account
     * @param email
     * @param name
     * @return
     */
    public String loginFacebook(String email, String name);
    
    /**
     * Login using a nickname and a password
     * @param nickname
     * @param pass
     * @return
     * @throws AuthenticationException
     */
    public DareUser login(String nickname, String pass)throws AuthenticationException;

    /**
     * Find a list of friends in function of a user id
     * @param userId
     * @return
     * @throws DataAccessException
     */
    public List<DareUser> findFriends(String userId) throws DataAccessException;
    
}
