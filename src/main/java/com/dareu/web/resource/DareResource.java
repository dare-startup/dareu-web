package com.dareu.web.resource;

import com.dareu.web.core.annotation.AllowedUsers;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.dareu.web.core.annotation.Secured;
import com.dareu.web.dto.security.SecurityRole;
import com.dareu.web.core.service.DareService;
import com.dareu.web.dto.request.CreateDareRequest;
import com.dareu.web.dto.request.DareConfirmationRequest;
import com.dareu.web.dto.request.FlagDareRequest;
import com.dareu.web.dto.response.AuthorizationResponse;
import com.dareu.web.dto.response.EntityRegistrationResponse;
import com.dareu.web.dto.response.UpdatedEntityResponse;
import com.dareu.web.dto.response.entity.ActiveDare;
import com.dareu.web.dto.response.entity.DareDescription;
import com.dareu.web.dto.response.entity.Page;
import com.dareu.web.dto.response.entity.UnacceptedDare;
import com.dareu.web.exception.application.InternalApplicationException;
import com.dareu.web.exception.application.InvalidRequestException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.QueryParam;

@Api(value = "dare", authorizations = {
                @Authorization(value = "MEMBER"),
                @Authorization(value = "ADMIN"),
                @Authorization(value = "SPONSOR")})
@Path("dare/")
@AllowedUsers(securityRoles = {SecurityRole.SPONSOR, SecurityRole.USER, SecurityRole.ADMIN})
public class DareResource {

    @Inject
    private DareService dareService;

    @ApiOperation(value = "Get created dares", produces = "application/json",
            notes = "Get created dares from logged user")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The operation ran successfuly",
                response = Page.class),
        @ApiResponse(code = 401, message = "User is not authorized to access this resource",
                response = AuthorizationResponse.class)
    })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response getCreatedDares(@HeaderParam("Authorization") String auth,
            @ApiParam(allowableValues = "number", defaultValue = "1", name = "pageNumber")
            @DefaultValue("1") @QueryParam("pageNumber") int pageNumber) throws InternalApplicationException, InvalidRequestException {
        return dareService.findCreatedDares(auth, pageNumber);
    }

    @ApiOperation(value = "Get the current active/accepted dare", produces = "application/json",
            notes = "Returns no content if ther is no current active dare")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The operation ran successfuly",
                response = ActiveDare.class),
        @ApiResponse(code = 401, message = "User is not authorized to access this resource",
                response = AuthorizationResponse.class)
    })
    @Path("active")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response getCurrentActiveDare(@HeaderParam("Authorization") String auth) throws InternalApplicationException {
        return dareService.getCurrentActiveDare(auth);
    }

    @ApiOperation(value = "Creates a new dare", produces = "application/json",
            consumes = "application/json")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The operation ran successfuly",
                response = EntityRegistrationResponse.class),
        @ApiResponse(code = 401, message = "User is not authorized to access this resource",
                response = AuthorizationResponse.class)
    })
    @Path("create")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response createNewDare(
            @ApiParam(allowableValues = "CreateDareRequest class", name = "createDareRequest",
                    required = true) CreateDareRequest request, @HeaderParam("Authorization") String auth) throws InternalApplicationException,
            InvalidRequestException {
        return dareService.createNewDare(request, auth);
    }

    @ApiOperation(value = "Confirms a dare request", produces = "application/json",
            consumes = "application/json",
            notes = "Accept or decline a dare request")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The operation ran successfuly",
                response = UpdatedEntityResponse.class),
        @ApiResponse(code = 401, message = "User is not authorized to access this resource",
                response = AuthorizationResponse.class)
    })
    @Path("confirm")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response confirmDareRequest(
            @ApiParam(allowableValues = "DareConfirmationRequest class", name = "dareConfirmationRequest",
                    required = true) DareConfirmationRequest request) throws InternalApplicationException, InvalidRequestException {
        return dareService.confirmDareRequest(request);
    }

    @ApiOperation(value = "get the top unaccepted/pending dare", produces = "application/json",
            notes = "Get an unaccepted/pending dare")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The operation ran successfuly",
                response = UnacceptedDare.class),
        @ApiResponse(code = 401, message = "User is not authorized to access this resource",
                response = AuthorizationResponse.class)
    })
    @Path("unaccepted")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response findUnacceptedDare(@HeaderParam("Authorization") String auth) throws InternalApplicationException {
        return dareService.findUnacceptedDare(auth);
    }

    @ApiOperation(value = "Discover dares", produces = "application/json",
            notes = "Discover a list of dares using pagination")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The operation ran successfuly",
                response = Page.class),
        @ApiResponse(code = 401, message = "User is not authorized to access this resource",
                response = AuthorizationResponse.class)
    })
    @Path("discover")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response discoverDares(
            @ApiParam(allowableValues = "number", name = "pageNumber",
                    required = false) @DefaultValue("1") @QueryParam("pageNumber") int pageNumber, @HeaderParam("Authorization") String authToken) throws InternalApplicationException {
        return dareService.discoverDares(pageNumber, authToken);
    }

    @ApiOperation(value = "Get a list of categories", produces = "application/json",
            notes = "Retrieve a list of categories")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The operation ran successfuly",
                response = Page.class),
        @ApiResponse(code = 401, message = "User is not authorized to access this resource",
                response = AuthorizationResponse.class)
    })
    @Path("category")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response getCategories(
            @ApiParam(allowableValues = "number", name = "pageNumber",
                    required = false) @QueryParam("pageNumber") int pageNumber) throws InternalApplicationException {
        return dareService.getCategories(pageNumber);
    }

    @ApiOperation(value = "Find a dare description", produces = "application/json", 
            notes = "Retrieve a dare description using a dare ID")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The operation ran successfuly", 
                response = DareDescription.class), 
        @ApiResponse(code = 401, message = "User is not authorized to access this resource", 
                response = AuthorizationResponse.class)
    })
    @Path("find")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response findDareDescription(
            @ApiParam(allowableValues = "String", name = "dareId",
                    required = true) @QueryParam("dareId") String dareId) throws InternalApplicationException, InvalidRequestException {
        return dareService.findDareDescription(dareId);
    }

    
    
    

    @ApiOperation(value = "Flags an existing dare", produces = "application/json", 
            notes = "Flag a dare as inappropriate")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The operation ran successfuly", 
                response = EntityRegistrationResponse.class), 
        @ApiResponse(code = 401, message = "User is not authorized to access this resource", 
                response = AuthorizationResponse.class)
    })
    @Path("flag")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured
    public Response flagDare(
            @ApiParam(allowableValues = "FlagDareRequest class", name = "request",
                    required = true) FlagDareRequest request, @HeaderParam("Authorization") String auth) throws InternalApplicationException, InvalidRequestException {
        return dareService.flagDare(request, auth);
    }
    
    
    @ApiOperation(value = "set a dare as expired", produces = "application/json", 
            notes = "Set a dare as expired")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The operation ran successfully",
                response = EntityRegistrationResponse.class), 
        @ApiResponse(code = 401, message = "User is not authorized to access this resource", 
                response = AuthorizationResponse.class)
    })
    @Path("expired")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response expireDare(@QueryParam("dareId")String dareId, @HeaderParam("Authorization")String auth)throws InternalApplicationException, InvalidRequestException{
        return dareService.setDareExpired(dareId, auth); 
    }
}
