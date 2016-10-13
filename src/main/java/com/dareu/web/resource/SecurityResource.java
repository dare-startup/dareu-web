package com.dareu.web.resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.dareu.web.core.service.AccountService;
import com.dareu.web.data.request.SigninRequest;
import com.dareu.web.exception.AuthenticationException;

import io.swagger.annotations.Api;

@Api(value="security")
@Path("security/")
public class SecurityResource {
	
	@Inject
	private AccountService accountService; 
	
	/**
     * Signin using a google account
     * @return 
     */
    @Path("authenticate")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signin(SigninRequest request)throws AuthenticationException{
        return accountService.authenticate(request);  
    }
}
