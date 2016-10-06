package com.dareu.web.resource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.dareu.web.data.repository.DareRepository;

@Path("testing")
public class JpaTestResource {
	
	@Inject
	private DareRepository repository; 
	
	@GET
	@Produces
	public Response getDares(){
		return Response.ok()
				.build();
	}
}
