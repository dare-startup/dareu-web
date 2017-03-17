package com.dareu.web.resource;

import com.dareu.web.core.annotation.AllowedUsers;
import com.dareu.web.core.annotation.Secured;
import com.dareu.web.core.service.DareService;
import com.dareu.web.dto.request.NewCommentRequest;
import com.dareu.web.dto.response.AuthorizationResponse;
import com.dareu.web.dto.response.EntityRegistrationResponse;
import com.dareu.web.dto.security.SecurityRole;
import com.dareu.web.exception.application.InternalApplicationException;
import com.dareu.web.exception.application.InvalidRequestException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Alberto Rubalcaba <arubalcaba@24hourfit.com>
 */
@Path("dare/response/comment")
@AllowedUsers(securityRoles = {SecurityRole.SPONSOR, SecurityRole.USER, SecurityRole.ADMIN})
public class ResponseCommentResource {
    
    @Inject
    private DareService dareService;

    
    @ApiOperation(value = "Creates a new response comment", 
            consumes = "application/json", produces = "application/json", 
            notes = "Creates a new comment over a response entity")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The operation ran successfuly", 
                response = EntityRegistrationResponse.class), 
        @ApiResponse(code = 401, message = "User is not authorized to access this resource", 
                response = AuthorizationResponse.class)
    })
    @Path("create")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured
    public Response createResponseComment(NewCommentRequest request, 
                                    @HeaderParam("Authorization")String token) throws InternalApplicationException, InvalidRequestException {
        return dareService.createResponseComment(request, token); 
    }
    
    @ApiOperation(value = "Find a page of comments", 
            produces = "application/json", 
            notes = "Get a response comments page")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The operation ran successfuly", 
                response = EntityRegistrationResponse.class), 
        @ApiResponse(code = 401, message = "User is not authorized to access this resource", 
                response = AuthorizationResponse.class)
    })
    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured
    public Response findResponseComments(@DefaultValue("1") @QueryParam("pageNumber")int pageNumber, 
                                        @QueryParam("responseId")String responseId) throws InternalApplicationException, InvalidRequestException {
        return dareService.findResponseComments(pageNumber, responseId); 
    }
    
     @ApiOperation(value = "Find a comment", 
            produces = "application/json", 
            notes = "Get a comment")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The operation ran successfuly", 
                response = EntityRegistrationResponse.class), 
        @ApiResponse(code = 401, message = "User is not authorized to access this resource", 
                response = AuthorizationResponse.class)
    })
    @Path("find")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured
    public Response findCommentDescription(@QueryParam("commentId")String commentId) throws InternalApplicationException, InvalidRequestException {
        return dareService.findResponseComment(commentId); 
    }
    
}
