package com.dareu.web.core.service.impl;

import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import com.dareu.web.core.DareUtils;
import com.dareu.web.core.service.DareService;
import com.dareu.web.data.entity.Dare;
import com.dareu.web.data.entity.DareUser;
import com.dareu.web.data.repository.DareRepository;
import com.dareu.web.data.request.CreateDareRequest;
import com.dareu.web.exception.InternalApplicationException;
import com.dareu.web.exception.InvalidRequestException;

@Stateless
public class DareServiceImpl implements DareService{

	@Inject
	private DareRepository dareRepository; 
	
	public DareServiceImpl(){
		
	}
	
	@Override
	public Response createNewDare(CreateDareRequest request)
			throws InvalidRequestException, InternalApplicationException {
		if(request == null)
			throw new InvalidRequestException("Invalid dare request");
		
		//validate 
		if(request.getCategoryId() == null)
			throw new InvalidRequestException("Category field not provided");
		if(request.getDescription() == null)
			throw new InvalidRequestException("Description field not provided");
		if(request.getFriendsIds() == null)
			throw new InvalidRequestException("Friends ID's field not provided");
		if(request.getName() == null)
			throw new InvalidRequestException("Name field not provided");
		if(request.getTimer() <= 0)
			throw new InvalidRequestException("Time field is not valid");
		
		//create a new dare 
		Dare dare = new Dare(); 
		dare.setAccepted(false);
		dare.setApproved(false);
		dare.setCreationDate(new Date());
		dare.setDescription(request.getDescription());
		dare.setEstimatedDareTime(request.getTimer()); //time is in hours
		dare.setName(request.getName());
		
		DareUser user = null; 
		//create relationships
		for(String friendId : request.getFriendsIds()){
			//create a new user 
			user = new DareUser(); 
			user.setId(friendId);
			dare.getUsers().add(user); 
		}
		
		//dareRepository.
		//TODO: fetch category here
		return null;
	}

}
