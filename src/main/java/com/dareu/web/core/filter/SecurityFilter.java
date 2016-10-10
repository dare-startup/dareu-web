/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.core.filter;

import com.dareu.web.core.annotation.Secured;
import com.dareu.web.data.entity.DareUser;
import com.dareu.web.data.repository.DareUserRepository;
import com.dareu.web.data.response.AuthenticationResponse;
import com.dareu.web.data.response.AuthorizationResponse;
import com.dareu.web.exception.DataAccessException;

import java.io.IOException;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.annotations.interception.Precedence;

/**
 *
 * @author MACARENA
 */
@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter{

	@Inject
	private Logger log; 
	
	@Inject
	private DareUserRepository repository; 
	
    public void filter(ContainerRequestContext crc) throws IOException {
        //authenticate user here...
    	log.info("SecurityFilter reached"); 
    	//get token 
    	String auth = crc.getHeaderString("Authorization"); 
    	if(auth != null){
    		//search the user 
    		DareUser user = null; 
    		try{
    			user = repository.findUserByToken(auth); 
    			if(user == null){
    				abort(crc);
    			}
    		}catch(DataAccessException ex){
    			abort(crc);
    		}
    	}else{
    		//return unauthorized response
    		abort(crc);
    	}
    }
    
    private void abort(ContainerRequestContext cxt){
    	cxt.abortWith(Response.status(Response.Status.UNAUTHORIZED)
				.entity(new AuthorizationResponse("Unauthorized"))
				.build());
    }
    
}
