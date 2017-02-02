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
import com.dareu.web.exception.InternalApplicationException;
import com.dareu.web.exception.InvalidRequestException;

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
     */
    @Path("me")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @ApiOperation(value = "Retrieve a user profile",
            httpMethod = "GET",
            nickname = "me")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The request has been processed successfully"),
        @ApiResponse(code = 401, message = "Unauthorized to process request")})
    @Secured
    public Response me() {
        return null;
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
     * @throws com.dareu.web.exception.InternalApplicationException 
     */
    @GET
    @Path("friends/find")
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response friends(@QueryParam("pageNumber") int pageNumber, @QueryParam("q")String query)throws InternalApplicationException{
        return accountService.findFriends(pageNumber, query);
    }

    /**
     * Update a registration id from Google Cloud Messaging
     *
     * @param regId
     * @param auth
     * @return
     */
    @Path("updateGcmRegId")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Secured
    public Response updateGcmRegId(@QueryParam("regId") String regId, @HeaderParam("Authorization") String auth) throws InvalidRequestException, InternalApplicationException {
        return accountService.updateRegId(regId, auth);
    }

    @Path("friendship/{userId}/update")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response responseFriendship(@PathParam(value = "userId")String userId, @QueryParam(value = "accepted")Boolean accepted) throws InvalidRequestException, InternalApplicationException {
        return accountService.friendshipResponse(userId, accepted);
    }
    
    @Path("friendship/find")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response findFriendshipDetails(@QueryParam("friendshipId")String friendshipId, @HeaderParam("Authorization")String auth) throws InternalApplicationException, InvalidRequestException{
        return accountService.findFriendshipDetails(friendshipId, auth); 
    } 
    
    @Path("me/profile")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @POST
    @Secured
    public Response updateImage(MultipartFormDataInput input, @HeaderParam("Authorization")String auth)throws InternalApplicationException{
        return accountService.updateProfileImage(input, auth);  
    }
    
    /**@Path("me/profile")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Secured
    public Response getImage(@HeaderParam("Authorization")String auth)throws InternalApplicationException, InvalidRequestException{
        return accountService.getAccountImage(auth);
    }**/
    

    @Path("me/profile")
    @Produces("image/jpeg")
    @GET
    @Secured
    public Response getImage(@HeaderParam("Authorization")String auth) throws InvalidRequestException, InternalApplicationException{
        return accountService.getAccountImage(auth); 
    }
    
    @Path("discoverUsers")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response discoverUsers(@QueryParam("pageNumber")int pageNumber)throws InternalApplicationException{
        return accountService.discoverUsers(pageNumber); 
    }
}
