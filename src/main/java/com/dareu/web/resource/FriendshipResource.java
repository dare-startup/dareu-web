package com.dareu.web.resource;

import com.dareu.web.core.annotation.Secured;
import com.dareu.web.core.service.AccountService;
import com.dareu.web.dto.response.AuthorizationResponse;
import com.dareu.web.dto.response.EntityRegistrationResponse;
import com.dareu.web.dto.response.entity.ConnectionDetails;
import com.dareu.web.dto.response.entity.Page;
import com.dareu.web.exception.application.InternalApplicationException;
import com.dareu.web.exception.application.InvalidRequestException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
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
@Path("account/friendship")
public class FriendshipResource {
    
    
    @Inject
    private AccountService accountService;
    
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
    @Path("{requestedUserId}/create")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Secured
    public Response requestFriendship(@PathParam(value = "requestedUserId") String requestedUserId) throws InvalidRequestException, InternalApplicationException {
        return accountService.requestFriendship(requestedUserId);
    }
    
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
    @Path("sent")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Secured
    public Response getPendingSentRequests(@QueryParam("pageNumber") int pageNumber, 
                                          @HeaderParam(value = "Authorization")String token) throws InvalidRequestException, InternalApplicationException {
        return accountService.getPendingSentRequests(pageNumber, token);
    }
    
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
    @Path("received")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Secured
    public Response getPendingReceivedRequests(@QueryParam("pageNumber") int pageNumber, 
                                          @HeaderParam(value = "Authorization")String token) throws InvalidRequestException, InternalApplicationException {
        return accountService.getPendingReceivedRequests(pageNumber, token);
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
    @Path("accepted/find")
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response friends(
            @ApiParam(name = "pageNumber", defaultValue = "1") @QueryParam("pageNumber") int pageNumber, 
            @ApiParam(name = "q", required = false)@QueryParam("q") String query) throws InternalApplicationException {
        return accountService.findFriends(pageNumber, query);
    }
    
    
    @ApiOperation(value = "Cancel a friendship request", produces = "application/json",
            consumes = "application/json",
            authorizations = {
                @Authorization(value = "MEMBER"),
                @Authorization(value = "ADMIN"),
                @Authorization(value = "SPONSOR")},
            notes = "Cancels a friendship request")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The operation ran successfuly",
                response = EntityRegistrationResponse.class),
        @ApiResponse(code = 401, message = "User is not authorized to access this resource",
                response = AuthorizationResponse.class)
    })
    @Path("/")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response cancelFriendshipRequest(@QueryParam("connectionId")String connId, 
                        @HeaderParam("Authrorization")String token)throws InternalApplicationException, InvalidRequestException{
        return accountService.cancelFriendshipRequest(connId, token);
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
    @Path("{userId}/update")
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
    @Path("find")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response findFriendshipDetails(
            @ApiParam(name = "friendshipId", required = true) @QueryParam("friendshipId") String friendshipId, @HeaderParam("Authorization") String auth) throws InternalApplicationException, InvalidRequestException {
        return accountService.findFriendshipDetails(friendshipId, auth);
    }
}
