package com.dareu.web.resource;

import com.dareu.web.core.annotation.Secured;
import com.dareu.web.core.service.AccountService;
import com.dareu.web.core.service.MultipartService;
import com.dareu.web.data.request.SignupRequest;
import com.dareu.web.exception.InvalidRequestException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path("rest/account/")
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
     */
    @Path("requestFriendship")
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response requestFriendship(){
        return null; 
    }
    
    
    /**
     * Find friends using a keyword
     * @return 
     */
    @Path("findFriends")
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    @GET
    public Response findFriends(){
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
    @Path("nicknameAvailable")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response nicknameAvailable(){
        return null; 
    }
    
    /**
     * check if a nickname is available
     * @return 
     */
    @Path("emailAvailable")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response emailAvailable(){
        return null; 
    }    
    
    /**
     * Register a new user
     * @return 
     */
    @Path("registerUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @POST
    public Response registerUser(MultipartFormDataInput input){
        return null; 
    }
    
    /**
     * Signin using a google account
     * @return 
     */
    @Path("signin")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signin(){
        return null; 
    }
    
     
}
