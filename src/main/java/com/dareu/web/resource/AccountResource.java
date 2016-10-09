package com.dareu.web.resource;

import com.dareu.web.core.annotation.Secured;
import com.dareu.web.core.service.AccountService;
import com.dareu.web.core.service.MultipartService;
import com.dareu.web.data.request.FriendshipRequest;
import com.dareu.web.data.request.FriendshipRequestResponse;
import com.dareu.web.data.request.SignupRequest;
import com.dareu.web.exception.InternalApplicationException;
import com.dareu.web.exception.InvalidRequestException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.PathParam;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path("account/")
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
    @Secured
    @GET
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
    @Secured
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response requestFriendship(FriendshipRequest request) throws InvalidRequestException, InternalApplicationException{
        return accountService.requestFriendship(request); 
    }
    
    
    /**
     * Find friends using a keyword
     * @return 
     */
    @Path("findFriends")
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    @GET
    public Response findFriends(@HeaderParam("authorization")String authorization){
        return null; 
    }
    
    /**
     * Update a registration id from Google Cloud Messaging
     * @return 
     */
    @Path("updateGcmRegId")
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    @POST
    public Response updateGcmRegId(){
        return null; 
    }
    
    /**
     * Register a new sponsor user
     * @return 
     */
    @Path("registerSponsor")
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerSponsorUser(){
        return null; 
    }
    
    
    /**
     * Login using a Facebook account
     * @return 
     */
    @Path("loginFacebook")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response loginFacebook(){
        return null; 
    }
    
    
    /**
     * Register using a Facebook account
     * @return 
     */
    @Path("registerFacebook")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerFacebook(){
        return null; 
    }
    
    /**
     * check if a nickname is available
     * @return 
     */
    @Path("nicknameAvailable/{nickname}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response nicknameAvailable(@PathParam("nickname")String nickname)throws InternalApplicationException{
        return accountService.isNicknameAvailable(nickname); 
    }
    
    /**
     * check if a nickname is available
     * @return 
     */
    @Path("emailAvailable/{email}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response emailAvailable(@PathParam("email")String email)throws InternalApplicationException{
        return accountService.isEmailAvailable(email);
    }    
    
    /**
     * Register a new user
     * @return 
     */
    @Path("registerUser")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public Response registerUser(MultipartFormDataInput input)throws Exception{
    	SignupRequest request = multipartService.getSignupRequest(input); 
        return  accountService.registerDareUser(request); 
    }
    
    @Path("responseFriendship")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response responseFriendship(FriendshipRequestResponse response)throws InvalidRequestException, InternalApplicationException{
    	return accountService.friendshipResponse(response); 
    }
    
     
}
