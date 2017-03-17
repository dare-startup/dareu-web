/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dareu.web.core.filter;

import com.dareu.web.core.annotation.AllowedUsers;
import com.dareu.web.core.annotation.Secured;
import com.dareu.web.core.security.DareuPrincipal;
import com.dareu.web.dto.security.SecurityRole;
import com.dareu.web.data.entity.DareUser;
import com.dareu.web.data.repository.DareUserRepository;
import com.dareu.web.dto.response.AuthorizationResponse;
import com.dareu.web.data.exception.DataAccessException;
import com.github.roar109.syring.annotation.ApplicationProperty;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

/**
 *
 * @author MACARENA
 */
@Secured
@Provider
public class SecurityFilter implements ContainerRequestFilter {

    @Inject
    private Logger log;

    @Inject
    private DareUserRepository dareUserRepository;

    @Inject
    @ApplicationProperty(name = "com.dareu.web.admin.token", type = ApplicationProperty.Types.SYSTEM)
    private String adminToken;

    @Override
    public void filter(ContainerRequestContext crc) throws IOException {
       //authenticate user here...
        ResourceMethodInvoker methodInvoker = (ResourceMethodInvoker) crc
                .getProperty("org.jboss.resteasy.core.ResourceMethodInvoker");
        //get invoked method 
        Method method = methodInvoker.getMethod();
        //get token 
        String auth = crc.getHeaderString("Authorization");
        //check if the incoming request is from the admin token 
        if(auth != null && auth.equals(adminToken)){
            //let it pass.. TODO:(can this be secure)
            return;
        }
        //first check if the class checks if is annotated with @AllowedUsers
        if (method.getDeclaringClass().isAnnotationPresent(AllowedUsers.class)) {
            //validate 
            validate(method.getDeclaringClass().getAnnotation(AllowedUsers.class), auth, crc);
        } else if (method.isAnnotationPresent(AllowedUsers.class)) {
            validate(method.getAnnotation(AllowedUsers.class), auth, crc);
        }

    }

    private void validate(AllowedUsers annotation, String token, ContainerRequestContext crc) {
        SecurityRole[] allowedRoles = annotation.securityRoles();
        //validate token
        if (token == null || token.isEmpty()) //no token content
        {
            abort(crc);
        } else if (!validateToken(allowedRoles, token)) //token have not been successfully validated
        {
            abort(crc);
        }

    }

    private boolean validateToken(SecurityRole[] roles, String token) {
        //get user by token
        DareUser user = null;
        try {
            user = dareUserRepository.findUserByToken(token);
            if (user != null) {
                for (SecurityRole role : roles) {
                    if (user.getRole() == role) {
                        //the user has role and has been authenticated successfully
                        //push security context 
                        ResteasyProviderFactory.pushContext(DareuPrincipal.class,
                                new DareuPrincipal(user.getId()));
                        return true;
                    }
                }
                return false;
            } else {
                return false;
            }
        } catch (DataAccessException ex) {
            log.error("Error finding user: " + ex.getMessage());
            return false;
        }
    }

    private void abort(ContainerRequestContext cxt) {
        cxt.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                .entity(new AuthorizationResponse("Unauthorized"))
                .type(MediaType.APPLICATION_JSON)
                .build());
    }

}
