/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.core.service;

import javax.ws.rs.core.Response;

import com.dareu.web.data.request.SigninRequest;
import com.dareu.web.data.request.SignupRequest;
import com.dareu.web.exception.AuthenticationException;
import com.dareu.web.exception.EntityRegistrationException;
import com.dareu.web.exception.InternalApplicationException;
/**
 *
 * @author MACARENA
 */
public interface AccountService {
    public Response registerDareUser(SignupRequest request)throws EntityRegistrationException, 
    															InternalApplicationException;
    public Response authenticate(SigninRequest request)throws AuthenticationException;
    public Response isEmailAvailable(String email)throws InternalApplicationException; 
    public Response isNicknameAvailable(String nickname)throws InternalApplicationException; 
    public Response findFriends(String authorizationHeader)throws AuthenticationException, InternalApplicationException; 
}
