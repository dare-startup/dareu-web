package com.dareu.web.core.service.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import com.dareu.web.core.service.DareService;
import com.dareu.web.data.repository.DareRepository;
import com.dareu.web.data.request.CreateDareRequest;
import com.dareu.web.exception.InternalApplicationException;

@Stateless
public class DareServiceImpl implements DareService{

	@Inject
	private DareRepository dareRepository; 
	
	public DareServiceImpl(){
		
	}
	
	@Override
	public Response createNewDare(CreateDareRequest request)
			throws InternalApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

}
