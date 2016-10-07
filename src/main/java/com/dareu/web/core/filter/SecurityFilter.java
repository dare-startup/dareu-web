/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.core.filter;

import com.dareu.web.core.annotation.Secured;

import java.io.IOException;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

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
	
    public void filter(ContainerRequestContext crc) throws IOException {
        //authenticate user here...
    	log.info("SecurityFilter reached"); 
    }
    
}
