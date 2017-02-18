package com.dareu.web.resource;

import com.dareu.web.core.annotation.AllowedUsers;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import com.dareu.web.core.annotation.Secured;
import com.dareu.web.dto.security.SecurityRole;
import com.dareu.web.core.service.AccountService;
import com.dareu.web.core.service.MultipartService;
import com.dareu.web.data.exception.AuthenticationException;
import com.dareu.web.dto.request.ChangeEmailAddressRequest;
import com.dareu.web.dto.response.AuthorizationResponse;
import com.dareu.web.dto.response.entity.Page;

import com.dareu.web.dto.response.EntityRegistrationResponse;
import com.dareu.web.dto.response.UpdatedEntityResponse;
import com.dareu.web.dto.response.entity.ConnectionDetails;
import com.dareu.web.exception.application.InternalApplicationException;
import com.dareu.web.exception.application.InvalidRequestException;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import java.io.InputStream;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path("account/")
@Api(basePath = "account", description = "Process requests regarding to Account operations", value = "/account",
        consumes = "application/json") //swagger documentation
@AllowedUsers(securityRoles = {SecurityRole.SPONSOR, SecurityRole.ADMIN, SecurityRole.USER})
public class AccountResource {

    @Inject
    private MultipartService multipartService;

    @Inject
    private AccountService accountService;

    /**
     * Get current profile from a logged user
     *
     * @return
     * @throws com.dareu.web.exception.application.InternalApplicationException
     */
    @Path("me")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @ApiOperation(value = "Retrieve a user profile",
            httpMethod = "GET",
            nickname = "me")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The request has been processed successfully"),
        @ApiResponse(code = 401, message = "Unauthorized to access this operation")})
    @Secured
    public Response me() throws InternalApplicationException{
        return accountService.me() ;
    }

    /**
     * Request a friendship to another dareu user
     *
     * @param requestedUserId
     * @param request
     * @return
     * @throws InternalApplicationException
     * @throws InvalidRequestException
     */
    @ApiOperation(value = "Requests a new friendship", produces = "application/json",
            authorizations = {
                @Authorization(value = "MEMBER"),
                @Authorization(value = "ADMIN"),
                @Authorization(value = "SPONSOR")},
            notes = "Request a new friendship to another user")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The operation ran successfuly",
                response = EntityRegistrationResponse.class),
        @ApiResponse(code = 401, message = "User is not authorized to access this resource",
                response = AuthorizationResponse.class)
    })
    @Path("friendship/{requestedUserId}/create")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Secured
    public Response requestFriendship(@PathParam(value = "requestedUserId") String requestedUserId) throws InvalidRequestException, InternalApplicationException {
        return accountService.requestFriendship(requestedUserId);
    }

    /**
     *
     * @param pageNumber
     * @param query
     * @return
     * @throws com.dareu.web.exception.application.InternalApplicationException
     */
    @ApiOperation(value = "Find frinds using a search query and pagination", produces = "application/json",
            authorizations = {
                @Authorization(value = "MEMBER"),
                @Authorization(value = "ADMIN"),
                @Authorization(value = "SPONSOR")},
            notes = "Find friends")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The operation ran successfuly",
                response = Page.class),
        @ApiResponse(code = 401, message = "User is not authorized to access this resource",
                response = AuthorizationResponse.class)
    })
    @GET
    @Path("friends/find")
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response friends(
            @ApiParam(name = "pageNumber", defaultValue = "1") @QueryParam("pageNumber") int pageNumber, 
            @ApiParam(name = "q", required = false)@QueryParam("q") String query) throws InternalApplicationException {
        return accountService.findFriends(pageNumber, query);
    }

    /**
     * Update a registration id from Google Cloud Messaging
     *
     * @param regId
     * @param auth
     * @return
     * @throws com.dareu.web.exception.application.InvalidRequestException
     * @throws com.dareu.web.exception.application.InternalApplicationException
     */
    @ApiOperation(value = "Updates a FCM registration id", produces = "application/json",
            authorizations = {
                @Authorization(value = "MEMBER"),
                @Authorization(value = "ADMIN"),
                @Authorization(value = "SPONSOR")},
            notes = "Updates a FCM token ")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The operation ran successfuly",
                response = UpdatedEntityResponse.class),
        @ApiResponse(code = 401, message = "User is not authorized to access this resource",
                response = AuthorizationResponse.class)
    })
    @Path("updateGcmRegId")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Secured
    public Response updateGcmRegId(
            @ApiParam(name = "regId",required = true) @QueryParam("regId") String regId, 
            @HeaderParam("Authorization") String auth) throws InvalidRequestException, InternalApplicationException {
        return accountService.updateRegId(regId, auth);
    }

    @ApiOperation(value = "Confirms a friendship request", produces = "application/json",
            authorizations = {
                @Authorization(value = "MEMBER"),
                @Authorization(value = "ADMIN"),
                @Authorization(value = "SPONSOR")},
            notes = "Accepts or decline a friendship request")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The operation ran successfuly",
                response = EntityRegistrationResponse.class),
        @ApiResponse(code = 401, message = "User is not authorized to access this resource",
                response = AuthorizationResponse.class)
    })
    @Path("friendship/{userId}/update")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response responseFriendship(@PathParam(value = "userId") String userId, 
            @ApiParam(name = "accepted", required = true) @QueryParam(value = "accepted") Boolean accepted) throws InvalidRequestException, InternalApplicationException {
        return accountService.friendshipResponse(userId, accepted);
    }

    @ApiOperation(value = "Get details about a friendship", produces = "application/json",
            authorizations = {
                @Authorization(value = "MEMBER"),
                @Authorization(value = "ADMIN"),
                @Authorization(value = "SPONSOR")},
            notes = "get details about a friendship between two users")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The operation ran successfuly",
                response = ConnectionDetails.class),
        @ApiResponse(code = 401, message = "User is not authorized to access this resource",
                response = AuthorizationResponse.class)
    })
    @Path("friendship/find")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response findFriendshipDetails(
            @ApiParam(name = "friendshipId", required = true) @QueryParam("friendshipId") String friendshipId, @HeaderParam("Authorization") String auth) throws InternalApplicationException, InvalidRequestException {
        return accountService.findFriendshipDetails(friendshipId, auth);
    }

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
    @Path("profile/image")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @POST
    @Secured
    public Response updateImage(
            @ApiParam(name = "input", required = true) MultipartFormDataInput input, @HeaderParam("Authorization") String auth) throws InternalApplicationException {
        return accountService.updateProfileImage(input, auth);
    }
    
    /**
     *
     * @param userId
     * @param auth
     * @return
     * @throws com.dareu.web.exception.application.InvalidRequestException
     * @throws com.dareu.web.exception.application.InternalApplicationException
     * @Path ("me/profile")
     * @Produces (MediaType.APPLICATION_JSON)
     * @GET
     * @Secured public Response getImage(@HeaderParam("Authorization")String
     * auth)throws InternalApplicationException, InvalidRequestException{ return
     * accountService.getAccountImage(auth); }
     */
    @ApiOperation(value = "Get a user profile image", produces = "image/jpeg",
            authorizations = {
                @Authorization(value = "MEMBER"),
                @Authorization(value = "ADMIN"),
                @Authorization(value = "SPONSOR")},
            notes = "Get a user profile image, if there is no userId provided, will return the logged user image")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The operation ran successfuly",
                response = InputStream.class),
        @ApiResponse(code = 401, message = "User is not authorized to access this resource",
                response = AuthorizationResponse.class)
    })
    @Path("profile/image")
    @Produces("image/jpeg")
    @GET
    @Secured
    public Response getImage(
            @ApiParam(name = "userId", required = false)@QueryParam("userId") String userId, @HeaderParam("Authorization") String auth) throws InvalidRequestException, InternalApplicationException {
        return accountService.getAccountImage(userId, auth);
    }

    @ApiOperation(value = "Discover users", produces = "application/json",
            authorizations = {
                @Authorization(value = "MEMBER"),
                @Authorization(value = "ADMIN"),
                @Authorization(value = "SPONSOR")},
            notes = "Get a list of new users")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The operation ran successfuly",
                response = Page.class),
        @ApiResponse(code = 401, message = "User is not authorized to access this resource",
                response = AuthorizationResponse.class)
    })
    @Path("discoverUsers")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response discoverUsers(
            @ApiParam(name = "pageNumber", required = false)@QueryParam("pageNumber") int pageNumber) throws InternalApplicationException {
        return accountService.discoverUsers(pageNumber);
    }
    
    @ApiOperation(value = "Change an account email address", produces = "application/json",
            consumes = "application/json",
            authorizations = {
                @Authorization(value = "MEMBER"),
                @Authorization(value = "ADMIN"),
                @Authorization(value = "SPONSOR")},
            notes = "Updates a user email address")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The operation ran successfuly",
                response = UpdatedEntityResponse.class),
        @ApiResponse(code = 401, message = "User is not authorized to access this resource",
                response = AuthorizationResponse.class)
    })
    @Path("changeEmailAddress")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured
    public Response changeEmailAddress(
            @ApiParam(name = "request", required = true) ChangeEmailAddressRequest request, @HeaderParam("Authorization")String auth)throws InternalApplicationException, InvalidRequestException{
        return accountService.changeEmailAddress(request, auth); 
    }
}
