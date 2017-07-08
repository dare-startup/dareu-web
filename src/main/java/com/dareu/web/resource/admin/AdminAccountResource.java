package com.dareu.web.resource.admin;

import com.dareu.web.core.annotation.AllowedUsers;
import com.dareu.web.dto.response.AuthorizationResponse;
import com.dareu.web.dto.response.EntityRegistrationResponse;
import com.dareu.web.dto.response.entity.Page;
import com.dareu.web.dto.security.SecurityRole;
import com.dareu.web.core.annotation.Secured;
import com.dareu.web.core.service.AccountService;
import com.dareu.web.dto.response.ResourceAvailableResponse;
import com.dareu.web.exception.application.InternalApplicationException;
import com.dareu.web.exception.application.InvalidRequestException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
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
@Api(value = "admin/account")
@Path("admin/account")
@AllowedUsers(securityRoles = { SecurityRole.ADMIN })
public class AdminAccountResource {
    
    @Inject
    private AccountService service;

    @ApiOperation(value = "Creates a new category to be available globally", produces = "application/json",
            consumes = "application/json",
            authorizations = {
                    @Authorization(value = "MEMBER"),
                    @Authorization(value = "ADMIN"),
                    @Authorization(value = "SPONSOR")},
            notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The operation ran successfully",
                    response = EntityRegistrationResponse.class),
            @ApiResponse(code = 401, message = "User is not authorized to access this resource",
                    response = AuthorizationResponse.class)
    })
    @Path("findUserByEmail/{email}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response findUserByEmail(@PathParam("email")String email)throws InvalidRequestException, 
                                                    InternalApplicationException{
        return service.findUserById(email); 
    }

    @ApiOperation(value = "Find users using a page number", produces = "application/json",
            consumes = "application/json",
            authorizations = {
                    @Authorization(value = "ADMIN")},
            notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The operation ran successfully",
                    response = Page.class),
            @ApiResponse(code = 401, message = "User is not authorized to access this resource",
                    response = AuthorizationResponse.class)
    })
    @Path("findUsers")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response findUsersByPage(@QueryParam("pageNumber")int pageNumber)throws InternalApplicationException{
        return service.findUsersByPage(pageNumber); 
    }

    public void pendingContactMessages(){
        //todo
    }
}
