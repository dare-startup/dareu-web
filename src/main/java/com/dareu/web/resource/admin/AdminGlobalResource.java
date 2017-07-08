package com.dareu.web.resource.admin;

import com.dareu.web.core.annotation.AllowedUsers;
import com.dareu.web.core.annotation.Secured;
import com.dareu.web.core.service.AdminGlobalService;
import com.dareu.web.dto.response.AuthorizationResponse;
import com.dareu.web.dto.response.entity.Page;
import com.dareu.web.dto.security.SecurityRole;
import com.dareu.web.exception.application.InternalApplicationException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("admin/global")
@AllowedUsers(securityRoles = SecurityRole.ADMIN)
public class AdminGlobalResource {

    @Inject
    private AdminGlobalService adminGlobalService;

    @ApiOperation(value = "Get a page of pending contact messages", produces = "application/json",
            authorizations = {
                    @Authorization(value = "ADMIN")},
            notes = "Get a list of flagged dares")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The operation ran successfully",
                    response = Page.class),
            @ApiResponse(code = 401, message = "User is not authorized to access this resource",
                    response = AuthorizationResponse.class)
    })
    @Path("pending/contact")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPendingContactMessages(@QueryParam("pageNumber") int pageNumber)throws InternalApplicationException{
        return adminGlobalService.getPendingContactMessages(pageNumber);
    }
}
