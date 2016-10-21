package com.dareu.web.resource;

import com.dareu.web.core.annotation.AllowedUsers;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import com.dareu.web.core.annotation.Secured;
import com.dareu.web.core.security.SecurityRole;
import com.dareu.web.core.service.AccountService;
import com.dareu.web.core.service.MultipartService;
import com.dareu.web.data.request.FriendshipRequest;
import com.dareu.web.data.request.FriendshipRequestResponse;
import com.dareu.web.data.response.DareUserProfile;
import com.dareu.web.exception.AuthenticationException;
import com.dareu.web.exception.InternalApplicationException;
import com.dareu.web.exception.InvalidRequestException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("account/")
@Api(basePath = "account", description = "Process requests regarding to Account operations", value="/account", 
			consumes = "application/json") //swagger documentation
@AllowedUsers(securityRoles = { SecurityRole.SPONSOR, SecurityRole.ADMIN, SecurityRole.USER })
public class AccountResource {


    @Inject
    private MultipartService multipartService; 
    
    @Inject
    private AccountService accountService; 
    
    /**
     * Get current profile from a logged user
     * @return 
     */
    
    @Path("me")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @ApiOperation(value = "Retrieve a user profile", 
    			response = DareUserProfile.class, 
    			httpMethod = "GET", 
    			nickname = "me")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "The request has been processed successfully"), 
    						@ApiResponse(code = 401, message = "Unauthorized to process request")})
    @Secured
    public Response me(){
        return null; 
    }
    
    /**
     * Request a friendship to another dareu user
     * @return 
     * @throws InternalApplicationException 
     * @throws InvalidRequestException 
     */
    @Path("requestFriendship")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured
    public Response requestFriendship(FriendshipRequest request) throws InvalidRequestException, InternalApplicationException{
        return accountService.requestFriendship(request); 
    }
    
    
    /**
     * Find friends using a keyword
     * @return 
     * @throws AuthenticationException 
     * @throws InternalApplicationException 
     */
    @GET
    @Path("findFriends")
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response findFriends(@HeaderParam("Authorization") String authorization) throws InternalApplicationException, AuthenticationException{
        return accountService.findFriends(authorization); 
    }
    
    /**
     * Update a registration id from Google Cloud Messaging
     * @return 
     */
    @Path("updateGcmRegId")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Secured
    public Response updateGcmRegId(@QueryParam("regId")String regId, @HeaderParam("Authorization")String auth)throws InvalidRequestException, InternalApplicationException{
        return accountService.updateRegId(regId, auth); 
    }
    
    @Path("responseFriendship")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response responseFriendship(FriendshipRequestResponse response)throws InvalidRequestException, InternalApplicationException{
    	return accountService.friendshipResponse(response); 
    }
    
     
}
