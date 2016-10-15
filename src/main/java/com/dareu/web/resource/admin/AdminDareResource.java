package com.dareu.web.resource.admin;

import com.dareu.web.core.annotation.AllowedUsers;
import com.dareu.web.core.annotation.Secured;
import com.dareu.web.core.security.SecurityRole;
import com.dareu.web.core.service.DareService;
import com.dareu.web.data.request.CreateCategoryRequest;
import com.dareu.web.exception.InternalApplicationException;
import com.dareu.web.exception.InvalidRequestException;
import javax.inject.Inject;
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
@Path("admin/dare")
@AllowedUsers(securityRoles = { SecurityRole.ADMIN })
public class AdminDareResource {
    
    @Inject
    private DareService dareService; 
    
    
    @Path("category/create")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response createNewCategory(CreateCategoryRequest request)
            throws InvalidRequestException,
            InternalApplicationException {
        return dareService.createNewCategory(request);
    }
}