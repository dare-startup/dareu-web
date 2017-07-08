package com.dareu.web.resource.admin;

import com.dareu.web.core.annotation.AllowedUsers;
import com.dareu.web.core.annotation.Secured;
import com.dareu.web.dto.request.EditCategoryRequest;
import com.dareu.web.dto.response.AuthorizationResponse;
import com.dareu.web.dto.response.EntityRegistrationResponse;
import com.dareu.web.dto.response.UpdatedEntityResponse;
import com.dareu.web.dto.response.entity.Page;
import com.dareu.web.dto.security.SecurityRole;
import com.dareu.web.core.service.DareService;
import com.dareu.web.dto.request.CreateCategoryRequest;
import com.dareu.web.exception.application.InternalApplicationException;
import com.dareu.web.exception.application.InvalidRequestException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

import javax.inject.Inject;
import javax.ws.rs.*;
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

    @ApiOperation(value = "Updates a category to be available globally", produces = "application/json",
            consumes = "application/json",
            authorizations = {
                    @Authorization(value = "MEMBER"),
                    @Authorization(value = "ADMIN"),
                    @Authorization(value = "SPONSOR")},
            notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The operation ran successfully",
                    response = UpdatedEntityResponse.class),
            @ApiResponse(code = 401, message = "User is not authorized to access this resource",
                    response = AuthorizationResponse.class)
    })
    @Path("category/update")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response editCategory(EditCategoryRequest request)
            throws InvalidRequestException,
            InternalApplicationException {
        return dareService.editCategory(request);
    }


    @ApiOperation(value = "Get unapproved dare responses for approval", produces = "application/json",
            consumes = "application/json",
            authorizations = {
                    @Authorization(value = "MEMBER"),
                    @Authorization(value = "ADMIN"),
                    @Authorization(value = "SPONSOR")},
            notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The operation ran successfully",
                    response = Page.class),
            @ApiResponse(code = 401, message = "User is not authorized to access this resource",
                    response = AuthorizationResponse.class)
    })
    @Path("/unapproved")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response findUnapprovedDares(@QueryParam("pageNumber")int pageNumber)throws InternalApplicationException{
        return dareService.findUnapprovedDares(pageNumber); 
    }


    @ApiOperation(value = "Creates a new sponsored dare to be available globally", produces = "application/json",
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
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured
    public Response createSponsoredDare(){
        return null; 
    }
}
