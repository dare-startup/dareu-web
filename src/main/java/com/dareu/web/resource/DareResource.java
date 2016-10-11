package com.dareu.web.resource;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.dareu.web.core.annotation.Secured;
import com.dareu.web.core.service.AccountService;
import com.dareu.web.core.service.DareService;
import com.dareu.web.data.request.CreateCategoryRequest;
import com.dareu.web.data.request.CreateDareRequest;
import com.dareu.web.exception.InternalApplicationException;
import com.dareu.web.exception.InvalidRequestException;

@Path("dare/")
public class DareResource {
	
	@Inject
	private DareService dareService; 
	
	@Inject
	private AccountService accountService;
	
	@Inject
	private Logger log; 
	
	@Path("create")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createNewDare(CreateDareRequest request)throws InternalApplicationException, 
								  InvalidRequestException, InternalApplicationException{
		return dareService.createNewDare(request); 
	}
	
	@Path("category/create")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createNewCategory(CreateCategoryRequest request)
								   throws InvalidRequestException, 
								   InternalApplicationException{
		return dareService.createNewCategory(request); 
	}
}
