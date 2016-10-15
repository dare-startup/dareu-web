package com.dareu.web.resource.admin;

import com.dareu.web.core.annotation.AllowedUsers;
import com.dareu.web.core.security.SecurityRole;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author jose.rubalcaba
 */
@Path("admin/account")
@AllowedUsers(securityRoles = { SecurityRole.ADMIN })
public class AdminAccountResource {
    /**
     * Register a new sponsor user
     * @return 
     */
    @Path("registerSponsor")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerSponsorUser(){
        return null; 
    }
}
