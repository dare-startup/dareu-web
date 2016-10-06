/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.core.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import com.dareu.web.core.DareUtils;
import com.dareu.web.core.security.SecurityRole;
import com.dareu.web.core.service.AccountService;
import com.dareu.web.core.service.FileService;
import com.dareu.web.core.service.FileService.FileType;
import com.dareu.web.data.entity.DareUser;
import com.dareu.web.data.repository.DareUserRepository;
import com.dareu.web.data.request.SigninRequest;
import com.dareu.web.data.request.SignupRequest;
import com.dareu.web.data.response.AuthenticationResponse;
import com.dareu.web.data.response.ResourceAvailableResponse;
import com.dareu.web.exception.AuthenticationException;
import com.dareu.web.exception.DataAccessException;
import com.dareu.web.exception.EntityRegistrationException;
import com.dareu.web.exception.InternalApplicationException;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

/**
 *
 * @author MACARENA
 */
@Stateless
public class AccountServiceImpl implements AccountService{
    
    @Inject
    private DareUserRepository dareUserRepository;
    
    @Inject
    private FileService fileService; 
    
    @Inject
    private DareUtils utils; 
    
    @Inject
    private Logger log; 
    
    public AccountServiceImpl(){
        
    }

	@Override
	public Response registerDareUser(SignupRequest request)
			throws EntityRegistrationException, InternalApplicationException {
		Response response = null; 
		
		//check entity 
		if(request == null)
			throw new EntityRegistrationException("No entity found on request");
		//create an entity 
		DareUser user = new DareUser();
		
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setNickname(request.getUsername()); 
		user.setPassword(request.getPassword()); //TODO:must encrypt password here
		user.setUserSince(DareUtils.DATE_FORMAT.format(new Date()));
		user.setGCM(request.getRegId());
		user.setCoins(0);
		user.setRole(SecurityRole.USER);
		user.setuScore(0);
		user.setBirthday(request.getBirthday());
		//save image 
		String path = ""; 
		try{
			fileService.saveFile(request.getImage(), FileType.PROFILE_IMAGE, user.getId() + ".jpg");
			user.setImagePath(user.getId() + ".jpg");
		}catch(IOException ex){
			log.severe("Could not save profile image file: " + ex.getMessage());
			throw new InternalApplicationException("Error writing image file, try again", ex); 
		}
		//save the entity 
		try{
			dareUserRepository.registerDareUser(user); 
		}catch(DataAccessException ex){
			log.severe("Could not register new dare user: " + ex.getMessage());
			throw new EntityRegistrationException("Could not register dare user, try again");
		}
		
		//generate a new token for this user 
		String token = utils.getNextSessionToken();
		
		//save the token here
		user.setSecurityToken(token);
		
		//update token
		dareUserRepository.updateSecurityToken(token, user.getId());
		
		//create response 
		response = Response.ok(new AuthenticationResponse(token, DareUtils.DATE_FORMAT.format(new Date()), "Welcome"))
				.build(); 
		return response;
	}

	@Override
	public Response authenticate(SigninRequest request)
			throws AuthenticationException {
		if(request == null)
			throw new AuthenticationException("No signin body provided");
		//try to authenticate
		DareUser user = dareUserRepository.login(request.getUser(), request.getPassword()); 
		if(user == null)
			throw new AuthenticationException("Username and/or password are incorrect");
		else{
			//generate a new token 
			String token = utils.getNextSessionToken(); 
			
			//update token 
			dareUserRepository.updateSecurityToken(token, user.getId());
			return Response.ok(new AuthenticationResponse(token, DareUtils.DATE_FORMAT.format(new Date()), "Welcome"))
					.build();
		}
	}

	@Override
	public Response isEmailAvailable(String email)
			throws InternalApplicationException {
		
		if(dareUserRepository.isEmailAvailable(email))
			return Response.ok(new ResourceAvailableResponse(true, "The email " + email + " is available", DareUtils.DATE_FORMAT.format(new Date())))
					.build(); 
		else return Response.ok(new ResourceAvailableResponse(false, "The email " + email + " is not available", DareUtils.DATE_FORMAT.format(new Date())))
				.build(); 
	}

	@Override
	public Response isNicknameAvailable(String nickname)
			throws InternalApplicationException {
		if(dareUserRepository.isNicknameAvailable(nickname))
			return Response.ok(new ResourceAvailableResponse(true, "The nickname " + nickname + " is available", DareUtils.DATE_FORMAT.format(new Date())))
					.build(); 
		else return Response.ok(new ResourceAvailableResponse(false, "The nickname " + nickname + " is not available", DareUtils.DATE_FORMAT.format(new Date())))
				.build();
	}

	@Override
	public Response findFriends(String authorizationHeader)
			throws AuthenticationException, InternalApplicationException {
		//validate header 
		return null; 
	}
}
