package com.dareu.web.resource;

import com.dareu.web.core.annotation.AllowedUsers;
import com.dareu.web.core.annotation.Secured;
import com.dareu.web.core.service.AccountService;
import com.dareu.web.dto.request.ChangeEmailAddressRequest;
import com.dareu.web.dto.response.AuthorizationResponse;
import com.dareu.web.dto.response.UpdatedEntityResponse;
import com.dareu.web.dto.security.SecurityRole;
import com.dareu.web.exception.application.InternalApplicationException;
import com.dareu.web.exception.application.InvalidRequestException;
import io.swagger.annotations.*;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by jose.rubalcaba on 03/21/2017.
 */
@Path("account/profile")
@Api(basePath = "account/profile", description = "Process requests regarding to user profile operations", value = "/account/profile",
        consumes = "application/json") //swagger documentation
@AllowedUsers(securityRoles = {SecurityRole.SPONSOR, SecurityRole.ADMIN, SecurityRole.USER})
public class ProfileResource {

    @Inject
    private AccountService accountService;

    @ApiOperation(value = "Update a logged user profile image", produces = "application/json",
            consumes = "multipart/form-data",
            authorizations = {
                    @Authorization(value = "MEMBER"),
                    @Authorization(value = "ADMIN"),
                    @Authorization(value = "SPONSOR")},
            notes = "Update a logged user profile image")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The operation ran successfuly",
                    response = UpdatedEntityResponse.class),
            @ApiResponse(code = 401, message = "User is not authorized to access this resource",
                    response = AuthorizationResponse.class)
    })
    @Path("image")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @POST
    @Secured
    public Response updateImage(
            @ApiParam(name = "input", required = true) MultipartFormDataInput input, @HeaderParam("Authorization") String auth) throws InternalApplicationException {
        return accountService.updateProfileImage(input, auth);
    }


    @ApiOperation(value = "Get a user account profile", produces = "application/json",
            authorizations = {
                    @Authorization(value = "MEMBER"),
                    @Authorization(value = "ADMIN"),
                    @Authorization(value = "SPONSOR")},
            notes = "Get a user account profile")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The operation ran successfuly",
                    response = UpdatedEntityResponse.class),
            @ApiResponse(code = 401, message = "User is not authorized to access this resource",
                    response = AuthorizationResponse.class)
    })
    @Path("{userId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response getUserAccountProfile(@PathParam("userId") String userId, @HeaderParam("Authorization")String auth)
                        throws InternalApplicationException, InvalidRequestException {
        return accountService.getUserProfile(userId, auth);
    }
}
