package com.dareu.web.resource.admin;

import com.dareu.web.core.annotation.AllowedUsers;
import com.dareu.web.dto.security.SecurityRole;
import com.dareu.web.core.annotation.Secured;
import com.dareu.web.core.service.AccountService;
import com.dareu.web.exception.InternalApplicationException;
import com.dareu.web.exception.InvalidRequestException;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author jose.rubalcaba
 */
@Path("admin/account")
@AllowedUsers(securityRoles = { SecurityRole.ADMIN })
public class AdminAccountResource {
    
    @Inject
    private AccountService service; 
    
    /**
     * Register a new sponsor user
     * @return 
     */
    @Path("registerSponsor")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured
    public Response registerSponsorUser(){
        return null; 
    }
    
    @Path("findUserByEmail/{email}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response findUserByEmail(@PathParam("email")String email)throws InvalidRequestException, 
                                                    InternalApplicationException{
        return service.findUserById(email); 
    }
    
    @Path("findUsers")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response findUsersByPage(@QueryParam("pageNumber")int pageNumber)throws InternalApplicationException{
        return service.findUsersByPage(pageNumber); 
    }
}
