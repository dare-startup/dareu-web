package com.dareu.web.resource;

import com.dareu.web.core.annotation.AllowedUsers;
import com.dareu.web.core.annotation.Secured;
import com.dareu.web.core.security.DareuPrincipal;
import com.dareu.web.core.security.SecurityRole;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 *
 * @author jose.rubalcaba
 */
@Path("test")
public class SecurityTestResource {
    
    @Inject
    private DareuPrincipal principal; 
    
    @Path(value = "security")
    @GET
    @Produces
    @Secured
    @AllowedUsers(securityRoles = { SecurityRole.USER })
    public Response testSecurity(){
        if(principal != null){
            
        }
        return Response.ok()
                .build();
    }
    
    @Path(value = "security1")
    @GET
    @Produces
    public Response testSecurity1(){
        return Response.ok()
                .build();
    }
}
