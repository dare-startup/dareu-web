package com.dareu.web.core.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import com.dareu.web.core.DareUtils;
import com.dareu.web.core.service.DareService;
import com.dareu.web.data.entity.Category;
import com.dareu.web.data.entity.Dare;
import com.dareu.web.data.entity.DareUser;
import com.dareu.web.data.entity.Friendship;
import com.dareu.web.data.repository.CategoryRepository;
import com.dareu.web.data.repository.DareRepository;
import com.dareu.web.data.request.CreateCategoryRequest;
import com.dareu.web.data.request.CreateDareRequest;
import com.dareu.web.data.response.EntityRegistrationResponse;
import com.dareu.web.data.response.EntityRegistrationResponse.RegistrationType;
import com.dareu.web.exception.DataAccessException;
import com.dareu.web.exception.InternalApplicationException;
import com.dareu.web.exception.InvalidRequestException;

@Stateless
public class DareServiceImpl implements DareService{

	@Inject
	private DareRepository dareRepository; 
	
	@Inject
	private CategoryRepository categoryRepository; 
	
	@Inject
	private Logger log; 
	
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
		List<Friendship> friendships = new ArrayList<Friendship>();
		//validate friendships
		//TODO: 
		
		try {
			//get category
			Category category = categoryRepository.find(request.getCategoryId()); 
			if(category == null)
				throw new InvalidRequestException("Category not valid, try again"); 
			
			dare.setCategory(category);
			String id = dareRepository.createDare(dare);
			log.info("Successfully created new dare with id: " + id); 
			
			return Response
					.ok(new EntityRegistrationResponse("Successfully created new dare", 
							RegistrationType.DARE, 
							DareUtils.DATE_FORMAT.format(new Date()), id))
					.build(); 
		} catch (DataAccessException e) {
			throw new InternalApplicationException("Error creating new dare: " + e.getMessage());
		}
	}

	@Override
	public Response createNewCategory(CreateCategoryRequest request)
			throws InvalidRequestException, InternalApplicationException {
		
		//validate request
		if(request == null)
			throw new InvalidRequestException("Invalid Category request"); 
		
		if(request.getName() == null || request.getName().isEmpty())
			throw new InvalidRequestException("Name field not provided");
		if(request.getDescription() == null || request.getDescription().isEmpty())
			throw new InvalidRequestException("Description field not provided");
		
		//create a category
		Category category = new Category(); 
		category.setName(request.getName());
		category.setDescription(request.getDescription());
		
		try{
			String id = categoryRepository.persist(category);
			return Response
					.ok(new EntityRegistrationResponse("Successfully created category",
							RegistrationType.CATEGORY, 
							DareUtils.DATE_FORMAT.format(new Date()), id))
					.build(); 
		}catch(DataAccessException ex){
			throw new InternalApplicationException("Could not add category: " + ex.getMessage()); 
		}
	}

}
