/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.core.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.dareu.web.core.DareUtils;
import com.dareu.web.core.security.SecurityRole;
import com.dareu.web.core.service.AccountService;
import com.dareu.web.core.service.FileService;
import com.dareu.web.core.service.FileService.FileType;
import com.dareu.web.data.entity.DareUser;
import com.dareu.web.data.entity.Friendship;
import com.dareu.web.data.repository.DareUserDareRepository;
import com.dareu.web.data.repository.DareUserRepository;
import com.dareu.web.data.repository.FriendshipRepository;
import com.dareu.web.data.request.FriendshipRequest;
import com.dareu.web.data.request.FriendshipRequestResponse;
import com.dareu.web.data.request.SigninRequest;
import com.dareu.web.data.request.SignupRequest;
import com.dareu.web.data.response.AuthenticationResponse;
import com.dareu.web.data.response.EntityRegistrationResponse;
import com.dareu.web.data.response.EntityRegistrationResponse.RegistrationType;
import com.dareu.web.data.response.FriendshipResponse;
import com.dareu.web.data.response.ResourceAvailableResponse;
import com.dareu.web.exception.AuthenticationException;
import com.dareu.web.exception.DataAccessException;
import com.dareu.web.exception.EntityRegistrationException;
import com.dareu.web.exception.InternalApplicationException;
import com.dareu.web.exception.InvalidRequestException;

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
    private FriendshipRepository friendshipRepository; 
    
    @Inject
    private DareUserDareRepository dareUserDareRepository;
    
    @Inject
    private FileService fileService; 
    
    @Inject
    private DareUtils utils; 
    
    @Inject
    private Logger log; 

    @Override
	public Response registerDareUser(SignupRequest request)
			throws EntityRegistrationException, InternalApplicationException {
		Response response = null; 
		
		//check entity 
		if(request == null)
			throw new EntityRegistrationException("No entity found on request");
		
		//validate email for duplicates 
		if(! dareUserRepository.isEmailAvailable(request.getEmail()))
			throw new EntityRegistrationException("An account with the email " + request.getEmail() + " already exists"); 
		
		//check username for duplicates
		if(! dareUserRepository.isNicknameAvailable(request.getUsername()))
			throw new EntityRegistrationException("An account with the username " + request.getUsername() + " already exists"); 
		
		
		//create an entity 
		DareUser user = new DareUser();
		
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setNickname(request.getUsername()); 
		user.setPassword(request.getPassword()); //TODO:must encrypt password here
		user.setUserSince(DareUtils.DATE_FORMAT.format(new Date()));
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
		
		List<FriendshipResponse> friends = null;
		//first get the user if exist
		try{
			final DareUser currentUser = dareUserRepository.findUserByToken(authorizationHeader);
			if(currentUser == null){
				throw new InternalApplicationException("User not found");
			}
			
			friends = friendshipRepository.findFriends(currentUser.getId(), Boolean.TRUE);
			
			//Aggregate the dare counts 
			for(FriendshipResponse fReq : friends){
				fReq.setDareCount(dareUserDareRepository.countDaresByChallenger(fReq.getId()));
			}
		}catch(Exception e){
			throw new InternalApplicationException(e.getMessage(), e);
		}
		return Response.ok(friends).build(); 
	}

	@Override
	public Response requestFriendship(FriendshipRequest request)
			throws InvalidRequestException, InternalApplicationException {
		//validate 
		if(request == null)
			throw new InvalidRequestException("Invalid request body"); 
		if(request.getRequestedUserId() == null || request.getRequestedUserId().isEmpty())
			throw new InvalidRequestException("Invalid requestedUserId field");
		if(request.getUserId() == null || request.getUserId().isEmpty())
			throw new InvalidRequestException("Invalid userId field");
		
		//create a friendship if not exists 
		Friendship friendship = new Friendship();
		friendship.setAccepted(false);
		friendship.setRequestDate(DareUtils.DATE_FORMAT.format(new Date()));
		
		//get requested user 
		DareUser requestedUser = null, user = null; 
		try{
			requestedUser = dareUserRepository.find(request.getRequestedUserId());
			user = dareUserRepository.find(request.getUserId()); 

			if(requestedUser != null && user != null){
				//set users 
				friendship.setRequestedUser(requestedUser);
				friendship.setUser(user);
				//try to persist
				String id = friendshipRepository.persist(friendship);
				
				//TODO send PUSH notification to both users 
				
				return Response
						.ok(new EntityRegistrationResponse("Friendship request sent to " + requestedUser.getName(), 
								RegistrationType.FRIENDSHIP_REQUEST, 
								DareUtils.DATE_FORMAT.format(new Date()), id))
						.build(); 
			}else
				//return bad response
				throw new InvalidRequestException("Users identificators are not valid, try again"); 
			
		}catch(DataAccessException ex){
			throw new InternalApplicationException("Error creating a friendship request: " + ex.getMessage()); 
		}
	}

	@Override
	public Response friendshipResponse(FriendshipRequestResponse response)
			throws InvalidRequestException, InternalApplicationException {
		if(response == null)
			throw new InvalidRequestException("Invalid friendship response body");
		
		if(response.getFriendshipid() == null || response.getFriendshipid().isEmpty())
			throw new InvalidRequestException("Invalid friendship id provided");
		
		//get the friendhip 
		Friendship f = null; 
		try{
			f = friendshipRepository.find(response.getFriendshipid()); 
			
			if(f == null)
				throw new InvalidRequestException("Friendship id not valid");
			
			f.setAccepted(response.isApproved());
			
			friendshipRepository.updateFriendhip(f.isAccepted(), f.getId());
			
			//TODO: send push notifications to both users here
			
			return Response
					.ok(new EntityRegistrationResponse("You are now friends with " + f.getRequestedUser().getName(), 
							RegistrationType.FRIENDSHIP_RESPONSE, 
							DareUtils.DATE_FORMAT.format(new Date()), 
							f.getId()))
					.build();
		}catch(DataAccessException ex){
			throw new InternalApplicationException("Could process friendhip: " + ex.getMessage()); 
		}
	}
	
	@Override
	public Response updateRegId(String regId, String auth)
			throws InvalidRequestException,
			InternalApplicationException {
		if(regId == null || regId.isEmpty())
			throw new InvalidRequestException(); 
		try{
			//try to update
			dareUserRepository.updateFcmRegId(regId, auth);
			
			//return response 
			return Response.ok()
					.build(); 
		}catch(DataAccessException ex){
			throw new InternalApplicationException("Could not update FCM: " + ex.getMessage()); 
		}
	}
}
