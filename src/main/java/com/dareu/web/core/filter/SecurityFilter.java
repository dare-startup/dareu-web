/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.core.filter;

import com.dareu.web.core.annotation.AllowedUsers;
import com.dareu.web.core.annotation.Secured;
import com.dareu.web.core.security.DareuPrincipal;
import com.dareu.web.core.security.SecurityRole;
import com.dareu.web.data.entity.DareUser;
import com.dareu.web.data.repository.DareUserRepository;
import com.dareu.web.data.response.AuthorizationResponse;
import com.dareu.web.exception.DataAccessException;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.spi.ResteasyProviderFactory;


/**
 *
 * @author MACARENA
 */
@Secured
@Provider
public class SecurityFilter implements ContainerRequestFilter{

	@Inject
	private Logger log; 
	
	@Inject
	private DareUserRepository dareUserRepository; 
	
    public void filter(ContainerRequestContext crc) throws IOException {
        //authenticate user here...
    	log.info("SecurityFilter reached"); 
        ResourceMethodInvoker methodInvoker = (ResourceMethodInvoker) crc
				.getProperty("org.jboss.resteasy.core.ResourceMethodInvoker");
        //get invoked method 
        Method method = methodInvoker.getMethod(); 
        //get a security context reference
        SecurityContext securityContext = crc.getSecurityContext(); 
        //get token 
    	String auth = crc.getHeaderString("Authorization"); 
        
        //first check if the class checks if is annotated with @AllowedUsers
        if(method.getDeclaringClass().isAnnotationPresent(AllowedUsers.class)){
            
        }
        //check annotation 
        if(method.isAnnotationPresent(AllowedUsers.class)){
            AllowedUsers securityAnnotation = method.getAnnotation(AllowedUsers.class); 
            SecurityRole[] allowedRoles = securityAnnotation.securityRoles();
            //validate token
            if(auth == null || auth.isEmpty())
                //no token content
                abort(crc); 
            else if(! validateToken(allowedRoles, auth))
                //token have not been successfully validated
                abort(crc);    
        }
    }
    
    private boolean validateToken(SecurityRole[] roles, String token){
        //get user by token
        DareUser user = null; 
        try{
            user = dareUserRepository.findUserByToken(token); 
            if(user != null){
                for(SecurityRole role : roles){
                    if(user.getRole() == role){
                        //the user has role and has been authenticated successfully
                        //push security context 
                        ResteasyProviderFactory.pushContext(DareuPrincipal.class, 
                                new DareuPrincipal(user.getId()));
                        return true; 
                    }
                }
                return false; 
            }else return false; 
        }catch(DataAccessException ex){
            log.severe("Error finding user: " + ex.getMessage()); 
            return false; 
        }
    }
    
    private void abort(ContainerRequestContext cxt){
    	cxt.abortWith(Response.status(Response.Status.UNAUTHORIZED)
				.entity(new AuthorizationResponse("Unauthorized"))
                .type(MediaType.APPLICATION_JSON)
				.build());
    }
    
}
