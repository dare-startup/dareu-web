package com.dareu.web.resource;

import com.dareu.web.core.annotation.AllowedUsers;
import com.dareu.web.core.annotation.Secured;
import com.dareu.web.core.service.DareService;
import com.dareu.web.dto.request.AnchorContentRequest;
import com.dareu.web.dto.response.AuthorizationResponse;
import com.dareu.web.dto.response.EntityRegistrationResponse;
import com.dareu.web.dto.response.UpdatedEntityResponse;
import com.dareu.web.dto.response.entity.AnchoredDescription;
import com.dareu.web.dto.response.entity.Page;
import com.dareu.web.dto.security.SecurityRole;
import com.dareu.web.exception.application.InternalApplicationException;
import com.dareu.web.exception.application.InvalidRequestException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Alberto Rubalcaba <arubalcaba@24hourfit.com>
 */
@Path("dare/response/anchor")
@AllowedUsers(securityRoles = {SecurityRole.SPONSOR, SecurityRole.USER, SecurityRole.ADMIN})
public class ResponseAnchorResource {

    @Inject
    private DareService dareService;

    @ApiOperation(value = "Stars a dare response to a user account", produces = "application/json",
            consumes = "application/json",
            authorizations = {
                    @Authorization(value = "MEMBER"),
                    @Authorization(value = "ADMIN"),
                    @Authorization(value = "SPONSOR")},
            notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The operation ran successfuly",
                    response = EntityRegistrationResponse.class),
            @ApiResponse(code = 401, message = "User is not authorized to access this resource",
                    response = AuthorizationResponse.class)
    })
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Secured
    public Response anchorContent(@QueryParam("responseId")String responseId,
                                @HeaderParam("Authorization")String token)throws InternalApplicationException, 
                                                            InvalidRequestException{
        return dareService.anchorContent(responseId, token);
    }

    @ApiOperation(value = "Removes a dare response starred by a user", produces = "application/json",
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
    @Path("unpin")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Secured
    public Response unpinAnchoredContent(@QueryParam("anchorContentId")String anchoredContentId,
                                @HeaderParam("Authorization")String token)throws InternalApplicationException, 
                                                            InvalidRequestException{
        return dareService.unpinAnchoredContent(anchoredContentId, token);
    }

    @ApiOperation(value = "Get anchored content for a specific user", produces = "application/json",
            consumes = "application/json",
            authorizations = {
                    @Authorization(value = "MEMBER"),
                    @Authorization(value = "ADMIN"),
                    @Authorization(value = "SPONSOR")},
            notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The operation ran successfuly",
                    response = Page.class),
            @ApiResponse(code = 401, message = "User is not authorized to access this resource",
                    response = AuthorizationResponse.class)
    })
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Secured
    public Response getAnchoredContent(@QueryParam("pageNumber")int pageNumber, 
                                    @HeaderParam("Authorization")String token)throws InternalApplicationException, 
                                                            InvalidRequestException{
        return dareService.getAnchoredContent(pageNumber, token);
    }
}
